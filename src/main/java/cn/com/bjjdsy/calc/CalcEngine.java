package cn.com.bjjdsy.calc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bjjdsy.calc.entity.KPath;
import cn.com.bjjdsy.calc.entity.ODInfo;
import cn.com.bjjdsy.calc.entity.WalkTimeQO;
import cn.com.bjjdsy.calc.path.ParsePath;
import cn.com.bjjdsy.calc.walktime.CalcWalkTime;

public class CalcEngine {

	static final Logger logger = LoggerFactory.getLogger(CalcEngine.class);

	public static void main(String[] args) {

	}

	private void calc() {
		// parse path
		KPath path = new KPath();
		path.setKpath("125-124-123-122-121-120-211-212-213-214-215-216-217");
		path.setTransferStation("120;211");
		List<KPath> pathList = new ParsePath().parse(path);
		ODInfo od = new ODInfo();
		for (KPath p : pathList) {
			WalkTimeQO qo = new WalkTimeQO();
			// get walk time
			od.setToDirect(Integer.parseInt(p.getDirect()));
			new CalcWalkTime().getOWalkTime(qo.getCmDate(), qo.getFromAccStationCode(), qo.getToDirect());

// get TRIP_NO

			// calc travel_time
		}
	}

}
