package desktop;

import java.awt.*;

public class DynamicWindowCoordinate {

	public static Point getWindowCoordinates() throws AWTException {
		// Get all window frames (you may need to filter for your app window)
		Window[] windows = Window.getWindows();

		// Assuming you want the first window
		Window appWindow = windows[0];

		// Get window position
		int windowX = (int) appWindow.getAlignmentX();
		int windowY = (int) appWindow.getAlignmentY();
		System.out.println("x: "+windowX);
		System.out.println("Y: "+windowY);
		return new Point(windowX, windowY);
	}

}
