package cn.com.bjjdsy.calc.data;

import cn.com.bjjdsy.calc.entity.AccseDateAttribute;
import cn.com.bjjdsy.common.CalcConstant;

public class ReadAccseDateAttributeFile extends ReadDataFile {

	@Override
	public void parseData(String[] data) {
		AccseDateAttribute dateAttr = new AccseDateAttribute();
		dateAttr.setCmDate(data[0]);
		dateAttr.setSection(Integer.parseInt(data[1]));
		dateAttr.setDateType(Integer.parseInt(data[2]));
		dateAttr.setVersion(data[3]);
		CalcConstant.accseDateAttributeDict.put(dateAttr.getCmDate(), dateAttr);
	}

}
