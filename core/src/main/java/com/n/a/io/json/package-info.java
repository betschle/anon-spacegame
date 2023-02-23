/**
 * Provides an easily extendable and straight-forward subsystem for loading data from
 * JSON file into a {@link com.n.a.game.repository.DataRepository}
 * as usable data source for a {@link com.n.a.game.DataPack}. Right now,
 * it is only possible to convert JSON into any Business Objects.
 * <p>
 * For ease of use and version stability, there are multiple object layers of this system.
 * </p>
 * <ul>
 *  <li><b>XYZ:</b> any business object used by the game.</li>
 *  <li><b>XYZFactory:</b> this is a factory object that creates instances of XYZ based on XYZSettings.</li>
 *  <li><b>XYZSettings:</b> this keyword marks an object to be persisted via JSON</li>
 *  <li><b>JSONToXYZ:</b> this keyword marks an object that converts JSON to instances of XYZSettings.</li>
 *  <li><b>JSONToXYZRepository:</b> this keyword is used for objects that convert JSON into
 *  many objects and making them accessible via DataRepository.</li>
 * </ul>
 * In order to add new game data to load from, all of these layers must be implemented. If objects are only of
 * structural or utility nature (that means, they do not have to be accessible for the generator via ID),
 * only XYZSettings and JSONToXYZ have to be implemented.
 *
 */
package com.n.a.io.json;