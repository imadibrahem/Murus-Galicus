package model.object;

import java.util.ArrayList;
import java.util.List;

public class Army {

    private final boolean isBlue;
    private final List<Piece> walls;
    private final List<Piece> towers;
    private final List<Piece> topped;
    private final List<Piece> sacrificedPieces;

    public Army(boolean isBlue) {
        this.isBlue = isBlue;
        this.walls = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.topped = new ArrayList<>();
        this.sacrificedPieces = new ArrayList<>();
        for (int i = 0; i < 16; i++) sacrificedPieces.add(new Piece(isBlue));
    }

    public boolean isBlue() {
        return isBlue;
    }

    public List<Piece> getWalls() {
        return walls;
    }

    public List<Piece> getTowers() {
        return towers;
    }

    public List<Piece> getTopped() {
        return topped;
    }

    public List<Piece> getSacrificedPieces() {
        return sacrificedPieces;
    }
    public void withdrawFromBoard() {
        // TODO: 10/22/2024  
        
    }
}
