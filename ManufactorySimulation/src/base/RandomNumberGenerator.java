package base;

/**
 * Generates random numbers using the Linear Congruential Method (LCM)
 */
public class RandomNumberGenerator {
    private double a, c, m, X, lambda;

    public RandomNumberGenerator(double a, double c, double m, double x, double lambda) {
        this.a = a;
        this.c = c;
        this.m = m;
        X = x;
        this.lambda = lambda;
    }

    public double generateRandomNumber(){
        double nextX = (a*X+c)%m;
        double R = nextX/m;
        X = nextX;
        return R;
    }

    public double generateRandomVariate(){
        double random = generateRandomNumber();
        double v = (-1/lambda)*Math.log(random);
        return v;
    }
}
