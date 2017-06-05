package info.zadnjipozdrav.zadnjipozdrav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        Downloader downloader = new Downloader(this, listView);
        downloader.execute();
    }
}
