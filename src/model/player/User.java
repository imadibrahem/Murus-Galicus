package model.player;

import model.Board;
import model.move.Move;
import model.move.MoveGenerator;
import view.UserInput;

public class User extends Player{
    UserInput userInput;

    public User(boolean isBlue, Board board, UserInput userInput) {
        super(isBlue, board);
        this.userInput = userInput;
    }

    public User(boolean isBlue, Board board, MoveGenerator moveGenerator, UserInput userInput) {
        super(isBlue, board, moveGenerator);
        this.userInput = userInput;
    }

    @Override
    public Move decideMove() {
        this.userInput.setPlayer(this);
        userInput.setChoosing(true);
        return userInput.decideMove();
    }


}
