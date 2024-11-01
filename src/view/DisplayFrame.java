package view;

import javax.swing.*;
import java.awt.*;

public class DisplayFrame {
    protected JFrame frame = new JFrame();
    DisplayBoard displayBoard = new DisplayBoard();

    public DisplayFrame() {
        frame.getContentPane().setBackground(Color.darkGray);
        this.frame.setLayout(new GridBagLayout());
        this.frame.setMinimumSize(new Dimension(1000,1000));
        this.frame.setLocationRelativeTo(null);
        frame.add(displayBoard);
        frame.setVisible(true);
    }
}
