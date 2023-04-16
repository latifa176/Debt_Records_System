package com.example.debtrecords;

public enum AmountType {
    Debt ("عليها"),
    Credit ("لها");
    private String value;

    private AmountType(String value){this.value = value;}
    @Override public String toString(){return value;}
}
