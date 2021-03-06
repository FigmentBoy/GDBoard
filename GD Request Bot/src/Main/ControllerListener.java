package Main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;

class ControllerListener {

	static void hook() {

		//region Controller Listener

		Robot r = null;
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		while (true) {
			try {
				ControllerManager controllers = new ControllerManager();
				controllers.initSDLGamepad();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (true) {
					ControllerState currState = controllers.getState(0);
					if (currState.leftStickClick) {
						if (Overlay.isVisible) {
							if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
								Overlay.setWindowsInvisible();
							}
						} else {
							if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
								Overlay.setWindowsVisible();
							}
							Overlay.frame.toFront();
						}
					}
					if (currState.rightStickClick) {
						r.keyPress(KeyEvent.VK_CONTROL);
						r.keyPress(KeyEvent.VK_V);

						r.keyRelease(KeyEvent.VK_V);
						r.keyRelease(KeyEvent.VK_CONTROL);
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception ignored) {
			}
		}

		//endregion
	}
}
