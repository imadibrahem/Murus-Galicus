package model.evolutionTheory.chromosome;

public abstract class Chromosome {
    protected boolean exploration = false;
    protected boolean exploitation = false;

    public abstract void mutate();

}
