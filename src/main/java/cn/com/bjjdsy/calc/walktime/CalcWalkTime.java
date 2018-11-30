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

	private int getTimeAttribute(AccseDateAttribute dateAttr) {
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
		// get dateType
		AccseDateAttribute dateAttr = this.getAccseDateAttribute(cmDate);
		// get timeAttr
		int timeAttr = this.getTimeAttribute(dateAttr);
		return this.getWalkTime(fromAccStationCode, fromAccStationCode, dateAttr.getDateType(), timeAttr, 0, toDirect);
	}

	public AccseWalkTime getDWalkTime(String cmDate, String toAccStationCode, int fromDirect) {
		// get dateType
		AccseDateAttribute dateAttr = this.getAccseDateAttribute(cmDate);
		// get timeAttr
		int timeAttr = this.getTimeAttribute(dateAttr);
		return this.getWalkTime(toAccStationCode, toAccStationCode, dateAttr.getDateType(), timeAttr, fromDirect, 0);
	}

	public AccseWalkTime getTWalkTime(String cmDate, String fromAccStationCode, String toAccStationCode, int fromDirect,
			int toDirect) {
		// get dateType
		AccseDateAttribute dateAttr = this.getAccseDateAttribute(cmDate);
		// get timeAttr
		int timeAttr = this.getTimeAttribute(dateAttr);
		return this.getWalkTime(fromAccStationCode, toAccStationCode, dateAttr.getDateType(), timeAttr, fromDirect,
				toDirect);
	}

	private AccseWalkTime getWalkTime(String fromAccStationCode, String toAccStationCode, int dateType, int timeAttr,
			int fromDirect, int toDirect) {
		String key = fromAccStationCode + ":" + toAccStationCode + ":" + dateType + ":" + timeAttr + ":" + fromDirect
				+ ":" + toDirect;
		return CalcConstant.accseWalkTimeDict.get(key);
	}

}
