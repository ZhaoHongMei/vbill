package com.example.vbill.adapter;

import com.example.vbill.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeChartSpinnerAdapter {
    public static List<Map<String,Object>> getListMaps(){
        List<Map<String,Object>> listMaps = new ArrayList<>();

        Map<String,Object> incomeMap = new HashMap<>();
        incomeMap.put("logo", R.drawable.income1);
        incomeMap.put("itemName","收入");

        Map<String,Object> paymentMap = new HashMap<>();
        paymentMap.put("logo", R.drawable.payment1);
        paymentMap.put("itemName","支出");

        listMaps.add(incomeMap);
        listMaps.add(paymentMap);

        return listMaps;
    }
}
