package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerSingleValueChromosome;

public class IsolatedWallsFactorChromosome extends IntegerSingleValueChromosome {

    public IsolatedWallsFactorChromosome() {
        super(75, 0, 0.1f);
    }

    public IsolatedWallsFactorChromosome(float mutationRate) {
        super(75, 0, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
