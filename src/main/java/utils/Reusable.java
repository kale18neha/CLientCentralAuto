package utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;

public class Reusable {
	public static void switchToNewWindow(WebDriver driver, String mainWindowHandle, Set<String> windowHandlesBefore) throws InterruptedException {
		Set<String> windowHandlesAfter = driver.getWindowHandles();
		String newWindowHandle = null;
		for (String handle : windowHandlesAfter) {
			if (!windowHandlesBefore.contains(handle)) {
				newWindowHandle = handle;

				break;
			}
		}
		if (newWindowHandle != null) {
			driver.switchTo().window(newWindowHandle);
			Reporter.log("Switched to new window with title: " + driver.getTitle(),true);
		} else {
			Reporter.log("No new window detected.",true);
		}
	}

	public static void switchToFrameInNewWindow(WebDriver driver, String framesetAttribute, String frameName) {
		WebElement frameset = driver.findElement(By.xpath("//frameset[@border and @" + framesetAttribute + "]"));
		driver.switchTo().frame(frameset.findElement(By.xpath("frame[@name='" + frameName + "']")));
	}

	public static String getLatestDownloadedFileName(String downloadDir) {
		File dir = new File(downloadDir);
		if (dir.exists() && dir.isDirectory()) {

			File[] files = dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return (new File(dir, name)).isFile();
				}
			});
			if (files != null && files.length > 0) {
				File latestFile = files[0];
				for (File file : files) {
					if (file.lastModified() > latestFile.lastModified()) {
						latestFile = file;
					}
				}
				return latestFile.getName();
			}
		}
		return null;
	}

	public static String waitForDownloadToComplete(String downloadDir) throws InterruptedException {
		File dir = new File(downloadDir);

		boolean downloaded = false;
		String downloadedFileName = "";

		while (!downloaded) {
			Reporter.log("Waiting for file to be downloaded.",true);
			File[] files = dir.listFiles((d, name) -> (name.startsWith("batch") && !name.endsWith(".crdownload")));
			if (files != null && files.length > 0) {
				downloaded = true;
				downloadedFileName = files[0].getName();
				continue;
			}
			Thread.sleep(1000L);
		}
		return downloadedFileName;
	}

	public static File unzipFile(String zipFilePath, String destDir) throws IOException {  
		Reporter.log("Unzipping the file into the folder " + destDir, true);
		File dir = new File(destDir);
		// Delete all files and subdirectories in the destination directory if they exist
		if (dir.exists()) {
			deleteDirectoryContents(dir);
		} else {
			dir.mkdirs();
		}

		File folderPath = createSubFolder(destDir);
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
		try {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				File newFile = new File(folderPath, entry.getName());
				if (entry.isDirectory()) {
					newFile.mkdirs();
				} else {
					// Ensure parent directories exist
					new File(newFile.getParent()).mkdirs();
					FileOutputStream fos = new FileOutputStream(newFile);
					try {
						byte[] buffer = new byte[1024];
						int len;
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
					} finally {
						fos.close();
					}
				}
				zis.closeEntry();
			}
		} finally {
			zis.close();
		}

		File zipFile = new File(zipFilePath);
		if (zipFile.delete()) {
			Reporter.log("Deleted ZIP file: " + zipFilePath, true);
		} else {
			Reporter.log("Failed to delete ZIP file: " + zipFilePath, true);
		}
		return folderPath;
	}

	//Deletes all files and subdirectories in a directory.

	private static void deleteDirectoryContents(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectoryContents(file);
				}
				if (!file.delete()) {
					throw new IOException("Failed to delete file: " + file.getAbsolutePath());
				}
			}
		}
	}
	private static File createSubFolder(String destDir) throws IOException {  
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File subfolder = new File(destDir, date);
		if (!subfolder.exists()) {
			subfolder.mkdirs();
		}
		return subfolder;
	}

	public static boolean readUnzippedFiles(File unzippedDir, String identifierSelected,ScreenshotUtil screenshotUtil) {
		boolean flag = false;
		File[] files = unzippedDir.listFiles();
		HashMap<String, String> map=new HashMap<>();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					Reporter.log("Reading file: " + file.getName(),true);
					try { BufferedReader br = new BufferedReader(new FileReader(file));
					try { 
						String str = "LOGO: "; 
						String line;
						while ((line = br.readLine()) != null) {
							if(line.contains(str))
							{
								Reporter.log(line.trim(),true);
								int index = line.indexOf(str) + str.length();
								String[] extractedValue = line.substring(index).trim().split(" ");
								Reporter.log("Actual value to be validate: " + extractedValue[0] + "\n",true);
								Reporter.log("Expected value: " + identifierSelected,true);
								if (identifierSelected.equals(extractedValue[0])) 
								{
									flag = true;
									Assert.assertEquals(identifierSelected, extractedValue[0], "LOGO value is not matching with the identifier selected.");
									map.put(file.getName(), "Validated");
									break;
								}
								flag = true;
								Assert.fail("LOGO value is not matching with the identifier selected.");
							}
						}
						br.close();
					} catch (Throwable throwable) {
						try {
							br.close();
						}
						catch (Throwable throwable1) {
							throwable.addSuppressed(throwable1); }
						throw throwable;
					}
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			StringBuilder sb = new StringBuilder();
			map.forEach((key, value) -> sb.append(key).append(" : ").append(value).append("<br>"));
			screenshotUtil.captureScreenshot("step7", "Sucessfully validated the LOGO value in below mentioned reports.<br>"+sb.toString());
			//deleteDirectory(unzippedDir);
		}
		return flag;
	}

	public static void deleteDirectory(File directory) {
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						if (file.delete()) {
							Reporter.log("Deleted file: " + file.getName(),true);
						} else {
							Reporter.log("Failed to delete file: " + file.getName(),true);
						}
					} else if (file.isDirectory()) {

						deleteDirectory(file);
					}}}}
		if (directory.delete()) {
			Reporter.log("Deleted directory: " + directory.getName(),true);
		} else {
			Reporter.log("Failed to delete directory: " + directory.getName(),true);
		}
	}
}