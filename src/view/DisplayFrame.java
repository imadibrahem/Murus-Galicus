package view;

import javax.swing.*;
import java.awt.*;

public class DisplayFrame extends JFrame{
    DisplayBoard displayBoard;
    UserInput userInput;

    public DisplayFrame(String FEN) {
        getContentPane().setBackground(Color.darkGray);
        setLayout(new GridBagLayout());
        //this.frame.setLayout(null);
        setMinimumSize(new Dimension(1000,1000));
        setTitle("Murus Gallicus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayBoard = new DisplayBoard(FEN);
       // userInput = new UserInput(displayBoard);
        add(displayBoard);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public DisplayBoard getDisplayBoard() {
        return displayBoard;
    }
}
