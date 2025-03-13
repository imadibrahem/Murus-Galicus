package model.evolutionTheory.playerAndMoves;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerSingleValueChromosome;

import java.io.Serial;
import java.io.Serializable;

public class TowersFactorChromosome extends IntegerSingleValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    public TowersFactorChromosome(){
        super(20, -20, 0.1f);
        produceValue();
    }
    public TowersFactorChromosome(float mutationRate) {
        super(20, -20, mutationRate);
    }

    @Override
    public void mutate() {
        System.out.println("mutation for Chromosome #?? is being applied ");
        System.out.println(this);
        float mutationType = random.nextFloat();
        float amountSign, highLevelMutationChance, rangeMutationChance;

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
        if (mutationType > highLevelMutationChance){
            System.out.println("applying High level mutation");
            highLevelMutation();
        }
        else if (mutationType > rangeMutationChance){
            System.out.println("applying range mutation");
            int amount = Math.max(1, (int) (upperLimit * mutationRate));
            rangeMutation(amount);
        }
        else {
            System.out.println("applying fixed mutation");
            int amount = Math.max(1, (int) (value[0] * mutationRate));
            amountSign = random.nextFloat();
            if (amountSign < 0.5) amount *= -1;
            fixedMutation(amount);
        }
        System.out.println(this );
    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        Chromosome[] offspring = new Chromosome[2];
        int firstParentValue;
        int secondParentValue;
        int firstChildValue;
        int secondChildValue;
        float mutationChance;
        Chromosome firstChild = new TowersFactorChromosome(mutationRate);
        Chromosome secondChild = new TowersFactorChromosome(mutationRate);
        if (value[0] > partner.value[0]){
            firstParentValue = partner.value[0];
            secondParentValue = value[0];
        }
        else {
            firstParentValue = value[0];
            secondParentValue = partner.value[0];
        }
        int difference = secondParentValue - firstParentValue;
        firstChildValue = firstParentValue + (difference / 3);
        secondChildValue = secondParentValue - (difference / 3);
        firstChild.value[0] = firstChildValue;
        secondChild.value[0] = secondChildValue;
        offspring[0] = firstChild;
        offspring[1] = secondChild;
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
