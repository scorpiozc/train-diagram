package cn.com.bjjdsy.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.com.bjjdsy.calc.entity.AccseDateAttribute;
import cn.com.bjjdsy.calc.entity.AccseTimeAttribute;
import cn.com.bjjdsy.calc.entity.AccseWalkTime;
import cn.com.bjjdsy.calc.entity.KPath;
import cn.com.bjjdsy.calc.entity.RunMap;
import cn.com.bjjdsy.calc.entity.RunMapKey;

public class CalcConstant {

	public static final String ACCSE_DATE_ATTRIBUTE = "/data/accse_date_attribute.txt";
	public static final String ACCSE_TIME_ATTRIBUTE = "/data/accse_time_attribute.txt";
	public static final String ACCSE_WALK_TIME = "/data/accse_walk_time.txt";
	public static final String K_PATH = "/data/k_path.txt";
	public static final String PLAN_RUN_MAP = "/data/plan_run_map.txt";
	public static final String SECTION_BASE_INFO = "/data/section_base_info.txt";

	/**
	 * key:section:date_type (accse_date_attribute) value:date_type
	 * (accse_time_attribute)
	 * 
	 */
	public static Map<String, Integer> dateTypeMap = new TreeMap<>();
	/**
	 * key:begin_time value:time_attribute (accse_time_attribute)
	 * 
	 */
	public static Map<Integer, Integer> accseTimeAttributeMap = new TreeMap<>();
	/**
	 * key:cmDate
	 */
	public static Map<String, AccseDateAttribute> accseDateAttributeDict = new TreeMap<>();
	/**
	 * key:accseStationCode:dateType
	 */
	public static Map<String, List<AccseTimeAttribute>> accseTimeAttributeDict = new TreeMap<>();
	/**
	 * key:fromAccStationCode:toAccStationCode:dateType:timeAttr:fromDirect:toDirect
	 */
	public static Map<String, AccseWalkTime> accseWalkTimeDict = new TreeMap<>();
	/**
	 * key:fromStation:toStation,value:direct
	 */
	public static Map<String, Integer> sectionDict = new HashMap<>();
	/**
	 * key:pltfId:direction
	 */
	public static Map<String, List<RunMapKey>> runMapKeyMap = new TreeMap<>();
	/**
	 * key:lineNo:tripNo sub key:pltfId
	 */
	public static Map<String, Map<String, RunMap>> runMapMap = new HashMap<>();

	public static List<KPath> kPathList = new ArrayList<>();
}
