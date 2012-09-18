package classic;

import java.util.Random;
import java.util.Vector;

import classic.Ball;
import classic.GameObject;
import classic.Paddle;
import classic.SoundManager;
import classic.GraphicsManager;
import classic.Vector2;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
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
	
	public static GraphicsManager graphicsManager; // Graphics Manager instance
													// will handle drawing of
													// game objects
	public static SoundManager soundManager; // Sound Manager instance will
												// handle all sounds
	public static Random randomGenerator; // Generator of random values

	Refresher refresher; // Separate thread class handling Game Object updates
							// and Draws

	private static boolean active; // Game is active flag

	public static Ball ball = new Ball(); // The game ball

	public static Paddle playerPaddle = new Paddle(); // The player paddle
	public static Paddle enemyPaddle = new Paddle(); // The enemy paddle

	Vector gameObjects; // An array of all game objects

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

		// Start rocking the tunes
		// soundManager.playMusic("Content/music.mp3");

		// Initialise the position and velocity of the ball
		ball
				.setPosition(new Vector2(Graphics.getScreenWidth() / 2
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
		gameObjects.addElement(ball);

		gameObjects.addElement(playerPaddle);
		gameObjects.addElement(enemyPaddle);

		// Construct the refresher Thread, this thread updates/draws game
		// objects
		int gameSpeed = 40;
		refresher = new Refresher(gameSpeed);

		// Construct the Enemy AI Threads
		int thinkTimesPerSecond = 12;
		EnemyAI enemyAI = new EnemyAI(enemyPaddle, thinkTimesPerSecond);
		enemyAI.startThinking();

	}

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

				// Detect GameObject Collisions
				detectCollisions(gameObjects);

				// Update Game Objects
				for (int i = 0; i < gameObjects.size(); i++) {
					GameObject object = (GameObject) gameObjects.elementAt(i);
					object.update();
				}

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
		default:
			break;
		}

		return returnValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.rim.device.api.ui.Screen#trackwheelClick(int, int) Overridden
	 * Blackberry method that handles clicking the track ball
	 */
	public boolean trackwheelClick(int status, int time) {

		// If the ball has not been started, this click starts the ball
		if (!ball.is_started()) {
			ball.setVelocity(new Vector2(1).multiply(ball.getCurrentSpeed()));
			ball.set_started(true);
		}
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

	// Methods
	// ---------------------------------------------------------------------------------------------
	
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
					GameObject object1 = (GameObject) gameObjects.elementAt(i);
					GameObject object2 = (GameObject) gameObjects.elementAt(j);
					if (detectCollision(object1, object2)) {
						object1.collisionReaction(object2);
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
}
