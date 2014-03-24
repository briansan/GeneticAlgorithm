//
// title = Generation.java
// by = Brian Kim
// description = this class defines the behavior 
//  of a population of chromosomes to evolve through 
//  crossovers and mutation
//

package geneticalgorithm;

public abstract class Generation // abbreviation: gen, plural: gens
{
    // 
    // properties (instance variables)
    //

    // a unique name for the generation (optional)
    public String name;
    
    // a counter for each evolution
    protected int count = 0;
    
    // ZZ: state machine?
    /* States
    private int state = 0;
    private final int GenerationStateInit = 0;
    private final int GenerationStateEvolving = 1;
    private final int GenerationStateBestChromosomeFound = 2;
    */
    
    // fitness value array for the gen
    protected double[] fitness; // of the current gen
    protected double[][] fitnessHistory; // ZZ: remember previous fitnesses
    
    // chromosome array represents the population
    protected Chromosome[] population; // of the current gen
    protected Chromosome[][] populationHistory; // ZZ: recall previous gens
    
    // the fitness fn that will rate the chromosomes to evaluate their fitnesses
    // must be set in a subclass constructor
    protected FitnessFunction ff;
    
    protected int population_size;
    
    // probability that a chromosome will mutate: [0, 1]
    protected double mutation_rate; 
    
    // number of bits that will mutate
    protected int mutation_volume;
    
    // reference to the most fit chromosome
    protected Chromosome most_fit;
    
    //
    // accessor methods
    //
    
    // once init, a generation should not let outside forces just set its
    // population
    public int getCount() {return count;}
    public Chromosome[] getPopulation() {return this.population;}
    public int getPopulationSize() {return this.population_size;}
    
    // method to get an array of decoded components of the population
    public Object[] getData()
    {
        // var decls: i for indexing, n for pop size, y is the output array
      int i, n = this.population_size;
      Object[] y = new Object[this.population_size];
      
      // for: each Chromoosome c in the population, set the corresponding
      //  element in the output array to be c's decoded components.
      for (i=0; i<n; i++)
      {
          y[i] = this.population[i].getComponents();
      }
      
      return y;
    }
    
    /*
     fitness function values */
    public double[] getFitness() {return this.fitness;}
    
    /*
     mutation probability */
    public double getMutationRate() {return mutation_rate;}
    public void setMutationRate( double rate ) { mutation_rate = cleanMutationRate(rate); }
    
    /*
     number of bit to mutate */
    public int getMutationVolume() {return mutation_volume;}
    public void setMutationVolume( int n ) { mutation_volume = n; }
    
    /* 
     get most fit chromosome */
    public Chromosome getMostFit() {return most_fit;}
    public double rateMostFit() {return ff.rate(most_fit);}
    
    /*
     get any chromosome */
    public Chromosome getChromosomeAtIndex(int i) {return this.population[i];}
    
    //
    // constructor methods
    //
    
    public Generation()
    {
        this( 10 );
    }
    
    public Generation( int pop )
    {
        this( pop, 0.02 );
    }
    public Generation( int pop, double mut_rate )
    {
        this( pop, mut_rate, 1);
    }
    public Generation( int pop, double mut_rate, int mut_vol )
    {
        this( pop, mut_rate, mut_vol, 0 );
    }
    public Generation( int pop, double mut_rate, int mut_vol, int options )
    {
        // assignment of properties
        this.population_size = pop;
        this.mutation_rate = mut_rate;
        this.mutation_volume = mut_vol;
        
        // init population data and call the rest in its subclass
        this.fitness = new double[pop];
        this.population = new Chromosome[pop];
        this.init();
        
        // generate a population if it hasn't been made yet 
        if (this.population == null)
        {
          // a random bit string
          String[] pop_s = randomPopulation( this.population_size, 27 );
        
          // generate the population
          for (int i = 0; i < this.population_size; i++)
          {
            this.population[i] = new Chromosome( pop_s[i] );
          }
        }
    }
    
    // override to init the fitness function and population
    protected abstract void init();
        
    // 
    // utility methods
    //
    
