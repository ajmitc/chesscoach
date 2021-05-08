package chesscoach.stockfish;

import chesscoach.game.Move;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Stockfish {
    private static final String STOCKFISH_EXECUTABLE = "./stockfish_13_linux_x64_bmi2";

    private static Logger logger = Logger.getLogger(Stockfish.class.getName());

    private Process process;
    private BufferedReader inputReader;
    private BufferedReader errorReader;
    private OutputStreamWriter outputStream;

    private Set<StockfishListener> listeners = new HashSet<>();

    private List<StockfishOption> options = new ArrayList<>();
    private List<String> moves = new ArrayList<>();

    private ReaderThread readerThread;

    public Stockfish(){

    }

    public void start(){
        try {
            process = new ProcessBuilder()
                    .directory(new File("stockfish"))
                    .command(STOCKFISH_EXECUTABLE)
                    .start();
            inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            outputStream = new OutputStreamWriter(process.getOutputStream());

            readerThread = new ReaderThread();
            readerThread.start();

            try{Thread.sleep(1000);}catch(Exception e){}
            send("uci");
        }
        catch (IOException ioException){
            logger.severe("Unable to start stockfish process: " + ioException);
        }
    }

    public void stop(){
        if (process != null && process.isAlive()) {
            send("quit");

            readerThread.setShouldStop(true);
            readerThread.interrupt();

            try { outputStream.close(); }catch (IOException ioe){}
            try { inputReader.close(); }catch (IOException ioe){}
            try { errorReader.close(); }catch (IOException ioe){}
            process.destroy();
            if (process.isAlive())
                process.destroyForcibly();
        }
    }

    private void handleUciOk(){
        // Set Hash to 32MB
        send("setoption name Hash value 32");
        newGame();
    }

    /**
     * id name Stockfish 13
     id author the Stockfish developers (see AUTHORS file)

     option name Debug Log File type string default
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
     uciok
     */
    private void handleInput(String line){
        //logger.info("Received from Stockfish: '" + line + "'");

        if (line.startsWith("id name ")){
            logger.info("Using " + line.substring(8));
        }
        else if (line.startsWith("option ")){
            options.add(new StockfishOption(line));
        }
        else if (line.startsWith("uciok")){
            handleUciOk();
        }
        else if (line.startsWith("bestmove")){
            String[] parts = line.split(" ");
            String move = parts[1];
            String ponder = null;
            if (parts.length >= 4)
                ponder = parts[3];
            Move move1 = new Move(move, ponder);
            sendToListeners(move1);
        }
        else if (line.startsWith("info ")){
            logger.info(line);
        }
        else if (line.equals("readyok")){
            sendReadyToListeners();
        }
    }

    public void newGame(){
        send("ucinewgame");
        sendIsReady();
    }

    private void sendIsReady(){
        send("isready");
    }

    public void setOption(StockfishOption option, String value){
        if (option.getType().equals("button"))
            send("setoption name " + option.getName());
        else
            send("setoption name " + option.getName() + " value " + value);
    }

    public void sendMoves(){
        if (!moves.isEmpty())
            send("position startpos moves " + moves.stream().collect(Collectors.joining(" ")));
        else
            send("position startpos");
    }

    public void go(Long maxMilliToSearch){
        StringBuilder sb = new StringBuilder("go ");
        if (maxMilliToSearch == null)
            sb.append("infinite ");  // must send "stop" to tell engine to stop searching
        else {
            sb.append("movetime ");
            sb.append(maxMilliToSearch);
            sb.append(" ");

            /*
            * depth <x>
		        search x plies only.
	        * nodes <x>
	            search x nodes only,
	        * mate <x>
		        search for a mate in x moves
             */
        }
        send(sb.toString());
    }

    /**
     * Tell stockfish to ponder this move
     * @param move
     */
    public void ponder(String move){
        send("go infinite ponder " + move);
    }

    /**
     * User has played ponder move
     */
    public void ponderhit(){
        send("ponderhit");
    }

    public void stopSearching(){
        send("stop");
    }


    private void send(String text){
        logger.info("Sending to Stockfish: '" + text + "'");
        try {
            outputStream.write(text + "\n");
            outputStream.flush();
        }
        catch (IOException ioe){
            logger.severe("Unable to send command to stockfish: " + ioe.getMessage());
        }
    }


    public void addListener(StockfishListener listener){
        listeners.add(listener);
    }

    private void sendToListeners(Move move){
        for (StockfishListener listener: listeners){
            listener.moveReceived(move);
        }
    }

    private void sendReadyToListeners(){
        for (StockfishListener listener: listeners){
            listener.stockfishIsReady();
        }
    }

    public List<String> getMoves() {
        return moves;
    }

    public void addMove(String move){
        moves.add(move);
    }

    private class ReaderThread extends Thread {
        private boolean shouldStop = false;

        public ReaderThread(){
            super();
        }

        @Override
        public void run() {
            while (!shouldStop) {
                String line = null;
                while ((line = receiveLine()) != null) {
                    handleInput(line);
                }
            }
            logger.warning("ReaderThread exited");
        }

        private String receiveLine(){
            try {
                return inputReader.readLine();
            }
            catch (IOException ioe){
                logger.severe("Unable to read from stockfish: " + ioe);
            }
            return "ERROR";
        }

        public void setShouldStop(boolean value){
            this.shouldStop = value;
        }
    }
}
