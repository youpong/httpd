package farm;

import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
//import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HttpLog {
	Writer out;
	Date date;
	HttpRequest request;
	HttpResponse response;
	String peerAddr;

	public HttpLog(Writer out) {
		this.out = out;
		this.date = new Date();
	}

	public void setPeerAddr(Socket socket) {
		peerAddr = socket.getInetAddress().getHostAddress();// + ":" + socket.getPort();
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}

	/**
	 * 192.168.1.7 - - [22/Aug/2020:11:48:57 +0900] "GET /favicon.ico HTTP/1.1" 404
	 * 197 "http://192.168.1.9/" "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6)
	 * AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36"
	 */
	public void write() throws IOException {
		StringBuffer buf = new StringBuffer();

		buf.append(peerAddr + " - - ");

		// 22/Aug/2020:09:29:58 +0900
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
		buf.append("[" + fmt.format(date) + "] ");

		buf.append("\"" + request.genRequestLine() + "\" ");

		if (response != null)
			buf.append(response.getStatusCode() + " ");

		// Content-Length
		if (response.getContentLength() == 0)
			buf.append("\"-\" ");
		else
			buf.append(response.getContentLength() + " ");

		// Referer
		if (request.getHeader("Referer") == null)
			buf.append("\"-\" ");
		else
			buf.append("\"" + request.getHeader("Referer") + "\" ");
		
		// User-Agent
		if (request.getHeader("User-Agent") == null)
			buf.append("\"-\" ");
		else
			buf.append("\"" + request.getHeader("User-Agent") + "\" ");
		
		buf.append("\n");
		out.write(buf.toString());
		out.flush();
	}
}
