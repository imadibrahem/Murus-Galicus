package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Player {
    private boolean isBlue;
    protected final boolean isEvaluationBlue;
    private final Board board;
    private boolean isOn = false;
    protected final EvaluationFunction evaluationFunction;
    protected int nodes = 0;
    protected int moveNodes = 0;
    protected List<Integer> movesNodes = new ArrayList<>();
    protected double moveStartTime;
    protected double duration = 0;
    protected double moveDuration;
    protected List<Double> moveDurations = new ArrayList<>();
    Map<Integer, Integer> directionMap = new HashMap<>();
    protected MoveGenerator moveGenerator;



    public Player(boolean isBlue,Board board, EvaluationFunction evaluationFunction) {
        this.isBlue = isBlue;
        this.board = board;
        this.isEvaluationBlue = isBlue;
        this.evaluationFunction = evaluationFunction;
        directionMap.put(-9, 8);
        directionMap.put(-8, 1);
        directionMap.put(-7, 2);
        directionMap.put(-1, 7);
        directionMap.put(1, 3);
        directionMap.put(7, 6);
        directionMap.put(8, 5);
        directionMap.put(9, 4);
    }

    public Player(boolean isBlue,Board board,MoveGenerator moveGenerator, EvaluationFunction evaluationFunction) {
        this.isBlue = isBlue;
        this.board = board;
        this.isEvaluationBlue = isBlue;
        this.moveGenerator = moveGenerator;
        this.evaluationFunction = evaluationFunction;
        directionMap.put(-9, 8);
        directionMap.put(-8, 1);
        directionMap.put(-7, 2);
        directionMap.put(-1, 7);
        directionMap.put(1, 3);
        directionMap.put(7, 6);
        directionMap.put(8, 5);
        directionMap.put(9, 4);
    }

    public double getDuration() {
        return duration;
    }

    public List<Double> getMoveDurations() {
        return moveDurations;
    }

    public int getNodes() {
        return nodes;
    }

    public List<Integer> getMovesNodes() {
        return movesNodes;
    }

    public boolean isBlue() {
        return isBlue;
    }

    public boolean isEvaluationBlue() {
        return isEvaluationBlue;
    }

    public boolean isOn() {
        return isOn;
    }

    public Board getBoard() {
        return board;
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }

    public EvaluationFunction getEvaluationFunction() {
        return evaluationFunction;
    }

    public void switchColor() {
        isBlue = !isBlue;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public void switchTurn(){
        isOn = !isOn;
    }

    public void makeMove(Move move){
        board.makeMove(move, isBlue);
    }

    public void unmakeMove(Move move){
        board.unmakeMove(move, isBlue);
    }

    public Move recieveCords(int initial , int targetNear, int targetFar){
        int location = this.isEvaluationBlue() ? initial : 55 - initial;
        int distance = isEvaluationBlue() ? targetNear - initial :initial - targetNear;
        // TODO: 11/28/2024 remove later:
        if (directionMap.get(distance) == null) System.out.println("Problem is with the distance: " + distance
                + "initial is: " + initial + "near target is : " + targetNear + "far target is : " + targetFar);
        int direction = directionMap.get(distance);
        int targetType;
        if (targetFar < 0) targetType = 4;
        else {
            boolean friendlyNear = getBoard().isFriendlyPiece(isEvaluationBlue(), targetNear);
            boolean friendlyFar = getBoard().isFriendlyPiece(isEvaluationBlue(), targetFar);
            if (!friendlyNear && !friendlyFar) targetType = 0;
            else if (friendlyNear && friendlyFar) targetType = 3;
            else if (friendlyNear) targetType = 1;
            else targetType = 2;
        }
        return new Move((short) (location << 7 | direction << 3 | targetType));
    }

    public Move findMove(){
        moveStartTime = System.currentTimeMillis();
        Move move = decideMove();
        moveDuration = (System.currentTimeMillis() - moveStartTime) / 1000;
        moveDurations.add(moveDuration);
        duration += moveDuration;
        return move;
    }
    public abstract Move decideMove();

}
