package cn.com.bjjdsy.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestFn {

	public static void main(String[] args) {
		testTreeMap();
	}

	private static void testTreeMap() {
		Map<Integer, String> timeMap = new TreeMap<>();
		timeMap.put(14400, "1");
		timeMap.put(25200, "2");
		timeMap.put(32400, "3");
		timeMap.put(61200, "4");
		timeMap.put(68400, "5");
		timeMap.forEach((key, value) -> {
			System.out.println(key + ":" + value);
		});
		System.out.println("------");
		timeMap.put(59710, "?");
		timeMap.forEach((key, value) -> {
			System.out.println(key + ":" + value);
		});
		System.out.println("------");
		// get prev elements
		List<Integer> list = new ArrayList<>(timeMap.keySet());
		int idx = list.indexOf(59710);
		int prev = list.get(idx - 1);
		System.out.println(prev + ":" + timeMap.get(prev));
	}
}
