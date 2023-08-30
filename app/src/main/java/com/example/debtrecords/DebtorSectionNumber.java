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
}
