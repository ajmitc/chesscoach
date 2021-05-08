package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.game.Side;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;

public class RookRules extends Rules{

    @Override
    public List<Space> getValidMoves(Piece piece, Space fromSpace, List<Piece> pieces) {
        List<Space> validDest = new ArrayList<>();
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank, 0), pieces));
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank, 7), pieces));
        validDest.addAll(getPath(fromSpace, new Space(1, fromSpace.getFile()), pieces));
        validDest.addAll(getPath(fromSpace, new Space(8, fromSpace.getFile()), pieces));
        return validDest;
    }
}
