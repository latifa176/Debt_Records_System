package com.example.debtrecords;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AmountChange {
    private LocalDateTime dateChanged;
    private float changeAmount;
    private AmountType changeType;

    public AmountChange(LocalDateTime dateChanged, float changeAmount, AmountType changeType) {
        this.dateChanged = dateChanged;
        this.changeAmount = changeAmount;
        this.changeType = changeType;
    }

    public LocalDateTime getDateChanged() {
        return dateChanged;
    }
    public String getFormattedDateChanged(){
        DateTimeFormatter dtf=DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return  dtf.format(dateChanged);
    }

    public float getChangeAmount() {
        return changeAmount;
    }
    public String getChangeAmountString()
    {
        if(changeType==AmountType.Credit)
            return "+"+changeAmount;
        else return "-"+changeAmount;
    }

    public AmountType getChangeType() {
        return changeType;
    }
    public String getAmountChangeTypeString()
    {
        if(changeType==AmountType.Credit)
            return "إضافة";
        else return "خصم";
    }
}
