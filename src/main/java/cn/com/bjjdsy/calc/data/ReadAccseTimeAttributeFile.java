package cn.com.bjjdsy.calc.data;

import java.util.ArrayList;

import cn.com.bjjdsy.calc.entity.AccseTimeAttribute;
import cn.com.bjjdsy.common.CalcConstant;

public class ReadAccseTimeAttributeFile extends ReadDataFile {

	@Override
	public void parseData(String[] data) {
		AccseTimeAttribute timeAttr = new AccseTimeAttribute();
		timeAttr.setAccseStationCode(data[0]);
		timeAttr.setDateType(Integer.parseInt(data[1]));
		timeAttr.setBeginTime(Integer.parseInt(data[2]));
		timeAttr.setTimeAttribute(Integer.parseInt(data[3]));
		String key = timeAttr.getAccseStationCode() + ":" + timeAttr.getDateType();
		if (CalcConstant.accseTimeAttributeDict.get(key) == null) {
			CalcConstant.accseTimeAttributeDict.put(key, new ArrayList<AccseTimeAttribute>());
		}
		CalcConstant.accseTimeAttributeDict.get(key).add(timeAttr);

	}

}
