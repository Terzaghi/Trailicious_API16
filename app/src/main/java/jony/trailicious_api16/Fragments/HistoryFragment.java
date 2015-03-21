package jony.trailicious_api16.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import jony.trailicious_api16.Adapters.HistoryAdapter;
import jony.trailicious_api16.ParseTables.Historico;
import jony.trailicious_api16.R;
import jony.trailicious_api16.TrailiciousApplication;
import jony.trailicious_api16.Utils.ConstantsUtils;

public class HistoryFragment extends Fragment {
    private final String TAG = HistoryFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private HistoryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_history);

        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Inicio el RecyclerView con una lista vacía. Cuando Parse me devuelva la lista la repinto
        mAdapter = new HistoryAdapter(new ArrayList<Historico>(), R.layout.history_row);
        recyclerView.setAdapter(mAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        getActivity().setTitle("Historial");


        //Inicio Parse con las claves de la app y del cliente
        Parse.initialize(getActivity().getApplicationContext(), ConstantsUtils.PARSE_APP_ID, ConstantsUtils.PARSE_CLIENT_KEY);

        //Registro el DTO Historico el cual extiende de Parse
        ParseObject.registerSubclass(Historico.class);

        //Obtengo el listado de entrenamientos desde Parse
        ParseQuery<Historico> query = ParseQuery.getQuery(Historico.class);
        query.findInBackground(new FindCallback<Historico>() {
            @Override
            public void done(List<Historico> historicos, ParseException e) {
                if (historicos != null) {
                    //De todos los entrenamientos, obtengo los del usuario
                    ArrayList<Historico> lstUsuario = new ArrayList<Historico>();

                    String idFacebook = TrailiciousApplication.getUsuarioLogueado().getIdFacebook();
                    for (Historico dato : historicos) {
                        if (dato.getUserFacebookID().equals(idFacebook))
                            lstUsuario.add(dato);
                    }

                    repintaListado(lstUsuario);
                }
                else {
                    repintaListado(null);
                }
            }
        });


        return rootView;
    }

    public void repintaListado(ArrayList<Historico> datos) {
        try {
            ProgressBar pb = (ProgressBar) getActivity().findViewById(R.id.progress_history);
            TextView txtNoDatos = (TextView) getActivity().findViewById(R.id.txt_no_hay_datos_historicos);

            if (datos == null || datos.size() == 0) {
                //Muestro un mensaje de que no hay datos
                recyclerView.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                txtNoDatos.setVisibility(View.VISIBLE);
            }
            else {
                // Pinto los datos

                recyclerView.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                txtNoDatos.setVisibility(View.GONE);

                mAdapter.addList(datos);
                mAdapter.notifyDataSetChanged();
                    /*mAdapter = new HistoryAdapter(lstUsuario, R.layout.history_row);
                    recyclerView.setAdapter(mAdapter);*/

            }

        }
        catch(Exception er) {
            Log.e(TAG, er.toString());
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
