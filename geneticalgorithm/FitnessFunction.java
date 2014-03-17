//
// title = FitnessFunction.java
// by = Brian Kim
// description = an interface to describe a fitness function
//

package geneticalgorithm;

/**
 *
 * @author bkim11
 */
public interface FitnessFunction {
    // must return a value between 0 and 1
    public double rate( Object x );
}
