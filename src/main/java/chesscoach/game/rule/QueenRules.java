package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.game.PieceType;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;

public class QueenRules extends Rules{

    @Override
    public List<Space> getValidMoves(Piece piece, Space fromSpace, List<Piece> pieces) {
        List<Space> validDest = new ArrayList<>();
        validDest.addAll(RuleSet.getRules(PieceType.ROOK).getValidMoves(piece, fromSpace, pieces));
        validDest.addAll(RuleSet.getRules(PieceType.BISHOP).getValidMoves(piece, fromSpace, pieces));
        return validDest;
    }
}
