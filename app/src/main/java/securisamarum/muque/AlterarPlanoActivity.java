package securisamarum.muque;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AlterarPlanoActivity extends AppCompatActivity {
    private Integer idPlano;
    private Button buttonAlterarPlano;
    private TextView textViewAlterarDiasSemana;
    private TextView editTextAlterarNomePlano;
    private SeekBar seekBarAlterarDiasSemana;
    private Integer progress;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_plano);

        Intent intent = getIntent();
        idPlano = intent.getIntExtra("idPlano",0);

        buttonAlterarPlano = (Button) findViewById(R.id.buttonAlterarPlano);
        textViewAlterarDiasSemana = (TextView) findViewById(R.id.textViewAlterarDiasSemana);
        seekBarAlterarDiasSemana = (SeekBar) findViewById(R.id.seekBarAlterarDiasSemana);
        editTextAlterarNomePlano = (EditText) findViewById(R.id.editTextAlterarNomePlano);

        buttonAlterarPlano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarPlano();
            }
        });

        seekBarAlterarDiasSemana.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
                alterarLabelDiaSemana(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        carrecarPlano();
    }

    private void alterarPlano(){
        if(!editTextAlterarNomePlano.getText().toString().equals("")) {
            try {
                bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
                String sql = "UPDATE plano_de_treinamento SET nome=?, qtde_dias=? WHERE id = ?";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, editTextAlterarNomePlano.getText().toString());
                stmt.bindLong(2, progress+1);
                stmt.bindLong(3, idPlano);
                long rowId = stmt.executeInsert();
                bancoDados.close();
                Intent returnIntent = getIntent();
                setResult(RESULT_OK, returnIntent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.preencha,Snackbar.LENGTH_LONG).show();
        }

    }

    private void alterarLabelDiaSemana(Integer i){
        Integer[] dias_semana = {
                R.string.um_dia_por_semana
                , R.string.dois_dias_por_semana
                , R.string.tres_dias_por_semana
                , R.string.quatro_dias_por_semana
                , R.string.cinco_dias_por_semana
                , R.string.seis_dias_por_semana
                , R.string.sete_dias_por_semana
        };
        textViewAlterarDiasSemana.setText(dias_semana[i]);
    }

    private void carrecarPlano(){
        try {
            bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT nome, qtde_dias FROM plano_de_treinamento WHERE id = "+idPlano.toString(),null);
            if(cursor.moveToFirst()) {
                String nome = cursor.getString(cursor.getColumnIndex("nome"));
                Integer qtde_dias = cursor.getInt(cursor.getColumnIndex("qtde_dias"));
                editTextAlterarNomePlano.setText(nome);
                seekBarAlterarDiasSemana.setProgress(qtde_dias-1);
                alterarLabelDiaSemana(qtde_dias-1);
                progress = qtde_dias-1;
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
