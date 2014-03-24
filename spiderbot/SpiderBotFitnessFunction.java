//
// title = SpiderBotFitnessFunction.java
// by = Brian Kim
// description = a class that implements the fitness function 
//  interface with respect to the proper setup of motors for the spiderbot
//

package spiderbot;

import geneticalgorithm.FitnessFunction;

/**
 *
 * @author bkim11
 */
public class SpiderBotFitnessFunction implements FitnessFunction 
{
    @Override
    public double rate( Object x )
    {
        //
        // variable declarations
        //
       
        MotorData[] motors;   // an array of motors decoded from the chromosome
        MotorData m1, m2, m3; // dummy variables...m1 = left, m2 = back, m3 = right
        double y;             // return value
        double err;           // total error
        double dir_err, dir_err_lr, dir_err_back;           // directional error (0.5)
        double spd_err, spd_err_lr, spd_err_lb, spd_err_rb; // speed error (0.5)
        boolean back_overflow; 
        // the speed of the back motor accounts for 50% of the speed error (0.25)
        
        // 
        // variable assignments
        //
        motors = (MotorData[])((SpiderChromosome)x).decode();
        m1 = motors[0]; m2 = motors[1]; m3 = motors[2]; 
        // m1 = left, m2 = back, m3 = right
        
        // left/right directional sync is most important (half weight)
        // left/back and right/back directional is half as important
        dir_err_lr = (m1.direction == m3.direction && m1.direction == true) ? 0 : 0.25;
        dir_err_back = m2.direction == false ? 0 : 0.25;
        
        // if back is too fast, then errors for left/back
        //  and right/back are automatically maxed out
        back_overflow = m2.motorSpeed < m1.motorSpeed;
        
        // errors are a function of difference between motor speeds
        //  multiplied by its weight
        spd_err_lr = ((double)Math.abs(m1.motorSpeed - m3.motorSpeed) / 256) * 0.25;
        spd_err_lb = (back_overflow ? 1.0 : (double)Math.abs(m1.motorSpeed - m2.motorSpeed/2)/128) * 0.125;
        spd_err_rb = (back_overflow ? 1.0 : (double)Math.abs(m3.motorSpeed - m2.motorSpeed/2)/128) * 0.125;
        
        // compute the errors
        dir_err = dir_err_lr + dir_err_back;
        spd_err = spd_err_lr + spd_err_lb + spd_err_rb;
        err = dir_err + spd_err;
        
        // fitness = 1 - error
        y = 1 - err;
        
        /* debugging
        {
          String out = "spiderff: \n";
          out += "  motors: \n1. " + m1 + "\n2. " + m2 + "\n3." + m3 + "\n";
          out += "  left/right directional error: " + dir_err_lr + "\n";
          out += "  back motor directional error: " + dir_err_back + "\n";
          out += "  left/right motor speed ratio error: " + spd_err_lr + "\n";
          out += "  left/back motor speed ratio error: " + spd_err_lb + "\n";
          out += "  right/back motor speed ratio error: " + spd_err_rb + "\n";
          out += "  error: " + err + ": fitness: " + y + "\n";
          System.out.println( out );
        }
        */
        
        return y;
    }
    
}
