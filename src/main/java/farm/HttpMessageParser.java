package farm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public abstract class HttpMessageParser {
	protected PushbackInputStream is;
	protected boolean debug;

	protected HttpMessageParser(InputStream is, boolean debug) {
		this.is = new PushbackInputStream(is);
		this.debug = debug;
	}

	protected void consum(int expected) throws IOException, UnexpectedCharException {
		int c = is.read();
		if (c != expected)
			throw new UnexpectedCharException(
					"expected (" + expected + ") actually (" + c + ")");
	}
}
