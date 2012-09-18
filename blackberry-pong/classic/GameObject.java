package classic;

import net.rim.device.api.system.Bitmap;

/**
 * @author Shadowcat Productions
 * @version 1.0 This class defines information about game objects. It is meant
 *          to be extended and re-implemented for specific use.
 */
public class GameObject {
	private Vector2 position; // A vector defining the Game Objects 2d position
	private Vector2 velocity; // A vector defining the Game Objects 2d velocity
	
	private Bitmap bitmap; // Stores information about the bitmap content for
							// the game object
	private GameObject parent; // defined if this game object is a child object
								// of some other game object
	boolean initialized = false;

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
		setBitmap(newBitmap);
		setParent(newParent);
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
	
	/**
	 * public getVelocity()
	 * @return velocity (Vector2)
	 *  - Returns the current velocity of the GameObject
	 */
	public Vector2 getVelocity() {
		return velocity;
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
	
	// Methods
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * public update()
	 *  - Method that updates this GameObject on every frame.
	 */
	public void update() {		
		this.setPosition(this.getPosition().add(this.getVelocity()));
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
		
	}
}
