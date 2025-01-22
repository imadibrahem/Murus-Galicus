package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerNormalArrayValueChromosome;

public class TowersDistancesFactorChromosome extends IntegerNormalArrayValueChromosome {

    public TowersDistancesFactorChromosome() {
        super(20, 0, 7, 0.1f);
    }

    public TowersDistancesFactorChromosome(float mutationRate) {
        super(20, 0, 7, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
