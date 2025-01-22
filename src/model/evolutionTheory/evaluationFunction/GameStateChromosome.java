package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerBalancedArrayValueChromosome;

public class GameStateChromosome extends IntegerBalancedArrayValueChromosome {

    public GameStateChromosome() {
        super(1000, 0, 8, 0.1f);
    }

    public GameStateChromosome(float mutationRate) {
        super(1000, 0, 8, mutationRate);
    }

    @Override
    public void mutate() {

    }

    @Override
    public void produceValue() {
        int positiveValue = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
        value[7] = random.nextInt(positiveValue);
        value[0] = -value[7];
        positiveValue -= value[7];
        value[6] = random.nextInt(positiveValue);
        value[1] = -value[6];
        value[5] = positiveValue - value[6];
        value[3] = -value[5];
    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        return new Chromosome[0];
    }
}
