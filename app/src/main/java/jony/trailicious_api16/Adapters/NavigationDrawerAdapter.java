package jony.trailicious_api16.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jony.trailicious_api16.R;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private TypedArray mIcons;

    //private String nombreUsuario;        //String Resource for header View Name

    //private static final int PROFILE_PIC_SIZE = 400;    // Tamaño de la imagen de perfil del usuairo en pixeles
    //String urlImagenPerfil;

    Context context;

    //private String email;       //String Resource for header view email


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profileImage;
        static TextView Name;
        TextView email;


        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{


                Name = (TextView) itemView.findViewById(R.id.drawer_nombre_usuario);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.drawer_email_usuario);       // Creating Text View object from header.xml for email
                profileImage = (ImageView) itemView.findViewById(R.id.drawer_icon_usuario);// Creating Image view object from header.xml for profileImage pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }


    }

    public NavigationDrawerAdapter(Context context){
        /*this.nombreUsuario = nombreUsuario;
        this.email = Email;
        this.urlImagenPerfil = urlFoto;*/
        this.mNavTitles = context.getResources().getStringArray(R.array.navigation_drawer_items_text);
        this.mIcons = context.getResources().obtainTypedArray(R.array.navigation_drawer_items_images);
        this.context = context;

    }



    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public NavigationDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.ViewHolder holder, int position) {
        if(holder.Holderid == 1) {
            holder.textView.setText(mNavTitles[position - 1]);

            holder.imageView.setImageResource(mIcons.getResourceId(position - 1, -1));
        }
        else {
            //El usuario se logea de forma asíncrona por lo que sus datos será pintados cuando esté dentro
            /*if (urlImagenPerfil != null && nombreUsuario != null && email != null) {
                //Por defecto la imagen viene en 50x50 px. La podemos reemplazar con la dimension que queramos reemplazando sz=X
                urlImagenPerfil = urlImagenPerfil.substring(0,
                        urlImagenPerfil.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(holder.profileImage).execute(urlImagenPerfil);

                holder.Name.setText(nombreUsuario);
                holder.email.setText(email);
            }*/
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }


    // With the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}