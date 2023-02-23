package com.n.a.ui.commons;

/**
 * Provides a basic interface for XYZ GUI Components.
 */
public interface XYZComponent {

    /**
     * Constructs/layouts this component after all its subcomponents
     * have been set. This only should be called once.
     * On multiple calls, this method should work without breaking
     * the component's function or its layout.
     */
    void construct();

    /**
     * Updates the information using the underlying model.
     *
     */
    void updateFromModel();
}
