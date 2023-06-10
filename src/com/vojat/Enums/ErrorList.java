package com.vojat.Enums;

import com.vojat.garden.Game;

public enum ErrorList {
    ERR_404("The thing you're looking for wasn't found"),
    ERR_IO("IO exception occured"),
    ERR_NPE("Required variable is equal to null"),
    ERR_INTERRUPT("The code has been interrupted"),
    ERR_CANTPLANT("You can not plant here"),
    ERR_NOPLANT("There is not a plant"),
    ERR_WATER("You do not have any water"),
    ERR_WELL("You are not standing near a well"), 
    ERR_RANGE_FAR("This is out of your reach"),
    ERR_RANGE_CLOSE("This is too close")
    ;

    public final String message;

    ErrorList(String mesage) {
        this.message = Game.ANSI_RED + mesage + Game.ANSI_RESET;
    }
}
