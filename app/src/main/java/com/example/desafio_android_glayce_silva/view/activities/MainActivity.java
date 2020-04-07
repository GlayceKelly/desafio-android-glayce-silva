package com.example.desafio_android_glayce_silva.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.desafio_android_glayce_silva.R;
import com.example.desafio_android_glayce_silva.model.characters.Result;
import com.example.desafio_android_glayce_silva.view.adapter.MarvelRecyclerViewAdapter;
import com.example.desafio_android_glayce_silva.view.interfaces.OnClick;
import com.example.desafio_android_glayce_silva.viewmodel.MarvelViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements OnClick {
    //Controles da classe
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    //Variaveis da classe
    private MarvelViewModel viewModel;
    private List<Result> listaResults = new ArrayList<>();
    private MarvelRecyclerViewAdapter adapter;
    private int pagina = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            initViews();
            setScrollView();
            carregaDados();

        } catch (Exception err) {
            Log.e("ERRO", "Erro MainActivity: " + err.getMessage());
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        viewModel = ViewModelProviders.of(this).get(MarvelViewModel.class);
        adapter = new MarvelRecyclerViewAdapter(listaResults, this);
    }

    private void carregaDados() {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Realiza a requisicao e obtem os personagens
        viewModel.getPersonagens(1);

        //Obtem a lista de personagens e atualiza a lista
        viewModel.getListaPersonagens().observe(this, resultadoLista -> {
            adapter.atualizaLista(resultadoLista);
        });

        viewModel.getLoading().observe(this, loading -> {
            if (loading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void click(Result result) {
        Intent intent = new Intent(MainActivity.this, DetalheActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Result", result);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Método que realiza a páginação noi scroll do recyclerView
    private void setScrollView(){

        //Adicionamos o listener no recyclerView e sobrescrevemos os métodos do listener do recyclerview
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //Método que ouve o status de mudança do recyclerview
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            //Método que realmente aconte o scroll do recyclerview e aqui acontece a páginação
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //Recuperamos o layout setado no  recyclerview em uma nova instancia do layout definido
                //Nesse caso estou trabalhando com o LinearLayoutManager
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                //variavel que irá receber o retorno da quantidade de itens no adapter vinculado ao RecyclerView
                int totalItemCount = layoutManager.getItemCount();

                //Variavel que irá receber a posição do adapter do último item visível.
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                //Variavel que um verdadeiro ou falso caso o valor da posição do ultimo item + 5
                // seja maior ou igual que a quantidade de intens total na lista
                boolean ultimoItem = lastVisible + 5 >= totalItemCount;

                //Verificação se a quantidade total de itens na lista for maior que zero e se o valor da variavel ultimoItem é verdadeiro
                //Se a condição for verdadeira o atributo de pagina irá incrementar mais 1 e chamamos o viewmodel solicitando novos dados para a api
                if (totalItemCount > 0 && ultimoItem) {
                    pagina++;
                    viewModel.getPersonagens(pagina);
                }
            }
        });
    }
}