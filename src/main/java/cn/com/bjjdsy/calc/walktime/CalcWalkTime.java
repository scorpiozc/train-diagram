package cn.com.bjjdsy.calc.walktime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bjjdsy.calc.entity.AccseDateAttribute;
import cn.com.bjjdsy.calc.entity.AccseTimeAttribute;
import cn.com.bjjdsy.calc.entity.AccseWalkTime;
import cn.com.bjjdsy.calc.entity.WalkTimeQO;
import cn.com.bjjdsy.common.CalcConstant;

public class CalcWalkTime {

	static final Logger logger = LoggerFactory.getLogger(CalcWalkTime.class);

	public static void main(String[] args) {

	}

	private int getTimeAttribute(String stationCode, int entryTime, AccseDateAttribute dateAttr) {
		Map<Integer, Integer> accseTimeAttributeMap = CalcConstant.accseTimeAttributeMap;
		int dateType = CalcConstant.dateTypeMap.get(dateAttr.getSection() + ":" + dateAttr.getDateType());
		List<AccseTimeAttribute> accseTimeAttributeList = this.findAccseTimeAttribute(stationCode, dateType);
		for (AccseTimeAttribute timeAttr : accseTimeAttributeList) {
			accseTimeAttributeMap.put(timeAttr.getBeginTime(), timeAttr.getTimeAttribute());
		}
		accseTimeAttributeMap.put(23400, 0);
//		accseTimeAttributeMap.keySet().forEach(k -> {
//			System.out.println(k);
//		});
		// get prev elements
		List<Integer> keyList = new ArrayList<>(accseTimeAttributeMap.keySet());
		int idx = keyList.indexOf(23400);
		int prev = keyList.get(idx - 1);
		int timeAttribute = accseTimeAttributeMap.get(prev);
//		System.out.println(prev + ":" + timeAttribute);
		accseTimeAttributeMap.clear();
		return timeAttribute;
	}

	private AccseDateAttribute getAccseDateAttribute(String cmDate) {
		return CalcConstant.accseDateAttributeDict.get(cmDate);
	}

	private List<AccseTimeAttribute> findAccseTimeAttribute(String stationCode, int dateType) {
		String key = stationCode + ":" + dateType;
		return CalcConstant.accseTimeAttributeDict.get(key);
	}

	public AccseWalkTime getOWalkTime(WalkTimeQO qo) {
		return this.getWalkTime(qo.getCmDate(), qo.getFromAccStationCode(), qo.getFromAccStationCode(), 0,
				qo.getToDirect(), qo.getPositionTime());
	}

	public AccseWalkTime getDWalkTime(WalkTimeQO qo) {
		return this.getWalkTime(qo.getCmDate(), qo.getToAccStationCode(), qo.getToAccStationCode(), qo.getFromDirect(),
				0, qo.getPositionTime());
	}

	public AccseWalkTime getTWalkTime(WalkTimeQO qo) {
		return this.getWalkTime(qo.getCmDate(), qo.getFromAccStationCode(), qo.getToAccStationCode(),
				qo.getFromDirect(), qo.getToDirect(), qo.getPositionTime());
	}

	private AccseWalkTime getWalkTime(String cmDate, String fromAccStationCode, String toAccStationCode,
			int fromDirect, int toDirect, int entryTime) {
		AccseDateAttribute dateAttr = this.getAccseDateAttribute(cmDate);
		int dateType = dateAttr.getDateType();
		int timeAttr = this.getTimeAttribute(fromAccStationCode, entryTime, dateAttr);
//		System.out.printf("dateType:%s timeAttr:%s\n", dateType, timeAttr);
		String key = fromAccStationCode + ":" + toAccStationCode + ":" + dateType + ":" + timeAttr + ":" + fromDirect
				+ ":" + toDirect;
//		System.out.printf("key:%s\n", key);
		return CalcConstant.accseWalkTimeDict.get(key);
	}

}
