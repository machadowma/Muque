package securisamarum.muque;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PlanoAdapter extends BaseAdapter {
    private final List<Plano> planos;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public PlanoAdapter(Context context, int resource, List<Plano> planos ) {
        this.context = context;
        this.resource = resource;
        this.planos = planos;
    }

    @Override
    public int getCount() {
        return planos.size();
    }

    @Override
    public Plano getItem(int position) {
        return planos.get(position);
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

        final Plano plano = planos.get(position);

        TextView textViewListaPlanos = (TextView) view.findViewById(R.id.textViewListaPlanos);
        textViewListaPlanos.setText(plano.getNome());

        ImageView imageViewListaPlanosUp = (ImageView) view.findViewById(R.id.imageViewListaPlanosUp);
        imageViewListaPlanosUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).subir(plano.getPosicao());
            }
        });

        ImageView imageViewListaPlanosDown = (ImageView) view.findViewById(R.id.imageViewListaPlanosDown);
        imageViewListaPlanosDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).descer(plano.getPosicao());
            }
        });

        return view;
    }

}
