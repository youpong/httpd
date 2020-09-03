package farm;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	private String contentType;
	private long contentLength;
	private String statusCode;
	private String serverName;
	private static Map<String, String> reasonPhrase;

	static {
		reasonPhrase = new HashMap<String, String>();
		reasonPhrase.put("200", "OK");
		reasonPhrase.put("404", "Not Found");
		//reasonPhrase.put("405", "Method Not Allowed");
		reasonPhrase.put("501", "Not Implemented");
	}

	public String gen() {
		StringBuffer buf = new StringBuffer();
		buf.append(genStatusLine());
		buf.append(genServer());
		buf.append(genContentType());
		buf.append(genContentLength());
		buf.append("\r\n");
		return buf.toString();
	}

	public String genStatusLine() {
		String phrase = reasonPhrase.get(statusCode);

		return HttpServer.HTTP_VERSION + " " + statusCode + " " + phrase + "\r\n";
	}

	private String genServer() {
		return "Server: " + this.serverName + "\r\n";
	}
	
	public String genContentType() {
		return "Content-Type: " + contentType + "\r\n";
	}

	public String genContentLength() {
		return "Content-Length: " + contentLength + "\r\n";
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentLength(long length) {
		this.contentLength = length;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setServer(String serverName) {
		this.serverName = serverName;
	}

}
