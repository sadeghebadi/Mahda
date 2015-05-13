package ir.rayacell.mahda.fragment;

import ir.rayacell.mahda.App;
import ir.rayacell.mahda.R;
import ir.rayacell.mahda.manager.Container;
import ir.rayacell.mahda.manager.Manager;
import ir.rayacell.mahda.manager.NetworkManager;
import ir.rayacell.mahda.model.Command;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestFragment extends Fragment {

	private View v;
	String text = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_test, container, false);
		
		Container.setTestFragment(this);

		TextView tv_ip = (TextView) v.findViewById(R.id.textView1);
		tv_ip.setText(NetworkManager.getIpAddress());

		final EditText et_dstip = (EditText) v.findViewById(R.id.et_ip);
		Button btn_setip = (Button) v.findViewById(R.id.btn_setip);
		btn_setip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				NetworkManager.setIpAddress(et_dstip.getText().toString());
//				Container.getProviderManager().setConnection(new BaseModel(0, null, null));
			}
		});

		final EditText et_message = (EditText) v
				.findViewById(R.id.et_send_message);
		Button btn_send = (Button) v.findViewById(R.id.btn_send_message);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Command command = new Command(1,  "1234567890", App
						.getContext().getResources()
						.getString(R.string.command_voice_record), et_message
						.getText().toString(), 0, 0, 0);
				Manager.soundRecord(command);
				updateView(et_message.getText().toString());
			}
		});

		return v;
	}

	public void updateView(String s) {
		TextView tv = (TextView) v.findViewById(R.id.tv_show_recieved_message);
		tv.setMovementMethod(new ScrollingMovementMethod());

		text += s + "\n";
		tv.setText(text);
	}
}
