package com.n.a.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.n.a.game.space.Planet;
import com.n.a.util.StandardFormats;
import com.n.a.ui.commons.Header;
import com.n.a.ui.commons.Separator;
import com.n.a.ui.commons.TargetItem;

/**
 * Creates standardized UI components for information purposes,
 * such as Tooltips, Icons, ProgressBar, Notifications, MessageBoxes
 * (Passive Components).
 * Components created here must be simple and contain
 * little logic or dependencies.
 */
class InformationComponentFactory {

    private Skin skin;

    public InformationComponentFactory( Skin skin) {
        this.skin = skin;
    }

    /**
     * Gets a simple progressbar.
     * @return
     */
    public ProgressBar getProgressBar(float min, float max, float stepSize, boolean vertical) {
        ProgressBar progressBar = new ProgressBar(min, max, stepSize, vertical, this.skin, "default");
        progressBar.setValue(0);
        return progressBar;
    }

    /**
     * Gets a simple label.
     * @return
     */
    public Label getLabel(String text, boolean background, int align) {
        Label label = new Label(text, skin, background ? "default" : "default-no-bg");
        label.setAlignment(align);
        return label;
    }

    /**
     * Gets a simple label without background.
     * @return
     */
    public Label getLabel(String text, int align) {
        return getLabel(text, false, align);
    }

    /**
     * Gets a simple label.
     * @return
     */
    public Label getLabel(String text, boolean background) {
        return this.getLabel(text, background, Align.center);
    }

    /**
     * Gets a simple label without background.
     * @return
     */
    public Label getLabel() {
        return this.getLabel("", false);
    }

    /**
     * Gets a title label for menus.
     * @return
     */
    public Label getTitleLabel(String title, boolean background) {
        Label titleLabel = new Label(title, skin, background ? "title" : "title-no-bg");
        titleLabel.setAlignment(Align.center, Align.center);
        return titleLabel;
    }

    /**
     * Creates a standardized Header.
     * @param title
     * @return
     */
    public Header getHeader(String title) {
        Header header = new Header(skin);
        header.setTitleLabel( getTitleLabel(title, false));
        header.setTitleSeparator( new Separator(skin, "panel_inset_brass_1") );
        header.construct();
        return header;
    }

    /**
     * Properly Constructs a TargetItem using a Planet as BO
     * @param planet
     * @param godmode true if name for undiscovered planets is visible
     * @return
     */
    public TargetItem getTargetItem(Planet planet, boolean godmode) {
        String planetName = planet.getDisplayName(godmode);

        TargetItem targetItem = new TargetItem(skin);
        targetItem.construct();
        targetItem.setAsPlanetGraphics( planetName,
                StandardFormats.DISTANCE.format(1000) + " km",
                planet.hasDiscoveredTraits() || godmode ?  planet.getArchetypeSettings().getIcon() : "planetType_unknown");
        targetItem.setUserObject( planet );
        return targetItem;
    }
}
