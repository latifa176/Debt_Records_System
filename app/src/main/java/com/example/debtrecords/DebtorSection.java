package com.example.debtrecords;

public enum DebtorSection {
    No_Section ("بدون قسم"),
    First ("أولى"),
    Second ("ثاني"),
    Third ("ثالث"),
    Other ("أخرى");
    private String value;

    private DebtorSection(String value)
    {
        this.value = value;
    }
    @Override public String toString()
    {
        return value;
    }
}
