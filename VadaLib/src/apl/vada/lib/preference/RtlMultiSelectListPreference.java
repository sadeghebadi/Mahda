package apl.vada.lib.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import apl.vada.lib.R;

public class RtlMultiSelectListPreference extends ListPreference {

	private String separator;
	private static final String DEFAULT_SEPARATOR = "\u0001\u0007\u001D\u0007\u0001";
	private boolean[] entryChecked;

	private TITLELISTMULT mtitleList;
	private ENTRYLISTMULT mentryList;
	private DIRECTIONLISTMULT mdirectionList;
	public RtlMultiSelectListPreference(Context context,
			AttributeSet attributeSet) {
		
		super(context, attributeSet);
		setPositiveButtonText("باشه");
		setNegativeButtonText("بیخیال");
		entryChecked = new boolean[getEntries().length];
		separator = DEFAULT_SEPARATOR;
		TypedArray customAttrs = context.obtainStyledAttributes(attributeSet,
				R.styleable.RTLListPreference);
		mtitleList = TITLELISTMULT.values()[customAttrs.getInt(R.styleable.RTLMultiSelectListPreference_titlelistmult, 0)];
		mentryList = ENTRYLISTMULT.values()[customAttrs.getInt(R.styleable.RTLMultiSelectListPreference_entrylistmulti,0)];
		mdrectionList = DIRECTIONLISTMULT.values()[customAttrs.getInt(R.styleable.RTLMultiSelectListPreference_directionlistmulti, 0)];
		customAttrs.recycle();

	}

	public RtlMultiSelectListPreference(Context context) {
		this(context, null);
		
	}

	private DIRECTIONLISTMULT mdrectionList;

	@Override
	protected View onCreateView(ViewGroup parent) {
		View view = super.onCreateView(parent);

		if (mdrectionList == DIRECTIONLISTMULT.RTL) {
			RelativeLayout layout = (RelativeLayout) ((LinearLayout) view)
					.getChildAt(1);
			RelativeLayout.LayoutParams lpTitle = new LayoutParams(
					view.getLayoutParams());
			RelativeLayout.LayoutParams lpDescription = new LayoutParams(
					view.getLayoutParams());

			lpTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lpDescription.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			TextView tvTitle = (TextView) view.findViewById(android.R.id.title);
			tvTitle.setLayoutParams(lpTitle);

			lpDescription.addRule(RelativeLayout.BELOW, tvTitle.getId());

			TextView tvDescription = (TextView) view.findViewById(android.R.id.summary);
			tvDescription.setGravity(Gravity.RIGHT);
			tvDescription.setLayoutParams(lpDescription);
		}
		return view;
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		CharSequence[] entries = getEntries();
		CharSequence[] entryValues = getEntryValues();
		setPositiveButtonText("باشه");
		setNegativeButtonText("بیخیال");
		if (entries == null || entryValues == null
				|| entries.length != entryValues.length) {
			throw new IllegalStateException(
					"MultiSelectListPreference requires an entries array and an entryValues "
							+ "array which are both the same length");
		}

		restoreCheckedEntries();
		OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener() {
			public void onClick(DialogInterface dialog, int which, boolean val) {
				entryChecked[which] = val;
			}
		};
		builder.setMultiChoiceItems(entries, entryChecked, listener);
	}

	private CharSequence[] unpack(CharSequence val) {
		if (val == null || "".equals(val)) {
			return new CharSequence[0];
		} else {
			return ((String) val).split(separator);
		}
	}

	/**
	 * Gets the entries values that are selected
	 * 
	 * @return the selected entries values
	 */
	public CharSequence[] getCheckedValues() {
		return unpack(getValue());
	}

	private void restoreCheckedEntries() {
		CharSequence[] entryValues = getEntryValues();

		// Explode the string read in sharedpreferences
		CharSequence[] vals = unpack(getValue());

		if (vals != null) {
			List<CharSequence> valuesList = Arrays.asList(vals);
			for (int i = 0; i < entryValues.length; i++) {
				CharSequence entry = entryValues[i];
				entryChecked[i] = valuesList.contains(entry);
			}
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		List<CharSequence> values = new ArrayList<CharSequence>();

		CharSequence[] entryValues = getEntryValues();
		if (positiveResult && entryValues != null) {
			for (int i = 0; i < entryValues.length; i++) {
				if (entryChecked[i] == true) {
					String val = (String) entryValues[i];
					values.add(val);
				}
			}

			String value = join(values, separator);
			setSummary(prepareSummary(values));
			setValueAndEvent(value);
		}
	}

	private void setValueAndEvent(String value) {
		if (callChangeListener(unpack(value))) {
			setValue(value);
		}
	}

	private CharSequence prepareSummary(List<CharSequence> joined) {
		List<String> titles = new ArrayList<String>();
		CharSequence[] entryTitle = getEntries();
		CharSequence[] entryValues = getEntryValues();
		int ix = 0;
		for (CharSequence value : entryValues) {
			if (joined.contains(value)) {
				titles.add((String) entryTitle[ix]);
			}
			ix += 1;
		}
		return join(titles, ", ");
	}

	@Override
	protected Object onGetDefaultValue(TypedArray typedArray, int index) {
		return typedArray.getTextArray(index);
	}

	@Override
	protected void onSetInitialValue(boolean restoreValue,
			Object rawDefaultValue) {
		String value = null;
		CharSequence[] defaultValue;
		if (rawDefaultValue == null) {
			defaultValue = new CharSequence[0];
		} else {
			defaultValue = (CharSequence[]) rawDefaultValue;
		}
		List<CharSequence> joined = Arrays.asList(defaultValue);
		String joinedDefaultValue = join(joined, separator);
		if (restoreValue) {
			value = getPersistedString(joinedDefaultValue);
		} else {
			value = joinedDefaultValue;
		}

		setSummary(prepareSummary(Arrays.asList(unpack(value))));
		setValueAndEvent(value);
	}

	protected static String join(Iterable<?> iterable, String separator) {
		Iterator<?> oIter;
		if (iterable == null || (!(oIter = iterable.iterator()).hasNext()))
			return "";
		StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
		while (oIter.hasNext())
			oBuilder.append(separator).append(oIter.next());
		return oBuilder.toString();
	}

	public enum TITLELISTMULT {
		STRING, FLOAT, DOUBLE, INTEGER

	}

	public enum ENTRYLISTMULT {
		STRING, FLOAT, DOUBLE, INTEGER
	}

	public enum DIRECTIONLISTMULT {
		RTL, LTR
	}
}