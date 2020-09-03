package farm;

//import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpResponseTest {
	@Test
	public void foo() {
		HttpResponse response = new HttpResponse();
		response.setStatusCode("200");
		response.setContentType("text/html");

		assertEquals("HTTP/1.1 200 OK\r\n", response.genStatusLine());
		assertEquals("Content-Type: text/html\r\n", response.genContentType());
	}

	@Test
	public void testGen() {
		HttpResponse response = new HttpResponse();
		response.setServer("bait/0.1");
		response.setStatusCode("404");
		response.setContentType("text/html");

		//@formatter:off
		assertEquals("HTTP/1.1 404 Not Found\r\n" 
				+ "Server: bait/0.1\r\n"
				+ "Content-Type: text/html\r\n" 
				+ "Content-Length: 0\r\n" + "\r\n",
				response.gen());
		//@formatter:on
	}
}
