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

    public TimeManager(double totalTime) {
        this.totalTime = totalTime;
        this.peakMove = 7;
        this.earlyGameMoves = peakMove * 2;
        this.midGameMoves = 16;
        this.earlyFactor = 0.75;
        this.midFactor = 0.2;
        this.endFactor = 0.2;
        this.moveTimes = new double[midGameMoves + earlyGameMoves];
        this.remainingTime = totalTime;
        distributeTime();
    }

    private void distributeTime() {
        double earlyTime = totalTime * earlyFactor;
        //System.out.println("Early time: " + earlyTime);
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
            //System.out.println("Early Move:" + i + " Time: " + moveTimes[i]);
        }
        // Allocate midFactor for midGameMoves  (equal distribution)
        //System.out.println("+++++++++++++++++++++++++++++++++");
        double midGameTime = totalTime * midFactor;
        //System.out.println("Mid time: " + midGameTime);
        double midMoveTime = midGameTime / midGameMoves;
        //System.out.println("midMoveTime: " + midMoveTime);
        //System.out.println("+++++++++++++++++++++++++++++++++");
        for (int i = earlyGameMoves; i < (earlyGameMoves + midGameMoves); i++) {
            moveTimes[i] = midMoveTime;
            //System.out.println("Mid Move:" + i + " Time: " + moveTimes[i]);
        }
        //System.out.println("+++++++++++++++++++++++++++++++++");
        //System.out.println("+++++++++++++++++++++++++++++++++");
    }

    public double getTimeForMove(int moveNumber) {
        double usedTime;
        if (moveNumber < moveTimes.length && moveTimes[moveNumber] > 0) {
            usedTime = Math.min(moveTimes[moveNumber], remainingTime);
            return usedTime;
        }
        usedTime = remainingTime * endFactor; // Panic mode: endFactor of remaining time
        return usedTime;
    }

    public void updateRemainingTime(double usedTime) {
        remainingTime -= usedTime;
        if (remainingTime < 0) remainingTime = 0;
    }

    public static void main (String[] args){
        TimeManager timeManager = new TimeManager(120000, 7, 16, 0.75, 0.2, 0.2);
        int i = 0;
        System.out.println("++++++++++++++++++++++++++++");
        while (timeManager.remainingTime > 0.000001){
            System.out.println("Move:" + i +" Time:" + timeManager.getTimeForMove(i) + " remainingTime:" + timeManager.remainingTime);
            i++;
        }
        System.out.println("++++++++++++++++++++++++++++");
        TimeManager timeManager2 = new TimeManager(120000);
        int j = 0;
        System.out.println("++++++++++++++++++++++++++++");
        while (timeManager2.remainingTime > 0.000001){
            System.out.println("Move:" + j +" Time:" + timeManager2.getTimeForMove(j) + " remainingTime:" + timeManager2.remainingTime);
            j++;
        }
    }
}
