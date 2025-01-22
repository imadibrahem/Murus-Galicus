package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerNormalArrayValueChromosome;

public class WallsColumnsFactorChromosome extends IntegerNormalArrayValueChromosome {

    public WallsColumnsFactorChromosome() {
        super(15, -2, 4, 0.1f);
    }

    public WallsColumnsFactorChromosome(float mutationRate) {
        super(15, -2, 4, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
