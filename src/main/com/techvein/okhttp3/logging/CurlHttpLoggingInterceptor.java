package com.techvein.okhttp3.logging;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import okio.Buffer;

import static okhttp3.internal.platform.Platform.INFO;

public class CurlHttpLoggingInterceptor implements Interceptor {

    public interface Logger {
        void log(String message);

        /** A {@link CurlHttpLoggingInterceptor.Logger} defaults output appropriate for the current platform. */
        CurlHttpLoggingInterceptor.Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };
    }

    public CurlHttpLoggingInterceptor() {
        this(CurlHttpLoggingInterceptor.Logger.DEFAULT);
    }

    public CurlHttpLoggingInterceptor(CurlHttpLoggingInterceptor.Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String url = request.url().toString();
        String method = request.method();
        String bodyString = "";
        String contentType = "";

        if (request.body() != null) {
            String body = parseRequestBody(request.body());
            if (body != null && !body.equals("")) {
                bodyString = "-d '" + body + "'";
            }
            MediaType type = request.body().contentType();
            if (type != null) {
                contentType = "-H Content-Type: '" + type + "'";
            }
        }
        Headers headers = request.headers();
        StringBuilder headersBuilder = new StringBuilder();
        for (int i = 0, size = headers.size(); i < size; i++) {
            headersBuilder.append(" -H '" + headers.name(i)).append(": ").append(headers.value(i)  + "' \\\n");
        }
        String headersString = headersBuilder.toString();

        logger.log("curl  -X " + method + " \\\n " +
                (!contentType.equals("") ? contentType + " \\\n": "") +
                headersString +
                (!bodyString.equals("") ? bodyString + " \\\n": "") +
                "'" + url + "'");
        return chain.proceed(request);
    }

    private String parseRequestBody(final RequestBody body){
        if (body == null) {
            return null;
        }
        try {
            final Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            return null;
        }
    }
}
