package apl.vada.lib.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import apl.vada.lib.R;


public class RtlListPreference extends ListPreference {

	private TITLELIST mtitleList;
	private ENTRYLIST mentryList;
	private DIRECTIONLIST mdrectionList;

	public RtlListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPositiveButtonText(R.string.accept);
		setNegativeButtonText(R.string.cancel);

		TypedArray customAttrs = context.obtainStyledAttributes(attrs,
				R.styleable.RTLListPreference);
		mtitleList = TITLELIST.values()[customAttrs.getInt(
				R.styleable.RTLListPreference_titlelist, 0)];
		mentryList = ENTRYLIST.values()[customAttrs.getInt(
				R.styleable.RTLListPreference_entrylist, 0)];
		mdrectionList = DIRECTIONLIST.values()[customAttrs.getInt(
				R.styleable.RTLListPreference_directionlist, 0)];
		customAttrs.recycle();

	}

	private CharSequence[] mEntries;
	private CharSequence[] mEntryValues;
	private String mValue;
	private int mClickedDialogEntryIndex;

	@Override
	protected void onPrepareDialogBuilder(
			android.app.AlertDialog.Builder builder) {
		// TODO Auto-generated method stub
		super.onPrepareDialogBuilder(builder);

		mEntries = getEntries();
		mEntryValues = getEntryValues();
		mValue = getValue();
		mClickedDialogEntryIndex = getValueIndex();

		if (mEntries == null || mEntryValues == null) {
			throw new IllegalStateException(
					"ListPreference requires an entries array and an entryValues array.");
		}
		
		String[] mEntriesString = new String[mEntries.length];
		int i = 0;
		for (CharSequence ch : mEntries) {
			mEntriesString[i++] = ch.toString();
		}
		ListAdapter adapter = new ArrayAdapter<String>(getContext(),
				R.layout.two_lines_list_preference_row, mEntriesString) {

			ViewHolder holder;

			class ViewHolder {
				TextView title;
				ImageView selectedIndicator;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				final LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				if (convertView == null) {
					convertView = inflater.inflate(
							R.layout.two_lines_list_preference_row, null);

					holder = new ViewHolder();
					holder.title = (TextView) convertView
							.findViewById(R.id.custom_list_view_row_text_view);
					holder.selectedIndicator = (ImageView) convertView
							.findViewById(R.id.custom_list_view_row_selected_indicator);

					convertView.setTag(holder);
				} else {
					// view already defined, retrieve view holder
					holder = (ViewHolder) convertView.getTag();
				}

				holder.title.setText(mEntries[position]);
				holder.selectedIndicator
						.setVisibility(position == mClickedDialogEntryIndex ? View.VISIBLE
								: View.GONE);

				return convertView;
			}
		};

		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mClickedDialogEntryIndex = which;
				RtlListPreference.this.onClick(dialog,
						DialogInterface.BUTTON_POSITIVE);
				dialog.dismiss();
			}
		});

		builder.setPositiveButton(null, null);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		if (positiveResult && mClickedDialogEntryIndex >= 0
				&& mEntryValues != null) {
			String value = mEntries[mClickedDialogEntryIndex].toString();
			setSummary(value);
			if (callChangeListener(value)) {
				setValue(value);
			}
		}
	}

	public int findIndexOfValue(String value) {
		if (value != null && mEntryValues != null) {
			for (int i = mEntryValues.length - 1; i >= 0; i--) {
				if (mEntryValues[i].equals(value)) {
					return i;
				}
			}
		}
		return -1;
	}

	private int getValueIndex() {
		return findIndexOfValue(mValue);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.onCreateView(parent);

		if (mdrectionList == DIRECTIONLIST.RTL) {
			RelativeLayout layout = (RelativeLayout) ((LinearLayout) view)
					.getChildAt(1);
			RelativeLayout.LayoutParams lpTitle = new LayoutParams(
					view.getLayoutParams());
			RelativeLayout.LayoutParams lpDescription = new LayoutParams(
					view.getLayoutParams());

			lpTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lpDescription.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			TextView tvTitle = (TextView) layout.getChildAt(0);
			tvTitle.setLayoutParams(lpTitle);

			lpDescription.addRule(RelativeLayout.BELOW, tvTitle.getId());

			TextView tvDescription = (TextView) layout.getChildAt(1);
			tvDescription.setGravity(Gravity.RIGHT);
			tvDescription.setLayoutParams(lpDescription);
			// RelativeLayout layout = (RelativeLayout) ((LinearLayout)
			// view).getChildAt(1);
			// RelativeLayout.LayoutParams lpTitle = new
			// LayoutParams(view.getLayoutParams());
			// RelativeLayout.LayoutParams lpDescription = new
			// LayoutParams(view.getLayoutParams());
			//
			// lpTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			// lpDescription.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			//
			// TextView tvTitle = (TextView) layout.getChildAt(0);
			// tvTitle.setLayoutParams(lpTitle);
			//
			// lpDescription.addRule(RelativeLayout.BELOW, tvTitle.getId());
			// lpDescription.addRule(RelativeLayout.BELOW, android.R.id.title);
			//
			// TextView summ = ((TextView)
			// view.findViewById(android.R.id.summary));
			// summ.setTextSize(30);
			// summ.;
			// TextView tvDescription = (TextView) layout.getChildAt(1);
			// tvDescription.setLayoutParams(lpDescription);
			// Log.d("$$$$",summ.getId() + "  "+ tvDescription.getId() );
			// Log.d("&*&&****", tvTitle.getId() + "  "+
			// tvDescription.getId()+"  "+tvDescription.toString());
		}
		return view;
	}

	public enum TITLELIST {
		STRING, FLOAT, DOUBLE, INTEGER

	}

	public enum ENTRYLIST {
		STRING, FLOAT, DOUBLE, INTEGER
	}

	public enum DIRECTIONLIST {
		RTL, LTR
	}
}