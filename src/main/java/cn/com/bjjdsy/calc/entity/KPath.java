package cn.com.bjjdsy.calc.entity;

public class KPath {

	private String fromStation;
	private String toStation;
	private String ksn;
	private String kpath;
	private String transferStation;
	private String direct;

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public String getFromStation() {
		return fromStation;
	}

	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}

	public String getToStation() {
		return toStation;
	}

	public void setToStation(String toStation) {
		this.toStation = toStation;
	}

	public String getKsn() {
		return ksn;
	}

	public void setKsn(String ksn) {
		this.ksn = ksn;
	}

	public String getKpath() {
		return kpath;
	}

	public void setKpath(String kpath) {
		this.kpath = kpath;
	}

	public String getTransferStation() {
		return transferStation;
	}

	public void setTransferStation(String transferStation) {
		this.transferStation = transferStation;
	}

}
