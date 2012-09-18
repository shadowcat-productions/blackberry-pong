package improved;

import java.util.Vector;


import net.rim.device.api.system.Bitmap;

/**
 * @author Shadowcat Productions
 * @version 1.0 This class defines information about game objects. It is meant
 *          to be extended and re-implemented for specific use.
 */
public class GameObject {
	private Vector2 position; // A vector defining the Game Objects 2d position
	private Vector2 velocity; // A vector defining the Game Objects 2d velocity
	private Vector2 spinVelocity; // A vector used to set the spin force of a ball from a paddle
	private Vector2 appliedForce; // A vector defining the constant force on a Game Object: ie. spin or gravity
	
	private boolean isAnimating = false, isSecond = false;
	private boolean collisionACK;
	
	private Bitmap bitmap; // Stores information about the bitmap content for
							// the game object
	private GameObject parent; // defined if this game object is a child object
								// of some other game object
	boolean initialized = false;
	
	private String name = "generic";

	/**
	 * @param newBitmapLocation
	 *            string location of the content This is a constructor for the
	 *            GameObject class. It will construct a new Game Object and set
	 *            information about its bitmap content given a string location
	 *            of that content.
	 */
	public GameObject(String newBitmapLocation) {
		this(Bitmap.getBitmapResource(newBitmapLocation),
				new Vector2(0.0, 0.0), new Vector2(0.0, 0.0));
	}

	/**
	 * @param newBitmap
	 *            This is a constructor for the GameObject class. It will
	 *            construct a new Game Object and set information about its
	 *            bitmap content
	 */
	public GameObject(Bitmap newBitmap) {
		this(newBitmap, new Vector2(0.0, 0.0), new Vector2(0.0, 0.0), null);
	}

	/**
	 * @param newBitmap
	 * @param newPosition
	 *            This is a constructor for the GameObject class. It will
	 *            construct a new Game Object and set information about its
	 *            bitmap and initial position.
	 */
	public GameObject(Bitmap newBitmap, Vector2 newPosition) {
		this(newBitmap, newPosition, new Vector2(0.0, 0.0), null);
	}

	/**
	 * @param newBitmap
	 * @param newPosition
	 * @param newVelocity
	 *            This is a constructor for the GameObject class. It will
	 *            construct a new Game Object and set information about its
	 *            bitmap, initial position and initial velocity.
	 */
	public GameObject(Bitmap newBitmap, Vector2 newPosition, Vector2 newVelocity) {
		this(newBitmap, newPosition, newVelocity, null);
	}

	/**
	 * @param newBitmap
	 * @param newPosition
	 * @param newVelocity
	 * @param newParent
	 *            This is a constructor for the GameObject class. It will
	 *            construct a new Game Object and set information about its
	 *            bitmap, position, velocity and parent Game Object
	 */
	public GameObject(Bitmap newBitmap, Vector2 newPosition,
			Vector2 newVelocity, GameObject newParent) {
		position = newPosition;
		velocity = newVelocity;
		appliedForce = new Vector2(0,0);
		setBitmap(newBitmap);
		setParent(newParent);
		collisionACK = true;
	}
	
	// Accessors
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public getPosition()
	 * @return position (Vector2)
	 *  - Returns the current position of the GameObject
	 */
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getCenterPosition() {
		double centerX = this.getPosition().getX() + (this.getBitmap().getWidth() / 2.0);
		double centerY = this.getPosition().getY() + (this.getBitmap().getHeight() / 2.0);
		return new Vector2(centerX, centerY);
	}
	
	/**
	 * public getVelocity()
	 * @return velocity (Vector2)
	 *  - Returns the current velocity of the GameObject
	 */
	public Vector2 getVelocity() {
		return velocity;
	}
	
	/**
	 * public getAppliedForce()
	 * @return appliedForce (Vector2)
	 *  - Returns the current force being applied to the GameObject
	 */
	public Vector2 getAppliedForce() {
		return appliedForce;
	}
	
	/**
	 * public getBitmap()
	 * @return bitmap (Bitmap)
	 *  - Returns the associated display bitmap for the GameObject
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	/**
	 * public getParent()
	 * @return parent (GameObject)
	 *  - Returns the parent of the GameObject, if one exists.
	 */
	public GameObject getParent() {
		return parent;
	}
	
	/**
	 * public getSpinVelocity()
	 * @return spinVelocity (Vector2)
	 *  - Gets the current spin of the GameObject
	 */
	public Vector2 getSpinVelocity () {
		return spinVelocity;
	}
	
	/**
	 * public getCollisionState()
	 * @return collisionACK (bool)
	 *  - Gets the current state on whether an object should be detected for collision
	 */
	public boolean getCollisionState(){
		return this.collisionACK;
	}
	
	/**
	 * public getName()
	 * @return name (string)
	 *  - Gets the name of the current object
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * public getAnimationState()
	 * @return isAnimating (bool)
	 *  - Gets the current state on whether an object should be animated
	 */
	public boolean getAnimationState(){
		return this.isAnimating;
	}
	
	/**
	 * public isSecond()
	 * @return isSecond (bool)
	 *  - Gets the current state of whether the object is up second for animation
	 */
	public boolean isSecond(){
		return this.isSecond;
	}

