package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerSingleValueChromosome;

public class IsolatedTowersFactorChromosome extends IntegerSingleValueChromosome {

    public IsolatedTowersFactorChromosome() {
        super(100, 0, 0.1f);
    }

    public IsolatedTowersFactorChromosome(float mutationRate) {
        super(100, 0, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
