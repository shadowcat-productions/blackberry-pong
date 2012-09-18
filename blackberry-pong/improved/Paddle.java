package improved;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;

/**
 * @author Pennell
 * 
 */
public class Paddle extends GameObject {

	// Constants
	private int bonusCap = 10; // change this appropriately
	private String controller; // "HUMAN" or "AI"
	private double timeToServe = 40.0;
	private int velocityRefresh = 10;

	// Variables
	private String AI_Type = "";
	private int score = 0; // Will be 0 if super downgraded

	private int lastPower = 3;
	private int currentPower = 1;
	private boolean upgrade = false;
	private double currentVelocity = 0.0;
	private boolean serving = false;
	private boolean isAnimating = false;
	private double lastHeight = 0;
	private int velocityRefreshTimer = 0; // Will be used as a timer to compare
	// paddle locations
	private String[] upgradeArray = new String[10];
	private String[] paddleArray = new String[10];

	private GameObject power1 = null;
	private GameObject power2 = null;

	private int[] hitsLeft = new int[10];

	private int extendPaddleHitsLeft = 3;
	private int laserShotsLeft = 3;
	private int magnetPullsLeft = 3;

	private int bonus = 0;

	public int laserAnimationDirection = 0;
	public int magnetAnimationDirection = 0;
	public int extendAnimationDirection = 0;

	/**
	 * Constructor for the Paddle. This constructor will create a Paddle Game
	 * Object with the default paddle bitmap content.
	 */
	public Paddle() {
		super(Bitmap.getBitmapResource(StaticGraphics.paddleGraphic));
		this.setName("paddle");
	}

	/**
	 * Constructor for the Paddle. This constructor will create a Paddle Game
	 * Object with the default paddle bitmap content.
	 */
	public Paddle(String controlType) {
		super(Bitmap.getBitmapResource(StaticGraphics.paddleGraphic));

		if (controlType == "HUMAN")
			controller = "HUMAN";
		else if (controlType == "AI")
			controller = "AI";

		this.setName("paddle");
	}

	/**
	 * @param newPosition
	 *            Constructor for the Paddle. This constructor will create a
	 *            Paddle Game Object with the default paddle bitmap content and
	 *            an initial position.
	 */
	public Paddle(Vector2 newPosition) {
		super(Bitmap.getBitmapResource(StaticGraphics.paddleGraphic),
				newPosition);
		this.setName("paddle");
	}

	/**
	 * @param newPosition
	 * @param newVelocity
	 *            Constructor for the Paddle. This constructor will create a
	 *            Paddle Game Object with the default paddle bitmap content, an
	 *            initial position and an initial velocity.
	 */
	public Paddle(Vector2 newPosition, Vector2 newVelocity) {
		super(Bitmap.getBitmapResource(StaticGraphics.paddleGraphic),
				newPosition, newVelocity);
		this.setName("paddle");
	}

	/**
	 * @param newPosition
	 * @param newVelocity
	 * @param newParent
	 *            Constructor for the Paddle. This constructor will create a
	 *            Paddle Game Object with the default paddle bitmap content, an
	 *            initial position, an initial velocity and a parent Game Object
	 */
	public Paddle(Vector2 newPosition, Vector2 newVelocity, GameObject newParent) {
		super(Bitmap.getBitmapResource(StaticGraphics.paddleGraphic),
				newPosition, newVelocity, newParent);
		this.setName("paddle");
	}

	// Accessors
	// ---------------------------------------------------------------------------------------------

	/**
	 * public getScore()
	 * 
	 * @return score (int) - Returns the current score of the paddle.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * public getCurrentPower()
	 * 
	 * @return currentPower (int) - Returns an int representing the current
	 *         power
	 */
	public int getCurrentPower() {
		return currentPower;
	}

	public int getLastPower() {
		return lastPower;
	}

	/**
	 * public hasUpgrade()
	 * 
	 * @return upgrade (boolean) - Returns a boolean flag representing if the
	 *         paddle has an upgrade.
	 */
	public boolean hasUpgrade() {
		return upgrade;
	}

