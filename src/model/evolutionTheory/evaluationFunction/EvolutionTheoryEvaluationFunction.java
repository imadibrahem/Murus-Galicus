package model.evolutionTheory.evaluationFunction;

import model.Board;
import model.evaluationFunction.EvaluationFunction;
import model.evolutionTheory.individual.Individual;

import java.io.Serial;
import java.io.Serializable;

public class EvolutionTheoryEvaluationFunction extends EvaluationFunction implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    public EvolutionTheoryEvaluationFunction(Board board, Individual individual) {
        super(board, individual.genome[0].value, individual.genome[1].value, individual.genome[2].value, individual.genome[3].value, individual.genome[4].value, individual.genome[5].value, individual.genome[6].value[0], individual.genome[7].value[0], individual.genome[8].value[0]);
    }
}
