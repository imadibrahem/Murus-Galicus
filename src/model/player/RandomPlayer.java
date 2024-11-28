package model.player;

import model.Board;
import model.move.Move;
import model.move.MoveGenerator;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player{

    public RandomPlayer(boolean isBlue, Board board, MoveGenerator moveGenerator) {
        super(isBlue, board, moveGenerator);
    }


    @Override
    public Move decideMove() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Move> moves = moveGenerator.generateMoves(isEvaluationBlue());
        Random random = new Random();
        int randomIndex = random.nextInt(moves.size());
        return moves.get(randomIndex);
    }

}
