package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.TrackMap;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.util.CalendarTool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class FileManagerFragment extends Fragment {

	private View v;
	private ListView mListView;
	public static final String DEFAULT_STORAGE_LOCATION = App.getContext()
			.getResources().getString(R.string.default_location);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_file, container, false);
		setUpInnerViewElements(v);
		updateView(v);
		return v;
	}

	private void setUpInnerViewElements(View v) {
		mListView = (ListView) v.findViewById(R.id.lv_filesm_list);
	}

	List<File> myfileslist;

	public void updateView(View v) {
		mListView = (ListView) v.findViewById(R.id.lv_filesm_list);
		// progressBar = (ProgressBar) getActivity().findViewById(
		// R.id.pb_in_download_fragment);
		File f = new File(DEFAULT_STORAGE_LOCATION + "/"
				+ NetworkManager.clientPhoneNumber);
		if (!f.exists()) {
			f.mkdir();
		}
		if (f.listFiles() == null) {
			return;
		}
		myfileslist = new ArrayList<File>(Arrays.asList(f.listFiles()));
		Collections.reverse(myfileslist);
		mListView.setDivider(null);
		mListView.setDividerHeight(5);
		FilesListAdapter adapter = new FilesListAdapter(App.getContext(),
				myfileslist);
		mListView.setAdapter(adapter);
		mListView.setVisibility(View.VISIBLE);
	}

	public class FilesListAdapter extends ArrayAdapter<File> {

		private List<File> mFiles;
		private Context mcontext;

		public FilesListAdapter(Context context, List<File> files) {
			super(context, android.R.layout.simple_list_item_1, files);

			this.mFiles = files;
			this.mcontext = context;
		}

		private String convertToPersian(String date) {

			String[] gDate = date.split("_");
			int year = Integer.parseInt(gDate[2]);
			int month = Integer.parseInt(gDate[3]);
			int day = Integer.parseInt(gDate[4]);
			CalendarTool ct = new CalendarTool(year, month, day);

			String returnStri = gDate[0] + "_" + gDate[1] + "_"
					+ ct.getIranianDate().replace("/", "_") + "_" + gDate[5]
					+ "_" + gDate[6];
			for (int i = 7; i <= gDate.length - 1; i++) {
				returnStri += "_" + gDate[i];
			}
			return returnStri;
		}

		@Override
		public long getItemId(final int position) {
			return getItem(position).hashCode();
		}
		public File mfile;
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) App.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.download_list_file_item,
					parent, false);
			final TextView tv_filename = (TextView) rowView
					.findViewById(R.id.tv_file_name);
			TextView tv_filesize = (TextView) rowView
					.findViewById(R.id.tv_file_size);

			 mfile = mFiles.get(position);
			String name = convertToPersian(mfile.getName());
			tv_filename.setText(name);
			tv_filesize.setText(mfile.length() / 1000 + " KB");

			ImageButton btn_delete = (ImageButton) rowView
					.findViewById(R.id.btn_delete_file);
			btn_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mFiles.get(position).delete();
					mFiles.remove(position);
					notifyDataSetChanged();
				}
				// }
			});
			ImageButton btn_donwload = (ImageButton) rowView
					.findViewById(R.id.btn_folder);
			setBackgroundDrawable(btn_donwload ,myfileslist.get(position).getName());
			btn_donwload.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					if (myfileslist.get(position).getName().endsWith(".txt")) {
						Intent intent = new Intent(getActivity(), TrackMap.class);
						intent.putExtra("filePath", myfileslist.get(position).getAbsolutePath());
						getActivity().startActivity(intent);
					} else {

						// BaseModel
						File file = new File(Environment
								.getExternalStorageDirectory()
								+ "/MAHDA/"
								+ NetworkManager.clientPhoneNumber
								+ "/"
								+ myfileslist.get(position).getName());
						// String mime =
						// MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getName().substring(file.getName().lastIndexOf("."),file.getName().length()).toUpperCase());
						// String mime =
						// MimeTypeMap.getSingleton().getMimeTypeFromExtension(".JPG");
						MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
						String mime = "*/*";
						Uri uri = Uri.fromFile(file);
						if (mimeTypeMap.hasExtension(MimeTypeMap
								.getFileExtensionFromUrl(uri.toString())))
							mime = mimeTypeMap
									.getMimeTypeFromExtension(MimeTypeMap
											.getFileExtensionFromUrl(uri
													.toString()));
						Intent intent = new Intent(
								android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(uri, mime);
						startActivity(intent);

					}
				}
			});

			btn_donwload.setVisibility(View.VISIBLE);

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
