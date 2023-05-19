package utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class payLoadConverter {
	
	public static String generatePayloadString(String fileName) throws IOException {
		
		String FileName = System.getProperty("user.dir") + "/src/test/resources/" + fileName ;
		
//		converting json into String
		String str = new String( Files.readAllBytes(Paths.get(FileName)) ) ;
		return str;
		
	}
}
