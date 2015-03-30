package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {

	MyGdxGame game; // Note it's "MyGame" not "Game"
	private SpriteBatch batch;
	private BitmapFont font;
	OrthographicCamera camera;
	int sizeY = Gdx.graphics.getWidth();
	int sizeX = Gdx.graphics.getHeight();
	float scale = 1.0f*sizeY;

	// constructor to keep a reference to the main Game class
	public SplashScreen(MyGdxGame game) {
		this.game = game;
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, sizeX, sizeY);
		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void render(float delta) {
		// update and draw stuff
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

		batch.begin();
		font.setScale(4.0f);
		font.draw(batch, "Caduti Nella Rete", sizeX/2, sizeY/2);
		batch.end();
		font.setColor(Color.RED);
		if (Gdx.input.justTouched()){} // use your own criterion here
			//game.setScreen(game.anotherScreen);
			game.setScreen(game.menuScreen);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// called when this screen is set as the screen with game.setScreen();
	}

	@Override
	public void hide() {
		// called when current screen changes from this to a different screen
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// never called automatically
		batch.dispose();
		font.dispose();
	}
}