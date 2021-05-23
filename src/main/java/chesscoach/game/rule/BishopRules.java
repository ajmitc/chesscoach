package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;

public class BishopRules extends Rules{

    @Override
    public List<Space> getValidMoves(Piece piece, Space fromSpace, List<Piece> pieces) {
        List<Space> validDest = new ArrayList<>();
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank + 7, fromSpace.file + 7), pieces));
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank - 7, fromSpace.file - 7), pieces));
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank + 7, fromSpace.file - 7), pieces));
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank - 7, fromSpace.file + 7), pieces));
        return validDest;
    }

    private int ceiling(int val){
        return Math.min(val, 7);
    }
    private int floor(int val){
        return Math.max(val, 0);
    }
}
