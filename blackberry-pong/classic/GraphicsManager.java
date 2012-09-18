package classic;

import classic.GameObject;

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

	public void drawBackround(Graphics graphics) {
		graphics.setBackgroundColor(Color.BLACK);
		graphics.clear();

		graphics.setColor(Color.WHITESMOKE);
		graphics.drawLine(0, 0, graphics.getScreenWidth(), 0);
		graphics.drawLine(0, graphics.getScreenHeight() - 1, graphics
				.getScreenWidth(), graphics.getScreenHeight() - 1);

		int x = graphics.getScreenWidth() / 2;
		for (int y = 0; y < graphics.getScreenHeight(); y += 4) {
			graphics.drawPoint(x, y);
		}
	}

	/**
	 * @param graphics
	 * @param object
	 *            Draws Game Object bitmaps to the screen
	 */
	public void Draw(Graphics graphics, GameObject object) {
		graphics.drawBitmap((int) object.getPosition().getX(),
				(int) object.getPosition().getY(), object.getBitmap()
						.getWidth(), object.getBitmap().getHeight(),
				object.getBitmap(), 0, 0);
	}

	public void DrawString(Graphics graphics, String printString, int x, int y,
			int colour) {
		graphics.setFont(gameFont);
		graphics.setColor(colour);
		graphics.drawText(printString, x, y);
	}
}
