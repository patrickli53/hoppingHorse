package com.patrickli.hoppinghorse;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class HoppingHorse extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture topTube;
	Texture bottomTube;
	Texture[] horses;
	Texture gameOver;
	int horseState = 0;
	float horseY = 0;
	float velocity = 0;
	Circle horCir;
	//ShapeRenderer shapeRenderer;

	int gameState = 0;
	float gravity = 2;

	int score = 0;
	int scoringTube = 0;
	BitmapFont font;

	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectanlges;
	Rectangle[] bottomTubeRectanlges;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameOver = new Texture("gameover.png");
		//shapeRenderer = new ShapeRenderer();

		horses = new Texture[2];
		horses[0] = new Texture("horse.png");
		horses[1] = new Texture("horse1.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		topTubeRectanlges = new Rectangle[numberOfTubes];
		bottomTubeRectanlges = new Rectangle[numberOfTubes];

		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		horCir = new Circle();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		startGame();

	}


	public void startGame(){
		horseY = Gdx.graphics.getHeight() / 2 - horses[0].getHeight() / 2;

		for(int i = 0; i < numberOfTubes; i++) {

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i*distanceBetweenTubes;

			topTubeRectanlges[i] = new Rectangle();
			bottomTubeRectanlges[i] = new Rectangle();
		}
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {
			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score++;

				Gdx.app.log("score", String.valueOf(score));

				if (scoringTube < numberOfTubes - 1){
					scoringTube++;
				} else{
					scoringTube = 0;
				}
			}
			if (Gdx.input.justTouched()) {

				velocity = -25;


			}
			for(int i = 0; i < numberOfTubes; i++) {

				if(tubeX[i] < -topTube.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else{
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectanlges[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectanlges[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			}
			if (horseY > 0) {

				velocity = velocity + gravity;
				horseY -= velocity;

			}else{
				gameState = 2;
			}

		} else if(gameState == 0){

			if (Gdx.input.justTouched()) {

				gameState = 1;

			}

		} else if(gameState == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight()/2);
			if (Gdx.input.justTouched()) {

				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;

			}
		}


		if (horseState == 0) {
			horseState = 1;
		} else {
			horseState = 0;
		}

		batch.draw(horses[horseState], Gdx.graphics.getWidth() / 2 - horses[horseState].getWidth() / 2, horseY);
		font.draw(batch, String.valueOf(score), 100,200);
		batch.end();

		horCir.set(Gdx.graphics.getWidth() / 2, horseY + horses[horseState].getHeight() / 2, horses[horseState].getWidth() /2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(horCir.x, horCir.y, horCir.radius);
		for(int i = 0; i < numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			if(Intersector.overlaps(horCir, topTubeRectanlges[i])|| Intersector.overlaps(horCir, bottomTubeRectanlges[i])){
				gameState = 2;

			}
		}

		//shapeRenderer.end();
	}


	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
