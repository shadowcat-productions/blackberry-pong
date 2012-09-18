package improved;

import net.rim.device.api.ui.*;

/**
 * @author Shadowcat Productions
 * @version 1.0 Graphics manager class handles drawing game objects to the
 *          screen
 */
public class GraphicsManager {

	public static Font gameFont;

	/**
	 * Constructs the Graphics Manager
	 */
	public GraphicsManager() {
		try {
			gameFont = FontFamily.forName("BBCondensed").getFont(
					FontFamily.SCALABLE_FONT, 16);
		} catch (ClassNotFoundException e) {
		}
	}

	/**
	 * public drawBackround()
	 * 
	 * @param graphics
	 *            - Graphics Draws the game background depending on current
	 *            stage.
	 */
	public void drawBackround(Graphics graphics) {

		int stage = GamePlay.getCurrentStage();

		switch (stage) {
		case 1:
			graphics.setBackgroundColor(Color.BLACK);
			break;
		case 2:
			graphics.setBackgroundColor(Color.DARKBLUE);
			break;
		case 3:
			graphics.setBackgroundColor(Color.DARKGREEN);
			break;
		case 4:
			graphics.setBackgroundColor(Color.DARKRED);
			break;
		case 5:
			graphics.setBackgroundColor(Color.BLACK);
			break;
		case 6:
			graphics.setBackgroundColor(Color.BLACK);
			break;
		case 7:
			graphics.setBackgroundColor(Color.BLACK);
			break;
		case 8:
			graphics.setBackgroundColor(Color.BLACK);
			break;
		case 9:
			graphics.setBackgroundColor(Color.BLACK);
			break;
		case 10:
			graphics.setBackgroundColor(Color.BLACK);
			break;
		default:
			graphics.setBackgroundColor(Color.BLACK);
		}

		graphics.clear();

		// Draw the game borders and dividers

		graphics.setColor(Color.WHITESMOKE);
		graphics.drawLine(0, 0, graphics.getScreenWidth(), 0);
		graphics.drawLine(0, graphics.getScreenHeight() - 1, graphics
				.getScreenWidth(), graphics.getScreenHeight() - 1);

		int x = graphics.getScreenWidth() / 2;
		for (int y = 0; y < graphics.getScreenHeight(); y += 4) {
			graphics.drawPoint(x, y);
		}
	}

	public void DrawLine(Graphics graphics, Line line) {

		if (line.isActive()) {
			int current_color = graphics.getColor();
			int current_alpha = graphics.getGlobalAlpha();

			graphics.setColor(line.getColour());
			graphics.setGlobalAlpha(line.getAlpha());

			graphics.drawLine((int) line.getStartPosition().getX(), (int) line
					.getStartPosition().getY(), (int) line.getEndPosition()
					.getX(), (int) line.getEndPosition().getY());

			graphics.setColor(current_color);
			graphics.setGlobalAlpha(current_alpha);
		}
	}

	/**
	 * @param graphics
	 * @param object
	 *            Draws Game Object bitmaps to the screen
	 */
	public void Draw(Graphics graphics, GameObject object) {
		graphics.drawBitmap((int) object.getPosition().getX(), (int) object
				.getPosition().getY(), object.getBitmap().getWidth(), object
				.getBitmap().getHeight(), object.getBitmap(), 0, 0);

		// Display messages from the message queue
		try {
			Message currentMessage = (Message) GamePlay.messageQueue
					.firstElement();
			currentMessage.updateAlpha();
			this.DrawMessage(graphics, currentMessage);
			currentMessage.decreaseSecondsLeft(1.0 / GamePlay.gameSpeed);
			if (currentMessage.getSecondsLeft() < 0.0) {
				GamePlay.messageQueue.removeElement(GamePlay.messageQueue
						.firstElement());
			}
		} catch (Exception ex) {
		}

	}

	/**
	 * public DrawMessage()
	 * 
	 * @param graphics
	 *            - Instance of the game graphics class
	 * @param theMessage
	 *            - Instance of a Message class. Takes a message and handles
	 *            displaying it for a specified period of time at a certain
	 *            alpha and colour value.
	 */
	public void DrawMessage(Graphics graphics, Message theMessage) {
		int savedAlpha = graphics.getGlobalAlpha();
		graphics.setGlobalAlpha(theMessage.getAlpha());
		this.DrawString(graphics, theMessage.getText(), theMessage.getFont(),
				theMessage.getPosition(), theMessage.getColour());
		graphics.setGlobalAlpha(savedAlpha);
	}

	/**
	 * public DrawString()
	 * 
	 * @param graphics
	 *            - Instance of the game graphics class
	 * @param printString
	 *            - The string you wish to print
	 * @param theFont
	 *            - Instance of the font class, represents what font to draw the
	 *            string with
	 * @param thePosition
	 *            - X, Y vector of the position to start drawing the string from
	 *            (position = top left)
	 * @param colour
	 *            - An integer representing what colour to daw the string in.
	 *            Draws the given string, in a given font, at a given position,
	 *            in a given colour.
	 */
	public void DrawString(Graphics graphics, String printString, Font theFont,
			Vector2 thePosition, int colour) {
		this.DrawString(graphics, printString, theFont, (int) thePosition
				.getX(), (int) thePosition.getY(), colour);
	}

	/**
	 * public DrawString()
	 * 
	 * @param graphics
	 *            - Instance of the game graphics class
	 * @param printString
	 *            - The string you wish to print
	 * @param x
	 *            - X coordinate of the top left position of the string to draw
	 * @param y
	 *            - Y coordinate of the top left position of the string to draw
	 * @param colour
	 *            - An integer representing what colour to daw the string in.
	 *            Draws the given string, in a given font, at a given position,
	 *            in a given colour.
	 */
	public void DrawString(Graphics graphics, String printString, int x, int y,
			int colour) {
		this.DrawString(graphics, printString, GraphicsManager.gameFont, x, y,
				colour);
	}

	/**
	 * public DrawString()
	 * 
	 * @param graphics
	 *            - Instance of the game graphics class
	 * @param printString
	 *            - The string you wish to print
	 * @param theFont
	 *            - Instance of the font class, represents what font to draw the
	 *            string with
	 * @param x
	 *            - X coordinate of the top left position of the string to draw
	 * @param y
	 *            - Y coordinate of the top left position of the string to draw
	 * @param colour
	 *            - An integer representing what colour to daw the string in.
	 *            Draws the given string, in a given font, at a given position,
	 *            in a given colour.
	 */
	public void DrawString(Graphics graphics, String printString, Font theFont,
			int x, int y, int colour) {
		graphics.setFont(theFont);
		graphics.setColor(colour);
		graphics.drawText(printString, x, y);
	}
}
