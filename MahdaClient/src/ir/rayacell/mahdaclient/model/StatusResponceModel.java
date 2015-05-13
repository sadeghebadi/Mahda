package ir.rayacell.mahdaclient.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class StatusResponceModel extends BaseModel {

	@Column(type = ColumnType.TEXT)
	private String IMEI;

	@Column(type = ColumnType.TEXT)
	private String serial_number;

	@Column(type = ColumnType.TEXT)
	private String latitude;

	@Column(type = ColumnType.TEXT)
	private String longitude;

	@Column(type = ColumnType.TEXT)
	private String battery_level;

	@Column(type = ColumnType.TEXT)
	private String memory_total;

	@Column(type = ColumnType.TEXT)
	private String memory_left;

	@Column(type = ColumnType.INTEGER)
	private int wifi_state;

	@Column(type = ColumnType.TEXT)
	private String IPaddress;

	public StatusResponceModel(long commandid, String phonenumber,
			String commandtype, String iMEI, String serial_number,
			String latitude, String longitude, String battery_level,
			String memory_total, String memory_left, int wifi_state,
			String iPaddress) {
		super(commandid, phonenumber, commandtype);
		this.IMEI = iMEI;
		this.serial_number = serial_number;
		this.latitude = latitude;
		this.longitude = longitude;
		this.battery_level = battery_level;
		this.memory_total = memory_total;
		this.memory_left = memory_left;
		this.wifi_state = wifi_state;
		this.IPaddress = iPaddress;
	}

	public String getIMEI() {
		return IMEI;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getBattery_level() {
		return battery_level;
	}

	public String getMemory_total() {
		return memory_total;
	}

	public String getMemory_left() {
		return memory_left;
	}

	public int getWifi_state() {
		return wifi_state;
	}

	public String getIPaddress() {
		return IPaddress;
	}
}