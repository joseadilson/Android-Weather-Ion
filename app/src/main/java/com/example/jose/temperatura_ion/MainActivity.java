package com.example.jose.temperatura_ion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jose.temperatura_ion.adapter.WeatherAdapter;
import com.example.jose.temperatura_ion.model.Weather;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private List<Weather> weatherList = new ArrayList<>();
    private WeatherAdapter weatherAdapter;

    private TextView lbTemperaturaDia;
    private String cidadePesq;
    private String cityConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("");
        progressDialog = new ProgressDialog(MainActivity.this);

        cidadePesq = getIntent().getStringExtra("NOME_CIDADE");
        requisicao();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_city, menu);
        return true;
    }

    public void menuCliqueExcluir(MenuItem item) {
        pesquisaCidade();
    }

    public void pesquisaCidade() {
        startActivity(new Intent(this, PesquisarCidadeActivity.class));
    }

    public void requisicao() {
        try {
            cityConverter = cidadePesq.toString();
        } catch (Exception e2){
            Toast.makeText(this, "Pesquise uma cidade!", Toast.LENGTH_SHORT).show();
            return;
        }

        RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_view);

        weatherAdapter = new WeatherAdapter(weatherList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv.setAdapter(weatherAdapter);

        lbTemperaturaDia = (TextView) findViewById(R.id.lbTemperatura);
        final TextView lbDescDia = (TextView) findViewById(R.id.lbDescDia);

        //replace
        String cidade = cityConverter;
        String city = cidade.replace(" ", "%20");
        //

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Buscando a cidade...");
        progressDialog.show();

        final String recebe = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + city +",ak"+"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        Ion.with(MainActivity.this)
                .load(recebe)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            JsonObject object   = result.get("query").getAsJsonObject();

                            try{
                                JsonObject object1  = object.get("results").getAsJsonObject();
                                JsonObject object2  = object1.get("channel").getAsJsonObject();
                                JsonObject object3  = object2.get("location").getAsJsonObject();
                                JsonObject object9  = object2.get("item").getAsJsonObject();
                                JsonObject object10 = object9.get("condition").getAsJsonObject();


                                //Local
                                String[] local = {object3.get("city").getAsString().toString(), " -", object3.get("region").getAsString().toString(), ", ", object3.get("country").getAsString().toString()};
                                String resultadoLocal = "";
                                for (int a = 0; a < local.length; a++) {
                                    resultadoLocal += local[a];
                                    getSupportActionBar().setTitle("" + resultadoLocal);
                                }
                                //

                                //dia
                                JsonArray jsonArray = object9.get("forecast").getAsJsonArray();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonArray forecast = jsonArray.getAsJsonArray();
                                    JsonObject object11 = forecast.get(0).getAsJsonObject();

                                    //Conversão
                                    DecimalFormat formato = new DecimalFormat("##");
                                    Double converter  = Double.valueOf(object10.get("temp").getAsString());
                                    Double calcular  = (converter - 32) / 1.8;

                                    //Temperatura, descrição do tempo do Dia
                                    lbTemperaturaDia.setText(formato.format(calcular) + "ºc");
                                    lbDescDia.setText(object10.get("text").getAsString());

//                                    //Tipo da imagem
                                    ImageView img  = (ImageView) findViewById(R.id.img);
                                    String pegarNome  = object10.get("text").getAsString();

                                    if (pegarNome.toString().equals("Partly Cloudy") || pegarNome.toString().equals("Mostly Cloudy") || pegarNome.toString().equals("Cloudy")) {
                                        img.setImageResource(R.drawable.nube);
                                    } if (pegarNome.toString().equals("Thunderstorms") || pegarNome.toString().equals("Scattered Thunderstorms")) {
                                        img.setImageResource(R.drawable.tormenta);
                                    } if (pegarNome.toString().equals("Scattered Showers") || pegarNome.toString().equals("Showers") || pegarNome.toString().equals("Rain")) {
                                        img.setImageResource(R.drawable.lluvia);
                                    } if (pegarNome.toString().equals("Mostly Sunny")) {
                                        img.setImageResource(R.drawable.nubesol);
                                    } if (pegarNome.toString().equals("clear")) {
                                        img.setImageResource(R.drawable.sol);
                                    } if (pegarNome.toString().equals("Snow") || pegarNome.toString().equals("Snow Showers")) {
                                        img.setImageResource(R.drawable.snow);
                                    }
                                }
                                //Semana Toda//
                                JsonArray arrayA = object9.get("forecast").getAsJsonArray();
                                for (int z = 0; z < arrayA.size(); z++) {
                                    JsonArray array = arrayA.getAsJsonArray();
                                    JsonObject objectSemana = array.get(z).getAsJsonObject();

                                    DecimalFormat formato = new DecimalFormat("##");
                                    Double converterSemanaMax  = Double.valueOf(objectSemana.get("high").getAsString());
                                    Double converterSemanaMin  = Double.valueOf(objectSemana.get("low").getAsString());
                                    Double calcularMax  = (converterSemanaMax - 32) / 1.8;
                                    Double calcularMin  = (converterSemanaMin - 32) / 1.8;

                                    String date = objectSemana.get("date").getAsString();
                                    String day  = objectSemana.get("day").getAsString();
                                    String high = formato.format(calcularMax);
                                    String low  = formato.format(calcularMin);
                                    String text = objectSemana.get("text").getAsString();

                                    Weather weather = new Weather(date.toString(), day.toString(), high.toString()+" ↑", low.toString()+" ↓", text.toString());
                                    weatherList.add(weather);
                                    weatherAdapter.notifyDataSetChanged();
                                }

                            }catch (Exception e1){
                                Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
