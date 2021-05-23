package chesscoach.stockfish;

public class StockfishInfo {
    private int scoreCentipawns = 0;
    private Integer mateInMoves = null;  // negative if engine will be mated
    private boolean scoreLowerbound = false;
    private boolean scoreUpperbound = false;
    private String bestLine = "";

    public StockfishInfo(String infoString){
        parse(infoString);
    }

    /**
     * info depth 8 seldepth 9 multipv 1 score cp 58 upperbound nodes 1065 nps 355000 tbhits 0 time 3 pv c2c4 e7e5
     * @param infoString
     */
    public void parse(String infoString){
        String[] parts = infoString.split(" ");
        for (int i = 0; i < parts.length; ++i){
            String token = parts[i];
            if (token.equals("cp")){
                scoreCentipawns = Integer.decode(parts[++i]);
            }
            else if (token.equals("upperbound")){
                scoreUpperbound = true;
            }
            else if (token.equals("lowerbound")){
                scoreLowerbound = true;
            }
            else if (token.equals("mate")){
                mateInMoves = Integer.decode(parts[++i]);
            }
            else if (token.equals("pv")){
                StringBuilder sb = new StringBuilder();
                for (int j = i + 1; j < parts.length; ++j){
                    if (j < parts.length - 1)
                        sb.append(" ");
                    sb.append(parts[j]);
                }
                bestLine = sb.toString();
                i = parts.length;
            }
        }
    }

    public int getScoreCentipawns() {
        return scoreCentipawns;
    }

    public Integer getMateInMoves() {
        return mateInMoves;
    }

    public boolean isScoreLowerbound() {
        return scoreLowerbound;
    }

    public boolean isScoreUpperbound() {
        return scoreUpperbound;
    }

    public String getBestLine() {
        return bestLine;
    }
}
