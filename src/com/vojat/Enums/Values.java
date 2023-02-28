package com.vojat.Enums;

public enum Values {
    TOCHANGE(5000),                             // 120000  Production value
    
    TODIE_REDTULIP(20000),                      // 600000  Production value
    TODISSAPEAR_REDTULIP(25000),                // 840000  Production value
    
    TODIE_ROSE(25000),
    TODISSAPEAR_ROSE(30000),
    ;

    public final int value;

    Values(int number) {
        this.value = number;
    }
}
