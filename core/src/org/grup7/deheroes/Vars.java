package org.grup7.deheroes;

import com.badlogic.gdx.math.Vector2;

public class Vars {
    public static final int gameWidth = 960;
    public static final int gameHeight = 640;
    public static final int deadPointX = 3000;
    public static final int deadPointY = 3000;
    public static final float UPDATE_TIME = 1 / 60F;
    public static final Vector2 deadPoint = new Vector2(deadPointX, deadPointY);
    public static final String socketURL = "http://alumnes.inspedralbes.cat:7042";
    public static final String apiURL = "http://alumnes.inspedralbes.cat:7378";

    public static final String localURL = "http://localhost:6369";
}
