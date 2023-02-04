package com.vojat.Enums;

public enum Values {
    TODIE(20000),       // 600000  Production value
    TODISSAPEAR(25000),     // 840000  Production value
    TOCHANGE(5000),     // 120000  Production value
    ;


    public final int value;

    Values(int number) {
        this.value = number;
    }
}
