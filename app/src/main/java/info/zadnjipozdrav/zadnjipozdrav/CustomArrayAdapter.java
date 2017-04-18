package info.zadnjipozdrav.zadnjipozdrav;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Obituary>{
    private final Context mContext;
    private final List<Obituary> mValues;
    private final SimpleDateFormat mDateFormat;

    public CustomArrayAdapter(Context context, List<Obituary> values){
        super(context, -1, values);
        mContext = context;
        mValues = values;
        mDateFormat = new SimpleDateFormat(mContext.getString(R.string.date_format));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.obituary_row, parent, false);
        TextView textViewNames = (TextView) rowView.findViewById(R.id.names);
        TextView textViewDates = (TextView) rowView.findViewById(R.id.dates);
        ImageView imageViewPicture = (ImageView) rowView.findViewById(R.id.picture);
        textViewNames.setText(mValues.get(position).getName() + " ("
                + mValues.get(position).getFathersName() + ") "
                + mValues.get(position).getFamilyName());

        textViewDates.setText(mDateFormat.format(mValues.get(position).getBirthDate())
                + " - "
                + mDateFormat.format(mValues.get(position).getDeathDate()));

        if (mValues.get(position).getPicture() == null) {
            imageViewPicture.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            imageViewPicture.setImageBitmap(mValues.get(position).getPicture());
        }

        return rowView;
    }
}
