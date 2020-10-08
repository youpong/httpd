package farm;

import java.io.IOException;
import java.io.OutputStream;

public class HttpRequest extends HttpMessage {
	private String method;
	private String requestURI;
	private String httpVersion;

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
