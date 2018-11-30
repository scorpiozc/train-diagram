package cn.com.bjjdsy.calc.walktime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;

import cn.com.bjjdsy.calc.entity.AccseDateAttribute;
import cn.com.bjjdsy.calc.entity.AccseTimeAttribute;
import cn.com.bjjdsy.calc.entity.AccseWalkTime;
import cn.com.bjjdsy.calc.entity.ODInfo;
import cn.com.bjjdsy.common.CalcConstant;

public class CalcWalkTime {

	public static void main(String[] args) {

	}

	private AccseWalkTime getWalkTime(ODInfo oDInfo, int type) {
		// get dateType
		AccseDateAttribute dateAttr = this.getAccseDateAttribute(oDInfo.getoDDate());
		// get timeAttr
		int timeAttr = this.getTimeAttribute(dateAttr);
		// get walkTime
		AccseWalkTime walkTime = null;
		if (type == 0) {
			walkTime = this.getOWalkTime(oDInfo.getoStationCode(), dateAttr.getDateType(), timeAttr);
		} else if (type == 1) {
			walkTime = this.getDWalkTime(oDInfo.getdStationCode(), dateAttr.getDateType(), timeAttr);
		} else if (type == 2) {
			walkTime = this.getTWalkTime(oDInfo.getoStationCode(), oDInfo.getdStationCode(), dateAttr.getDateType(),
					timeAttr, oDInfo.getFromDirect(), oDInfo.getToDirect());
		}
		return walkTime;
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

	private AccseDateAttribute getAccseDateAttribute(Date oDDate) {
		String key = DateFormatUtils.format(oDDate, "yyyyMMdd");
		return CalcConstant.accseDateAttributeDict.get(key);
	}

	private List<AccseTimeAttribute> findAccseTimeAttribute(String stationCode, int dateType) {
		String key = stationCode + ":" + dateType;
		return CalcConstant.accseTimeAttributeDict.get(key);
	}

	public AccseWalkTime getOWalkTime(String oStationCode, int dateType, int timeAttr) {
		return this.getWalkTime(oStationCode, oStationCode, dateType, timeAttr, 0, toDirect);
	}

	public AccseWalkTime getDWalkTime(String dStationCode, int dateType, int timeAttr) {
		return this.getWalkTime(dStationCode, dStationCode, dateType, timeAttr, fromDirect, 0);
	}

	public AccseWalkTime getTWalkTime(String fromAccStationCode, String toAccStationCode, int dateType, int timeAttr,
			int fromDirect, int toDirect) {

		return this.getWalkTime(fromAccStationCode, toAccStationCode, dateType, timeAttr, fromDirect, toDirect);
	}

	private AccseWalkTime getWalkTime(String fromAccStationCode, String toAccStationCode, int dateType, int timeAttr,
			int fromDirect, int toDirect) {
		String key = fromAccStationCode + ":" + toAccStationCode + ":" + dateType + ":" + timeAttr + ":" + fromDirect
				+ ":" + toDirect;
		return CalcConstant.accseWalkTimeDict.get(key);
	}

}
