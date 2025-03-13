package model.evolutionTheory.playerAndMoves;

import model.evolutionTheory.chromosome.BooleanValueChromosome;
import model.evolutionTheory.chromosome.Chromosome;

import java.io.Serial;
import java.io.Serializable;

public class MoveGeneratingFrontToBackBooleanChromosome extends BooleanValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    public MoveGeneratingFrontToBackBooleanChromosome(){
        super(0.1f);
        produceValue();
    }
    public MoveGeneratingFrontToBackBooleanChromosome(float mutationRate) {
        super(mutationRate);
    }

    @Override
    public void mutate() {
        System.out.println("mutation for Chromosome #03 is being applied ");
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
        Chromosome firstChild = new MoveGeneratingFrontToBackBooleanChromosome(mutationRate);
        Chromosome secondChild = new MoveGeneratingFrontToBackBooleanChromosome(mutationRate);
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
