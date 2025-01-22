package model.evolutionTheory.chromosome;

public abstract class Chromosome {
    protected boolean exploration = false;
    protected boolean exploitation = false;
    protected float mutationRate;

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
}
