package cn.com.bjjdsy.common.util;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author jingjiwu
 * @date 2017年3月28日 下午6:19:42
 */
public class JsonUtils {

	/**
	 * 
	 * @param obj
	 * @return
	 * @author jingjiwu
	 * @date 2017年3月28日 下午6:19:46
	 */
	public static String writeObject(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			return JSON.toJSONString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @author jingjiwu
	 * @date 2017年3月28日 下午6:19:49
	 */
	public static <E> E readObject(String json, Class<E> clazz) {
		if (json == null) {
			return null;
		}
		try {
			return JSON.parseObject(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
