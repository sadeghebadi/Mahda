package ir.rayacell.mahda.adapter;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.model.FileModel;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DownloadListAdapter extends ArrayAdapter<FileModel> {

	private List<FileModel> mFiles;

	public DownloadListAdapter(Context context, List<FileModel> files) {
		super(context, android.R.layout.simple_list_item_1, files);
		this.mFiles = files;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) App.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.download_list_item, parent);

		return rowView;
	}
}
