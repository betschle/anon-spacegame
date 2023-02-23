package com.n.a.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.n.a.game.SpaceShip;
import com.n.a.input.controller.NavigationPolling;
import com.n.a.input.controller.ShipInputController;
import com.n.a.util.MathUtil;
import com.n.a.game.EntityID;

/**
 * Spaceship graphics controller.
 */
public class ShipGraphics extends AbstractGraphics {

    private SpaceShip spaceShip;
    private TextureRegion ship;
    public ExhaustGraphics exhaust;
    public NavigationPolling controller = new ShipInputController();
    float currentThrust = 0;

    // properties derived from ship model
    float rotationVelocity = 2;
    final float maxThrust = 15;
    final float strafeSpeed = 0.6f;

    // calculated values for polling
    private Vector2 currentVelocity = new Vector2(0f, 0f);
    private Vector2 oldPosition = new Vector2(0f, 0f); // for speed measurement

    public ShipGraphics(EntityID id) {
        super(id);
        this.addListener(controller);
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public void setSpaceShip(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    /**
     *
     * @return a copy of the current velocity, as a factor in relation
     * to its full thrust
     */
    public Vector2 getCurrentVelocity() {
        return currentVelocity.cpy();
    }

    public void setShipTexture(TextureRegion ship) {
        this.ship = ship;
        this.setSize(this.ship.getRegionWidth(), this.ship.getRegionHeight());
        this.setOrigin(Align.center);
    }

    public float getCurrentThrust() {
        return currentThrust;
    }

    /**
     * Gets the current thruster power as factor
     * @return a float ranging from 0-1
     */
    public float getThrusterPower() {
        return currentThrust / maxThrust;
    }

    /**
     * Resets the current thrust. Equivalents to killing
     * off the fuel supply to the engine instantly.
     */
    public void resetCurrentThrust() {
        this.currentThrust = 0;
    }

    public ExhaustGraphics getExhaust() {
        return exhaust;
    }

    /**
     * Sets the controller which defines how to receive controlling input (user/ai/etc)
     * @param controller
     */
    public void setController(NavigationPolling controller) {
        this.controller = controller;
    }

    /**
     * Sets the thruster power which affects
     * the exhaust animation
     * @param thrusters in percent
     */
    public void setThrusterPower(float thrusters) {
        exhaust.setThrust(thrusters);
    }

    public void act(float delta) {
        super.act(delta);
        this.spaceShip.update(delta);
        currentVelocity.set( ( getX() / oldPosition.x ) - 1 , ( getY() / oldPosition.y ) - 1 );
        this.setThrusterPower(currentThrust / maxThrust * 100);

        oldPosition.x = getX();
        oldPosition.y = getY();

        pollInput(delta);
        applyEngineThrust();
        applyBrakeForce();
    }

    /**
     * Polls the input from the input controller
     */
    private void pollInput(float delta) {
        if( controller.isLeft() ) {
            addAction(Actions.rotateBy(rotationVelocity/2f + (rotationVelocity * (1 - (this.currentThrust / this.maxThrust)) ) ,
                    0.2f, Interpolation.pow2));
        }
        if( controller.isRight() ) {
            addAction(Actions.rotateBy(-rotationVelocity/2f - (rotationVelocity * (1 - (this.currentThrust / this.maxThrust)) )
                    , 0.2f, Interpolation.pow2));
        }
        if( controller.isAccelerate() ) {
            currentThrust = Math.min (maxThrust,  currentThrust + 5f * delta );
        }
        if( controller.isBrake() ) {
            currentThrust -= 10f * delta;
        }
        if( controller.isStrafeLeft() ) {
            Vector2 strafeVelocity = MathUtil.getDirectionalVector(strafeSpeed, (float) java.lang.Math.toRadians(this.getRotation() + 90) );
            addAction(Actions.moveBy(strafeVelocity.x, strafeVelocity.y, 0.5f,  Interpolation.pow2));
        }
        if( controller.isStrafeRight() ) {
            Vector2 strafeVelocity = MathUtil.getDirectionalVector(strafeSpeed, (float) java.lang.Math.toRadians(this.getRotation() - 90) );
            addAction(Actions.moveBy(strafeVelocity.x, strafeVelocity.y, 0.5f, Interpolation.pow2));
        }
    }

    /**
     * Applies thrust from engines to the actor
     */
    private void applyEngineThrust() {
        if( Math.abs( currentThrust ) > 0.04 ) {
            Vector2 lateralVelocity = MathUtil.getDirectionalVector(currentThrust, (float) java.lang.Math.toRadians(this.getRotation()) );
            addAction(Actions.moveBy(lateralVelocity.x, lateralVelocity.y, 0.5f,  Interpolation.pow3));
        }
    }

    /**
     * Apply brake force to the actor if the thrust is negative.
     * The thrust then converges to 0. The mapped backwards key must be hold for reverse travel.
     */
    private void applyBrakeForce() {
        if( currentThrust < 0) {
            currentThrust *= 0.9f;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // draw player
        batch.draw(ship,
                this.getX(),
                this.getY(),
                ship.getRegionWidth()/2f, ship.getRegionHeight()/2f,
                ship.getRegionWidth(), ship.getRegionHeight(),
                this.getScaleX(), this.getScaleY(), this.getRotation());
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {

        super.drawChildren(batch, parentAlpha);
    }
}
