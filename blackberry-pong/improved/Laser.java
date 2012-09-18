package improved;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

public class Laser extends GameObject{
	/**
	 * This is a constructor for the Laser class. It will construct a new game
	 * ball with the default Ball bitmap.
	 */
	
	public Laser(){
		super(Bitmap.getBitmapResource(StaticGraphics.laserGraphic));
		
		Line laserLine = new Line();
		laserLine.setActive(true);
		laserLine.setStartPosition(this.getPosition());
		laserLine.setEndPosition(new Vector2(Graphics.getScreenWidth(),this.getPosition().getY()));
		GamePlay.lineObjects.addElement(laserLine);
		this.setName("laser");
	}

	/**
	 * @param newPosition
	 *            This is a constructor for the Ball class. It will construct a
	 *            new game ball and set its position
	 */
	public Laser(Vector2 newPosition) {
		super(Bitmap.getBitmapResource(StaticGraphics.laserGraphic), newPosition);
		this.setName("laser");
	}

	/**
	 * @param newPosition
	 * @param newVelocity
	 *            This is a constructor for the Ball class. It will construct a
	 *            new game ball and set its position and velocity
	 */
	public Laser(Vector2 newPosition, Vector2 newVelocity) {
		super(Bitmap.getBitmapResource(StaticGraphics.laserGraphic), newPosition,
				newVelocity);
		this.setName("laser");
	}

	public void update() {
		super.update();
		
		if(this.getPosition().getX() > Graphics.getScreenWidth() || this.getPosition().getX() + this.getBitmap().getWidth() < 0){
			GamePlay.gameObjects.removeElement(this);
		}
		
	}
	
	public void collisionReaction(GameObject collisionObject) {
		
		double laserPower = 5;
		
		if(collisionObject.getName() == "paddle"){
			System.out.println("Collision Reaction: Laser -> Paddle");
			
		}else if(collisionObject.getName() == "ball"){
			System.out.println("Collision Reaction: Laser -> Ball");
			//collisionObject.setAppliedForce(new Vector2(0,0));
			//collisionObject.setSpinVelocity(new Vector2(0,0));
			
			double laserDirection = this.getVelocity().getX();
			double diffY = collisionObject.getCenterPosition().getY() - this.getCenterPosition().getY();
			
			
			Vector2 newBallVector = new Vector2(
					collisionObject.getVelocity().getX() - this.getVelocity().getX(), 0);
					
			
			if(newBallVector.getY() != 0){ // as to not divide by 0
				newBallVector = newBallVector.rotateX(-90/newBallVector.getY());
			}
			
			Vector2 newLaserVector = newBallVector.reflect(new Vector2(1,0));
											
			if ((newBallVector.getX() < 0 && collisionObject.getVelocity().getX() < 0) ||
				(newBallVector.getX() > 0 && collisionObject.getVelocity().getX() > 0))
			{
				newBallVector.reflect(new Vector2(0,1));
			}
			
			if ((laserDirection > 0 && newBallVector.getX() < 0) || (laserDirection < 0 && newBallVector.getX() > 0))
			{
				System.out.println("Ball going in the wrong direction.");
				newBallVector.setX(newBallVector.getX() * -1);
			}
			
			newBallVector.normalise();
			newBallVector.setY(diffY / 10);
			//newBallVector.rotateY(newBallVector.getY() * -1 * laserPower);
			newBallVector.multiply(laserPower);
			
			collisionObject.setVelocity(newBallVector);
			System.out.println("New ball vector: " + newBallVector.getX() + ", " + newBallVector.getY());
			
		}
		
		
		
		//Laser is leaving the game now	
		GamePlay.gameObjects.removeElement(this);
		
	}

}