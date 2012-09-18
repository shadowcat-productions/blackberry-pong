package classic;

import classic.GamePlay;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

/**
 * @author Pennell
 * 
 */
public class Paddle extends GameObject {

	private int score = 0;	
	private double currentVelocity = 0.0;
	

	/**
	 * Constructor for the Paddle. This constructor will create a Paddle Game
	 * Object with the default paddle bitmap content.
	 */
	public Paddle() {
		super(Bitmap.getBitmapResource("Content/paddle.png"));
	}

	/**
	 * @param newPosition
	 *            Constructor for the Paddle. This constructor will create a
	 *            Paddle Game Object with the default paddle bitmap content and
	 *            an initial position.
	 */
	public Paddle(Vector2 newPosition) {
		super(Bitmap.getBitmapResource("Content/paddle.png"), newPosition);
	}

	/**
	 * @param newPosition
	 * @param newVelocity
	 *            Constructor for the Paddle. This constructor will create a
	 *            Paddle Game Object with the default paddle bitmap content, an
	 *            initial position and an initial velocity.
	 */
	public Paddle(Vector2 newPosition, Vector2 newVelocity) {
		super(Bitmap.getBitmapResource("Content/paddle.png"), newPosition,
				newVelocity);
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
		super(Bitmap.getBitmapResource("Content/paddle.png"), newPosition,
				newVelocity, newParent);
	}

	// Accessors
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public getScore()
	 * @return score (int)
	 *  - Returns the current score of the paddle.
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * public getCurrentVelocity()
	 * @return currentVelocity (double)
	 *  - Returns the current velocity of the ball
	 */
	public double getCurrentVelocity(){
		return currentVelocity;
	}
	
	// Modifiers
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public setScore()
	 * @param newScore (int)
	 *  - Sets the current score of the paddle
	 */
	public void setScore( int newScore ) {
		score = newScore;
	}
	
	/**
	 * public setCurrentVelocity()
	 * @param newCurrentVelocity (double)
	 *  - Sets the current velocity field of the paddle
	 */
	public void setCurrentVelocity( double newCurrentVelocity ){
		currentVelocity = newCurrentVelocity;
	}
	
	// Methods
	// ---------------------------------------------------------------------------------------------

	/**
	 * public increaseScore()
	 *  - Method that increases the paddles score by 1
	 */
	public void increaseScore() {
		score = score + 1;
	}
	
	/* (non-Javadoc)
	 * @see library2D.GameObject#update()
	 * public update()
	 *  - Method that updates the paddle every frame.
	 */
	public void update() {
		super.update();
		if (this.getPosition().getY() > (Graphics.getScreenHeight() - this.getBitmap().getHeight())) {
			this.setPosition(new Vector2(this.getPosition().getX(), Graphics.getScreenHeight()- this.getBitmap().getHeight()));
			this.setVelocity(Vector2.Zero());
		} else if (this.getPosition().getY() < 1) {
			this.setPosition(new Vector2(this.getPosition().getX(), 0));
			this.setVelocity(Vector2.Zero());
		}
	}

	/* (non-Javadoc)
	 * @see library2D.GameObject#think()
	 * public think()
	 *  - Method that describes how the enemy paddle thinks. 
	 */
	public void think() {

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
			this.getVelocity().multiply(4);
		} else {
			this.setVelocity(Vector2.Zero());
		}
	}
	
	/* (non-Javadoc)
	 * @see library2D.GameObject#collisionReaction(library2D.GameObject)
	 * public collisionReaction(GameObject collisionObject)
	 *  - Method that describes what the paddle does when it collides with another object.
	 */
	public void collisionReaction(GameObject collisionObject) {
	
		double ballDistance = (this.getPosition().getY() + this.getBitmap().getHeight() / 2) - 
							  (collisionObject.getPosition().getY() + collisionObject.getBitmap().getHeight() / 2);
		
		if (ballDistance < 0) ballDistance *= -1;
	}
}
