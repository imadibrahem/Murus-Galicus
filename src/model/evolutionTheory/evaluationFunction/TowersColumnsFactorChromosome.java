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
        float mutationType;
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
        for (int i = 0; i < value.length; i++){
            mutationType = random.nextFloat();
            if (mutationType > highLevelMutationChance) value[i] = highLevelMutation();
            else if (mutationType > rangeMutationChance) value[i] = rangeMutation(value[i], (int) (upperLimit * mutationRate));
            else {
                int amount = (int) (value[i] * mutationRate);
                amountSign = random.nextFloat();
                if (amountSign < 0.5) amount *= -1;
                value[i] = fixedMutation(value[i],amount);
            }
        }
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
            randomIndex = random.nextFloat();
            if (isExploration()) offspring[i].setExploration(true);
            else if (isExploitation()) offspring[i].setExploitation(true);
            if (randomIndex < mutationRate) offspring[i].mutate();
        }
        return offspring;
    }
}
