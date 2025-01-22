package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerSingleValueChromosome;

public class MobilityFactorChromosome extends IntegerSingleValueChromosome {

    public MobilityFactorChromosome() {
        super(40, 0, 0.1f);
    }

    public MobilityFactorChromosome(float mutationRate) {
        super(40, 0, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
