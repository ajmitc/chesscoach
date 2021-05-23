package chesscoach;

import chesscoach.game.Move;
import chesscoach.game.MoveAnalysis;
import chesscoach.game.Piece;
import chesscoach.game.Side;
import chesscoach.stockfish.StockfishInfo;
import chesscoach.stockfish.StockfishListener;
import chesscoach.util.Space;
import chesscoach.util.Util;
import chesscoach.view.View;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

public class Controller implements StockfishListener {
    private static Logger logger = Logger.getLogger(Controller.class.getName());

    private Model model;
    private View view;

    private Space selectedSpace;
    private Piece selectedPiece;
    private boolean playersTurn;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;

        playersTurn = model.getGame().getPlayerSide() == Side.LIGHT;

        view.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    model.getGame().getStockfish().stop();
                }
                catch (Exception ex){}
            }
        });

        view.getBoardPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                selectedSpace = view.getBoardPanel().getSpaceAt(e.getX(), e.getY());
                if (selectedSpace != null)
                    selectedPiece = model.getGame().findPieceAt(selectedSpace);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (selectedPiece != null){
                    Space destSpace = view.getBoardPanel().getSpaceAt(e.getX(), e.getY());
                    move(selectedPiece, selectedSpace, destSpace, true);
                }
            }
        });

        view.getBoardPanel().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                view.getBoardPanel().setHover(e.getX(), e.getY());
                view.refresh();
            }
        });

        model.getGame().getStockfish().addListener(this);

        if (!playersTurn) {
            model.getGame().getStockfish().sendMoves();
            model.getGame().getStockfish().go(3000L);
        }
    }

    private void move(Piece piece, Space fromSpace, Space toSpace, boolean tellCompGo){
        MoveAnalysis analysis = model.getGame().analyzeMove(piece, fromSpace, toSpace);
        if (!analysis.isValid()){
            for (String reason: analysis.getInvalidReasons()) {
                logger.warning("Move Invalid: " + reason);
            }
            return;
        }
        Piece capturedPiece = model.getGame().findPieceAt(toSpace);
        if (capturedPiece != null) {
            capturedPiece.setCaptured(true);
            model.getGame().updateScoreWithCapture(piece.getSide(), capturedPiece.getType().getPower());
        }
        piece.moveTo(toSpace.rank, toSpace.file);
        view.refresh();

        StringBuilder sb = new StringBuilder(fromSpace.toString());
        sb.append(toSpace.toString());
        model.getGame().getStockfish().addMove(sb.toString());

        playersTurn = !playersTurn;

        model.getGame().getStockfish().sendMoves();
        model.getGame().getStockfish().go(3000L);
    }

    /**
     * Stockfish Listener method
     * @param move
     */
    @Override
    public void moveReceived(Move move) {
        String moveString = move.getMove();
        logger.info("Received move from Stockfish: " + moveString);
        Space[] moveSpaces = Util.parseMove(moveString);
        Space fromSpace = moveSpaces[0];
        Space toSpace = moveSpaces[1];
        Piece piece = model.getGame().findPieceAt(fromSpace);
        if (!playersTurn)
            move(piece, fromSpace, toSpace, false);
        else {
            model.getGame().setBestMove(move.getMove());
            model.getGame().setBestMoveScore(move.getScoreCp());
        }

        if (move.getPonder() != null){
            //model.getGame().getStockfish().ponder(move.getPonder());
        }
    }

    @Override
    public void infoReceived(StockfishInfo info) {

    }

    @Override
    public void stockfishIsReady() {

    }
}
