package chesscoach.game.rule;

import chesscoach.game.PieceType;

import java.util.HashMap;
import java.util.Map;

public class RuleSet {
    private static Map<PieceType, Rules> rules = new HashMap<>();

    static {
        rules.put(PieceType.PAWN,   new PawnRules());
        rules.put(PieceType.ROOK,   new RookRules());
        rules.put(PieceType.BISHOP, new BishopRules());
        rules.put(PieceType.KNIGHT, new KnightRules());
        rules.put(PieceType.QUEEN,  new QueenRules());
        rules.put(PieceType.KING,   new KingRules());
    }

    public static Rules getRules(PieceType pieceType){
        return rules.get(pieceType);
    }

    private RuleSet(){}
}