    // primary function for evolving a population
    public int evolve()
    {
        // var decls: ijk for indexing, chr for dummy chromosome pointer
        //            indicies for holding a sorted array of indicies in the generation
        int i=0,j=0,k=0;
        Chromosome chr;
        int[] indicies;
        
        Chromosome[] new_population = new Chromosome[this.population_size];
        
        // iterate through population and rate each chromosome
        for (i=0; i<this.population_size; i++)
        {
            chr = population[i];
            this.fitness[i] = ff.rate( chr );
        }
        
        // a pretty expensive sorting routine
        indicies = sort(this.fitness);
        
        // likewise, a pretty expensive loop that generates a whole new 
        // generation
        // for each index in the sorted array of indicies
        for (i=0, j=0; i < population_size; j++)
        {
            // get the next best chromosome
            double next_fit = this.fitness[indicies[j]]; // used for later 
            Chromosome next = this.population[indicies[j]];

            // try to mutate next and allow into next generation
            // allow next to pass onto the next generation
            this.mutate(next);
            new_population[i++] = next;

            // for each successive chromosome in the fitness queue
            for (k=j+1; k<indicies.length && i < population_size; k++)
            {
                // calculation variables
                double worse_fit, prob;
                worse_fit = fitness[k];

                // caluclate probability
                prob = worse_fit;

                // the selection test pass condition
                if (Math.random() < prob)
                {
                    // since it passed, it gets to preserve itself 
                    // into the next generation
                    Chromosome worse = this.population[indicies[k]];
                    
                    worse.mutate(mutation_volume);
                    new_population[i++] = worse;
                    
                    if (i < population_size)
                    {
                        double coin = Math.random();
                        if (coin > 0.5)
                            new_population[i++] = next.crossover(worse);
                        else
                            new_population[i++] = worse.crossover(next);
                    }
                }
            }
        }
        
        this.most_fit = this.population[indicies[0]];
        
        // evolve the generation
        // Chromosome[] old = this.population.clone();
        this.population = new_population;
        this.count++;
        
        return this.count;
    }
    
    protected boolean mutate( Chromosome chr )
    {
        // generate a random number to determine mutation
        double rand = Math.random();
        if (rand <= this.mutation_rate)
        {
            chr.mutate(this.mutation_volume);
            return true;
        }
        else return false;
    }
            
    // the reason this is static is because sorting is a routine that you 
    // want to call into the class, not an object of that class
    public static int[] sort( double[] fitnesses )
    {
        // var decls
        int i, j, k;
        int n = fitnesses.length;
        
        // return value: an array of indicies arranged in order based on the
        //   input fitness array
        int[] y = new int[n];
        
        // loop for filling up all fitnesses: O(n) operation
        for (i = 0; i < n; i++)
        {
            // finding the max value in the fitness array and its index
            int max_i = 0;
            double max;
            
            // looping over all the fitnesses again, from the beginning
            // O(n^2): performing n*n ops
            for (j = 0, max = 0; j < n; j++)
            {
                // if we find a possible max value
                if (fitnesses[j] > max)
                {
                    // hold a reference to the previous max value
                    double oldmax = max;
                    // assign max to be the current fitness at j
                    max = fitnesses[j];
                    
                    // a flag to indicate that j already exists in y[]
                    boolean j_exists = false;
                    
                    // loop an index k from 0 to the current index i
                    // ex. at i = 3, we are filling up the third element in 
                    //  our return index array
                    for (k = 0; k < i; k++)
                    {
                        if (y[k] == j) {
                            max = oldmax;
                            j_exists = true;
                            break;
                        }
                    }
                    
                    // if j doesn't exist, fill it into y[]
                    if (!j_exists) {
                        max_i = j;
                    }
                }
            }
            // set the max index to the value of i'th value at y
            y[i] = max_i;
        }
        return y;
    }
    
    // make sure mutation rate is in the range [0, 1]
    public static double cleanMutationRate( double r )
    {
        if (r > 1.0) r = 1.0;
        if (r < 0.0) r = 0.0;
        return r;
    }
    
    // returns an array of random bitstrings
    public static String[] randomPopulation( int n, int n_bits )
    {
        String[] y = new String[n];
        for (int i = 0; i < n; i++)
        {
            y[i] = BitString.randomBitString(n_bits);
        }
        return y;
    }
    
    @Override
    public String toString()
    {
        // var decls
        String y = "g" + count + ": \n";
        int i, n = this.getPopulationSize();

        // get all the fitnesses and populations
        double[] fitnesses = this.getFitness();
        Chromosome[] pop = this.getPopulation();
        
        // print out all the chromosomes and
        // corresponding fitnesses
        for (i = 0; i < n; i++)
        {
            y += "  " + (i+1) + ". Chromosome: ";
            y += pop[i].toString() + "    Fitnesses: ";
            y += fitnesses[i] + "\n";
            
        }
        return y;
    }
}
