package cn.com.bjjdsy.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created with IntelliJ IDEA. User: zhangjj Date: 14-2-25 Time: 上午11:38 To
 * change this template use File | Settings | File Templates.
 */
public class JacksonUtils {
	private static final ObjectMapper mapper = new ObjectMapper();

	private JacksonUtils() {
	}

	public static ObjectMapper getInstance() {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		return mapper;
	}
}
