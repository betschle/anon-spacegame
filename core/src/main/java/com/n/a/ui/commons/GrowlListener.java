package com.n.a.ui.commons;

// this is unused for now, if this is never going to be useful just delete this
public interface GrowlListener {

    /**
     * Fired when Growl finished fading in/showing.
     * @param growl
     */
    void onShow(Growl growl);
    /**
     * Fired when Growl was finished fading out/hiding.
     * @param growl
     */
    void onHide(Growl growl);
}
