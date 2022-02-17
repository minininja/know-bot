package org.dorkmaster.knowbot.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChannelMatcher implements Serializable {
    public static final double ALPHA = 0.25;
    List<Map<String, Double>> searchWeights;

    public ChannelMatcher(List<String> sequences) {
        System.out.println("Search sequences: " + sequences);
        searchWeights = new ArrayList<>(sequences.size());
        for (String sequence : sequences) {
            String[] terms = sequence.split("-");
            Map<String, Double> weights = new LinkedHashMap<>(terms.length);
            ExponentialMovingAverage ema = new ExponentialMovingAverage(ALPHA);
            for (int i = terms.length; i > 0; i--) {
                double term = ema.average(i/terms.length);
                weights.put(terms[terms.length - i], term);
            }
            searchWeights.add(weights);
        }
    }

    double add(double orig, Double additional) {
        return orig + (null == additional ? 0 : additional);
    }

    public Collection<Pair<Double, String>> matches(String sequence) {
        String[] tokens = sequence.split("\\s");
        List<Pair<Double, String>> result = new ArrayList<>(searchWeights.size()); // worst case
        for (Map<String, Double> terms : searchWeights) {
            double weight = 0.0;
            for (String t : tokens) {
                weight = add(weight, terms.get(t));
            }
            if (weight > 0.0) {
                result.add(new Pair<Double, String>(weight, String.join("-", terms.keySet())));
            }
        }
        return result.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList());
    }
}
