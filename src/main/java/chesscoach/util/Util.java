package chesscoach.util;

import java.util.Date;
import java.util.Random;

public class Util {
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
            return new Space(rank, file);
        }

        throw new RuntimeException("Unable to parse coord '" + coord + "'.  Length must be 2 or 4.");
    }

    public static String formatCoord(int rank, int file){
        StringBuilder sb = new StringBuilder();
        sb.append((char) ('a' + file));
        sb.append(rank + 1);
        return sb.toString();
    }

    public static int getRandInt(int min, int max){
        return min + GEN.nextInt(max - min);
    }

    private Util(){}
}
