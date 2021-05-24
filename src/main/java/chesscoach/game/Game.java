package chesscoach.game;

import chesscoach.game.rule.RuleSet;
import chesscoach.stockfish.Stockfish;
import chesscoach.util.Space;
import chesscoach.util.Util;

import java.util.*;
import java.util.logging.Logger;

public class Game {
    private static Logger logger = Logger.getLogger(Game.class.getName());

    private List<Piece> pieces = new ArrayList<>();

    private Map<Side, Integer> score = new HashMap<>();

    private Side playerSide = Side.values()[Util.getRandInt(0, 2)];

    private Stockfish stockfish;

    // Best move as returned by Stockfish
    private String bestMove;
    private int bestMoveScore;

    public Game(){
        pieces.add(new Piece(PieceType.ROOK, Side.LIGHT, 0, 0));
        pieces.add(new Piece(PieceType.KNIGHT, Side.LIGHT, 0, 1));
        pieces.add(new Piece(PieceType.BISHOP, Side.LIGHT, 0, 2));
        pieces.add(new Piece(PieceType.QUEEN, Side.LIGHT, 0, 3));
        pieces.add(new Piece(PieceType.KING, Side.LIGHT, 0, 4));
        pieces.add(new Piece(PieceType.BISHOP, Side.LIGHT, 0, 5));
        pieces.add(new Piece(PieceType.KNIGHT, Side.LIGHT, 0, 6));
        pieces.add(new Piece(PieceType.ROOK, Side.LIGHT, 0, 7));
        for (int i = 0; i < 8; ++i)
            pieces.add(new Piece(PieceType.PAWN, Side.LIGHT, 1, i));

        pieces.add(new Piece(PieceType.ROOK, Side.DARK, 7, 0));
        pieces.add(new Piece(PieceType.KNIGHT, Side.DARK, 7, 1));
        pieces.add(new Piece(PieceType.BISHOP, Side.DARK, 7, 2));
        pieces.add(new Piece(PieceType.QUEEN, Side.DARK, 7, 3));
        pieces.add(new Piece(PieceType.KING, Side.DARK, 7, 4));
        pieces.add(new Piece(PieceType.BISHOP, Side.DARK, 7, 5));
        pieces.add(new Piece(PieceType.KNIGHT, Side.DARK, 7, 6));
        pieces.add(new Piece(PieceType.ROOK, Side.DARK, 7, 7));
        for (int i = 0; i < 8; ++i)
            pieces.add(new Piece(PieceType.PAWN, Side.DARK, 6, i));

        score.put(Side.LIGHT, 0);
        score.put(Side.DARK, 0);

        stockfish = new Stockfish();
        stockfish.start();
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public Piece findPieceAt(int rank, int file){
        Optional<Piece> opt = pieces.stream().filter(piece -> piece.getRank() == rank && piece.getFile() == file && !piece.isCaptured()).findFirst();
        return opt.isPresent()? opt.get(): null;
    }

    public Piece findPieceAt(Space space){
        return findPieceAt(space.rank, space.file);
    }

    public MoveAnalysis analyzeMove(Piece piece, Space fromSpace, Space toSpace){
        MoveAnalysis moveAnalysis = new MoveAnalysis(piece, fromSpace, toSpace);
        List<Space> validMoves = RuleSet.getRules(piece.getType()).getValidMoves(piece, fromSpace, pieces);
        moveAnalysis.setValid(validMoves.contains(toSpace));
        if (!moveAnalysis.isValid()){
            moveAnalysis.getInvalidReasons().add("Invalid movement");
        }
        return moveAnalysis;
    }

    public Integer getScore(Side side) {
        return score.get(side);
    }

    public void updateScoreWithCapture(Side winningSide, int powerLost){
        score.put(winningSide, score.get(winningSide) + powerLost);
        int lightScore = score.get(Side.LIGHT);
        int darkScore = score.get(Side.DARK);
        if (lightScore > 0 && darkScore > 0) {
            int min = Math.min(lightScore, darkScore);
            lightScore -= min;
            darkScore -= min;
            score.put(Side.LIGHT, lightScore);
            score.put(Side.DARK, darkScore);
        }
        logger.info("Score: LIGHT(" + score.get(Side.LIGHT) + ") DARK(" + score.get(Side.DARK) + ")");
    }

    public void setRandomSide(){
        playerSide = Side.values()[Util.getRandInt(0, 2)];
    }

    public Side getPlayerSide() {
        return playerSide;
    }

    public void setPlayerSide(Side playerSide) {
        this.playerSide = playerSide;
    }

    public Stockfish getStockfish() {
        return stockfish;
    }

    public void setBestMove(String bestMove) {
        this.bestMove = bestMove;
    }

    public String getBestMove() {
        return bestMove;
    }

    public int getBestMoveScore() {
        return bestMoveScore;
    }

    public void setBestMoveScore(int bestMoveScore) {
        this.bestMoveScore = bestMoveScore;
    }
}
