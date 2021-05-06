package chesscoach.view;

import chesscoach.Model;

import javax.swing.*;

public class View {
    private Model model;
    private JFrame frame;

    public View(Model model){
        this.model = model;

        frame = new JFrame();
        frame.setTitle("Chess Coach");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public JFrame getFrame() {
        return frame;
    }
}
