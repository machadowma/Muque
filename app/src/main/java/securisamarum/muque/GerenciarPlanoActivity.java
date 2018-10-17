package securisamarum.muque;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GerenciarPlanoActivity extends AppCompatActivity {
    private TextView textViewNomePlano;
    private Integer idPlano;
    private SQLiteDatabase bancoDados;
    private ImageButton buttonExcluirPlano, buttonEditarPlano;
    private ListView listViewListaDiasPlano;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_plano);

        textViewNomePlano = (TextView) findViewById(R.id.textViewNomePlano);
        buttonExcluirPlano = (ImageButton) findViewById(R.id.buttonExcluirPlano);
        buttonEditarPlano = (ImageButton) findViewById(R.id.buttonEditarPlano);
        listViewListaDiasPlano = (ListView) findViewById(R.id.listViewListaDiasPlano);

        Intent intent = getIntent();
        idPlano = intent.getIntExtra("idPlano",0);

        buttonExcluirPlano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(GerenciarPlanoActivity.this);
                View my_view = getLayoutInflater().inflate(R.layout.dialog_excluir_plano, null);
                builder.setView(my_view);
                builder.setTitle(R.string.excluir);
                builder.setIcon(android.R.drawable.ic_menu_delete);
                final RadioButton radioButtonCertezaExcluir = (RadioButton) my_view.findViewById(R.id.radioButtonCertezaExcluirPlano);
                Button buttonCertezaExcluir = (Button) my_view.findViewById(R.id.buttonCertezaExcluirPlano);
                Button buttonCancelaExcluir = (Button) my_view.findViewById(R.id.buttonCancelaExcluirPlano);
                final AlertDialog dialog = builder.create();
                buttonCancelaExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                buttonCertezaExcluir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(radioButtonCertezaExcluir.isChecked()) {
                            removerPlano();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        listViewListaDiasPlano.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                abrirDia(i+1);
            }
        });

        buttonEditarPlano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarPlano();
            }
        });

        carregarPlano();

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarPlano();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // Criar Plano de Treinamento
            if (resultCode == RESULT_OK) {
                Snackbar.make(findViewById(android.R.id.content), R.string.plano_alterado,Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void removerPlano(){
        try {
            bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);

            String sql = "UPDATE plano_de_treinamento SET posicao= posicao-1 WHERE posicao > (SELECT posicao FROM plano_de_treinamento WHERE id = ?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, idPlano);
            long rowId = stmt.executeUpdateDelete();

            sql = "DELETE FROM plano_de_treinamento WHERE id =  ?";
            stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, idPlano);
            rowId = stmt.executeUpdateDelete();

            bancoDados.close();
            Intent returnIntent = getIntent();
            setResult(1, returnIntent);
            finish();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void alterarPlano() {
        Intent intent = new Intent(this, AlterarPlanoActivity.class);
        intent.putExtra("idPlano", idPlano);
        startActivityForResult(intent, 1);
    }

    private void carregarPlano(){
        try {
            bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT nome,qtde_dias FROM plano_de_treinamento WHERE id = "+idPlano.toString(),null);
            if(cursor.moveToFirst()) {
                textViewNomePlano.setText(cursor.getString(cursor.getColumnIndex("nome")));
                Integer qtde_dias = cursor.getInt(cursor.getColumnIndex("qtde_dias"));
                ArrayList<String> arrayDias = new ArrayList<String>(qtde_dias);
                ArrayAdapter adapter = new ArrayAdapter<String>(
                        getApplicationContext()
                        , android.R.layout.simple_list_item_2
                        , android.R.id.text1
                        , arrayDias
                );
                listViewListaDiasPlano.setAdapter(adapter);

                for(int i=1;i<=qtde_dias;i++){
                    arrayDias.add("Dia: "+i);
                }
            }
            bancoDados.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirDia(Integer dia) {
        Intent intent = new Intent(this, DiaActivity.class);
        intent.putExtra("idPlano", idPlano);
        intent.putExtra("dia", dia);
        startActivity(intent);
    }
}
