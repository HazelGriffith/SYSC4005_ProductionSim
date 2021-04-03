package base;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * The SimulationRunner class runs the Model simulation R number of times, and calculates the long-run mean statistics with 95% confidence intervals
 *
 */
public class SimulationRunner {
	public static enum resultType {APC, ABPI1, ABPI2, APC_CONFIDENCE_INTERVAL, ABPI1_CONFIDENCE_INTERVAL, ABPI2_CONFIDENCE_INTERVAL};
	

	
    public static void main(String[] args) {
    	// R is number of replications
    	int R = 117;
    	
		//The seeds are calculated
    	int firstSeed = 1234;
    	int[] seeds = new int[6];
    	for (int i = 0; i < 6; i++) {
    		seeds[i] = firstSeed+i;
    	}
    	
    	HashMap<resultType, Double> originalResults = runReplications(seeds, 2000, false, R);
    	
    	HashMap<resultType, Double> alternateResults = runReplications(seeds, 2000, true, R);
    	
    	System.out.println("ORIGINAL RESULTS");
    	System.out.println("The long run mean product count = " + originalResults.get(resultType.APC)+ "+-" + originalResults.get(resultType.APC_CONFIDENCE_INTERVAL));
    	System.out.println("The long run mean blocked I1 proportion = " + originalResults.get(resultType.ABPI1) + "+-" + originalResults.get(resultType.ABPI1_CONFIDENCE_INTERVAL));
    	System.out.println("The long run mean blocked I2 proportion = " + originalResults.get(resultType.ABPI2) + "+-" + originalResults.get(resultType.ABPI2_CONFIDENCE_INTERVAL));
    	
    	System.out.println("ALTERNATIVE RESULTS");
    	System.out.println("The long run mean product count = " + alternateResults.get(resultType.APC)+ "+-" + alternateResults.get(resultType.APC_CONFIDENCE_INTERVAL));
    	System.out.println("The long run mean blocked I1 proportion = " + alternateResults.get(resultType.ABPI1) + "+-" + alternateResults.get(resultType.ABPI1_CONFIDENCE_INTERVAL));
    	System.out.println("The long run mean blocked I2 proportion = " + alternateResults.get(resultType.ABPI2) + "+-" + alternateResults.get(resultType.ABPI2_CONFIDENCE_INTERVAL));
    	
    }
    
    private static HashMap<resultType, Double> runReplications(int[] seeds, int timeToRun, boolean useAltPolicy, int R) {
    	//This HashMap relates the long-run mean statistics taken and their confidence intervals with their associated values
    	HashMap<resultType, Double> results = new HashMap<>();
    	
    	//This arrayList contains the 3 main stats of each replication
    	ArrayList<double[]> acrossRresults = new ArrayList<>();
    	
    	//long-run mean product count
    	double averageProductCount = 0;
    	
    	//S value of long-run mean product count
    	double APCS = 0;
    	
    	//standard error of the long-run mean product count
    	double APCStandardError;
    	
    	//confidence interval of the long-run mean product count
    	double APCConfidenceInterval;
    	
    	//long-run mean blocked time proportion of I1
    	double averageBlockedProportionI1 = 0;
    	
    	//S value of long-run mean blocked time proportion of I1
    	double ABPI1S = 0;
    	
    	//standard error of the long-run mean blocked time proportion of I1
    	double ABPI1StandardError;
    	
    	//confidence interval of the long-run mean blocked time proportion of I1
    	double ABPI1ConfidenceInterval;
    	
    	// long-run mean blocked time proportion of I2
		double averageBlockedProportionI2 = 0;
		
		//S value of long-run mean blocked time proportion of I2
		double ABPI2S = 0;
		
		//standard error of long-run mean blocked time proportion of I2
		double ABPI2StandardError;
		
		//confidence interval of the long-run mean blocked time proportion of I2
		double ABPI2ConfidenceInterval;
		
		// R replications of the simulation are run
    	for (int i = 0; i < R; i++) {
    		
    		acrossRresults.add(Model.runSimulation(seeds, timeToRun, useAltPolicy));
    		averageProductCount += acrossRresults.get(i)[0];
    		averageBlockedProportionI1 += acrossRresults.get(i)[1];
    		averageBlockedProportionI2 += acrossRresults.get(i)[2];
    	}
    	
    	//averages are calculated
    	averageProductCount /= R;
    	averageBlockedProportionI1 /= R;
    	averageBlockedProportionI2 /= R;
    	
    	//The S values are calculated
    	for (int i = 0; i < R; i++) {
    		APCS += Math.pow((acrossRresults.get(i)[0] - averageProductCount), 2);
    		ABPI1S += Math.pow((acrossRresults.get(i)[1] - averageBlockedProportionI1), 2);
    		ABPI2S += Math.pow((acrossRresults.get(i)[2] - averageBlockedProportionI2), 2);
    	}
    	APCS /= R-1;
    	APCS = Math.sqrt(APCS);
    	ABPI1S /= R-1;
    	ABPI1S = Math.sqrt(ABPI1S);
    	ABPI2S /= R-1;
    	ABPI2S = Math.sqrt(ABPI2S);
    	
    	//standard error is calculated
    	APCStandardError = APCS/Math.sqrt(R);
    	ABPI1StandardError = ABPI1S/Math.sqrt(R);
    	ABPI2StandardError = ABPI2S/Math.sqrt(R);
    	
    	//confidence intervals are calculated with 95% over 117 replications
    	APCConfidenceInterval = (1.98*APCStandardError);
    	ABPI1ConfidenceInterval = (1.98*ABPI1StandardError);
    	ABPI2ConfidenceInterval = (1.98*ABPI2StandardError);
    	
    	results.put(resultType.APC, averageProductCount);
    	results.put(resultType.APC_CONFIDENCE_INTERVAL, APCConfidenceInterval);
    	results.put(resultType.ABPI1, averageBlockedProportionI1);
    	results.put(resultType.ABPI1_CONFIDENCE_INTERVAL, ABPI1ConfidenceInterval);
    	results.put(resultType.ABPI2, averageBlockedProportionI2);
    	results.put(resultType.ABPI2_CONFIDENCE_INTERVAL, ABPI2ConfidenceInterval);
    	
    	return results;
    }
}
