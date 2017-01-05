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

//    EditText edBuscaCidade;
    private ProgressDialog progressDialog;

    private List<Weather> weatherList = new ArrayList<>();
    private WeatherAdapter weatherAdapter;

    String cidadePesq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cidadePesq = getIntent().getStringExtra("NOME_CIDADE");
        getSupportActionBar().setTitle("");
        requisicao();
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


//        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Digite a cidade");
//        builder.setMessage("Encontre a cidade");
//
//        edBuscaCidade = new EditText(MainActivity.this);
//        builder.setView(edBuscaCidade);
//
//        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                requisicao();
//            }
//        });
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
    }

    public void requisicao() {
//        String cityConverter = edBuscaCidade.getText().toString();
        String cityConverter = cidadePesq.toString();
        RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_view);

        weatherAdapter = new WeatherAdapter(weatherList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv.setAdapter(weatherAdapter);

        final TextView lbTemperaturaDia = (TextView) findViewById(R.id.lbTemperatura);
        final TextView lbDescDia = (TextView) findViewById(R.id.lbDescDia);

        final ImageView img  = (ImageView) findViewById(R.id.img);

        if (cityConverter.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Preencha o campo Cidade", Toast.LENGTH_SHORT).show();
        } else {

            //replace
            String cidade = cityConverter;
            String city = cidade.replace(" ", "%20");
            //

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

}
