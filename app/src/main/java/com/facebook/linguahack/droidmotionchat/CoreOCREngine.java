package com.facebook.linguahack.droidmotionchat;


import android.util.Log;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class CoreOCREngine {

    public static String TAG = android.os.Build.MODEL + " CoreOCREngine";

    private Map<Character, BitSet> samples = new HashMap<Character, BitSet>();

    public CoreOCREngine() {
        for (char symbol='A'; symbol <= 'Z'; ++symbol ) {
//        for (char symbol='a'; symbol <= 'z'; ++symbol ) {
            samples.put(symbol, Util.convert(GestureView.getTemplate(symbol)));
        }
        samples.put(CoreNetworkEngine.SYMBOL_NEW, Util.convert(GestureView.getTemplate(CoreNetworkEngine.SYMBOL_NEW)));
//        samples.put('√', Util.convert(GestureView.getTemplate('√')));
//        samples.put('∆', Util.convert(GestureView.getTemplate('∆')));
//        samples.put('λ', Util.convert(GestureView.getTemplate('λ')));
        samples.put( ' ', Util.convert(GestureView.getTemplate('/')));
    }

    public Character recognize(int[] pattern) {
        BitSet p = Util.convert(pattern);
        Character matchedSymbol = 'A';
        double maxProbability = 0.0;

        for (Character symbol : samples.keySet()) {
            int distance = calculateHammingDistance(p, samples.get(symbol));
            double R = 1000000.0 / (1.0 + distance*distance);
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
