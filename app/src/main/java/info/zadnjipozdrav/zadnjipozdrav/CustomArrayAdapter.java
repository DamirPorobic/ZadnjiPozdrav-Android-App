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
        TextView textViewNames = (TextView) rowView.findViewById(R.id.row_names);
        TextView textViewDates = (TextView) rowView.findViewById(R.id.row_dates);
        ImageView imageViewPicture = (ImageView) rowView.findViewById(R.id.row_picture);
        FrameLayout frame = (FrameLayout) rowView.findViewById(R.id.row_frame);
        Obituary obituary = mValues.get(position);
        textViewNames.setText(obituary.getName() + " (" + obituary.getFathersName() + ") " + obituary.getFamilyName());

        textViewDates.setText(mDateFormat.format(obituary.getBirthDate())
                + " - "
                + mDateFormat.format(obituary.getDeathDate()));

        if (obituary.getPicture() == null) {
            imageViewPicture.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            imageViewPicture.setImageBitmap(mValues.get(position).getPicture());
        }

        switch (obituary.getReligion()) {
            case 1:
                frame.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.row_green, null));
                break;
            case 2:
            case 3:
                frame.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.row_black, null));
                break;
            case 4:
                frame.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.row_blue, null));
                break;
        }

        return rowView;
    }
}
