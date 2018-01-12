package hw4.hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        Map<Integer, Integer> hcCount = new HashMap<>();
        for (Oomage o : oomages) {
            int hc = (o.hashCode() & 0x7FFFFFF) % M;
            int count;
            if (hcCount.containsKey(hc)) {
                hcCount.put(hc, hcCount.get(hc) + 1);
            } else {
                hcCount.put(hc, 1);
            }
        }
        int N = oomages.size();
        for (int count : hcCount.values()) {
            if (count < (double) N / 50 || count > (double) N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
