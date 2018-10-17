package securisamarum.muque;

import android.content.Context;
import android.content.res.Resources;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ExercicioAdapter extends BaseAdapter {
    private final List<Exercicio> exercicios;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public ExercicioAdapter(Context context, int resource, List<Exercicio> exercicios ) {
        this.context = context;
        this.resource = resource;
        this.exercicios = exercicios;
    }

    @Override
    public int getCount() {
        return exercicios.size();
    }

    @Override
    public Exercicio getItem(int position) {
        return exercicios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater  layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        final Exercicio exercicio = exercicios.get(position);

        String str = exercicio.getNome();
        TextView linhaExercTextViewNome = (TextView) view.findViewById(R.id.linhaExercTextViewNome);
        linhaExercTextViewNome.setText(str);

        if(exercicio.getId_tipo_exercicio()==1) {
            LinearLayout linhaExercLayoutTipo = (LinearLayout) view.findViewById(R.id.linhaExercLayoutTipo);
            LinearLayout linhaExercLayoutCarga = (LinearLayout) view.findViewById(R.id.linhaExercLayoutCarga);
            LinearLayout linhaExercLayoutSeries = (LinearLayout) view.findViewById(R.id.linhaExercLayoutSeries);
            LinearLayout linhaExercLayoutRepeticoes = (LinearLayout) view.findViewById(R.id.linhaExercLayoutRepeticoes);
            TextView linhaExercTextViewTipo = (TextView) view.findViewById(R.id.linhaExercTextViewTipo);
            TextView linhaExercTextViewCarga = (TextView) view.findViewById(R.id.linhaExercTextViewCarga);
            TextView linhaExercTextViewSeries = (TextView) view.findViewById(R.id.linhaExercTextViewSeries);
            TextView linhaExercTextViewRepeticoes = (TextView) view.findViewById(R.id.linhaExercTextViewRepeticoes);

            str = exercicio.getStr_grupo_muscular();
            linhaExercLayoutTipo.setVisibility(LinearLayout.VISIBLE);
            linhaExercTextViewTipo.setText(str);

            if(!exercicio.getCarga().toString().equals("0")) {
                str = exercicio.getCarga().toString()+" "+context.getResources().getString(R.string.kg);
                linhaExercLayoutCarga.setVisibility(LinearLayout.VISIBLE);
                linhaExercTextViewCarga.setText(str);
            }

            if(!exercicio.getSeries().toString().equals("0")&&!exercicio.getRepeticoes().toString().equals("0")) {
                str = context.getResources().getString(R.string.series_repeticoes, exercicio.getSeries().toString(), exercicio.getRepeticoes().toString());
                linhaExercLayoutSeries.setVisibility(LinearLayout.VISIBLE);
                linhaExercTextViewSeries.setText(str);
            } else {
                if (!exercicio.getSeries().toString().equals("0")) {
                    str = exercicio.getSeries().toString() + " " + context.getResources().getString(R.string.serie);
                    linhaExercLayoutSeries.setVisibility(LinearLayout.VISIBLE);
                    linhaExercTextViewSeries.setText(str);
                }

                if (!exercicio.getRepeticoes().toString().equals("0")) {
                    str = exercicio.getRepeticoes().toString() + " " + context.getResources().getString(R.string.repeticoes);
                    linhaExercLayoutRepeticoes.setVisibility(LinearLayout.VISIBLE);
                    linhaExercTextViewRepeticoes.setText(str);
                }
            }

        } else if(exercicio.getId_tipo_exercicio()==2) {
            LinearLayout linhaExercLayoutTipo = (LinearLayout) view.findViewById(R.id.linhaExercLayoutTipo);
            LinearLayout linhaExercLayoutTempo = (LinearLayout) view.findViewById(R.id.linhaExercLayoutTempo);
            LinearLayout linhaExercLayoutDistancia = (LinearLayout) view.findViewById(R.id.linhaExercLayoutDistancia);
            TextView linhaExercTextViewTipo = (TextView) view.findViewById(R.id.linhaExercTextViewTipo);
            TextView linhaExercTextViewTempo = (TextView) view.findViewById(R.id.linhaExercTextViewTempo);
            TextView linhaExercTextViewDistancia = (TextView) view.findViewById(R.id.linhaExercTextViewDistancia);

            str = "Cárdio";
            linhaExercLayoutTipo.setVisibility(LinearLayout.VISIBLE);
            linhaExercTextViewTipo.setText(str);


            if(!exercicio.getTempo_segundos().toString().equals("0")) {
                str = exercicio.getTempo_segundos().toString() + " " + context.getResources().getString(R.string.minutos);
                linhaExercTextViewTempo.setText(str);
                linhaExercLayoutTempo.setVisibility(LinearLayout.VISIBLE);
            }

            if(!exercicio.getDistancia_km().toString().equals("0")) {
                str = exercicio.getDistancia_km().toString() + " " + context.getResources().getString(R.string.km);
                linhaExercTextViewDistancia.setText(str);
                linhaExercLayoutDistancia.setVisibility(LinearLayout.VISIBLE);
            }
        }


        LinearLayout linhaExercLayoutPrincipal = (LinearLayout) view.findViewById(R.id.linhaExercLayoutPrincipal);
        linhaExercLayoutPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linhaExercLayoutEdit = (LinearLayout) view.findViewById(R.id.linhaExercLayoutEdit);
                if(linhaExercLayoutEdit.getVisibility()==LinearLayout.VISIBLE){
                    linhaExercLayoutEdit.setVisibility(LinearLayout.GONE);
                } else {
                    linhaExercLayoutEdit.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        ImageButton imageButtonDeletar = (ImageButton) view.findViewById(R.id.imageButtonDeletar);
        imageButtonDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiaActivity)context).excluirExercicio(exercicio.getId());
            }
        });

        ImageButton imageButtonEditar = (ImageButton) view.findViewById(R.id.imageButtonEditar);
        imageButtonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiaActivity)context).editarExercicio(exercicio.getId());
            }
        });

        ImageView linhaExercImageUp = (ImageView) view.findViewById(R.id.linhaExercImageUp);
        linhaExercImageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiaActivity)context).subir(exercicio.getPosicao());
            }
        });

        ImageView linhaExercImageDown = (ImageView) view.findViewById(R.id.linhaExercImageDown);
        linhaExercImageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiaActivity)context).descer(exercicio.getPosicao());
            }
        });

        return view;
    }

}
