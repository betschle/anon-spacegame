package com.n.a.gfx.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * Original implementation in PlanetGraphics
 */
public class MaskedSprite extends Actor {

    private SpriteBatch maskBatch;
    private Sprite maskSprite;
    private List<Sprite> sprites = new ArrayList<>();

    public MaskedSprite( SpriteBatch batch) {
        this.maskBatch = batch;
    }
    /**
     * Combines alpha mask ofRegion with the mask sprite
     * @param maskBatch
     * @param sprite
     */
    private void drawMask(Batch maskBatch, Sprite sprite) {
        float width = getWidth();
        float height = getHeight();
        float x = getX();
        float y = getY();

        /* Disable RGB color writing, enable alpha writing to the frame buffer. */
        Gdx.gl.glColorMask(false, false, false, true);

        /* Change the blending function for our alpha map. */
        maskBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);

        /* Draw alpha masks. Combine alpha with texture*/
        maskSprite.draw(maskBatch);
        sprite.draw(maskBatch);
        // this.drawTiledTexture(maskBatch, maskSprite, offset, width, height, x, y);
        // this.drawTiledTexture(maskBatch, ofRegion, offset, width, height, x, y);

        /* This blending function makes it so we subtract instead of adding to the alpha map. */
        maskBatch.setBlendFunction(GL20.GL_ZERO, GL20.GL_SRC_ALPHA);

        /* Remove the masked sprite's inverse alpha from the map. */
        this.maskSprite.draw(maskBatch);

        /* Flush the batch to the GPU. */
        maskBatch.flush();
    }

    private void drawIntoMask(Batch maskBatch, Sprite sprite) {
        float width = getWidth();
        float height = getHeight();
        float x = getX();
        float y = getY();
        /* Now that the buffer has our alpha, we simply draw the sprite with the mask applied. */
        Gdx.gl.glColorMask(true, true, true, true);

        /* Change the blending function so the rendered pixels alpha blend with our alpha map. */
        maskBatch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);

        /* Draw our sprite to be masked. */
        this.maskSprite.draw(maskBatch);

        // draw textures
        sprite.draw(maskBatch);
        /* Remember to flush before changing GL states again. */
        maskBatch.flush();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if( maskSprite != null) {
            // TODO setposition on all Sprites otherwise it wont work
            this.maskSprite.setPosition(this.getX(), this.getY());
            maskBatch.setProjectionMatrix( batch.getProjectionMatrix() );
            maskBatch.begin();

            for( Sprite sprite : sprites) {
                // drawMask(maskBatch, sprite);
                drawIntoMask(maskBatch, sprite);
            }
            maskBatch.end();
        }
    }
}
