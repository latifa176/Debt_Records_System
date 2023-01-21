package com.example.debtrecords;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;
import java.util.List;

public class RecordItem
{
    private String nameOfDebtor;
    private LocalDateTime dateCreated;
    private float totalAmount;
    private DebtorSection sectionOfDebtor;
    private DebtorSectionNumber sectionNumOfDebtor;
    private AmountType amountType;
    private List<AmountChange> changeHistory;

    public RecordItem(String nameOfDebtor, LocalDateTime dateCreated, float totalAmount, DebtorSection sectionOfDebtor, DebtorSectionNumber sectionNumOfDebtor, AmountType amountType)
    {
        this.nameOfDebtor = nameOfDebtor;
        this.dateCreated = dateCreated;
        this.totalAmount = totalAmount;
        this.sectionOfDebtor = sectionOfDebtor;
        this.sectionNumOfDebtor = sectionNumOfDebtor;
        this.amountType = amountType;
        changeHistory = new LinkedList<>();
        changeHistory.add(new AmountChange(dateCreated, totalAmount, amountType));
    }
    public String getNameOfDebtor() {
        return nameOfDebtor;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
    public String getFormattedDateCreated()
    {
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

    public List<AmountChange> getChangeHistory() { return  changeHistory; }
}
