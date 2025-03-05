package model;

public class TimeManager {
    private final double totalTime;
    private final double[] moveTimes;
    private final int peakMove;
    private final int earlyGameMoves;
    private final int midGameMoves;
    private final double earlyFactor;
    private final double midFactor;
    private final double endFactor;
    private double remainingTime;

    public TimeManager(double totalTime, int peakMove, int midGameMoves, double earlyFactor, double midFactor, double endFactor) {
        this.totalTime = totalTime;
        this.peakMove = peakMove;
        this.earlyGameMoves = peakMove * 2;
        this.midGameMoves = midGameMoves;
        this.earlyFactor = earlyFactor;
        this.midFactor = midFactor;
        this.endFactor = endFactor;
        this.moveTimes = new double[midGameMoves + earlyGameMoves];
        this.remainingTime = totalTime;
        distributeTime();
    }

    private void distributeTime() {
        double earlyTime = totalTime * earlyFactor;
        System.out.println("Early time: " + earlyTime);
        double[] tempEarlyTimes = new double[earlyGameMoves];
        double sumEarly = 0;
        for (int i = 0; i < earlyGameMoves; i++) {
            if (i < peakMove) {
                tempEarlyTimes[i] = i + 1; // Increasing phase
            } else {
                tempEarlyTimes[i] = earlyGameMoves - i; // Decreasing phase
            }
            sumEarly += tempEarlyTimes[i];
        }
        // Normalize early move times to exactly earlyFactor of total time
        double scaleFactor = earlyTime / sumEarly;
        for (int i = 0; i < earlyGameMoves; i++) {
            moveTimes[i] = tempEarlyTimes[i] * scaleFactor;
            System.out.println("Early Move:" + i + " Time: " + moveTimes[i]);
        }
        // Allocate midFactor for midGameMoves  (equal distribution)
        System.out.println("+++++++++++++++++++++++++++++++++");
        double midGameTime = totalTime * midFactor;
        System.out.println("Mid time: " + midGameTime);
        double midMoveTime = midGameTime / midGameMoves;
        System.out.println("midMoveTime: " + midMoveTime);
        System.out.println("+++++++++++++++++++++++++++++++++");
        for (int i = earlyGameMoves; i < (earlyGameMoves + midGameMoves); i++) {
            moveTimes[i] = midMoveTime;
            System.out.println("Mid Move:" + i + " Time: " + moveTimes[i]);
        }
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++");

    }

    public double getTimeForMove(int moveNumber) {
        double usedTime;
        if (moveNumber < moveTimes.length && moveTimes[moveNumber] > 0) {
            usedTime = Math.min(moveTimes[moveNumber], remainingTime);
            updateRemainingTime(usedTime);//todo remove and replace with time from player
            return usedTime;
        }
        usedTime = remainingTime * endFactor; // Panic mode: endFactor of remaining time
        updateRemainingTime(usedTime);//todo remove and replace with time from player
        return usedTime;
    }

    public void updateRemainingTime(double usedTime) {
        remainingTime -= usedTime;
        if (remainingTime < 0) remainingTime = 0;
    }

    public static void main (String[] args){
        TimeManager timeManager = new TimeManager(6000, 7, 14, 0.75, 0.2, 0.15);
        int i = 0;
        System.out.println("++++++++++++++++++++++++++++");
        while (timeManager.remainingTime > 0.001){
            System.out.println("Move:" + i +" Time:" + timeManager.getTimeForMove(i) + " remainingTime:" + timeManager.remainingTime);
            i++;
        }
    }
}
