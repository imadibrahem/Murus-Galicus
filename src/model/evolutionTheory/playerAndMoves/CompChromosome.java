package model.evolutionTheory.playerAndMoves;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerPermutationArrayValueChromosome;
import model.evolutionTheory.evaluationFunction.WallsDistancesFactorChromosome;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CompChromosome extends IntegerPermutationArrayValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    public CompChromosome(){
        super(0, 3, 0.1f);
        produceValue();
    }
    public CompChromosome(float mutationRate) {
        super(0, 3, mutationRate);
    }

    @Override
    public void mutate() {
        System.out.println("mutation for Chromosome #13 is being applied ");
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
        Chromosome firstChild = new CompChromosome(mutationRate);
        Chromosome secondChild = new CompChromosome(mutationRate);

        int mutationPoint = random.nextInt(length);
        randomIndex = random.nextFloat();
        Set<Integer> firstChildUsed = new HashSet<>();
        Set<Integer> secondChildUsed = new HashSet<>();

        for (int i = 0; i < mutationPoint; i++) {
            if (randomIndex < 0.5) {
                firstChild.value[i] = value[i];
                secondChild.value[i] = partner.value[i];
            } else {
                firstChild.value[i] = partner.value[i];
                secondChild.value[i] = value[i];
            }
            firstChildUsed.add(firstChild.value[i]);
            secondChildUsed.add(secondChild.value[i]);
        }

        // Step 2: Fill in remaining values from the other parent in order
        int firstIndex = mutationPoint;
        int secondIndex = mutationPoint;

        for (int j = 0; j < length; j++) {
            if (randomIndex < 0.5) {
                if (!firstChildUsed.contains(partner.value[j]) && firstIndex < length) {
                    firstChild.value[firstIndex] = partner.value[j];
                    firstChildUsed.add(partner.value[j]);
                    firstIndex++;
                }
                if (!secondChildUsed.contains(value[j]) && secondIndex < length) {
                    secondChild.value[secondIndex] = value[j];
                    secondChildUsed.add(value[j]);
                    secondIndex++;
                }
            }
            else{
                if (!firstChildUsed.contains(value[j]) && firstIndex < length) {
                    firstChild.value[firstIndex] = value[j];
                    firstChildUsed.add(value[j]);
                    firstIndex++;
                }
                if (!secondChildUsed.contains(partner.value[j]) && secondIndex < length) {
                    secondChild.value[secondIndex] = partner.value[j];
                    secondChildUsed.add(partner.value[j]);
                    secondIndex++;
                }
            }
            if (firstIndex >= length && secondIndex >= length) {
                break;
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
