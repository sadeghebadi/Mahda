package ir.rayacell.mahda.model;

import apl.vada.lib.db.annotations.Column;
import apl.vada.lib.db.util.ColumnType;

public class deviceInfo {
	@Column(type = ColumnType.INTEGER, isPrimaryKey = true , isAutoincrement = true)
	public int _id;
	@Column(type = ColumnType.INTEGER)
	public int PROGRESS3G;
	@Column(type = ColumnType.INTEGER)
	public int PROGRESSCONNECTION;
	@Column(type = ColumnType.INTEGER)
	public int PROGRESSDIRECT;
	@Column(type = ColumnType.TEXT)
	public String phonenumber;
	
	public deviceInfo(){
		
	}
	
	public deviceInfo(int pROGRESS3G, int pROGRESSCONNECTION,
			int pROGRESSDIRECT, String phonenumber) {
		super();
		PROGRESS3G = pROGRESS3G;
		PROGRESSCONNECTION = pROGRESSCONNECTION;
		PROGRESSDIRECT = pROGRESSDIRECT;
		this.phonenumber = phonenumber;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getPROGRESS3G() {
		return PROGRESS3G;
	}

	public void setPROGRESS3G(int pROGRESS3G) {
		PROGRESS3G = pROGRESS3G;
	}

	public int getPROGRESSCONNECTION() {
		return PROGRESSCONNECTION;
	}

	public void setPROGRESSCONNECTION(int pROGRESSCONNECTION) {
		PROGRESSCONNECTION = pROGRESSCONNECTION;
	}

	public int getPROGRESSDIRECT() {
		return PROGRESSDIRECT;
	}

	public void setPROGRESSDIRECT(int pROGRESSDIRECT) {
		PROGRESSDIRECT = pROGRESSDIRECT;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
}
