package com.example.jose.temperatura_ion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {


    EditText edBuscaCidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edBuscaCidade = (EditText)findViewById(R.id.edBuscaCidade);


    }

    public void onClickBuscar(View view) {
        String cidade = edBuscaCidade.getText().toString();
        final TextView lbLocalidade  = (TextView)findViewById(R.id.lbLocalidade);
        final TextView lbTemperaturaDia = (TextView)findViewById(R.id.lbTemperatura);
        final TextView lbMaxDia = (TextView)findViewById(R.id.lbMax);
        final TextView lbMinDia = (TextView)findViewById(R.id.lbMin);

        if (cidade.trim().isEmpty()) {
            Toast.makeText(this, "Preencha o campo Cidade", Toast.LENGTH_SHORT).show();
        } else {

            final String recebe = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + cidade + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
            Ion.with(this)
                    .load(recebe)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                JsonObject object = result.get("query").getAsJsonObject();
                                JsonObject object1 = object.get("results").getAsJsonObject();
                                JsonObject object2 = object1.get("channel").getAsJsonObject();
                                JsonObject object3 = object2.get("location").getAsJsonObject();
                                JsonObject object9 = object2.get("item").getAsJsonObject();
                                JsonObject object10 = object9.get("condition").getAsJsonObject();

                                //Local
                                String[] local = {object3.get("city").getAsString().toString()," -",object3.get("region").getAsString().toString(),", ",object3.get("country").getAsString().toString()};
                                String resultadoLocal = "";
                                for (int a = 0; a < local.length; a++) {
                                    resultadoLocal += local[a];
                                    lbLocalidade.setText(resultadoLocal);
                                }
                                //



//                                Toast.makeText(MainActivity.this, "Date: "+object10.get("date").getAsString(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, "Temperatura: "+object10.get("temp").getAsString(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(MainActivity.this, "Desc: "+object10.get("text").getAsString(), Toast.LENGTH_SHORT).show();
                                //

                                //percorrer os dias da semana
                                JsonArray jsonArray = object9.get("forecast").getAsJsonArray();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonArray forecast = jsonArray.getAsJsonArray();
                                    JsonObject object11 = forecast.get(0).getAsJsonObject();
//                                    JsonObject object12 = forecast.get(1).getAsJsonObject();
//                                    JsonObject object13 = forecast.get(2).getAsJsonObject();
//                                    JsonObject object14 = forecast.get(3).getAsJsonObject();
//                                    JsonObject object15 = forecast.get(4).getAsJsonObject();
//                                    JsonObject object16 = forecast.get(5).getAsJsonObject();
//                                    JsonObject object17 = forecast.get(6).getAsJsonObject();
//
//                                    //Temperatura, data e descrição do tempo
                                    lbTemperaturaDia.setText(object10.get("temp").getAsString()+"ºF");
                                    lbMaxDia.setText(object11.get("high").getAsString());
                                    lbMinDia.setText(object11.get("low").getAsString());
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "ERRO: " + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
