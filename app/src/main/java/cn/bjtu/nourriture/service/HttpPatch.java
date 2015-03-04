package cn.bjtu.nourriture.service;

import org.apache.http.client.methods.HttpPost;

/**
 * Created by ftb on 15-1-10.
 */

public class HttpPatch extends HttpPost {
    public static final String METHOD_PATCH = "PATCH";

    public HttpPatch(final String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return METHOD_PATCH;
    }
}