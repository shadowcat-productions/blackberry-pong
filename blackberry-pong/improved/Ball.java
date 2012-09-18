package improved;


import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import improved.StaticGraphics;

/**
 * @author Shadowcat Productions
 * @version 1.1.1.1.1 adfasd asdfasdf asdf asdf asdfasdf
 * 
 */
public class Ball extends GameObject {
	private double baseSpeed = 1;
	private double currentSpeed = baseSpeed;
	private double maxForce = 50;
	private double spin = 0.0;
	private boolean started = false;
	private double initialVelocity; // Initial Y velocity of the ball
	private double maxY = 6;
	private double maxX = 10;

	/**
	 * This is a constructor for the Ball class. It will construct a new game
	 * ball with the default Ball bitmap.
	 */
	public Ball() {
		super(Bitmap.getBitmapResource(StaticGraphics.ballGraphic));
		this.setName("ball");
	}

	/**
	 * @param newPosition
	 *            This is a constructor for the Ball class. It will construct a
	 *            new game ball and set its position
	 */
	public Ball(Vector2 newPosition) {
		super(Bitmap.getBitmapResource(StaticGraphics.ballGraphic), newPosition);
		this.setName("ball");
	}

	/**
	 * @param newPosition
	 * @param newVelocity
	 *            This is a constructor for the Ball class. It will construct a
	 *            new game ball and set its position and velocity
	 */
	public Ball(Vector2 newPosition, Vector2 newVelocity) {
		super(Bitmap.getBitmapResource(StaticGraphics.ballGraphic), newPosition,
				newVelocity);
		this.setName("ball");
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
		super(Bitmap.getBitmapResource(StaticGraphics.ballGraphic), newPosition,
				newVelocity, newParent);
		this.setName("ball");
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
	 * public getSpin()
	 * @return spin (double)
	 *  - Returns the current spin value of the ball.  Used for curve ball effect.
	 */
	public double getSpin() {
		return spin;
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

	/**
	 * public setSpin()
	 * @param newSpin (double)
	 *  - Sets the spin value of the ball.
	 */
	public void setSpin(double newSpin) {
		spin = newSpin;
	}
	
	// Methods
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public capVelocity()
	 *  - Method that will reduce the ball Y velocity if necessary
	 */
	private void capVelocity(){
		if (this.getVelocity().getY() > maxY){
			this.setVelocityY(maxY);
		}
		if (this.getVelocity().getX() > maxX){
			this.setVelocityX(maxX);
		}
	}
	

	/**
	 * public applyForce( Vector2 incomingVector )
	 * @param incomingVector (Vector2)
	 *  - Applies the force of an incoming vector onto the velocity of the ball.
	 */
	public void applyForce( Vector2 incomingVector ){		
		this.setVelocity(this.getVelocity().add(incomingVector));
	}
	
	/* (non-Javadoc)
	 * @see library2D.GameObject#collisionReaction(library2D.GameObject)
	 * public collisionReaction(GameObject collisionObject)
	 *  - Describes the behaviour of the ball when it collides with another game object.
	 */
	public void collisionReaction(GameObject collisionObject) {
		
		//We only want to deal with hitting the paddle here:
		if(collisionObject.getName() == "paddle"){
			
			System.out.println("Collision Reaction: Ball -> Paddle");
		
			// Set the initial Y velocity for spin calculations
			this.setVelocityY(0);
			initialVelocity = 0;
			super.setAppliedForce(collisionObject.getSpinVelocity());
			
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
			//this.increaseSpeed();
			//Here let's set the speed according to where the ball reflected off of the paddle
			//Further from the center = faster
			double newSpeed = this.getBaseSpeed() + Math.abs(ballCenterPosition - collisionObjectCenterPosition) / 15;
			double D = (collisionObject.getPosition().getY() + collisionObject.getBitmap().getHeight() / 2) - this.getPosition().getY();
			if (D < 0) D *= -1;
			// Accuracy speed bonus
			if (D <= 5) {
				newVelocity.multiply(2);
			}
			newVelocity.multiply(newSpeed);
			
			this.setVelocity(newVelocity);
			initialVelocity = this.getVelocity().getY();
		}else if(collisionObject.getName() == "laser"){
			System.out.println("Collision Reaction: Ball -> Laser");
		}
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
		capVelocity();
				
		this.setVelocityY(this.getVelocity().getY() - (initialVelocity + this.getAppliedForce().getY())/220);
		
		//System.out.println("v = " + this.getVelocity().getLength());
		
		//Check the position of the ball, reflect it if it hits a wall
		if (this.getPosition().getY() >= (Graphics.getScreenHeight() - this.getBitmap().getHeight()/2)) 
		{
			System.out.println("BORK DOWN");
			this.setVelocity(this.getVelocity().reflect(Vector2.UnitY().getNormal()));
			this.setPosition(new Vector2(this.getPosition().getX(), Graphics.getScreenHeight()	- this.getBitmap().getHeight()/2));
		} 
		else if (this.getPosition().getY() <= this.getBitmap().getHeight()/2) 
		{
			System.out.println("BORK UP");
			this.setVelocity(this.getVelocity().reflect(Vector2.UnitY().getNormal()));
			this.setPosition(new Vector2(this.getPosition().getX(), this.getBitmap().getHeight()/2));
			
		}
		
		
		
		//Check the position of the ball, did a player score?
		if (this.getPosition().getX() > (Graphics.getScreenWidth() - this.getBitmap().getWidth())) {
			// Player Point
			

			GamePlay.playerPaddle.increaseScore();
			GamePlay.playerPaddle.setServing(true);
			Message message = new Message("Your Serve!", Color.GREEN);
			GamePlay.messageQueue.addElement(message);
			
			this.setAppliedForce(new Vector2(0,0));
			this.initialVelocity = 0;
			
			this.setCurrentSpeed(GamePlay.ball.getBaseSpeed());
			this.setPosition(new Vector2(Graphics.getScreenWidth() / 2
					- this.getBitmap().getWidth() / 2, Graphics
					.getScreenHeight()
					/ 2 - this.getBitmap().getHeight() / 2));
			this.setVelocity(new Vector2(0));
			this.set_started(false);
			GamePlay.checkStageProgression();
			SoundManager.vibrate(500);
			GamePlay.setMagnetFalse();
		} else if (this.getPosition().getX() < 1) {
			// Enemy Point		
			
			//GamePlay.playerPaddle.increaseScore();
			this.setAppliedForce(new Vector2(0,0));
			this.initialVelocity = 0;
			
			this.setCurrentSpeed(GamePlay.ball.getBaseSpeed());
			
			GamePlay.enemyPaddle.increaseScore();
			GamePlay.enemyPaddle.setServing(true);
			
			Message message = new Message("Enemy's Serve!", Color.RED);
			GamePlay.messageQueue.addElement(message);
			
			this.setPosition(new Vector2(Graphics.getScreenWidth() / 2
					- this.getBitmap().getWidth() / 2, Graphics
					.getScreenHeight()
					/ 2 - this.getBitmap().getHeight() / 2));
			this.setVelocity(new Vector2(0));
			this.set_started(false);
			GamePlay.checkStageProgression();
			SoundManager.vibrate(500);
			GamePlay.setMagnetFalse();
		}
	}
}
