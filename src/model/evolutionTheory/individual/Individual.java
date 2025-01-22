package model.evolutionTheory.individual;

import model.evolutionTheory.chromosome.Chromosome;

public abstract class Individual {
    protected boolean exploration = false;
    protected boolean exploitation = false;
    protected float mutationRate;
    public Chromosome[] genome;

    public abstract void mutate();

    public abstract void produceChromosomes();

    public abstract Individual[] crossover(Individual partner);

    public void explore(){
        exploration = true;
        exploitation = false;
        for (Chromosome chromosome: genome) chromosome.explore();
    }

    public void exploit(){
        exploration = false;
        exploitation = true;
        for (Chromosome chromosome: genome) chromosome.exploit();
    }

    public void normalMode(){
        exploration = false;
        exploitation = false;
        for (Chromosome chromosome: genome) chromosome.normalMode();
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
}
