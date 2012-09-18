package improved;

import java.util.Timer;

import net.rim.device.api.ui.*;

public class Message {
	
	private static final Vector2 default_position = new Vector2(10, 10);
	private static final  int default_colur = Color.GREEN;
	private static final  Font default_font = GraphicsManager.gameFont;
	private static final  double default_display_seconds = 2.0;
	private static final  Align default_align = new Align();
	

	private String text;
	private int colour;
	private Vector2 position;
	private Font font;
	private Align alignment;
	private double displaySeconds;
	private double secondsLeft;
	private int alpha;
	private boolean fadeIn;

	/**
	 * public Message()
	 * 
	 * @param theText
	 *            (String) - Constructor for the Message class
	 */
	public Message(String theText) {
		this(theText, Color.GREEN);
	}

	/**
	 * public Message()
	 * 
	 * @param theText
	 *            (String)
	 * @param theColour
	 *            (int) - Constructor for the Message class
	 */
	public Message(String theText, int theColour) {
		this(theText, theColour, Message.default_position);
	}

	/**
	 * public Message()
	 * 
	 * @param theText
	 *            (String)
	 * @param thePosition
	 *            (Vector2) - Constructor for the Message class
	 */
	public Message(String theText, Vector2 thePosition) {
		this(theText, Message.default_colur, thePosition);
	}

	/**
	 * public Message()
	 * 
	 * @param theText
	 *            (String)
	 * @param theColour
	 *            (int)
	 * @param thePosition
	 *            (Vector2) - Constructor for the Message class
	 */
	public Message(String theText, int theColour, Vector2 thePosition) {
		this(theText, theColour, thePosition, Message.default_font,
				Message.default_align, Message.default_display_seconds);
	}

	/**
	 * public Message()
	 * 
	 * @param theText
	 *            (String)
	 * @param theColour
	 *            (int)
	 * @param thePosition
	 *            (Vector2)
	 * @param theDisplaySeconds
	 *            (double) - Constructor for the Message class
	 */
	public Message(String theText, int theColour, Vector2 thePosition,
			double theDisplaySeconds) {
		this(theText, theColour, thePosition, Message.default_font,
				Message.default_align, theDisplaySeconds);
	}

	/**
	 * public Message()
	 * 
	 * @param theText
	 *            (String)
	 * @param theColour
	 *            (int)
	 * @param thePosition
	 *            (Vector2)
	 * @param theFont
	 *            (Font)
	 * @param theAlignment
	 *            (Align) - Constructor for the Message class
	 */
	public Message(String theText, int theColour, Vector2 thePosition,
			Font theFont, Align theAlignment, double theDisplaySeconds) {
		this.setText(theText);
		this.setColour(theColour);
		this.setPosition(thePosition);
		this.setFont(theFont);
		this.setAlignment(theAlignment);
		this.setDisplaySeconds(theDisplaySeconds);
		this.alpha = 255;
		this.fadeIn = true;
		this.setSecondsLeft(this.getDisplaySeconds());
	}

	// Accessors
	// ---------------------------------------------------------------------------------------------

	/**
	 * public getText()
	 * 
	 * @return text (String) - Returns the text that this message will display.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * public getColour()
	 * 
	 * @return colour (int) - Returns the colour that this messages text will be
	 *         displayed in.
	 */
	public int getColour() {
		return this.colour;
	}

	/**
	 * public getPosition()
	 * 
	 * @return position (Vector2) - Returns the position this message will be
	 *         displayed at.
	 */
	public Vector2 getPosition() {
		return this.position;
	}

	/**
	 * public getFont()
	 * 
	 * @return font (Font) - Returns the font that this message will be
	 *         displayed in.
	 */
	public Font getFont() {
		return this.font;
	}

	/**
	 * public getAlignment()
	 * 
	 * @return alignment (Align) - Returns the alignment that this message will
	 *         be displayed in.
	 */
	public Align getAlignment() {
		return this.alignment;
	}

	/**
	 * public getDisplaySeconds()
	 * 
	 * @return displaySeconds (double) - Return the number of seconds to display
	 *         this message.
	 */
	public double getDisplaySeconds() {
		return this.displaySeconds;
	}
	
	/**
	 * public getSecondsLeft()
	 * @return secondsLeft (double)
	 *  - Returns the number of seconds left in this messages display
	 */
	public double getSecondsLeft() {
		return this.secondsLeft;
	}
	
	/**
	 * public getAlpha()
	 * @return alpha (int) - Return the alpha value to display this message.
	 */
	public int getAlpha() {
		return this.alpha;
	}
	
	/**
	 * public getFadeIn()
	 * @return fadeIn (boolean) - A boolean flag representing if this message should fadeIn/fadeOut when displayed
	 */
	public boolean getFadeIn(){
		return this.fadeIn;
	}

	// Modifiers
	// ---------------------------------------------------------------------------------------------

	/**
	 * public setText()
	 * 
	 * @param newText
	 *            (String) - Sets the text that this message will be displayed
	 *            in.
	 */
	public void setText(String newText) {
		this.text = newText;
	}

	/**
	 * public setColour()
	 * 
	 * @param newColour
	 *            (int) - Sets the colour that this message will be displayed
	 *            in.
	 */
	public void setColour(int newColour) {
		this.colour = newColour;
	}

