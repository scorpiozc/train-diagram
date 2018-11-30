package cn.com.bjjdsy.calc.data;

import cn.com.bjjdsy.common.CalcConstant;

public class LoadData {

	public void load() {
		this.loadDateTypeMap();
	}

	private void loadDateTypeMap() {
		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				CalcConstant.dateTypeMap.put(i + ":" + j, k);
				k++;
			}
		}
		CalcConstant.dateTypeMap.forEach((key, value) -> {
			System.out.println(key + ":" + value);
		});
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
}
