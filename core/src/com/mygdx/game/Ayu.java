package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.screens.mainMenu.MainMenuScreen;
import com.mygdx.game.screens.playing.ScreenAyu;

public class Ayu extends Game{

	AssetManager assetManager;
	public SpriteBatch batch;
	public BitmapFont font;

	public AssetManager getAssetManager() {
		return assetManager;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		font = new BitmapFont();
		Gdx.app.setLogLevel(Logger.DEBUG);
		String path = "gameplay/gameplay.atlas";
		TextureAtlas atlas = new TextureAtlas(path);
		for (TextureAtlas.AtlasRegion tr:atlas.getRegions()) {
			System.out.println("public static final String "+tr.name.toUpperCase()+" = \""+tr.name+"\"; //"+tr.packedWidth+"x"+tr.packedHeight);
		}
		for (TextureAtlas.AtlasRegion tr:atlas.getRegions()) {
			System.out.println("private TextureRegion "+tr.name+"Region;");
		}
		for (TextureAtlas.AtlasRegion tr:atlas.getRegions()) {
			System.out.println(tr.name+"Region"+" = gamePlayAtlas.findRegion(RegionNames."+tr.name.toUpperCase()+");");
		}
		assetManager.load(AssetDescriptors.GAME_PLAY);
		assetManager.load(AssetDescriptors.RESET_SOUND);
		assetManager.load(AssetDescriptors.PLACEHOLDER_IMAGE);
		assetManager.load(AssetDescriptors.GAME_SKIN);
		assetManager.finishLoading();

		selectFirstScreen();
		//this.setScreen(new MainMenuScreen(this));
	}

	public void selectFirstScreen() {
		setScreen(new MainMenuScreen(this));
	}

	public void safeExit() {
		Gdx.app.exit();
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		screen.dispose();
		assetManager.dispose();
	}

}
