package chesscoach.stockfish;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StockfishOption {
    private String name;
    private String type;
    private String defaultValue;
    private int minValue;
    private int maxValue;
    private List<String> vars = new ArrayList<>();

    public StockfishOption(){

    }

    public StockfishOption(String line){
        parseOptionLine(line);
    }

    /**
     * option name Debug Log File type string default
option name Contempt type spin default 24 min -100 max 100
option name Analysis Contempt type combo default Both var Off var White var Black var Both
option name Threads type spin default 1 min 1 max 512
option name Hash type spin default 16 min 1 max 33554432
option name Clear Hash type button
option name Ponder type check default false
option name MultiPV type spin default 1 min 1 max 500
option name Skill Level type spin default 20 min 0 max 20
option name Move Overhead type spin default 10 min 0 max 5000
option name Slow Mover type spin default 100 min 10 max 1000
option name nodestime type spin default 0 min 0 max 10000
option name UCI_Chess960 type check default false
option name UCI_AnalyseMode type check default false
option name UCI_LimitStrength type check default false
option name UCI_Elo type spin default 1350 min 1350 max 2850
option name UCI_ShowWDL type check default false
option name SyzygyPath type string default <empty>
option name SyzygyProbeDepth type spin default 1 min 1 max 100
option name Syzygy50MoveRule type check default true
option name SyzygyProbeLimit type spin default 7 min 0 max 7
option name Use NNUE type check default true
option name EvalFile type string default nn-62ef826d1a6d.nnue
     * @param line
     */
    public void parseOptionLine(String line){
        String[] parts = line.split(" ");
        if (parts.length < 6)
            return;
        if (!parts[0].equals("option"))
            return;
        String category = null;
        List<String> args = new ArrayList<>();
        for (int i = 1; i < parts.length; ++i){
            if (parts[i].equalsIgnoreCase("name") ||
                    parts[i].equalsIgnoreCase("type") ||
                    parts[i].equalsIgnoreCase("default") ||
                    parts[i].equalsIgnoreCase("min") ||
                    parts[i].equalsIgnoreCase("max") ||
                    parts[i].equalsIgnoreCase("var")){
                if (category != null){
                    if (category.equalsIgnoreCase("name")){
                        this.name = args.stream().collect(Collectors.joining(" "));
                    }
                    else if (category.equalsIgnoreCase("type")) {
                        this.type = args.get(0);
                    }
                    else if (category.equalsIgnoreCase("default")){
                        if (args.isEmpty())
                            this.defaultValue = "";
                        else
                            this.defaultValue = args.get(0);
                    }
                    else if (category.equalsIgnoreCase("min")){
                        minValue = Integer.decode(args.get(0));
                    }
                    else if (category.equalsIgnoreCase("max")){
                        maxValue = Integer.decode(args.get(0));
                    }
                    else if (category.equalsIgnoreCase("var")){
                        vars.addAll(args);
                    }
                }
                args.clear();
                category = parts[i];
            }
            else {
                args.add(parts[i]);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public List<String> getVars() {
        return vars;
    }
}
