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
        if (row >= 0 && row < 8 && col >= 0 && col < 8)
            return new Space(row, col);
        return null;
    }

    @Override
    public void paintComponent(Graphics graphics){
        // Fill background with dark color
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
        for (int i = 0; i < 8; ++i) {
            int x = BOARD_XOFFSET + 7 + (i * SPACE_SIZE);
            int y = BOARD_YOFFSET - 3;
            graphics.drawString("" + (char) ('a' + i), x, y);
            graphics.drawString("" + (char) ('a' + i), x, y + (SPACE_SIZE * BOARD_WIDTH) + RANK_FILE_FONT_SIZE + 3);
        }

        // Draw rank labels
        for (int i = 0; i < 8; ++i) {
            int x = 3;
            int y = BOARD_YOFFSET + (SPACE_SIZE * BOARD_WIDTH) - (i * SPACE_SIZE) - 4;
            graphics.drawString("" + (i + 1), x, y);
            graphics.drawString("" + (i + 1), x + ((BOARD_WIDTH + 1) * SPACE_SIZE) - 2, y);
        }

        // Draw hover space
        if (hoverRank != null && hoverFile != null){
            int x = BOARD_XOFFSET + (hoverFile * SPACE_SIZE);
            int y = BOARD_YOFFSET + ((hoverRank + 1) * -SPACE_SIZE) + (BOARD_WIDTH * SPACE_SIZE);
            graphics.setColor(HIGHLIGHT_COLOR);
            graphics.drawRect(x, y, SPACE_SIZE, SPACE_SIZE);
        }


        // Draw pieces
        for (Piece piece: model.getGame().getPieces()){
            if (piece.isCaptured())
                continue;
            int x = BOARD_XOFFSET + (piece.getFile() * SPACE_SIZE);
            int y = BOARD_YOFFSET + ((piece.getRank() + 1) * -SPACE_SIZE) + (SPACE_SIZE * BOARD_WIDTH);
            drawPieceAt(graphics, piece, x, y);
        }
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
