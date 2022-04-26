package ma.projet.sqlite.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import ma.projet.sqlite.bean.Machine;
import ma.projet.sqlite.dao.IDao;
import ma.projet.sqlite.util.MySQLiteHelper;


public class MachineService implements IDao<Machine> {

    private MySQLiteHelper helper = null;
    private SalleService salleService = null;

    /**
     * CRUD operations (create "add", read "get", update, delete) Salle + get all salle + delete all salle
     */

    // Salles table name
    private static final String TABLE_MACHINE = "machine";

    // Salles Table Columns names
    private static final String KEY_ID = "idM";
    private static final String KEY_MARQUE = "marque";
    private static final String KEY_REFERENCE = "reference";
    private static final String KEY_SALLE = "idSalle";

    private static final String[] COLUMNS = {KEY_ID, KEY_MARQUE, KEY_REFERENCE, KEY_SALLE};

    public MachineService(Context context) {
        this.helper = new MySQLiteHelper(context);
        this.salleService = new SalleService(context);
    }

    @Override
    public void add(Machine o) {
        //for logging
        Log.d("add", o.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_MARQUE, o.getMarque()); // get Marque
        values.put(KEY_REFERENCE, o.getRefernce()); // get Reference
        values.put(KEY_SALLE, o.getSalle().getId()); // get Salle

        // 3. insert
        db.insert(TABLE_MACHINE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();

    }

    @Override
    public Machine findById(int id) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.helper.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_MACHINE, // a. table
                        COLUMNS, // b. column names
                        " idM = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build Machine object
        Machine machine = new Machine();
        machine.setId(Integer.parseInt(cursor.getString(0)));
        machine.setMarque(cursor.getString(1));
        machine.setRefernce(cursor.getString(2));
        machine.setSalle(salleService.findById(cursor.getInt(3)));

        //log
        Log.d("getMachine(" + id + ")", machine.toString());

        // 5. return Machine
        return machine;
    }

    @Override
    public List<Machine> findAll() {
        List<Machine> machines = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MACHINE;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build Machine and add it to list
        Machine machine = null;
        if (cursor.moveToFirst()) {
            do {
                machine = new Machine();
                machine.setId(Integer.parseInt(cursor.getString(0)));
                machine.setMarque(cursor.getString(1));
                machine.setRefernce(cursor.getString(2));
                machine.setSalle(salleService.findById(cursor.getInt(3)));
                // Add salle to salle
                machines.add(machine);
                Log.d("getAllMachine()", machine.toString());
            } while (cursor.moveToNext());
        }
        return machines;
    }

    public List<Machine> findMachines(int id) {
        List<Machine> machines = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MACHINE+" where idSalle = "+ id;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build Machine and add it to list
        Machine machine = null;
        if (cursor.moveToFirst()) {
            do {
                machine = new Machine();
                machine.setId(Integer.parseInt(cursor.getString(0)));
                machine.setMarque(cursor.getString(1));
                machine.setRefernce(cursor.getString(2));
                machine.setSalle(salleService.findById(cursor.getInt(3)));
                // Add salle to salle
                machines.add(machine);
            } while (cursor.moveToNext());
        }
        return machines;
    }

    @Override
    public void update(Machine o) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("marque", o.getMarque()); // get Marque
        values.put("reference", o.getRefernce()); // get Reference
        values.put(KEY_SALLE, o.getSalle().getId()); // get Salle

        // 3. updating row
        int i = db.update(TABLE_MACHINE, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(o.getId())}); //selection args
        Log.d("confirmation","OK");
        // 4. close
        db.close();

    }

    @Override
    public void delete(Machine o) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_MACHINE, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(o.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteMachine", o.toString());

    }

}
