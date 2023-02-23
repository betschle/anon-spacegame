package com.n.a.ui.game.planet;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.n.a.util.StandardFormats;
import com.n.a.ui.commons.LabelValuePair;
import com.n.a.ui.commons.Separator;

/**
 * View to display detailed information about planets.
 */
public class PlanetDetailPanel extends Table {

    private PlanetDetailViewModel model = new PlanetDetailViewModel();

    private LabelValuePair<Label> namePair = new LabelValuePair<Label>();
    private LabelValuePair<Label> typePair = new LabelValuePair<Label>();
    private LabelValuePair<Label> discoveredPair = new LabelValuePair<Label>();
    private LabelValuePair<Label> sizePair = new LabelValuePair<Label>();

    private Separator separator;

    private LabelValuePair<ProgressBar> habitabilityPair = new LabelValuePair<ProgressBar>();
    private LabelValuePair<ProgressBar> atmospherePair = new LabelValuePair<ProgressBar>();
    private LabelValuePair<ProgressBar> temperaturePair = new LabelValuePair<ProgressBar>();
    private LabelValuePair<ProgressBar> radiationPair = new LabelValuePair<ProgressBar>();
    private LabelValuePair<ProgressBar> toxicityPair = new LabelValuePair<ProgressBar>();

    public static class PlanetDetailViewModel {
        public String name;
        public String type;
        public float discovered;
        public float size;
        public float habitability;
        public float atmosphere;
        public float temperature;
        public float radiation;
        public float toxicity;
    }
    public PlanetDetailPanel(Skin skin) {
        super(skin);
    }

    public void construct() {
        this.clearActions();
        this.clearChildren();

        LabelValuePair<?>[] pairs = new LabelValuePair[] {
                this.namePair, this.typePair, this.discoveredPair, this.sizePair,
                null, //  creates a separator here
                this.habitabilityPair, this.atmospherePair, this.temperaturePair,
                this.radiationPair, this.toxicityPair
        };

        for( LabelValuePair<?> pair : pairs) {
            if( pair == null)  {
                this.add(this.separator).colspan(2).minHeight(10f).pad(20, 5, 20, 5).fill();
                this.row();
                continue;
            }
            this.add(pair.getLabel()).padRight(10).fill();
            this.add(pair.getValue()).minWidth(120f).pad(3).fill();
            this.row();
        }
    }

    /**
     * Applies values from model to display.
     */
    public void updateFromModel() {
        if( this.model != null) {
            this.namePair.getValue().setText(this.model.name);
            this.typePair.getValue().setText(this.model.type);
            this.discoveredPair.getValue().setText(StandardFormats.PERCENT.format(this.model.discovered) + " %");
            this.sizePair.getValue().setText(StandardFormats.PERCENT.format(this.model.size) + " er");
            this.habitabilityPair.getValue().setValue(this.model.habitability);
            this.atmospherePair.getValue().setValue(this.model.atmosphere);
            this.temperaturePair.getValue().setValue(this.model.temperature);
            this.radiationPair.getValue().setValue(this.model.radiation);
            this.toxicityPair.getValue().setValue(this.model.toxicity);
        } else {
            this.namePair.getValue().setText("-");
            this.typePair.getValue().setText("-");
            this.discoveredPair.getValue().setText("-");
            this.sizePair.getValue().setText("-");
            this.habitabilityPair.getValue().setValue(0);
            this.atmospherePair.getValue().setValue(0);
            this.temperaturePair.getValue().setValue(0);
            this.radiationPair.getValue().setValue(0);
            this.toxicityPair.getValue().setValue(0);
        }
    }

    public PlanetDetailViewModel getModel() {
        return model;
    }

    public void setModel(PlanetDetailViewModel model) {
        this.model = model;
        this.updateFromModel();
    }

    public Separator getSeparator() {
        return separator;
    }

    public void setSeparator(Separator separator) {
        this.separator = separator;
    }

    public LabelValuePair<Label> getNamePair() {
        return namePair;
    }

    public void setNamePair(LabelValuePair<Label> namePair) {
        this.namePair = namePair;
    }

    public LabelValuePair<Label> getTypePair() {
        return typePair;
    }

    public void setTypePair(LabelValuePair<Label> typePair) {
        this.typePair = typePair;
    }

    public LabelValuePair<Label> getDiscoveredPair() {
        return discoveredPair;
    }

    public void setDiscoveredPair(LabelValuePair<Label> discoveredPair) {
        this.discoveredPair = discoveredPair;
    }

    public LabelValuePair<Label> getSizePair() {
        return sizePair;
    }

    public void setSizePair(LabelValuePair<Label> sizePair) {
        this.sizePair = sizePair;
    }

    public LabelValuePair<ProgressBar> getHabitabilityPair() {
        return habitabilityPair;
    }

    public void setHabitabilityPair(LabelValuePair<ProgressBar> habitabilityPair) {
        this.habitabilityPair = habitabilityPair;
    }

    public LabelValuePair<ProgressBar> getAtmospherePair() {
        return atmospherePair;
    }

    public void setAtmospherePair(LabelValuePair<ProgressBar> atmospherePair) {
        this.atmospherePair = atmospherePair;
    }

    public LabelValuePair<ProgressBar> getTemperaturePair() {
        return temperaturePair;
    }

    public void setTemperaturePair(LabelValuePair<ProgressBar> temperaturePair) {
        this.temperaturePair = temperaturePair;
    }

    public LabelValuePair<ProgressBar> getRadiationPair() {
        return radiationPair;
    }

    public void setRadiationPair(LabelValuePair<ProgressBar> radiationPair) {
        this.radiationPair = radiationPair;
    }

    public LabelValuePair<ProgressBar> getToxicityPair() {
        return toxicityPair;
    }

    public void setToxicityPair(LabelValuePair<ProgressBar> toxicityPair) {
        this.toxicityPair = toxicityPair;
    }
}
