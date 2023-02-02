package com.example.debtrecords;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<AmountChange>> getData() {
        HashMap<String, List<AmountChange>> expandableListDetail = new HashMap<String, List<AmountChange>>();

        List<AmountChange> history = new ArrayList<AmountChange>();
        history.add(new AmountChange(LocalDateTime.now(), 10, AmountType.Debt));


        expandableListDetail.put("History", history);
        return expandableListDetail;
    }
}