package com.dm;


import android.util.Log;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class CoreOCREngine {

    public static String TAG = "CoreOCREngine";

    private Map<Integer, BitSet> samples = new HashMap<Integer, BitSet>();

    public CoreOCREngine() {
        for (char c='A'; c <= 'Z'; c++ ) {
            samples.put((int) c, Util.convert(Grid.getTemplate(c)));
        }
    }

    public Integer recognize(int[] pattern) {
        BitSet p = Util.convert(pattern);
        int matchedSymbol = 'A';
        double maxProbability = 0.0;

        for (Integer s : samples.keySet()) {
            int distance = calculateHammingDistance(p, samples.get(s));
            double R = 1000000.0 / (1.0 + distance*distance);

            char symbol = (char)s.intValue();
            Log.d(TAG, symbol + " -> " + R);

            if (maxProbability < R) {
                maxProbability = R;
                matchedSymbol = symbol;
            }
        }
        return matchedSymbol;
    }

    private Integer calculateHammingDistance(BitSet sample, BitSet pattern) {
        if (sample.size() != pattern.size()) {
            return 0;
        }

        Integer len = pattern.size();
        Integer result = 0;
        for (int i=0; i<len; ++i) {
            result += sample.get(i) == pattern.get(i) ? 0 : 1;
        }
        return result;
    }
}
