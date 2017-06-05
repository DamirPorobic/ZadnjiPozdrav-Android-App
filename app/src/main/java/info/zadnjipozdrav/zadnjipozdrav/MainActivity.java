package info.zadnjipozdrav.zadnjipozdrav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.obituary_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Obituary item = (Obituary) parent.getItemAtPosition(position);
                if (item != null) {
                    Intent i = new Intent(MainActivity.this, DetailsView.class);
                    i.putExtra(getResources().getString(R.string.obituary_id), item.getId());
                    startActivity(i);
                }
            }

        });

        refreshList(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_menu:
                refreshList(null);
                return true;
            case R.id.settings_menu:
                return true;
            case R.id.about_menu:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshList(ListView list) {
        if (list == null) {
            list = (ListView) findViewById(R.id.obituary_list);
        }

        Downloader downloader = new Downloader(this, list);
        downloader.execute();
    }
}
