package gdx.net;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Net {


        public static interface HttpResponse {
                byte[] getResult ();

                String getResultAsString ();

                InputStream getResultAsStream ();

                HttpStatus getStatus ();
                
                String getHeader(String name);

                Map<String, List<String>> getHeaders ();
        }

        public static interface HttpMethods {

                public static final String GET = "GET";
                public static final String POST = "POST";
                public static final String PUT = "PUT";
                public static final String DELETE = "DELETE";

        }

        public static class HttpRequest {

                private final String httpMethod;
                private String url;
                private Map<String, String> headers;
                private int timeOut = 0;

                private String content;
                private InputStream contentStream;
                private long contentLength;

                /** Creates a new HTTP request with the specified HTTP method, see {@link HttpMethods}.
                 * @param httpMethod This is the HTTP method for the request, see {@link HttpMethods} */
                public HttpRequest (String httpMethod) {
                        this.httpMethod = httpMethod;
                        this.headers = new HashMap<String, String>();
                }

                /** Sets the URL of the HTTP request.
                 * @param url The URL to set. */
                public void setUrl (String url) {
                        this.url = url;
                }

                /** Sets a header to this HTTP request. Headers definition could be found at <a
                 * href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">HTTP/1.1: Header Field Definitions</a> document.
                 * @param name the name of the header.
                 * @param value the value of the header. */
                public void setHeader (String name, String value) {
                        headers.put(name, value);
                }

                /** Sets the content to be used in the HTTP request.
                 * @param content A string encoded in the corresponding Content-Encoding set in the headers, with the data to send with the
                 * HTTP request. For example, in case of HTTP GET, the content is used as the query string of the GET while on a
                 * HTTP POST it is used to send the POST data. */
                public void setContent (String content) {
                        this.content = content;
                }

                /** Sets the content as a stream to be used for a POST for example, to transmit custom data.
                 * @param contentStream The stream with the content data. */
                public void setContent (InputStream contentStream, long contentLength) {
                        this.contentStream = contentStream;
                        this.contentLength = contentLength;
                }

                /** Sets the time to wait for the HTTP request to be processed, use 0 block until it is done. The timeout is used for both
                 * the timeout when establishing TCP connection, and the timeout until the first byte of data is received.
                 * @param timeOut the number of milliseconds to wait before giving up, 0 or negative to block until the operation is done */
                public void setTimeOut (int timeOut) {
                        this.timeOut = timeOut;
                }

                /** Returns the timeOut of the HTTP request.
                 * @return the timeOut. */
                public int getTimeOut () {
                        return timeOut;
                }

                /** Returns the HTTP method of the HttpRequest. */
                public String getMethod () {
                        return httpMethod;
                }

                /** Returns the URL of the HTTP request. */
                public String getUrl () {
                        return url;
                }

                /** Returns the content string to be used for the HTTP request. */
                public String getContent () {
                        return content;
                }

                /** Returns the content stream. */
                public InputStream getContentStream () {
                        return contentStream;
                }

                /** Returns the content length in case content is a stream. */
                public long getContentLength () {
                        return contentLength;
                }

                /** Returns a Map<String, String> with the headers of the HTTP request. */
                public Map<String, String> getHeaders () {
                        return headers;
                }

        }

        public static interface HttpResponseListener {
                void handleHttpResponse (HttpResponse httpResponse);

                void failed (Throwable t);

        }

        public void sendHttpRequest (HttpRequest httpRequest, HttpResponseListener httpResponseListener);

}