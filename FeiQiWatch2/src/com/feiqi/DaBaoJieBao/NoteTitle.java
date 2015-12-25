package com.feiqi.DaBaoJieBao;

public class NoteTitle {

	private String name;// 人名
	private String phoneNumber;// 電話號碼
	private String smsbody; // 主体短信内容
	private String date; // 时间
	private String type; // 判断短信是发送的，还是接收的

	//二维码格式key
	private String bluetoolthName;
	private String bluetoolthAddress;
	private String MY_UUID;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSmsbody() {
		return smsbody;
	}

	public void setSmsbody(String smsbody) {
		this.smsbody = smsbody;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBluetoolthName() {
		return bluetoolthName;
	}

	public void setBluetoolthName(String bluetoolthName) {
		this.bluetoolthName = bluetoolthName;
	}

	public String getBluetoolthAddress() {
		return bluetoolthAddress;
	}

	public void setBluetoolthAddress(String bluetoolthAddress) {
		this.bluetoolthAddress = bluetoolthAddress;
	}
	public String getMY_UUID() {
		return MY_UUID;
	}

	public void setMY_UUID(String mY_UUID) {
		MY_UUID = mY_UUID;
	}
}
