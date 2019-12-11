package com.mygdx.game;

import apple.laf.JRSUIConstants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Fish {
    private Texture texture = new Texture("assets/arrow.png");
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 acceleration;
    private float maxForce = 0.2f;
    private float maxSpeed = 7f;

    Fish(){
        Random rand = new Random();

        float random = rand.nextFloat();

        position = new Vector2(rand.nextInt(800),rand.nextInt(800));
        acceleration = new Vector2(0,0);
        velocity = new Vector2();

        velocity = velocity.setToRandomDirection();
        velocity = velocity.set(velocity.x * random,velocity.y * random);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 align(Fish[] fish){
        int perception = 100;
        float total = 0;
        Vector2 steering = new Vector2();
        for(Fish f: fish){
            if (f != this && f.getPosition().dst(getPosition()) <= perception){
                steering.add(f.getVelocity());
                total += 1;
            }
        }
        if (total > 0){
            steering.scl(1/total);
            steering.setLength(maxSpeed);
            steering.sub(velocity);
            steering.limit(maxForce);
        }

        return steering;
    }



    public Vector2 cohesion(Fish[] fish){
        int perception = 1000;
        float total = 0;
        Vector2 steering = new Vector2();
        for(Fish f: fish){
            if (f != this && f.getPosition().dst(getPosition()) <= perception){
                steering.add(f.getPosition());
                total += 1;
            }
        }
        if (total > 0){
            steering.scl(1/total);
            steering.sub(position);
            steering.setLength(maxSpeed);
            steering.sub(velocity);
            steering.limit(maxForce - 0.1f);
        }

        return steering;
    }

    public Vector2 separation(Fish[] fish){
        int perception = 20;
        float total = 0;
        Vector2 steering = new Vector2();
        for(Fish f: fish){
            if (f != this && f.getPosition().dst(getPosition()) <= perception){
                Vector2 diff = position.cpy().sub(f.getPosition());
                diff.scl(1/f.getPosition().dst(position));
                steering.add(diff);
                total += 1;
            }
        }
        if (total > 0){
            steering.scl(1/total);
            steering.setLength(maxSpeed);
            steering.sub(velocity);
            steering.limit(maxForce + 0.05f);
        }

        return steering;
    }



    public void edges(){
        if (position.x > 800){
            position.x = 1;
        }else if (position.x < 0){
            position.x = 799;
        }
        if (position.y > 800){
            position.y = 1;
        }else if (position.y < 0){
            position.y = 799;
        }
    }


    public void flock(Fish[] fish){
        Vector2 alignment = align(fish);
        Vector2 cohesion = cohesion(fish);
        Vector2 separation = separation(fish);
        acceleration.add(alignment);
        acceleration.add(cohesion);
        acceleration.add(separation);
    }

    public void update(){
        position.add(velocity);
        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        acceleration.set(0, 0);
    }

    public void render(){
        Window.batch.draw(texture, position.x, position.y, 10,10);
    }
}
