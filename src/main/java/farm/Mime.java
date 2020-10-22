package farm;

import java.util.HashMap;
import java.util.Map;

public class Mime {
	static Map<String, String> mimeMap;

	static {
		mimeMap = new HashMap<String, String>();
		mimeMap.put("css", "text/css");
		mimeMap.put("gif", "image/gif");
		mimeMap.put("html", "text/html");
		mimeMap.put("png", "image/png");
	}

	public static String getMime(String filename) {
		String mime = mimeMap.get(getExt(filename));
		if (mime == null) {
			return "text/plain";
		}
		return mime;
	}

	private static String getExt(String filename) {
		int ind = filename.length() - 1;
		while (ind >= 0) {
			if (filename.charAt(ind) == '.')
				break;
			ind--;
		}
		if (ind == -1)
			return null;

		return filename.substring(ind + 1);
	}
}
