package base;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * The SimulationRunner class runs the Model simulation R number of times, and calculates the long-run mean statistics with 95% confidence intervals
 *
 */
public class SimulationRunner {
	public static enum resultType {APC, ABPI1, ABPI2, APC_CONFIDENCE_INTERVAL, ABPI1_CONFIDENCE_INTERVAL, ABPI2_CONFIDENCE_INTERVAL};
	
	public static double[] oProductCount, aProductCount, oBlockedProportionI1,aBlockedProportionI1, oBlockedProportionI2,
			aBlockedProportionI2, diffProductCount, diffBlockProportionI1, diffBlockProportionI2;
	
    public static void main(String[] args) {
    	// R is number of replications
    	int R = 117;

		oProductCount = new double[R];
		aProductCount = new double[R];
		oBlockedProportionI1 = new double[R];
		aBlockedProportionI1 = new double[R];
		oBlockedProportionI2 = new double[R];
		aBlockedProportionI2 = new double[R];
		diffProductCount = new double[R];
		diffBlockProportionI1 = new double[R];
		diffBlockProportionI2 = new double[R];
    	
		//The seeds are calculated
    	int firstSeed = 1234;
    	int[] seeds = new int[6];
    	for (int i = 0; i < 6; i++) {
    		seeds[i] = firstSeed+i;
    	}
    	
    	runReplications(seeds, 2000, false, R);
    	
    	runReplications(seeds, 2000, true, R);

    	for(int j=0; j<R; j++){
    		diffProductCount[j] = oProductCount[j]-aProductCount[j];
    		diffBlockProportionI1[j] = oBlockedProportionI1[j] - aBlockedProportionI1[j];
    		diffBlockProportionI2[j] = oBlockedProportionI2[j] - aBlockedProportionI2[j];
		}
    	double sumPC =0.0;
    	double sumBPI1 =0.0;
    	double sumBPI2 =0.0;
    	for(int a=0; a<R; a++){
    		sumPC += diffProductCount[a];
    		sumBPI1 += diffBlockProportionI1[a];
    		sumBPI2 += diffBlockProportionI2[a];
		}
    	double avgPC=0.0;
    	double avgBPI1=0.0;
    	double avgBPI2=0.0;
    	avgPC = sumPC/R;
    	avgBPI1 = sumBPI1/R;
    	avgBPI2 = sumBPI2/R;

    	double svPC=0.0;
    	double svBPI1=0.0;
    	double svBPI2=0.0;
		for(int b=0; b<R; b++){
			svPC += Math.pow(diffProductCount[b]-avgPC, 2);
			svBPI1 += Math.pow(diffBlockProportionI1[b]-avgBPI1, 2);
			svBPI2 += Math.pow(diffBlockProportionI2[b]-avgBPI2, 2);
		}
		svPC /= (R-1);
		svBPI1 /= (R-1);
		svBPI2 /= (R-1);

		double sePC=0.0;
		double seBPI1=0.0;
		double seBPI2=0.0;
		sePC=Math.sqrt(svPC)/Math.sqrt(R);
		seBPI1=Math.sqrt(svBPI1)/Math.sqrt(R);
		seBPI2=Math.sqrt(svBPI2)/Math.sqrt(R);
		double[] ciPC = new double[2];
		double[] ciBPI1 = new double[2];
		double[] ciBPI2 = new double[2];
		//lower bound
		ciPC[0] = avgPC - (1.66*sePC);
		//upper bound
		ciPC[1] = avgPC + (1.66*sePC);
		//lower bound
		ciBPI1[0] = avgBPI1 - (1.66*seBPI1);
		//upper bound
		ciBPI1[1] = avgBPI1 + (1.66*seBPI1);
		//lower bound
		ciBPI2[0] = avgBPI2 - (1.66*seBPI2);
		//upper bound
		ciBPI2[1] = avgBPI2 + (1.66*seBPI2);

		System.out.println("***SAMPLE AVERAGES***");
		System.out.println("The sample average of product count: "+avgPC);
		System.out.println("The sample average of blocked I1 proportion: "+avgBPI1);
		System.out.println("The sample average of blocked I2 proportion: "+avgBPI2);

		System.out.println("***SAMPLE VARIANCES OF DIFFERENCES***");
		System.out.println("The standard variance of product count: "+svPC);
		System.out.println("The standard variance of blocked I1 proportion: "+svBPI1);
		System.out.println("The standard variance of blocked I2 proportion: "+svBPI2);

		System.out.println("***STANDARD ERRORS***");
		System.out.println("The standard error of product count: "+sePC);
		System.out.println("The standard error of blocked I1 proportion: "+seBPI1);
		System.out.println("The standard error of blocked I2 proportion: "+seBPI2);

		System.out.println("***95% CONFIDENCE INTERVALS***");
		System.out.println("The 95% confidence interval of product count: "+ciPC[0]+"<= D <="+ciPC[1]);
		System.out.println("The 95% confidence interval of blocked I1 proportion: "+ciBPI1[0]+"<= D <="+ciBPI1[1]);
		System.out.println("The 95% confidence interval of blocked I2 proportion: "+ciBPI2[0]+"<= D <="+ciBPI2[1]);
    	

    }
    
    private static void runReplications(int[] seeds, int timeToRun, boolean useAltPolicy, int R) {
		// R replications of the simulation are run
		for (int i = 0; i < R; i++) {
			double[] results;
			results = Model.runSimulation(seeds, timeToRun, useAltPolicy);

			if (!useAltPolicy) {
				oProductCount[i] = results[0];
				oBlockedProportionI1[i] = results[1];
				oBlockedProportionI2[i] = results[2];
			} else {
				aProductCount[i] = results[0];
				aBlockedProportionI1[i] = results[1];
				aBlockedProportionI2[i] = results[2];
			}
		}
	}

}
