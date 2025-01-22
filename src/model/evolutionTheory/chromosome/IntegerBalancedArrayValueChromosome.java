package model.evolutionTheory.chromosome;

import java.util.Random;

public abstract class IntegerBalancedArrayValueChromosome extends BalancedArrayValueChromosome{
    protected final int upperLimit;
    protected final int lowerLimit;
    protected final int length;
    public int [] value;
    protected Random random = new Random();


    protected IntegerBalancedArrayValueChromosome(int upperLimit, int lowerLimit, int length, float mutationRate) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.length = length;
        this.mutationRate = mutationRate;
        this.value = new int[length];
        for (int i = 0; i < length; i++){
            value[i] = 0;
        }
    }
    public int getUpperLimit() {
        return upperLimit;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public int getLength() {
        return length;
    }

    public float getMutationRate() {
        return mutationRate;
    }

    public int[] getValue() {
        return value;
    }

    public void setValue(int[] value) {
        this.value = value;
    }

}
