//===========================================================
// title = Chromosome.java
// by = Brian Kim
// description = a class that builds its own internal bitstring 
//   by encoding those encodable objects and concatenating them
//   together
// 
// notes:
// - should be subclassed; won't do much as an instance
//  - override decode() to define the bitstring within the
//    context of your implementation
// - crossover() and mutate() should NOT be overriden
// ZZ:
// - components and decode() represent the same data
//  - must ensure robustness between the two
//===========================================================

package geneticalgorithm;

// a chromosome is a bitstring
public class Chromosome extends BitString
{
    //
    // properties (instance variables)
    //

    // internal storage of components
    protected Encodable[] components;

    // number of components
    protected int size;
    
    //
    // constructor methods
    //

    // copy constructor
    public Chromosome( Chromosome ch )
    {
        this( ch.toString() );
    }
    
    // construction by components
    public Chromosome( Encodable[] genes )
    {
        super( countBits( genes ) );
        components = genes;
        size = components.length;
        
        updateBits();
    }
    
    // construction by bitstring.
    public Chromosome( String bits )
    {
        super(bits);
        components = this.decode();
        size = components.length;
    }
    
    //
    // accessor methods
    //

    // component getter
    public Encodable[] getComponents() {
        this.components = this.decode();
        return components;
    }
    
    // component setter
    // ZZ: is this a good idea?
    public void setComponent( int i, Encodable obj )
    {
        String msg = err_msg() + "setComponent: ";
        if (i > size - 1 || i < 0) 
        {
            msg += "out of bounds component access";
            System.err.println( msg );
            return;
        }
        components[i] = obj;
        updateBits();
    }
    
    // get number of bits
    public int n_bits() {return n_bits;}

    // bitstring setter 
    // ZZ: why is this here...
    @Override
    public void setBits( String bits )
    {
        super.setBits( bits );
    }

    //
    // utility methods
    // 

    // convenience method for error reporting
    private static String err_msg() {return "error: Chromosome: ";}
    
    // decodes the bitstring into its components
    // should be overriden
    public Encodable[] decode()
    {
        return components;
    }
    
    // flips a <volume> number of bits 
    public void mutate( int volume )
    {
        // make sure the volume is a valid value
        volume = this.cleanMutationVolume(volume);
        
        // indicies that have already been mutated
        int indicies[] = new int[volume];

        // mutating <volume> random bits 
        int i = 0;
        while( i < volume )
        {
            // get a random index
            int index = randomIndex();

            // has the index already been mutated?
            boolean indexExists = false;
            for (int j = 0; j < i; j++)
            {
                if (indicies[j] == index) {
                    indexExists = true;
                    break;
                }
            }

            // if not, mutate bit at <index> and incr i
            if (!indexExists) { 
                complementBit(index); 
                indicies[i] = index;
                i++; 
            }
        }
    }
    
    // reproduce with another chromosome <partner>
    public Chromosome crossover( Chromosome partner )
    {
        // make sure classes match between mates
        if (!this.getClass().equals(partner.getClass()))
        {
            String msg = err_msg() + "crossover: type mismatch with partner";
            System.err.println(msg);
            return null;
        }
        
        // create a copy of this chromosome
        Chromosome child = (Chromosome)this.clone();

        // get a random index 
        int pivot = randomIndex();
        
        // copy over bits [0, pivot) from this to child
        // ZZ: this doesn't seem necessary
        for (int i = 0; i < pivot; i++)
        {
            child.setBit(i, this.getBit(i));
        }

        // copy over bits [pivot, n_bits) from partner to child
        for (int i = pivot; i < partner.n_bits; i++)
        {
            child.setBit(i, partner.getBit(i));
        }
        
        // update the components of the child with the new bitstring
        child.components = (Encodable[])child.decode();
        
        return child;
    }
    
    // used for creating a child
    @Override
    public Object clone()
    {
        return new Chromosome( components );
    }
    
    // BitString (gene) concatenation of encodable objects
    // ZZ: relationship between bitstring and components
    //     is not stable 
    public void updateBits()
    {
        String bin = "";
        for (int i = 0; i < size; i++)
        {
            bin += components[i].encode();
        }
        setBits(bin);
    }
    
    // make sure <v> is a valid value within the range [0, n_bits)
    public int cleanMutationVolume( int v )
    {
        if (v >= n_bits) v = n_bits - 1;
        return v;
    }
    
    // count the total number of bits that the encodable 
    //  objects require
    public static int countBits( Encodable[] bit_list )
    {
        // a simple summation algorithm
        int size = 0;
        for (int i = 0; i < bit_list.length; i++)
        {
            size += bit_list[i].n_bits();
        }
        return size;
    }
    
    // returns the bitstring
    public String toBitString()
    {
        return super.toString();
    }

    // meant to be overriden to provide a meaningful
    // output since toString() must return a BitString
    public String toEnglishString()
    {
    	return "this is a chromosome";
    }
}
