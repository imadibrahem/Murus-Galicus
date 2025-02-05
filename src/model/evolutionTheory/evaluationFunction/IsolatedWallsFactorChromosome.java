package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.chromosome.IntegerSingleValueChromosome;

import java.io.Serial;
import java.io.Serializable;

public class IsolatedWallsFactorChromosome extends IntegerSingleValueChromosome implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    public IsolatedWallsFactorChromosome() {
        super(75, 0, 0.1f);
        produceValue();

    }

    public IsolatedWallsFactorChromosome(float mutationRate) {
        super(75, 0, mutationRate);
    }

    @Override
    public void mutate() {
        float mutationType = random.nextFloat();
        float amountSign;
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
        else if (mutationType > rangeMutationChance) rangeMutation((int) (upperLimit * mutationRate));
        else {
            int amount = (int) (value[0] * mutationRate);
            amountSign = random.nextFloat();
            if (amountSign < 0.5) amount *= -1;
            fixedMutation(amount);
        }
    }

    @Override
    public Chromosome[] crossover(Chromosome partner) {
        Chromosome[] offspring = new Chromosome[2];
        int firstParentValue;
        int secondParentValue;
        int firstChildValue;
        int secondChildValue;
        float mutationChance;
        Chromosome firstChild = new IsolatedWallsFactorChromosome(mutationRate);
        Chromosome secondChild = new IsolatedWallsFactorChromosome(mutationRate);
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
            mutationChance = random.nextFloat();
            if (isExploration()) offspring[i].setExploration(true);
            else if (isExploitation()) offspring[i].setExploitation(true);
            if (mutationChance < mutationRate) offspring[i].mutate();
        }
        return offspring;
    }
}
