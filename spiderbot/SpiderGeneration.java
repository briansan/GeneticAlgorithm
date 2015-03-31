//
// title = SpiderGeneration.java
// by = Brian Kim
// description = a subclass of the generation 
//
package spiderbot;

import geneticalgorithm.Generation;

/**
 *
 * @author bkim11
 */
public class SpiderGeneration extends Generation
{
    //
    // constructors
    //
    public SpiderGeneration( int pop_size )                  
     {super(pop_size);}
    public SpiderGeneration( int pop_size, double mut_rate ) 
     {super(pop_size, mut_rate);}
    public SpiderGeneration( int pop_size, double mut_rate, int mut_vol)
    {super(pop_size,mut_rate,mut_vol);}
    public SpiderGeneration( int pop_size, double mut_rate, int mut_vol, Generation.CrossoverStrategy options)
    {super(pop_size,mut_rate,mut_vol,options);}
    public SpiderGeneration( int pop_size, double mut_rate, int mut_vol,
    						 Generation.CrossoverStrategy x_strategy, boolean x_random )
    {super(pop_size,mut_rate,mut_vol,x_strategy,x_random);}
    
    //
    // init the SpiderGeneration
    //
    @Override
    protected void init()
    {
        int i;
        
        // set the fitness function
        this.ff = new SpiderBotFitnessFunction();

        // generate a random spider population
        String[] pop_s = randomPopulation( this.population_size, 27 );
        for (i = 0; i < this.population_size; i++)
        {
            this.population[i] = new SpiderChromosome( pop_s[i] );
        }
    }
}
