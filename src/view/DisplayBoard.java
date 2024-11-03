package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DisplayBoard extends JPanel {
    int tileSize = 85;
    int rows = 7;
    int cols = 8;
    int squares = 56;
    BufferedImage blueWallSheet;
    BufferedImage blueTowerSheet;
    BufferedImage redWallSheet;
    BufferedImage redTowerSheet;
    Image blueWall;
    Image blueTower;
    Image redWall;
    Image redTower;
    String FEN;
    {
        try {
            blueTowerSheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("blueTower.png"));
            blueTower = blueTowerSheet.getScaledInstance(tileSize, tileSize, BufferedImage.SCALE_SMOOTH);
            blueWallSheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("blueWall.png"));
            blueWall = blueWallSheet.getScaledInstance(tileSize, tileSize, BufferedImage.SCALE_SMOOTH);
            redTowerSheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("redTower.png"));
            redTower = redTowerSheet.getScaledInstance(tileSize, tileSize, BufferedImage.SCALE_SMOOTH);
            redWallSheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("redWall.png"));
            redWall = redWallSheet.getScaledInstance(tileSize, tileSize, BufferedImage.SCALE_SMOOTH);

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public DisplayBoard(String FEN) {
        this.setPreferredSize(new Dimension(cols * tileSize, rows*tileSize));
        this.FEN = FEN;
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        for (int r = 0; r < rows; r++)
        for (int c = 0; c < cols; c++){
            g2d.setColor((c + r) % 2 == 0 ? new Color(208, 207, 147) : new Color(86, 77, 41) );
            g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
        }
        paintPieces(g2d);

    }

    public void paintPieces(Graphics2D g2d){
        String [] rows = FEN.split("[/\\s]+");
        int squareCol;
        for (int r = 0; r < 7; r++){
            squareCol = 0;
            for (int c = 0; c < rows[r].length(); c++){
                if (Character.isDigit(rows[r].charAt(c))){
                    squareCol += Character.getNumericValue(rows[r].charAt(c));
                }
                else {
                    if (rows[r].charAt(c) == 'w') g2d.drawImage(redWall, squareCol * tileSize, r * tileSize, null);
                    else if (rows[r].charAt(c) == 'W')g2d.drawImage(blueWall, squareCol * tileSize, r * tileSize, null);
                    else if (rows[r].charAt(c) == 't')g2d.drawImage(redTower, squareCol * tileSize, r * tileSize, null);
                    else g2d.drawImage(blueTower, squareCol * tileSize, r * tileSize, null);
                    squareCol++;

                }
            }
        }
    }

    public void updateBoard(String FEN){
        this.FEN = FEN;
        repaint();
    }
}
