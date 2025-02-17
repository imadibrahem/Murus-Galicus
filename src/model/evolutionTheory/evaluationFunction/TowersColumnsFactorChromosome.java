package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerNormalArrayValueChromosome;

import java.io.Serial;
import java.io.Serializable;

public class TowersColumnsFactorChromosome extends IntegerNormalArrayValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    public TowersColumnsFactorChromosome() {
        super(20, -4, 4, 0.1f);
        produceValue();
    }

    public TowersColumnsFactorChromosome(float mutationRate) {
        super(20, -4, 4, mutationRate);
    }

    @Override
    public void mutate() {
        System.out.println("mutation for Chromosome #3 is being applied ");
        System.out.println(this);
        float mutationType, amountSign, highLevelMutationChance, rangeMutationChance;

        if (isExploration()){
            highLevelMutationChance = 0.9f;
            rangeMutationChance = 0.25f;
        }
        else if (isExploitation()){
            highLevelMutationChance = 0.98f;
            rangeMutationChance = 0.7f;
        }
        else {
            highLevelMutationChance = 0.95f;
            rangeMutationChance = 0.45f;
        }
        for (int i = 0; i < value.length; i++){
            System.out.print("mutation for Gene #" + i + " is being applied: ");
            mutationType = random.nextFloat();
            if (mutationType > highLevelMutationChance){
                System.out.print("applying High level mutation | ");
                value[i] = highLevelMutation();
            }
            else if (mutationType > rangeMutationChance){
                System.out.print("applying range mutation | ");
                int range = Math.max(1, (int) (upperLimit * mutationRate));
                value[i] = rangeMutation(value[i], range);
            }
            else {
                System.out.print("applying fixed mutation | ");
                int amount = Math.max(1, (int) (value[i] * mutationRate));
                amountSign = random.nextFloat();
                if (amountSign < 0.5) amount *= -1;
                value[i] = fixedMutation(value[i],amount);
            }
        }
        System.out.println();
        System.out.println(this);
    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        Chromosome[] offspring = new Chromosome[2];
        float randomIndex;
        Chromosome firstChild = new TowersColumnsFactorChromosome(mutationRate);
        Chromosome secondChild = new TowersColumnsFactorChromosome(mutationRate);
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
            if (randomIndex < mutationRate) offspring[i].mutate();
            System.out.println();
        }
        return offspring;
    }
}
