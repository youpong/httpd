package farm;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpMessage {
	protected Map<String, String> headerMap = new HashMap<String, String>();
	
	public void setHeader(String key, String value) {
		headerMap.put(key, value);
	}

	public String getHeader(String key) {
		return headerMap.get(key);
	}

	public void setAllHeaders(Map<String, String> map) {
		headerMap.putAll(map);
	}
	
	public abstract void generate(OutputStream os) throws IOException;
}
