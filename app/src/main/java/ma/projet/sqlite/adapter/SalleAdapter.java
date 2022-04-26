package ma.projet.sqlite.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ma.projet.sqlite.R;
import java.util.List;

import ma.projet.sqlite.bean.Salle;

/**
 * Created by leblond on 16/11/2015.
 */
public class SalleAdapter  extends BaseAdapter{

    private List<Salle> salles;
    private LayoutInflater layoutInflater;

    public SalleAdapter(Context context, List<Salle> salles) {
        this.salles = salles;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return salles.size();
    }

    @Override
    public Object getItem(int position) {
        return salles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       if(convertView == null)
           convertView = layoutInflater.inflate(R.layout.salle_item, null);
        TextView code = (TextView) convertView.findViewById(R.id.code);
        TextView libelle = (TextView) convertView.findViewById(R.id.libelle);
        TextView ids = (TextView) convertView.findViewById(R.id.ids);
        ids.setText(salles.get(position).getId()+"");
        code.setText("Code : " + salles.get(position).getCode());
        libelle.setText("Libelle : " + salles.get(position).getLibelle());


        return convertView;
    }
}
