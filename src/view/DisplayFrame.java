package view;

import javax.swing.*;
import java.awt.*;

public class DisplayFrame {
    protected JFrame frame = new JFrame();
    DisplayBoard displayBoard;

    public DisplayFrame(String FEN) {
        frame.getContentPane().setBackground(Color.darkGray);
        this.frame.setLayout(new GridBagLayout());
        this.frame.setMinimumSize(new Dimension(1000,1000));
        this.frame.setLocationRelativeTo(null);
        frame.setTitle("Murus Gallicus");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayBoard = new DisplayBoard(FEN);
        frame.add(displayBoard);
        frame.setVisible(true);
    }

    public DisplayBoard getDisplayBoard() {
        return displayBoard;
    }
}
