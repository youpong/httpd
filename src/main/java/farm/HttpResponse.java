package farm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * respsents HTTP Response message.
 * 
 * @author nakajimay
 */
public class HttpResponse {
	private final String HTTP_VERSION = "HTTP/1.1";
	private static Map<String, String> reasonPhraseMap;
	private String httpVersion;
	private String statusCode;
	private String reasonPhrase;
	private Map<String, String> headerMap = new HashMap<String, String>();
	private byte[] body = null;

	static {
		reasonPhraseMap = new HashMap<String, String>();
		reasonPhraseMap.put("200", "OK");
		reasonPhraseMap.put("404", "Not Found");
		// reasonPhrase.put("405", "Method Not Allowed");
		reasonPhraseMap.put("501", "Not Implemented");
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setResonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setHeader(String key, String value) {
		headerMap.put(key, value);
	}

	public void setAllHeaders(Map<String, String> map) {
		headerMap.putAll(map);
	}

	public String getHeader(String key) {
		return headerMap.get(key);
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	/**
	 * write Contents, HTTP message payload.
	 */
	public void writeBody(OutputStream os) throws IOException {
		os.write(body);
		os.flush();
	}

	/**
	 * Generate HTTP message. put it on HTTP.
	 */
	public void generate(OutputStream os) throws IOException {
		generateStatusLine(os);
		generateAllHeaders(os);
		os.write("\r\n".getBytes());
		generateBody(os);

		os.flush();
	}

	private void generateStatusLine(OutputStream os) throws IOException {
		String buf = HTTP_VERSION + " " + statusCode + " "
				+ reasonPhraseMap.get(statusCode) + "\r\n";
		os.write(buf.getBytes());
	}

	private void generateAllHeaders(OutputStream os) throws IOException {
		StringBuffer buf = new StringBuffer();

		for (var entry : headerMap.entrySet()) {
			buf.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
		}

		if (!headerMap.containsKey("Content-Length"))
			buf.append("Content-Length: 0\r\n");

		os.write(buf.toString().getBytes());
	}

	private void generateBody(OutputStream os) throws IOException {
		if (body == null)
			return;

		os.write(body);
		os.flush();
	}
}
