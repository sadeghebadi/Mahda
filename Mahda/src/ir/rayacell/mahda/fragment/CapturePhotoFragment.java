package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.DateTimeManager;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.Command;
import ir.rayacell.mahda.util.Util;
import ir.smartlab.persindatepicker.PersianDatePicker;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class CapturePhotoFragment extends Fragment {

	private View v;
	private EditText et_photo_count;
	private EditText et_photo_delay;
	private TimePicker tp_start;
	private PersianDatePicker dp_start;
	private Button btn_send_command;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_capture_photo, container, false);
		setUpInnerViewElements();
		return v;
	} 

	private void setUpInnerViewElements() {
		et_photo_count = (EditText) v.findViewById(R.id.et_capture_photo_count);
		et_photo_delay = (EditText) v
				.findViewById(R.id.et_capture_photo_interarrival);

		tp_start = (TimePicker) v.findViewById(R.id.tp_capture_photo);
		dp_start = (PersianDatePicker) v.findViewById(R.id.dp_capture_photo);
		Util.set_timepicker_text_colour(tp_start, getActivity());
		btn_send_command = (Button) v.findViewById(R.id.btn_send_command_photo);
		btn_send_command.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Util.checkEmpty(getActivity(), new EditText[]{et_photo_count,et_photo_delay}))
				{
					return;
				}
				Command command = new Command(0,
						NetworkManager.clientPhoneNumber, App.getContext()
								.getResources()
								.getString(R.string.command_capture_photo),
						new DateTimeManager().getDateTime(dp_start, tp_start),
						0,
						Integer.parseInt(et_photo_count.getText().toString()),
						Integer.parseInt(et_photo_delay.getText().toString()));
				Manager.capturePhoto(command);
			}
		});
	}

}
