package ir.rayacell.mahda.adapter;


import ir.rayacell.mahda.R;
import ir.rayacell.mahda.model.device;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerItemCustomAdapter extends ArrayAdapter<device> {
	 
    Context mContext;
    int layoutResourceId;
    ArrayList<device> data = null;
 
    
    
    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ArrayList<device> data) {
 
        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }
 
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        View listItem = convertView;
 
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
 
        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.im_device_image);
        TextView textViewName = (TextView) listItem.findViewById(R.id.tv_device_name);
        
        device folder = data.get(position);
        textViewName.setText(folder.name);
        
        return listItem;
    }
 
}