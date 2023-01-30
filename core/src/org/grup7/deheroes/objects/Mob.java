package org.grup7.deheroes.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import org.grup7.deheroes.helpers.AssetManager;
import org.grup7.deheroes.utils.Settings;

public class Mob extends Actor {


    private final Vector2 position;

    private final int width;
    private final int height;


    private final Rectangle collisionRect;


    public Mob(float x, float y, int width, int height) {
        this.width = width;
        this.height = height;
        position = new Vector2(x,y);

        collisionRect = new Rectangle();

        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);

    }


    public void act(float delta, Vector2 positionHero) {
            if(position.x>positionHero.x){
                this.position.x -= Settings.Mob_VELOCITY * delta;
            }
            if(position.x<positionHero.x){
                this.position.x += Settings.Mob_VELOCITY * delta;
            }
            if(position.y>positionHero.y){
                this.position.y -= Settings.Mob_VELOCITY * delta;
            }
            if(position.y<positionHero.y){
                this.position.y += Settings.Mob_VELOCITY * delta;
            }

            /*float HeroMidX = GameScreen.mainChar.getX()+ GameScreen.mainChar.getWidth()/2;
            float HeroMidY = GameScreen.mainChar.getY()+ GameScreen.mainChar.getHeight()/2;
            float mobMidX = GameScreen.mob.getX()+ GameScreen.mob.getWidth()/2;
            float mobMidY = GameScreen.mob.getY()+ GameScreen.mob.getHeight()/2;
            float distX= HeroMidX-mobMidX;
            float distY = HeroMidY-mobMidY;*/




    }
    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public Texture getTexture(){
        return AssetManager.PurpleFlameSheet;
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getTexture(), position.x, position.y, width, height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }


}
