package cn.com.bjjdsy.calc;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bjjdsy.calc.data.LoadData;
import cn.com.bjjdsy.calc.entity.AccseWalkTime;
import cn.com.bjjdsy.calc.entity.KPath;
import cn.com.bjjdsy.calc.entity.ODInfo;
import cn.com.bjjdsy.calc.entity.RunMap;
import cn.com.bjjdsy.calc.entity.RunMapKey;
import cn.com.bjjdsy.calc.entity.TransferStation;
import cn.com.bjjdsy.calc.entity.WalkTimeQO;
import cn.com.bjjdsy.calc.path.ParsePath;
import cn.com.bjjdsy.calc.walktime.CalcWalkTime;
import cn.com.bjjdsy.common.CalcConstant;

public class CalcEngine {

	static final Logger logger = LoggerFactory.getLogger(CalcEngine.class);
	final String pattern = "yyyyMMddHHmmss";

	public static void main(String[] args) {
//		try {
//			Date testDate = DateUtils.parseDate("20181201223610", "yyyyMMddHHmmss");
//			testDate.setTime(testDate.getTime() + 120 * 1000);
//			System.out.println(DateFormatUtils.format(testDate, "yyyyMMddHHmmss"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		new CalcEngine().timeToMinutes("063000");
	}

	public void start() {
		try {
			new LoadData().load();
			calc();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void calc() throws ParseException {
		// parse path 113,208,1,1-2,113-114-204-205-206-207-208,856,17.77,114;204
		KPath path = new KPath();
		path.setFromStation("113");
		path.setToStation("208");
		path.setKsn("1");
		path.setKpath("113-114-204-205-206-207-208");
		path.setTransferStation("114;204");
		List<KPath> pathList = new ParsePath().parse(path);
		ODInfo od = new ODInfo();
//		od.setoDDate(DateUtils.parseDate("20180910063000", "yyyyMMddHHmmss"));
		od.setEntryTime("20180910063000");
		CalcWalkTime calcWalkTime = new CalcWalkTime();
		int i = 1;
		int quick = 0;
		String positionTime = od.getEntryTime().substring(8);
		WalkTimeQO qo = new WalkTimeQO();
		qo.setCmDate("20180910");
		for (KPath subPath : pathList) {
			qo.setFromAccStationCode(subPath.getFromStation());
			qo.setToAccStationCode(subPath.getToStation());
			qo.setToDirect(subPath.getDirect());
			qo.setPositionTime(this.timeToSeconds(positionTime));
			// entry time
			if (i == 1) {
				AccseWalkTime walkTime = calcWalkTime.getOWalkTime(qo);
				quick = walkTime.getQuick();
				path.setoTime(quick);
				path.addTime(quick);

			}
			// untransfer
			if (pathList.size() == 1 || i == 1) {
				logger.info("untransfer!");
			}
			// transfer
			else {
				TransferStation transfer = subPath.getTransferStationMap()
						.get(subPath.getFromStation() + ":" + subPath.getDirect());
				qo.setFromDirect(transfer.getFromDirect());
				AccseWalkTime walkTime = calcWalkTime.getTWalkTime(qo);
				quick = walkTime.getQuick();
			}

			// sub path time
			String quickDeptTime = this.calcPositionTime(DateUtils.parseDate(positionTime, pattern), quick);
			logger.info("quickDeptTime:" + quickDeptTime);
			// find the trip
			RunMapKey next = this.getRunMapKey(path, quickDeptTime);
			if (next == null) {
				break;
			}
			logger.info(next.getDepTime() + ":" + next.getTripNo());
			positionTime = this.getNextPositionTime(path, subPath, next);
			qo.setPositionTime(this.timeToSeconds(positionTime));

			if (i == pathList.size()) {
//out time
				AccseWalkTime walkTime = calcWalkTime.getDWalkTime(qo);
				quick = walkTime.getQuick();
				path.setdTime(quick);
				path.addTime(quick);
			}

			// ever sub path time

// get TRIP_NO

			// calc travel_time
			i++;
		}

	}

	private String calcPositionTime(Date src, int increase) {
		Date positionTime = new Date(src.getTime() + increase * 1000);
		return DateFormatUtils.format(positionTime, pattern);
	}

	private int timeToMinutes(String time) {
		String hh = time.substring(0, 2);
		String mm = time.substring(2, 4);
		System.out.println(Integer.parseInt(hh) * 60 + Integer.parseInt(mm));
		return Integer.parseInt(hh) * 60 + Integer.parseInt(mm);
	}

	private int timeToSeconds(String time) {
		String hh = time.substring(0, 2);
		String mm = time.substring(2, 4);
		String ss = time.substring(4, 6);
		System.out.println(Integer.parseInt(hh) * 3600 + Integer.parseInt(mm) * 60 + Integer.parseInt(ss));
		return Integer.parseInt(hh) * 3600 + Integer.parseInt(mm) * 60 + Integer.parseInt(ss);
	}

	private RunMapKey getRunMapKey(KPath subPath, String quickDeptTime) {
		String key = subPath.getFromStation() + ":" + subPath.getDirect();
		List<RunMapKey> runMapKeyList = CalcConstant.runMapKeyMap.get(key);
		// sort deptime
		RunMapKey tmp = new RunMapKey();
		tmp.setDepTime(quickDeptTime);
		runMapKeyList.add(tmp);
		Collections.sort(runMapKeyList);
		runMapKeyList.forEach(k -> {
			logger.info(k.getDepTime());
		});
		int idx = runMapKeyList.indexOf(tmp);

		// no trip info
		if (idx == runMapKeyList.size() - 1) {
			return null;
		}
		RunMapKey next = runMapKeyList.get(idx + 1);
		return next;
	}

	private String getNextPositionTime(KPath path, KPath subPath, RunMapKey next) {
		Map<String, RunMap> runMaps = CalcConstant.runMapMap.get(next.getLineNo() + ":" + next.getTripNo());
		// get path runtime
		RunMap startStation = runMaps.get(subPath.getFromStation());
		RunMap stopStation = runMaps.get(subPath.getToStation());
		logger.info("{} start {}:{} stop {}", startStation.getPltfId(), startStation.getDepTime(),
				stopStation.getPltfId(), stopStation.getArrTime());
		int runtime = this.timeToSeconds(stopStation.getArrTime().substring(8))
				- this.timeToSeconds(startStation.getDepTime().substring(8));
		path.addTime(runtime);
		subPath.setTime(runtime);
		return stopStation.getArrTime();
	}

}
