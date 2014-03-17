//===========================================================
// title = Encodable.java
// by = Brian Kim
// description = an interface that declares functions for
//  encoding and decoding bits
//===========================================================

package geneticalgorithm;

/**
 *
 * @author bkim11
 */
public interface Encodable 
{
    public BitString encode();
    public int n_bits();
}
