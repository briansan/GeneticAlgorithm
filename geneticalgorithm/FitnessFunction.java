//
// title = FitnessFunction.java
// by = Brian Kim
// description = an interface to declare a fitness function
//

package geneticalgorithm;

public interface FitnessFunction {

    // returns a value between 0 and 1
    public double rate( Object x );
}
