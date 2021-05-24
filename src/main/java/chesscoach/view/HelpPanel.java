package chesscoach.view;

import chesscoach.Model;

import javax.swing.*;
import java.awt.*;

public class HelpPanel extends JPanel {
    private static final Font HELP_FONT = new Font("Serif", Font.PLAIN, 10);
    private Model model;
    private View view;

    private JButton btnShowHelp;
    private JLabel lblHelp;

    public HelpPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        btnShowHelp = new JButton("?");
        lblHelp = new JLabel("");
        lblHelp.setFont(HELP_FONT);

        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonpanel.add(btnShowHelp);

        setLayout(new BorderLayout());
        add(buttonpanel, BorderLayout.NORTH);
        add(lblHelp, BorderLayout.CENTER);
    }

    public JButton getBtnShowHelp() {
        return btnShowHelp;
    }

    public void showHelp(){
        lblHelp.setText("<html>Best Move: " + model.getGame().getBestMove() + "<br/>Score: " + model.getGame().getBestMoveScore() + "</html>");
    }

    public void clearHelp(){
        lblHelp.setText("");
    }
}
