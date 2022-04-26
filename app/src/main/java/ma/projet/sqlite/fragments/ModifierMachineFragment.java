package ma.projet.sqlite.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.projet.sqlite.R;
import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.service.MachineService;
import ma.projet.sqlite.service.SalleService;

public class ModifierMachineFragment extends Fragment {
    private static final String TAG = "ModifierMachineFragment";
    private EditText marque ;
    private EditText reference ;
    private Spinner spinner ;
    private Button modifier ;
    private HashMap data ;
    private Serializable s;
    private MachineService machineService;
    private SalleService salleService;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ModifierMachineFragment() {
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
    public static ModifierMachineFragment newInstance(String param1, String param2) {
        ModifierMachineFragment fragment = new ModifierMachineFragment();
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
        View v = inflater.inflate(R.layout.fragment_modifier_machine, container, false);

        marque = v.findViewById(R.id.marque);
        reference = v.findViewById(R.id.reference);
        modifier = v.findViewById(R.id.modifier);
        spinner = v.findViewById(R.id.Spinner);
        salleService = new SalleService(getActivity().getApplicationContext());

        ArrayAdapter<String> adapter;
        List<String> liste= new ArrayList<String>();
        for(Salle salle : salleService.findAll()) {
            liste.add(salle.getCode());
        }
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, liste);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        s = getArguments().getSerializable("data");
        if(s != null)
        {
            data = (HashMap) s ;
            marque.setText(""+data.get("marque"));
            reference.setText(""+data.get("reference"));
            for (int i = 0; i < spinner.getCount(); i++) {
                String item = spinner.getItemAtPosition(i).toString();
                if (item.equals(data.get("idSalle").toString())){
                    spinner.setSelection(i);
                    break;
                }
            }
        }else
        {
            Toast.makeText(getActivity().getApplicationContext() , " Impossible" , Toast.LENGTH_LONG).show();
        }

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salleService = new SalleService(getActivity().getApplicationContext());
                machineService = new MachineService(getActivity().getApplicationContext());
                int id = salleService.findByCode(spinner.getSelectedItem().toString()).getId();
                machineService.update(new Machine(Integer.parseInt(data.get("id").toString()),marque.getText().toString() , reference.getText().toString() , salleService.findById(id)));
                Log.d("Marque : ",marque.getText().toString());
                Log.d("Reference : ",reference.getText().toString());
                MachineFragment fragment = new MachineFragment();
                FragmentManager fm1 = getFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.fragment_container, fragment);
                ft1.commit();
            }
        });

        return v;
    }
}

