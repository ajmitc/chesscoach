package chesscoach.view;

import chesscoach.Model;

import javax.swing.*;
import java.awt.*;

public class View {
    private Model model;
    private JFrame frame;

    private BoardPanel boardPanel;

    public View(Model model){
        this.model = model;

        frame = new JFrame();
        frame.setTitle("Chess Coach");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        boardPanel = new BoardPanel(model, this);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);
    }

    public void refresh(){
        boardPanel.refresh();
    }

    public JFrame getFrame() {
        return frame;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
