package model.object;

import java.util.ArrayList;
import java.util.List;

public class Army {

    private final boolean isBlue;
    private final List<Piece> walls;
    private final List<Piece> towers;
    private final List<Piece> topped;
    private final List<Piece> capturedPieces;

    public Army(boolean isBlue) {
        this.isBlue = isBlue;
        this.walls = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.capturedPieces = new ArrayList<>();
        this.topped = new ArrayList<>();
        for (int i = 0; i < 16; i++) capturedPieces.add(new Piece(isBlue));
    }
}
