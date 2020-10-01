package farm.httpserver;

import java.io.IOException;
import java.io.Writer;
import java.net.Socket;
//import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import farm.HttpRequest;
import farm.HttpResponse;

public class Logger {
	Writer out;
	Date requestDate;
	HttpRequest request;
	HttpResponse response;
	String peerAddr;

	public Logger(Writer out) {
		this.out = out;
	}

	public void setPeerAddr(Socket socket) {
		peerAddr = socket.getInetAddress().getHostAddress();
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
		this.requestDate = new Date();
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
		SimpleDateFormat fmt = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z",
				Locale.US);
		buf.append("[" + fmt.format(requestDate) + "] ");

		buf.append("\"" + request.getMethod() + " " + request.getRequestURI() + " "
				+ request.getHttpVersion() + "\" ");

		if (response == null) {
			buf.append("\n");
			out.write(buf.toString());
			out.flush();
			return;
		}

		buf.append(response.getStatusCode() + " ");

		// Content-Length
		if (Long.parseLong(response.getHeader("Content-Length")) == 0)
			buf.append("\"-\" ");
		else
			buf.append(response.getHeader("Content-Length") + " ");

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
