package model.evolutionTheory.chromosome;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public abstract class Chromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    protected boolean exploration = false;
    protected boolean exploitation = false;
    protected float mutationRate;
    public int[] value;


    public abstract void mutate();

    public abstract void produceValue();

    public abstract Chromosome[] crossover(Chromosome partner);

    public void explore(){
        exploration = true;
        exploitation = false;
    }

    public void exploit(){
        exploration = false;
        exploitation = true;
    }

    public void normalMode(){
        exploration = false;
        exploitation = false;
    }

    public int[] getValue() {
        return value;
    }

    public void setExploration(boolean exploration) {
        this.exploration = exploration;
    }

    public void setExploitation(boolean exploitation) {
        this.exploitation = exploitation;
    }

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public boolean isExploration() {
        return exploration;
    }

    public boolean isExploitation() {
        return exploitation;
    }

    public float getMutationRate() {
        return mutationRate;
    }

    @Override
    public String toString() {
        return Arrays.toString(value);
    }
}
