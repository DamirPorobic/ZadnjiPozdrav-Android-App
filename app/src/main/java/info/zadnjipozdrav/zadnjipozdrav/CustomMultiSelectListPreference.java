package info.zadnjipozdrav.zadnjipozdrav;

import android.content.Context;
import android.preference.MultiSelectListPreference;
import android.util.AttributeSet;

import java.util.List;

public class CustomMultiSelectListPreference extends MultiSelectListPreference {

    CustomMultiSelectListPreference(Context context){
        super(context);
    }

    CustomMultiSelectListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    // Supported with API 21 and newer
//    CustomMultiSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
//         super(context, attrs, defStyleAttr);
//    }

//    // Supported with API 21 and newer
//    CustomMultiSelectListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    // Used to open the dialog from code, on first run to allow the user to select boroughs
    public void show() {
        showDialog(null);
    }

    public void loadBoroughs(List<Borough> list) {
        CharSequence[] entriesBorough = new CharSequence[list.size()];
        CharSequence[] valuesBorough = new CharSequence[list.size()];
        for (int i = 0; i < list.size(); i++) {
            entriesBorough[i] = list.get(i).getName();
            valuesBorough[i] = Long.toString(list.get(i).getId());
        }
        setEntries(entriesBorough);
        setEntryValues(valuesBorough);
    }
}
