package model;

import model.player.Player;
import view.UserInput;

public class PlayerComparator {
    UserInput userInput1;
    Player blue1;
    Player red1;
    UserInput userInput2;
    Player blue2;
    Player red2;
    String FenInitial;
    Game first;
    Game second;

    public PlayerComparator(UserInput userInput1, Player blue1, Player red1, UserInput userInput2, Player blue2, Player red2, String fenInitial) {
        this.userInput1 = userInput1;
        this.blue1 = blue1;
        this.red1 = red1;
        this.userInput2 = userInput2;
        this.blue2 = blue2;
        this.red2 = red2;
        FenInitial = fenInitial;
    }

    public void playGames(){
        first = new Game(userInput1, red1, blue1, FenInitial);
        first.playGame();
        second = new Game(userInput2, red2, blue2, FenInitial);
        second.playGame();
        System.out.println(first.moves);
        System.out.println(second.moves);
        //if (first.hashes != second.hashes) System.out.println("!!!!!!!!!!!!!!! Problem with Hashing !!!!!!!!!!!!!!!");
        System.out.println(first.hashes);
        System.out.println(second.hashes);

    }
}
