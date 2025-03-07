package model.evolutionTheory.evaluationFunction.individual;

import model.evolutionTheory.chromosome.Chromosome;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public abstract class Individual implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    protected boolean exploration = false;
    protected boolean exploitation = false;
    protected float mutationRate;
    protected int genomeLength;
    protected Random random = new Random();

    public Chromosome[] genome;

    public abstract void mutate();

    public abstract void produceChromosomes();

    public abstract Individual[] crossover(Individual partner);

    public void explore(){
        exploration = true;
        exploitation = false;
        mutationRate *= 1.2;
        for (Chromosome chromosome: genome){
            chromosome.explore();
            chromosome.setMutationRate(mutationRate);
        }
    }

    public void exploit(){
        exploration = false;
        exploitation = true;
        mutationRate *= 0.8;
        for (Chromosome chromosome: genome){
            chromosome.exploit();
            chromosome.setMutationRate(mutationRate);

        }
    }

    public void normalMode(){
        exploration = false;
        exploitation = false;
        mutationRate = 0.1f;
        for (Chromosome chromosome: genome){
            chromosome.normalMode();
            chromosome.setMutationRate(0.1f);
        }
    }

    public void setGenome(Chromosome[] genome) {
        this.genome = genome;
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

    public void setExploration(boolean exploration) {
        this.exploration = exploration;
    }

    public void setExploitation(boolean exploitation) {
        this.exploitation = exploitation;
    }

    @Override
    public String toString() {
        return "Individual{" +
                "exploration=" + exploration +
                ", exploitation=" + exploitation +
                ", mutationRate=" + mutationRate +
                ", genome=" + Arrays.toString(genome) +
                '}';
    }
}
