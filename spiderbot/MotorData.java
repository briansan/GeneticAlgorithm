//===========================================================
// title = MotorBitString.java
// by = Brian Kim
// description = an encodable class that can translate bits
//  into motor direction and magnitude
// note = this is an immutable data type
//============================================================

package spiderbot;

import geneticalgorithm.BitString;
import geneticalgorithm.Encodable;

/**
 *
 * @author bkim11
 */
public class MotorData implements Encodable
{
    protected int motorSpeed;
    protected boolean direction; // true == forward, false == reverse

    // default constructor sets speed = 0, direction = forward
    public MotorData()
    {
        this( 10, true );
    }
    
    public MotorData( int speed, boolean dir )
    {
        // speed cannot exceed the bounds of 0-255
        if (speed > 255) speed = 255;
        if (speed < 0) speed = 0;
        
        motorSpeed = speed;
        direction = dir;
    }
    
    // accessor methods
    public int getMotorSpeed() {return motorSpeed;}
    public boolean getDirection() {return direction;}
    
    @Override
    public BitString encode() 
    {
        // convert motorspeed to bits, pad higher order bits with 0's as needed
        String motorBits = BitString.dec2bin(motorSpeed);
        int padding = 8 - motorBits.length();
        String pad = "";
        for (int i = 0; i < padding; i++)
        {
            pad += "0";
        }
        
        // convert direction to a bit
        String all =  direction ? "1" : "0";
        
        // concatenate bits together
        all += pad + motorBits;
        return new BitString(all);
    }
    
    @Override
    public String toString()
    {
        String dir = direction ? "-" : "+";
        return dir + motorSpeed;
    }
    
    @Override
    public int n_bits() 
    {
        return 9;
    }
    
    public static MotorData bin2motorData( String b )
    {
        // this function can only convert 9 bit strings
        if (b.length() != 9) return null; 
        
        // get the first 8 bits and convert to decimal
        String motorspeed_s = b.substring(1, 9);
        int motorspeed = BitString.bin2dec(motorspeed_s);
        
        // get the last bit: 1 == reverse, 0 == forward
        boolean direction = b.charAt(0) == '1';
        return new MotorData( motorspeed, direction );
    }
    
    public static MotorData bits2motorData( BitString b )
    {
        return bin2motorData(b.toString());
    }
}
