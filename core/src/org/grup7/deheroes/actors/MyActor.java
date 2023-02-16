package org.grup7.deheroes.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import org.grup7.deheroes.Vars;

public class MyActor extends Actor {
    protected int rows, cols;
    protected Vector2 velocity;
    protected float speed;
    protected float tick;
    protected Body body;
    protected World world;
    protected boolean alive;


    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void awake(Vector2 spawnPoint) {
        body.setTransform(spawnPoint, 0);
        setAlive(true);
    }

    public void sleep() {
        body.setTransform(new Vector2(Vars.deadPointX, Vars.deadPointY), 0);
        setAlive(false);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public void setPosition(Vector2 position) {
        setX(position.x);
        setY(position.y);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void setVelocityY(Float velocityY) {
        this.velocity.y = velocityY;
    }

    public void setVelocityX(Float velocityX) {
        this.velocity.x = velocityX;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getTick() {
        return tick;
    }

    public void setTick(float tick) {
        this.tick = tick;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void dispose() {
        world.destroyBody(getBody());
        this.remove();
    }
}
