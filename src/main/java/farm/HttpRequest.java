package farm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	private String method;
	private String requestURI;
	private String httpVersion;
	private Map<String, String> headerMap = new HashMap<String, String>();

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public String getHttpVersion() {
		return httpVersion;
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

	public boolean hasMessageBody() {
		// TODO
		return false;
	}

	//
	// Generate
	//

	public void generate(OutputStream os) throws IOException {
		generateRequestLine(os);

		StringBuffer buf = new StringBuffer();
		for (var entry : headerMap.entrySet()) {
			buf.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
		}
		buf.append("\r\n");

		os.write(buf.toString().getBytes());
		os.flush();
	}

	private void generateRequestLine(OutputStream os) throws IOException {
		String buf = method + " " + requestURI + " " + httpVersion + "\r\n";
		os.write(buf.getBytes());
	}

}
