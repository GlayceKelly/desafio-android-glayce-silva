package com.example.desafio_android_glayce_silva.view.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.desafio_android_glayce_silva.R;
import com.example.desafio_android_glayce_silva.model.comicsid.Price;
import com.example.desafio_android_glayce_silva.model.comicsid.Result;
import com.example.desafio_android_glayce_silva.viewmodel.MarvelViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class HqMaisCaraActivity extends AppCompatActivity {
    //Controles da classe
    private ImageView imagem = null;
    private TextView txtTitulo = null;
    private TextView txtDescricao = null;
    private TextView txtPreco = null;
    private ProgressBar progressBar;

    //Variaveis da classe
    private Long idPersonagem;
    private MarvelViewModel viewModel;
    private List<Result> listaResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_hq_mais_cara);

            initViews();
            carregaDados();
        }
        catch (Exception err) {
            Log.e("ERRO", "Erro HqMaisCaraActivity: " + err.getMessage());
        }
    }

    private void initViews() {
        imagem = findViewById(R.id.imgDetalhe);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescricao = findViewById(R.id.txtDescricaoHq);
        txtPreco = findViewById(R.id.txtPrecoHq);
        progressBar = findViewById(R.id.progressBar);
        viewModel = ViewModelProviders.of(this).get(MarvelViewModel.class);
    }

    private void carregaDados() {
        //Obtem o resulta da view anterior
        com.example.desafio_android_glayce_silva.model.characters.Result result = getIntent().getParcelableExtra("Result");
        idPersonagem = result.getId();

        //Realiza a requisicao passando o id do personagem
        viewModel.getComics(idPersonagem);

        //Obtem a lista de comics e carrega a hq mais cara
        viewModel.getListaComics().observe(this, resultadoLista -> {
            listaResult = resultadoLista;

            carregaDadosHqMaisCara(listaResult);
        });

        //Apresenta/remove o loading
        viewModel.getLoading().observe(this, loading -> {
            if (loading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void carregaDadosHqMaisCara(List<Result> listResultParam) {
        //Variaveis do metodo
        List<Price> listaPrice = new ArrayList<>();
        double maxValor = 0;
        int index = -1;
        double price = 0;

        //Percorre o array de result do comics
        for(int iCountResults = 0 ; iCountResults < listResultParam.size(); iCountResults++) {
            //Obtem a lista de precos
            listaPrice = listResultParam.get(iCountResults).getPrices();

            //Recupera o primeiro valor da lista
            maxValor = Double.parseDouble(listResultParam.get(0).getPrices().get(0).getPrice());
            index = 0;

            //Percorre o array de precos
            for( int iCountPrices = 0; iCountPrices < listaPrice.size(); iCountPrices++) {
                price = Double.parseDouble(listaPrice.get(iCountPrices).getPrice());

                if( price > maxValor) {
                    maxValor = price;
                    index = iCountResults;
                }
            }
        }

        //Preenche os controles da tela com as informacoes
        Picasso.get().load(listResultParam.get(index).getThumbnail().getPath() + ".jpg").into(imagem);
        txtTitulo.setText(listResultParam.get(index).getTitle());
        txtDescricao.setText(listResultParam.get(index).getDescription());
        txtPreco.setText(maxValor + "$");
    }
}