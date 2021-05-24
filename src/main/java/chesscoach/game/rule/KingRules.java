package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.game.PieceType;
import chesscoach.game.Side;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        // Allow castling
        if (!piece.isMoved()){
            int modifier = piece.getSide() == Side.LIGHT? 1: -1;

            // King-side Castling
            // Find king-side rook
            Piece rookSpacePiece = getPieceAt(fromSpace.panFile(modifier * 3), pieces);
            if (rookSpacePiece != null && rookSpacePiece.getType() == PieceType.ROOK && rookSpacePiece.getSide() == piece.getSide() && !rookSpacePiece.isMoved()) {
                // Check that Bishop and Knight spaces are empty
                Space bishopSpace = fromSpace.panFile(modifier * 1);
                Space knightSpace = fromSpace.panFile(modifier * 2);
                Piece bishopSpacePiece = getPieceAt(bishopSpace, pieces);
                Piece knightSpacePiece = getPieceAt(knightSpace, pieces);
                if (bishopSpacePiece == null && knightSpacePiece == null) {
                    validDest.add(knightSpace);
                }
            }

            // Queen-side Castling
            // Find queen-side rook
            rookSpacePiece = getPieceAt(fromSpace.panFile(modifier * -4), pieces);
            if (rookSpacePiece != null && rookSpacePiece.getType() == PieceType.ROOK && rookSpacePiece.getSide() == piece.getSide() && !rookSpacePiece.isMoved()) {
                // Check that Bishop and Knight spaces are empty
                Space queenSpace  = fromSpace.panFile(modifier * -1);
                Space bishopSpace = fromSpace.panFile(modifier * -2);
                Space knightSpace = fromSpace.panFile(modifier * -3);
                Piece queenSpacePiece = getPieceAt(queenSpace, pieces);
                Piece bishopSpacePiece = getPieceAt(bishopSpace, pieces);
                Piece knightSpacePiece = getPieceAt(knightSpace, pieces);
                if (queenSpacePiece == null && bishopSpacePiece == null && knightSpacePiece == null) {
                    validDest.add(bishopSpace);
                }
            }
        }

        // TODO Cannot put self in danger!

        return validDest;
    }
}
