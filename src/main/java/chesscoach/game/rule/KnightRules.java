package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;

public class KnightRules extends Rules{

    @Override
    public List<Space> getValidMoves(Piece piece, Space fromSpace, List<Piece> pieces) {
        List<Space> validDest = new ArrayList<>();
        // ------
        //      |
        validDest.add(fromSpace.pan(1, -2));
        // ------
        // |
        validDest.add(fromSpace.pan(1, 2));
        //   |
        //   |
        // ---
        validDest.add(fromSpace.pan(2, 1));
        // |
        // |
        // ---
        validDest.add(fromSpace.pan(2, -1));
        //      |
        // ------
        validDest.add(fromSpace.pan(-1, -2));
        // |
        // ------
        validDest.add(fromSpace.pan(-1, 2));
        // ---
        //   |
        //   |
        validDest.add(fromSpace.pan(-2, 1));
        // ---
        // |
        // |
        validDest.add(fromSpace.pan(-2, -1));
        return validDest;
    }
}
