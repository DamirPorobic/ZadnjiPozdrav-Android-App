package info.zadnjipozdrav.zadnjipozdrav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new DataSource(this);
        datasource.open();

//        datasource.createBorough(1, "Banja Luka");
//        datasource.createBorough(2, "Banovići");
//        datasource.createBorough(3, "Berkovići");
//        datasource.createBorough(4, "Bihać");
//        datasource.createBorough(5, "Bijeljina");
//        datasource.createBorough(6, "Bileća");
//        datasource.createBorough(7, "Kostajnica");
//        datasource.createBorough(8, "Bosanska Krupa");
//        datasource.createBorough(9, "Brod");
//        datasource.createBorough(10, "Bosanski Petrovac");
//
//        datasource.createCemetery(1, "Gradsko groblje Bare", "Jukićeva bb", 43.877171, 18.39807);
//        datasource.createCemetery(2, "Groblje Alifakovac", "Veliki Alifakovac bb", 43.857818, 18.437423);
//        datasource.createCemetery(3, "Jevrejsko groblje u Sarajevu", "Gabrielle Moreno Locatelli 11", 43.850959, 18.407472);

//        datasource.createObituary("Meho", "Mehic", "Huse", "", -440640000, 1489805200, "Some text goes here.", 1489968000, 1490023000, null, 1, 1,4);
//        datasource.createObituary("Haso", "Hasic", "Reuf", "", -440630000, 1489815200, "Some text goes here.", 1489968000, 1490024000, null, 1, 2,5);
//        datasource.createObituary("Stanislav", "Stanic", "Huse", "", -440610000, 1489785200, "Some text goes here.", 1489968000, 1490025000, null, 2, 3,6);
//        datasource.createObituary("Marija", "Radic", "Anto", "", -440580000, 1489797200, "Some text goes here.", 1489968000, 1490026000, null, 3, 1,7);
//        datasource.createObituary("Igor", "Kapetanovic", "Milos", "", -440650000, 1489791200, "Some text goes here.", 1489968000, 1490027000, null, 4, 2,8);

        List<Obituary> values = datasource.getAllObituaries();

        CustomArrayAdapter adapter = new CustomArrayAdapter(this, values);
        ListView listView = (ListView) findViewById(R.id.obituary_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Obituary item = (Obituary) parent.getItemAtPosition(position);
                if (item != null) {
                    Toast.makeText(MainActivity.this, item.getName(), Toast.LENGTH_SHORT).show();
                }
            }

        });
        datasource.close();
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
