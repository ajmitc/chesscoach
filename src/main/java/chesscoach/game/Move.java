package chesscoach.game;

public class Move {
    private String move;
    private String ponder;

    public Move(String move, String ponder){
        this.move = move;
        this.ponder = ponder;
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
}
