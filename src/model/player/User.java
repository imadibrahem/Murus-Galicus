package model.player;

import model.Board;
import model.Move;
import view.UserInput;

import java.util.HashMap;
import java.util.Map;

public class User extends Player{
    UserInput userInput;

    public User(boolean isBlue, Board board, UserInput userInput) {
        super(isBlue, board);
        this.userInput = userInput;
    }


    @Override
    public Move decideMove() {
        this.userInput.setPlayer(this);
        userInput.setChoosing(true);
        return userInput.decideMove();
    }


}
