import java.util.ArrayList;

public class CI {
    public static Double[] calc95CI(ArrayList<ArrayList<Double>> dps, int idx){
        double mean = 0;
        double var_ = 0;
        for (ArrayList<Double> doubles : dps) {
            mean += doubles.get(idx);
        }
        mean = mean/dps.size();

        for (ArrayList<Double> dp : dps) {
            var_ += Math.pow(dp.get(idx) - mean, 2);
        }
        var_ = var_/dps.size();


        Double[] CI = new Double[3];
        CI[0] = mean - 1.96*Math.sqrt(var_/dps.size());
        CI[1] = mean;
        CI[2] = mean + 1.96*Math.sqrt(var_/dps.size());
        return CI;
    }


    public static boolean overlappingCI(ArrayList<Double[]> CIs){
        for (int i = 0; i < CIs.size(); i++){
            double lb = CIs.get(i)[0];
            double ub = CIs.get(i)[2];
            for (int j = 0; j < CIs.size(); j++){
                if (i != j){
                    if((CIs.get(j)[2] >= lb && CIs.get(j)[2] <= ub) || (CIs.get(j)[0] >= lb && CIs.get(j)[0] <= ub)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
