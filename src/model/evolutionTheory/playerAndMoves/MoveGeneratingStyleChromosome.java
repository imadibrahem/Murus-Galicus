package model.evolutionTheory.playerAndMoves;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerSingleValueChromosome;
import model.evolutionTheory.evaluationFunction.IsolatedWallsFactorChromosome;

import java.io.Serial;
import java.io.Serializable;

public class MoveGeneratingStyleChromosome extends IntegerSingleValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    public MoveGeneratingStyleChromosome() {
        super(5, 0, 0.1f);
        produceValue();
    }

    public MoveGeneratingStyleChromosome(float mutationRate) {
        super(5, 0, mutationRate);
    }
    @Override
    public void mutate() {
        System.out.println("mutation for Chromosome #?? is being applied ");
        System.out.println(this);
        produceValue();
        System.out.println(this);
    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        Chromosome[] offspring = new Chromosome[2];
        int firstParentValue = value[0];
        int secondParentValue = partner.value[0];
        float mutationChance;
        Chromosome firstChild = new MoveGeneratingStyleChromosome(mutationRate);
        Chromosome secondChild = new MoveGeneratingStyleChromosome(mutationRate);
        firstChild.value[0] = firstParentValue;
        secondChild.value[0] = secondParentValue;
        float order = random.nextFloat();
        if (order < 0.5) {
            offspring[0] = firstChild;
            offspring[1] = secondChild;
        }
        else {
            offspring[1] = firstChild;
            offspring[0] = secondChild;
        }
        for (int i = 0; i < 2; i++){
            System.out.println("offspring #" + i + " mode..");
            mutationChance = random.nextFloat();
            if (isExploration()) offspring[i].setExploration(true);
            else if (isExploitation()) offspring[i].setExploitation(true);
            System.out.println("checking for mutation..");
            System.out.println("Chromosome mutation chance is: " + mutationChance + " mutation rate is: " + mutationRate);
            if (mutationChance < mutationRate){
                offspring[i].mutate();
            }
            System.out.println();
        }
        return offspring;
    }
}
