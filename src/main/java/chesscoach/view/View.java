package chesscoach.view;

import chesscoach.Model;

import javax.swing.*;
import java.awt.*;

public class View {
    private Model model;
    private JFrame frame;

    private BoardPanel boardPanel;
    private HelpPanel helpPanel;

    public View(Model model){
        this.model = model;

        frame = new JFrame();
        frame.setTitle("Chess");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        boardPanel = new BoardPanel(model, this);
        helpPanel = new HelpPanel(model, this);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);
        frame.getContentPane().add(helpPanel, BorderLayout.EAST);
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

    public HelpPanel getHelpPanel() {
        return helpPanel;
    }
}
