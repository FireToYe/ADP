package com.zhilink.adp.client.codec;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhilink.adp.client.core.AdpClient;

import feign.RequestTemplate;
import feign.codec.Encoder;

public class ApiGsonEncoder implements Encoder {

	private final Gson gson;
	private AdpClient client;
	@SuppressWarnings("rawtypes")
	public ApiGsonEncoder(AdpClient client) {
		this(new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create(),client);
	}

	public ApiGsonEncoder(Gson gson,AdpClient client) {
		this.client=client;
		this.gson = gson;
	}

	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) {
		// ApiRequest req = new ApiRequest(object);
		// System.out.println(gson.toJson(req, ApiRequest.class));
		// template.body(gson.toJson(req, ApiRequest.class));
		template.body(gson.toJson(object, bodyType));
	}

	// public static void main(String args[]) {
	// ApiRequest req = new ApiRequest("222");
	// Gson gson = new GsonBuilder()
	// .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
	// .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
	// .create();
	// System.out.println(gson.toJson(req));
	//
	// }

}
