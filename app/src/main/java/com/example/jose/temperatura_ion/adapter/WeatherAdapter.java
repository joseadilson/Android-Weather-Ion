package com.example.jose.temperatura_ion.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jose.temperatura_ion.R;
import com.example.jose.temperatura_ion.dominio.Weather;

import java.util.List;

/**
 * Created by Jose on 04/01/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder>{

    private List<Weather> weatherList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView lbDate;
        public TextView lbDay;
        public TextView lbHigh;
        public TextView lbLow;
        public TextView lbText;


        public MyViewHolder(View itemView) {
            super(itemView);

            lbDate = (TextView)itemView.findViewById(R.id.lbDate);
            lbDay  = (TextView)itemView.findViewById(R.id.lbDay);
            lbHigh = (TextView)itemView.findViewById(R.id.lbHigh);
            lbLow  = (TextView)itemView.findViewById(R.id.lbLow);
            lbText = (TextView)itemView.findViewById(R.id.lbText);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weather, parent, false);

        return new MyViewHolder(view);
    }

    public WeatherAdapter(List<Weather> weatherList){
        this.weatherList = weatherList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.lbDate.setText(weather.getDate());
        holder.lbDay.setText(weather.getDay());
        holder.lbHigh.setText(weather.getHigh());
        holder.lbLow.setText(weather.getLow());
        holder.lbText.setText(weather.getText());

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


}
