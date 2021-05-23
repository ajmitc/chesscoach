package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.game.PieceType;
import chesscoach.game.Side;
import chesscoach.util.Space;
import chesscoach.util.Util;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BishopRulesTest {
    private static Logger logger = Logger.getLogger(BishopRulesTest.class.getName());

    @Test
    public void testBishopRulesG1(){
        BishopRules rules = new BishopRules();

        // g1
        Space startingSpace = Util.parseCoord("f1");
        Piece bishop = new Piece(PieceType.BISHOP, Side.LIGHT, startingSpace.rank, startingSpace.file);
        List<Piece> board = new ArrayList<>();
        board.add(bishop);

        List<Space> validSpaces = rules.getValidMoves(bishop, startingSpace, board);

        List<Space> expectedValidSpaces = new ArrayList<>();
        expectedValidSpaces.add(Util.parseCoord("g2"));
        expectedValidSpaces.add(Util.parseCoord("h3"));
        expectedValidSpaces.add(Util.parseCoord("e2"));
        expectedValidSpaces.add(Util.parseCoord("d3"));
        expectedValidSpaces.add(Util.parseCoord("c4"));
        expectedValidSpaces.add(Util.parseCoord("b5"));
        expectedValidSpaces.add(Util.parseCoord("a6"));

        for (Space expectedValidSpace: expectedValidSpaces){
            if (validSpaces.contains(expectedValidSpace)){
                validSpaces.remove(expectedValidSpace);
            }
            else {
                logger.severe("Unable to find expected valid space '" + expectedValidSpace + "', starting space '" + startingSpace + "'");
                assert false;
            }
        }
        // Make sure the rules didn't return any extra valid moves
        assert validSpaces.isEmpty();
    }
}
