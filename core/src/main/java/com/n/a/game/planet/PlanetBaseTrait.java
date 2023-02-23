package com.n.a.game.planet;

import com.n.a.game.space.ScanCategory;

/**
 * TODO adjust this class to be able to load data in planetArcheTypes.json
 *
 * Describes a PlanetBaseTrait for Planets. A base trait would be
 * Temperature, Weather and Life and further, consistency of its
 * surface (solid/gaseous). A BaseTrait has levels which are
 * increased or decreased by single {@link PlanetTrait}s.
 * This class is a prototype to help construct individual
 * objects (Prototype Pattern).
 *
 * @deprecated
 *
 */
@Deprecated
public class PlanetBaseTrait {
    /** The Id for reference */
    private String id;
    /** The Display Name */
    private String name;
    /** A General Scan Description of this trait. */
    private String scanDescription;
    /** The classification of this trait.  */
    private int classification;
    /** Scanning category */
    private ScanCategory category;

    /**
     * Convenience Method to gets a Modifier Trait
     * @param baseTrait the trait to modify
     * @param classification the new classification level of the baseTrait
     * @return A copy of the input PlanetBaseTrait that only has its ID, Name and Classification set.
     * The method parameter classification is used for the new classification, the ID and Name remains the same.
     */
    public static PlanetBaseTrait getModifierTrait( PlanetBaseTrait baseTrait, int classification) {
        PlanetBaseTrait modifierTrait = new PlanetBaseTrait();
        modifierTrait.setId(baseTrait.getId());
        modifierTrait.setName(baseTrait.getName());
        return modifierTrait;
    }

    /**
     * Serialization only
     */
    public PlanetBaseTrait() {
    }

    public PlanetBaseTrait(String id, String name, int classification) {
        this.id = id;
        this.name = name;
    }

    public ScanCategory getCategory() {
        return category;
    }

    public void setCategory(ScanCategory category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScanDescription() {
        return scanDescription;
    }

    public void setScanDescription(String scanDescription) {
        this.scanDescription = scanDescription;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }
}
