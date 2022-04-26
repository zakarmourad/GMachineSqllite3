package ma.projet.sqlite.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import ma.projet.sqlite.HomeActivity;
import ma.projet.sqlite.R;
import ma.projet.sqlite.adapter.SalleAdapter;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.service.SalleService;

public class SalleFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "SalleFragment";
    private EditText code;
    private EditText libelle;
    private Button add;
    private ListView list;
    private View v;
    private SalleService db = null;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SalleFragment newInstance(String param1, String param2) {
        SalleFragment fragment = new SalleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_salle, container, false);

        code = v.findViewById(R.id.code);
        libelle = v.findViewById(R.id.libelle);
        add = v.findViewById(R.id.add);
        list = v.findViewById(R.id.listViewSalle);

        db = new SalleService(getActivity().getApplicationContext());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db =  new SalleService(getActivity().getApplicationContext());
                db.add(new Salle(code.getText().toString(), libelle.getText().toString()));
                SalleAdapter as = new SalleAdapter(getActivity().getApplicationContext(), db.findAll());
                list.setAdapter(as);
                Toast.makeText(getActivity().getApplicationContext(), " Salle crée " , Toast.LENGTH_SHORT).show();
            }
        });

        SalleAdapter as = new SalleAdapter(getActivity().getApplicationContext(), db.findAll());
        list.setAdapter(as);
        list.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        v = view;
        AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                alertDialogBuilder1.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        int id = Integer.parseInt(((TextView) v.findViewById(R.id.ids)).getText().toString());
                        db.delete(db.findById(id));
                        Toast.makeText(getActivity().getApplicationContext(), " Salle supprimée " , Toast.LENGTH_SHORT).show();
                        SalleAdapter as = new SalleAdapter(getActivity().getApplicationContext(), db.findAll());
                        list.setAdapter(as);
                    }
                });
                alertDialogBuilder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder1.create();
                alertDialog.show();
            }
        });

        alertDialogBuilder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = Integer.parseInt(((TextView) v.findViewById(R.id.ids)).getText().toString());
                Salle salle = db.findById(id);
                HashMap<String,String> data = new HashMap<>();
                data.put("id",salle.getId()+"");
                data.put("code",salle.getCode());
                data.put("libelle",salle.getLibelle());
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);
                ModifierSalleFragment fragment = new ModifierSalleFragment();
                fragment.setArguments(bundle);
                FragmentManager fm1 = getFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.fragment_container, fragment);
                ft1.commit();
            }
        });

        AlertDialog alertDialog1 = alertDialogBuilder.create();
        alertDialog1.show();
    }
}