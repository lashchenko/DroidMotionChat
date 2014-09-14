package com.facebook.linguahack.droidmotionchat;


import android.util.Log;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class CoreOCREngine {

    public static String TAG = android.os.Build.MODEL + " CoreOCREngine";

    private Map<Character, BitSet> samples = new HashMap<Character, BitSet>();

    public static final Character SYMBOL_NOT_RECOGNIZED = '*';

    public CoreOCREngine() {
        for (char symbol='A'; symbol <= 'Z'; ++symbol ) {
//        for (char symbol='a'; symbol <= 'z'; ++symbol ) {
            samples.put(symbol, Util.convert(GestureView.getTemplate(symbol)));
        }
        samples.put(CoreNetworkEngine.SYMBOL_NEW, Util.convert(GestureView.getTemplate(CoreNetworkEngine.SYMBOL_NEW)));
//        samples.put('√', Util.convert(GestureView.getTemplate('√')));
//        samples.put('∆', Util.convert(GestureView.getTemplate('∆')));
//        samples.put('λ', Util.convert(GestureView.getTemplate('λ')));
    }

    public Character recognize(int[] pattern) {
        BitSet p = Util.convert(pattern);
        Character matchedSymbol = 'A';
        double maxProbability = 0.0;

        for (Character symbol : samples.keySet()) {
            int distance = calculateHammingDistance(p, samples.get(symbol));
            double megaK = 1000000.0;
            double R =  megaK / (1.0 + distance*distance);
            Log.d(TAG, symbol + " -> " + R);

            double vD = calculateVDistance(p, samples.get(symbol));
            double vR = megaK / (1.0 + vD*vD);
            Log.d(TAG, symbol + " ----> " + vR);

            if (maxProbability < R) {
                maxProbability = R;
                matchedSymbol = symbol;
            }
        }

//        double nothing = 1000000.0 / (1+Math.pow(MainActivity.N, 4));
//        double limit = nothing * 5;
//        if (maxProbability < limit) {
//            Log.d("NOTHING ::: ", "" + nothing + " " + limit + "  " + maxProbability + " " + matchedSymbol);
//            return SYMBOL_NOT_RECOGNIZED;
//        }
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

    private Integer calculateVDistance(BitSet sample, BitSet pattern) {
        if (sample.size() != pattern.size()) {
            return 0;
        }

        Integer len = pattern.size();
        Integer result = 0;

        int limit = 5;
        for (int i=0; i<len; i+=limit) {

            int countSample = 0;
            int countPattern = 0;

            for (int j=0; j<limit; ++j) {
                countSample += sample.get(i+j) ? 0 : 1;
                countPattern += pattern.get(i+j) ? 0 : 1;
            }

            boolean eachEmpty = countPattern == 0 && countSample == 0;
            boolean eachHasPoints = countPattern > 0 && countSample > 0;
//            if (eachEmpty || eachHasPoints) {
//                result += Math.abs(countPattern - countSample);
//                result += Math.abs(countPattern - countSample);
//            }
//            result += sample.get(i) == pattern.get(i) ? 0 : 1;
            result += (!(eachEmpty || eachHasPoints)) ? 1 : 0;
        }
        return result;
    }
}
