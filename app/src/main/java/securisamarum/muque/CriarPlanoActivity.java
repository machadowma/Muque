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
import android.widget.Toast;

public class CriarPlanoActivity extends AppCompatActivity {
    private EditText editTextNomePlano;
    private Button buttonCriarPlano;
    private SQLiteDatabase bancoDados;
    private SeekBar seekBarDiasSemana;
    private TextView textViewDiasSemana;
    private Integer progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_plano);



        progress = 0;

        editTextNomePlano = (EditText) findViewById(R.id.editTextNomePlano);
        buttonCriarPlano = (Button) findViewById(R.id.buttonCriarPlano);
        seekBarDiasSemana = (SeekBar) findViewById(R.id.seekBarDiasSemana);
        textViewDiasSemana = (TextView) findViewById(R.id.textViewDiasSemana);


        buttonCriarPlano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarPlano();
            }
        });

        seekBarDiasSemana.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
    }

    private void criarPlano(){
        if(!editTextNomePlano.getText().toString().equals("")) {
            try {
                bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
                Cursor cursor = bancoDados.rawQuery("SELECT MAX(posicao) as maxposicao FROM plano_de_treinamento",null);
                cursor.moveToFirst();
                Integer proximaPosicao = cursor.getInt(0)+1;
                String sql = "INSERT INTO plano_de_treinamento (nome,posicao,qtde_dias) VALUES (?,?,?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, editTextNomePlano.getText().toString());
                stmt.bindLong(2, proximaPosicao);
                stmt.bindLong(3, progress+1);
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
        textViewDiasSemana.setText(dias_semana[i]);
    }
}
