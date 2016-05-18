package gabrielgarcia.peluchesapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button agregarp;
    Button busccarp;
    Button eliminarp;
    Button actualizarp;
    Button ventap;
    Button gananciasp;
    EditText idtext;
    EditText nombretext;
    EditText cantidadtext;
    EditText valortext;
    private TextView Resultado;
    private int gananciatotal=0;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        agregarp = (Button) findViewById(R.id.agregar);
        busccarp = (Button) findViewById(R.id.buscar);
        eliminarp = (Button) findViewById(R.id.eliminar);
        actualizarp = (Button) findViewById(R.id.actualizar);

        ventap = (Button) findViewById(R.id.venta);
        gananciasp = (Button) findViewById(R.id.gananciasto);
        idtext = (EditText) findViewById(R.id.textid);
        nombretext = (EditText) findViewById(R.id.textnombre);
        cantidadtext = (EditText) findViewById(R.id.textcantidad);
        Resultado = (TextView) findViewById(R.id.txtResultado);
        valortext = (EditText) findViewById(R.id.textvalor);

        //Abrimos la base de datos 'UsuariosBD' en modo escritura
        SQliteHelper usuario = new SQliteHelper(this);
        //UsuariosSQLiteHelper usuario = new UsuariosSQLiteHelper(this, "UsuariosBD", null, 1);

        db = usuario.getWritableDatabase();

        Ver_Tabla();

        agregarp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = idtext.getText().toString();
                String nombre = nombretext.getText().toString();
                String cantidadd = cantidadtext.getText().toString();
                String valorr = valortext.getText().toString();


                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("id",id);
                nuevoRegistro.put("nombre", nombre);
                nuevoRegistro.put("cantidad", cantidadd);
                nuevoRegistro.put("valor", valorr);
                //.........

                String[] campos = new String[]{"id", "nombre", "cantidad", "valor"};
                String[] args = new String[]{nombre};

                Cursor c = db.query("Inventario", campos, "nombre=?", args, null, null, null, null);

                if (c.moveToFirst()) {
                    Toast.makeText(getApplicationContext(), "Ya existe", Toast.LENGTH_SHORT).show();
                }else{
                    db.insert("Inventario", null, nuevoRegistro);

                    Ver_Tabla();
                }
                //-ñ----

            }
        });

        actualizarp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String idtexto = idtext.getText().toString();
                String nombre = nombretext.getText().toString();
                String cantidadtexto = cantidadtext.getText().toString();
                String valorr = valortext.getText().toString();

                ContentValues nuevoValor = new ContentValues();
                nuevoValor.put("nombre",nombre);
                nuevoValor.put("cantidad",cantidadtexto);
                nuevoValor.put("valor", valorr);

                boolean D = db.update("Inventario", nuevoValor, "nombre='" + nombre+"'", null)>0;
                if (D) {
                    Toast.makeText(getApplicationContext(), "Se modificó correctamente", Toast.LENGTH_SHORT).show();
                }
                Ver_Tabla();
            }
        });

        eliminarp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = nombretext.getText().toString();
                db.delete("Inventario", "nombre='"+nombre+"'", null);
                Ver_Tabla();
            }
        });

        busccarp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = nombretext.getText().toString();
            String[] campos = new String[]{"id", "nombre", "cantidad", "valor"};
            String[] args = new String[]{nombre};

            Cursor c = db.query("Inventario", campos, "nombre=?", args, null, null, null, null);



            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                Resultado.setText("");
                //Recorremos el cursor hasta que no encontrar mas registros
                do {
                    String idd = c.getString(0);
                    String nombree = c.getString(1);
                    int cantidadd = c.getInt(2);
                    int valorr = c.getInt(3);

                    Resultado.append(" " + idd + "   " + nombree + "   " + cantidadd + "   " + valorr +"\n");
                } while (c.moveToNext());
            }
        }
        });

        ventap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

                String nombre = nombretext.getText().toString();
                String[] campos = new String[]{"id", "nombre", "cantidad", "valor"};
                String[] args = new String[]{nombre};

                Cursor c = db.query("Inventario", campos, "nombre=?", args, null, null, null, null);



                //Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {
                    Resultado.setText("");
                    //Recorremos el cursor hasta que no encontrar mas registros

                    String idd = c.getString(0);
                    String nombree = c.getString(1);
                    int cantidadd = c.getInt(2);
                    int valorr = c.getInt(3);
                    cantidadd=cantidadd-1;
                    gananciatotal=gananciatotal+valorr;
                    ContentValues nuevoValor = new ContentValues();



                    nuevoValor.put("cantidad",cantidadd);


                    db.update("Inventario", nuevoValor, "nombre='" + nombre+"'", null);

                    Resultado.append(" " + idd + " - " + nombree + " - " + cantidadd + " - " + valorr +"\n");

                }
                int i = c.getInt(3);
                i=i-1;
                if ( i<=5 ) {

                    Intent notIntent;
                    builder.setContentTitle("PELUCHES APP")
                            .setContentText("Casi no queda inventario!!")
                            .setTicker("cosa")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentInfo(c.getString(3));

                    notIntent = new Intent(MainActivity.this, MainActivity.class);

                    PendingIntent contintent = PendingIntent.getActivity(MainActivity.this, 0,notIntent, 0);

                    builder.setContentIntent(contintent);

                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    nm.notify(1,builder.build());
                }
            }
        });

        gananciasp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "ganancias: "+gananciatotal, Toast.LENGTH_LONG).show();
        }

        });

    }



    protected void Ver_Tabla() {
        //PAra mostrar todos los campos de la tabla
        Cursor c = db.rawQuery("SELECT id,nombre,cantidad,valor FROM Inventario", null);

        Resultado.setText("");
        if (c.moveToFirst())
            do {
                String idd = c.getString(0);
                String nombree = c.getString(1);
                int    cantidadd = c.getInt(2);
                int  valorr = c.getInt(3);

                Resultado.append(" " + idd + "    " + nombree + "    " + cantidadd + " Unidades" + "  " + "$" + valorr + "\n");

            }while (c.moveToNext());
    }

}