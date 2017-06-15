package info.zadnjipozdrav.zadnjipozdrav;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    public static final String SETUP_FILTER = "setup_filter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup return button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Display the fragment as the main content.
        PrefsFragment fragment = new PrefsFragment();

        // Check if any bundle was provided, in this bundle we eventually send information for the
        // fragment to show the filter selection.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Inner class representing the fragment which holds all configuration settings.
     */
    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // Populate list with interval entries for notification check intervals
            final ListPreference listNotifyIntervals = (ListPreference) findPreference(getString(R.string.settings_notify_interval_key));
            CharSequence[] entriesInterval = new CharSequence[NotifyInterval.values().length];
            CharSequence[] valuesInterval = new CharSequence[NotifyInterval.values().length];
            for (int i = 0; i < NotifyInterval.values().length; i++) {
                entriesInterval[i] = NotifyInterval.values()[i].toString();
                valuesInterval[i] = Long.toString(NotifyInterval.values()[i].getValue());
            }
            listNotifyIntervals.setEntries(entriesInterval);
            listNotifyIntervals.setEntryValues(valuesInterval);
            listNotifyIntervals.setDefaultValue(Long.toString(NotifyInterval.OneHour.getValue()));
            if (listNotifyIntervals.getValue() == null) {
                listNotifyIntervals.setValueIndex(1);
            }

            final CheckBoxPreference checkboxEnableNotify = (CheckBoxPreference) findPreference(getString(R.string.settings_notify_enable_key));
            checkboxEnableNotify.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlarmReceiver.enableNotification(App.getContext(),
                            checkboxEnableNotify.isChecked(),
                            Long.parseLong(listNotifyIntervals.getValue()));
                    return false;
                }
            });

            DataSource dataSource = new DataSource(App.getContext());
            dataSource.open();
            final List<Borough> list = dataSource.getAllBorough();
            dataSource.close();

            // Populate multiselection list with boroughs that we use as filter.
            CustomMultiSelectListPreference multiListBoroughs = (CustomMultiSelectListPreference) findPreference(getString(R.string.settings_filter_borough_key));
            multiListBoroughs.loadBoroughs(list);

            // when a bundle was provided and the setup_filter is true, then open the filter
            // selection right away.
            Bundle bundle = getArguments();
            if (bundle != null && bundle.getBoolean(SETUP_FILTER)) {
                multiListBoroughs.show();
            }
        }
    }
}
