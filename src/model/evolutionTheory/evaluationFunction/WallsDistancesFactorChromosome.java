package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerNormalArrayValueChromosome;

public class WallsDistancesFactorChromosome extends IntegerNormalArrayValueChromosome {

    public WallsDistancesFactorChromosome() {
        super(15, 0, 7, 0.1f);
    }

    public WallsDistancesFactorChromosome(float mutationRate) {
        super(15, 0, 7, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
