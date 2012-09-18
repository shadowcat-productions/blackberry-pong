package improved;

import net.rim.device.api.ui.Color;

public final class Line {
	
	private Vector2 startPosition;
	private Vector2 endPosition;
	private int width;
	private int colour;
	private int alpha;
	private boolean active;
	
	public Line(){
		this.setStartPosition(Vector2.Zero());
		this.setEndPosition(new Vector2(5, 5));
		this.setWidth(1);
		this.setColour(Color.RED);
		this.setAlpha(255);
		this.setActive(true);
	}
	
	public Vector2 getStartPosition(){
		return startPosition;
	}
	
	public Vector2 getEndPosition(){
		return endPosition;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getColour(){
		return colour;
	}
	
	public int getAlpha(){
		return alpha;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void setStartPosition(Vector2 newStartPosition){
		startPosition = newStartPosition;
	}
	
	public void setEndPosition(Vector2 newEndPosition){
		endPosition = newEndPosition;
	}
	
	public void setWidth(int newWidth){
		width = newWidth;
	}
	
	public void setColour(int newColour){
		colour = newColour;
	}
	
	public void setAlpha(int newAlpha){
		alpha = newAlpha;
	}
	
	public void setActive(boolean newActive){
		active = newActive;
	}	
}
