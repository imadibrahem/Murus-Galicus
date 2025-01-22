package model.evolutionTheory.chromosome;

import java.util.Random;

public abstract class IntegerSingleValueChromosome extends SingleValueChromosome{
    private final int upperLimit;
    private final int lowerLimit;
    public int value;
    Random random = new Random();

    public IntegerSingleValueChromosome(int upperLimit, int lowerLimit, float mutationRate) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.mutationRate = mutationRate;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public float getMutationRate() {
        return mutationRate;
    }

    public int getValue() {
        return value;
    }

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void produceValue (){
        this.value = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
    }

    public void highLevelMutation(){
        this.value = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
    }

    public void fixedMutation(int amount){
        this.value += amount;
        if (value > upperLimit) value = upperLimit;
        else if (value < lowerLimit) value = lowerLimit;
    }

    public void rangeMutation(int range){
        int amount = random.nextInt(2 * range + 1) - range;
        this.value += amount;
        if (value > upperLimit) value = upperLimit;
        else if (value < lowerLimit) value = lowerLimit;
    }

}
