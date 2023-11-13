package com.example.debtrecords;

public enum AmountType {
    Debt ("عليها"),
    Credit ("لها");
    private String value;

    private AmountType(String value){this.value = value;}
    @Override public String toString(){return value;}
    public static AmountType getEnumWithValueOf(String value)
    {
        if(value.equals(Debt.value))
            return Debt;
        else return Credit;
    }
}
