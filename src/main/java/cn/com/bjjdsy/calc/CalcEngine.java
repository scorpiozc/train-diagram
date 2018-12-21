package cn.com.bjjdsy.calc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bjjdsy.calc.data.LoadData;
import cn.com.bjjdsy.calc.entity.AccseWalkTime;
import cn.com.bjjdsy.calc.entity.CalcPeriod;
import cn.com.bjjdsy.calc.entity.KPath;
import cn.com.bjjdsy.calc.entity.RunMap;
import cn.com.bjjdsy.calc.entity.RunMapKey;
import cn.com.bjjdsy.calc.entity.SubPath;
import cn.com.bjjdsy.calc.entity.TransferStation;
import cn.com.bjjdsy.calc.entity.WalkTimeQO;
import cn.com.bjjdsy.calc.path.ParsePath;
import cn.com.bjjdsy.calc.walktime.CalcWalkTime;
import cn.com.bjjdsy.common.CalcConstant;
import cn.com.bjjdsy.common.WalkTimeEnum;
import cn.com.bjjdsy.common.util.JsonUtils;
import cn.com.bjjdsy.common.util.LocalDateTimeUtils;
import cn.com.bjjdsy.common.util.Stopwatch;

public class CalcEngine {

	static final Logger logger = LoggerFactory.getLogger(CalcEngine.class);
	final String pattern = "yyyyMMddHHmmss";
	private Map<String, KPath> pathMap;
	private List<String> paths;

	public static void main(String[] args) {
//		try {
//			Date testDate = DateUtils.parseDate("20181201223610", "yyyyMMddHHmmss");
//			testDate.setTime(testDate.getTime() + 120 * 1000);
//			System.out.println(DateFormatUtils.format(testDate, "yyyyMMddHHmmss"));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}20180910063955 20180910064741
		long duration = Duration
				.between(LocalDateTimeUtils.parseStringToDateTime("20180910063955", LocalDateTimeUtils.PATTERN),
						LocalDateTimeUtils.parseStringToDateTime("20180910064741", LocalDateTimeUtils.PATTERN))
				.toMillis() / 1000;
//		logger.info("duration:{}", duration);
		Stopwatch timer = new Stopwatch();
		timer.start();
		CalcEngine calcEngine = new CalcEngine();
		calcEngine.start();
		timer.stop();
		logger.info("calc spend: {} seconds\n", String.format("%f", timer.time()));
		timer.start();
		calcEngine.print();
		timer.stop();
		logger.info("print spend: {} seconds\n", String.format("%f", timer.time()));
		int speed = WalkTimeEnum.QUICK.ordinal();
		speed = WalkTimeEnum.SLOW.ordinal();
//		logger.info("speed:{}", speed);
	}

