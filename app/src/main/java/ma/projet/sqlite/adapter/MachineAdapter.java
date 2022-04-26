package ma.projet.sqlite.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ma.projet.sqlite.R;
import java.util.List;

import ma.projet.sqlite.bean.Machine;


/**
 * Created by PC-PRO on 19/11/2016.
 */

public class MachineAdapter extends BaseAdapter{

    List<Machine> machines ;
    private LayoutInflater layoutInflater ;

    public MachineAdapter(Context context, List<Machine> machines) {
        this.machines = machines;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return machines.size();
    }

    @Override
    public Object getItem(int position) {
        return machines.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null)
            view =  layoutInflater.inflate(R.layout.machine_item, null) ;

        TextView salle = (TextView) view.findViewById(R.id.salle);
        TextView marque = (TextView) view.findViewById(R.id.marque);
        TextView reference = (TextView) view.findViewById(R.id.reference);
        TextView id = (TextView) view.findViewById(R.id.ids);

        id.setText(machines.get(position).getId()+"");
        salle.setText("Salle : " + machines.get(position).getSalle().getCode());
        marque.setText("Marque : " + machines.get(position).getMarque());
        reference.setText("Référence : " + machines.get(position).getRefernce());

        return view;
    }
}
