package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerBalancedArrayValueChromosome;

import java.io.Serial;
import java.io.Serializable;

public class GameStateChromosome extends IntegerBalancedArrayValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    public GameStateChromosome() {
        super(1000, 0, 8, 0.1f);
        produceValue();

    }

    public GameStateChromosome(float mutationRate) {
        super(1000, 0, 8, mutationRate);
    }

    @Override
    public void mutate() {
        System.out.println("mutation for Chromosome #5 is being applied ");
        System.out.println(this);
        float mutationType = random.nextFloat();
        float highLevelMutationChance;
        float rangeMutationChance;
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
        if (mutationType > highLevelMutationChance) highLevelMutation();
        else if (mutationType > rangeMutationChance) rangeMutation();
        else fixedMutation();
        System.out.println(this);
    }

    @Override
    public void produceValue() {
        int positiveValue = random.nextInt(upperLimit - lowerLimit + 1) + lowerLimit;
        value[7] = positiveValue > 1 ? random.nextInt(positiveValue) : random.nextInt(positiveValue + 1);
        value[0] = -value[7];
        positiveValue -= value[7];
        if (positiveValue < 1) positiveValue = 1;
        value[6] = random.nextInt(positiveValue);
        value[1] = -value[6];
        value[5] = positiveValue - value[6];
        value[3] = -value[5];
    }
    public void highLevelMutation(){
        System.out.println("applying High level mutation");
        produceValue();
    }

    public void fixedMutation(){
        System.out.println("applying fixed mutation");
        int positiveValue = value[7] + value[6] + value[5];
        float amountSign = random.nextFloat();
        int amount = (int) (positiveValue * mutationRate);
        if (amountSign < 0.5) amount *= -1;
        positiveValue += amount;
        if (positiveValue > upperLimit) amount = upperLimit - (value[7] + value[6] + value[5]);
        else if (positiveValue < lowerLimit) amount = -((value[7] + value[6] + value[5]) - lowerLimit);
        value[7] += (amount/3);
        value[6] += (amount/3);
        value[5] += (amount/3);
        value[0] -= (amount/3);
        value[1] -= (amount/3);
        value[3] -= (amount/3);
    }

    public void rangeMutation(){
        System.out.println("applying range mutation");
        int positiveValue = value[7] + value[6] + value[5];
        int difference = upperLimit - positiveValue > lowerLimit ? upperLimit - positiveValue : lowerLimit + 1;
        int difference_2 = positiveValue - lowerLimit < upperLimit ? positiveValue - lowerLimit : upperLimit - 1;
        if (difference_2 < difference) difference = difference_2;
        int range = difference < positiveValue ? (int) ((difference / 3) * mutationRate) : (int) ((positiveValue / 3) * mutationRate);
        int amount_1, amount_2, amount_3;
        int currentPositiveValue;

        int maxRetries = 100;
        int attempts = 0;
        boolean success = false;
        while(true) {
            amount_1 = random.nextInt(2 * range + 1) - range;
            amount_2 = random.nextInt(2 * range + 1) - range;
            amount_3 = random.nextInt(2 * range + 1) - range;
            currentPositiveValue = positiveValue + amount_1 + amount_2 + amount_3;
            attempts++;
            if (currentPositiveValue >= upperLimit &&  currentPositiveValue <= lowerLimit){
                success = true;
                break;
            }
            if (attempts >= maxRetries) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("Warning: rangeMutation reached max retries! applying highLevelMutation");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                break;
            }
        }
        if (success){
            value[7] += amount_1;
            value[6] += amount_2;
            value[5] += amount_3;
            value[0] -= amount_1;
            value[1] -= amount_2;
            value[3] -= amount_3;
        }
        else {
            highLevelMutation();
        }
    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        Chromosome[] offspring = new Chromosome[2];
        float randomIndex;
        int difference;
        Chromosome firstChild = new GameStateChromosome(mutationRate);
        Chromosome secondChild = new GameStateChromosome(mutationRate);
        for (int i = 5; i < 8; i++){
            difference = value[i] > partner.value[i] ? value[i] - partner.value[i] : partner.value[i] -  value[i];
            if (value[i] > partner.value[i]){
                firstChild.value[i] = value[i] - (difference / 3);
                secondChild.value[i] = partner.value[i] + (difference / 3);
            }
            else {
                firstChild.value[i] = value[i] + (difference / 3);
                secondChild.value[i] = partner.value[i] - (difference / 3);
            }
        }
        firstChild.value[0] = - firstChild.value[7];
        secondChild.value[0] = - secondChild.value[7];
        firstChild.value[1] = - firstChild.value[6];
        secondChild.value[1] = - secondChild.value[6];
        firstChild.value[3] = - firstChild.value[5];
        secondChild.value[3] = - secondChild.value[5];
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
