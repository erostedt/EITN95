import java.util.ArrayList;

public class confidenceInterval {
    public static Double[] getConfidenceInterval(ArrayList< ArrayList<Double> > allTimes, int experiment){
        double mean = 0;
        double var = 0;
        for (ArrayList<Double> times : allTimes) {
            mean += times.get(experiment);
        }
        mean = mean/allTimes.size();

        for (ArrayList<Double> times : allTimes) {
            var += Math.pow(times.get(experiment) - mean, 2);
        }
        var = var/allTimes.size();
        double SE = Math.sqrt(var/allTimes.size());
        double critVal = 1.96;

        Double[] confidenceInterval = new Double[3];
        confidenceInterval[0] = mean - critVal*SE;
        confidenceInterval[1] = mean;
        confidenceInterval[2] = mean + critVal*SE;
        return confidenceInterval;
    }

    public static boolean anyOverlap(ArrayList<Double[]> confidenceIntervals){
        /**
         * Checks if any confidence interval overlaps with another.
         */
        for (int i = 0; i < confidenceIntervals.size(); i++){
            double lower1 = confidenceIntervals.get(i)[0];
            double upper1 = confidenceIntervals.get(i)[2];
            for (int j = 0; j < confidenceIntervals.size(); j++){
                double lower2 = confidenceIntervals.get(j)[0];
                double upper2 = confidenceIntervals.get(j)[2];
                if (i != j){
                    if((upper2 > lower1 && upper2 < upper1) || (lower2 > lower1 && lower2 < upper2)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
