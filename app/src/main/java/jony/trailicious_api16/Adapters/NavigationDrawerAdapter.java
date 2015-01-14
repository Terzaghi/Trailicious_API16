package jony.trailicious_api16.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jony.trailicious_api16.R;

public class NavigationDrawerAdapter extends BaseAdapter {
    private Context context;
    String filasDrawer[];
    int[] images = {R.drawable.icon_launcher, R.drawable.icon_launcher, R.drawable.icon_launcher, R.drawable.icon_launcher, R.drawable.icon_launcher};

    public NavigationDrawerAdapter(Context context) {
        this.context = context;
        filasDrawer = context.getResources().getStringArray(R.array.navigation_drawer_items);
    }
    @Override
    public int getCount() {
        //Número de elementos del ListView
        return filasDrawer.length;
    }

    @Override
    public Object getItem(int position) {
        //Elemento en la posición dada
        return filasDrawer[position];
    }

    @Override
    public long getItemId(int position) {
        //No lo necesitamos pero debemos implementarlo obligatoriamente
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_row, parent, false);
        }
        else {
            row = convertView;
        }

        //Creamos cada row del ListView
        TextView titleTextView1 = (TextView) row.findViewById(R.id.textView1);
        ImageView titleImageView = (ImageView) row.findViewById(R.id.imageView1);

        titleTextView1.setText(filasDrawer[position]);
        titleImageView.setImageResource(images[position]);

        return row;
    }
}
