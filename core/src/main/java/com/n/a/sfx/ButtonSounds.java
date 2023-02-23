package com.n.a.sfx;

/**
 * Button sound "styles" to configure sounds
 * played on entering certain button states
 */
public class ButtonSounds {

    /** Sound played when button is down/pressed once */
    private String down = "click1";
    /** Sound played when button is unchecked/released */
    private String uncheck = "click2";
    /** Sound played when button is hovered over */
    private String hover;

    public ButtonSounds() {

    }

    /**
     *
     * @param soundDown Sound played when button is down/pressed once
     * @param soundUncheck Sound played when button is unchecked/released
     * @param soundHover Sound played when button is hovered over
     */
    public ButtonSounds( String soundDown, String soundUncheck, String soundHover) {
        this.down = soundDown;
        this.uncheck = soundUncheck;
        this.hover = soundHover;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getUncheck() {
        return uncheck;
    }

    public void setUncheck(String uncheck) {
        this.uncheck = uncheck;
    }

    public String getHover() {
        return hover;
    }

    public void setHover(String hover) {
        this.hover = hover;
    }
}
