//
// title = SpiderBotGeneticAlgorithm.java
// by = Brian Kim
// description = an implementation of the genetic algorithm in the context
//  of the spider bot
//
package spiderbot;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;
import geneticalgorithm.Chromosome;
import geneticalgorithm.Generation;
import geneticalgorithm.GeneticAlgorithm;

/**
 *
 * @author bkim11
 */
public class SpiderBotGeneticAlgorithm extends GeneticAlgorithm
{
	// 
	// properties (instance variables)
	//

    // reference to each motor
	NXTRegulatedMotor left_m, back_m, right_m;
	boolean bestFound = false;
	int wait;
	
	// 
	// constructors
	//
    
	public SpiderBotGeneticAlgorithm( SpiderGeneration gen )
	{
		this(gen,3000);
	}
	
    public SpiderBotGeneticAlgorithm( SpiderGeneration gen, int wait_time )
    {
    	super(gen,0.95);
    	
    	left_m = Motor.A;
    	right_m = Motor.B;
    	back_m = Motor.C;
    	this.wait = wait_time;
    }

    //
    // start method for the NXT
    // 
	public void start()
	{
		// variables: g = generation, n = population size, ff = fitness function
		Generation g = this.getGeneration();
		int n = g.getPopulationSize();
		SpiderBotFitnessFunction ff = new SpiderBotFitnessFunction();

		// main loop
		do
		{	
			// determine the generation number
			int gen_count = g.getCount();
			System.out.println( "Generation " + gen_count);

			// looping through the population
			for (int i = 0; i < n; i++)
			{
				// get the chromosome and decode it
				SpiderChromosome chr = (SpiderChromosome)g.getChromosomeAtIndex(i);
				MotorData[] motors = (MotorData[])chr.decode();
				MotorData m1 = motors[0], m2 = motors[1], m3 = motors[2];
				
				// scale the motor speeds to the proper NXT context
				double scale = 360./256;
				int spd_l = (int)(m1.getMotorSpeed() * scale);
				int spd_b = (int)(m2.getMotorSpeed() * scale);
				int spd_r = (int)(m3.getMotorSpeed() * scale);
				
				// print out chromosome information to screen
				System.out.println( "Chomosome " + (gen_count*n+i+1) );
				System.out.print( chr.toEnglishString() );
				String fitness = String.valueOf(ff.rate(chr));
				if (fitness.length() > 7) fitness = fitness.substring( 0, 7 );
				System.out.println( "fitness: " +fitness+ "\n" );

				// set the motor speeds and run
				left_m.setSpeed(spd_l);
				back_m.setSpeed(spd_b);
				right_m.setSpeed(spd_r);

				if (m1.getDirection()) left_m.backward(); else left_m.forward();
				if (m2.getDirection()) back_m.backward(); else back_m.forward();
				if (m3.getDirection()) right_m.backward(); else right_m.forward();

				//
				// pick one: wait for user input or wait 3 sec to continue
				// 

				/*
				Button.waitForAnyPress();
			      if (Button.ENTER.isDown()) break;
			      if (Button.ESCAPE.isDown()) return;
			      if (Button.LEFT.isDown()) if (i>2) i-=2;
			      if (Button.RIGHT.isDown()) continue;
				*/
				Delay.msDelay(this.wait);
			}

			// evolve the population to the next generation
			bestFound = super.evolve();
		} while (!bestFound); // until the best chromosome is found
		
		// get the best chromosome and display its information
		Chromosome chr = this.generation.getMostFit();
		String msg = "";
		msg +=  "target found:\n" ;
		msg += "bits: " + chr + "\n";
		msg += chr.toEnglishString();
		msg += "fit: " + ff.rate(chr);
		System.out.println(msg);

		// decode its motor speeds
		MotorData[] motors = (MotorData[])chr.decode();
		left_m.setSpeed(motors[0].getMotorSpeed());
		back_m.setSpeed(motors[1].getMotorSpeed());
		right_m.setSpeed(motors[2].getMotorSpeed());

		// set the motors
		if (motors[0].getDirection()) left_m.backward(); else left_m.forward();
		if (motors[1].getDirection()) back_m.backward(); else back_m.forward();
		if (motors[2].getDirection()) right_m.backward(); else right_m.forward();
		
		// wait one minute
		Delay.msDelay(60000);
		
		// stop the motors
		left_m.stop();
		back_m.stop();
		right_m.stop();
	}
}
