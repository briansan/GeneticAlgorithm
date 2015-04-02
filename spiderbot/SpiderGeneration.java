//
// title = SpiderGeneration.java
// by = Brian Kim
// description = a subclass of the generation 
//
package spiderbot;

import geneticalgorithm.*;

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
    						 Generation.CrossoverStrategy x_strategy, Generation.CrossoverPoint x_pt )
    {super(pop_size,mut_rate,mut_vol,x_strategy,x_pt);}
    
    
    @Override
    protected Chromosome[] initial_population( int n )
    {
    	SpiderChromosome[] y = new SpiderChromosome[n];
    	
    	// generate a known pattern of initial chromosomes
    	
    	// an array of motor speeds
    	int[] speeds = new int[]{ 17, 37, 67, 107, 157, 167, 197, 227 };
    	
    	// k is the index into the speed array that will increment at a factorial rate
    	int k = 0, incr = 1;
    	boolean dir = false; // for the direction, will toggle at each iteration
    	
    	// over all the population...
        for (int i = 0; i < n; i++)
        {
        	String chr_s = "";
        	for (int j = 0; j < 3; j++)
        	{
        		// generate a value
        		int val = (dir ? 256 : 0) + speeds[k%8];
        		chr_s += BitString.dec2bin(val,9);
        		// iterate
        		dir = !dir;
        		k += incr;
        		incr += 3;
        	}
            y[i] = new SpiderChromosome(chr_s);
        }
        return y;
    }
    
    @Override
    protected FitnessFunction fitness_function()
    {
    	return new SpiderBotFitnessFunction();
    }
}
