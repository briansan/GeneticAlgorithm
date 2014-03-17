//===========================================================
// title = GeneticAlgorithm.java0
// by = Brian Kim
// description = the main class for the genetic algorithm
//   implementation
//===========================================================

package geneticalgorithm;

public abstract class GeneticAlgorithm
{
    //
    // utility variables
    //
    
    // a generation object that will control our chromosomes
    // - has an internal array of chromosomes
    // - keeps track of the current generation count
    // - evolve(): updates internal array of fitness values with
    //              internal fitness function
    public Generation generation;
    
    //
    // accessor methods
    //
    public Generation getGeneration() {return this.generation;}
    
    protected double target_fitness;
    protected int options;
    
    //
    // constructor methods
    //
    
    public GeneticAlgorithm()
    {
        this( 10 );
    }
    
    public GeneticAlgorithm( int popsize )
    {
        this( popsize, 0.05 );
    }
    
    public GeneticAlgorithm( int popsize, double target_fit )
    {
        this( popsize, target_fit, 1 );
    }
    
    public GeneticAlgorithm( int popsize, double target_fit, int opt )
    {
        this.target_fitness = target_fit;
        this.options = opt;
        // generation and fitness function should be instantiated by init()
        this.init( popsize );
    }
    
    // 
    // important API
    //
    
    // override this method to initialize ff and population
    protected abstract void init( int popsize );
    
    public boolean evolve()
    {
        this.generation.evolve();
        if (generation.rateMostFit() >= this.target_fitness)
        {
        	return true;
        }
        return false;
    }
    
    
}