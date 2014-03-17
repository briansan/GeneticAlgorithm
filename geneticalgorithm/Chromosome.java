//===========================================================
// title = Chromosome.java
// by = Brian Kim
// description = a class that builds its own internal bitstring 
//   by encoding those encodable objects and concatenating them
//   together
//===========================================================

package geneticalgorithm;

public class Chromosome extends BitString
{
    protected Encodable[] components;
    protected int size;
    
    private static String err_msg() {return "error: Chromosome: ";}
    
    public Chromosome( Chromosome ch )
    {
        this( ch.toString() );
    }
    
    public Chromosome( Encodable[] genes )
    {
        super( countBits( genes ) );
        components = genes;
        size = components.length;
        
        updateBits();
    }
    
    // constructor method
    // POSSIBLE BUG: does not set the components or size ivars...
    public Chromosome( String bits )
    {
        super(bits);
        components = this.decode();
        size = components.length;
    }
    
    public Encodable[] getComponents() {
        this.components = this.decode();
        return components;
    }
    
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
    
    public Encodable[] decode()
    {
        return components;
    }
    @Override
    public void setBits( String bits )
    {
        super.setBits( bits );
    }
     
   public int n_bits() {return n_bits;}
    
    public void mutate( int volume )
    {
        // clean the mutation volume
        volume = this.cleanMutationVolume(volume);
        
        int indicies[] = new int[volume];
        int i = 0;
        while( i < volume )
        {
            int index = randomIndex();
            boolean indexExists = false;
            for (int j = 0; j < i; j++)
            {
                if (indicies[j] == index) {
                    indexExists = true;
                    break;
                }
            }
            if (!indexExists) { 
                complementBit(index); 
                indicies[i] = index;
                i++; 
            }
        }
    }
    
    public Chromosome crossover( Chromosome partner )
    {
        if (!this.getClass().equals(partner.getClass()))
        {
            String msg = err_msg() + "crossover: type mismatch with partner";
            System.err.println(msg);
            return null;
        }
        
        Chromosome child = (Chromosome)this.clone();
        int pivot = randomIndex();
        
        for (int i = 0; i < pivot; i++)
        {
            child.setBit(i, this.getBit(i));
        }
        for (int i = pivot; i < partner.n_bits; i++)
        {
            child.setBit(i, partner.getBit(i));
        }
        
        // create the child
        child.components = (Encodable[])child.decode();
        
        return child;
    }
    
    @Override
    public Object clone()
    {
        return new Chromosome( components );
    }
    
    // BitString (gene) concatenation of encodable objects
    public void updateBits()
    {
        String bin = "";
        for (int i = 0; i < size; i++)
        {
            bin += components[i].encode();
        }
        setBits(bin);
    }
    
    public int cleanMutationVolume( int v )
    {
        if (v >= n_bits) v = n_bits - 1;
        return v;
    }
    
    public static int countBits( Encodable[] bit_list )
    {
        int size = 0;
        for (int i = 0; i < bit_list.length; i++)
        {
            size += bit_list[i].n_bits();
        }
        return size;
    }
    
    public String toEnglishString()
    {
    	return "this is a chromosome";
    }
}
