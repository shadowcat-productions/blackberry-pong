package classic;

import classic.GamePlay;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

/**
 * @author Shadowcat Productions
 * @version 1.0
 * 
 */
public class Ball extends GameObject {
	private double baseSpeed = 4;
	private double currentSpeed = 4;
	private boolean started = false;
	private double maxYVelocity = 6;
	private double maxXVelocity = 10;

	/**
	 * This is a constructor for the Ball class. It will construct a new game
	 * ball with the default Ball bitmap.
	 */
	public Ball() {
		super(Bitmap.getBitmapResource("Content/ball.png"));
	}

	/**
	 * @param newPosition
	 *            This is a constructor for the Ball class. It will construct a
	 *            new game ball and set its position
	 */
	public Ball(Vector2 newPosition) {
		super(Bitmap.getBitmapResource("Content/ball.png"), newPosition);
	}

	/**
	 * @param newPosition
	 * @param newVelocity
	 *            This is a constructor for the Ball class. It will construct a
	 *            new game ball and set its position and velocity
	 */
	public Ball(Vector2 newPosition, Vector2 newVelocity) {
		super(Bitmap.getBitmapResource("Content/ball.png"), newPosition,
				newVelocity);
	}

	/**
	 * @param newPosition
	 * @param newVelocity
	 * @param newParent
	 *            - allows us to define child game objects when one object
	 *            belongs to another This is a constructor for the Ball class.
	 *            It will construct a new game ball and set its position,
	 *            velocity and define it as a child game object of some other
	 *            game object
	 */
	public Ball(Vector2 newPosition, Vector2 newVelocity, GameObject newParent) {
		super(Bitmap.getBitmapResource("Content/ball.png"), newPosition,
				newVelocity, newParent);
	}

	// Accessors
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public getCurrentSpeed()
	 * @return currentSpeed (double)
	 *  - Returns the current speed of the ball.
	 */
	public double getCurrentSpeed() {
		return currentSpeed;
	}
	
	/**
	 * public getBaseSpeed()
	 * @return baseSpeed (double)
	 *  - Returns the base speed of the ball, the speed that the ball is set to initially.
	 */
	public double getBaseSpeed() {
		return baseSpeed;
	}
	
	/**
	 * public is_started()
	 * @return started (boolean)
	 *  - Returns a boolean flag representing if the ball has been started yet.  Ie. Player 
	 *  clicked the trackball and started the game.
	 */
	public boolean is_started(){
		return started;
	}
	
	// Modifiers
	// ---------------------------------------------------------------------------------------------

	/**
	 * public setCurrentSpeed()
	 * @param newSpeed (double)
	 *  - Sets the current speed of the ball.
	 */
	public void setCurrentSpeed(double newSpeed) {
		currentSpeed = newSpeed;
	}	

	/**
	 * public setBaseSpeed()
	 * @param newSpeed (double)
	 *  - Sets the base speed of the ball, the speed the ball is set to initially.
	 */
	public void setBaseSpeed(double newSpeed) {
		baseSpeed = newSpeed;
	}
	
	/**
	 * public set_started()
	 * @param newStarted (boolean)
	 *  - Sets whether or not the ball has been started yet.  Ie Player 
	 *  clicked the trackball and started the game.
	 */
	public void set_started( boolean newStarted ){
		started = newStarted;
	}	
	
	// Methods
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public capVelocity()
	 *  - Method that will reduce the ball Y velocity if necessary
	 */
	private void capVelocity(){
		if (this.getVelocity().getY() > maxYVelocity){
			this.setVelocityY(maxYVelocity);
		}
		if (this.getVelocity().getX() > maxXVelocity){
			this.setVelocityX(maxXVelocity);
		}
	}
	
