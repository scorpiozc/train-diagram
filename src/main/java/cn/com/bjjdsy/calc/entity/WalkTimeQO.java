package cn.com.bjjdsy.calc.entity;

public class WalkTimeQO {

	private String fromAccStationCode;
	private String toAccStationCode;
	private int fromDirect;
	private int toDirect;
	private String cmDate;
	private int positionTime;// minutes

	public String getFromAccStationCode() {
		return fromAccStationCode;
	}

	public void setFromAccStationCode(String fromAccStationCode) {
		this.fromAccStationCode = fromAccStationCode;
	}

	public String getToAccStationCode() {
		return toAccStationCode;
	}

	public void setToAccStationCode(String toAccStationCode) {
		this.toAccStationCode = toAccStationCode;
	}

	public int getFromDirect() {
		return fromDirect;
	}

	public void setFromDirect(int fromDirect) {
		this.fromDirect = fromDirect;
	}

	public int getToDirect() {
		return toDirect;
	}

	public void setToDirect(int toDirect) {
		this.toDirect = toDirect;
	}

	public String getCmDate() {
		return cmDate;
	}

	public void setCmDate(String cmDate) {
		this.cmDate = cmDate;
	}

	public int getPositionTime() {
		return positionTime;
	}

	public void setPositionTime(int positionTime) {
		this.positionTime = positionTime;
	}

}