	// Modifiers
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public changeBitmap()
	 * @param newBitmap(String)
	 *  - changes the current GameObjects bitmap source
	 */
	public void changeBitmap(String newBitmap){
		setBitmap(Bitmap.getBitmapResource(newBitmap));
	}
	
	public void changeBitmap(Bitmap newBitmap) {
		// TODO Auto-generated method stub
		setBitmap(newBitmap);
	}
	
	/**
	 * public setSpinVelocity()
	 * @param newSpin (Vector2)
	 *  - Sets the current spin of the GameObject
	 */
	public void setSpinVelocity(Vector2 newSpin) {
		spinVelocity = newSpin;
	}
	
	/**
	 * public setCenterPosition()
	 * @param centerX (double)
	 * @param centerY (double)
	 *  - Sets the current position of the GameObject based on its center position
	 */
	public void setCenterPosition(Vector2 newCenterPosition){
		this.setPositionX(newCenterPosition.getX() - (this.getBitmap().getWidth() / 2.0));
		this.setPositionY(newCenterPosition.getY() - (this.getBitmap().getHeight() / 2.0));
	}
	
	/**
	 * public setPosition()
	 * @param newPosition (Vector2)
	 *  - Sets the current position of the GameObject
	 */
	public void setPosition(Vector2 newPosition) {
		position = newPosition;
	}
	
	/**
	 * public setPositionX()
	 * @param newX (double)
	 *  - Sets the X coordinate of the GameObjects position
	 */
	public void setPositionX(double newX) {
		position.setX(newX);
	}
	
	/**
	 * public setPositionY()
	 * @param newY (double)
	 *  - Sets the Y coordinate of the GameObjects position
	 */
	public void setPositionY(double newY) {
		position.setY(newY);
	}
	
	/**
	 * public setVelocity()
	 * @param newVelocity (Vector2)
	 *  - Sets the velocity of the GameObject
	 */
	public void setVelocity(Vector2 newVelocity) {
		velocity = newVelocity;
	}
	
	/**
	 * public setVelocityX()
	 * @param newX (double)
	 *  - Sets the X component of the GameObjects velocity
	 */
	public void setVelocityX(double newX) {
		velocity.setX(newX);
	}
	
	/**
	 * public setVelocityY()
	 * @param newY (double)
	 *  - Sets the Y component of the GameObjects velocity
	 */
	public void setVelocityY(double newY) {
		velocity.setY(newY);
	}

	/**
	 * public setAppliedForce()
	 * @param newAppliedForce (Vector2)
	 *  - Sets a force to be applied to the GameObect on the next update.
	 */
	public void setAppliedForce(Vector2 newAppliedForce) {
		appliedForce = newAppliedForce;
	}
	
	/**
	 * public setBitmap()
	 * @param newBitmap (Bitmap)
	 *  - Sets the associated display bitmap for this GameObject
	 */
	public void setBitmap(Bitmap newBitmap) {
		bitmap = newBitmap;
	}

	/**
	 * public setBitmap()
	 * @param location (String)
	 *  - Sets the associated display bitmap for this GameObject
	 */
	public void setBitmap(String location) {
		bitmap = Bitmap.getBitmapResource(location);
	}
	
	/**
	 * public setParent()
	 * @param newParent (GameObject)
	 *  - Sets the parents GameObject for this GameObject
	 */
	public void setParent(GameObject newParent) {
		parent = newParent;
	}
	
	/**
	 * public setCollisionFalse()
	 *  - Sets the current state on whether an object should be detected for collision
	 */
	public void setCollisionFalse(){
		collisionACK = false;
	}
	
	/**
	 * public setName()
	 *  - Sets the name for the current object
	 */
	public void setName(String newName){
		name = newName;
	}
	
	/**
	 * public setSecondAnimation()
	 *  - Sets the current object as the second object to be animated
	 */
	public void setSecondAnimation(){
		isSecond = true;
	}
	
	/**
	 * public isAnimating()
	 *  - Sets the current state on whether an object should be animating and its desired animation velocity
	 */
	public void startAnimating(Vector2 newAnimationVector){
		this.setVelocity(newAnimationVector);
		this.isAnimating = true;
	}
	
	/**
	 * public stopAnimating()
	 *  - stops the current object from animating by resetting its velocity and changing its animating state
	 */
	public void stopAnimating(){
		this.setVelocity(Vector2.Zero());
		this.isSecond = false;
		this.isAnimating = false;
	}
	
	// Methods
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public update()
	 *  - Method that updates this GameObject on every frame.
	 */
	public void update() {
		Vector2 newPosition;
		newPosition = this.getPosition();
		
		//The balls new position is a function of his current velocity, and any force that has been
		//applied to it.
		newPosition.add(this.getVelocity());
		//newPosition.add(this.getAppliedForce());
		
		this.setPositionX(newPosition.getX());
		this.setPositionY(newPosition.getY());
	}

	/**
	 * public think()
	 *  - Method that describes how this GameObject thinks if controlled by AI. 
	 */
	public void think() {

	}

	/**
	 * public collisionReaction()
	 * @param collisionObject (GameObject)
	 *  - Method that describes how this GameObject reacts to colliding with another GameObject
	 */
	public void collisionReaction(GameObject collisionObject) {
		collisionObject.setAppliedForce(this.getSpinVelocity());
	}

	
}
