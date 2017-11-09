package com.programacaobrasil.agendacontato;

import com.programacaobrasil.agendacontato.dominio.RepositorioContato;
import com.programacaobrasil.agendacontato.dominio.entidades.Contato;
import com.programacaobrasil.agendacontato.service.*;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnAdd;
    private EditText edtPesquisa;
    private ListView lstContatos;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private ArrayAdapter<Contato> adpContatos;
    private RepositorioContato repositorioContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPesquisa = (EditText)findViewById(R.id.edtPesquisa);
        lstContatos = (ListView)findViewById(R.id.lstContatos);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(conn);

            adpContatos = repositorioContato.buscarContatos(this);

            lstContatos.setAdapter(adpContatos);
        }
        catch (SQLException ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro na base de dados: " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent it = new Intent(this, CadContatoActivity.class);
        startActivityForResult(it, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adpContatos = repositorioContato.buscarContatos(this);

        lstContatos.setAdapter(adpContatos);
    }
}
