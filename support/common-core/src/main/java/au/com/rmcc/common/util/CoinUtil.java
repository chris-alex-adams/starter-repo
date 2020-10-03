package au.com.rmcc.common.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoinUtil {
	
	static Logger logger = LoggerFactory.getLogger(CoinUtil.class);
	
	public static String encodeBase64FromBufferedImage(BufferedImage input) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(input, "png", bos);
		} catch (IOException e) {
			logger.error("Failed to encode image: {}", e);
		}
		byte[] imageBytes = bos.toByteArray();
		String encodedString = encodeBase64(imageBytes);
		return encodedString;
	}
	
	public static String encodeBase64(byte[] input) {
		String encodedString = 
				  Base64.getEncoder().withoutPadding().encodeToString(input);
		return encodedString;
	}
	
	public static BufferedImage decodeBase64ToBufferedImage(String input) {
		BufferedImage bufferedImage = null;
		byte[] decodedBytes = Base64.getDecoder().decode(input);
		ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
		try {
			bufferedImage = ImageIO.read(bis);
		} catch (IOException e) {
			logger.error("Failed to decode image: {}", e);
		}
		return bufferedImage;
	}
	
	public static String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	

}
