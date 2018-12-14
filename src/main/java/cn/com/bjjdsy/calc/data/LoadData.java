package cn.com.bjjdsy.calc.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bjjdsy.common.CalcConstant;
import cn.com.bjjdsy.common.util.Stopwatch;

public class LoadData {

	static final Logger logger = LoggerFactory.getLogger(LoadData.class);

	public static void main(String[] args) {
		new LoadData().load();
	}

	public void load() {
		Stopwatch timer = new Stopwatch();
		timer.start();
		this.loadDateTypeMap();
		this.loadAccseDateAttribute();
		this.loadAccseTimeAttribute();
		this.loadAccseWalkTime();
		this.loadKPath();
		this.loadRunMap();
//		CalcConstant.runMapKeyMap.forEach((key, value) -> {
//
//			logger.info("key:{}", key);
//			value.forEach(k -> {
//				logger.info("depTime:{}", k.getDepTime());
//			});
//		});
		timer.stop();
		logger.info("load data from file: {} seconds\n", timer.time());
	}

	private void loadDateTypeMap() {
		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				CalcConstant.dateTypeMap.put(i + ":" + j, k);
				k++;
			}
		}
//		CalcConstant.dateTypeMap.forEach((key, value) -> {
//			System.out.println(key + ":" + value);
//		});
	}

	private void loadAccseDateAttribute() {
		ReadDataFile rdf = new ReadAccseDateAttributeFile();
		rdf.read(CalcConstant.ACCSE_DATE_ATTRIBUTE);
	}

	private void loadAccseTimeAttribute() {
		ReadDataFile rdf = new ReadAccseTimeAttributeFile();
		rdf.read(CalcConstant.ACCSE_TIME_ATTRIBUTE);
	}

	private void loadAccseWalkTime() {
		ReadDataFile rdf = new ReadAccseWalkTimeFile();
		rdf.read(CalcConstant.ACCSE_WALK_TIME);
	}

	private void loadKPath() {
		ReadDataFile rdf = new ReadKPathFile();
		rdf.read(CalcConstant.K_PATH);
	}

	private void loadRunMap() {
		ReadDataFile rdf = new ReadRunMapFile();
		rdf.read(CalcConstant.PLAN_RUN_MAP);
//		CalcConstant.runMapMap.forEach((key, value) -> {
//			value.forEach((k, v) -> {
//				System.out.println(key + ":" + k + ":" + v.getDepTime());
//			});
//		});
	}
}