	/**
	 * public getCurrentVelocity()
	 * 
	 * @return currentVelocity (double) - Returns the current velocity of the
	 *         ball
	 */
	public double getCurrentVelocity() {
		return currentVelocity;
	}

	/**
	 * public getArraySize() - Method that returns the size of the
	 * upgradeArray[]
	 */
	public int getArraySize() {
		return this.upgradeArray.length;
	}

	/**
	 * public isServing()
	 * 
	 * @return boolean - A boolean flag representing if the paddle is currently
	 *         serving.
	 */
	public boolean isServing() {
		return serving;
	}

	// Modifiers
	// ---------------------------------------------------------------------------------------------

	/**
	 * public setScore()
	 * 
	 * @param newScore
	 *            (int) - Sets the current score of the paddle
	 */
	public void setScore(int newScore) {
		score = newScore;
	}

	/**
	 * public setCurrentPower()
	 * 
	 * @param newPower
	 *            (int) - Sets the current power of the paddle.
	 */
	public void setCurrentPower(int newPower) {
		currentPower = newPower;
	}

	public void setLastPower(int newPower) {
		lastPower = newPower;
	}

	/**
	 * public setUpgrade()
	 * 
	 * @param newUpgrade
	 *            (boolean) - Sets a boolean flag representing if the paddle has
	 *            an upgrade.
	 */
	public void setUpgrade(boolean newUpgrade) {
		upgrade = newUpgrade;
	}

	/**
	 * public setCurrentVelocity()
	 * 
	 * @param newCurrentVelocity
	 *            (double) - Sets the current velocity field of the paddle
	 */
	public void setCurrentVelocity(double newCurrentVelocity) {
		currentVelocity = newCurrentVelocity;
	}

	/**
	 * public setHitsLeft() - Method that decrements the number of uses a user
	 * has for a given power
	 */
	public void setHitsLeft() {
		hitsLeft[getCurrentPower() - 1] = hitsLeft[getCurrentPower() - 1] - 1;

		if (hitsLeft[getCurrentPower() - 1] < 0) {
			hitsLeft[getCurrentPower() - 1] = 0;
		} else if (hitsLeft[getCurrentPower() - 1] == 0) {
			changeBitmap(paddleArray[0]);
		}

	}

	/**
	 * public populateUpgrades() - Method called upon at system start to
	 * populate the upgrades array with a string corresponding to its image
	 * file.
	 */
	public void populateUpgrades() {
		this.upgradeArray[0] = StaticGraphics.extend_select_graphic;
		this.upgradeArray[1] = StaticGraphics.laser_select_graphic;
		this.upgradeArray[2] = StaticGraphics.magnet_select_graphic;
	}

	/**
	 * public populatePaddles() - Method called upon at system start to populate
	 * the paddles array with a string corresponding to its image file.
	 */
	public void populatePaddles() {
		this.paddleArray[0] = "Content/paddle.png";
		this.paddleArray[1] = "Content/x-paddle-base.png,Content/x-paddle-top.png,Content/x-paddle-bottom.png";
		this.paddleArray[2] = "Content/laser-paddle1.png,Content/laser-paddle2.png";
		this.paddleArray[3] = "Content/magnet-paddle.png";
	}

	/**
	 * public populateHitsLeft() - Method that populates the array that keeps
	 * track of the number of uses remaining for each power.
	 */
	public void populateHitsLeft() {
		this.hitsLeft[0] = extendPaddleHitsLeft;
		this.hitsLeft[1] = laserShotsLeft;
		this.hitsLeft[2] = magnetPullsLeft;
	}

	/**
	 * public setServing()
	 * 
	 * @param newServing
	 *            - Boolean flag representing if the paddle is serving right
	 *            about now.
	 */
	/**
	 * @param newServing
	 */
	public void setServing(boolean newServing) {
		serving = newServing;
		GamePlay.serveLine.setActive(newServing);
	}

	// Methods
	// ---------------------------------------------------------------------------------------------

	/**
	 * public increaseScore() - Method that increases the paddles score by 1
	 */
	public void increaseScore() {
		score = score + 1;
	}

