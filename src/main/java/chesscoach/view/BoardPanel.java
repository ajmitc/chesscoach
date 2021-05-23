package chesscoach.view;

import chesscoach.Model;
import chesscoach.game.Piece;
import chesscoach.game.Side;
import chesscoach.util.Space;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    public static final int BOARD_WIDTH = 8;
    public static final int SPACE_SIZE = 20;
    public static final Color LIGHT_COLOR = Color.WHITE;
    public static final Color DARK_COLOR = Color.GREEN.darker();
    public static final Color BORDER_COLOR = Color.BLACK;
    public static final Color PIECE_FONT_COLOR = Color.BLACK;
    public static final Color HIGHLIGHT_COLOR = Color.YELLOW;
    public static final int BOARD_XOFFSET = 15;
    public static final int BOARD_YOFFSET = 25;
    public static final int RANK_FILE_FONT_SIZE = 10;
    public static final Font RANK_FILE_FONT = new Font("Serif", Font.PLAIN, RANK_FILE_FONT_SIZE);
    private static final char[] FILES_LIGHT = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static final char[] FILES_DARK  = {'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a'};
    private static final char[] RANK_LIGHT  = {'1', '2', '3', '4', '5', '6', '7', '8'};
    private static final char[] RANK_DARK   = {'8', '7', '6', '5', '4', '3', '2', '1'};
    public static final int CAPTURED_PLAYER_PIECES_YOFFSET = 0;
    public static final int CAPTURED_COMP_PIECES_YOFFSET   = BOARD_YOFFSET + (SPACE_SIZE * BOARD_WIDTH) + RANK_FILE_FONT_SIZE + 5;
    public static final int BEST_MOVE_XOFFSET  = BOARD_XOFFSET + (SPACE_SIZE * BOARD_WIDTH) + RANK_FILE_FONT_SIZE + 10;
    public static final int BEST_MOVE_YOFFSET  = BOARD_YOFFSET;
    public static final int BEST_SCORE_XOFFSET = BOARD_XOFFSET + (SPACE_SIZE * BOARD_WIDTH) + RANK_FILE_FONT_SIZE + 10;
    public static final int BEST_SCORE_YOFFSET = BOARD_YOFFSET + RANK_FILE_FONT_SIZE + 10;

    private Model model;
    private View view;

    private Integer hoverRank;
    private Integer hoverFile;

    public BoardPanel(Model model, View view){
        this.model = model;
        this.view = view;
    }

    public Space getSpaceAt(int mx, int my){
        int row = (-(my - BOARD_YOFFSET) / SPACE_SIZE) + (BOARD_WIDTH) - 1;
        int col = (mx - BOARD_XOFFSET) / SPACE_SIZE;
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            if (model.getGame().getPlayerSide() == Side.LIGHT) {
                return new Space(row, col);
            } else {
                return new Space(7 - row, 7 - col);
            }
        }
        return null;
    }

    @Override
    public void paintComponent(Graphics graphics){
        // Clear background
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        // Fill board background with dark color
        graphics.setColor(DARK_COLOR);
        graphics.fillRect(BOARD_XOFFSET, BOARD_YOFFSET, SPACE_SIZE * BOARD_WIDTH, SPACE_SIZE * BOARD_WIDTH);

        // Paint light color squares
        graphics.setColor(LIGHT_COLOR);
        for (int row = 0; row < BOARD_WIDTH; ++row){
            for (int col = 0; col < BOARD_WIDTH; ++col){
                if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
                    int x = BOARD_XOFFSET + (col * SPACE_SIZE);
                    int y = BOARD_YOFFSET + (row * SPACE_SIZE);
                    graphics.fillRect(x, y, SPACE_SIZE, SPACE_SIZE);
                }
            }
        }

        // Draw border
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(BOARD_XOFFSET, BOARD_YOFFSET, SPACE_SIZE * BOARD_WIDTH, SPACE_SIZE * BOARD_WIDTH);

        // Draw file labels
        graphics.setColor(Color.BLACK);
        graphics.setFont(RANK_FILE_FONT);
        char[] fileSeq = model.getGame().getPlayerSide() == Side.LIGHT? FILES_LIGHT: FILES_DARK;
        for (int i = 0; i < 8; ++i) {
            int x = BOARD_XOFFSET + 7 + (i * SPACE_SIZE);
            int y = BOARD_YOFFSET - 3;
            graphics.drawString("" + fileSeq[i], x, y);
            graphics.drawString("" + fileSeq[i], x, y + (SPACE_SIZE * BOARD_WIDTH) + RANK_FILE_FONT_SIZE + 3);
        }

        // Draw rank labels
        char[] rankSeq = model.getGame().getPlayerSide() == Side.LIGHT? RANK_LIGHT: RANK_DARK;
        for (int i = 0; i < 8; ++i) {
            int x = 3;
            int y = BOARD_YOFFSET + (SPACE_SIZE * BOARD_WIDTH) - (i * SPACE_SIZE) - 4;
            graphics.drawString("" + rankSeq[i], x, y);
            graphics.drawString("" + rankSeq[i], x + ((BOARD_WIDTH + 1) * SPACE_SIZE) - 2, y);
        }

        // Draw hover space
        if (hoverRank != null && hoverFile != null){
            int file = model.getGame().getPlayerSide() == Side.LIGHT? hoverFile: 7 - hoverFile;
            int rank = model.getGame().getPlayerSide() == Side.LIGHT? hoverRank: 7 - hoverRank;
            int x = BOARD_XOFFSET + (file * SPACE_SIZE);
            int y = BOARD_YOFFSET + ((rank + 1) * -SPACE_SIZE) + (BOARD_WIDTH * SPACE_SIZE);
            graphics.setColor(HIGHLIGHT_COLOR);
            graphics.drawRect(x, y, SPACE_SIZE, SPACE_SIZE);
        }

        // Draw pieces
        for (Piece piece: model.getGame().getPieces()){
            if (piece.isCaptured())
                continue;
            int file = model.getGame().getPlayerSide() == Side.LIGHT? piece.getFile(): 7 - piece.getFile();
            int rank = model.getGame().getPlayerSide() == Side.LIGHT? piece.getRank(): 7 - piece.getRank();
            int x = BOARD_XOFFSET + (file * SPACE_SIZE);
            int y = BOARD_YOFFSET + ((rank + 1) * -SPACE_SIZE) + (SPACE_SIZE * BOARD_WIDTH);
            drawPieceAt(graphics, piece, x, y);
        }

        // Draw captured pieces
        int playerX = 0;
        int compX = 0;
        for (Piece piece: model.getGame().getPieces()) {
            if (piece.isCaptured()) {
                if (piece.getSide() == model.getGame().getPlayerSide()){
                    drawPieceAt(graphics, piece, playerX, CAPTURED_PLAYER_PIECES_YOFFSET);
                    playerX += SPACE_SIZE;
                }
                else {
                    drawPieceAt(graphics, piece, compX, CAPTURED_COMP_PIECES_YOFFSET);
                    compX += SPACE_SIZE;
                }
            }
        }

        // Draw best move & score
        graphics.setColor(Color.BLACK);
        graphics.drawString("Best Move: " + model.getGame().getBestMove(), BEST_MOVE_XOFFSET, BEST_MOVE_YOFFSET);
        graphics.drawString("Score: " + model.getGame().getBestMoveScore(), BEST_SCORE_XOFFSET, BEST_SCORE_YOFFSET);
    }

    private void drawPieceAt(Graphics graphics, Piece piece, int x, int y){
        graphics.setColor(piece.getSide() == Side.LIGHT? Color.WHITE: Color.BLACK);
        graphics.fillOval(x + 2, y + 2, SPACE_SIZE - 4, SPACE_SIZE - 4);
        graphics.setColor(piece.getSide() == Side.LIGHT? Color.BLACK: Color.WHITE);
        graphics.drawOval(x + 2, y + 2, SPACE_SIZE - 4, SPACE_SIZE - 4);
        graphics.drawString(piece.getType().getCode(), x + 7, y + SPACE_SIZE - 5);
    }

    public void refresh(){
        repaint();
    }

    public void setHover(int mx, int my){
        Space space = getSpaceAt(mx, my);
        if (space != null){
            hoverRank = space.rank;
            hoverFile = space.file;
        }
        else {
            hoverFile = null;
            hoverRank = null;
        }
    }
}
