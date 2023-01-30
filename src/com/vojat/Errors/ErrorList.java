package com.vojat.Errors;

public enum ErrorList {
    ERR_404("The file you're looking for wasn't found"),
    ERR_IO("Index is out of bounds"),
    ERR_NPE("Some required variable is equal to null")
    ;

    public final String message;
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    ErrorList (String mesage) {
        this.message = ANSI_RED + mesage + ANSI_RESET;
    }
}
