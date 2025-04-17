package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.Reporter;

public class BaseTest {
	public static Properties prop = new Properties();

	static String propertyfilePath;
	/*	static {
		propertyfilePath=System.getProperty("user.dir")+ "/src/main/resources/config.properties"; 
		prop = new Properties();
		try	(FileInputStream inputStream = new FileInputStream(propertyfilePath)) 
		{
			prop.load(inputStream); 
		}
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
			throw new RuntimeException("Failed to load properties file: " + propertyfilePath);
		}
	}*/


	static {
		String configFilePath = System.getProperty("configFile");

		if (configFilePath == null || configFilePath.isEmpty()) {
			Reporter.log("Config file path is not specified. Please provide it using -DconfigFile.",true);
		}

		try {
			FileInputStream input = new FileInputStream(configFilePath);

			try {
				prop.load(input);
				input.close();
			} catch (Throwable throwable) {
				try {
					input.close();
				} catch (Throwable throwable1) {
					throwable.addSuppressed(throwable1);
				}
				throw throwable;
			}
		} catch (IOException e) {
			Reporter.log("Error loading configuration file: " + e.getMessage(),true);
			e.printStackTrace();
		}

	}
}

