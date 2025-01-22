package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerNormalArrayValueChromosome;

public class TowersRatioFactorChromosome extends IntegerNormalArrayValueChromosome {

    public TowersRatioFactorChromosome() {
        super(20, -5, 8, 0.1f);
    }

    public TowersRatioFactorChromosome(float mutationRate) {
        super(20, -5, 8, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
