package cn.com.bjjdsy.calc.entity;

public class RunMapKey implements Comparable<RunMapKey> {

	private int depTime;

	public int getDepTime() {
		return depTime;
	}

	public void setDepTime(int depTime) {
		this.depTime = depTime;
	}

	public String getTripNo() {
		return tripNo;
	}

	public void setTripNo(String tripNo) {
		this.tripNo = tripNo;
	}

	private String tripNo;

	@Override
	public int compareTo(RunMapKey o) {
		return this.getDepTime() - o.getDepTime();
	}

}
