package model.evolutionTheory.chromosome;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public abstract class BooleanValueChromosome extends SingleValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    protected Random random = new Random();

    public BooleanValueChromosome(float mutationRate) {
        this.mutationRate = mutationRate;
        this.value = new int[1];
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
    public void produceValue() {
        float randomIndex = random.nextFloat();
        if (randomIndex < 0.5){
            this.value[0] = 0;
        }
        else{
            this.value[0] = 1;
        }
    }

    public boolean isValueTrue() {
        return value[0] == 1;
    }
}
