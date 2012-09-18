package battleBall;

import net.rim.device.api.ui.UiApplication;

/**
 * @author Shadowcat Productions
 * @version 1.0 The main entry point class for the game. This class creates the
 *          Main Menu and pushes it onto the screen stack.
 */
public class BattleBall extends UiApplication {

	public static void main(String[] args) {
		BattleBall pong = new BattleBall();
		pong.enterEventDispatcher();
	}

	/**
	 * Pushes the Main Menu onto the screen stack.
	 */
	public BattleBall() {
		pushScreen(new MainMenu());
	}

}
