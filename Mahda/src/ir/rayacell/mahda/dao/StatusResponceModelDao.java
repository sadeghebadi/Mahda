package ir.rayacell.mahda.dao;

import ir.rayacell.mahda.model.StatusResponceModel;
import ir.rayacell.mahda.model.device;
import android.content.Context;
import apl.vada.lib.db.SQLiteDAO;

public class StatusResponceModelDao  extends SQLiteDAO<StatusResponceModel>{

	public StatusResponceModelDao( Context context) {
		super(StatusResponceModel.class, context,"mahda.sqlite");
	}
}
