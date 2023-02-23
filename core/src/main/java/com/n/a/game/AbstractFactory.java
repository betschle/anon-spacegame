package com.n.a.game;

import com.n.a.util.sequences.NumberGenerator;
import com.n.a.gfx.GraphicsAssembler;

import java.util.logging.Logger;

/**
 * A Factory that operates on a single datapack.
 */
public class AbstractFactory {
    // TODO this is not implemented
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

    protected DataPack dataPack;
    protected GraphicsAssembler graphicsAssembler;
    protected EntityIDFactory entityIDFactory;
    protected NumberGenerator numberGenerator;

    protected AbstractFactory( DataPack dataPack) {
        this.dataPack = dataPack;
        this.entityIDFactory = dataPack.getEntityIDFactory();
        this.numberGenerator = dataPack.getNumberGenerator();
        this.graphicsAssembler = dataPack.getGraphicsAssembler();
    }
}
