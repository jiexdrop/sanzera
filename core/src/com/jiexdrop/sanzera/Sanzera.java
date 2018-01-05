package com.jiexdrop.sanzera;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jiexdrop.sanzera.view.screens.GameScreen;
import com.jiexdrop.sanzera.view.screens.LoadingScreen;
import com.jiexdrop.sanzera.view.screens.MenuScreen;
import com.jiexdrop.sanzera.view.screens.PreferencesScreen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Sanzera extends Game {
    public static final float TIME_STEP = 1/60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS= 2;
    public static final int PTM_RATIO = 24;


    private LoadingScreen loadingScreen;
	private PreferencesScreen preferencesScreen;
	private MenuScreen menuScreen;
	private GameScreen mainScreen;
	public AssetManager manager = new AssetManager();

	public final static int MENU = 0;
	public final static int PREFERENCES = 1;
	public final static int APPLICATION = 2;

	public final static String NAME = "Sanzera";
	public final static String ATLAS = NAME + ".atlas";
    public final static String SKIN = "skin/metal-ui.json";
    public final static String SKIN_ATLAS = "skin/metal-ui.atlas";
	public final static String DAT_FILE_SAVE= ".dat";
	public final static String ACTUAL_LEVEL = "level";

	public ArrayList<String> levels;


    public Preferences prefs;

	@Override
	public void create () {
        prefs = Gdx.app.getPreferences(NAME);
		loadLevels();
	    loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
		Gdx.input.setCatchBackKey(true);
	}

	public void changeScreen(int screen){
		switch(screen){
			case MENU:
				if(menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case PREFERENCES:
				if(preferencesScreen == null) preferencesScreen = new PreferencesScreen(this);
				this.setScreen(preferencesScreen);
				break;
			case APPLICATION:
				if(mainScreen == null) mainScreen = new GameScreen(this);
				this.setScreen(mainScreen);
				break;
		}
	}

	public String addLevel(){
		int n = levels.size() + 1;
		String res = "level"+n;
		levels.add(res);
		saveLevels();
		return res;
	}

	public void saveLevels(){

		FileHandle fl = Gdx.files.local("levels.dat");
		ObjectOutputStream out;
		try {
			if(fl.exists()) {

                out = new ObjectOutputStream(fl.write(false));

				out.writeObject(levels);

				out.flush();

				out.close();
			} else {
				System.out.println("w?");
			}

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void loadLevels(){
		FileHandle fl = Gdx.files.internal("levels.dat");
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			if(fl.exists()) {
				in = new ObjectInputStream(fl.read());

				levels = (ArrayList<String>) in.readObject();

				for (String l: levels) {
					System.out.println(l);
				}

				in.close();
			} else {
				System.out.println("New Game!!");
				out = new ObjectOutputStream(fl.write(true));

				levels = new ArrayList<String>();
				levels.add("level1");
				out.writeObject(levels);

				out.flush();

				out.close();
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void dispose(){
		manager.dispose();
	}

	public void nextLevel() {
		String actual = prefs.getString(Sanzera.ACTUAL_LEVEL);
		int i = levels.indexOf(actual) + 1;
		if(i<levels.size())
        prefs.putString(Sanzera.ACTUAL_LEVEL, levels.get(i));
        prefs.flush();
		changeScreen(Sanzera.APPLICATION);
	}
}
