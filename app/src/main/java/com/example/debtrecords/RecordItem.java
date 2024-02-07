package com.example.debtrecords;

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
    private boolean recordTypeChangeFlag;

    public RecordItem(String nameOfDebtor, LocalDateTime dateCreated, float totalAmount, DebtorSection sectionOfDebtor, DebtorSectionNumber sectionNumOfDebtor, AmountType amountType, String amountChangeHistory)
    {
        this.nameOfDebtor = nameOfDebtor;
        this.dateCreated = dateCreated;
        this.totalAmount = totalAmount;
        this.sectionOfDebtor = sectionOfDebtor;
        this.sectionNumOfDebtor = sectionNumOfDebtor;
        this.amountType = amountType;

        changeHistory = new LinkedList<>();
        String[] amountChangeHistorySegments = amountChangeHistory.split("/");
        for(int i=0; i<amountChangeHistorySegments.length; i++)
        {
            String[] amountChangeItemSegments = amountChangeHistorySegments[i].split(",");
            String dateChanged = amountChangeItemSegments[0];
            String additionalAmount = amountChangeItemSegments[1];
            String additionalAmountType = amountChangeItemSegments[2];
            changeHistory.add(new AmountChange(LocalDateTime.parse(dateChanged), Float.parseFloat(additionalAmount), AmountType.getEnumWithValueOf(additionalAmountType)));
        }
    }
    public boolean addNewAmountChange(LocalDateTime dateCreated, float newAmount, AmountType newAmountType)
    {
        changeHistory.add(new AmountChange(dateCreated, newAmount, newAmountType));

        //Recalculate total amount
        if(amountType == AmountType.Debt)
        {
            if (newAmountType == AmountType.Debt) totalAmount += newAmount;
            else totalAmount -= newAmount;
        }
        else //amountType = Credit
        {
            if(newAmountType == AmountType.Credit) totalAmount += newAmount;
            else totalAmount -= newAmount;
        }

        if (totalAmount == 0)
        {
            totalAmount = 0;
            return false; //to inform caller that it should be deleted
        }
        else if (totalAmount <= 0)
        {
            totalAmount = Math.abs(totalAmount);

            //Switch Debt -> Credit & vice versa
            if (amountType == AmountType.Debt) amountType = AmountType.Credit;
            else amountType = AmountType.Debt;

            recordTypeChangeFlag = true;
        }
        return true;
    }
    public boolean recordTypeShouldChange()
    {
        if (recordTypeChangeFlag)
        {
            recordTypeChangeFlag = false; //resetting flag
            return true;
        }
        return false;
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
