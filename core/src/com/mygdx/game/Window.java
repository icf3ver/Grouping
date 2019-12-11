package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Window extends ApplicationAdapter {
	public static SpriteBatch batch;
	Fish[] fish = new Fish[300];
	int timer = 50;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		for (int i = 0; i < fish.length; i++){
			fish[i] = new Fish();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for(Fish f: fish){
			f.flock(fish);
			f.edges();
			f.update();
			f.render();
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
