
package improved;

import java.util.Random;
import java.util.Vector;


import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.FullScreen;

/**
 * @author Shadowcat Productions
 * @version 1.0 This class extends the BlackBerry FullScreen type. It controls
 *          all updates, draws, sound, input and Enemy AI for the game.
 */
/**
 * @author james
 * 
 */
public class GamePlay extends FullScreen {
	
	public static boolean DEBUG = true;
	
	public static GraphicsManager graphicsManager; // Graphics Manager instance
													// will handle drawing of
													// game objects
	public static SoundManager soundManager; // Sound Manager instance will
												// handle all sounds
	public static Random randomGenerator; // Generator of random values

	public static int gameSpeed = 40;
	Refresher refresher; // Separate thread class handling Game Object updates
							// and Draws

	private static boolean trackBallHeld = false; // The enemy paddle

	// Game play variables for score and stages
	private static int currentStage;
	private static int mustWinBy;
	private static int scoreToWin;
	private static int numberStages;
	
	public static Vector messageQueue;
	
	public static Vector2 gameCenter;

	private static boolean active; // Game is active flag

	public static Ball ball = new Ball(); // The game ball

	public static Paddle playerPaddle = new Paddle("HUMAN"); // The player paddle
	public static Paddle enemyPaddle = new Paddle("AI"); // The enemy paddle
	
	public static GameObject playerUpgrade, playerLeftUpgrade, playerRightUpgrade;
	public static boolean openUpgrades = false, magnet = false;
	
	private double magnetPower = 10;
	private double laserSpeed = 10;
	
	public static int upgradeTimer = 0;
	//public static Upgrade enemyUpgrade = new Upgrade(enemyPaddle.getUpgrade());
	
	public static Vector gameObjects; // An array of all game objects
	public static Vector lineObjects; // An array of all line objects
	
	public static Line serveLine;
	
	Vector upgradeObject;
	/**
	 * GamePlay Constructor This constructor initialises all game content and
	 * game control threads.
	 */
	public GamePlay() {
		soundManager = new SoundManager(); // Construct the Sound Manager
		graphicsManager = new GraphicsManager(); // Construct the Graphics
													// Manager
		randomGenerator = new Random(); // Construct the Random Number Generator

		setActive(true); // Activate the Game
		gameObjects = new Vector();
		lineObjects = new Vector();
		//upgradeObjects = new Vector();
		
		// Start rocking the tunes
		// soundManager.playMusic("Content/music.mp3");
		
		serveLine = new Line();
		serveLine.setActive(true);
		lineObjects.addElement(serveLine);
		
		//Let the player know that he/she/it can serve first
		playerPaddle.setServing(true);

		// Initalize score and stage fields
		setCurrentStage(1);
		setMustWinBy(2);
		setScoreToWin(3);
		setNumberStages(10);

		// Initialise the position and velocity of the ball
		
		gameCenter = new Vector2(Graphics.getScreenWidth() / 2, Graphics.getScreenHeight() / 2);
		
		ball.setPosition(new Vector2(Graphics.getScreenWidth() / 2
				- ball.getBitmap().getWidth() / 2,
				(Graphics.getScreenHeight() / 2 - ball.getBitmap()
						.getHeight() / 2)));

		// Initialise the players paddle position
		playerPaddle.setPosition(new Vector2(2, Graphics.getScreenHeight() / 2
				- playerPaddle.getBitmap().getHeight() / 2));
		playerPaddle.setScore(0);
		// Initialise the enemies paddle position
		enemyPaddle.setPosition(new Vector2(Graphics.getScreenWidth()
				- enemyPaddle.getBitmap().getWidth() - 2, Graphics
				.getScreenHeight()
				/ 2 - enemyPaddle.getBitmap().getHeight() / 2));
		enemyPaddle.setScore(0);
		
		//enemyPaddle.setLastPower(1);
		//enemyPaddle.setCurrentPower(2);
		//enemyPaddle.changePaddle();
		
		//Initialise the players Upgrade position
		//playerUpgrade.setPosition(new Vector2(35, Graphics
				//.getScreenHeight() - 35));
		
		//Game objects
		gameObjects.addElement(ball);
		gameObjects.addElement(playerPaddle);
		gameObjects.addElement(enemyPaddle);
		
		// Construct the refresher Thread, this thread updates/draws game
		// objects
		refresher = new Refresher(GamePlay.gameSpeed);

		// Construct the Enemy AI Threads
		int thinkTimesPerSecond = 15;
		EnemyAI enemyAI = new EnemyAI(enemyPaddle, thinkTimesPerSecond);
		enemyAI.startThinking();
		
		
		//Start Message
		GamePlay.messageQueue = new Vector();

		Message message = new Message("Stage 1", Color.YELLOWGREEN);
		Vector2 messagePosition = new Vector2(Graphics.getScreenWidth() / 2 - 28, 50);
		message.setPosition(messagePosition);
		GamePlay.messageQueue.addElement(message);
		
		//Initialize Powers
		playerPaddle.initPowers();
		enemyPaddle.initPowers();
		
		
		
	}

