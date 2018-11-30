package cn.com.bjjdsy.calc.entity;

import java.util.Date;

public class ODInfo {

	private String oStationCode;
	private String dStationCode;
	private Date oDDate;
	private int fromDirect;
	private int toDirect;

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

	public String getoStationCode() {
		return oStationCode;
	}

	public void setoStationCode(String oStationCode) {
		this.oStationCode = oStationCode;
	}

	public String getdStationCode() {
		return dStationCode;
	}

	public void setdStationCode(String dStationCode) {
		this.dStationCode = dStationCode;
	}

	public Date getoDDate() {
		return oDDate;
	}

	public void setoDDate(Date oDDate) {
		this.oDDate = oDDate;
	}
}
