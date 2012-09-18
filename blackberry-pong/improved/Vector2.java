package improved;

/**
 * @author Shadowcat Productions
 * @version 1.0 This class is an implementation of a 2d vector.
 */
public class Vector2 {
	private double x; // 2d x component of the vector
	private double y; // 2d y component of the vector

	/**
	 * Constructor for the Vector2 Class. This constructor creates a 2d vector
	 * and sets both x and y components to 0
	 */
	public Vector2() {
		setX(0.0);
		setY(0.0);
	}

	/**
	 * @param value
	 *            Constructor for the Vector2 Class. This constructor creates a
	 *            2d vector and sets both x and y components to a given value
	 */
	public Vector2(double value) {
		setX(value);
		setY(value);
	}

	/**
	 * @param newX
	 * @param newY
	 *            Constructor for the Vector2 Class. This constructor creates a
	 *            2d vector and sets its x and y components to given values
	 */
	public Vector2(double newX, double newY) {
		setX(newX);
		setY(newY);
	}

	/**
	 * @return x component of the 2d vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return y component of the 2d vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param newX
	 *            sets the x component of the vector
	 */
	public void setX(double newX) {
		x = newX;
	}

	/**
	 * @param newY
	 *            sets the y component of the vector
	 */
	public void setY(double newY) {
		y = newY;
	}

	/**
	 * @return length of the vector
	 */
	public double getLength() {
		return Math.sqrt((x * x) + (y * y));
	}

	/**
	 * @param otherVector
	 * @return dot product of this vector with another given vector
	 */
	public double dot(Vector2 otherVector) {
		return this.getX() * otherVector.getX() + this.getY()
				* otherVector.getY();
	}

	/**
	 * @param value
	 *            multiplies this vector by a given value
	 */
	public Vector2 multiply(double value) {
		this.setX(this.getX() * value);
		this.setY(this.getY() * value);
		return this;
	}

	/**
	 * @param otherVector
	 *            multiplies this vector by a given vector
	 */
	public Vector2 multiply(Vector2 otherVector) {
		this.setX(this.getX() * otherVector.getX());
		this.setY(this.getY() * otherVector.getY());
		return this;
	}

	/**
	 * @param value
	 *            adds a given value to the vector
	 */
	public Vector2 add(double value) {
		this.setX(this.getX() + value);
		this.setY(this.getY() + value);
		return this;
	}

	/**
	 * @param otherVector
	 *            adds a given vector to the vector
	 */
	public Vector2 add(Vector2 otherVector) {
		this.setX(this.getX() + otherVector.getX());
		this.setY(this.getY() + otherVector.getY());
		return this;
	}

	/**
	 * @param value
	 *            subtracts a given value from the vector
	 */
	public Vector2 subtract(double value) {
		this.setX(this.getX() - value);
		this.setY(this.getY() - value);
		return this;
	}

	/**
	 * @param otherVector
	 *            subtracts a given vector from the vector
	 */
	public Vector2 subtract(Vector2 otherVector) {
		this.setX(this.getX() - otherVector.getX());
		this.setY(this.getY() - otherVector.getY());
		return this;
	}

	/**
	 * normalises this vector
	 */
	public Vector2 normalise() {
		double length = this.getLength();
		this.setX(this.getX() / length);
		this.setY(this.getY() / length);
		return this;
	}

	/**
	 * @return the normal of the vector
	 */
	public Vector2 getNormal() {
		double length = this.getLength();
		return new Vector2(this.getX() / length, this.getY() / length);
	}

	/**
	 * @param theta
	 *            rotates this vector along the x axis by a given angle
	 */
	public Vector2 rotateX(double theta) {
		theta = Math.toRadians(theta);
		this.setX((Math.cos(theta) * this.getX())
				- (Math.sin(theta) * this.getY()));
		return this;
	}

	/**
	 * @param theta
	 *            rotates this vector along the y access by a given angle
	 */
	public Vector2 rotateY(double theta) {
		theta = Math.toRadians(theta);
		this.setY((Math.sin(theta) * this.getX())
				+ (Math.cos(theta) * this.getY()));
		return this;
	}

	/**
	 * @param otherVector
	 * @return the distance between this vector and a given other vector
	 */
	public double distance(Vector2 otherVector) {
		return (this.getX() - otherVector.getX())
				+ (this.getY() - otherVector.getX());
	}

	/**
	 * @param otherVector
	 *            reflects the vector based on a wall normal
	 */
	public Vector2 reflect(Vector2 wallNormal) {
		return this.subtract((wallNormal.multiply(wallNormal.dot(this)))
				.multiply(2));
	}

	/**
	 * @return Static Vector Y = 1, X = 1
	 */
	public static Vector2 One() {
		return new Vector2(1.0, 1.0);
	}

	/**
	 * @return Static Vector X = 0, Y = 0
	 */
	public static Vector2 Zero() {
		return new Vector2(0.0, 0.0);
	}

	/**
	 * @return Static Unit Vector, X
	 */
	public static Vector2 UnitX() {
		return new Vector2(1.0, 0.0);
	}

	/**
	 * @return Static Unit Vector Y
	 */
	public static Vector2 UnitY() {
		return new Vector2(0.0, 1.0);
	}

	public void print() {
		System.out.println("{" + this.getX() + ", " + this.getY() + "}");
	}
}
