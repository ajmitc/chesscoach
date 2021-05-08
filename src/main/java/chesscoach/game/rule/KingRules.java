package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.game.Side;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;

public class KingRules extends Rules{

    @Override
    public List<Space> getValidMoves(Piece piece, Space fromSpace, List<Piece> pieces) {
        List<Space> validDest = new ArrayList<>();

        // up
        validDest.addAll(getPath(fromSpace, fromSpace.pan(1, 0), pieces));
        // down
        validDest.addAll(getPath(fromSpace, fromSpace.pan(-1, 0), pieces));
        // left
        validDest.addAll(getPath(fromSpace, fromSpace.pan(0, -1), pieces));
        // right
        validDest.addAll(getPath(fromSpace, fromSpace.pan(0, 1), pieces));

        // up-left
        validDest.addAll(getPath(fromSpace, fromSpace.pan(1, -1), pieces));
        // down-left
        validDest.addAll(getPath(fromSpace, fromSpace.pan(-1, -1), pieces));
        // up-right
        validDest.addAll(getPath(fromSpace, fromSpace.pan(1, 1), pieces));
        // down-right
        validDest.addAll(getPath(fromSpace, fromSpace.pan(-1, 1), pieces));


        // TODO Cannot put self in danger!

        return validDest;
    }
}
