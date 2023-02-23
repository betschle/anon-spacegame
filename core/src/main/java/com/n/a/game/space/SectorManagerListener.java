package com.n.a.game.space;

/**
 * <p>A listener for the sector manager to provide entry points into different
 * stages of the caching mechanism.</p>
 *
 * <p>Note the overlap: onSectorLoaded, onNewSectorGenerated and onSectorSaved may be invoked on the same object in case
 * of a newly generated sector.</p>
 *
 * @param <C>
 */
public interface SectorManagerListener<C> {

    /**
     * Invoked before sectors are being changed.
     * @param oldX old sector x coords
     * @param oldY old sector y coords
     * @param newX new sector x coords
     * @param newY new sector y coords
     */
    void onSectorPreChange(int oldX, int oldY, int newX, int newY);

    /**
     * Invoked after sectors were changed.
     * @param oldX old sector x coords
     * @param oldY old sector y coords
     * @param newX new sector x coords
     * @param newY new sector y coords
     */
    void onSectorPostChange(int oldX, int oldY, int newX, int newY);

    /**
     * Invoked when a new sector was generated.
     * @param sector
     */
    void onNewSectorGenerated(Sector<C> sector);

    /**
     * Invoked when a sector was loaded from disk.
     * @param sector
     */
    void onSectorLoaded(Sector<C> sector);

    /**
     * Invoked when a sector was saved to be cached
     * on disk.
     *
     * @param sector
     */
    void onSectorSaved(Sector<C> sector);
}
