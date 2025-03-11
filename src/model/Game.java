package model;

import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;
import model.evaluationFunction.InitialEvaluationFunction;
import model.evolutionTheory.evaluationFunction.OptimumEvaluationFunction;
import model.evolutionTheory.evaluationFunction.habitats.SeventhHabitatEvaluationFunction;
import model.move.MoveGeneratorEvolutionTheory;
import model.move.Move;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveType;
import model.object.ObjectBoard;
import model.player.*;
import view.DisplayBoard;
import view.DisplayFrame;
import view.UserInput;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Game {
    String initialFEN;
    String FEN;
    DisplayFrame displayFrame;
    DisplayBoard displayBoard;
    Board blueBoard;
    Board redBoard;
    Board generalBoard;
    Player red;
    Player blue;
    Player playerOn;
    UserInput userInput;
    List<Short> history;
    Deque<Move> moves;
    List<Move> blueMoves;
    List<Move> redMoves;
    List<Long> hashes;
    List<Long> blueHashes;
    List<Long> redHashes;
    int rounds = 0;
    Player winner;
    short oldMoveInitial = 0;
    short oldMoveFirst = 0;
    short oldMoveSecond = 0;
    double blueDurationMean = 0;
    double redDurationMean = 0;
    double blueNodesMean = 0;
    double redNodesMean = 0;
    double gameDuration = 0;
    int [] winnerReport = new int[3];

    Scanner scanner = new Scanner(System.in);

    public Game(UserInput userInput, Player red, Player blue, String initialFEN) {
        this.red = red;
        this.blue = blue;
        this.initialFEN =initialFEN;
        generalBoard = new BitBoard(FenTrimmer(initialFEN));
        history = new ArrayList<>();
        moves = new ArrayDeque<>();
        blueMoves = new ArrayList<>();
        redMoves = new ArrayList<>();
        hashes = new ArrayList<>();
        blueHashes = new ArrayList<>();
        redHashes = new ArrayList<>();
        setFenAndPlayerOn();
        displayFrame = new DisplayFrame(FEN);
        displayBoard = displayFrame.getDisplayBoard();
        this.userInput = userInput;
        userInput.setDisplayBoard(displayBoard);
        setPlayer(blue);
        setPlayer(red);
        winner = null;
    }

    public static String FenTrimmer (String initialFEN){
        String [] parts = initialFEN.split(",");
        return parts[0];
    }

    public void setFenAndPlayerOn(){
        String [] parts = initialFEN.split(",");
        FEN = parts[0];
        playerOn = parts[1].equals("b") ? blue : red;
        playerOn.setOn(true);
        short startingPlayer = (short) (playerOn.isEvaluationBlue() ? 1 : 0);
        history.add(startingPlayer);
    }

    public void setPlayer(Player player){
        if (player.isEvaluationBlue()) {
            this.blue = player;
            this.blueBoard = blue.getBoard();
            if (player instanceof User && playerOn.isEvaluationBlue())userInput.setPlayer(blue);

        }
        else{
            this.red = player;
            this.redBoard = red.getBoard();
            if (player instanceof User && !(playerOn.isEvaluationBlue()))userInput.setPlayer(red);

        }
    }

    public void switchPlayer() {
        playerOn = playerOn.isEvaluationBlue()? red : blue;
        playerOn.getBoard().build(FEN);
        blue.switchTurn();
        red.switchTurn();
        if (playerOn instanceof User)userInput.setPlayer(playerOn);
    }

    public void playRound() {
        rounds++;
        Move move = playerOn.findMove();
        if (!generalBoard.generateMoves(playerOn.isEvaluationBlue()).contains(move)) {
            System.out.println("Invalid Move!! Player lost");
            winner = playerOn.isEvaluationBlue()? red : blue;
        }
        else{
            addMove(move);
            history.add(move.getValue());
            playerOn.makeMove(move);
            addHashes();
            generalBoard.makeMove(move,playerOn.isEvaluationBlue());
            FEN = generalBoard.generateFEN();
            colorOldMove(playerOn.isEvaluationBlue(), move);
            displayBoard.updateBoard(FEN);
        }

    }

    public void withdrawRound() {
        rounds--;
        Move move = moves.pollLast();
        moves.remove(move);
        history.remove(history.get(history.size()-1));
        playerOn.unmakeMove(move);
        FEN = playerOn.getBoard().generateFEN();
        displayBoard.updateBoard(FEN);
    }

    public void makeRound(Move move) {
        rounds++;
        //System.out.println(move);
        addMove(move);
        history.add(move.getValue());
        playerOn.makeMove(move);
        FEN = playerOn.getBoard().generateFEN();
        colorOldMove(playerOn.isEvaluationBlue(), move);
        displayBoard.updateBoard(FEN);
    }

    public void unmakeRound(Move move) {
        rounds--;
        moves.remove(move);
        history.remove(history.get(history.size()-1));
        playerOn.unmakeMove(move);
        FEN = playerOn.getBoard().generateFEN();
        displayBoard.updateBoard(FEN);
    }


    public void playGame() {
        while (winner == null){
            playRound();
            switchPlayer();
            checkForWinner();
        }
    }

    public void rewindGame() {
        playerOn = playerOn.isEvaluationBlue()? red : blue;
        playerOn.getBoard().build(FEN);
        blue.switchTurn();
        red.switchTurn();
        while (rounds > 0){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Move move = moves.pollLast();
            System.out.println(move);
            playerOn.unmakeMove(move);
            colorOldMove(playerOn.isEvaluationBlue(), move);
            FEN = playerOn.getBoard().generateFEN();
            displayBoard.updateBoard(FEN);
            playerOn = playerOn.isEvaluationBlue()? red : blue;
            playerOn.getBoard().build(FEN);
            blue.switchTurn();
            red.switchTurn();
            rounds--;

        }
    }

    public void replayGame() {
        String [] parts = initialFEN.split(",");
        FEN = parts[0];
        playerOn = parts[1].equals("b") ? blue : red;
        Player playerOff = parts[1].equals("b") ? red : blue;
        playerOff.setOn(false);
        playerOn.setOn(true);
        red.getBoard().build(FEN);
        blue.getBoard().build(FEN);
        displayBoard.updateBoard(FEN);
        while (!moves.isEmpty()){
            /*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

             */
            Move move = moves.pollFirst();
            // System.out.println(move);
            scanner.nextLine();
            playerOn.makeMove(move);
            FEN = playerOn.getBoard().generateFEN();
            displayBoard.updateBoard(FEN);
            playerOn = playerOn.isEvaluationBlue()? red : blue;
            playerOn.getBoard().build(FEN);
            blue.switchTurn();
            red.switchTurn();
        }
    }

    public void checkForWinner(){
        if (playerOn.isEvaluationBlue()){
            if (blueBoard.lostGame(true)) winner = red;
            else if (redBoard.lostGame(false)) winner = blue;
        }
        else {
            if (redBoard.lostGame(false)) winner = blue;
            else if (blueBoard.lostGame(true)) winner = red;
        }
        if (winner != null){
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            if (winner == blue) System.out.println("+++++++++++ GAME OVER BLUE PLAYER WON!! ++++++++++++++");
            else System.out.println("+++++++++++ GAME OVER RED PLAYER WON!! +++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            blue.updateTables();
            red.updateTables();
            printGameResults();
            displayFrame.dispose();
        }
    }

    public void printGameResults(){
        System.out.println();
        System.out.println("++++++++++++ Blue Player played : "+ blue.getRounds() +" rounds ++++++++++++");
        System.out.println("++++++++++++ Blue Player Nodes: "+ blue.getNodes() +" ++++++++++++++++");
        System.out.println("Blue Player Moves Nodes: "+ blue.getMovesNodes());
        System.out.println();
        System.out.println("++++++++++ Blue Player Duration: "+ blue.getDuration() +" ++++++++++++++");
        System.out.println("Blue Player Moves Durations: "+ blue.getMoveDurations());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println("++++++++++++ Red Player played : "+ red.getRounds() +" rounds ++++++++++++");
        System.out.println("++++++++++++ Red Player Nodes: "+ red.getNodes() +" ++++++++++++++++");
        System.out.println("Red Player Moves Nodes: "+ red.getMovesNodes());
        System.out.println();
        System.out.println("++++++++++ Red Player Duration: "+ red.getDuration() +" ++++++++++++++");
        System.out.println("Red Player Moves Durations: "+ red.getMoveDurations());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        blueDurationMean = blue.getDuration()/blue.getRounds();
        redDurationMean = red.getDuration()/red.getRounds();
        blueNodesMean =  blue.getNodes()/(double)blue.getRounds();
        redNodesMean = red.getNodes()/(double)red.getRounds();
        gameDuration = blue.getDuration() + red.getDuration();
        int gameDurationHours = (int) (gameDuration / 3600);
        int gameDurationMinutes = (int) ((gameDuration % 3600) / 60);
        double gameDurationSeconds = gameDuration % 60;
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Blue Duration Mean: " + blueDurationMean + " ||| Red Duration Mean: " + redDurationMean);
        System.out.println("Blue Nodes Mean: " + blueNodesMean + " ||| Red Nodes Mean: " + redNodesMean);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("gameDuration: " + gameDurationHours + " hours, " + gameDurationMinutes + " minutes, " + String.format("%.2f", gameDurationSeconds) + " seconds");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println("++++++++++++++++++ Tables report +++++++++++++++++++++ ");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Blue Table: " + blue.tableUsageReport());
        System.out.println("Red Table: " + red.tableUsageReport());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        if (winner == blue){
            winnerReport[0] = blue.getRounds();
            winnerReport[1] = (int) blueNodesMean;
            winnerReport[2] = (int) (blueDurationMean * 1000);
        }
        else {
            winnerReport[0] = red.getRounds();
            winnerReport[1] = (int) redNodesMean;
            winnerReport[2] = (int) (redDurationMean * 1000);
        }
    }

    public void makeAndUnmakeAllMoves(){
        Scanner scanner = new Scanner(System.in);
        List<Move> allMoves = playerOn.getMoveGenerator().generateMoves(playerOn.isEvaluationBlue());
        for (Move move : allMoves){
            String before = playerOn.getBoard().printBoard(playerOn.isEvaluationBlue());
            System.out.println("Next Move: " + move + " Press to make");
            scanner.nextLine();
            makeRound(move);
            System.out.println("Move was: " + move + " Press to unmake");
            scanner.nextLine();
            unmakeRound(move);
            String after = playerOn.getBoard().printBoard(playerOn.isEvaluationBlue());
            if (!before.equals(after)){
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("!!!!!!!!!!!!!!!!!!!! BEFORE !!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(before);
                System.out.println("!!!!!!!!!!!!!!!!!!!! AFTER !!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(after);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            System.out.println();
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
    }

    public void addMove(Move move){
        if (playerOn.isEvaluationBlue()) {
            blueMoves.add(move);
        } else {
            redMoves.add(move);
        }
        //System.out.println(move);
        moves.add(move);
    }

    public void addHashes(){
        if (playerOn.isEvaluationBlue()) {
            blueHashes.add(playerOn.getZobristHashing().getHash());
        } else {
            redHashes.add(playerOn.getZobristHashing().getHash());
        }
        hashes.add(playerOn.getZobristHashing().getHash());
    }


    public void colorOldMove(boolean isBlue, Move move){
        displayBoard.displaySquare[oldMoveInitial].returnOldColor();
        displayBoard.displaySquare[oldMoveFirst].returnOldColor();
        displayBoard.displaySquare[oldMoveSecond].returnOldColor();
        int initial = move.getInitialLocation(isBlue);
        oldMoveInitial = (short) initial;
        if (move.isTargetEnemy()){
            oldMoveFirst = playerOn.getBoard().sacrificingMovesLocation(isBlue, initial,move.getDirection());
            displayBoard.displaySquare[oldMoveFirst].changeColor(Color.orange);
        }
        else {
            short[] targets = playerOn.getBoard().normalMovesLocation(isBlue, initial,move.getDirection());
            oldMoveFirst = targets[0];
            oldMoveSecond = targets[1];
            displayBoard.displaySquare[oldMoveSecond].changeColor(Color.orange);
        }
        displayBoard.displaySquare[oldMoveFirst].changeColor(Color.orange);
        displayBoard.displaySquare[oldMoveInitial].changeColor(Color.BLUE);

    }

    public Player getWinner() {
        return winner;
    }

    public int[] getWinnerReport() {
        return winnerReport;
    }

    public static void main (String[] args){
        UserInput userInput = new UserInput();
        UserInput userInput2 = new UserInput();
        UserInput userInput3 = new UserInput();

        String FenInitial = "tttttttt/8/8/8/8/8/TTTTTTTT,b";
        FenInitial = "t/8/8/5Tw1/8/8/T7,b";
        //FenInitial = "1tt4t/wwT/T6t/8/1W6/1WW5/1T1W3T,b";
        MoveType[] moveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
        int [] directions = {1, 8, 2, 3, 7, 6, 4, 5};
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Board blueBoard = new ObjectBoard(FenTrimmer(FenInitial));
        MoveGenerator blueGenerator = new MoveGeneratorEvolutionTheory(blueBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        EvaluationFunction blueEvaluationFunction = new InitialEvaluationFunction(blueBoard);
        //Player blue = new User(true, blueBoard,blueGenerator ,blueEvaluationFunction,userInput);
        //Player blue = new RandomPlayer(true, blueBoard,blueGenerator, blueEvaluationFunction);
        //Player blue = new FunctionPlayer(true, blueBoard,blueGenerator, blueEvaluationFunction);
        //Player blue = new MinMax(true, blueBoard,blueGenerator, blueEvaluationFunction,4);
        //Player blue = new AlphaBeta(true, blueBoard,blueGenerator, blueEvaluationFunction,4);
        //Player blue = new IterativeDeepeningFixedDepth(true, blueBoard,blueGenerator, blueEvaluationFunction,4);
        //Player blue = new QuiescencePlayer(true, blueBoard,blueGenerator, blueEvaluationFunction,4);
        //Player blue = new Tester(true, blueBoard,blueGenerator, blueEvaluationFunction,4);
        //Player blue = new DoubleTabledPlayer(true, blueBoard,blueGenerator, blueEvaluationFunction,6);

        Board redBoard = new ObjectBoard(FenTrimmer(FenInitial));
        MoveGenerator redGenerator = new MoveGeneratorEvolutionTheory(redBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        EvaluationFunction redEvaluationFunction = new InitialEvaluationFunction(redBoard);
        //Player red = new User(false, redBoard, redGenerator, redEvaluationFunction, userInput);
        //Player red = new MinMax(false, redBoard,redGenerator, redEvaluationFunction,4);
        //Player red = new AlphaBeta(false, redBoard,redGenerator, redEvaluationFunction,4);
        //Player red = new IterativeDeepeningFixedDepth(false, redBoard,redGenerator, redEvaluationFunction,4);
        //Player red = new QuiescencePlayer(false, redBoard,redGenerator, redEvaluationFunction,4);
        //Player red = new Tester(false, redBoard,redGenerator, redEvaluationFunction,2);
        //Player red = new DoubleTabledPlayer(false, redBoard,redGenerator, redEvaluationFunction,6);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Board blueBoard2 = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator blueGenerator2 = new MoveGeneratorEvolutionTheory(blueBoard2, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        EvaluationFunction blueEvaluationFunction2 = new OptimumEvaluationFunction(blueBoard2);
        //EvaluationFunction blueEvaluationFunction2 = new FirstHabitatEvaluationFunction(blueBoard2);
        Player blue2 = new User(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,userInput2);
        //Player blue2 = new AlphaBeta(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,4);
        //Player blue2 = new IterativeDeepeningFixedDepth(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,5);
        //Player blue2 = new QuiescencePlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,5);
        //Player blue2 = new Tester(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,5);
        //Player blue2 = new NullWindowAspirationPlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,6);
        //Player blue2 = new MoveSortingPlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,6);
        //Player blue2 = new ImprovedQuiescenceAndMoveSortingPlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,7);
        //Player blue2 = new ThreateningQuiescencePlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,7);
        //Player blue2 = new NullMovePlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,6);
        //Player blue2 = new HashTester(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,6);
        //Player blue2 = new LateMoveReductionPlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,9);
        //Player blue2 = new TranspositionTablePlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2, 8);
        //Player blue2 = new DoubleTabledPlayer(true, blueBoard2,blueGenerator2, blueEvaluationFunction2,9);

        Board redBoard2 = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator redGenerator2 = new MoveGeneratorEvolutionTheory(redBoard2, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        //EvaluationFunction redEvaluationFunction2 = new InitialEvaluationFunction(redBoard2);
        EvaluationFunction redEvaluationFunction2 = new SeventhHabitatEvaluationFunction(redBoard2);
        Player red2 = new User(false, redBoard2,redGenerator2, redEvaluationFunction2, userInput2);
        //Player red2 = new AlphaBeta(false, redBoard2,redGenerator2, redEvaluationFunction2, 4);
        //Player red2 = new IterativeDeepeningFixedDepth(false, redBoard2,redGenerator2, redEvaluationFunction2, 6);
        //Player red2 = new QuiescencePlayer(false, redBoard2,redGenerator2, redEvaluationFunction2, 4);
        //Player red2 = new Tester(false, redBoard2,redGenerator2, redEvaluationFunction2, 4);
        //Player red2 = new NullWindowAspirationPlayer(false, redBoard2,redGenerator2, redEvaluationFunction2,5);
        //Player red2 = new MoveSortingPlayer(false, redBoard2,redGenerator2, redEvaluationFunction2,6);
        //Player red2 = new ImprovedQuiescenceAndMoveSortingPlayer(false, redBoard2,redGenerator2, redEvaluationFunction2,8);
        //Player red2 = new ThreateningQuiescencePlayer(false, redBoard2,redGenerator2, redEvaluationFunction2,7);
        //Player red2 = new NullMovePlayer(false, redBoard2,redGenerator2, redEvaluationFunction2,8);
        //Player red2 = new HashTester(false, redBoard2,redGenerator2, redEvaluationFunction2,6);
        //Player red2 = new LateMoveReductionPlayer(false, redBoard2,redGenerator2, redEvaluationFunction2,6);
        //Player red2 = new TranspositionTablePlayer(false, redBoard2,redGenerator2, redEvaluationFunction2,8);
        //Player red2 = new DoubleTabledPlayer(false, redBoard2,redGenerator2, redEvaluationFunction2,9);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Board blueBoard3 = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator blueGenerator3 = new MoveGeneratorEvolutionTheory(blueBoard3, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        EvaluationFunction blueEvaluationFunction3 = new InitialEvaluationFunction(blueBoard3);
        //Player blue3 = new AlphaBeta(true, blueBoard3,blueGenerator3, blueEvaluationFunction3,4);
        //Player blue3 = new IterativeDeepeningFixedDepth(true, blueBoard3,blueGenerator3, blueEvaluationFunction3,5);
        //Player blue3 = new NullWindowAspirationPlayer(true, blueBoard3,blueGenerator3, blueEvaluationFunction3,5);
        //Player blue3 = new MoveSortingPlayer(true, blueBoard3,blueGenerator3, blueEvaluationFunction3,6);
        //Player blue3 = new DoubleTabledPlayer(true, blueBoard3,blueGenerator3, blueEvaluationFunction3,6);

        Board redBoard3 = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator redGenerator3 = new MoveGeneratorEvolutionTheory(redBoard3, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        EvaluationFunction redEvaluationFunction3 = new InitialEvaluationFunction(redBoard3);
        //Player red3 = new MinMax(false, redBoard3,redGenerator3, redEvaluationFunction3, 4);
        //Player red3 = new AlphaBeta(false, redBoard3,redGenerator3, redEvaluationFunction3, 4);
        //Player red3 = new IterativeDeepeningFixedDepth(false, redBoard3,redGenerator3, redEvaluationFunction3, 4);
        //Player red3 = new QuiescencePlayer(false, redBoard3,redGenerator3, redEvaluationFunction3, 4);
        //Player red3 = new NullWindowAspirationPlayer(false, redBoard3,redGenerator3, redEvaluationFunction3, 5);
        //Player red3 = new MoveSortingPlayer(false, redBoard3,redGenerator3, redEvaluationFunction3, 5);
        //Player red3 = new DoubleTabledPlayer(false, redBoard3,redGenerator3, redEvaluationFunction3, 6);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Game game = new Game(userInput, red, blue, FenInitial);
        //game.playGame();

        Game game2 = new Game(userInput2, red2, blue2, FenInitial);
        game2.playGame();
        //game2.replayGame();

        //GameComparator gameComparator = new GameComparator(game, game2);
        //gameComparator.compareBoardsFunctions(false);
        //gameComparator.compareHashingSymmetry();
        //PlayerComparator playerComparator = new PlayerComparator(userInput2, blue2, red2, userInput3, blue3, red3, FenInitial);
        //playerComparator.playGames();
    }

}
