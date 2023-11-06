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
    public static DebtorSection getEnumWithValueOf(String value)
    {
        if(value.equals(No_Section.value))
            return No_Section;
        else if(value.equals(First.value))
            return First;
        else if(value.equals(Second.value))
            return Second;
        else if(value.equals(Third.value))
            return Third;
        else return Other;
    }
}
