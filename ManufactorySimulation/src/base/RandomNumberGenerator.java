package base;

/**
 * Generates random numbers using the Linear Congruential Method (LCM)
 */
public class RandomNumberGenerator {
    private double a, c, m, X, lambda;

    /**
     * The RandomNumberGenerator function creates RNG objects with the given parameters
     * @param a
     * @param c
     * @param m
     * @param x
     * @param lambda
     */
    public RandomNumberGenerator(double a, double c, double m, double x, double lambda) {
        this.a = a;
        this.c = c;
        this.m = m;
        X = x;
        this.lambda = lambda;
    }

    /**
     * generateRandomNumber generates the next random number in the sequence
     * @return double R is the random number calculated
     */
    public double generateRandomNumber(){
        double nextX = (a*X+c)%m;
        double R = nextX/m;
        X = nextX;
        return R;
    }

    /**
     * generateRandomVariate function calculates a variate using the generateRandomNumber function according to the exponential distribution given
     * @return
     */
    public double generateRandomVariate(){
        double random = generateRandomNumber();
        double v = (-1/lambda)*Math.log(random);
        return v;
    }
}
