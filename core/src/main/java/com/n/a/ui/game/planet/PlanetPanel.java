package com.n.a.ui.game.planet;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.n.a.game.space.Planet;
import com.n.a.game.planet.PlanetTrait;
import com.n.a.ui.commons.widgets.ListWithTooltips;
import com.n.a.ui.commons.Separator;
import com.n.a.ui.commons.widgets.TextButtonTabs;

import java.util.List;

public class PlanetPanel extends Table {

    private boolean godmode = false;

    private Planet planet;

    private PlanetDetailPanel planetDetailPanel;
    private ListWithTooltips<TextButton> traitsPanel;

    public PlanetPanel(Skin skin) {
        super(skin);
        this.setBackground("panel_inset_1");
        this.traitsPanel = new ListWithTooltips<TextButton> (skin);
    }

    public void construct() {
        this.add(this.planetDetailPanel).fill().pad(20);
        this.row();
        this.add(new Separator(this.getSkin(), "panel_inset_brass_1"))
                .minSize(250, 10)
                .padBottom(20)
                .expand();
        this.row();
        this.add("Traits:", "default-no-bg");
        this.row();
        this.add(this.traitsPanel).growY().top().pad(20);;
        this.pack();
        this.setGodmode(this.godmode);
    }

    public PlanetDetailPanel getPlanetDetailView() {
        return planetDetailPanel;
    }

    public void setPlanetDetailView(PlanetDetailPanel planetDetailPanel) {
        this.planetDetailPanel = planetDetailPanel;
    }

    public ListWithTooltips<TextButton> getTraitsPanel() {
        return traitsPanel;
    }

    public void setTraitsPanel(ListWithTooltips<TextButton> traitsPanel) {
        this.traitsPanel = traitsPanel;
    }

    public boolean isGodmode() {
        return godmode;
    }

    /**
     * Turn on for omnipotent knowledge
     * @param godmode
     */
    public void setGodmode(boolean godmode) {
        this.godmode = godmode;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;

        this.planetDetailPanel.setModel( getModelByPlanet(planet));
        this.planetDetailPanel.updateFromModel();

        if( this.planet != null) {
            ListWithTooltips.ListWithTooltipsModel modelByPlanetTraits = this.getListModelByPlanetTraits(planet);
            this.traitsPanel.setListModel(modelByPlanetTraits);
        } else {
            this.traitsPanel.setListModel(null);
        }

    }

    private TextButtonTabs.TextButtonTabModel getModelByPlanetTraits(Planet planet) {
        TextButtonTabs.TextButtonTabModel tabModel = new TextButtonTabs.TextButtonTabModel();
        List<PlanetTrait> traits = this.godmode ? planet.getPlanetStats().getTraits() : planet.getPlanetStats().getDiscoveredTraits();
        for( PlanetTrait trait : traits ) {
            tabModel.addEntry(trait.getName(), trait.getDiscoveryDescription() );
        }
        return tabModel;
    }

    private ListWithTooltips.ListWithTooltipsModel getListModelByPlanetTraits(Planet planet) {
        ListWithTooltips.ListWithTooltipsModel tabModel = new ListWithTooltips.ListWithTooltipsModel();
        List<PlanetTrait> traits = this.godmode ? planet.getPlanetStats().getTraits() : planet.getPlanetStats().getDiscoveredTraits();
        for( PlanetTrait trait : traits ) {
            tabModel.addEntry(trait.getIcon(), trait.getName(), "icon_info", trait.getDiscoveryDescription() );
        }
        return tabModel;
    }

    private PlanetDetailPanel.PlanetDetailViewModel getModelByPlanet(Planet planet) {
        if( planet == null) return null;
        PlanetDetailPanel.PlanetDetailViewModel model = new PlanetDetailPanel.PlanetDetailViewModel();
        if( this.godmode ) {
            model.name = planet.getDisplayName(this.godmode);
            model.type = planet.getArchetypeSettings().getName();
            model.size = planet.getGraphics().getPlanetScale();
            model.discovered = planet.getPlanetStats().getPercentageDiscovered();
            // this is bullshit, max values are not correct. Temporary solution
            model.habitability = planet.getPlanetStats().getPlanetRating("classification_life") / 4f;
            model.atmosphere = planet.getPlanetStats().getPlanetRating("classification_weather") / 4f;
            model.temperature = planet.getPlanetStats().getPlanetRating("classification_temperature") / 4f;
            model.radiation = planet.getPlanetStats().getPlanetRating("classification_radiation") / 4f;
            model.toxicity = planet.getPlanetStats().getPlanetRating("classification_radiation") / 4f;
        } else {
            model.name = planet.getDisplayName();
            model.type = planet.getArchetypeSettings().getName();
            model.size = planet.getGraphics().getPlanetScale();
            model.discovered = planet.getPlanetStats().getPercentageDiscovered();
            // this is bullshit, max values are not correct. Temporary solution
            model.habitability = planet.getPlanetStats().getDiscoveredPlanetRating("classification_life") / 4f;
            model.atmosphere = planet.getPlanetStats().getDiscoveredPlanetRating("classification_weather") / 4f;
            model.temperature = planet.getPlanetStats().getDiscoveredPlanetRating("classification_temperature") / 4f;
            model.radiation = planet.getPlanetStats().getDiscoveredPlanetRating("classification_radiation") / 4f;
            model.toxicity = planet.getPlanetStats().getDiscoveredPlanetRating("classification_radiation") / 4f;
        }
        return model;
    }

}
