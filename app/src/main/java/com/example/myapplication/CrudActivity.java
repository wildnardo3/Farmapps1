package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CrudActivity extends AppCompatActivity {

    EditText edtRut, edtNombre, edtDireccion;
    Spinner spComuna;
    ListView lista;
    ArrayList<String> registros;
    ArrayAdapter<String> adapter;
    DataHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        // Inicialización de campos
        edtRut = findViewById(R.id.edtRut);
        edtNombre = findViewById(R.id.edtNombre);
        spComuna = findViewById(R.id.spComuna);
        lista = findViewById(R.id.lsLista);

        // Configuración del spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Puente Alto", "Macul", "San Miguel"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComuna.setAdapter(spinnerAdapter);

        helper = new DataHelper(this);

        // Cargar lista de registros
        cargarLista();

        // Botón Agregar
        findViewById(R.id.btnAgregar).setOnClickListener(view -> agregarRegistro());
    }

    private void cargarLista() {
        registros = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT rut, nombre, direccion, comuna FROM alumno", null);

        if (cursor.moveToFirst()) {
            do {
                String registro = "RUT: " + cursor.getInt(0) + ", Nombre: " + cursor.getString(1) +
                        ", Dirección: " + cursor.getString(2) + ", Comuna: " + cursor.getString(3);
                registros.add(registro);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, registros);
        lista.setAdapter(adapter);
    }

    private void agregarRegistro() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("rut", edtRut.getText().toString());
        values.put("nombre", edtNombre.getText().toString());
        values.put("direccion", edtDireccion.getText().toString());
        values.put("comuna", spComuna.getSelectedItem().toString());

        long result = db.insert("alumno", null, values);
        db.close();

        if (result != -1) {
            Toast.makeText(this, "Registro agregado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            cargarLista();
        } else {
            Toast.makeText(this, "Error al agregar", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        edtRut.setText("");
        edtNombre.setText("");
        edtDireccion.setText("");
    }
}
