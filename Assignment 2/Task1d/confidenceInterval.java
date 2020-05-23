import java.util.ArrayList;

public class confidenceInterval {
    public static Double[] getConfidenceInterval(ArrayList< ArrayList<Double> > allTimes, int experiment){
        double mean = 0;
        double var = 0;
        for (ArrayList<Double> doubles : allTimes) {
            mean += doubles.get(experiment);
        }
        mean = mean/allTimes.size();

        for (ArrayList<Double> dp : allTimes) {
            var += Math.pow(dp.get(experiment) - mean, 2);
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


    public static boolean anyOverlap(ArrayList< Double[] > confidenceIntervals){
        for (Double[] confidenceInterval: confidenceIntervals){
            for (Double[] other: confidenceIntervals){
                if (!confidenceInterval.equals(other)){
                    if((other[2] >= confidenceInterval[0] && other[0] <= confidenceInterval[2]) || (other[0] >= confidenceInterval[0] && other[0] <= confidenceInterval[2])){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
