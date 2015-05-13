package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.BaseModel;
import ir.rayacell.mahda.model.DeleteDownloadModel;
import ir.rayacell.mahda.model.FileModel;
import ir.rayacell.mahda.model.FileModel.MyFile;
import ir.rayacell.mahda.util.CalendarTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadFragment extends Fragment {

	private View v;
	private ListView mListView;
	private ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_download, container, false);
		setUpInnerViewElements();
		return v;
	}

	private void setUpInnerViewElements() {
//		mListView = (ListView) getActivity().findViewById(R.id.lv_files_list);
//
//		progressBar = (ProgressBar) getActivity().findViewById(
//				R.id.pb_in_download_fragment);

		Button btn_fileslist = (Button) v.findViewById(R.id.btn_get_files_list);
		btn_fileslist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BaseModel model = new BaseModel(0,
						NetworkManager.clientPhoneNumber, App.getContext()
								.getResources()
								.getString(R.string.command_get_file_list));
				Manager.getFileList(model, DownloadFragment.this);
			}
		});
	}

	public void updateView(BaseModel model) {
		mListView = (ListView) getActivity().findViewById(R.id.lv_files_list);
		progressBar = (ProgressBar) getActivity().findViewById(
				R.id.pb_in_download_fragment);
		FileModel mModel = (FileModel) model;
		List<MyFile> myfileslist = (mModel.getmFilesList());
	    Collections.reverse(myfileslist);
		 mListView.setDivider(null);
		 mListView.setDividerHeight(5);
		FilesListAdapter adapter = new FilesListAdapter(App.getContext(),
				myfileslist);
		mListView.setAdapter(adapter);
		mListView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		// v.invalidate();
	}

	public class FilesListAdapter extends ArrayAdapter<MyFile> {

		private List<MyFile> mFiles;
		private Context mcontext;

		public FilesListAdapter(Context context, List<MyFile> files) {
			super(context, android.R.layout.simple_list_item_1, files);

			this.mFiles = files;
			this.mcontext = context;
		}

		@Override
		public long getItemId(final int position) {
			return getItem(position).hashCode();
		}
		private String convertToPersian(String date){
			
			String[] gDate = date.split("_");
			int year = Integer.parseInt(gDate[2]);
			int month = Integer.parseInt(gDate[3]);
			int day = Integer.parseInt(gDate[4]);
			CalendarTool ct = new CalendarTool(year,month,day);
			
			String returnStri = gDate[0]+"_"+gDate[1]+"_"+ct.getIranianDate().replace("/", "_")+"_"+gDate[5]+"_"+gDate[6];
			for(int i = 7 ; i <= gDate.length-1; i++){
				returnStri +="_"+gDate[i];
			}
			return returnStri;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) App.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.download_list_item,
					parent, false);
			final TextView tv_filename = (TextView) rowView
					.findViewById(R.id.tv_file_name);
			TextView tv_filesize = (TextView) rowView
					.findViewById(R.id.tv_file_size);

			final MyFile mfile = mFiles.get(position);
			String name = convertToPersian(mfile.getName());
			tv_filename.setText(name);
			tv_filesize.setText(mfile.getSize());
			
			ImageButton btn_delete = (ImageButton)rowView.findViewById(R.id.btn_delete_file);
			btn_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					BaseModel model = new DeleteDownloadModel(0,
							NetworkManager.clientPhoneNumber, App.getContext()
									.getResources()
									.getString(R.string.command_delete_file), mfile.getName().toString());
					Manager.deleteFile(model, DownloadFragment.this);
				}
			});
			ImageButton btn_donwload = (ImageButton)rowView.findViewById(R.id.btn_download_file);
			btn_donwload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					BaseModel model = new DeleteDownloadModel(0,
							NetworkManager.clientPhoneNumber, App.getContext()
									.getResources()
									.getString(R.string.command_download_file), mfile.getName().toString());
					Manager.downloadFile(model, DownloadFragment.this);					
				}
			});
			ImageButton btn_file = (ImageButton)rowView.findViewById(R.id.btn_folder);
			setBackgroundDrawable(btn_file, name);
			
			return rowView;
		}
		private void setBackgroundDrawable(ImageButton btn_donwload, String name) {
			if(name.endsWith(".jpg")){
				btn_donwload.setBackground(Container.BASEACTIVITY.getResources().getDrawable(R.drawable.pic));
			}else if(name.endsWith(".txt")){
				btn_donwload.setBackground(Container.BASEACTIVITY.getResources().getDrawable(R.drawable.track));

			}else if(name.endsWith(".3gp")){
				btn_donwload.setBackground(Container.BASEACTIVITY.getResources().getDrawable(R.drawable.seda));

			}else if(name.endsWith(".mp4")){
				btn_donwload.setBackground(Container.BASEACTIVITY.getResources().getDrawable(R.drawable.play_fiml));

			}else if(name.endsWith(".3gpp")){
				btn_donwload.setBackground(Container.BASEACTIVITY.getResources().getDrawable(R.drawable.tel));

			}
		}
	}

}
