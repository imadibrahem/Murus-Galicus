package model.evolutionTheory.chromosome;

import java.util.Random;

public abstract class IntegerNormalArrayValueChromosome extends NormalArrayValueChromosome{
    private int upperLimit;
    private int lowerLimit;
    private int length;
    private float mutationRate;
    public int [] value;
    Random random = new Random();

    public IntegerNormalArrayValueChromosome(int upperLimit, int lowerLimit, int length, float mutationRate) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.length = length;
        this.mutationRate = mutationRate;
        this.value = new int[length];
        for (int i = 0; i < length; i++){
            value[i] = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
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

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setValue(int[] value) {
        this.value = value;
    }

    public int highLevelMutation(){
         return random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
    }


    public int rangeMutation(int range){
        return random.nextInt(2 * range + 1) - range;
    }

}
