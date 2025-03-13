package model.evolutionTheory.chromosome;

import java.io.Serial;
import java.util.Random;

public abstract class IntegerPermutationArrayValueChromosome extends PermutationArrayValueChromosome{
    @Serial
    protected static final long serialVersionUID = 1L;
    protected final int length;
    protected float mutationRate;
    protected final int lowerLimit;
    protected Random random = new Random();

    protected IntegerPermutationArrayValueChromosome(int lowerLimit, int length, float mutationRate) {
        this.length = length;
        this.mutationRate = mutationRate;
        this.lowerLimit = lowerLimit;
        this.value = new int[length];
    }
    public float getMutationRate() {
        return mutationRate;
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
            value[i] = lowerLimit + i;
        }
        for (int i = length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = value[i];
            value[i] = value[j];
            value[j] = temp;
        }
    }

    public void highLevelMutation(){
        produceValue();
    }

    public void swapNext(){
        int i = random.nextInt(length - 1);
        int temp = value[i];
        int nextValue = value[i + 1];
        value[i] = nextValue;
        value[i + 1] = temp;
    }

    public void swap(){
        int i = random.nextInt(length - 1);
        int j = random.nextInt(length - 1);
        int temp = value[i];
        int temp2 = value[j];
        value[i] = temp2;
        value[j] = temp;
    }
}
