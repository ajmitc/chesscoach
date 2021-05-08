package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;

public class BishopRules extends Rules{

    @Override
    public List<Space> getValidMoves(Piece piece, Space fromSpace, List<Piece> pieces) {
        List<Space> validDest = new ArrayList<>();
        // upper-left diagonal
        int min = Math.min(fromSpace.rank, fromSpace.file);
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank + min, fromSpace.file - min), pieces));
        // upper-right diagonal
        min = Math.min(fromSpace.rank, 8 - fromSpace.file);
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank + min, fromSpace.file + min), pieces));
        // lower-left diagonal
        min = Math.min(8 - fromSpace.rank, fromSpace.file);
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank - min, fromSpace.file - min), pieces));
        // lower-right diagonal
        min = Math.min(8 - fromSpace.rank, 8 - fromSpace.file);
        validDest.addAll(getPath(fromSpace, new Space(fromSpace.rank - min, fromSpace.file + min), pieces));
        return validDest;
    }
}
