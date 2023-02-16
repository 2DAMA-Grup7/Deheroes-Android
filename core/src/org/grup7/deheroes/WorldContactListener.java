package org.grup7.deheroes;

import static org.grup7.deheroes.screens.SinglePlayer.allMobs;
import static org.grup7.deheroes.screens.SinglePlayer.allSpells;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.heroes.Witch;
import org.grup7.deheroes.actors.mobs.PurpleFlame;
import org.grup7.deheroes.actors.mobs.PurpleFlameBoss;
import org.grup7.deheroes.actors.spells.IceBall;


public class WorldContactListener implements ContactListener {

    private final Hero player;

    public WorldContactListener(Hero player) {
        this.player = player;
    }

    @Override
    public void beginContact(Contact cnt) {
        Fixture fa = cnt.getFixtureA();
        Fixture fb = cnt.getFixtureB();
        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        System.out.println(fa.getUserData());
        System.out.println(fb.getUserData());

        if (isSpellContact(fa, fb) || isSpellContact2(fa, fb)) {
            allSpells.forEach(spell -> {
                if (spell.getBody().equals(fb.getBody())) spell.sleep();
            });
            allMobs.forEach(mob -> {
                if (mob.getBody().equals(fa.getBody())) mob.setHP(mob.getHP() - 60);
            });
        }
    }

    @Override
    public void endContact(Contact cnt) {
    }

    private boolean isMobContact(Fixture a, Fixture b) {
        return ((a.getUserData() instanceof Witch && b.getUserData() instanceof PurpleFlame) || (a.getUserData() instanceof PurpleFlame && b.getUserData() instanceof Witch));
    }

    private boolean isSpellContact(Fixture a, Fixture b) {
        return ((a.getUserData() instanceof PurpleFlame && b.getUserData() instanceof IceBall) || (a.getUserData() instanceof IceBall && b.getUserData() instanceof PurpleFlame));
    }

    private boolean isSpellContact2(Fixture a, Fixture b) {
        return ((a.getUserData() instanceof PurpleFlameBoss && b.getUserData() instanceof IceBall) || (a.getUserData() instanceof IceBall && b.getUserData() instanceof PurpleFlameBoss));
    }


    @Override
    public void preSolve(Contact cnt, Manifold manifold) {
        Fixture fa = cnt.getFixtureA();
        Fixture fb = cnt.getFixtureB();
        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;
        if (isMobContact(fa, fb)) {
            player.setHp(player.getHp() - 0.3F);
        }
    }

    @Override
    public void postSolve(Contact cnt, ContactImpulse ci) {

    }

}