//
// title = SpiderBotGeneticAlgorithm.java
// by = Brian Kim
// description = an implementation of the genetic algorithm in the context
//  of the spider bot
//
package spiderbot;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
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
	NXTRegulatedMotor left_m;
	NXTRegulatedMotor back_m;
	NXTRegulatedMotor right_m;
	
	boolean bestFound = false;
	
    public SpiderBotGeneticAlgorithm( int popsize )
    {super(popsize);}
    
    public SpiderBotGeneticAlgorithm( int popsize, double target )
    {super(popsize,target);}
    
    public SpiderBotGeneticAlgorithm( int popsize, double target, int opt)
    {super(popsize,target,opt);}
    
    protected void init( int popsize )
    {
    	left_m = new NXTRegulatedMotor(MotorPort.A);
    	back_m = new NXTRegulatedMotor(MotorPort.B);
    	right_m = new NXTRegulatedMotor(MotorPort.C);
        this.generation = new SpiderGeneration( popsize );
    }

	public void start()
	{
		Generation g = this.getGeneration();
		int n = g.getPopulationSize();
		SpiderBotFitnessFunction ff = new SpiderBotFitnessFunction();
		do
		{	
			int gen_count = g.getCount();
			System.out.println( "Generation " + gen_count);
			for (int i = 0; i < n; i++)
			{
				SpiderChromosome chr = (SpiderChromosome)g.getChromosomeAtIndex(i);
				MotorData[] motors = (MotorData[])chr.decode();
				MotorData m1 = motors[0], m2 = motors[1], m3 = motors[2];
				
				double scale = 360./256;
				int spd_l = (int)(m1.getMotorSpeed() * scale);
				int spd_b = (int)(m2.getMotorSpeed() * scale);
				int spd_r = (int)(m3.getMotorSpeed() * scale);
				
				System.out.println( "Chomosome " + (gen_count*n+i+1) );
				System.out.print( chr.toEnglishString() );
				String fitness = String.valueOf(ff.rate(chr));
				if (fitness.length() > 7) fitness = fitness.substring( 0, 7 );
				System.out.println( "fitness: " +fitness+ "\n" );
				left_m.setSpeed(spd_l);
				back_m.setSpeed(spd_b);
				right_m.setSpeed(spd_r);

				if (m1.getDirection()) left_m.backward(); else left_m.forward();
				if (m2.getDirection()) back_m.backward(); else back_m.forward();
				if (m3.getDirection()) right_m.backward(); else right_m.forward();

				/*
				Button.waitForAnyPress();
			      if (Button.ENTER.isDown()) break;
			      if (Button.ESCAPE.isDown()) return;
			      if (Button.LEFT.isDown()) if (i>2) i-=2;
			      if (Button.RIGHT.isDown()) continue;
				*/
				Delay.msDelay(3000);
			}
			bestFound = super.evolve();
		} while (!bestFound);
		
		Chromosome chr = this.generation.getMostFit();
		String msg = "";
		msg +=  "target found:\n" ;
		msg += "bits: " + chr + "\n";
		msg += chr.toEnglishString();
		msg += "fit: " + ff.rate(chr);
		System.out.println(msg);

		MotorData[] motors = (MotorData[])chr.decode();
		left_m.setSpeed(motors[0].getMotorSpeed());
		back_m.setSpeed(motors[1].getMotorSpeed());
		right_m.setSpeed(motors[2].getMotorSpeed());

		if (motors[0].getDirection()) left_m.backward(); else left_m.forward();
		if (motors[1].getDirection()) back_m.backward(); else back_m.forward();
		if (motors[2].getDirection()) right_m.backward(); else right_m.forward();
		
		Button.waitForAnyPress();
		
		left_m.stop();
		back_m.stop();
		right_m.stop();
	}
    
}
