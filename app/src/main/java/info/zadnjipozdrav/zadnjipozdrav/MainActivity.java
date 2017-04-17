package info.zadnjipozdrav.zadnjipozdrav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ObituaryDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new ObituaryDataSource(this);
        datasource.open();

        datasource.createObituary("Meho", "Mehic", "Huse", "Test", -440640000, 1489795200,
                "Some text goes here.", 1489968000, 1490022000, null, "Islam", "Bare",
                "Janiceva 17", 43.877076, 18.398143);

        List<Obituary> values = datasource.getAllObituaries();

        CustomArrayAdapter adapter = new CustomArrayAdapter(this, values);
        ListView list = (ListView) findViewById(R.id.obituary_list);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
