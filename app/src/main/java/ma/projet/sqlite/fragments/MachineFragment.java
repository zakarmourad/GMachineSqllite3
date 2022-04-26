package ma.projet.sqlite.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ma.projet.sqlite.R;
import ma.projet.sqlite.adapter.MachineAdapter;
import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.service.MachineService;
import ma.projet.sqlite.service.SalleService;

public class MachineFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "MachineFragment";
    private ListView listView;
    private MachineAdapter machineAdapter = null;
    private EditText marque ;
    private EditText reference ;
    private Button create ;
    private Spinner spinner ;
    private SalleService salleService;
    private MachineService machineService;
    private View v ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MachineFragment() {
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
    public static MachineFragment newInstance(String param1, String param2) {
        MachineFragment fragment = new MachineFragment();
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
        View v = inflater.inflate(R.layout.fragment_machine, container, false);
        listView = v.findViewById(R.id.listViewMachine);
        salleService = new SalleService(getActivity().getApplicationContext());
        machineService = new MachineService(getActivity().getApplicationContext());
        marque = v.findViewById(R.id.marque);
        reference = v.findViewById(R.id.reference);
        create = v.findViewById(R.id.btnCreate);
        spinner = v.findViewById(R.id.Spinner);

        ArrayAdapter<String> adapter;
        List<String> liste= new ArrayList<String>();
        for(Salle salle : salleService.findAll()) {
            liste.add(salle.getCode());
        }
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, liste);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineService machineService = new MachineService(getActivity().getApplicationContext());
                salleService = new SalleService(getActivity().getApplicationContext());
                Salle salle = salleService.findByCode(spinner.getSelectedItem().toString());
                machineService.add(new Machine(marque.getText().toString(), reference.getText().toString(), salle ));
                MachineAdapter as = new MachineAdapter(getActivity().getApplicationContext(), machineService.findAll());
                listView.setAdapter(as);
                Toast.makeText(getActivity().getApplicationContext() ," Machine crée ", Toast.LENGTH_SHORT).show();
            }
        });

        MachineAdapter as = new MachineAdapter(getActivity().getApplicationContext(), machineService.findAll());
        listView.setAdapter(as);
        listView.setOnItemClickListener(this);

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
                alertDialogBuilder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        int id = Integer.parseInt(((TextView) v.findViewById(R.id.ids)).getText().toString());
                        machineService.delete(machineService.findById(id));
                        MachineAdapter as = new MachineAdapter(getActivity().getApplicationContext(), machineService.findAll());
                        listView.setAdapter(as);
                        Toast.makeText(getActivity().getApplicationContext() ," Machine supprimée ", Toast.LENGTH_LONG).show();
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
                Machine machine = machineService.findById(id);
                HashMap<String,String> data = new HashMap<>();
                data.put("id",machine.getId()+"");
                data.put("idSalle",machine.getSalle().getCode()+"");
                data.put("marque",machine.getMarque());
                data.put("reference",machine.getRefernce());
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",data);
                ModifierMachineFragment fragment = new ModifierMachineFragment();
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
