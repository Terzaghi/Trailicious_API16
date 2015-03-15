package jony.trailicious_api16.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import jony.trailicious_api16.Adapters.HistoryAdapter;
import jony.trailicious_api16.R;
import jony.trailicious_api16.Utils.ConstantsUtils;
import jony.trailicious_api16.dto.Historico;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter mAdapter;
    //ArrayList<Historico> lstHistoricos;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_history);

        //TODO: Obtengo las rutas seguidas por el usuario de BD (Parse)

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
                    //Repinto la lista
                    //pintaListado(historicos);
                    /*mAdapter.clear();
                    mAdapter.addAll(historicos);*/
                    mAdapter = new HistoryAdapter(historicos, R.layout.history_row);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });

        //lstHistoricos = getHistoricos(idUsuario);

        //lstHistoricos = new ArrayList<>();
        /*for (int i=1; i<=10; i++) {
            Historico h = new Historico();
            h.setIdEntrenamiento(i);
            h.setIdRuta(i);
            h.setDuracion("0:50:45");
            h.setDistancia("10,05 Km");

            h.setFecha(new Date(0));

            lstHistoricos.add(h);
        }*/



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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void pintaListado(List<Historico> lstHistoricos) {
        try {
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(new HistoryAdapter(lstHistoricos, R.layout.history_row));

            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.scrollToPosition(0);
            recyclerView.setLayoutManager(layoutManager);
        }
        catch(Exception er){}
    }
}
