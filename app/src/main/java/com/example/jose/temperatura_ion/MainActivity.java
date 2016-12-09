package com.example.jose.temperatura_ion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText edBuscaCidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pesquisaCidade();

    }

    public void pesquisaCidade() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Digite a cidade");
        builder.setMessage("Adicione a cidade");

        edBuscaCidade = new EditText(MainActivity.this);
        builder.setView(edBuscaCidade);

        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requisicao();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    public void requisicao() {
        String cityConverter = edBuscaCidade.getText().toString();
//        final TextView lbLocalidade  = (TextView)findViewById(R.id.lbLocalidade);
        final TextView lbTemperaturaDia = (TextView)findViewById(R.id.lbTemperatura);
        final TextView lbMaxDia = (TextView)findViewById(R.id.lbMax);
        final TextView lbMinDia = (TextView)findViewById(R.id.lbMin);
        final TextView lbDescDia = (TextView)findViewById(R.id.lbDescDia);

        final TextView lbDataSemana = (TextView)findViewById(R.id.lbDataSemana);
        final TextView lbDataSemanaSeg = (TextView)findViewById(R.id.lbDataSemanaSeg);
        final TextView lbDataSemanaTer = (TextView)findViewById(R.id.lbDataSemanaTer);

        final TextView lbDiaSemana = (TextView)findViewById(R.id.lbDiaSemana);
        final TextView lbDiaSemanaSeg = (TextView)findViewById(R.id.lbDiaSemanaSeg);
        final TextView lbDiaSemanaTerc = (TextView)findViewById(R.id.lbDiaSemanaTer);

        final TextView lbMinSemana = (TextView)findViewById(R.id.lbMinSemana);
        final TextView lbMinSemanaSeg = (TextView)findViewById(R.id.lbMinSemanaSeg);
        final TextView lbMinSemanaTer = (TextView)findViewById(R.id.lbMinSemanaTer);

        final TextView lbMaxSemana = (TextView)findViewById(R.id.lbMaxSemana);
        final TextView lbMaxSemanaSeg = (TextView)findViewById(R.id.lbMaxSemanaSeg);
        final TextView lbMaxSemanaTer = (TextView)findViewById(R.id.lbMaxSemanaTer);

//        final TextView lbDescSemana = (TextView)findViewById(R.id.lbDescSemana);
//        final TextView lbDescSemanaSeg = (TextView)findViewById(R.id.lbDescSemanaSeg);
//        final TextView lbDescSemanaTer = (TextView)findViewById(R.id.lbDescSemanaTer);

        final ImageView img = (ImageView) findViewById(R.id.img);

        if (cityConverter.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "Preencha o campo Cidade", Toast.LENGTH_SHORT).show();
        } else {

            //replace
            String cidade = cityConverter;
            String city = cidade.replace(" ", "%20");
            //

            final String recebe = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + city + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
            Ion.with(MainActivity.this)
                    .load(recebe)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            if (e == null) {
                                JsonObject object   = result.get("query").getAsJsonObject();
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
//                                    lbLocalidade.setText(resultadoLocal);
                                    getSupportActionBar().setTitle(""+resultadoLocal);
                                }
                                //

                                //percorrer os dias da semana
                                JsonArray jsonArray = object9.get("forecast").getAsJsonArray();
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonArray forecast  = jsonArray.getAsJsonArray();
                                    JsonObject object11 = forecast.get(0).getAsJsonObject();
                                    JsonObject object12 = forecast.get(1).getAsJsonObject();
                                    JsonObject object13 = forecast.get(2).getAsJsonObject();
                                    JsonObject object14 = forecast.get(3).getAsJsonObject();

                                    //Conversão
                                    DecimalFormat formato = new DecimalFormat("##");
                                    Double converter  = Double.valueOf(object10.get("temp").getAsString());
                                    Double converter1 = Double.valueOf(object11.get("high").getAsString());
                                    Double converter2 = Double.valueOf(object11.get("low").getAsString());
                                    Double converter3 = Double.valueOf(object12.get("low").getAsString());
                                    Double converter4 = Double.valueOf(object12.get("high").getAsString());
                                    Double converter5 = Double.valueOf(object13.get("low").getAsString());
                                    Double converter6 = Double.valueOf(object13.get("high").getAsString());
                                    Double converter7 = Double.valueOf(object14.get("low").getAsString());
                                    Double converter8 = Double.valueOf(object14.get("high").getAsString());

                                    Double calcular  = (converter - 32) / 1.8;
                                    Double calcular1 = (converter1 - 32) / 1.8;
                                    Double calcular2 = (converter2 - 32) / 1.8;
                                    Double calcular3 = (converter3 - 32) / 1.8;
                                    Double calcular4 = (converter4 - 32) / 1.8;
                                    Double calcular5 = (converter5 - 32) / 1.8;
                                    Double calcular6 = (converter6 - 32) / 1.8;
                                    Double calcular7 = (converter7 - 32) / 1.8;
                                    Double calcular8 = (converter8 - 32) / 1.8;
                                    //

                                    //Temperatura, descrição do tempo do Dia
                                    lbTemperaturaDia.setText(formato.format(calcular) + "ºc");
                                    lbMaxDia.setText(formato.format(calcular1) + "º");
                                    lbMinDia.setText(formato.format(calcular2) + "º");
                                    lbDescDia.setText(object10.get("text").getAsString());

                                    //Imagens
//                                    String vImageSol = "https://s-media-cache-ak0.pinimg.com/236x/3d/f0/66/3df066f31d689257b22643d52b12aa38.jpg";
//                                    String vImageChuva = "https://image.freepik.com/freie-ikonen/regen-wolke-schlaganfall-wettersymbol_318-71123.jpg";
//                                    String vImageNublado = "http://www.clipartpal.com/_thumbs/pd/weather/04.png";

                                    String vImageTrovoada       = "http://blog.bloxxter.cz/wp-content/uploads/2016/01/thunderstorm-wear-icons-46090.png";
                                    String vImageParNubado      = "http://iconshow.me/media/images/ui/ios7-icons/png/512/partlysunny.png";
                                    String vImageParEnsolarado  = "http://english.onlinekhabar.com/wp-content/uploads/2016/05/partlysunny.png";
                                    String vImageEnsolarado     = "http://static.tumblr.com/bf1be289a6f908d496e37b2765f8e0af/vl8wqgb/wJlmu3wka/tumblr_static_black_sun_with_rays_u2600_icon_256x256.png";
                                    String vImageNublado        = "http://www.ionidea.com/contact/cloud.png";
                                    String vImageVento          = "http://freevector.co/wp-content/uploads/2013/10/55939-wind-weather-lines-group-symbol.png";

                                    String pegarNome  = object10.get("text").getAsString();
                                    String pegarImage = null;
                                    if (pegarNome.toString().equals("Thunderstorms")) {
                                        pegarImage = vImageTrovoada;
                                    } if (pegarNome.toString().equals("Scattered Thunderstorms")) {
                                        pegarImage = vImageTrovoada;
                                    } if (pegarNome.toString().equals("Partly Cloudy")) {
                                        pegarImage= vImageParNubado;
                                    } if (pegarNome.toString().equals("Mostly Cloudy")) {
                                        pegarImage= vImageParNubado;
                                    }if (pegarNome.toString().equals("Partly Sunny")) {
                                        pegarImage = vImageParEnsolarado;
                                    }if (pegarNome.toString().equals("Mostly Clear")) {
                                        pegarImage = vImageParEnsolarado;
                                    } if (pegarNome.toString().equals("Sunny")) {
                                        pegarImage = vImageEnsolarado;
                                    } if (pegarNome.toString().equals("Cloudy")) {
                                        pegarImage = vImageNublado;
                                    } if (pegarNome.toString().equals("Breezy")) {
                                        pegarImage = vImageVento;
                                    }
                                    Picasso.with(getBaseContext())
                                            .load(pegarImage)
                                            .into(img);
                                    //

                                    //Primeiro Dia
                                    lbDataSemana.setText(object12.get("date").getAsString());
                                    lbDiaSemana.setText(object12.get("day").getAsString());
                                    lbMinSemana.setText(formato.format(calcular3) + "º");
                                    lbMaxSemana.setText(formato.format(calcular4) + "º");
//                                    lbDescSemana.setText(object12.get("text").getAsString());
                                    //
                                    //Segundo Dia
                                    lbDataSemanaSeg.setText(object13.get("date").getAsString());
                                    lbDiaSemanaSeg.setText(object13.get("day").getAsString());
                                    lbMinSemanaSeg.setText(formato.format(calcular5) + "º");
                                    lbMaxSemanaSeg.setText(formato.format(calcular6) + "º");
//                                    lbDescSemanaSeg.setText(object13.get("text").getAsString());
                                    //
                                    //Terceiro Dia
                                    lbDataSemanaTer.setText(object14.get("date").getAsString());
                                    lbDiaSemanaTerc.setText(object14.get("day").getAsString());
                                    lbMinSemanaTer.setText(formato.format(calcular7) + "º");
                                    lbMaxSemanaTer.setText(formato.format(calcular8) + "º");
//                                    lbDescSemanaTer.setText(object14.get("text").getAsString());
                                    //
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "ERRO: " + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    public void onClickBuscar(View view) {

    }
}
