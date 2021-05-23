package chesscoach.game;

public class Move {
    private String move;
    private String ponder;
    private Integer scoreCp;
    private Integer mateInMoves;

    public Move(String move, String ponder){
        this.move = move;
        this.ponder = ponder;
        this.scoreCp = null;
        this.mateInMoves = null;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getPonder() {
        return ponder;
    }

    public void setPonder(String ponder) {
        this.ponder = ponder;
    }

    public Integer getScoreCp() {
        return scoreCp;
    }

    public void setScoreCp(Integer scoreCp) {
        this.scoreCp = scoreCp;
    }

    public Integer getMateInMoves() {
        return mateInMoves;
    }

    public void setMateInMoves(Integer mateInMoves) {
        this.mateInMoves = mateInMoves;
    }
}
