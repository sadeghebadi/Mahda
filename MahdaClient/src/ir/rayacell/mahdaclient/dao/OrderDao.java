package ir.rayacell.mahdaclient.dao;

import ir.rayacell.mahdaclient.model.Orders;
import android.content.Context;
import apl.vada.lib.db.SQLiteDAO;

public class OrderDao extends SQLiteDAO<Orders>  {
	public OrderDao( Context context) {
		super(Orders.class, context,"mahda.sqlite");
	}
}
