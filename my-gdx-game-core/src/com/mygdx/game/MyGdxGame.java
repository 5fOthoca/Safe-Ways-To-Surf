package com.mygdx.game;

import com.badlogic.gdx.Game;



public class MyGdxGame extends Game {
	 

    SplashScreen splashScreen;
    AnotherScreen anotherScreen;
    MenuScreen menuScreen;


   @Override
    public void create() {
            splashScreen = new SplashScreen(this);
            anotherScreen = new AnotherScreen(this);
            menuScreen = new MenuScreen(this);
            setScreen(splashScreen);              
    }
}
