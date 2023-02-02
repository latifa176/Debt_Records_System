package com.example.debtrecords;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<RecordItem, List<AmountChange>> getData() {
        HashMap<RecordItem, List<AmountChange>> expandableListDetail = new HashMap<RecordItem, List<AmountChange>>();

        List<AmountChange> history = new ArrayList<AmountChange>();
        history.add(new AmountChange(LocalDateTime.now(), 10, AmountType.Debt));


        expandableListDetail.put(new RecordItem
                ("Latifa", LocalDateTime.now(), 10, DebtorSection.First, DebtorSectionNumber.Two, AmountType.Debt
                ), history);
        return expandableListDetail;
    }
}