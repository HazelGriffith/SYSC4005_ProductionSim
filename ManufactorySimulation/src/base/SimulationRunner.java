package base;

import java.util.ArrayList;

public class SimulationRunner {
    public static void main(String[] args) {
    	int R = 117;
    	ArrayList<double[]> acrossRresults = new ArrayList<>();
    	double averageProductCount = 0;
    	double APCS = 0;
    	double APCStandardError;
    	double APCConfidenceInterval;
    	double averageBlockedProportionI1 = 0;
    	double ABPI1S = 0;
    	double ABPI1StandardError;
    	double ABPI1ConfidenceInterval;
		double averageBlockedProportionI2 = 0;
		double ABPI2S = 0;
		double ABPI2StandardError;
		double ABPI2ConfidenceInterval;
    	int firstSeed = 1234;
    	int[] seeds = new int[6];
    	for (int i = 0; i < 6; i++) {
    		seeds[i] = firstSeed+i;
    	}
        //int[] seeds = {1234,1234,1234,1234,1234,1234};
    	for (int i = 0; i < R; i++) {
    		//Model.runSimulation(seeds, 2000);
    		acrossRresults.add(Model.runSimulation(seeds, 2000));
    		averageProductCount += acrossRresults.get(i)[0];
    		averageBlockedProportionI1 += acrossRresults.get(i)[1];
    		averageBlockedProportionI2 += acrossRresults.get(i)[2];
    	}
    	averageProductCount /= R;
    	averageBlockedProportionI1 /= R;
    	averageBlockedProportionI2 /= R;
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
    	
    	APCStandardError = APCS/Math.sqrt(R);
    	ABPI1StandardError = ABPI1S/Math.sqrt(R);
    	ABPI2StandardError = ABPI2S/Math.sqrt(R);
    	
    	APCConfidenceInterval = (1.98*APCStandardError);
    	ABPI1ConfidenceInterval = (1.98*ABPI1StandardError);
    	ABPI2ConfidenceInterval = (1.98*ABPI2StandardError);
    	
    	System.out.println("The long run mean product count = " + averageProductCount+ "+-" + APCConfidenceInterval);
    	System.out.println("The long run mean blocked I1 proportion = " + averageBlockedProportionI1 + "+-" + ABPI1ConfidenceInterval);
    	System.out.println("The long run mean blocked I2 proportion = " + averageBlockedProportionI2 + "+-" + ABPI2ConfidenceInterval);
    }
}
