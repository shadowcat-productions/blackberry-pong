package battleBall;

import java.util.Vector;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

/**
 * @author Shadowcat Productions
 * @version 1.0 This class defines the Main Menu.
 */
public class MainMenu extends MainScreen {

	classic.GamePlay game2D; // Instance of the GamePlay screen
	improved.GamePlay improvedGame2D; // Instance of the GamePlay screen
	int invokeID;

	/**
	 * Class Constructor, constructs the main menu
	 * 
	 */
	public MainMenu() {

		// disable vertical scroll
		super(NO_VERTICAL_SCROLL);

		// Draw the title to the screen
		LabelField title = new LabelField("Classic Pong",
				LabelField.FIELD_HCENTER);
		setTitle(title);
		getScreen().add(new CustomManager(20));

		// Add the instructions label to the screen
		add(new LabelField("Instructions", LabelField.FIELD_HCENTER));

		CustomManager instructionManager = new CustomManager(Graphics
				.getScreenHeight() - 145);
		getScreen().add(instructionManager);

		// Define the instructions
		Vector instructionText = new Vector();
		instructionText.addElement(new CustomTextField("Escape:", 1, 0, 20));
		instructionText.addElement(new CustomTextField("Quit Game", 2, 0, 20));
		instructionText.addElement(new CustomTextField("Scroll Wheel:", 1, 10,
				40));
		instructionText
				.addElement(new CustomTextField("Move Player", 2, 10, 40));

		// Draw the instructions
		for (int i = 0; i < instructionText.size(); i++) {
			CustomTextField field = (CustomTextField) instructionText
					.elementAt(i);
			field.setFont(Font.getDefault().derive(Font.PLAIN, 16));
			instructionManager.add(field);
		}

		// Add the start buttons to the screen
		add(startPong2D);
		add(startImprovedPong2D);

		// Add copyright information to the screen
		getScreen().add(new CustomManager(10));
		LabelField copyrightText = new LabelField(
				"Copyright 2009 Shadowcat Productions",
				LabelField.FIELD_HCENTER);
		copyrightText.setFont(Font.getDefault().derive(Font.ITALIC, 14));
		add(copyrightText);

	}

	// Define the button click event, it should run the game
	ButtonField startImprovedPong2D = new ButtonField("Improved Pong2D",
			ButtonField.FIELD_HCENTER | ButtonField.FIELD_BOTTOM) {

		protected boolean trackwheelClick(int status, int time) {
			improvedGame2D = new improved.GamePlay();
			getUiEngine().pushScreen(improvedGame2D);
			invokeID = getApplication().invokeLater(new Runnable() {

				public void run() {
					if (improved.GamePlay.getActive() == false) {
						getApplication().cancelInvokeLater(invokeID);
						//GamePlay.soundManager.stopMusic();
						getUiEngine().popScreen(improvedGame2D);

						String winMessage;
						if (improved.GamePlay.playerPaddle.getScore() > improved.GamePlay.enemyPaddle
								.getScore()) {
							winMessage = "You Win";
						} else if (improved.GamePlay.playerPaddle.getScore() < improved.GamePlay.enemyPaddle
								.getScore()) {
							winMessage = "You Lose";
						} else {
							winMessage = "Tie Game";
						}

						Dialog informer = new Dialog(Dialog.D_OK, "Player: "
								+ improved.GamePlay.playerPaddle.getScore()
								+ "\nEnemy: " + improved.GamePlay.enemyPaddle.getScore()
								+ "\n\n" + winMessage, 0, null, 0);
						informer.setPadding(5, 40, 5, 40);
						informer.show();

						improvedGame2D = null;
					}
				}
			}, 500, true);

			return true;
		}
	};
	
	// Define the button click event, it should run the game
	ButtonField startPong2D = new ButtonField("Pong2D",
			ButtonField.FIELD_HCENTER | ButtonField.FIELD_BOTTOM) {

		protected boolean trackwheelClick(int status, int time) {
			game2D = new classic.GamePlay();
			getUiEngine().pushScreen(game2D);
			invokeID = getApplication().invokeLater(new Runnable() {

				public void run() {
					if (classic.GamePlay.getActive() == false) {
						getApplication().cancelInvokeLater(invokeID);
						//GamePlay.soundManager.stopMusic();
						getUiEngine().popScreen(game2D);

						String winMessage;
						if (classic.GamePlay.playerPaddle.getScore() > classic.GamePlay.enemyPaddle
								.getScore()) {
							winMessage = "You Win";
						} else if (classic.GamePlay.playerPaddle.getScore() < classic.GamePlay.enemyPaddle
								.getScore()) {
							winMessage = "You Lose";
						} else {
							winMessage = "Tie Game";
						}

						Dialog informer = new Dialog(Dialog.D_OK, "Player: "
								+ classic.GamePlay.playerPaddle.getScore()
								+ "\nEnemy: " + classic.GamePlay.enemyPaddle.getScore()
								+ "\n\n" + winMessage, 0, null, 0);
						informer.setPadding(5, 40, 5, 40);
						informer.show();

						game2D = null;
					}
				}
			}, 500, true);

			return true;
		}
	};


	/**
	 * @author Shadowcat Productions
	 * @version 1.0 This class is an extension of the BlackBerry type Manager.
	 *          It is a helper class that will allow us to control a custom text
	 *          field class.
	 */
	class CustomManager extends Manager {
		int managerHeight;

		public CustomManager(int passHeight) {
			super(Manager.NO_HORIZONTAL_SCROLL | Manager.NO_VERTICAL_SCROLL);
			managerHeight = passHeight;
		}

		protected void sublayout(int width, int height) {
			CustomTextField field;
			for (int lcv = 0; lcv < getFieldCount(); lcv++) {
				field = (CustomTextField) getField(lcv);

				switch (field.getCustomStyle()) {
				case 1:
					setPositionChild(field, width / 8, field.getY());
					break;
				case 2:
					setPositionChild(field, width * 7 / 8
							- field.getPreferredWidth(), field.getY());
					break;
				default:
					setPositionChild(field, field.getX(), field.getY());

				}
				layoutChild(field, width, height);
			}
			setExtent(width, managerHeight);
		}

		public int getPreferredWidth() {
			return Graphics.getScreenWidth();
		}

		public int getPreferredHeight() {
			return Graphics.getScreenHeight();
		}
	}

	/**
	 * @author Shadowcat Productions
	 * @version 1.0 This class is an extension of the BlackBerry type
	 *          LabelField. It will allow us to draw text to the screen in a
	 *          better format.
	 */
	class CustomTextField extends LabelField {
		int x, y, style;

		CustomTextField(String label, int newStyle, int newX, int newY) {
			super(label);
			setX(newX);
			setY(newY);
			setStyle(newStyle);
		}

		int getX() {
			return x;
		}

		int getY() {
			return y;
		}

		int getCustomStyle() {
			return style;
		}

		void setX(int newX) {
			x = newX;
		}

		void setY(int newY) {
			y = newY;
		}

		void setStyle(int newStyle) {
			style = newStyle;
		}

	}

}
