package chesscoach.game;

import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;

public class MoveAnalysis {
    private Piece piece;
    private Space fromSpace;
    private Space toSpace;

    private boolean valid;  // is the move valid?
    private List<String> invalidReasons = new ArrayList<>();

    public MoveAnalysis(Piece piece, Space fromSpace, Space toSpace){
        this.piece = piece;
        this.fromSpace = fromSpace;
        this.toSpace = toSpace;
    }

    public Piece getPiece() {
        return piece;
    }

    public Space getFromSpace() {
        return fromSpace;
    }

    public Space getToSpace() {
        return toSpace;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getInvalidReasons() {
        return invalidReasons;
    }
}
