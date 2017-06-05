package info.zadnjipozdrav.zadnjipozdrav;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class DetailsView extends AppCompatActivity {
    private Obituary obituary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        }

        DataSource dataSource = null;
        SimpleDateFormat dateFormat = null;
        SimpleDateFormat timeFormat = null;

        try {
            dataSource = new DataSource(this);
            dateFormat = new SimpleDateFormat(getResources().getString(R.string.date_format));
            timeFormat = new SimpleDateFormat(getResources().getString(R.string.time_format));

            dataSource.open();
            obituary = dataSource.getObituary(extras.getLong(getResources().getString(R.string.obituary_id)));

        } finally {
            dataSource.close();
            if (obituary == null) {
                finish();
            }
        }

        // Setup return button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView TextDetailsName = (TextView) findViewById(R.id.details_name);
        TextDetailsName.setText(obituary.getName() + " (" + obituary.getFathersName() + ") " + obituary.getFamilyName());

        TextView TextDetailsDates = (TextView) findViewById(R.id.details_dates);

        TextDetailsDates.setText(dateFormat.format(obituary.getBirthDate())
                + " - "
                + dateFormat.format(obituary.getDeathDate()));

        TextView TextDetailsText = (TextView) findViewById(R.id.details_text);
        TextDetailsText.setText(obituary.getText());

        String cemeteryText, funeralText;
        switch (obituary.getReligion()) {
            case 1:
                cemeteryText = "Mezar: ";
                funeralText = "Dzenaza: ";
                break;
            default:
                cemeteryText = "Groblje: ";
                funeralText = "Sahrana: ";

        }

        TextView TextFuneralDate = (TextView) findViewById(R.id.details_funeralDate);
        TextFuneralDate.setText(funeralText + dateFormat.format(obituary.getFuneralDate())
                + " u " + timeFormat.format(obituary.getFuneralTime()) + " sati.");

        Cemetery cemetery = obituary.getCemetery();

        if (cemetery != null) {
            TextView TextFuneralAddress = (TextView) findViewById(R.id.details_funeralAddress);
            TextFuneralAddress.setText(cemeteryText + cemetery.getName() + ", " + cemetery.getAddress());
            TextFuneralAddress.setVisibility(View.VISIBLE);

            Button button = (Button) findViewById(R.id.details_mapButton);
            button.setVisibility(View.VISIBLE);
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

    public void onOpenMap(View view) {
        if (obituary == null) {
            return;
        }

        Cemetery cemetery = obituary.getCemetery();
        if (cemetery == null) {
            return;
        }

        Uri uri = Uri.parse("geo:" + cemetery.getLat() + "," + cemetery.getLon() + "?q=" + Uri.encode(cemetery.getName()));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Check if google mpas is installed.
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onAddToCalender(View view) {
        if (obituary == null) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_EDIT);
//        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.DTSTART, obituary.getFuneralDate().getTime());
        intent.putExtra(CalendarContract.Events.DTEND, obituary.getFuneralDate().getTime() + 60 * 60 * 1000);
        intent.putExtra(CalendarContract.Events.TITLE, obituary.getName() + " " + obituary.getFamilyName() + " zadnji pozdrav.");

        Cemetery cemetery = obituary.getCemetery();
        if (cemetery != null) {
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, cemetery.getName() + ", " + cemetery.getAddress());
        }
        startActivity(intent);
    }
}