	/**
	 * public addUpgrade() - Method to call to remove a temporary upgrade from
	 * the array after its use
	 */
	public void removeUpgrade(int index) {
		if (this.upgradeArray[index] != null) {
			for (int i = index - 1; i < upgradeArray.length - 2; i++) {
				if (this.upgradeArray[i + 1] != null) {
					this.upgradeArray[i] = this.upgradeArray[i + 1];
				}
			}
		} else {
			this.upgradeArray[index - 1] = null;
		}
	}

	/**
	 * public getUpgrade() - returns the file path location of the current
	 * upgrade image
	 */
	public String getUpgrade() {
		return this.upgradeArray[this.getCurrentPower() - 1];
	}

	/**
	 * public getUpgrade() - returns the file path location of the current
	 * upgrade image
	 */
	public String getLeftUpgrade() {
		if (this.getCurrentPower() == 1) {
			System.out.println("power = " + (this.getCurrentPower() - 1)
					+ "left = " + 2);
			return this.upgradeArray[2];

		} else {
			System.out.println("left = " + (this.getCurrentPower() - 2));
			return this.upgradeArray[this.getCurrentPower() - 2];
		}
	}

	/**
	 * public getUpgrade() - returns the file path location of the current
	 * upgrade image
	 */
	public String getRightUpgrade() {
		if (this.getCurrentPower() == 3) {
			System.out.println("power = " + (this.getCurrentPower() - 1)
					+ "right = " + 0);
			return this.upgradeArray[0];
		} else {
			System.out.println("right = " + (this.getCurrentPower()));
			return this.upgradeArray[this.getCurrentPower()];
		}

	}

	/**
	 * public getUpgrade() - returns the number of hits remaining for the
	 * current paddle
	 */
	public int getHitsLeft() {
		return hitsLeft[this.getCurrentPower() - 1];
	}

	/**
	 * public cycleUpgrade() - Method that will cycle the paddles current
	 * upgrade
	 */
	public void cyclePower() {

		if (this.getCurrentPower() != 0) {
			int newPower = this.getCurrentPower() + 1;
			if (newPower > 3) {
				this.setLastPower(3);
				this.setCurrentPower(1);
			} else {
				this.setLastPower(this.currentPower);
				this.setCurrentPower(newPower);
			}
		}
	}

	public void finishedFromAnimation() {
		GamePlay.gameObjects.removeElement(this.power1);
		GamePlay.gameObjects.removeElement(this.power2);

		this.power1 = null;
		this.power2 = null;

		this.changeToPaddle();
	}

	/**
	 * Specific Animation Functions
	 */

	public void animateToLaser() {

		this.laserAnimationDirection = 1;
		this.magnetAnimationDirection = 0;
		this.extendAnimationDirection = 0;

		// Create the laser graphic objects

		this.power1 = new GameObject(StaticGraphics.laser_paddle1);
		this.power2 = new GameObject(StaticGraphics.laser_paddle2);

		this.power1.setParent(this);
		this.power2.setParent(this);

		// These graphics line up with the center the current paddle paddle
		Vector2 newPosition = this.getCenterPosition();
		this.power1.setCenterPosition(newPosition);
		this.power2.setCenterPosition(newPosition);

		// The ball should not collide with the laser attachments
		this.power1.setCollisionFalse();
		this.power2.setCollisionFalse();

		// Start the animation process for the base laser image.
		if (this.controller == "HUMAN") {
			this.power1.startAnimating(new Vector2(1, 0));
		} else {
			this.power1.startAnimating(new Vector2(-1, 0));
		}

		// Add laser graphics to the gameobjects vector so they get drawn
		// properly
		GamePlay.gameObjects.addElement(this.power1);
		GamePlay.gameObjects.addElement(this.power2);

	}

