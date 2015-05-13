package ir.rayacell.mahda.dao;

import ir.rayacell.mahda.model.Orders;
import android.content.Context;
import apl.vada.lib.db.SQLiteDAO;

public class OrderDao extends SQLiteDAO<Orders>  {
	public OrderDao( Context context) {
		super(Orders.class, context,"mahda.sqlite");
	}
}