	// Accessors
	// ---------------------------------------------------------------------------------------------

	/**
	 * public static getActive() Method
	 * 
	 * @return active (boolean) - Returns a boolean flag representing the active
	 *         state of the Game
	 */
	public static boolean getActive() {
		return active;
	}

	/**
	 * public static getTrackBallHeld()
	 * 
	 * @return trackBallHeld (boolean) - Returns a boolean flag representing if
	 *         the Blackberry Trackball is being held down
	 */
	public static boolean getTrackBallHeld() {
		return trackBallHeld;
	}

	/**
	 * private static getCurrentStage()
	 * 
	 * @return currentStage (int) - Returns an int representing the current game
	 *         stage.
	 */
	public static int getCurrentStage() {
		return currentStage;
	}

	/**
	 * private static getMustWinBy()
	 * 
	 * @return mustWinBy (int) - Returns an int representing the number of
	 *         points a paddle must win by, to win a stage
	 */
	private static int getMustWinBy() {
		return mustWinBy;
	}

	/**
	 * private static getScoreToWin()
	 * 
	 * @return scoreToWin (int) - Returns an int representing the total score a
	 *         paddle must attain in a stage to win
	 */
	private static int getScoreToWin() {
		return scoreToWin;
	}

	/**
	 * private static getNumberStages()
	 * 
	 * @return numberStages (int) - Returns an int representing the total number
	 *         of stages in the game
	 */
	private static int getNumberStages() {
		return numberStages;
	}

	// Modifiers
	// ---------------------------------------------------------------------------------------------

	/**
	 * public static setActive()
	 * 
	 * @param newActive
	 *            (boolean) - Sets a boolean flag representing that active state
	 *            of the Game
	 */
	public static void setActive(boolean newActive) {
		active = newActive;
	}

	/**
	 * public static setTrackBallHeld()
	 * 
	 * @param newTrackBallHeld
	 *            (boolean) - Sets a boolean flag representing if the Blackberry
	 *            Trackball is being held down
	 */
	public static void setTrackBallHeld(boolean newTrackBallHeld) {
		trackBallHeld = newTrackBallHeld;
	}

	/**
	 * private static setCurrentStage()
	 * 
	 * @param newCurrentStage
	 *            (int) - Sets an int representing the current game stage.
	 */
	private static void setCurrentStage(int newCurrentStage) {
		currentStage = newCurrentStage;
	}

	/**
	 * private static setMustWinBy()
	 * 
	 * @param newMustWinBy
	 *            (int) - Sets an int representing the number of points a paddle
	 *            must win by, to win a stage
	 */
	private static void setMustWinBy(int newMustWinBy) {
		mustWinBy = newMustWinBy;
	}

