package improved;

import net.rim.device.api.ui.component.Dialog;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

import net.rim.device.api.system.Alert;

/**
 * @author Shadowcat Productions
 * @version 1.0 This class handles sound implementation
 */
public class SoundManager {
	Player musicPlayer; // Java media player

	public SoundManager() {
	}

	// Play a midi file for background music
	void playMusic(String passMusic) {
		try {
			InputStream in = getClass().getResourceAsStream("/" + passMusic);
			musicPlayer = Manager.createPlayer(in, "audio/mpeg");

			musicPlayer.realize();
			musicPlayer.prefetch();
			musicPlayer.setLoopCount(-1);

			VolumeControl volume = (VolumeControl) musicPlayer
					.getControl("VolumeControl");
			volume.setLevel(50);

			//MusicMaker musicMaker = new MusicMaker();

		} catch (Exception e) {
			Dialog.alert("can't play music: " + e.getMessage());
		}
	}

	// Stop playing music
	void stopMusic() {
		try {
			// Tell player to stop playing
			musicPlayer.stop();

		} catch (Exception e) {
		}

		// Then release the data and close out the player
		musicPlayer.deallocate();
		musicPlayer.close();
	}

	// Activates the phone's vibration functionality for a specific number of ms
	public static void vibrate(int passMilli) {
		Alert.startVibrate(passMilli);
	}

	private class MusicMaker extends Thread {

		// Once constructed, run the thread
		MusicMaker() {
			start();
		}

		public void run() {
			try {
				musicPlayer.start();
			} catch (MediaException e) {
				Dialog.alert("Music F'd Up");
			}
		}
	}
}