	public CalcEngine start() {
		new LoadData().load();
		pathMap = new HashMap<>();
		paths = new ArrayList<>();
		CalcConstant.kPathList.forEach(p -> {
			try {
				CalcPeriod calcPeriod = this.getEntryTime(p);
				String positionTime = calcPeriod.getStartTime();
				for (long i = 0; i < calcPeriod.getDuration(); i++) {
					p.setEntryTime(positionTime);
					calc(p);
					positionTime = this.calcPositionTime(DateUtils.parseDate(positionTime, pattern), 60);
				}
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		});
		return this;
	}

	private void calc(KPath path) throws ParseException {
		int speed = WalkTimeEnum.QUICK.ordinal();
		// parse path 113,208,1,1-2,113-114-204-205-206-207-208,856,17.77,114;204
//		KPath path = new KPath();
//		path.setFromStation("113");
//		path.setToStation("208");
//		path.setKsn("1");
//		path.setKpath("113-114-204-205-206-207-208-209-555-557");
//		path.setTransferStation("114;204-209;555");
		List<SubPath> pathList = new ParsePath().parse(path);
		CalcWalkTime calcWalkTime = new CalcWalkTime();
		int i = 1;
		int quick = 0;
		String positionTime = path.getEntryTime();
		WalkTimeQO qo = new WalkTimeQO();
		qo.setCmDate(positionTime.substring(0, 8));
		for (SubPath subPath : pathList) {
			qo.setFromAccStationCode(subPath.getFromStation());
			qo.setToAccStationCode(subPath.getToStation());
			qo.setToDirect(subPath.getDirect());
			qo.setPositionTime(this.timeToSeconds(positionTime.substring(8)));
			// entry time
			if (i == 1) {
				logger.info("entry station time:{}", positionTime);
				AccseWalkTime walkTime = calcWalkTime.getOWalkTime(qo);
				quick = walkTime.getQuick();
				path.setoSpendTime(quick);
				path.addTotalTime(quick);
				logger.info("entry quick walk spend:{}", quick);
			}
			// untransfer
			if (pathList.size() == 1 || i == 1) {
				logger.info("untransfer!");
			}
			// transfer
			else {
				logger.info("transfer!");
				TransferStation transfer = path.getTransferStationMap()
						.get(subPath.getFromStation() + ":" + subPath.getDirect());
				qo.setFromDirect(transfer.getFromDirect());
				qo.setFromTransfer(transfer.getFromStation());
				qo.setToTransfer(transfer.getToStation());
				AccseWalkTime walkTime = calcWalkTime.getTWalkTime(qo);
				quick = walkTime.getQuick();
				path.addTotalTime(quick);
				path.addTSpendTime(quick);
				subPath.setTwalktime(quick);
				logger.info("transfer quick walk spend:{}", quick);
			}

			// sub path time
			String quickDeptTime = this.calcPositionTime(DateUtils.parseDate(positionTime, pattern), quick);
			logger.info("cur position time:{}", quickDeptTime);
			// find the trip
			RunMapKey next = this.getRunMapKey(subPath, quickDeptTime);
			if (next == null) {
				logger.warn("no runmap info exit!");
				break;
			}
			logger.info("next deptime:{} tripno:{}", next.getDepTime(), next.getTripNo());
			positionTime = this.getNextPositionTime(path, subPath, next);
			logger.info("next position time:{}", positionTime);
			qo.setPositionTime(this.timeToSeconds(positionTime));

			if (i == pathList.size()) {
//out time
				AccseWalkTime walkTime = calcWalkTime.getDWalkTime(qo);
				quick = walkTime.getQuick();
				path.setdSpendTime(quick);
				path.addTotalTime(quick);
				logger.info("out quick walk spend:{}", quick);
				String outTime = this.calcPositionTime(DateUtils.parseDate(positionTime, pattern), quick);
				path.setOutTime(outTime);
				logger.info("out station time:{}", outTime);
			}

			// ever sub path time

// get TRIP_NO

			// calc travel_time
			i++;
		}
		pathMap.put(path.getKsn(), path);
		paths.add(JsonUtils.writeObject(path));
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
//		logger.info("time to seconds:{}",
//				Integer.parseInt(hh) * 3600 + Integer.parseInt(mm) * 60 + Integer.parseInt(ss));
		return Integer.parseInt(hh) * 3600 + Integer.parseInt(mm) * 60 + Integer.parseInt(ss);
	}

	private RunMapKey getRunMapKey(SubPath subPath, String quickDeptTime) {
		String key = subPath.getFromStation() + ":" + subPath.getDirect();
		logger.info("key:{}", key);
		List<RunMapKey> runMapKeyList = CalcConstant.runMapKeyMap.get(key);
		// sort deptime
		RunMapKey tmp = new RunMapKey();
		tmp.setDepTime(quickDeptTime);
		runMapKeyList.add(tmp);
		Collections.sort(runMapKeyList);
		runMapKeyList.forEach(k -> {
//			logger.warn("deptime:{} tripno:{}", k.getDepTime(), k.getTripNo());
		});
		int idx = runMapKeyList.indexOf(tmp);

		// no trip info
		if (idx == runMapKeyList.size() - 1) {
			runMapKeyList.remove(idx);
			return null;
		}
		RunMapKey next = runMapKeyList.get(idx + 1);
		runMapKeyList.remove(idx);
		return next;
	}

	private String getNextPositionTime(KPath path, SubPath subPath, RunMapKey next) {
		Map<String, RunMap> runMaps = CalcConstant.runMapMap.get(next.getLineNo() + ":" + next.getTripNo());
//		runMaps.forEach((k, v) -> {
		// System.out.println(k);
//		});
		// get path runtime
		if (runMaps == null) {
			System.out.println(next);
		}
		RunMap startStation = runMaps.get(subPath.getFromStation());
		RunMap stopStation = runMaps.get(subPath.getToStation());
		long duration = Duration.between(LocalDateTimeUtils.parseStringToDateTime(startStation.getDepTime(), pattern),
				LocalDateTimeUtils.parseStringToDateTime(stopStation.getArrTime(), pattern)).toMillis() / 1000;
		logger.info("{} start:{} {} stop:{} spend:{}", startStation.getPltfId(), startStation.getDepTime(),
				stopStation.getPltfId(), stopStation.getArrTime(), duration);

		int runtime = this.timeToSeconds(stopStation.getArrTime().substring(8))
				- this.timeToSeconds(startStation.getDepTime().substring(8));
		path.addTotalTime(runtime);
		path.addRuntime(runtime);
		subPath.setRuntime(runtime);
		subPath.setTripNo(next.getTripNo());
		return stopStation.getArrTime();
	}

	private CalcPeriod getEntryTime(KPath path) throws ParseException {
		List<String> oDeptimeList = CalcConstant.deptimeMap.get(path.getFromStation());
		Collections.sort(oDeptimeList);
//		String startTime = oDeptimeList.get(0);

		List<String> dDeptimeList = CalcConstant.deptimeMap.get(path.getToStation());
		Collections.sort(dDeptimeList);
//		String stopTime = dDeptimeList.get(dDeptimeList.size() - 1);

		String startTime = this.calcPositionTime(DateUtils.parseDate(oDeptimeList.get(0), pattern), -60);
		String stopTime = this.calcPositionTime(DateUtils.parseDate(oDeptimeList.get(dDeptimeList.size() - 1), pattern),
				-60);

		long duration = Duration.between(LocalDateTimeUtils.parseStringToDateTime(startTime, pattern),
				LocalDateTimeUtils.parseStringToDateTime(stopTime, pattern)).toMinutes();
		System.out.println("duration:" + duration);
//		path.setEntryTime(entryTime);
//		System.out.println(path.getFromStation() + ":" + deptimeList.get(0) + ":" + entryTime);
		CalcPeriod calcPeriod = new CalcPeriod();
		calcPeriod.setStartTime(startTime);
		calcPeriod.setStopTime(stopTime);
		calcPeriod.setDuration(duration);
		return calcPeriod;
	}

	private void print() {
//		pathMap.forEach((k, v) -> {
//			logger.info("{},{},{},{},{},{},{},{}", v.getFromStation(), v.getToStation(), v.getKpath(), v.getEntryTime(),
//					v.getoSpendTime(), v.getOutTime(), v.getdSpendTime(), v.getTotalTime());
//			logger.info("{}", JsonUtils.writeObject(v));

//		});
		File file = new File("forward.txt");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			IOUtils.writeLines(paths, null, fos);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