	/**
	 * private static setScoreToWin()
	 * 
	 * @param newScoreToWin
	 *            (int) - Sets an int representing the total score a paddle must
	 *            attain in a stage to win
	 */
	private static void setScoreToWin(int newScoreToWin) {
		scoreToWin = newScoreToWin;
	}

	/**
	 * private static setNumberStages()
	 * 
	 * @param newNumberStages
	 *            (int) - Sets an int representing the total number of stages in
	 *            the game
	 */
	private static void setNumberStages(int newNumberStages) {
		numberStages = newNumberStages;
	}

	// Methods
	// ---------------------------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rim.device.api.ui.Screen#paint(net.rim.device.api.ui.Graphics)
	 * This is an overridden BlackBerry method that handles drawing to the
	 * screen. This method is called by invalidate() in the refresher thread.
	 */
	protected void paint(Graphics graphics) {
		graphicsManager.drawBackround(graphics);

		graphicsManager.DrawString(graphics, String.valueOf(playerPaddle
				.getScore()), Graphics.getScreenWidth()
				/ 2
				- GraphicsManager.gameFont.getAdvance(String
						.valueOf(playerPaddle.getScore())) - 5, 5, Color.WHITE);
		
		graphicsManager.DrawString(graphics, String.valueOf(enemyPaddle
				.getScore()), Graphics.getScreenWidth() / 2 + 5, Graphics
				.getScreenHeight()
				- GraphicsManager.gameFont.getHeight() - 5, Color.WHITE);

		// Draw Game Objects
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject object = (GameObject) gameObjects.elementAt(i);
			graphicsManager.Draw(graphics, object);
		}
		
