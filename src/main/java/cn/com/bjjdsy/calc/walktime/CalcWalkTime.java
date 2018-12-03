package cn.com.bjjdsy.calc.walktime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.bjjdsy.calc.entity.AccseDateAttribute;
import cn.com.bjjdsy.calc.entity.AccseTimeAttribute;
import cn.com.bjjdsy.calc.entity.AccseWalkTime;
import cn.com.bjjdsy.common.CalcConstant;

public class CalcWalkTime {

	public static void main(String[] args) {

	}

	private int getTimeAttribute(String stationCode, AccseDateAttribute dateAttr) {
		Map<Integer, Integer> accseTimeAttributeMap = CalcConstant.accseTimeAttributeMap;
		int dateType = CalcConstant.dateTypeMap.get(dateAttr.getSection() + ":" + dateAttr.getDateType());
		List<AccseTimeAttribute> accseTimeAttributeList = this.findAccseTimeAttribute("103", dateType);
		for (AccseTimeAttribute timeAttr : accseTimeAttributeList) {
			accseTimeAttributeMap.put(timeAttr.getBeginTime(), timeAttr.getTimeAttribute());
		}
		accseTimeAttributeMap.put(23400, 0);
		// get prev elements
		List<Integer> keyList = new ArrayList<>(accseTimeAttributeMap.keySet());
		int idx = keyList.indexOf(23400);
		int prev = keyList.get(idx - 1);
		int timeAttribute = accseTimeAttributeMap.get(prev);
		System.out.println(prev + ":" + timeAttribute);
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

	public AccseWalkTime getOWalkTime(String cmDate, String fromAccStationCode, int toDirect) {
		return this.getWalkTime(cmDate, fromAccStationCode, fromAccStationCode, 0, toDirect);
	}

	public AccseWalkTime getDWalkTime(String cmDate, String toAccStationCode, int fromDirect) {
		return this.getWalkTime(cmDate, toAccStationCode, toAccStationCode, fromDirect, 0);
	}

	public AccseWalkTime getTWalkTime(String cmDate, String fromAccStationCode, String toAccStationCode, int fromDirect,
			int toDirect) {
		return this.getWalkTime(cmDate, fromAccStationCode, toAccStationCode, fromDirect, toDirect);
	}

	private AccseWalkTime getWalkTime(String cmDate, String fromAccStationCode, String toAccStationCode, int fromDirect,
			int toDirect) {
		AccseDateAttribute dateAttr = this.getAccseDateAttribute(cmDate);
		int dateType = dateAttr.getDateType();
		int timeAttr = this.getTimeAttribute(fromAccStationCode, dateAttr);
		String key = fromAccStationCode + ":" + toAccStationCode + ":" + dateType + ":" + timeAttr + ":" + fromDirect
				+ ":" + toDirect;
		return CalcConstant.accseWalkTimeDict.get(key);
	}

}
