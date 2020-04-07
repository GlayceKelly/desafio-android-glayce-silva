package com.example.desafio_android_glayce_silva.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.desafio_android_glayce_silva.R;
import com.example.desafio_android_glayce_silva.model.characters.Result;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class DetalheActivity extends AppCompatActivity {
    //Controles da classe
    private ImageView imgBanner;
    private TextView txtTitulo;
    private TextView txtDescricao;
    private Button btnHq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detalhe);

            initViews();
            carregaDados();
        } catch (Exception err) {
            Log.e("ERRO", "Erro DetalheActivity: " + err.getMessage());
        }
    }

    private void initViews() {
        imgBanner = findViewById(R.id.imgBanner);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescricao = findViewById(R.id.txtDescricao);
        btnHq = findViewById(R.id.btnHq);
    }

    private void carregaDados() {
        if (getIntent() != null) {
            //Obtem o result da view anterior e define os dados nos controles da tela
            Result result = getIntent().getParcelableExtra("Result");
            Picasso.get().load(result.getThumbnail().getPath() + ".jpg").into(imgBanner);
            txtTitulo.setText(result.getName());
            txtDescricao.setText(result.getDescription());

            btnHq.setOnClickListener(v -> {
                Intent intent = new Intent(DetalheActivity.this, HqMaisCaraActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Result", result);
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }
    }
}