package com.programacaobrasil.listatarefas;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private EditText txtTarefa;
    private Button btnAdd;
    private ListView lstTarefas;
    private SQLiteDatabase bancoDados;
    private ArrayAdapter<String> itensAdapter;
    private ArrayList<String> itens;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        initializeEventsComponents();
        createTables();
    }

    private void initializeComponents()
    {
        txtTarefa = (EditText)findViewById(R.id.txtTarefa);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        lstTarefas = (ListView) findViewById(R.id.lstTarefas);

        bancoDados = openOrCreateDatabase("appTarefas", MODE_PRIVATE, null);
    }

    private void initializeEventsComponents()
    {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tarefa = txtTarefa.getText().toString();
                insertTarefa(tarefa);
                listarTarefas();
                txtTarefa.setText("");
            }
        });

        lstTarefas.setLongClickable(true);
        lstTarefas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                removerTarefa(ids.get(position));
                listarTarefas();
                return false;
            }
        });

        listarTarefas();
    }

    private void createTables()
    {
        try {
            //Tabela de tarefas
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas (id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR)");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void insertTarefa(String tarefa)
    {
        try {
            if (tarefa.trim().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Preencha o campo tarefa", Toast.LENGTH_SHORT).show();
            }
            else
            {
                bancoDados.execSQL("INSERT INTO tarefas (tarefa) VALUES ('" + tarefa + "')");
                Toast.makeText(getApplicationContext(), "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void listarTarefas()
    {
        try
        {
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            itens = new ArrayList<String>();
            ids = new ArrayList<Integer>();

            itensAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, itens);
            lstTarefas.setAdapter(itensAdapter);

            cursor.moveToFirst();
            while (cursor!=null)
            {
                Log.i("Resultado - ", "Tarefa: " + cursor.getString( indiceColunaTarefa ) );

                ids.add( Integer.parseInt(cursor.getString(indiceColunaId)) );
                itens.add(cursor.getString(indiceColunaTarefa));

                cursor.moveToNext();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void removerTarefa(Integer id)
    {
        try
        {
            bancoDados.execSQL("DELETE FROM tarefas WHERE id="+id);
            Toast.makeText(getApplicationContext(), "Tarefa removida com sucesso!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
