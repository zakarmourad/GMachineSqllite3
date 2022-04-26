package ma.projet.sqlite.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.LinkedList;
import java.util.List;

import ma.projet.sqlite.bean.Salle;
import ma.projet.sqlite.dao.IDao;
import ma.projet.sqlite.util.MySQLiteHelper;


public class SalleService implements IDao<Salle> {

    private MySQLiteHelper helper = null;

    /**
     * CRUD operations (create "add", read "get", update, delete) Salle + find all salle
     */
    // Table name
    private static final String TABLE_SALLE = "salle";
    // Salle Table Columns names
    private static final String KEY_ID = "idS";
    private static final String KEY_CODE = "code";
    private static final String KEY_LIBELLE = "libelle";
    private static final String[] COLUMNS = {KEY_ID, KEY_CODE, KEY_LIBELLE};
    public SalleService(Context context) {
        this.helper = new MySQLiteHelper(context);
    }

    @Override
    public void add(Salle o) {
        //for logging
        Log.d("Create", o.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CODE, o.getCode()); // get code
        values.put(KEY_LIBELLE, o.getLibelle()); // get libelle
        // 3. insert
        db.insert(TABLE_SALLE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
    }

    @Override
    public Salle findById(int id) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.helper.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_SALLE, // a. table
                        COLUMNS, // b. column names
                        " idS = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        // 4. build Salle object
        Salle salle = new Salle();
        salle.setId(Integer.parseInt(cursor.getString(0)));
        salle.setCode(cursor.getString(1));
        salle.setLibelle(cursor.getString(2));
        // 5. return Salle
        return salle;
    }

    public Salle findByCode(String code) {
        // 1. get reference to readable DB
        SQLiteDatabase db = this.helper.getReadableDatabase();
        // 2. build query
        Cursor cursor =
                db.query(TABLE_SALLE, // a. table
                        COLUMNS, // b. column names
                        " code = ?", // c. selections
                        new String[]{code}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        // 4. build Salle object
        Salle salle = new Salle();
        salle.setId(Integer.parseInt(cursor.getString(0)));
        salle.setCode(cursor.getString(1));
        salle.setLibelle(cursor.getString(2));
        // 5. return Salle
        return salle;
    }

    @Override
    public List<Salle> findAll() {
        List<Salle> salles = new LinkedList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_SALLE;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // 3. go over each row, build Salle and add it to list
        Salle salle = null;
        if (cursor.moveToFirst()) {
            do {
                salle = new Salle();
                salle.setId(Integer.parseInt(cursor.getString(0)));
                salle.setCode(cursor.getString(1));
                salle.setLibelle(cursor.getString(2));
                // Add salle to salle
                salles.add(salle);
                Log.d("findAllSalle()", salle.toString());
            } while (cursor.moveToNext());
        }
        // return salles
        return salles;
    }


    @Override
    public void update(Salle o) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("code", o.getCode()); Log.d("code"," " + o.getCode());  // get code
        values.put("libelle", o.getLibelle());  Log.d("code"," " + o.getLibelle()); Log.d("code"," " + o.getId()); // get libelle Log.d("code"," ");
        // 3. updating row
        int i = db.update(TABLE_SALLE, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(o.getId())}); //selection args
        // 4. closeLog.d("code"," ");
        db.close();
    }

    @Override
    public void delete(Salle o) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.helper.getWritableDatabase();
        // 2. delete
        db.delete(TABLE_SALLE, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(o.getId()) }); //selections args
        // 3. close
        db.close();
        //log
        Log.d("deletesalle", o.toString());
    }
}