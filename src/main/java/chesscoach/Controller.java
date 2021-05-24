package chesscoach;

import chesscoach.game.*;
import chesscoach.stockfish.StockfishInfo;
import chesscoach.stockfish.StockfishListener;
import chesscoach.util.Space;
import chesscoach.util.Util;
import chesscoach.view.View;

import java.awt.event.*;
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
                catch (Exception ex){
                    logger.warning("Failed to stop stockfish: " + ex);
                }
            }
        });

        view.getBoardPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                selectedSpace = view.getBoardPanel().getSpaceAt(e.getX(), e.getY());
                if (selectedSpace != null) {
                    selectedPiece = model.getGame().findPieceAt(selectedSpace);
                    if (selectedPiece != null){
                        // TODO Show possible movements
                    }
                }
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

        view.getHelpPanel().getBtnShowHelp().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getHelpPanel().showHelp();
            }
        });

        model.getGame().getStockfish().addListener(this);

        if (!playersTurn) {
            model.getGame().getStockfish().sendMoves();
            model.getGame().getStockfish().go(3000L);
        }
    }

    private void move(Piece piece, Space fromSpace, Space toSpace, boolean tellCompGo){
        view.getHelpPanel().clearHelp();

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
            capturedPiece.setRank(-1);
            capturedPiece.setFile(-1);
            model.getGame().updateScoreWithCapture(piece.getSide(), capturedPiece.getType().getPower());
        }
        piece.moveTo(toSpace.rank, toSpace.file);
        view.getBoardPanel().setPreviousMove(fromSpace, toSpace);

        // Check for castling
        if (piece.getType() == PieceType.KING) {
            int diff = toSpace.getFile() - fromSpace.getFile();
            if (Math.abs(diff) == 2){
                // Move rook next to king
                if (piece.getSide() == Side.LIGHT) {
                    if (diff > 0) {
                        Piece rook = model.getGame().findPieceAt(0, 7);
                        rook.moveTo(0, 5);
                    } else {
                        // Move queen-side
                        Piece rook = model.getGame().findPieceAt(0, 0);
                        rook.moveTo(0, 2);
                    }
                }
                else {
                    if (diff > 0) {
                        // Move queen side
                        Piece rook = model.getGame().findPieceAt(7, 7);
                        rook.moveTo(7, 5);
                    } else {
                        // Move king -side
                        Piece rook = model.getGame().findPieceAt(7, 0);
                        rook.moveTo(7, 2);
                    }
                }
            }
        }
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
