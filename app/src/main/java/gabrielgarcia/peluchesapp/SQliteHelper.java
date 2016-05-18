package gabrielgarcia.peluchesapp;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;



public class SQliteHelper extends SQLiteOpenHelper {
    private static final String DATA_BASE_NAME="Inventario";
    private static final int DATA_VERSION=1;

    //Sentencia SQL Para crear una tabla
    String sqlCreate = "CREATE TABLE Inventario(id INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT,cantidad INTEGER, valor INTEGER)";

    public SQliteHelper (Context contexto) {
        super(contexto, DATA_BASE_NAME, null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Inventario");
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}