package chesscoach.game;

import chesscoach.util.Util;

public class Piece {
    private PieceType type;
    private Side side;
    private int rank;   // 0-7
    private int file;   // a-h
    private int startingRank;
    private int startingFile;
    private boolean captured = false;
    private boolean moved = false;

    public Piece(PieceType type, Side side){
        this.type = type;
        this.side = side;
    }

    public Piece(PieceType type, Side side, int rank, int file){
        this.type = type;
        this.side = side;
        this.rank = rank;
        this.file = file;
        this.startingRank = rank;
        this.startingFile = file;
    }

    public void moveTo(int rank, int file){
        setRank(rank);
        setFile(file);
        moved = true;
    }

    public PieceType getType() {
        return type;
    }

    public Side getSide() {
        return side;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    public int getStartingFile() {
        return startingFile;
    }

    public int getStartingRank() {
        return startingRank;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public String toString(){
        return type.getCode() + " (" + side + "; " + Util.formatCoord(rank, file) + ")";
    }
}
