package securisamarum.muque;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private ArrayList<Integer> arrayIds;
    private ListView listaPlanos;
    private List<Plano> planos;
    private PlanoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaPlanos = (ListView) findViewById(R.id.listaPlanos);
        View footerView = getLayoutInflater().inflate(R.layout.footer_layout, null);
        listaPlanos.addFooterView(footerView);

        FloatingActionButton fab_incluir_plano = (FloatingActionButton) findViewById(R.id.fab_incluir_plano);
        fab_incluir_plano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                criarPlano();
            }
        });

        listaPlanos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                abrirPlano(arrayIds.get(i));
            }
        });

        criarBancoDados();
        listarPlanos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarPlanos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // Criar Plano de Treinamento
            if (resultCode == RESULT_OK) {
                Snackbar.make(findViewById(android.R.id.content), R.string.plano_criado,Snackbar.LENGTH_LONG).show();
            }
        }
        if (requestCode == 2) { // Abrir Plano de Treinamento
            if (resultCode == 1) { // Excluir
                Snackbar.make(findViewById(android.R.id.content), R.string.plano_excluido,Snackbar.LENGTH_LONG).show();
            }
        }
    }


    private void criarBancoDados() {
        try {

            bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
/*
            bancoDados.execSQL("DROP TABLE exercicio");
            bancoDados.execSQL("DROP TABLE grupo_muscular");
            bancoDados.execSQL("DROP TABLE tipo_exercicio");
            bancoDados.execSQL("DROP TABLE plano_de_treinamento");
*/
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS plano_de_treinamento(" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR" +
                    ", qtde_dias INTEGER" +
                    ", posicao INTEGER)");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tipo_exercicio(" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR)");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS grupo_muscular(" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR)");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS exercicio(" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", id_plano_de_treinamento INTEGER" +
                    ", dia_de_treinamento INTEGER" +
                    ", id_tipo_exercicio" +
                    ", id_grupo_muscular" +
                    ", nome VARCHAR" +
                    ", posicao INTEGER" +
                    ", carga INTEGER" +
                    ", series INTEGER" +
                    ", repeticoes INTEGER" +
                    ", tempo_min INTEGER" +
                    ", distancia_km INTEGER" +
                    ", FOREIGN KEY (id_plano_de_treinamento) REFERENCES plano_de_treinamento(id)" +
                    ", FOREIGN KEY (id_tipo_exercicio) REFERENCES tipo_exercicio(id)" +
                    ", FOREIGN KEY (id_grupo_muscular) REFERENCES grupo_muscular(id))");

            Cursor cursor = bancoDados.rawQuery("SELECT id FROM tipo_exercicio WHERE id = 1",null);
            if(!cursor.moveToFirst()) {
                /*01*/ bancoDados.execSQL("INSERT INTO tipo_exercicio(nome) VALUES('Musculação')");
                /*02*/ bancoDados.execSQL("INSERT INTO tipo_exercicio(nome) VALUES('Cardio')");
            }
            cursor = bancoDados.rawQuery("SELECT id FROM grupo_muscular WHERE id = 1",null);
            if(!cursor.moveToFirst()){
                /*01*/ bancoDados.execSQL("INSERT INTO grupo_muscular(nome) VALUES('Abdômen')");
                /*02*/ bancoDados.execSQL("INSERT INTO grupo_muscular(nome) VALUES('Membros Inferiores')");
                /*03*/ bancoDados.execSQL("INSERT INTO grupo_muscular(nome) VALUES('Peito')");
                /*04*/ bancoDados.execSQL("INSERT INTO grupo_muscular(nome) VALUES('Tríceps')");
                /*05*/ bancoDados.execSQL("INSERT INTO grupo_muscular(nome) VALUES('Bíceps')");
                /*06*/ bancoDados.execSQL("INSERT INTO grupo_muscular(nome) VALUES('Costas')");
                /*07*/ bancoDados.execSQL("INSERT INTO grupo_muscular(nome) VALUES('Ombro')");
                /*08*/ bancoDados.execSQL("INSERT INTO grupo_muscular(nome) VALUES('Trapézio')");
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listarPlanos(){
        arrayIds = new ArrayList<Integer>();
        planos = new ArrayList();
        adapter = new PlanoAdapter(
                this
                , R.layout.linha_plano
                ,planos
        );
        listaPlanos.setAdapter(adapter);
        try {
            bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id, nome, posicao FROM plano_de_treinamento ORDER BY posicao",null);
            if(cursor.moveToFirst()) {
                do {

                    Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                    String nome = cursor.getString(cursor.getColumnIndex("nome"));
                    Integer posicao = cursor.getInt(cursor.getColumnIndex("posicao"));
                    Plano plano = new Plano(id, nome, posicao);
                    planos.add(plano);
                    arrayIds.add(cursor.getInt(cursor.getColumnIndex("id")));
                } while (cursor.moveToNext());
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void criarPlano(){
        Intent intent = new Intent(this,CriarPlanoActivity.class);
        startActivityForResult(intent,1);
    }


    public void subir(Integer posicao){
        if(posicao>1){
            trocarPosicao(posicao, posicao-1);
        }
    }

    public void descer(Integer posicao){
        if(posicao<adapter.getCount()){
            trocarPosicao(posicao,posicao+1);
        }
    }

    public void trocarPosicao(Integer posicao1, Integer posicao2) {
        try {
            bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
            bancoDados.execSQL("UPDATE plano_de_treinamento SET posicao = -1 WHERE posicao = "+posicao1.toString());
            bancoDados.execSQL("UPDATE plano_de_treinamento SET posicao = "+posicao1.toString()+" WHERE posicao = "+posicao2.toString());
            bancoDados.execSQL("UPDATE plano_de_treinamento SET posicao = "+posicao2.toString()+" WHERE posicao = -1");
            bancoDados.close();
            listarPlanos();
            Snackbar.make(findViewById(android.R.id.content), R.string.ordem_alterada,Snackbar.LENGTH_LONG).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void abrirPlano(Integer idPlano){
        Intent intent = new Intent(this,GerenciarPlanoActivity.class);
        intent.putExtra("idPlano",idPlano);
        startActivityForResult(intent,2);
    }
}
