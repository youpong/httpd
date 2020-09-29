package farm;

public class Util {

	// completion scheme:
	public static String completionScheme(String uri) {
		if (!uri.startsWith("http:")) {
			return "http://" + uri;
		}

		return uri;
	}
}
