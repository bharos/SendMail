package id;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class GetPropertyValues {
	
	InputStream inputStream;
 
	public String getPropValues(String p) throws IOException {
		String result = "";
		try {
			Properties prop = new Properties();
			String propFileName = "resources/config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

 
			// get the property value and print it out
			result = prop.getProperty(p);
			if(result == null)
				System.out.println("Error reading the properties file. null read.");

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
}
