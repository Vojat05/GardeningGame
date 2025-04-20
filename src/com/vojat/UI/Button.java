package com.vojat.UI;

import com.vojat.Interface.IElement;

public class Button implements IElement {
    public void hover(Runnable hoverAction) {
        if (hoverAction != null) hoverAction.run();
    }

    public void onClick(Runnable onclickAction) {
        if (onclickAction != null) onclickAction.run();
    }
}
