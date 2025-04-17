package desktop;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.testng.Reporter;

import base.BaseTest;
import utils.ScreenshotUtil;


public class TokenGenerator extends BaseTest
{
	public static Point calculateDynamicCoordinates(int baseX, int baseY, int baseScreenWidth, int baseScreenHeight) {
		// Get the current screen size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int currentScreenWidth = (int) screenSize.getWidth();
		int currentScreenHeight = (int) screenSize.getHeight();

		// Calculate dynamic x and y based on current screen size
		int dynamicX = (int) (baseX * ((double) currentScreenWidth / baseScreenWidth));
		int dynamicY = (int) (baseY * ((double) currentScreenHeight / baseScreenHeight));

		return new Point(dynamicX, dynamicY);
	}

	public String getApproveToken(ScreenshotUtil screenshotUtil) throws IOException, InterruptedException, AWTException {
		Thread.sleep(3000L);
		/*
		 * int x = 552; int y = 319;
		 */
		// Get dynamic coordinates
		int baseX = 552;
		int baseY = 319;
		int baseScreenWidth = 1920;
		int baseScreenHeight = 1080;
		Point dynamicCoordinates = calculateDynamicCoordinates(baseX, baseY, baseScreenWidth, baseScreenHeight);

		Robot robot = new Robot();
		launchApplication(robot, "Approve", screenshotUtil);

		robot.delay(5000);
		robot.keyPress(10);
		robot.keyRelease(10);
		Thread.sleep(4000L);

		typeText(robot, prop.getProperty("ApprovePassword"));
		robot.delay(2000);
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_SPACE);
		robot.keyRelease(KeyEvent.VK_ALT);

		// Wait for the menu to appear
		robot.delay(500);  // Delay in milliseconds

		// Simulate pressing 'X' to maximize the window
		robot.keyPress(KeyEvent.VK_X);
		robot.keyRelease(KeyEvent.VK_X);
		robot.delay(500);
		robot.keyPress(9);
		robot.keyRelease(9);
		robot.keyPress(10);
		robot.keyRelease(10);
		robot.delay(3000);
		//moveAndClick(robot, x, y);
		robot.delay(3000);
		String copiedText = getClipboardText();
		Reporter.log("Copied Token: " + copiedText,true);


		robot.keyPress(18);
		robot.keyPress(115);
		robot.keyRelease(115);
		robot.keyRelease(18);
		return copiedText;
	}




	private static void launchApplication(Robot robot, String appName, ScreenshotUtil screenshotUtil) throws InterruptedException {
		robot.keyPress(524);
		robot.keyRelease(524);
		Thread.sleep(1000L);
		for (char c : appName.toCharArray()) {
			int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
			robot.keyPress(keyCode);
			robot.keyRelease(keyCode);
			Thread.sleep(100L);
		}
		robot.keyPress(10);
		robot.keyRelease(10);
		screenshotUtil.captureScreenshot("step2", "Launching he Approve app and copy the token");
	}


	private static void typeText(Robot robot, String text) {
		for (char c : text.toCharArray()) {
			int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);


			if (keyCode == 0) {
				Reporter.log("Undefined key code for character: " + c,true);

			}
			else {

				if (Character.isUpperCase(c)) {
					robot.keyPress(16);
					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);
					robot.keyRelease(16);
				} else if (c == '@') {

					robot.keyPress(16);
					robot.keyPress(50);
					robot.keyRelease(50);
					robot.keyRelease(16);
				} else {

					robot.keyPress(keyCode);
					robot.keyRelease(keyCode);
				}
				robot.delay(100);
			}
		}
	}

	private static void moveAndClick(Robot robot, int x, int y) throws InterruptedException {
		robot.mouseMove(x, y);
		Thread.sleep(1000L);
		robot.mousePress(1024);
		robot.mouseRelease(1024);
	}
	private static String getClipboardText() {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			return (String)clipboard.getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


