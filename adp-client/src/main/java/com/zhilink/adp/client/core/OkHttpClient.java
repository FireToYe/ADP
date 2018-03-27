package com.zhilink.adp.client.core;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import feign.Client;

/**
 * benxu
 */
public final class OkHttpClient implements Client {

	private final okhttp3.OkHttpClient delegate;
	private AdpClient adpClient;

	public OkHttpClient(AdpClient adpClient) {
		this(new okhttp3.OkHttpClient(), adpClient);
	}

	public OkHttpClient(okhttp3.OkHttpClient delegate, AdpClient adpClient) {
		this.delegate = delegate;
		this.adpClient = adpClient;
	}

	static Request toOkHttpRequest(feign.Request input) {
		Request.Builder requestBuilder = new Request.Builder();
		requestBuilder.url(input.url());

		MediaType mediaType = null;
		boolean hasAcceptHeader = false;
		for (String field : input.headers().keySet()) {
			if (field.equalsIgnoreCase("Accept")) {
				hasAcceptHeader = true;
			}

			for (String value : input.headers().get(field)) {
				requestBuilder.addHeader(field, value);
				if (field.equalsIgnoreCase("Content-Type")) {
					mediaType = MediaType.parse(value);
					if (input.charset() != null) {
						mediaType.charset(input.charset());
					}
				}
			}
		}
		// Some servers choke on the default accept string.
		if (!hasAcceptHeader) {
			requestBuilder.addHeader("Accept", "*/*");
		}

		byte[] inputBody = input.body();
		boolean isMethodWithBody = "POST".equals(input.method()) || "PUT".equals(input.method());
		if (isMethodWithBody) {
			requestBuilder.removeHeader("Content-Type");
			if (inputBody == null) {
				// write an empty BODY to conform with okhttp 2.4.0+
				// http://johnfeng.github.io/blog/2015/06/30/okhttp-updates-post-wouldnt-be-allowed-to-have-null-body/
				inputBody = new byte[0];
			}
		}

		RequestBody body = inputBody != null ? RequestBody.create(mediaType, inputBody) : null;
		requestBuilder.method(input.method(), body);
		return requestBuilder.build();
	}

	private static feign.Response toFeignResponse(Response input) throws IOException {
		return feign.Response.builder().status(input.code()).reason(input.message()).headers(toMap(input.headers()))
				.body(toBody(input.body())).build();
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Collection<String>> toMap(Headers headers) {
		return (Map) headers.toMultimap();
	}

	private static feign.Response.Body toBody(final ResponseBody input) throws IOException {
		if (input == null || input.contentLength() == 0) {
			if (input != null) {
				input.close();
			}
			return null;
		}
		final Integer length = input.contentLength() >= 0 && input.contentLength() <= Integer.MAX_VALUE
				? (int) input.contentLength() : null;

		return new feign.Response.Body() {

			@Override
			public void close() throws IOException {
				input.close();
			}

			@Override
			public Integer length() {
				return length;
			}

			@Override
			public boolean isRepeatable() {
				return false;
			}

			@Override
			public InputStream asInputStream() throws IOException {
				return input.byteStream();
			}

			@Override
			public Reader asReader() throws IOException {
				return input.charStream();
			}
		};
	}

	@SuppressWarnings("serial")
	@Override
	public feign.Response execute(feign.Request input, feign.Request.Options options) throws IOException {
		okhttp3.OkHttpClient requestScoped;
		// mark token in request
//		Map<String, Collection<String>> headers = input.headers();
//		Map<String, Collection<String>> newheaders = new HashMap<String, Collection<String>>();
//		for (Entry<String, Collection<String>> entry : headers.entrySet()) {
//			newheaders.put(entry.getKey(), entry.getValue());
//		}
//		newheaders.put("token", new ArrayList<String>() {
//			{
//				add(adpClient.getToken());
//			}
//		});
		 
		if (delegate.connectTimeoutMillis() != options.connectTimeoutMillis()
				|| delegate.readTimeoutMillis() != options.readTimeoutMillis()) {
			requestScoped = delegate.newBuilder().connectTimeout(options.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
					.readTimeout(options.readTimeoutMillis(), TimeUnit.MILLISECONDS).build();
		} else {
			requestScoped = delegate;
		}
		Request request = toOkHttpRequest(input);
		Response response = requestScoped.newCall(request).execute();
		return toFeignResponse(response).toBuilder().request(input).build();
	}

	public void addHeaders(Map<String, String> mapHeaders) {
		if (mapHeaders == null || mapHeaders.size() == 0) {
			return;
		}

		Interceptor headInterceptor = new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Builder builder = chain.request().newBuilder();
				for (Entry<String, String> kv : mapHeaders.entrySet()) {
					builder.addHeader(kv.getKey(), kv.getValue());
				}
				Request request = builder.build();
				return chain.proceed(request);
			}
		};

		this.delegate.interceptors().add(headInterceptor);
	}

	public void addHeaderToken(String token) {
		if (null != token && !"".equals(token)) {
			Map<String, String> mapHeaders = new HashMap<String, String>();
			mapHeaders.put("token", token);
			addHeaders(mapHeaders);
		}
	}

}
