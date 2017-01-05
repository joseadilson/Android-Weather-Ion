package com.example.jose.temperatura_ion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class PesquisarCidadeActivity extends AppCompatActivity {

    private EditText edNomeCidade;
    String pegarCidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_cidade);

        edNomeCidade = (EditText)findViewById(R.id.edPegarCidade);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_city, menu);
        return true;
    }

    public void menuCliqueExcluir(MenuItem item) {
        pegarCidade = edNomeCidade.getText().toString();
        Intent it = new Intent(PesquisarCidadeActivity.this, MainActivity.class);
        it.putExtra("NOME_CIDADE", pegarCidade.toString());
        startActivity(it);
    }
}
