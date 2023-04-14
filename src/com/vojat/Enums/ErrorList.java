package com.vojat.Enums;

public enum ErrorList {
    ERR_404("The thing you're looking for wasn't found"),
    ERR_IO("Index is out of bounds (IO)"),
    ERR_NPE("Required variable is equal to null"),
    ERR_INTERRUPT("The code has been interrupted"),
    ERR_CANTPLANT("You already planted a plant here"),
    ERR_NOPLANT("There is not a plant"),
    ERR_WATER("You do not have any water"),
    ERR_WELL("You are not staning at the well")
    ;

    public final String message;
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    ErrorList(String mesage) {
        this.message = ANSI_RED + mesage + ANSI_RESET;
    }
}
