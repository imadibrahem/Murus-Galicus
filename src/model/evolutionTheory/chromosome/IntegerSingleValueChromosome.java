package model.evolutionTheory.chromosome;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public abstract class IntegerSingleValueChromosome extends SingleValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    protected final int upperLimit;
    protected final int lowerLimit;
    protected Random random = new Random();

    public IntegerSingleValueChromosome(int upperLimit, int lowerLimit, float mutationRate) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.mutationRate = mutationRate;
        this.value = new int[1];
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

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public void setValue(int value) {
        this.value[0] = value;
    }

    @Override
    public void produceValue (){
        this.value[0] = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
    }

    public void highLevelMutation(){
        this.value[0] = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
    }

    public void fixedMutation(int amount){
        this.value[0] += amount;
        if (value[0] > upperLimit) value[0] = upperLimit;
        else if (value[0] < lowerLimit) value[0] = lowerLimit;
    }

    public void rangeMutation(int range){
        if (range < 0) range *= -1;
        int amount = random.nextInt(2 * range + 1) - range;
        if (amount != 0){
            this.value[0] += amount;
            if (value[0] > upperLimit) value[0] = upperLimit;
            else if (value[0] < lowerLimit) value[0] = lowerLimit;
        }
        else rangeMutation(range);
    }

}
