package farm;

public class HttpResponse {
    private String contentType;
    private String statusCode;
    private String reasonPhrase;

    public String genStatusLine() {
        return HttpServer.HTTP_VERSION + " " + statusCode + " " + reasonPhrase + "\r\n";
    }

    public String genContentType() {
        return "Content-Type: " + contentType + "\r\n";
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

}
