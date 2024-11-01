package view;

import javax.swing.*;
import java.awt.*;

public class DisplayBoard extends JPanel {
    int tileSize = 85;
    int rows = 7;
    int cols = 8;
    int squares = 56;

    public DisplayBoard() {
        this.setPreferredSize(new Dimension(cols * tileSize, rows*tileSize));
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        for (int r = 0; r < rows; r++)
        for (int c = 0; c < cols; c++){
            g2d.setColor((c + r) % 2 == 0 ? new Color(208, 207, 147) : new Color(86, 77, 41) );
            g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
        }

    }
}
