package com.example.acividad8;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {

    private EditText etTitle, etSeasons, etDate;
    private Button btnSave, btnCancel;
    private DBHelper db;
    private long id = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        etTitle = findViewById(R.id.etTitle);
        etSeasons = findViewById(R.id.etSeasons);
        etDate = findViewById(R.id.etDate);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        db = new DBHelper(this);

        Intent i = getIntent();
        if (i != null && i.hasExtra("id")) {
            id = i.getLongExtra("id", -1);
            etTitle.setText(i.getStringExtra("title"));
            etSeasons.setText(String.valueOf(i.getIntExtra("seasons", 1)));
            etDate.setText(i.getStringExtra("date"));
        }

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // month is 0-based
                String dd = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                etDate.setText(dd);
            }
        }, year, month, day);
        dpd.show();
    }

    private void saveItem() {
        String title = etTitle.getText().toString().trim();
        String seasonsStr = etSeasons.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        if (title.isEmpty() || seasonsStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        int seasons;
        try {
            seasons = Integer.parseInt(seasonsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Temporadas/Capítulos debe ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }

        if (id == -1) {
            Item it = new Item();
            it.setTitle(title);
            it.setSeasons(seasons);
            it.setReleaseDate(date);
            long newId = db.addItem(it);
            if (newId > 0) {
                Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error al agregar", Toast.LENGTH_SHORT).show();
            }
        } else {
            Item it = new Item(id, title, seasons, date);
            int rows = db.updateItem(it);
            if (rows > 0) {
                Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

