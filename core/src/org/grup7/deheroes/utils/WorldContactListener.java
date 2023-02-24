package org.grup7.deheroes.utils;

import static org.grup7.deheroes.screens.SinglePlayer.allMobs;
import static org.grup7.deheroes.screens.SinglePlayer.allSpells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.grup7.deheroes.actors.heroes.Hero;
import org.grup7.deheroes.actors.mobs.Mob;
import org.grup7.deheroes.actors.spells.Explosion;
import org.grup7.deheroes.actors.spells.Spell;


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

        if (isMobContactHero(fa, fb))
            Gdx.audio.newSound(Gdx.files.internal(Assets.Sounds.heroHit)).play();


        //System.out.println(fa.getUserData());System.out.println(fb.getUserData());
        if (isSpellContactMob(fa, fb)) {
            allSpells.forEach(spell -> {
                if (spell.getBody().equals(fa.getBody())) spell.setHP(0);
            });
            allMobs.forEach(mob -> {
                if (mob.getBody().equals(fb.getBody())) {
                    mob.setHP(mob.getHP() - 30);
                    mob.getHitSound().play();
                }
            });
        }

    }

    @Override
    public void endContact(Contact cnt) {
        Fixture fa = cnt.getFixtureA();
        Fixture fb = cnt.getFixtureB();
        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        if (isExplosionContactHero(fa, fb)) {
            player.setHp(player.getHp() - 20);
        }
    }

    private boolean isMobContactHero(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Hero && b.getUserData() instanceof Mob);
    }

    private boolean isSpellContactMob(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Spell && b.getUserData() instanceof Mob);
    }

    private boolean isExplosionContactHero(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Hero && b.getUserData() instanceof Explosion);
    }


    @Override
    public void preSolve(Contact cnt, Manifold manifold) {
        Fixture fa = cnt.getFixtureA();
        Fixture fb = cnt.getFixtureB();
        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        if (isMobContactHero(fa, fb)) player.setHp(player.getHp() - 0.3F);

    }

    @Override
    public void postSolve(Contact cnt, ContactImpulse ci) {

    }

}