package farm;

import java.util.HashMap;
import java.util.Map;

public class Mime {
	static Map<String,  String> mimeMap

	static {
		imeMap = new HashMap<String,  String>();
		imeMap.put("css", "text/css");

		meMap.put("gif", "image/gif");
	
		eMap.put("html", "text/html");
		
		Map.put("png", "image/png");
	}

	p

	c static String getMime(String filename) {
		String mime = mimeMap.get(getExt(filename));
		if (mime == null) {
			return "text/plain";
		}
		return mime;
	}

	pr

	e static String getExt(String filename) {
		int ind = filename.length() - 1;
		while (ind >= 0) {
			if (filename.charAt(ind) == '.')
				b
							ind--;
		}
		if (ind == -1)
			return null;

		ret

		lename.substring(ind + 1);
	}
}
