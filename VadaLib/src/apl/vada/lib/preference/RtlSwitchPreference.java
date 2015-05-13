package apl.vada.lib.preference;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import apl.vada.lib.R;

public class RtlSwitchPreference extends TwoStatePreference {
	public RtlSwitchPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private final Listener mListener = new Listener();
	private ExternalListener mExternalListener;

	@Override
	protected View onCreateView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(R.layout.toggle_preference_layout, parent,
				false);
	}

	@Override
	protected void onBindView(View view) {
		super.onBindView(view);

		Switch toggleButton = (Switch) view
				.findViewById(R.id.toggle_togglebutton);
		toggleButton.setChecked(isChecked());
		toggleButton.setOnCheckedChangeListener(mListener);
	}

	/**
	 * This gets called when the preference (as a whole) is selected by the
	 * user. The TwoStatePreference implementation changes the actual state of
	 * this preference, which we don't want, since we're handling preference
	 * clicks with our 'external' listener. Hence, don't call super.onClick(),
	 * but the onPreferenceClick of our listener.
	 */
	@Override
	protected void onClick() {
		if (mExternalListener != null)
			mExternalListener.onPreferenceClick();
	}

	/**
	 * Simple interface that defines an external listener that can be notified
	 * when the preference has been been clicked. This may be useful e.g. to
	 * navigate to a new activity from your PreferenceActivity, or display a
	 * dialog.
	 */
	public static interface ExternalListener {
		void onPreferenceClick();
	}

	/** Sets an external listener for this preference */
	public void setExternalListener(ExternalListener listener) {
		mExternalListener = listener;
	}

	/**
	 * Listener to update the boolean flag that gets stored into the Shared
	 * Preferences
	 */
	private class Listener implements CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (!callChangeListener(isChecked)) {
				// Listener didn't like it, change it back.
				// CompoundButton will make sure we don't recurse.
				buttonView.setChecked(!isChecked);
				return;
			}

			RtlSwitchPreference.this.setChecked(isChecked);
		}
	}

	private enum DEFAULT_VALUESWITCH {
		TRUE, FALSE
	}

	private enum SWITCHDIRECTION {
		RTL, LTR
	}
}
