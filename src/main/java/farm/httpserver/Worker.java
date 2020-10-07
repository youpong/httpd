package farm.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import farm.HttpRequest;
import farm.HttpRequestParser;
import farm.HttpResponse;
import farm.UnknownMethodException;

class Worker implements Runnable {
	Socket socket;
	Logger log;
	Options options;

	public Worker(Socket socket, Logger log, Options options) {
		this.options = options;
		this.log = log;
		this.socket = socket;
	}

	public void run() {
		try {
			// while Connection is alive
			while (true) {
				HttpRequest request = HttpRequestParser.parse(socket.getInputStream(),
						options.debug());
				HttpResponse response = createHttpResponse(request);
				response.generate(socket.getOutputStream());

				log.write(socket, request, response);

				if ("close".equals(request.getHeader("Connection")))
					break;
			}

			socket.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private HttpResponse createHttpResponse(HttpRequest request)
			throws IOException, UnknownMethodException {
		HttpResponse response = new HttpResponse();

		switch (request.getMethod()) {
		case "GET" :
		case "HEAD" :
			// HTTP Version
			response.setHttpVersion(Server.HTTP_VERSION);
			
			// Status Code
			File targetFile = new File(options.documentRoot(),
					request.getRequestURI());
			if (!targetFile.canRead() || targetFile.isDirectory()) {
				targetFile = new File(options.documentRoot(), "error.html");
				response.setStatusCode("404");
			} else
				response.setStatusCode("200");

			// Server
			response.setHeader("Server", Server.SERVER_NAME);

			// Content-Type
			response.setHeader("Content-Type", "text/html");

			// Content-Length(set same value with GET/HEAD)
			response.setHeader("Content-Length", Long.toString(targetFile.length()));

			// Body
			if ("GET".equals(request.getMethod()))
				readFile(response, targetFile.getPath());

			return response;
		default :
			throw new UnknownMethodException("Method: " + request.getMethod());
		}
	}

	private void readFile(HttpResponse response, String path) throws IOException {
		InputStream is = new FileInputStream(path);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];

		int len;
		while ((len = is.read(buf, 0, 1024)) != -1) {
			os.write(buf, 0, len);
		}
		response.setBody(os.toByteArray());
		is.close();
	}
}