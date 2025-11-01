package com.example.acividad8;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListadoActivity extends AppCompatActivity {

    private static final int REQ_EDIT = 1;

    private DBHelper db;
    private ListView listView;
    private Button btnAdd;
    private List<Item> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private List<String> display = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        db = new DBHelper(this);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, display);
        listView.setAdapter(adapter);

        loadItems();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListadoActivity.this, EditItemActivity.class);
                startActivityForResult(i, REQ_EDIT);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item it = items.get(position);
                Intent i = new Intent(ListadoActivity.this, EditItemActivity.class);
                i.putExtra("id", it.getId());
                i.putExtra("title", it.getTitle());
                i.putExtra("seasons", it.getSeasons());
                i.putExtra("date", it.getReleaseDate());
                startActivityForResult(i, REQ_EDIT);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(ListadoActivity.this)
                        .setTitle("Eliminar")
                        .setMessage("¿Eliminar este registro?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Item it = items.get(position);
                                int rows = db.deleteItem(it.getId());
                                if (rows > 0) {
                                    Toast.makeText(ListadoActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                                    loadItems();
                                } else {
                                    Toast.makeText(ListadoActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    private void loadItems() {
        items = db.getAllItems();
        display.clear();
        for (Item it : items) {
            display.add(it.getTitle() + " — " + it.getSeasons() + " — " + it.getReleaseDate());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_EDIT && resultCode == RESULT_OK) {
            loadItems();
        }
    }
}

