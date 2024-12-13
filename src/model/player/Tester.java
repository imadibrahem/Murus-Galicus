package model.player;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.move.Move;
import model.move.MoveGenerator;
import view.DisplayBoard;
import view.DisplayFrame;

import java.awt.*;
import java.util.List;

public class Tester extends Player{
    private final int searchDepth;
    private int currentSearchDepth = 1;
    private Move best;
    private Move globalBest;
    String FEN = board.generateFEN();
    DisplayFrame displayFrame;
    DisplayBoard displayBoard;
    short oldMoveInitial = 0;
    short oldMoveFirst = 0;
    short oldMoveSecond = 0;

    public Tester(boolean isBlue, Board board, MoveGenerator moveGenerator, EvaluationFunction evaluationFunction, int searchDepth) {
        super(isBlue, board, moveGenerator, evaluationFunction);
        this.searchDepth = searchDepth;
        displayFrame = new DisplayFrame(FEN);
        displayBoard = displayFrame.getDisplayBoard();

    }

    @Override
    public Move decideMove() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FEN = board.generateFEN();
        displayBoard.updateBoard(FEN);
        while (currentSearchDepth < (searchDepth + 1)){
            iterateDepth(currentSearchDepth);
            currentSearchDepth++;
        }
        movesNodes.add(moveNodes);
        nodes += moveNodes;
        moveNodes = 0;
        currentSearchDepth = 1;
        return globalBest;
    }

    private int iterateDepth(int depth) {
        int depthScore = maximizer(depth,Integer.MIN_VALUE, Integer.MAX_VALUE);
        globalBest = best;
        return depthScore;
    }

    private int maximizer(int depth, int alpha, int beta) {
        if (board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
        if (depth == 0) {

            //switchColor();
            int q = quiescenceSearch(currentSearchDepth, alpha, beta);
            //switchColor();
            return q;
        }
        moveNodes++;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        if (currentSearchDepth > 1 && depth == currentSearchDepth){
            allMoves.remove(globalBest);
            allMoves.add(0,globalBest);
        }
        for (Move move : allMoves) {
            makeMove(move);
            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            //displayMove(move,700);
            switchColor();
            int rating = minimizer(depth-1, alpha, beta);
            switchColor();
            unmakeMove(move);
            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            //displayMove(move,700);
            if (rating > alpha){
                alpha = rating;
                if (depth == currentSearchDepth) best = move;
            }
            if (alpha >= beta) return alpha;
        }
        return alpha;
    }

    private int minimizer(int depth, int alpha, int beta) {
        if (board.lostGame(true) || board.lostGame(false))return evaluationFunction.evaluate(isEvaluationBlue, currentSearchDepth - depth);
        if (depth == 0) {
            //switchColor();
            int q = quiescenceSearch(currentSearchDepth, alpha, beta);
            //switchColor();
            return q;
        }
        moveNodes++;
        List<Move> allMoves = moveGenerator.generateMoves(isBlue());
        for (Move move : allMoves) {
            makeMove(move);
            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            //displayMove(move,700);
            switchColor();
            int rating = maximizer(depth-1, alpha, beta);
            switchColor();
            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
            //displayMove(move,700);
            unmakeMove(move);
            if (rating < beta) beta = rating;
            if (alpha >= beta) return beta;
        }
        return beta;
    }

    private int quiescenceSearch(int depth, int alpha, int beta) {
        if (board.lostGame(true) || board.lostGame(false) || (depth - currentSearchDepth) >= (currentSearchDepth))return evaluationFunction.evaluate(isEvaluationBlue,depth);
        moveNodes++;
        int standPat = evaluationFunction.evaluate(isEvaluationBlue, depth);
        boolean isMaximizingPlayer = (isBlue() == isEvaluationBlue);
        if (isMaximizingPlayer) {
            if (standPat >= beta) return beta;
            if (alpha < standPat) alpha = standPat;
        } else {
            if (standPat <= alpha) return alpha;
            if (beta > standPat) beta = standPat;
        }
        List<Move> loudMoves = moveGenerator.generateLoudMoves(isBlue());
        for (Move move : loudMoves) {
            makeMove(move);
            //System.out.println("!!!!!!!!!!!!!!!!!!!!");
            displayMove(move,700);
            switchColor();
            int score = quiescenceSearch(depth + 1 ,alpha, beta);
            switchColor();
            unmakeMove(move);
            //System.out.println("!!!!!!!!!!!!!!!!!!!!");
            displayMove(move,700);
            if (isMaximizingPlayer) {
                if (score >= beta) return beta;
                if (score > alpha) alpha = score;
            } else {
                if (score <= alpha) return alpha;
                if (score < beta) beta = score;
            }
        }
        return isMaximizingPlayer ? alpha : beta;
    }

    public void colorOldMove(boolean isBlue, Move move){
        displayBoard.displaySquare[oldMoveInitial].returnOldColor();
        displayBoard.displaySquare[oldMoveFirst].returnOldColor();
        displayBoard.displaySquare[oldMoveSecond].returnOldColor();
        int initial = move.getInitialLocation(isBlue);
        oldMoveInitial = (short) initial;
        if (move.isTargetEnemy()){
            oldMoveFirst = board.sacrificingMovesLocation(isBlue, initial,move.getDirection());
            displayBoard.displaySquare[oldMoveFirst].changeColor(Color.orange);
        }
        else {
            short[] targets = board.normalMovesLocation(isBlue, initial,move.getDirection());
            oldMoveFirst = targets[0];
            oldMoveSecond = targets[1];
            displayBoard.displaySquare[oldMoveSecond].changeColor(Color.orange);
        }
        displayBoard.displaySquare[oldMoveFirst].changeColor(Color.orange);
        displayBoard.displaySquare[oldMoveInitial].changeColor(Color.BLUE);

    }

    public void displayMove(Move move, long timer){
        try {
            Thread.sleep(timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FEN = board.generateFEN();
        colorOldMove(isBlue(), move);
        displayBoard.updateBoard(FEN);
    }
}
