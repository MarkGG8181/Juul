package juul.font;

import java.awt.Font;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FontUtil {

	private static Font getFont(Map<String, Font> locationMap, String location, int size) {
		Font font = null;
		try {
			if (locationMap.containsKey(location)) {
				font = locationMap.get(location).deriveFont(Font.PLAIN, size);
			} else {
				InputStream is = FontUtil.class.getResourceAsStream("/assets/juul/fonts/" + location);
				if (is == null) {
					throw new RuntimeException("Font resource not found: " + location);
				}
				font = Font.createFont(Font.TRUETYPE_FONT, is);
				locationMap.put(location, font);
				font = font.deriveFont(Font.PLAIN, size);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font: " + location);
			font = new Font("default", Font.PLAIN, size);
		}
		return font;
	}
	
	public static volatile int completed;
	
	public static boolean hasLoaded() {
		return completed >= 2;
	}
	
	public static CustomFontRenderer semibold_18, semibold_16, semibold_14, semibold_10, book_14;
	private static Font  semibold_18_, semibold_16_, semibold_14_, semibold_10_, book_14_;
	
	public static void bootstrap() {
		new Thread(() -> {
			Map<String, Font> locationMap = new HashMap<String, Font>();
			semibold_18_ = getFont(locationMap, "whitney-semibold.ttf", 18);
			semibold_16_ = getFont(locationMap, "whitney-semibold.ttf", 16);
			semibold_14_ = getFont(locationMap, "whitney-semibold.ttf", 14);
			semibold_10_ = getFont(locationMap, "whitney-semibold.ttf", 10);
			completed++;
		}).start();
		new Thread(() -> {
			Map<String, Font> locationMap = new HashMap<String, Font>();
			book_14_ = getFont(locationMap, "whitney-medium.ttf", 14);
			completed++;
		}).start();
		while(!hasLoaded()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		semibold_18 = CustomFontRenderer.createFontRenderer(semibold_18_);
		semibold_16 = CustomFontRenderer.createFontRenderer(semibold_16_);
		semibold_14 = CustomFontRenderer.createFontRenderer(semibold_14_);
		semibold_10 = CustomFontRenderer.createFontRenderer(semibold_10_);
		book_14 = CustomFontRenderer.createFontRenderer(book_14_);
		
	}
	
}
