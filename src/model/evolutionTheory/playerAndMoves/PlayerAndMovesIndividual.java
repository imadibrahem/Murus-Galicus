package model.evolutionTheory.playerAndMoves;

import model.evolutionTheory.chromosome.Chromosome;
import model.evolutionTheory.evaluationFunction.EvaluationFunctionIndividual;
import model.evolutionTheory.evaluationFunction.individual.Individual;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class PlayerAndMovesIndividual extends Individual implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;

    public PlayerAndMovesIndividual() {
        this.genomeLength = 20;
        this.genome = new Chromosome[genomeLength];
        this.mutationRate = 0.1f;
        produceChromosomes();
    }

    public PlayerAndMovesIndividual(float mutationRate) {
        this.genomeLength = 20;
        this.genome = new Chromosome[genomeLength];
        this.mutationRate = mutationRate;
    }

    @Override
    public void mutate() {
        float mutationChance;
        String result = this +"\n";
        for (int i = 0; i < genomeLength; i++){
            mutationChance = random.nextFloat();
            System.out.println("Chromosome sub mutation chance is: " + mutationChance + " mutation rate is: " + (1 - mutationRate));
            if (mutationChance >  (mutationRate * 2)) {
                genome[i].mutate();
            }
            System.out.println();
        }
        result += this.toString();
        System.out.println();
        System.out.println("++++++++++++++++++ Mutation Result +++++++++++++++++++++");
        System.out.println(result);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    }

    @Override
    public void produceChromosomes() {
        genome[0] = new MoveGeneratingStyleChromosome();
        genome[1] = new MoveGeneratingMoveTypesChromosome();
        genome[2] = new DirectionsChromosome();
        genome[3] = new MoveGeneratingMoveTypesChromosome();
        genome[4] = new WindowChromosome();
        genome[5] = new WindowChromosome();
        genome[6] = new InteractiveDepthRatioChromosome();
        genome[7] = new FullDepthMoveNumberChromosome();
        genome[8] = new RoundsFactorChromosome();
        genome[9] = new TowersFactorChromosome();
        genome[10] = new DistancesFactorChromosome();
        genome[11] = new KillerSortChromosome();
        genome[12] = new MoveComparatorMoveTypesChromosome();
        genome[13] = new CompChromosome();
        genome[14] = new MoveComparatorFrontToBackBooleanChromosome();
        genome[15] = new PeakMoveChromosome();
        genome[16] = new MidGameMovesChromosome();
        genome[17] = new EarlyFactorChromosome();
        genome[18] = new MidFactorChromosome();
        genome[19] = new EndFactorChromosome();
    }

    @Override
    public Individual[] crossover(Individual partner) {
        Individual[] offspring = new Individual[4];
        Chromosome[] crossover;
        Individual firstChild = new PlayerAndMovesIndividual(mutationRate);
        Individual secondChild = new PlayerAndMovesIndividual(mutationRate);
        Individual thirdChild = new PlayerAndMovesIndividual(mutationRate);
        Individual fourthChild = new PlayerAndMovesIndividual(mutationRate);
        firstChild.produceChromosomes();
        secondChild.produceChromosomes();
        float randomIndex;
        System.out.println("child #1 & child #2: (Full Chromosomes crossover):");
        for (int i = 0; i < genomeLength; i++){
            System.out.println("Chromosome #" + i);
            randomIndex = random.nextFloat();
            if (randomIndex < 0.5){
                firstChild.genome[i].value = Arrays.copyOf(genome[i].value, genome[i].value.length);
                secondChild.genome[i].value = Arrays.copyOf(partner.genome[i].value, partner.genome[i].value.length);

            }
            else {
                firstChild.genome[i].value = Arrays.copyOf(partner.genome[i].value, partner.genome[i].value.length);
                secondChild.genome[i].value = Arrays.copyOf(genome[i].value, genome[i].value.length);
            }
        }
        System.out.println();
        offspring[0] = firstChild;
        offspring[1] = secondChild;
        System.out.println("adjusting First child & Second child modes and checking for mutations..");
        for (int i = 0; i < 2; i++){
            System.out.println();
            randomIndex = random.nextFloat();
            if (i == 0){
                System.out.println("First child mode..");
            }
            else {
                System.out.println("Second child mode..");
            }
            if (isExploration()) offspring[i].setExploration(true);
            else if (isExploitation()) offspring[i].setExploitation(true);
            System.out.println("checking for mutation..");
            System.out.println("mutation chance is: " + randomIndex + "mutation rate is: " + mutationRate + "=> Full Chromosomes crossover mutation rate is: " + (mutationRate * 2));
            if (randomIndex < (mutationRate * 2)) {
                System.out.println("mutation is being applied ");
                System.out.println();
                offspring[i].mutate();
            }
        }
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
        System.out.println("child #3 & child #4: (Single Gene crossover):");
        System.out.println("adjusting Third child & Forth child modes..");
        offspring[2] = thirdChild;
        offspring[3] = fourthChild;
        for (int i = 0; i < 2; i++){
            if (i == 0){
                System.out.println("Third child mode..");
            }
            else {
                System.out.println("Fourth child mode..");
            }
            if (isExploration()) offspring[i + 2].setExploration(true);
            else if (isExploitation()) offspring[i + 2].setExploitation(true);
        }
        System.out.println("applying crossover for Third child & Forth child and checking for mutations..");
        System.out.println();
        for (int i = 0; i < genomeLength; i++){
            System.out.println("Chromosome #" + i);
            System.out.println();
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
            System.out.println();
        }

        System.out.println("+++++++++++ Children ++++++++++");
        for (Individual child : offspring){
            System.out.println(child);
        }
        System.out.println("+++++++++++ Children ++++++++++");

        return offspring;
    }
}
