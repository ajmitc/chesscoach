package chesscoach.game.rule;

import chesscoach.game.Piece;
import chesscoach.util.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class Rules {
    private static Logger logger = Logger.getLogger(Rules.class.getName());

    public abstract List<Space> getValidMoves(Piece piece, Space fromSpace, List<Piece> pieces);


    /**
     * Get the unbroken path between fromSpace and toSpace.  If another piece is encountered, stop
     * @param fromSpace
     * @param toSpace
     * @return
     */
    public List<Space> getPath(Space fromSpace, Space toSpace, List<Piece> pieces){
        int dx = toSpace.file - fromSpace.file;
        int dy = toSpace.rank - fromSpace.rank;
        if (dx < -1) dx = -1;
        if (dx >  1) dx =  1;
        if (dy < -1) dy = -1;
        if (dy >  1) dy =  1;

        List<Space> spaces = new ArrayList<>();
        int cx = fromSpace.file;
        int cy = fromSpace.rank;
        while (cx != toSpace.file || cy != toSpace.rank){
            cx += dx;
            cy += dy;
            // If we're off the board, stop
            if (cx < 0 || cx >= 8 || cy < 0 || cy >= 8)
                break;
            spaces.add(new Space(cy, cx));
            Piece piece = getPieceAt(cy, cx, pieces);
            if (piece != null && !piece.isCaptured()){
                //logger.warning("Found piece " + piece);
                break;
            }
        }
        return spaces;
    }

    protected Piece getPieceAt(int rank, int file, List<Piece> pieces){
        Optional<Piece> opt = pieces.stream().filter(piece -> piece.getRank() == rank && piece.getFile() == file && !piece.isCaptured()).findFirst();
        return opt.isPresent()? opt.get(): null;
    }

    protected Piece getPieceAt(Space space, List<Piece> pieces){
        return getPieceAt(space.rank, space.file, pieces);
    }
}
