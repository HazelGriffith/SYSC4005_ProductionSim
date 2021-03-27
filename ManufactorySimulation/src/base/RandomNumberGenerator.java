package base;

/**
 * Generates random numbers using the Linear Congruential Method (LCM)
 */
public class RandomNumberGenerator {
    private int a, c, m, X;

    public RandomNumberGenerator(int a, int c, int m, int x) {
        this.a = a;
        this.c = c;
        this.m = m;
        X = x;
    }

    private double generateRandomNumber(){
        int nextX = (a*X+c)%m;
        double R = nextX/m;
        X = nextX;
        return R;
    }

    public double generateRandomVariate(){
        return generateRandomNumber();
    }
}
