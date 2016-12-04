package com.example.jose.temperatura_ion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickBuscar(View view) {
//        String url = "https://query.yahooapis.com/v1/public/yql?q=";
//        String select = "%20select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text=%22CIANORTE%22)%20";
//        String option = "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
//        String recebe = url+select+option;

//        String recebe  ="https://query.yahooapis.com/v1/public/yql?q=%20select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text=%22CIANORTE%22)%20&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
//        String recebe = "http://microblogging.wingnity.com/JSONParsingTutorial/jsonActors";

        final String  recebe = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22CIANORTE%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
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
//                            JsonObject object3 = object2.get("units").getAsJsonObject();
//                            JsonObject object4 = object3.get("location").getAsJsonObject();
//                            JsonObject object5 = object4.get("wind").getAsJsonObject();
//                            JsonObject object6 = object5.get("atmosphere").getAsJsonObject();
//                            JsonObject object7 = object6.get("astronomy").getAsJsonObject();
//                            JsonObject object8 = object7.get("image").getAsJsonObject();
                            JsonObject object9 = object2.get("item").getAsJsonObject();
//                            JsonObject object10 = object2.get("condition").getAsJsonObject();

                            JsonArray jsonArray = object9.get("forecast").getAsJsonArray();
                            for (int i = 0; i < jsonArray.size(); i++){
                                JsonArray forecast = jsonArray.getAsJsonArray();
                                JsonObject object11 = forecast.get(0).getAsJsonObject();
                                JsonObject object12 = forecast.get(1).getAsJsonObject();
                                JsonObject object13 = forecast.get(2).getAsJsonObject();
                                JsonObject object14 = forecast.get(3).getAsJsonObject();
                                JsonObject object15 = forecast.get(4).getAsJsonObject();
                                JsonObject object16 = forecast.get(5).getAsJsonObject();
                                JsonObject object17 = forecast.get(6).getAsJsonObject();


                                Toast.makeText(MainActivity.this, ""+object11, Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, ""+object12, Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, ""+object13, Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, ""+object14, Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, ""+object15, Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, ""+object16, Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, ""+object17, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "ERRO: "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
