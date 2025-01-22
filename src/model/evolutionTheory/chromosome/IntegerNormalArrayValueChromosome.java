package model.evolutionTheory.chromosome;

import java.util.Random;

public abstract class IntegerNormalArrayValueChromosome extends NormalArrayValueChromosome{
    protected final int upperLimit;
    protected final int lowerLimit;
    protected final int length;
    protected float mutationRate;
    public int [] value;
    protected Random random = new Random();

    public IntegerNormalArrayValueChromosome(int upperLimit, int lowerLimit, int length, float mutationRate) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.length = length;
        this.mutationRate = mutationRate;
        this.value = new int[length];
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

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setValue(int[] value) {
        this.value = value;
    }

    @Override
    public void produceValue (){
        for (int i = 0; i < length; i++){
            value[i] = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
        }
    }

    public int highLevelMutation(){
        int gene = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
        if (gene > upperLimit) gene = upperLimit;
        else if (gene < lowerLimit) gene = lowerLimit;
        return gene;
    }

    public int fixedMutation(int amount, int gene){
        gene += amount;
        if (gene > upperLimit) gene = upperLimit;
        else if (gene < lowerLimit) gene = lowerLimit;
        return gene;
    }

    public int rangeMutation(int range, int gene){
        int amount = random.nextInt(2 * range + 1) - range;
        gene += amount;
        if (gene > upperLimit) gene = upperLimit;
        else if (gene < lowerLimit) gene = lowerLimit;
        return gene;
    }
}
