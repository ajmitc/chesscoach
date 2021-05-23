package chesscoach.util;

import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class Util {
    private static Logger logger = Logger.getLogger(Util.class.getName());

    private static Random GEN = new Random(new Date().getTime());

    public static Space parseCoord(String coord){
        int rank = 1;
        int file = 0;

        // e2e4 - take last two characters only
        if (coord.length() == 4){
            coord = coord.substring(2);
        }

        // e2
        if (coord.length() == 2){
            file = coord.charAt(0) - 'a';
            rank = Integer.decode(coord.substring(1, 2));
            return new Space(rank - 1, file);
        }

        throw new RuntimeException("Unable to parse coord '" + coord + "'.  Length must be 2 or 4.");
    }

    public static String formatCoord(int rank, int file){
        StringBuilder sb = new StringBuilder();
        sb.append((char) ('a' + file));
        sb.append(rank + 1);
        return sb.toString();
    }

    /**
     * Parse a string like "g8f6" to [Space(g8), Space(f6)]
     * @param moveString
     * @return List with two Space objects - from coord, to coord
     */
    public static Space[] parseMove(String moveString){
        if (moveString.length() != 4){
            logger.severe("Unable to parse move string '" + moveString + "', expected string of length 4");
            return null;
        }
        Space fromCoord = parseCoord(moveString.substring(0, 2));
        Space toCoord   = parseCoord(moveString);
        return new Space[]{fromCoord, toCoord};
    }

    public static int getRandInt(int min, int max){
        return min + GEN.nextInt(max - min);
    }

    private Util(){}
}
