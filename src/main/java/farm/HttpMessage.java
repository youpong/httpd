package farm;

import java.util.HashMap;
import java.util.Map;

public class HttpMessage {
	protected Map<String, String> headerMap = new HashMap<String, String>();
	
	public void setAllHeaders(Map<String, String> map) {
		headerMap.putAll(map);
	}
}
