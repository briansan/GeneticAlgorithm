/**
 * @file   Main.java
 * @author Brian Kim
 * @brief  the main driver code for the spider bot genetic algorithm
 */

package spiderbot;

import geneticalgorithm.Generation.*;

public class Main {

    public static void main( String[] args ) {
    	
    	// create constants
        final int               POPULATION_SIZE    = 8;    // size of the population
        final double            MUTATION_RATE      = 0.02; // probability of mutation
        final int               MUTATION_VOLUME    = 1;    // number of bits to mutate
        final CrossoverStrategy CROSSOVER_STRATEGY = CrossoverStrategy.Parent1Hi_Parent2Lo; // choices: Parent1Hi_Parent2Lo, Parent2Hi_Parent1Lo, Random
        final CrossoverPoint    CROSSOVER_POINT    = CrossoverPoint.Random; // choices: A_BC, AB_C, Random
        final int				PAUSE_TIME		   = 1000; // wait time between chromosome displays (milliseconds)
        
        // create the initial generation
        SpiderGeneration generation = new SpiderGeneration( POPULATION_SIZE, MUTATION_RATE, MUTATION_VOLUME, CROSSOVER_STRATEGY, CROSSOVER_POINT );
        // create the genetic algorithm system
        SpiderBotGeneticAlgorithm genalg = new SpiderBotGeneticAlgorithm(generation, PAUSE_TIME);
        // start the genetic algorithm
        genalg.start();
    }
}
