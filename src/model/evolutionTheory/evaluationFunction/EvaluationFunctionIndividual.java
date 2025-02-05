package model.evolutionTheory.evaluationFunction;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.individual.Individual;

import java.io.Serial;
import java.io.Serializable;

public class EvaluationFunctionIndividual extends Individual  implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    public EvaluationFunctionIndividual() {
        this.genomeLength = 9;
        this.genome = new Chromosome[genomeLength];
        this.mutationRate = 0.1f;
        produceChromosomes();
    }

    public EvaluationFunctionIndividual(float mutationRate) {
        this.genomeLength = 9;
        this.genome = new Chromosome[genomeLength];
        this.mutationRate = mutationRate;
    }

    @Override
    public void produceChromosomes() {
        genome[0] = new WallsDistancesFactorChromosome();
        genome[1] = new WallsColumnsFactorChromosome();
        genome[2] = new TowersDistancesFactorChromosome();
        genome[3] = new TowersColumnsFactorChromosome();
        genome[4] = new TowersRatioFactorChromosome();
        genome[5] = new GameStateChromosome();
        genome[6] = new MobilityFactorChromosome();
        genome[7] = new IsolatedTowersFactorChromosome();
        genome[8] = new IsolatedWallsFactorChromosome();
    }

    @Override
    public void mutate() {
        float mutationChance;
        for (Chromosome chromosome: genome){
            mutationChance = random.nextFloat();
            if (mutationChance > 0.5f) chromosome.mutate();
        }
    }

    @Override
    public Individual[] crossover(Individual partner) {
        Individual[] offspring = new Individual[4];
        Chromosome[] crossover;
        Individual firstChild = new EvaluationFunctionIndividual(mutationRate);
        Individual secondChild = new EvaluationFunctionIndividual(mutationRate);
        Individual thirdChild = new EvaluationFunctionIndividual(mutationRate);
        Individual fourthChild = new EvaluationFunctionIndividual(mutationRate);
        float randomIndex;
        for (int i = 0; i < genomeLength; i++){
            randomIndex = random.nextFloat();
            if (randomIndex < 0.5){
                firstChild.genome[i] = genome[i];
                secondChild.genome[i] = partner.genome[i];
            }
            else {
                firstChild.genome[i] = partner.genome[i];
                secondChild.genome[i] = genome[i];
            }
            crossover = genome[i].crossover(partner.genome[i]);
            randomIndex = random.nextFloat();
            if (randomIndex < 0.5){
                thirdChild.genome[i] = crossover[0];
                fourthChild.genome[i] = crossover[1];
            }
            else {
                fourthChild.genome[i] = crossover[0];
                thirdChild.genome[i] = crossover[1];
            }
        }

        offspring[0] = firstChild;
        offspring[1] = secondChild;
        offspring[2] = thirdChild;
        offspring[3] = fourthChild;
        for (int i = 0; i < 2; i++){
            randomIndex = random.nextFloat();
            if (isExploration()) offspring[i].setExploration(true);
            else if (isExploitation()) offspring[i].setExploitation(true);
            if (randomIndex < mutationRate) offspring[i].mutate();
        }
        return offspring;
    }
}
