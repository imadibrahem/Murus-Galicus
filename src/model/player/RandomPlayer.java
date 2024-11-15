package model.player;

import model.Board;
import model.move.Move;
import model.move.MoveGenerator;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player{
    MoveGenerator moveGenerator;

    public RandomPlayer(boolean isBlue, Board board, MoveGenerator moveGenerator) {
        super(isBlue, board);
        this.moveGenerator = moveGenerator;
    }

    @Override
    public Move decideMove() {
        List<Move> moves = moveGenerator.generateMoves(isBlue());
        Random random = new Random();
        int randomIndex = random.nextInt(moves.size());
        // TODO: 11/15/2024 remove later
        moveGenerator.generateAllStyles(isBlue());
        return moves.get(randomIndex);
    }

}