	/* (non-Javadoc)
	 * @see library2D.GameObject#collisionReaction(library2D.GameObject)
	 * public collisionReaction(GameObject collisionObject)
	 *  - Describes the behaviour of the ball when it collides with another game object.
	 */
	public void collisionReaction(GameObject collisionObject) {
		
		// Set the initial Y velocity for spin calculations
		this.setVelocityY(0);
		
		//Hitting a wall is handled in Ball.update(), so we're assuming here that the ball is hitting
		//a paddle.  This will have to change when we introduce new GameObjects that the ball can hit.
		
		
		double ballPosition = this.getPosition().getY();
		double ballCenterPosition = ballPosition
				+ (this.getBitmap().getHeight() / 2);

		double collisionObjectPosition = collisionObject.getPosition().getY();
		double collisionObjectCenterPosition = collisionObjectPosition
				+ (collisionObject.getBitmap().getHeight() / 2);

		double angle = Math
				.abs(3 * (ballCenterPosition - collisionObjectCenterPosition));
		if (ballCenterPosition < collisionObjectCenterPosition) {
			angle = -angle;
		} else {
		}
		Vector2 newVelocity = new Vector2();
		newVelocity = Vector2.UnitX();

		if (this.getPosition().getX() > Graphics.getScreenWidth() / 2) {
			newVelocity.multiply(-1);
			newVelocity.rotateY(-angle);
		} else {
			newVelocity.rotateY(angle);
		}
		
		//Here let's set the speed according to where the ball reflected off of the paddle
		//Further from the center = faster
		double newSpeed = this.getBaseSpeed() + Math.abs(ballCenterPosition - collisionObjectCenterPosition) / 15;
		
		newVelocity.multiply(newSpeed);
		this.setVelocity(newVelocity);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see pongPackage.GameObject#update() 
	 * public update()
	 *  - Method that will update the ball on every frame.  Has to handle hitting the wall and reflecting.
	 *  Also checks if it has scored for a particular player. 
	 */
	public void update() {
		
		//Insure to call the super update method, which applies force and velocity to position
		super.update();
		
		//Make sure the ball isn't going too fast
		this.capVelocity();
		
		//Check the position of the ball, reflect it if it hits a wall
		if (this.getPosition().getY() > (Graphics.getScreenHeight() - this
				.getBitmap().getHeight())) {
			
			//The ball hit the bottom of the screen, refect up
			this.setVelocity(this.getVelocity().reflect(
					Vector2.UnitY().getNormal()));
			this.setPosition(new Vector2(this.getPosition().getX(), Graphics
					.getScreenHeight()
					- this.getBitmap().getHeight()));
			
		} else if (this.getPosition().getY() < 1) {
			
			//The ball hit the top of the screen, reflect down
			this.setVelocity(this.getVelocity().reflect(
					Vector2.UnitY().getNormal()));
			this.setPosition(new Vector2(this.getPosition().getX(), 0));
			
		}
		
		//Check the position of the ball, did a player score?
		if (this.getPosition().getX() > (Graphics.getScreenWidth() - this
				.getBitmap().getWidth())) {

			//Increase the players score
			GamePlay.playerPaddle.increaseScore();
			
			//Set the balls position and speed to the defaults
			this.setCurrentSpeed(GamePlay.ball.getBaseSpeed());
			this.setPosition(new Vector2(Graphics.getScreenWidth() / 2
					- this.getBitmap().getWidth() / 2, Graphics
					.getScreenHeight()
					/ 2 - this.getBitmap().getHeight() / 2));
			
			//Stop the ball
			this.setVelocity(new Vector2(0));
			this.set_started(false);
			
			//Vibrate the device
			SoundManager.vibrate(500);
			
		} else if (this.getPosition().getX() < 1) {
			
			//Increase the enemies score
			GamePlay.enemyPaddle.increaseScore();
			
			//Set the balls position and speed to the defaults
			this.setCurrentSpeed(GamePlay.ball.getBaseSpeed());
			this.setPosition(new Vector2(Graphics.getScreenWidth() / 2
					- this.getBitmap().getWidth() / 2, Graphics
					.getScreenHeight()
					/ 2 - this.getBitmap().getHeight() / 2));
			
			//Stop the ball
			this.setVelocity(new Vector2(0));
			this.set_started(false);
			
			//Vibrate the device
			SoundManager.vibrate(500);
			
		}
	}
}
