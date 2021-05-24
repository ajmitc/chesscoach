package chesscoach.view;

import chesscoach.game.Piece;
import chesscoach.game.PieceType;
import chesscoach.game.Side;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static chesscoach.view.ImageUtil.toBufferedImage;

public class PieceImageUtil {
    private static String IMAGE_FILE = "pieces-trans.png";
    private static Map<Side, Map<PieceType, Point>> pointMap = new HashMap<>();
    private static int PIECE_SIZE = 45;
    private static int XOFFSET = 15;
    private static int YOFFSET = 12; //160;
    private static int DARK_YOFFSET = YOFFSET + PIECE_SIZE + 16;

    private static int KING_XOFFSET   = XOFFSET;
    private static int QUEEN_XOFFSET  = KING_XOFFSET + 60;
    private static int ROOK_XOFFSET   = QUEEN_XOFFSET + PIECE_SIZE + 20;
    private static int BISHOP_XOFFSET = ROOK_XOFFSET + PIECE_SIZE + 16;
    private static int KNIGHT_XOFFSET = BISHOP_XOFFSET + PIECE_SIZE + 16;
    private static int PAWN_XOFFSET   = KNIGHT_XOFFSET + PIECE_SIZE + 18;

    static {
        pointMap.put(Side.DARK, new HashMap<>());
        pointMap.put(Side.LIGHT, new HashMap<>());

        pointMap.get(Side.DARK).put(PieceType.PAWN,   new Point(PAWN_XOFFSET,   DARK_YOFFSET));
        pointMap.get(Side.DARK).put(PieceType.KING,   new Point(KING_XOFFSET,   DARK_YOFFSET));
        pointMap.get(Side.DARK).put(PieceType.QUEEN,  new Point(QUEEN_XOFFSET,  DARK_YOFFSET));
        pointMap.get(Side.DARK).put(PieceType.BISHOP, new Point(BISHOP_XOFFSET, DARK_YOFFSET));
        pointMap.get(Side.DARK).put(PieceType.KNIGHT, new Point(KNIGHT_XOFFSET, DARK_YOFFSET));
        pointMap.get(Side.DARK).put(PieceType.ROOK,   new Point(ROOK_XOFFSET,   DARK_YOFFSET));

        pointMap.get(Side.LIGHT).put(PieceType.PAWN,   new Point(PAWN_XOFFSET,   YOFFSET));
        pointMap.get(Side.LIGHT).put(PieceType.KING,   new Point(KING_XOFFSET,   YOFFSET));
        pointMap.get(Side.LIGHT).put(PieceType.QUEEN,  new Point(QUEEN_XOFFSET,  YOFFSET));
        pointMap.get(Side.LIGHT).put(PieceType.BISHOP, new Point(BISHOP_XOFFSET, YOFFSET));
        pointMap.get(Side.LIGHT).put(PieceType.KNIGHT, new Point(KNIGHT_XOFFSET, YOFFSET));
        pointMap.get(Side.LIGHT).put(PieceType.ROOK,   new Point(ROOK_XOFFSET,   YOFFSET));
    }

    public static BufferedImage get(PieceType type, Side side, int width){
        BufferedImage cachedImage = ImageUtil.get(getKey(type, side));
        if (cachedImage != null){
            return cachedImage;
        }
        cachedImage = ImageUtil.get(IMAGE_FILE);
        Point p = pointMap.get(side).get(type);
        BufferedImage bi = cachedImage.getSubimage(p.x, p.y, PIECE_SIZE, PIECE_SIZE);
        int iw = bi.getWidth(null);
        int ih = bi.getHeight(null);
        double scale = ((double) width) / ((double) iw);
        double h = (int) (ih * scale);
        bi = toBufferedImage(bi.getScaledInstance(width, (int) h, BufferedImage.SCALE_SMOOTH));
        ImageUtil.addToCache(getKey(type, side), bi);
        return bi;
    }

    private static String getKey(PieceType type, Side side){
        return side.name() + type.getCode();
    }

    private PieceImageUtil(){}
}
