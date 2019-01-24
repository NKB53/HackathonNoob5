package com.example.android.examhelper.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AlgorithmUtils {
    public static double Standardsimilarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Implementation of the Levenshtein Edit Distance
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static String getUniqueWords(List<String> input) {
        List<String> uniquewords = new ArrayList<String>();
        for (String s : input) {
            String[] words = s.replaceAll("[^A-Za-z\\s]", "").toLowerCase().split(" ");
            for (String word : words) {
                if (!uniquewords.contains(word)) {
                    uniquewords.add(word);
                }
            }
        }
        Collections.sort(uniquewords);
        String retS = uniquewords.toString();
        return retS;
    }

    public static HashMap<String, Integer> counter(List<String> questions) {
        HashMap<String, Integer> h = new HashMap<String, Integer>();
        HashMap<String, List<String>> hSimilar = new HashMap<String, List<String>>();
        List<String> list1, list2;
        String oldkey, newKey;

        for (int i = 0; i < questions.size(); i++) {

            for (int j = i + 1; j < questions.size(); j++) {
                if (i == questions.size() - 1) break;
                list1 = Arrays.asList(questions.get(i).split("\\s"));
                list2 = Arrays.asList(questions.get(j).split("\\s"));
                if (Standardsimilarity(getUniqueWords(list1), getUniqueWords(list2)) >= 0.75) {

                    if (h.containsKey(questions.get(i))) {
                        h.put(questions.get(i), h.get(questions.get(i)) + 1);
                        hSimilar.get(questions.get(i)).add(questions.get(j));
                        questions.remove(j);
                        j--;
                    } else {
                        h.put(questions.get(i), 2);
                        List<String> initList = new ArrayList<String>();
                        hSimilar.put(questions.get(i), initList);
                        hSimilar.get(questions.get(i)).add(questions.get(j));
                        questions.remove(j);
                        j--;

                    }

                }

            }

        }

        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(h.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;

    }


}