	public void checkAnimateToLaser() {

		System.out
				.println("Checking animation to laser for " + this.controller);

		if (this.controller == "HUMAN") {
			if (this.power1.getAnimationState()
					&& this.power1.getPosition().getX() > (this.getPosition()
							.getX() + this.getBitmap().getWidth())) {
				// Power 1 Animation Finished
				this.power1.stopAnimating();
				this.power2.startAnimating(new Vector2(1, 0));
			}

			if (this.power2.getAnimationState()
					&& this.power2.getPosition().getX() > (this.power1
							.getPosition().getX() + this.power1.getBitmap()
							.getWidth())) {
				this.power2.stopAnimating();
			}

		} else {

			this.power1.getAnimationState();
			this.power1.getPosition().getX();

			if (this.power1.getAnimationState()
					&& (this.power1.getPosition().getX() + this.power1
							.getBitmap().getWidth()) < (this.getPosition()
							.getX())) {
				// Power 1 Animation Finished
				this.power1.stopAnimating();
				this.power2.startAnimating(new Vector2(-1, 0));
			}

			if (this.power2.getAnimationState()
					&& (this.power2.getPosition().getX() + this.power2
							.getBitmap().getWidth()) < (this.power1
							.getPosition().getX())) {
				this.power2.stopAnimating();
			}

		}
	}

	public void animateFromLaser() {
		this.laserAnimationDirection = -1;
		this.magnetAnimationDirection = 0;
		this.extendAnimationDirection = 0;

		if (this.controller == "HUMAN") {
			this.power2.startAnimating(new Vector2(-1, 0));
		} else {
			this.power2.startAnimating(new Vector2(1, 0));
		}
	}

	public void checkAnimateFromLaser() {
		boolean done = false;

		if (this.controller == "HUMAN") {
			if (this.power2.getAnimationState()
					&& (this.power2.getPosition().getX() + this.power2
							.getBitmap().getWidth()) < (this.power1
							.getPosition().getX() + this.power1.getBitmap()
							.getWidth())) {
				this.power1.startAnimating(new Vector2(-1, 0));
			}

			if (this.power1.getAnimationState()
					&& (this.power1.getPosition().getX() + this.power1
							.getBitmap().getWidth()) < (this.getPosition()
							.getX() + this.getBitmap().getWidth())) {
				done = true;
			}
		} else {
			if (this.power2.getAnimationState()
					&& (this.power2.getPosition().getX()) > (this.power1
							.getPosition().getX())) {
				this.power1.startAnimating(new Vector2(-1, 0));
			}

			if (this.power1.getAnimationState()
					&& (this.power1.getPosition().getX()) > (this.getPosition()
							.getX())) {
				done = true;
			}
		}

		if (done)
			this.finishedFromAnimation();
	}

	public void animateToMagnet() {
		this.laserAnimationDirection = 0;
		this.magnetAnimationDirection = 1;
		this.extendAnimationDirection = 0;

		this.setBitmap(StaticGraphics.magnet_paddle);

	}

	public void checkAnimateToMagnet() {
		// We need to do nothing right now, maybe something in the future will
		// go here.
	}

	public void animateFromMagnet() {
		this.laserAnimationDirection = 0;
		this.magnetAnimationDirection = -1;
		this.extendAnimationDirection = 0;

		this.setBitmap(StaticGraphics.paddleGraphic);

	}

	public void checkAnimateFromMagnet() {
		boolean done = false;

		// We need to do nothing right now, maybe something in the future will
		// go here.
		done = true;

		if (done)
			this.finishedFromAnimation();
	}

	public void animateToExtend() {
		this.laserAnimationDirection = 0;
		this.magnetAnimationDirection = 0;
		this.extendAnimationDirection = 1;
		// TODO: this must change base paddle graphic - extend

		this.setBitmap(StaticGraphics.x_paddle_base);

		this.power1 = new GameObject(StaticGraphics.x_paddle_top);
		this.power2 = new GameObject(StaticGraphics.x_paddle_bottom);

		this.power1.setParent(this);
		this.power2.setParent(this);

		// These graphics line up with the center the current paddle paddle
		Vector2 newPosition = this.getPosition();
		this.power1.setPosition(newPosition);

		newPosition.setY(newPosition.getY()
				+ (this.getBitmap().getHeight() - this.power2.getBitmap()
						.getHeight()));
		this.power2.setPosition(newPosition);

		// Start the animation process for the base laser image.
		this.power1.startAnimating(new Vector2(0, -1));
		this.power2.startAnimating(new Vector2(0, 1));

		// Add laser graphics to the gameobjects vector so they get drawn
		// properly
		GamePlay.gameObjects.addElement(this.power1);
		GamePlay.gameObjects.addElement(this.power2);

	}

