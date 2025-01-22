package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerNormalArrayValueChromosome;

public class TowersColumnsFactorChromosome extends IntegerNormalArrayValueChromosome {

    public TowersColumnsFactorChromosome() {
        super(20, -4, 4, 0.1f);
    }

    public TowersColumnsFactorChromosome(float mutationRate) {
        super(20, -4, 4, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
