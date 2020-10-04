package farm.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import farm.Http;
import farm.HttpRequest;
import farm.HttpRequestParser;
import farm.HttpResponse;
import farm.UnknownMethodException;

class Worker implements Runnable {
	Socket socket;
	Options options;

	public Worker(Socket socket, Options options) {
		this.options = options;
		this.socket = socket;
	}

	public void run() {
		try {
			Logger log;

			// while Connection is alive
			while (true) {
				log = new Logger(new FileWriter(options.accessLog(), true));

				log.setPeerAddr(socket);

				HttpRequest request = HttpRequestParser.parse(socket.getInputStream(),
						options.debug());
				log.setRequest(request);

				HttpResponse response = createHttpResponse(request);
				response.generate(socket.getOutputStream());
				log.setResponse(response);

				log.write();

				if ("close".equals(request.getHeader("Connection")))
					break;
			}

			socket.close();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(Http.EXIT_FAILURE);
		}		
	}

	private HttpResponse createHttpResponse(HttpRequest request)
			throws IOException, UnknownMethodException {
		HttpResponse response = new HttpResponse();

		switch (request.getMethod()) {
		case "GET":
		case "HEAD":
			// Server
			response.setHeader("Server", Server.SERVER_NAME);

			// Status Code
			File targetFile = new File(options.documentRoot(),
					request.getRequestURI());
			if (!targetFile.canRead() || targetFile.isDirectory()) {
				targetFile = new File(options.documentRoot(), "error.html");
				response.setStatusCode("404");
			} else
				response.setStatusCode("200");

			// Content-Type
			response.setHeader("Content-Type", "text/html");

			// Content-Length(set same value with GET/HEAD)
			response.setHeader("Content-Length", Long.toString(targetFile.length()));

			// Body
			if ("GET".equals(request.getMethod()))
				readFile(response, targetFile.getPath());

			return response;
		default:
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