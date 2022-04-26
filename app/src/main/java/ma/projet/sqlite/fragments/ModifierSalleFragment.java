package ma.projet.sqlite.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;

import ma.projet.sqlite.R;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.service.SalleService;

public class ModifierSalleFragment extends Fragment {
    private static final String TAG = "ModifierSalleFragment";
    private EditText code;
    private EditText libelle;
    private Button modifier;
    private HashMap data;
    private Serializable s;
    private SalleService salleService;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModifierSalleFragment() {
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
    public static ModifierSalleFragment newInstance(String param1, String param2) {
        ModifierSalleFragment fragment = new ModifierSalleFragment();
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
        View v = inflater.inflate(R.layout.fragment_modifier_salle, container, false);

        code = v.findViewById(R.id.code);
        libelle = v.findViewById(R.id.libelle);
        modifier = v.findViewById(R.id.edit);

        s = getArguments().getSerializable("data");
        if (s != null) {
            data = (HashMap) s;
            code.setText("" + data.get("code"));
            libelle.setText("" + data.get("libelle"));
        } else {
            Toast.makeText(getActivity().getApplicationContext(), " Impossible", Toast.LENGTH_LONG).show();
        }

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salleService = new SalleService(getActivity().getApplicationContext());
                salleService.update(new Salle(Integer.parseInt(data.get("id").toString()), code.getText().toString(), libelle.getText().toString()));
                SalleFragment fragment = new SalleFragment();
                FragmentManager fm1 = getFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.fragment_container, fragment);
                ft1.commit();
            }
        });

        return v;
    }
}

