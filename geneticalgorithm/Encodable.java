//===========================================================
// title = Encodable.java
// by = Brian Kim
// description = an interface that declares functions for
//  encoding some sort of data into a sequence of bits
//===========================================================

package geneticalgorithm;

public interface Encodable 
{
    public BitString encode();
    public int n_bits();
}