	public void checkAnimateToExtend() {

		if (this.power1.getPosition().getY() < (this.getPosition().getY() - this.power1
				.getBitmap().getHeight())) {
			this.power1.stopAnimating();
		}

		if (this.power2.getPosition().getY() > (this.getPosition().getY() + this.getBitmap().getHeight())) {
			this.power2.stopAnimating();
		}
	}

	public void animateFromExtend() {
		this.laserAnimationDirection = 0;
		this.magnetAnimationDirection = 0;
		this.extendAnimationDirection = -1;
	}

	public void checkAnimateFromExtend() {
		boolean done = true;

		// TODO: Make this work

		done = true;

		if (done) {
			this.changeBitmap(StaticGraphics.paddleGraphic);
			this.finishedFromAnimation();
		}
	}

	public void changeToPaddle() {
		if (this.getCurrentPower() == 1) {
			// Animating to Extend-a-Paddle
			this.animateToExtend();
		} else if (this.getCurrentPower() == 2) {
			// Animating to Laser
			this.animateToLaser();
		} else if (this.getCurrentPower() == 3) {
			// Animating to Magnet
			this.animateToMagnet();
		}
	}

	/**
	 * public cyclePaddle() - Creates a paddle change animation for the given
	 * paddle selected
	 */
	public void changePaddle() {

		if (this.getLastPower() == 1) {
			// Animating from Extend-a-Paddle
			this.animateFromExtend();
		} else if (this.getLastPower() == 2) {
			// Animating from Laser
			this.animateFromLaser();
		} else if (this.getLastPower() == 3) {
			// Animating from Magnet
			this.animateFromMagnet();
		} else {
			this.finishedFromAnimation();
		}
	}

	/**
	 * public initPowers() - Method that initialize all game power variable
	 */
	public void initPowers() {
		System.out.println("--------------------- init powers");
		this.populatePaddles();
		System.out.println("poipulated paddles");
		this.populateUpgrades();
		this.populateHitsLeft();
	}

