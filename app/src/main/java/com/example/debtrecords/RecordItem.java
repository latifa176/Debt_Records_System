package com.example.debtrecords;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class RecordItem {
    private String nameOfDebtor;
    private LocalDateTime dateCreated;
    private float totalAmount;
    private DebtorSection sectionOfDebtor;
    private DebtorSectionNumber sectionNumOfDebtor;
    private AmountType amountType;

    public RecordItem(String nameOfDebtor, LocalDateTime dateCreated, float totalAmount, DebtorSection sectionOfDebtor, DebtorSectionNumber sectionNumOfDebtor, AmountType amountType) {
        this.nameOfDebtor = nameOfDebtor;
        this.dateCreated = dateCreated;
        this.totalAmount = totalAmount;
        this.sectionOfDebtor = sectionOfDebtor;
        this.sectionNumOfDebtor = sectionNumOfDebtor;
        this.amountType = amountType;
    }
    public String getNameOfDebtor() {
        return nameOfDebtor;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
    public String getFormattedDateCreated(){
        DateTimeFormatter dtf=DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return  dtf.format(dateCreated);
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public DebtorSection getSectionOfDebtor() {
        return sectionOfDebtor;
    }

    public DebtorSectionNumber getSectionNumOfDebtor() {
        return sectionNumOfDebtor;
    }

    public AmountType getAmountType() {
        return amountType;
    }
}
