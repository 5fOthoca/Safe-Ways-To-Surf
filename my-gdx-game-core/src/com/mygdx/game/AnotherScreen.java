package com.mygdx.game;


import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class AnotherScreen implements Screen {
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private long lastDropTime;
	
	private int score;
	private String yourScoreName;
	BitmapFont yourBitmapFontName;
	
	MyGdxGame game; // Note it's "MyGame" not "Game"


    // constructor to keep a reference to the main Game class
     public AnotherScreen(MyGdxGame game){
             this.game = game;
 	    	score = 0;
 	    	yourScoreName = "score: 0";
 	    	yourBitmapFontName = new BitmapFont();
 		
 		// load the images for the droplet and the bucket, 64x64 pixels each
 	      dropImage = new Texture(Gdx.files.internal("droplet.png"));
 	      bucketImage = new Texture(Gdx.files.internal("bucket.png"));

 	      // load the drop sound effect and the rain background "music"
 	      dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
 	      rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

 	      // start the playback of the background music immediately
 	      rainMusic.setLooping(true);
 	      rainMusic.play();
 	      // create the camera and the SpriteBatch
 	      camera = new OrthographicCamera();
 	      camera.setToOrtho(false, 800, 480);
 	      batch = new SpriteBatch();
 	      // create a Rectangle to logically represent the bucket
 	      bucket = new Rectangle();
 	      bucket.x = 800 - 64 - 20; // center the bucket horizontally
 	      bucket.y = 480 / 2 - 64 / 2; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
 	      bucket.width = 64;
 	      bucket.height = 64;
 		
 	      
 	      // create the raindrops array and spawn the first raindrop
 	      raindrops = new Array<Rectangle>();
 	      spawnRaindrop();

     }
	
/*	@Override
	public void create () {
	    
	    	score = 0;
	    	yourScoreName = "score: 0";
	    	yourBitmapFontName = new BitmapFont();
		
		// load the images for the droplet and the bucket, 64x64 pixels each
	      dropImage = new Texture(Gdx.files.internal("droplet.png"));
	      bucketImage = new Texture(Gdx.files.internal("bucket.png"));

	      // load the drop sound effect and the rain background "music"
	      dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
	      rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

	      // start the playback of the background music immediately
	      rainMusic.setLooping(true);
	      rainMusic.play();
	      // create the camera and the SpriteBatch
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 800, 480);
	      batch = new SpriteBatch();
	      // create a Rectangle to logically represent the bucket
	      bucket = new Rectangle();
	      bucket.x = 800 - 64 - 20; // center the bucket horizontally
	      bucket.y = 480 / 2 - 64 / 2; // bottom left corner of the bucket is 20 pixels above the bottom screen edge
	      bucket.width = 64;
	      bucket.height = 64;
		
	      
	      // create the raindrops array and spawn the first raindrop
	      raindrops = new Array<Rectangle>();
	      spawnRaindrop();
		
	} */

	 private void spawnRaindrop() {
	      Rectangle raindrop = new Rectangle();
	      raindrop.y = MathUtils.random(0, 480-64);
	      raindrop.x = 20;
	      raindrop.width = 64;
	      raindrop.height = 64;
	      raindrops.add(raindrop);
	      lastDropTime = TimeUtils.nanoTime();
	   }
	
	
	
	@Override
	public void render (float delta) {
	      // clear the screen with a dark blue color. The
	      // arguments to glClearColor are the red, green
	      // blue and alpha component in the range [0,1]
	      // of the color to be used to clear the screen.
	      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	      
	      // tell the camera to update its matrices.
	      camera.update();

	      // tell the SpriteBatch to render in the
	      // coordinate system specified by the camera.
	      batch.setProjectionMatrix(camera.combined);
	      
	      // begin a new batch and draw the bucket and
	      // all drops
	      batch.begin();
	      
	      yourBitmapFontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	      yourBitmapFontName.draw(batch, yourScoreName, 25, 100); 
	      
	      batch.draw(bucketImage, bucket.x, bucket.y);
	      for(Rectangle raindrop: raindrops) {
	         batch.draw(dropImage, raindrop.x, raindrop.y);
	      }
	      batch.end();
	      
	      // process user input
	      if(Gdx.input.isTouched()) {
	         Vector3 touchPos = new Vector3();
	         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
	         camera.unproject(touchPos);
	         bucket.y = touchPos.y - 64 / 2;
	      }
	      
	      // make sure the bucket stays within the screen bounds
	      if(bucket.y < 0) bucket.y = 0;
	      if(bucket.y > 480 - 64) bucket.y = 480 - 64;
	      
	      // check if we need to create a new raindrop
	      if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

	      // move the raindrops, remove any that are beneath the bottom edge of
	      // the screen or that hit the bucket. In the later case we play back
	      // a sound effect as well.
	      Iterator<Rectangle> iter = raindrops.iterator();
	      while(iter.hasNext()) {
	         Rectangle raindrop = iter.next();
	         raindrop.x += 200 * Gdx.graphics.getDeltaTime();
	         if(raindrop.x - 64 > 800) iter.remove();
	         if(raindrop.overlaps(bucket)) {	        	 
	             score++;
	             yourScoreName = "score: " + score;	        	 
	            dropSound.play();
	            iter.remove();
	         }
	      }

	}
	
	   @Override
	   public void dispose() {
	      // dispose of all the native resources
	      dropImage.dispose();
	      bucketImage.dispose();
	      dropSound.dispose();
	      rainMusic.dispose();
	      batch.dispose();
	   }
	   @Override
	   public void resize(int width, int height) {
	   }

	   @Override
	   public void pause() {
	   }

	   @Override
	   public void resume() {
	   }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	
}
