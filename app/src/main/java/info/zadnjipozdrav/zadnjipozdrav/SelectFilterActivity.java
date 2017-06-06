package info.zadnjipozdrav.zadnjipozdrav;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

public class SelectFilterActivity extends AppCompatActivity {
    private final static String PREF_NAME = "info.zadnjipozdrav.filter_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_filter);

        DataSource dataSource = new DataSource(this);
        dataSource.open();
        final List<Borough> list = dataSource.getAllBorough();
        dataSource.close();

        final ListView listView = (ListView) findViewById(R.id.select_filter_list);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, list);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Borough item = (Borough) parent.getItemAtPosition(position);
                if (item != null) {
                    boolean checked = listView.isItemChecked(position);
                    if (checked) {
                        saveEntry(item);
                    } else {
                        removeEntry(item);
                    }
                }
            }

        });

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, 0);
        Map<String,?> listSaved = prefs.getAll();

        for (int i = 0; i < adapter.getCount(); i++) {
            Borough b = (Borough) adapter.getItem(i);
            if (b != null) {
                if (listSaved.containsKey(b.getName())) {
                    listView.setItemChecked(i, true);
                }
            }
        }

        // Setup return button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveEntry(Borough borough) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(borough.getName(), borough.getId());
        editor.apply();
    }

    public void removeEntry(Borough borough) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(borough.getName());
        editor.apply();
    }
}
