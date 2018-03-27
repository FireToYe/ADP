package com.zhilink.adp.client.codec;

/**
 * jaray
 */

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.zhilink.adp.client.core.AdpClient;
import com.zhilink.adp.client.exception.ApiException;
import feign.Response;
import feign.RetryableException;
import feign.Util;
import feign.codec.Decoder;

import static feign.Util.ensureClosed;

public class ApiGsonDecoder implements Decoder {
	private final Gson gson;
	private AdpClient client;

	@SuppressWarnings("rawtypes")
	public ApiGsonDecoder(AdpClient client) {
		this(new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create(), client);
	}

	public ApiGsonDecoder(Gson gson, AdpClient client) {
		this.gson = gson;
		this.client = client;
	}

	@Override
	public Object decode(Response response, Type type) throws IOException {
		if (response.status() == 404)
			return Util.emptyValueOf(type);
		if (response.body() == null)
			return null;
		Reader reader = response.body().asReader();
		ApiResponse rep = null;
		try {
			rep = gson.fromJson(reader, ApiResponse.class);
		} catch (Exception e) {// 格式有问题
			throw new ApiException("1001", "Response Format Error！Please contact the ADP developer！ ");
		}
		try {

			if (1 != rep.getHead().getStatus()) {
				System.out.println(rep.getHead().getErrorCode());
				// 未登录 重新登录
				if ("0001".equals(rep.getHead().getErrorCode())) {
					// 擦除token 信息
					this.client.InvalidToken();
					throw new RetryableException(rep.getHead().getErrorMsg() + " will login ", new Date());
				}
				throw new ApiException(rep.getHead().getErrorCode(), rep.getHead().getErrorMsg());
			}
			String json = gson.toJson(rep.getBody());
			System.out.println(json);
			return gson.fromJson(json, type);
		} catch (JsonIOException e) {
			throw new ApiException("1001", "response Format Error！Please contact the ADP developer！ ");
			// throw new CommonException(e.getMessage());
		} finally {
			ensureClosed(reader);
		}
	}
}
