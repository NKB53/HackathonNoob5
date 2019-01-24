package com.example.android.examhelper;

import java.util.HashMap;
import java.util.List;

public class MapHolder {
    public static HashMap<String, Integer> mH1;
    public static HashMap<String, List<String>> mH2;

    public MapHolder(HashMap<String, Integer> h1, HashMap<String, List<String>> h2) {
        this.mH1 = h1;
        this.mH2 = h2;
    }

}
