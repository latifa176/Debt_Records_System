package com.example.debtrecords;

public enum DebtorSectionNumber {
    None ("-"),
    One ("1"),
    Two ("2"),
    Three ("3"),
    Four ("4"),
    Five ("5");
    private String value;

    private DebtorSectionNumber(String value) { this.value = value; }
    @Override public String toString(){return value;}
    public static DebtorSectionNumber getEnumWithValueOf(String value)
    {
        if(value.equals(One.value))
            return One;
        else if(value.equals(Two.value))
            return Two;
        else if(value.equals(Three.value))
            return Three;
        else if(value.equals(Four.value))
            return Four;
        else if(value.equals(Five.value))
            return Five;
        else return None;
    }
}