	/**
	 * public setPosition()
	 * 
	 * @param newPosition
	 *            (Vector2) - Sets the position that this message will be
	 *            displayed at.
	 */
	public void setPosition(Vector2 newPosition) {
		this.position = newPosition;
	}

	/**
	 * public setPositionX()
	 * 
	 * @param newPositionX
	 *            (double) - Sets the X coordinate of the position that this
	 *            message will be displayed at.
	 */
	public void setPositionX(double newPositionX) {
		this.position.setX(newPositionX);
	}

	/**
	 * public setPositionY()
	 * 
	 * @param newPositionY
	 *            (double) - Sets the Y coordinate of the position that this
	 *            message will be displayed at.
	 */
	public void setPositionY(double newPositionY) {
		this.position.setY(newPositionY);
	}

	/**
	 * public setFont()
	 * 
	 * @param newFont
	 *            (Font) - Sets the font that this message will be displayed in.
	 */
	public void setFont(Font newFont) {
		this.font = newFont;
	}

	/**
	 * public setAlignment()
	 * 
	 * @param newAlignment
	 *            (Align) - Sets the alignment that the text will be displayed
	 *            in, relative to it's position.
	 */
	public void setAlignment(Align newAlignment) {
		this.alignment = newAlignment;
	}

	/**
	 * public setDisplaySeconds()
	 * 
	 * @param newDisplaySeconds
	 *            (double) - Set the number of seconds to display this message.
	 */
	public void setDisplaySeconds(double newDisplaySeconds) {
		this.displaySeconds = newDisplaySeconds;
	}
	
	/**
	 * public setSecondsLeft()
	 * @param newSecondsLeft (double)
	 *  - Sets the number of seconds left to display this message
	 */
	public void setSecondsLeft(double newSecondsLeft) {
		this.secondsLeft = newSecondsLeft;
	}
	
	/**
	 * public setAlpha()
	 * @param newAlpha (int) - Set the alpha value to display this message
	 */
	public void setAlpha(int newAlpha) {
		if(newAlpha > 255) newAlpha = 255;
		else if(newAlpha < 0) newAlpha = 0;
		this.alpha = newAlpha;
	}

	// Methods
	// ---------------------------------------------------------------------------------------------

	/**
	 * public decreaseSecondsLeft()
	 * 
	 * @param decreaseBySeconds
	 *            (double) - Method that will decrease the message display time
	 *            by a given number of seconds.
	 */
	public void decreaseSecondsLeft(double decreaseBySeconds) {
		this.setSecondsLeft(this.getSecondsLeft() - decreaseBySeconds);
	}
	
	public void updateAlpha(){
		
		//If we even care about updating the alpha value
		if(this.getFadeIn()){
			
			double middlePoint = this.getDisplaySeconds() / 2.0;
			
			double secondsExpired = this.getDisplaySeconds() - this.getSecondsLeft();
			double start255Window = middlePoint - (middlePoint * 0.02);
			double end255Window = middlePoint + (middlePoint * 0.02);
			
			if(secondsExpired < start255Window){
				//Alpha going down
				
				this.setAlpha((int)(255 * secondsExpired));
				
				
			}else if(secondsExpired > end255Window){
				//Alpha going up
				
				this.setAlpha((int)(255 * this.getSecondsLeft()));
				
			}
			
		}
	}

	// Internal Classes
	// ---------------------------------------------------------------------------------------------
	public static class Align {

		private int alignment;

		public Align() {
			this.alignment = Align.LEFT();
		}

		public Align(int theAlignment) {
			try {
				this.setAlignment(theAlignment);
			} catch (UnknownAlignmentException e) {

				// Fail Silently, default to align left
				this.alignment = Align.LEFT();
			}
		}

		/**
		 * public getAlignment()
		 * 
		 * @return alignment (int) - Returns the alignment.
		 */
		public int getAlignment() {
			return alignment;
		}

		/**
		 * public setAlignment()
		 * 
		 * @param newAlignment
		 *            (int) - Sets the alignment
		 */
		public void setAlignment(int newAlignment)
				throws UnknownAlignmentException {

			if (newAlignment != Align.LEFT() || newAlignment != Align.CENTER()
					|| newAlignment != Align.RIGHT()) {
				throw new UnknownAlignmentException();
			}
			alignment = newAlignment;
		}

		/**
		 * public static LEFT()
		 * 
		 * @return left (int) - Returns that value associated with a left
		 *         alignment.
		 */
		public static int LEFT() {
			return 1;
		}

		/**
		 * public static CENTER()
		 * 
		 * @return center - Returns that value associated with a center
		 *         alignment.
		 */
		public static int CENTER() {
			return 2;
		}

		/**
		 * public static RIGHT()
		 * 
		 * @return right - Returns that value associated with a right alignment.
		 */
		public static int RIGHT() {
			return 3;
		}

		class UnknownAlignmentException extends Exception {
			public UnknownAlignmentException() {
				super("Unknown alignment specified, Expected: " + Align.LEFT()
						+ ": Align.LEFT, " + Align.CENTER()
						+ ": Align.CENTER, or " + Align.RIGHT()
						+ ": Align.RIGHT");
			}
		}
	}
}
