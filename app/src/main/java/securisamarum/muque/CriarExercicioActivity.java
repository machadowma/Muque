package securisamarum.muque;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabWidget;
import android.widget.Toast;

public class CriarExercicioActivity extends AppCompatActivity {
    private RadioGroup radioGroupNovoExerc;
    private LinearLayout NovoExercLayoutMusc,NovoExercLayoutCardio;
    private Button buttonCriarExerc;
    private EditText editTextNovoExercNome,editTextNovoExercCarga,editTextNovoExercSeries,editTextNovoExercRepeticoes,editTextNovoExercTempo,editTextNovoExercDistancia;
    private RadioButton radioButtonNovoExercMusc,radioButtonNovoExercCardio;
    private Spinner NovoExercSpinnerGrupoMusc;
    private SQLiteDatabase bancoDados;
    Integer idPlano, dia, opcao, idExerc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_exercicio);

        Intent intent = getIntent();
        idPlano = intent.getIntExtra("idPlano",0);
        dia = intent.getIntExtra("dia",0);
        opcao = intent.getIntExtra("opcao",0);
        idExerc = intent.getIntExtra("idExerc",0);


        radioGroupNovoExerc = (RadioGroup) findViewById(R.id.radioGroupNovoExerc);
        NovoExercLayoutMusc = (LinearLayout) findViewById(R.id.NovoExercLayoutMusc);
        NovoExercLayoutCardio = (LinearLayout) findViewById(R.id.NovoExercLayoutCardio);
        buttonCriarExerc = (Button) findViewById(R.id.buttonCriarExerc);
        radioButtonNovoExercMusc = (RadioButton) findViewById(R.id.radioButtonNovoExercMusc);
        radioButtonNovoExercCardio = (RadioButton) findViewById(R.id.radioButtonNovoExercCardio);
        editTextNovoExercNome = (EditText) findViewById(R.id.editTextNovoExercNome);
        editTextNovoExercCarga = (EditText) findViewById(R.id.editTextNovoExercCarga);
        editTextNovoExercSeries = (EditText) findViewById(R.id.editTextNovoExercSeries);
        editTextNovoExercRepeticoes = (EditText) findViewById(R.id.editTextNovoExercRepeticoes);
        editTextNovoExercTempo = (EditText) findViewById(R.id.editTextNovoExercTempo);
        editTextNovoExercDistancia = (EditText) findViewById(R.id.editTextNovoExercDistancia);
        NovoExercSpinnerGrupoMusc = (Spinner) findViewById(R.id.NovoExercSpinnerGrupoMusc);

        String[] array_grupos={"Abdômen","Membros Inferiores","Peito","Tríceps","Bíceps","Costas","Ombro","Trapézio"};
        Spinner spin = (Spinner) findViewById(R.id.NovoExercSpinnerGrupoMusc);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,array_grupos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        radioGroupNovoExerc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    if(checkedRadioButton.getId()==R.id.radioButtonNovoExercMusc){
                        NovoExercLayoutMusc.setVisibility(LinearLayout.VISIBLE);
                        NovoExercLayoutCardio.setVisibility(LinearLayout.GONE );
                    } else if(checkedRadioButton.getId()==R.id.radioButtonNovoExercCardio){
                        NovoExercLayoutMusc.setVisibility(LinearLayout.GONE );
                        NovoExercLayoutCardio.setVisibility(LinearLayout.VISIBLE);
                    }
                }
            }
        });

        buttonCriarExerc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opcao==1){
                    criarExercicio();
                } else if (opcao==2){
                    alterarExercicio();
                }

            }
        });

        if (opcao==2){
            carregarExercicio();
        }
    }

    private void criarExercicio(){
        if(editTextNovoExercNome.getText().toString().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), R.string.preencha, Snackbar.LENGTH_LONG).show();
            return;
        } else {
            try {
                bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
                Cursor cursor = bancoDados.rawQuery("SELECT MAX(posicao) as maxposicao FROM exercicio WHERE id_plano_de_treinamento = " + idPlano.toString() + " AND dia_de_treinamento = " + dia.toString(), null);
                cursor.moveToFirst();
                Integer proximaPosicao = cursor.getInt(0) + 1;
                if (radioButtonNovoExercMusc.isChecked()) {
                    if (editTextNovoExercCarga.getText().toString().equals("") && editTextNovoExercSeries.getText().toString().equals("") && editTextNovoExercRepeticoes.getText().toString().equals("")) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.preencha, Snackbar.LENGTH_LONG).show();
                        return;
                    } else {
                        String sql = "INSERT INTO exercicio (id_plano_de_treinamento,dia_de_treinamento,id_tipo_exercicio,nome,posicao,carga,series,repeticoes,id_grupo_muscular) VALUES (?,?,?,?,?,?,?,?,?)";
                        SQLiteStatement stmt = bancoDados.compileStatement(sql);
                        stmt.bindLong(1, idPlano);
                        stmt.bindLong(2, dia);
                        stmt.bindLong(3, 1);
                        stmt.bindString(4, editTextNovoExercNome.getText().toString());
                        stmt.bindLong(5, Integer.parseInt(proximaPosicao.toString()));
                        if(!editTextNovoExercCarga.getText().toString().equals("")) {
                            stmt.bindLong(6, Integer.parseInt(editTextNovoExercCarga.getText().toString()));
                        }
                        if(!editTextNovoExercSeries.getText().toString().equals("")) {
                            stmt.bindLong(7, Integer.parseInt(editTextNovoExercSeries.getText().toString()));
                        }
                        if(!editTextNovoExercRepeticoes.getText().toString().equals("")) {
                            stmt.bindLong(8, Integer.parseInt(editTextNovoExercRepeticoes.getText().toString()));
                        }
                        stmt.bindLong(9, 1+NovoExercSpinnerGrupoMusc.getSelectedItemPosition());
                        long rowId = stmt.executeInsert();
                        bancoDados.close();
                        Intent returnIntent = getIntent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                } else if (radioButtonNovoExercCardio.isChecked()) {
                    if (editTextNovoExercTempo.getText().toString().equals("") && editTextNovoExercDistancia.getText().toString().equals("")) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.preencha, Snackbar.LENGTH_LONG).show();
                        return;
                    } else {
                        String sql = "INSERT INTO exercicio (id_plano_de_treinamento,dia_de_treinamento,id_tipo_exercicio,nome,posicao,tempo_min,distancia_km) VALUES (?,?,?,?,?,?,?)";
                        SQLiteStatement stmt = bancoDados.compileStatement(sql);
                        stmt.bindLong(1, idPlano);
                        stmt.bindLong(2, dia);
                        stmt.bindLong(3, 2);
                        stmt.bindString(4, editTextNovoExercNome.getText().toString());
                        stmt.bindLong(5, Integer.parseInt(proximaPosicao.toString()));
                        if(!editTextNovoExercTempo.getText().toString().equals("")) {
                            stmt.bindLong(6, Integer.parseInt(editTextNovoExercTempo.getText().toString()));
                        }
                        if(!editTextNovoExercDistancia.getText().toString().equals("")) {
                            stmt.bindLong(7, Integer.parseInt(editTextNovoExercDistancia.getText().toString()));
                        }
                        long rowId = stmt.executeInsert();
                        bancoDados.close();
                        Intent returnIntent = getIntent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void alterarExercicio(){
        if(editTextNovoExercNome.getText().toString().equals("")) {
            Snackbar.make(findViewById(android.R.id.content), R.string.preencha, Snackbar.LENGTH_LONG).show();
            return;
        } else {
            try {
                bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
                if (radioButtonNovoExercMusc.isChecked()) {
                    if (editTextNovoExercCarga.getText().toString().equals("") && editTextNovoExercSeries.getText().toString().equals("") && editTextNovoExercRepeticoes.getText().toString().equals("")) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.preencha, Snackbar.LENGTH_LONG).show();
                        return;
                    } else {
                        String sql = "UPDATE exercicio SET id_tipo_exercicio=?,nome=?,carga=?,series=?,repeticoes=?,id_grupo_muscular=? WHERE id=?";
                        SQLiteStatement stmt = bancoDados.compileStatement(sql);
                        stmt.bindLong(1, 1);
                        stmt.bindString(2, editTextNovoExercNome.getText().toString());
                        if(!editTextNovoExercCarga.getText().toString().equals("")) {
                            stmt.bindLong(3, Integer.parseInt(editTextNovoExercCarga.getText().toString()));
                        }
                        if(!editTextNovoExercSeries.getText().toString().equals("")) {
                            stmt.bindLong(4, Integer.parseInt(editTextNovoExercSeries.getText().toString()));
                        }
                        if(!editTextNovoExercRepeticoes.getText().toString().equals("")) {
                            stmt.bindLong(5, Integer.parseInt(editTextNovoExercRepeticoes.getText().toString()));
                        }
                        stmt.bindLong(6, 1+NovoExercSpinnerGrupoMusc.getSelectedItemPosition());
                        stmt.bindLong(7, idExerc);
                        long rowId = stmt.executeUpdateDelete();
                        bancoDados.close();
                        Intent returnIntent = getIntent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                } else if (radioButtonNovoExercCardio.isChecked()) {
                    if (editTextNovoExercTempo.getText().toString().equals("") && editTextNovoExercDistancia.getText().toString().equals("")) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.preencha, Snackbar.LENGTH_LONG).show();
                        return;
                    } else {
                        String sql = "UPDATE exercicio SET id_tipo_exercicio=?,nome=?,tempo_min=?,distancia_km=? WHERE id=?";
                        SQLiteStatement stmt = bancoDados.compileStatement(sql);
                        stmt.bindLong(1, 2);
                        stmt.bindString(2, editTextNovoExercNome.getText().toString());
                        if(!editTextNovoExercTempo.getText().toString().equals("")) {
                            stmt.bindLong(3, Integer.parseInt(editTextNovoExercTempo.getText().toString()));
                        }
                        if(!editTextNovoExercDistancia.getText().toString().equals("")) {
                            stmt.bindLong(4, Integer.parseInt(editTextNovoExercDistancia.getText().toString()));
                        }
                        stmt.bindLong(5, idExerc);
                        long rowId = stmt.executeUpdateDelete();
                        bancoDados.close();
                        Intent returnIntent = getIntent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void carregarExercicio(){
        try {
            bancoDados = openOrCreateDatabase("muque", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT id,id_plano_de_treinamento,dia_de_treinamento,id_tipo_exercicio,id_grupo_muscular,nome,posicao,carga,series,repeticoes,tempo_min,distancia_km FROM exercicio WHERE id = "+idExerc,null);
            if(cursor.moveToFirst()) {
                editTextNovoExercNome.setText(cursor.getString(cursor.getColumnIndex("nome")));
                editTextNovoExercCarga.setText(cursor.getString(cursor.getColumnIndex("carga")));
                editTextNovoExercSeries.setText(cursor.getString(cursor.getColumnIndex("series")));
                editTextNovoExercRepeticoes.setText(cursor.getString(cursor.getColumnIndex("repeticoes")));
                editTextNovoExercTempo.setText(cursor.getString(cursor.getColumnIndex("tempo_min")));
                editTextNovoExercDistancia.setText(cursor.getString(cursor.getColumnIndex("distancia_km")));
                if(cursor.getInt(cursor.getColumnIndex("id_tipo_exercicio"))==1){
                    NovoExercSpinnerGrupoMusc.setSelection(cursor.getInt(cursor.getColumnIndex("id_grupo_muscular"))-1);
                    NovoExercLayoutMusc.setVisibility(LinearLayout.VISIBLE);
                    NovoExercLayoutCardio.setVisibility(LinearLayout.GONE );
                    radioButtonNovoExercMusc.setChecked(true);
                } else if(cursor.getInt(cursor.getColumnIndex("id_tipo_exercicio"))==2){
                    NovoExercLayoutMusc.setVisibility(LinearLayout.GONE);
                    NovoExercLayoutCardio.setVisibility(LinearLayout.VISIBLE );
                    radioButtonNovoExercCardio.setChecked(true);
                }
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
