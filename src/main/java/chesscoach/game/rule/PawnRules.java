package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.game.Side;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;

public class PawnRules extends Rules{

    @Override
    public List<Space> getValidMoves(Piece piece, Space fromSpace, List<Piece> pieces) {
        List<Space> validDest = new ArrayList<>();

        int dir = piece.getSide() == Side.LIGHT? 1: -1;
        // Check if on starting space
        if (piece.getStartingFile() == piece.getFile() && piece.getStartingRank() == piece.getRank()){
            validDest.addAll(getPath(fromSpace, fromSpace.panRank(dir * 2), pieces));
        }
        else {
            validDest.addAll(getPath(fromSpace, fromSpace.panRank(dir), pieces));
        }

        // Check if can capture diagonal
        Space diagLeft = fromSpace.pan(dir, -1);
        Space diagRight = fromSpace.pan(dir, 1);
        Piece diagLeftPiece = getPieceAt(diagLeft.rank, diagLeft.file, pieces);
        Piece diagRightPiece = getPieceAt(diagRight.rank, diagRight.file, pieces);

        if (diagLeftPiece != null && diagLeftPiece.getSide() != piece.getSide())
            validDest.add(diagLeft);
        if (diagRightPiece != null && diagRightPiece.getSide() != piece.getSide())
            validDest.add(diagRight);

        return validDest;
    }
}
