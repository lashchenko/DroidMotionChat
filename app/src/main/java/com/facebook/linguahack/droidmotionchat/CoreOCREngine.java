package com.facebook.linguahack.droidmotionchat;


import android.util.Log;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class CoreOCREngine {

    public static String TAG = android.os.Build.MODEL + " CoreOCREngine";

    private Map<Character, BitSet> samples = new HashMap<Character, BitSet>();
//    private Map<Character, BitSet> samples2 = new HashMap<Character, BitSet>();

    public static final Character SYMBOL_NOT_RECOGNIZED = '*';

    public CoreOCREngine() {
        for (char symbol='A'; symbol <= 'Z'; ++symbol ) {
//        for (char symbol='a'; symbol <= 'z'; ++symbol ) {
            samples.put(symbol, Util.convert(GestureView.getTemplate(symbol)));
//            samples2.put(symbol, Util.convert(GestureView.getTemplate2(symbol)));
        }
        samples.put(CoreNetworkEngine.SYMBOL_NEW, Util.convert(GestureView.getTemplate(CoreNetworkEngine.SYMBOL_NEW)));
//        samples2.put(CoreNetworkEngine.SYMBOL_NEW, Util.convert(GestureView.getTemplate2(CoreNetworkEngine.SYMBOL_NEW)));

//        samples.put('√', Util.convert(GestureView.getTemplate('√')));
//        samples.put('∆', Util.convert(GestureView.getTemplate('∆')));
//        samples.put('λ', Util.convert(GestureView.getTemplate('λ')));

        samples.put(' ', Util.convert(GestureView.getTemplate('/')));
//        samples2.put(' ', Util.convert(GestureView.getTemplate2('/')));
    }

    public Character recognize(int[] pattern) {
        BitSet p = Util.convert(pattern);
        Character matchedSymbol = 'A';
        double maxProbability = 0.0;

        for (Character symbol : samples.keySet()) {
            int distance = calculateHammingDistance(p, samples.get(symbol));

//            int distance1 = calculateHammingDistance(p, samples.get(symbol));
//            int distance2 = calculateHammingDistance(p, samples2.get(symbol));
//            int distance = Math.abs(distance1+distance2)/2;

//            int distance = calculateBlockHammingDistance(p, samples.get(symbol));
            double megaK = 1000000.0;
            double R =  megaK / (1.0 + distance*distance);
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

    private Integer calculateBlockHammingDistance(BitSet sample, BitSet pattern) {
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

            boolean eachHasPoints = countPattern > 0 && countSample > 0;
            result += eachHasPoints ? Math.abs(countPattern - countSample) : 0;
        }
        return result;
    }
}
