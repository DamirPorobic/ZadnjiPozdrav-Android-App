package info.zadnjipozdrav.zadnjipozdrav;


import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Obituary>{
    private final Context context;
    private final List<Obituary> values;
    private final SimpleDateFormat dateFormat;

    public CustomArrayAdapter(Context context, List<Obituary> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        dateFormat = new SimpleDateFormat(this.context.getString(R.string.date_format));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.obituary_row, parent, false);
        TextView textViewNames = (TextView) rowView.findViewById(R.id.row_names);
        TextView textViewDates = (TextView) rowView.findViewById(R.id.row_dates);
        ImageView imageViewPicture = (ImageView) rowView.findViewById(R.id.row_picture);
        FrameLayout frame = (FrameLayout) rowView.findViewById(R.id.row_frame);
        Obituary obituary = values.get(position);
        textViewNames.setText(obituary.getName() + " (" + obituary.getFathersName() + ") " + obituary.getFamilyName());

        textViewDates.setText(dateFormat.format(obituary.getBirthDate())
                + " - "
                + dateFormat.format(obituary.getDeathDate()));

        if (obituary.getPicture() == null) {
            imageViewPicture.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            imageViewPicture.setImageBitmap(values.get(position).getPicture());
        }

        switch (obituary.getReligion()) {
            case 1:
                frame.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.row_green, null));
                break;
            case 2:
            case 3:
                frame.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.row_black, null));
                break;
            case 4:
                frame.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.row_blue, null));
                break;
        }

        return rowView;
    }
}
