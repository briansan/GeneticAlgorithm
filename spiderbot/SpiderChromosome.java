//===========================================================
// title = SpiderChromosome.java
// by = Brian Kim
// description = a chromosome class that holds information
//  about its three motors
//============================================================

package spiderbot;
import geneticalgorithm.Chromosome;
import geneticalgorithm.Encodable;

/**
 *
 * @author bkim11
 */
public class SpiderChromosome extends Chromosome
{
    //
    // constructors
    //
    public SpiderChromosome( String s )
    {
        super( s );
    }
    
    public SpiderChromosome( Chromosome ch )
    {
        super( ch );
    }
    
    // MUST be three MotorData objects in the constructor
    public SpiderChromosome( MotorData[] motors )
    {
        super( cleanMotors(motors) );
    }
    
    //
    // utility methods
    //

    // bitstring -> motors
    @Override
    public Encodable[] decode()
    {
        String s = this.toString();
        
        String m1_s = s.substring(0, 9);
        String m2_s = s.substring(9, 18);
        String m3_s = s.substring(18, 27);
        MotorData m1 = MotorData.bin2motorData(m1_s);
        MotorData m2 = MotorData.bin2motorData(m2_s);
        MotorData m3 = MotorData.bin2motorData(m3_s);
        
        return new MotorData[]{m1,m2,m3};
    }
    
    // make sure that <motors> has exactly 3 elements
    private static MotorData[] cleanMotors( MotorData[] motors )
    {
        MotorData[] ret = new MotorData[3];
        MotorData blank = new MotorData();
        
        if (motors.length == 0)
        {
            ret[0] = blank;
            ret[1] = blank;
            ret[2] = blank;
        }
        if (motors.length >= 1)
        {
            ret[0] = motors[0];
        }
        if (motors.length >= 2)
        {
            ret[1] = motors[1];
        }
        if (motors.length >= 3)
        {
            ret[2] = motors[2];
        }
        
        return ret;
    }
    
    //
    // utility methods
    //

    @Override
    public Object clone()
    {
        return new SpiderChromosome( (MotorData[])this.components );
    }
    
    public String toEnglishString()
    {
        String y = "";
        for (int i = 0; i < 3; i++)
        {
            y += "motor" + (i+1) + ": ";
            MotorData m = (MotorData)components[i];
            y += m + "\n";
        }
        return y;
    }
}
