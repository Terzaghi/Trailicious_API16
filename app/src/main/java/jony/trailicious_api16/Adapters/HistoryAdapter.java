package jony.trailicious_api16.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jony.trailicious_api16.R;
import jony.trailicious_api16.Utils.Parse;
import jony.trailicious_api16.dto.Historico;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Historico> lstHistoricos;
    private int itemLayout;


    public HistoryAdapter(List<Historico> data, int itemLayout) {
        this.lstHistoricos = data;
        this.itemLayout = itemLayout;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fecha;
        public TextView duracion;
        public TextView distancia;

        public ViewHolder(View itemView) {

            super(itemView);
            fecha = (TextView) itemView.findViewById(R.id.txtFecha);
            distancia = (TextView) itemView.findViewById(R.id.txtDistancia);
            duracion = (TextView) itemView.findViewById(R.id.txtDuracion);

        }
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        Historico historico = lstHistoricos.get(position);


        String duracion = Parse.segundos2String(historico.getDuration());
        String distancia = Parse.distancia2String(historico.getDistance());
        String fecha = Parse.date2String(historico.getFecha());


        holder.fecha.setText(fecha);
        holder.distancia.setText(distancia);
        holder.duracion.setText(duracion);

        holder.itemView.setTag(historico);
    }


    @Override
    public int getItemCount() {
        if (this.lstHistoricos != null)
            return this.lstHistoricos.size();
        else
            return 0;
    }
}