	public void updatePowerPosition() {

		Vector2 newPosition = new Vector2(this.power1.getCenterPosition()
				.getX(), this.getCenterPosition().getY());

		power1.setCenterPosition(newPosition);

		Vector2 newPosition2 = new Vector2(this.power2.getCenterPosition()
				.getX(), this.getCenterPosition().getY());

		power2.setCenterPosition(newPosition2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see library2D.GameObject#update() public update() - Method that updates
	 * the paddle every frame.
	 */
	public void update() {
		super.setAppliedForce(new Vector2(0, 0));
		super.update();

		// Refresh current velocity every velocityRefresh updates
		velocityRefreshTimer += 1;
		if (velocityRefreshTimer == velocityRefresh) {
			velocityRefreshTimer = 0;
			this.setCurrentVelocity(this.getPosition().getY() - lastHeight);
			lastHeight = this.getPosition().getY();
			super.setSpinVelocity(new Vector2(0, this.getCurrentVelocity()));

		}
		if (this.getPosition().getY() > (Graphics.getScreenHeight() - this
				.getBitmap().getHeight())) {
			this.setPosition(new Vector2(this.getPosition().getX(), Graphics
					.getScreenHeight()
					- this.getBitmap().getHeight()));
			this.setVelocity(Vector2.Zero());
		} else if (this.getPosition().getY() < 1) {
			this.setPosition(new Vector2(this.getPosition().getX(), 0));
			this.setVelocity(Vector2.Zero());
		}

		if (power1 != null) {
			this.updatePowerPosition();
		}

		if (this.isServing()) {
			GamePlay.serveLine.setStartPosition(this.getCenterPosition());

			double x1 = this.getCenterPosition().getX();
			double y1 = this.getCenterPosition().getY();
			double x2 = GamePlay.ball.getCenterPosition().getX();
			double y2 = GamePlay.ball.getCenterPosition().getY();
			double x3 = x2 + (x2 - x1);
			double y3 = y2 + (y2 - y1);

			GamePlay.serveLine.setEndPosition(new Vector2(x3, y3));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see library2D.GameObject#think() public think() - Method that describes
	 * how the enemy paddle thinks.
	 */
	public void think() {

		if (GamePlay.getCurrentStage() == 1 || GamePlay.getCurrentStage() == 2
				|| GamePlay.getCurrentStage() == 6)
			AI_Type = "basic";
		else if (GamePlay.getCurrentStage() == 3
				|| GamePlay.getCurrentStage() == 7)
			AI_Type = "smart";
		else if (GamePlay.getCurrentStage() == 4
				|| GamePlay.getCurrentStage() == 8)
			AI_Type = "aggressive";
		else if (GamePlay.getCurrentStage() == 5
				|| GamePlay.getCurrentStage() == 9)
			AI_Type = "sniper";
		else if (GamePlay.getCurrentStage() == 10)
			AI_Type = "gump";

		if (this.isServing()) {
			if (this.timeToServe < 0) {
				this.serve();
			} else {
				this.timeToServe = this.timeToServe - 1.0;
			}
		} else {
			// Basic is the current paddle AI configuration that scales it's
			// difficulty based upon
			// level, which increases the enemy paddle speed (Levels 1,2,6)
			if (AI_Type == "basic") {
				if (GamePlay.ball.getVelocity().getX() > 0) {
					double top = this.getPosition().getY();
					double bottom = this.getPosition().getY()
							+ this.getBitmap().getHeight();

					if (GamePlay.ball.getPosition().getY() < top
							+ (this.getBitmap().getHeight() * 0.1)) {
						this.setVelocity(Vector2.UnitY().multiply(-1));
					} else if (GamePlay.ball.getPosition().getY() > bottom
							- (this.getBitmap().getHeight() * 0.1)) {
						this.setVelocity(Vector2.UnitY());
					} else {
						this.setVelocity(Vector2.Zero());
					}
					if (GamePlay.getCurrentStage() == 1)
						this.getVelocity().multiply(4);
					if (GamePlay.getCurrentStage() == 2)
						this.getVelocity().multiply(5);
					if (GamePlay.getCurrentStage() == 6)
						this.getVelocity().multiply(6);
				} else {
					this.setVelocity(Vector2.Zero());
				}
			}

			// Smart AI attempts to predict the final ball position and uses
			// powers intelligently.
			// Moves slower than other AI - makes up for it by predicting the
			// ball position and using
			// smart powers/upgrades
			else if (AI_Type == "smart") {
				double ballSlope = (GamePlay.ball.getVelocity().getY() / GamePlay.ball
						.getVelocity().getX());
				double futureBallLocationY = ballSlope
						* (this.getCenterPosition().getX() - GamePlay.ball
								.getPosition().getX())
						+ GamePlay.ball.getPosition().getY();

				System.out.println("ballx = "
						+ GamePlay.ball.getVelocity().getX());

				if (GamePlay.ball.getVelocity().getX() > 0) {
					if (this.getCenterPosition().getY() > futureBallLocationY) {
						this.setVelocity(Vector2.UnitY().multiply(-1));
					} else if (this.getCenterPosition().getY() < futureBallLocationY) {
						this.setVelocity(Vector2.UnitY());
					} else {
						this.setVelocity(Vector2.Zero());
					}

					if (GamePlay.getCurrentStage() == 3
							|| GamePlay.getCurrentStage() == 1)
						this.getVelocity().multiply(4);
					if (GamePlay.getCurrentStage() == 7)
						this.getVelocity().multiply(5);

				}

			}
			// Fast, lots of power usage, only attempts to return the ball when
			// it is past x% of the
			// board. Uses as many powers as possible otherwise.
			else if (AI_Type == "aggressive") {
				if (GamePlay.ball.getVelocity().getX() > 0
						&& GamePlay.ball.getPosition().getX() > Graphics
								.getScreenWidth() / 2) {
					if (this.getCenterPosition().getY() > GamePlay.ball
							.getPosition().getY()) {
						this.setVelocity(Vector2.UnitY().multiply(-1));
					} else if (this.getCenterPosition().getY() < GamePlay.ball
							.getPosition().getY()) {
						this.setVelocity(Vector2.UnitY());
					} else {
						this.setVelocity(Vector2.Zero());
					}

					if (GamePlay.getCurrentStage() == 4
							|| GamePlay.getCurrentStage() == 1)
						this.getVelocity().multiply(6);
					if (GamePlay.getCurrentStage() == 8)
						this.getVelocity().multiply(7);

				}
			}

			// Attempts to get bonus points when returning, uses lasers
			// otherwise.
			else if (AI_Type == "sniper") {

			}

			// Gump sat alone on a bench in the park
			// My name is Forrest he casually remarked
			// Waiting for the bus with his hands in his pockets
			// he just kept saying "life is like a box of chocolates"
			else if (AI_Type == "gump") {

			}

		}
	}

	// Use upgrade
	public boolean useUpgrade() {
		if (bonus >= bonusCap) {
			// Insert bonus get code here
			bonus = 0;
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see library2D.GameObject#collisionReaction(library2D.GameObject) public
	 * collisionReaction(GameObject collisionObject) - Method that describes
	 * what the paddle does when it collides with another object.
	 */
	public void collisionReaction(GameObject collisionObject) {

		if (collisionObject.getName() == "ball") {

			System.out.println("Collision Reaction: Paddle -> Ball");

			String bonusString = "";

			double ballDistance = (this.getPosition().getY() + this.getBitmap()
					.getHeight() / 2)
					- (collisionObject.getPosition().getY() + collisionObject
							.getBitmap().getHeight() / 2);

			if (ballDistance < 0)
				ballDistance *= -1;

			boolean gotSpeedBonus = false;

			// Detect super bounce
			if (ballDistance >= (this.getBitmap().getHeight() / 2) - 2) {
				bonus++;
				bonusString = "SUPER BOUNCE!  " + bonus;
				gotSpeedBonus = true;
			}

			// Detect super accuracy
			if (ballDistance <= 5) {
				bonus++;
				bonusString = "SUPER ACCURATE!  " + bonus;
				gotSpeedBonus = true;
			}

			// Detect super spin
			if (super.getSpinVelocity().getY() > 30
					|| super.getSpinVelocity().getY() < -30) {
				bonus++;
				bonusString = "SUPER SPIN!  " + bonus;
			}
			GamePlay.setMagnetFalse();

			// If a bonus occurred and the paddle is controlled by a human,
			// create and add message to the message queue

			if (bonusString != "" && controller == "HUMAN") {
				Message m = new Message(bonusString, Color.YELLOWGREEN);
				Vector2 messagePosition = new Vector2(
						this.getPosition().getX() + 10, this.getPosition()
								.getY()
								+ this.getBitmap().getHeight() / 2);
				m.setPosition(messagePosition);
				GamePlay.messageQueue.addElement(m);
			}
		} else if (collisionObject.getName() == "laser") {
			System.out.println("Collision Reaction: Paddle -> Laser");
		}
	}

	/**
	 * public serve() - Serve the ball. THis calculates a decent looking angle
	 * from the serving paddle to the ball and beyond. Then sets the ball a
	 * rollin'.
	 */
	public void serve() {
		if (!GamePlay.ball.is_started()) {

			Vector2 serveVector = new Vector2();
			serveVector = GamePlay.ball.getCenterPosition().subtract(
					this.getCenterPosition()).normalise();

			GamePlay.ball.setCurrentSpeed(GamePlay.ball.getBaseSpeed());
			GamePlay.ball.setVelocity(serveVector.multiply(GamePlay.ball
					.getCurrentSpeed()));

			GamePlay.ball.set_started(true);
			this.setServing(false);
			this.timeToServe = 40.0;

			GamePlay.serveLine.setActive(false);

		}
	}
}