		// Draw Line Objects
		for (int i = 0; i < lineObjects.size(); i++) {
			Line line = (Line) lineObjects.elementAt(i);
			graphicsManager.DrawLine(graphics, line);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rim.device.api.ui.Screen#keyChar(char, int, int) This is an
	 * overidden BlackBerry method that handles input from the BlackBerry
	 * Keyboard
	 */
	public boolean keyChar(char key, int status, int time) {
		boolean returnValue = false;

		// If the player Hits Escape - Deactivate the game
		switch (key) {
		case Characters.ESCAPE:
			setActive(false);
			returnValue = true;
			break;
		case Characters.SPACE:
			changeActivePower();
			break;
		default:
			break;
		}

		return returnValue;
	}
	
	
	/**
	 * private void changeActivePower() 
	 * - A method that cycles the power icons on screen.
	 */
	private void changeActivePower(){
		upgradeTimer = 0;
		if(openUpgrades == false){
			playerUpgrade = new GameObject(playerPaddle.getUpgrade());
			playerUpgrade.setPosition(new Vector2(35, 205));
			playerUpgrade.setCollisionFalse();
			gameObjects.addElement(playerUpgrade);
			
			EncodedImage li = EncodedImage.getEncodedImageResource(playerPaddle.getLeftUpgrade());  
			li.setScale(2); // divide size by this number  
			playerLeftUpgrade = new GameObject(li.getBitmap());
			playerLeftUpgrade.setPosition(new Vector2(15, 220));
			playerLeftUpgrade.setCollisionFalse();
			gameObjects.addElement(playerLeftUpgrade);
			
			EncodedImage ri = EncodedImage.getEncodedImageResource(playerPaddle.getRightUpgrade());  
			ri.setScale(2); // divide size by this number  
			playerRightUpgrade = new GameObject(ri.getBitmap());
			playerRightUpgrade.setPosition(new Vector2(70, 220));
			playerRightUpgrade.setCollisionFalse();
			gameObjects.addElement(playerRightUpgrade);
			
			openUpgrades = true;
		}else{
			playerPaddle.cyclePower();
			if(playerPaddle.getHitsLeft() <= 0){
				do{
					playerPaddle.cyclePower();
				}while(playerPaddle.getHitsLeft() <= 0);
			}
			playerUpgrade.changeBitmap(playerPaddle.getUpgrade());
			
			EncodedImage li = EncodedImage.getEncodedImageResource(playerPaddle.getLeftUpgrade());  
			li.setScale(2); // divide size by this number  
			playerLeftUpgrade.changeBitmap(li.getBitmap());
			
			EncodedImage ri = EncodedImage.getEncodedImageResource(playerPaddle.getRightUpgrade());  
			ri.setScale(2); // divide size by this number  
			playerRightUpgrade.changeBitmap(ri.getBitmap());
		}
	}
	
	/**
	 * private static checkUpgrades() 
	 * - A method that handles when the upgrade Icons should disappear.
	 */
	private void checkUpgrades(){
		if(openUpgrades == true){
			upgradeTimer++;
			if(upgradeTimer == 75){
				gameObjects.removeElement(playerLeftUpgrade);
				gameObjects.removeElement(playerRightUpgrade);
				gameObjects.removeElement(playerUpgrade);
				playerPaddle.changePaddle();
				openUpgrades = false;
			}
		}
	}
	
	/**
	 * public userPowers()
	 *  - Method that determines what power is selected and uses that power
	 */
	private void usePower(){
		if(playerPaddle.getCurrentPower() == 2){			
			
			Vector2 laserPosition = playerPaddle.getCenterPosition();
			laserPosition.setX(laserPosition.getX() + (playerPaddle.getBitmap().getWidth() / 2) + 1);
			Laser laser = new Laser(laserPosition, Vector2.UnitX().multiply(laserSpeed));
			
			gameObjects.addElement(laser);
			
			int index = gameObjects.indexOf(laser);
			System.out.println("GamePlay Index: " + index);
			
		}else if(playerPaddle.getCurrentPower() == 3){
			if(ball.getVelocity().getX() < 0){
				magnet = true;
			}
		}
	}
	
	/**
	 * private magnetUse()
	 *  - Use the magnet power, called every frame that the magnet power is being used.  Applys force to the
	 *  ball to bring it right to the paddle.
	 */
	private void magnetUse(){
		if(magnet == true){
			
			GamePlay.ball.setAppliedForce(Vector2.Zero());
			GamePlay.ball.setSpin(0);
			
			Vector2 newVelocity = playerPaddle.getCenterPosition().subtract(ball.getCenterPosition()).normalise();
			newVelocity = newVelocity.multiply(10.0);
			GamePlay.ball.setVelocity(newVelocity);
		}
	}
	
	/**
	 * public setMagnetFalse()
	 *  - Turns off the magnet power
	 */
	public static void setMagnetFalse(){
		magnet = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rim.device.api.ui.Screen#keyStatus(int, int) This is an
	 * overidden BlackBerry method that handles input from the BlackBerry
	 * Keyboard
	 */
	/*public boolean keyStatus(int keycode, int time){
		boolean returnValue = false;
		
		if(keycode == 16777282 || keycode == 16777216 || keycode == 16908322 || keycode == 16908288){
			playerPaddle.changeBitmap(playerPaddle.getPaddle());
			returnValue = true;
		}
		
		return returnValue;
	}*/
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rim.device.api.ui.Screen#trackwheelClick(int, int) Overridden
	 * Blackberry method that handles clicking the track ball
	 */
	public boolean trackwheelClick(int status, int time) {

		// If the ball has not been started, this click starts the ball
		if (playerPaddle.isServing()) {
			playerPaddle.serve();
		}else if (ball.is_started()){
			usePower();
		}

		// Set a flag so we know when we are "holding" the track wheel down
		GamePlay.setTrackBallHeld(true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rim.device.api.ui.Screen#trackwheelUnclick(int, int) Overriden
	 * Blackberry method that handles unclicking the track ball wheel
	 */
	public boolean trackwheelUnclick(int status, int time) {

		// Set a flag so we know that we are no longer "holding" the track wheel
		// down
		GamePlay.setTrackBallHeld(false);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rim.device.api.ui.Screen#navigationMovement(int, int, int, int)
	 * Overriden Blackberry method that handles moving the track wheel.
	 * Currently controls the position of our players paddle
	 */
	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		if (dy != 0) {
			Vector2 newPosition = new Vector2();
			newPosition.setX(playerPaddle.getPosition().getX());
			newPosition.setY(dy / Math.abs(dy) * 5
					+ playerPaddle.getPosition().getY());
			playerPaddle.setPosition(newPosition);
		}
		return true;
	}
	
	/**
	 * private static stageComplete() - A method that handles when the current
	 * stage is complete.
	 */
	private static void stageComplete() {
		int completeStage = getCurrentStage();
		int nextStage = completeStage + 1;
		if (nextStage > getNumberStages()) {
			GamePlay.gameOver(true);
		}
		setCurrentStage(nextStage);
		
		Message message = new Message("Stage " + nextStage, Color.YELLOWGREEN);
		Vector2 messagePosition = new Vector2(Graphics.getScreenWidth() / 2 - 28, 50);
		message.setPosition(messagePosition);
		GamePlay.messageQueue.addElement(message);
		
		//Reset scores
		playerPaddle.setScore(0);
		enemyPaddle.setScore(0);
		playerPaddle.initPowers();
		enemyPaddle.initPowers();
	}

	/**
	 * public static checkStageProgression() - A method that handles checking
	 * the current stage progression. ie. if a player has won yet.
	 */
	public static void checkStageProgression() {

		// Check if the enemy has won, if so, game over
		if (enemyPaddle.getScore() >= getScoreToWin()
				&& enemyPaddle.getScore() >= (playerPaddle.getScore() + getMustWinBy())) {
			GamePlay.gameOver(false);
		}

		// Check if the player has won the stage
		if (playerPaddle.getScore() >= getScoreToWin()
				&& playerPaddle.getScore() >= (enemyPaddle.getScore() + getMustWinBy())) {
			// Player wins stage
			stageComplete();
		}
	}

	/**
	 * private static gameOver()
	 * 
	 * @param didPlayerWin
	 *            (boolean) - A flag representing if the player won the game, or
	 *            not. - A method that handles when the game is over, regardless
	 *            of who won.
	 */
	private static void gameOver(boolean didPlayerWin) {
		
		GamePlay.setActive(false);
	}

	/**
	 * public static detectCollisions()
	 * 
	 * @param gameObjects
	 *            (Vector) - An array of GameObject instances. - A method that
	 *            will detect if game objects have collided with eachother.
	 */
	public static void detectCollisions(Vector gameObjects) {
		for (int i = 0; i < gameObjects.size(); i++) {
			for (int j = 0; j < gameObjects.size(); j++) {
				if (i != j) {
					try{
						GameObject object1 = (GameObject) gameObjects.elementAt(i);
						GameObject object2 = (GameObject) gameObjects.elementAt(j);
						
						if (detectCollision(object1, object2) && object1.getCollisionState() && object2.getCollisionState()) {
							object1.collisionReaction(object2);
						}
					}catch(Exception e){
						//Here we fail silently, this means that an object was removed from from 
						//gameObjects during this loop.
					}
				}
			}
		}
	}

	/**
	 * public static detectCollision()
	 * 
	 * @param object1
	 *            (GameObject) - An object in the game.
	 * @param object2
	 *            (GameObject) - A second object in the game.
	 * @return objectsCollided (boolean) - A boolean flag representing if two
	 *         objects collided. - A method that accepts two game objects and
	 *         determines if they collided with eachother.
	 */
	public static boolean detectCollision(GameObject object1, GameObject object2) {
		double object1X1, object1X2, object1Y1, object1Y2;
		double object2X1, object2X2, object2Y1, object2Y2;

		object1X1 = object1.getPosition().getX();
		object1X2 = object1.getPosition().getX()
				+ object1.getBitmap().getWidth();
		object1Y1 = object1.getPosition().getY();
		object1Y2 = object1.getPosition().getY()
				+ object1.getBitmap().getHeight();

		object2X1 = object2.getPosition().getX();
		object2X2 = object2.getPosition().getX()
				+ object2.getBitmap().getWidth();
		object2Y1 = object2.getPosition().getY();
		object2Y2 = object2.getPosition().getY()
				+ object2.getBitmap().getHeight();

		if (object2X1 < object1X2 && object2X2 > object1X1
				&& object2Y1 < object1Y2 && object2Y2 > object1Y1) {
			return true;
		}

		return false;
	}
	
	public static final Vector split(final String data, final char splitChar){
        Vector v = new Vector();

        String working = data;
        int index = working.indexOf(splitChar);

        while (index != -1)
        {
            String tmp = "";
            if (index > 0)
            {
                tmp = working.substring(0, index);
            }
            v.addElement(tmp);

            working = working.substring(index + 1);

            // Find the next index
            index = working.indexOf(splitChar);
        }

        // Add the rest of the working string
        v.addElement(working);

        return v;
    }
	
	// Internal Classes
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * @author Shadowcat Productions
	 * @version 1.0 This class extends Thread. It is a separate thread in the
	 *          game that handles updates and drawing of game objects.
	 */
	private class Refresher extends Thread {

		int refreshesPerSecond;

		// Once constructed, run the thread
		Refresher(int newRefreshesPerSecond) {
			refreshesPerSecond = newRefreshesPerSecond;
			start();
		}

		public void run() {

			// only update and draw if the game is active
			while (getActive()) {
				
				// Check to see if upgrade Icons should still be displaying
				checkUpgrades();
				
				magnetUse();
				
				// Detect GameObject Collisions
				detectCollisions(gameObjects);

				// Update Game Objects
				for (int i = 0; i < gameObjects.size(); i++) {
					GameObject object = (GameObject) gameObjects.elementAt(i);
					object.update();
				}
				
				checkAnimations(playerPaddle);
				checkAnimations(enemyPaddle);				

				// Call the BlackBerry update screen method
				invalidate();

				// Sleep this thread, controls game speed
				try {
					Refresher.sleep(1000 / refreshesPerSecond);

				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	public void checkAnimations(Paddle paddle){
		if(paddle.extendAnimationDirection == -1){
			//Check if we need to stop retracting extend animation
			paddle.checkAnimateFromExtend();
		}
		else if(paddle.extendAnimationDirection == 1){
			//Check if we need to stop extending extend animation
			paddle.checkAnimateToExtend();
		}						
		else if(paddle.magnetAnimationDirection == -1){
			//Check if we need to stop retracting magnet animation
			paddle.checkAnimateFromMagnet();
		}
		else if(paddle.magnetAnimationDirection == 1){
			//Check if we need to stop extending magnet animation
			paddle.checkAnimateToMagnet();
		}						
		else if(paddle.laserAnimationDirection == -1){
			//Check if we need to stop retracting last animation
			paddle.checkAnimateFromLaser();
		}
		else if(paddle.laserAnimationDirection == 1){
			//Check if we need to stop extending laser animation
			paddle.checkAnimateToLaser();
		}
	}

	/**
	 * @author Shadowcat Productions
	 * @version 1.0 This class extends thread. It is a separate thread in the
	 *          game that handles enemy ai.
	 */
	private class EnemyAI extends Thread {

		GameObject gameObject;
		int thinkTimesPerSecond;

		// Once constructed, run the thread
		EnemyAI(GameObject newGameObject, int newThinkTimesPerSecond) {
			gameObject = newGameObject;
			thinkTimesPerSecond = newThinkTimesPerSecond;
		}

		public void startThinking() {
			start();
		}

		public void run() {

			// Only allow objects to think if the game is active
			while (getActive()) {

				// Sleep the thread, allows us to control how fast enemies think
				try {
					EnemyAI.sleep(1000 / thinkTimesPerSecond);
				} catch (InterruptedException e) {
				}

				// Tell Game objects to think
				gameObject.think();

			}
		}
	}
}
