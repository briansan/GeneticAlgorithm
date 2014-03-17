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
    public SpiderGeneration( int pop_size, double mut_rate, int mut_vol, int options)
    {super(pop_size,mut_rate,mut_vol,options);}
    
    //
    // init the SpiderGeneration
    //
  @Override
  protected void init()
  {
      int i;
      
      this.ff = new SpiderBotFitnessFunction();
      String[] pop_s = randomPopulation( this.population_size, 27 );
        
        for (i = 0; i < this.population_size; i++)
        {
            this.population[i] = new SpiderChromosome( pop_s[i] );
        }
  }
 }
