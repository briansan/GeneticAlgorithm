//===========================================================
// title = BitString.java
// by = Brian Kim
// description = a class that defines a bitstring 
//  that can be encoded and decoded
//  encoding is done through the Encodable interface
// 
// notes:
// - implemented as an array of bytes
// - consider an array a of n bytes:
//  - index = 0 represents:
//     the left-most (first) element of a's memory block
//  - index = n-1 represents:
//     the right-most (last) element of a's memory block
// - however, for bitstrings,  bit 0 is the 
//    right most digit.
// - we solve this by converting all our array indicies 
//    to little endian using our toggleEnd() method 
//    so that our addressing scheme is more consistent 
//===========================================================

package geneticalgorithm;

public class BitString
{
    //
    // properties (instance variables)
    //
    protected int n_bits; // = size of bitstring
    protected byte[] bits; // = internal storage of bits as an array of bytes
    protected Integer bits_i; // = internal storage of bits as an Integer
    
    private static String err_msg() { return "error: BitString: "; }
            
    //
    // constructors
    //
    
    //  n = size of bitstring
    public BitString( int n )
    {
        n_bits = n;
        bits = new byte[n];
    }
    
    // constructor method with array of bits (byte) specified
    public BitString( byte[] b )
    {
        this( b.length );
        bits = b;
    }
    
    // constructor method with a character string of bits specified
    public BitString( String s )
    {
        this( s.length() );
        setBits( s );
    }
    
    
    //
    // accessor methods
    //
    
    public Integer intValue()
    {
        return bits_i;
    }
    
    // gets the ith bit of the byte array
    public byte getBit( int i )
    {
        return bits[toggleEnd(i)];
    }
    
    // sets the ith bit of the byte array to val
    public void setBit( int i, byte val )
    {
        bits[toggleEnd(i)] = val != 0 ? (byte)1 : 0;
    }
    
    // gets the entire array of bits
    public byte[] getBits()
    {
        return bits;
    }
    
    // sets the entire array of bits with a byte array
    public void setBits( byte[] val )
    {
        /* 
         * ZZ: what to do with byte arrays whose sizes != n_bits
         * 
        if (val.length != n_bits) {
            String err = err_msg() + "setBits(byte[]): size mismatch";
            System.err.println(err);
            return;
        }
        * */
        bits = val;
    }
    
    // sets the entire array of bits with a character string
    public void setBits( String s )
    {
        /*
         * ZZ: what to do with byte arrays whose sizes != n_bits
         * 
        if (s.length() != n_bits) {
            String err = err_msg() + "setBits(String): size mismatch...string: " + s.length() + ", n_bits: " + n_bits ;
            System.err.println(err);
            return;
        }
        */
        for (int i = 0; i < s.length(); i++)
        {
            byte val = s.charAt(i)=='0' ? 0 : (byte)1;
            setBit( i, val );
        }
    }
    
    // 
    // utility methods
    //
    
    // toggle the big endian end of the byte array to little endian 
    protected int toggleEnd( int i )
    {
        return n_bits - i - 1;
    }
    
    // flips a bit
    public void complementBit( int i )
    {
        // make sure the index isn't out of bounds
        if (i >= n_bits || i < 0) {
            String err = err_msg() + "complementBit(int)): index out of bounds";
            System.err.println(err);
            return;
        }
        // get the value of little endian ith bit.
        byte b = getBit(i);
        
        // our complementing function
        b = b == (byte)0 ? (byte)1 : 0;
        
        // set the little endian ith bit to the complemented bit
        setBit(i, b);
    }
    
    // returns a random index that is within the bounds of this bit string
    public int randomIndex()
    {
        // [ 0, n_bits-1 ]
        return (int)(Math.random() * n_bits);
    }
    
    @Override
    public String toString()
    {
        // build a string that represents the bit string
        char[] s = new char[n_bits];
        
        // converting each bit in bits to a character
        for (int i = 0; i < n_bits; i++)
        {
            s[i] = getBit(i) == 1 ? '1' : '0';
        }
        return new String(s);
    }
    
    @Override
    public boolean equals( Object obj )
    {
        // if obj is a BitString
        if (obj.getClass()==this.getClass())
            return ((BitString)obj).intValue().equals( this.intValue() );
        return false;
    }
    
    //
    // convenience static methods
    //
    
    // counts the number of bits that a number n requires
    public static int countBin( int n )
    {
      // count the number of bits for x
      int count = 1;
      while( n > 1 )
      {
        // increment count and divide n by 2
        count++;
        n /= 2;    
      }
      return count;
    }
    
    // converts a binary string to a decimal value (int)
    public static int bin2dec( String binary )
    {
        // returning val which represents the sum
        int val = 0;
        
        // iterating through the bit string
        int size = binary.length();
        for (int i = 0; i < size; i++)
        {
            // convert to the ith digit in the bit string
            int digit = size - i - 1;
            
            // if the bit is 1, add the value of 2 raised its digit
            char b = binary.charAt(i);
            if (b == '1') val += Math.pow( 2, digit );
        }
        return val;
    }
    
    // converts a bit string to a decimal value (int)
    public static int bits2dec( BitString bitstring )
    {
        return bin2dec( bitstring.toString() );
    }
    
    // converts an integer value to a bit string
    public static BitString dec2bits( int x )
    {
        return new BitString( dec2bin(x) );  
    }
    
    // converts an integer value to a binary character string
    public static String dec2bin( int x )
    {
        // build the bit-string
        int count = countBin( x );
        int n = x;
        char[] s = new char[count];

        // keep looping through powers of 2 
        for (int i = 0; i < count; i++)
        {
          // get the decimal value of each digit
          int next = (int)Math.pow( 2, count-i-1 );
          // check the value of next: printf( "2^%d = %d\n", count, next );

          // if the input value n is less than the calculated next, put 0
          if (next > n) s[i] = '0';
          else 
          {
            // if n is greater than next, put 1 and subtract next by n
            s[i] = '1';
            n -= next; 
          }
        }
        return new String(s);
    }
    
    // returns a random bit string object that is n bits long
    public static String randomBitString( int n )
    {
        // building the string y
        String y = "";
        for (int i = 0; i < n; i++ )
        {
            // get a random bit and append it to y
            byte b = (byte)Math.round(Math.random());
            y += b;
        }
        return y;
    }
}
