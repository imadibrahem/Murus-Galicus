package model.evolutionTheory.playerAndMoves;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerPermutationArrayValueChromosome;

import java.io.Serial;
import java.io.Serializable;

public class KillerSortChromosome extends IntegerPermutationArrayValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    public KillerSortChromosome(){
        super(0, 6, 0.1f);
        produceValue();
    }
    public KillerSortChromosome(float mutationRate) {
        super(0, 6, mutationRate);
    }

    @Override
    public void mutate() {
        System.out.println("mutation for Chromosome #?? is being applied ");
        System.out.println(this);
        float mutationType = random.nextFloat();
        float highLevelMutationChance, swapMutationChance;

        if (isExploration()){
            highLevelMutationChance = 0.9f;
            swapMutationChance = 0.25f;
        }
        else if (isExploitation()){
            highLevelMutationChance = 0.98f;
            swapMutationChance = 0.7f;
        }
        else {
            highLevelMutationChance = 0.95f;
            swapMutationChance = 0.45f;
        }
        if (mutationType > highLevelMutationChance){
            System.out.println("applying High level mutation");
            highLevelMutation();
        }
        else if (mutationType > swapMutationChance){
            System.out.println("applying Swap mutation");
            swap();
        }
        else {
            System.out.println("applying Swap next mutation");
            swapNext();
        }
        System.out.println(this);
    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        Chromosome[] offspring = new Chromosome[2];
        float randomIndex;
        Chromosome firstChild = new KillerSortChromosome(mutationRate);
        Chromosome secondChild = new KillerSortChromosome(mutationRate);
        for (int i = 0; i < length; i++){
            randomIndex = random.nextFloat();
            if (randomIndex < 0.5){
                firstChild.value[i] = value[i];
                secondChild.value[i] = partner.value[i];
            }
            else {
                firstChild.value[i] = partner.value[i];
                secondChild.value[i] = value[i];
            }
        }
        offspring[0] = firstChild;
        offspring[1] = secondChild;
        for (int i = 0; i < 2; i++){
            System.out.println("offspring #" + i + " mode..");
            randomIndex = random.nextFloat();
            if (isExploration()) offspring[i].setExploration(true);
            else if (isExploitation()) offspring[i].setExploitation(true);
            System.out.println("checking for mutation..");
            System.out.println("Chromosome mutation chance is: " + randomIndex + " mutation rate is: " + mutationRate);
            if (randomIndex < mutationRate){
                offspring[i].mutate();
            }
            System.out.println();
        }
        return offspring;
    }
}

