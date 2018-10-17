package securisamarum.muque;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DiaActivity extends AppCompatActivity {
    private Integer idPlano, dia, exercId;
    private SQLiteDatabase bancoDados;
    private ListView listViewExercicios;
    private ArrayList<Integer> arrayIdExerc;
    private List<Exercicio> exercicios;
    private ExercicioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarExercicio();
            }
        });

        listViewExercicios = (ListView) findViewById(R.id.listViewExercicios);

        Intent intent = getIntent();
        idPlano = intent.getIntExtra("idPlano",0);
        dia = intent.getIntExtra("dia",0);

        listarExercicios();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarExercicios();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // Criar Exercício
            if (resultCode == RESULT_OK) {
                Snackbar.make(findViewById(android.R.id.content), R.string.exercicio_criado,Snackbar.LENGTH_LONG).show();
            }
        }
        if (requestCode == 2) { // Editar Exercício
            if (resultCode == RESULT_OK) {
                Snackbar.make(findViewById(android.R.id.content), R.string.exercicio_editado,Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void criarExercicio(){
        Intent intent = new Intent(this,CriarExercicioActivity.class);
        intent.putExtra("idPlano",idPlano);
        intent.putExtra("dia",dia);
        intent.putExtra("opcao",1);//opcao=1 -> INSERIR
        startActivityForResult(intent,1);
    }

    private void listarExercicios(){

        arrayIdExerc = new ArrayList<Integer>();
        exercicios = new ArrayList();
        adapter = new ExercicioAdapter(
                this
                , R.layout.linha_exercicio
                ,exercicios
        );
        listViewExercicios.setAdapter(adapter);
        try {
            bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,id_plano_de_treinamento,dia_de_treinamento,id_tipo_exercicio,id_grupo_muscular,nome,posicao,carga,series,repeticoes,tempo_min,distancia_km FROM exercicio WHERE id_plano_de_treinamento = "+idPlano.toString()+" AND dia_de_treinamento = "+dia+" ORDER BY posicao",null);

            if(cursor.moveToFirst()) {
                do {
                    if(cursor.getInt(cursor.getColumnIndex("id_tipo_exercicio"))==1) {
                        Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                        Integer id_tipo_exercicio = 1;
                        Integer posicao = cursor.getInt(cursor.getColumnIndex("posicao"));
                        Integer carga = cursor.getInt(cursor.getColumnIndex("carga"));
                        Integer series = cursor.getInt(cursor.getColumnIndex("series"));
                        Integer repeticoes = cursor.getInt(cursor.getColumnIndex("repeticoes"));
                        String nome = cursor.getString(cursor.getColumnIndex("nome"));
                        Integer id_grupo_muscular = cursor.getInt(cursor.getColumnIndex("id_grupo_muscular"));
                        Exercicio exercicio = new Exercicio(id, id_tipo_exercicio,id_grupo_muscular, posicao, carga, series, repeticoes, nome);
                        arrayIdExerc.add(id);
                        exercicios.add(exercicio);
                    } else if(cursor.getInt(cursor.getColumnIndex("id_tipo_exercicio"))==2) {
                        Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                        Integer id_tipo_exercicio = 2;
                        Integer posicao = cursor.getInt(cursor.getColumnIndex("posicao"));
                        Integer tempo_min = cursor.getInt(cursor.getColumnIndex("tempo_min"));
                        Integer distancia_km = cursor.getInt(cursor.getColumnIndex("distancia_km"));
                        String nome = cursor.getString(cursor.getColumnIndex("nome"));
                        Integer id_grupo_muscular = cursor.getInt(cursor.getColumnIndex("id_grupo_muscular"));
                        Exercicio exercicio = new Exercicio(id, id_tipo_exercicio,id_grupo_muscular, posicao, tempo_min, distancia_km, nome);
                        arrayIdExerc.add(id);
                        exercicios.add(exercicio);
                    }

                }while(cursor.moveToNext());
            }
            bancoDados.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void excluirExercicio(Integer id){
        exercId = id;
        AlertDialog.Builder msgBox = new AlertDialog.Builder(DiaActivity.this);
        msgBox.setTitle(R.string.excluir);
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage(R.string.certeza_excluir_exercicio);
        msgBox.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);

                    String sql = "UPDATE exercicio SET posicao= posicao-1 WHERE id_plano_de_treinamento = ? AND dia_de_treinamento = ? AND posicao > (SELECT posicao FROM exercicio WHERE id = ?)";
                    SQLiteStatement stmt = bancoDados.compileStatement(sql);
                    stmt.bindLong(1, idPlano);
                    stmt.bindLong(2, dia);
                    stmt.bindLong(3, exercId);
                    long rowId = stmt.executeUpdateDelete();


                    sql = "DELETE from exercicio WHERE id = ?";
                    stmt = bancoDados.compileStatement(sql);
                    stmt.bindLong(1, exercId);
                    rowId = stmt.executeInsert();
                    bancoDados.close();
                    Snackbar.make(findViewById(android.R.id.content), R.string.exercicio_excluido,Snackbar.LENGTH_LONG).show();
                    listarExercicios();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        msgBox.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        msgBox.show();
    }

    public void editarExercicio(Integer id) {
        Intent intent = new Intent(this,CriarExercicioActivity.class);
        intent.putExtra("idPlano",idPlano);
        intent.putExtra("dia",dia);
        intent.putExtra("opcao",2);//opcao=2 -> EDITAR
        intent.putExtra("idExerc",id);
        startActivityForResult(intent,2);
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
            bancoDados.execSQL("UPDATE exercicio SET posicao = -1 WHERE id_plano_de_treinamento = "+idPlano.toString()+" AND dia_de_treinamento = "+dia.toString()+" AND posicao = "+posicao1.toString());
            bancoDados.execSQL("UPDATE exercicio SET posicao = "+posicao1.toString()+" WHERE id_plano_de_treinamento = "+idPlano.toString()+" AND dia_de_treinamento = "+dia.toString()+" AND posicao = "+posicao2.toString());
            bancoDados.execSQL("UPDATE exercicio SET posicao = "+posicao2.toString()+" WHERE  id_plano_de_treinamento = "+idPlano.toString()+" AND dia_de_treinamento = "+dia.toString()+" AND posicao = -1");
            bancoDados.close();
            listarExercicios();
            Snackbar.make(findViewById(android.R.id.content), R.string.ordem_alterada,Snackbar.LENGTH_LONG).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
