package chesscoach.game;

public enum PieceType {
    PAWN("P", 1),
    ROOK("R", 5),
    KNIGHT("N", 3),
    BISHOP("B", 3),
    QUEEN("Q", 9),
    KING("K", 0);

    private String code;
    private int power;
    PieceType(String code, int power){
        this.code = code;
        this.power = power;
    }

    public String getCode() {
        return code;
    }

    public int getPower() {
        return power;
    }

    @Override
    public String toString() {
        return code;
    }
}
