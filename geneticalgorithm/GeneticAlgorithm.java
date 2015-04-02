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
    // properties (instance variables)
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
    	this( 8 );
    }
    
    public GeneticAlgorithm( int pop_size )
    {
        this( new Generation(pop_size) {
        	@Override
        	protected FitnessFunction fitness_function() {
        		return null;
        	}
        	@Override
        	protected Chromosome[] initial_population( int n ) {
        		return null;
        	}
        });
    }
    
    public GeneticAlgorithm( Generation gen )
    {
        this( gen, 0.95 );
    }
    
    
    public GeneticAlgorithm( Generation gen , double target_fit )
    {
    	this.generation = gen;
        this.target_fitness = target_fit;
    }
    
    // 
    // important API
    //
    
    // evolve the generation
    // returns whether or not the most fit chromosome
    //   beats the fitness function
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