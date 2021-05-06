package chesscoach.stockfish;

import chesscoach.game.Move;

public interface StockfishListener {
    void moveReceived(Move move);
}
