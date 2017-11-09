package com.programacaobrasil.agendacontato;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;

import com.programacaobrasil.agendacontato.dominio.RepositorioContato;
import com.programacaobrasil.agendacontato.dominio.entidades.Contato;
import com.programacaobrasil.agendacontato.service.DataBase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CadContatoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNome;
    private EditText edtTelefone;
    private EditText edtEmail;
    private EditText edtEndereco;
    private EditText edtDatasEspeciais;
    private EditText edtGrupos;

    private Spinner spnTelefone;
    private Spinner spnEmail;
    private Spinner spnEndereco;
    private Spinner spnDatasEspeciais;

    private ArrayAdapter<String> adpTelefone;
    private ArrayAdapter<String> adpEmail;
    private ArrayAdapter<String> adpEndereco;
    private ArrayAdapter<String> adpDatasEspeciais;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private ArrayAdapter<String> adpContatos;
    private RepositorioContato repositorioContato;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_contato);

        contato = new Contato();

        edtNome=(EditText)findViewById(R.id.edtNome);
        edtTelefone=(EditText)findViewById(R.id.edtTelefone);
        edtEmail=(EditText)findViewById(R.id.edtEmail);
        edtEndereco=(EditText)findViewById(R.id.edtEndereco);
        edtDatasEspeciais=(EditText)findViewById(R.id.edtDatasEspeciais);
        edtGrupos=(EditText)findViewById(R.id.edtGrupos);

        spnTelefone=(Spinner)findViewById(R.id.spnTelefone);
        spnEmail=(Spinner)findViewById(R.id.spnEmail);
        spnEndereco=(Spinner)findViewById(R.id.spnEndereco);
        spnDatasEspeciais=(Spinner)findViewById(R.id.spnDatasEspeciais);

        adpTelefone = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpTelefone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpEmail = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpEmail.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpEndereco = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpEndereco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adpDatasEspeciais = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adpDatasEspeciais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnTelefone.setAdapter(adpTelefone);
        spnEmail.setAdapter(adpEmail);
        spnEndereco.setAdapter(adpEndereco);
        spnDatasEspeciais.setAdapter(adpDatasEspeciais);

        adpTelefone.add("Celular");
        adpTelefone.add("Trabalho");
        adpTelefone.add("Casa");
        adpTelefone.add("Principal");
        adpTelefone.add("Fax Trabalho");
        adpTelefone.add("Fax Casa");
        adpTelefone.add("Pager");
        adpTelefone.add("Outros");

        adpEmail.add("Casa");
        adpEmail.add("Trabalho");
        adpEmail.add("Outros");

        adpEndereco.add("Casa");
        adpEndereco.add("Trabalho");
        adpEndereco.add("Outros");

        adpDatasEspeciais.add("Aniversário");
        adpDatasEspeciais.add("Data Comemorativa");
        adpDatasEspeciais.add("Outros");

        ExibirDataListener listener = new ExibirDataListener();
        edtDatasEspeciais.setOnClickListener(listener);
        edtDatasEspeciais.setOnFocusChangeListener(listener);

        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioContato = new RepositorioContato(conn);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meu_cad_contato_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.mnSalvar:
                if (contato==null)
                {
                    inserir();
                }
                else
                {
                    editar();
                }
                finish();
                break;
            case R.id.mnExcluir:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inserir()
    {
        try
        {
            contato.setNome(edtNome.getText().toString());
            contato.setTelefone(edtTelefone.getText().toString());
            contato.setEmail(edtEmail.getText().toString());
            contato.setEndereco(edtEndereco.getText().toString());
            contato.setGrupos(edtGrupos.getText().toString());

            //Date date = new Date();
            //contato.setDatasEspeciais(date);

            contato.setTipoTelefone(String.valueOf(spnTelefone.getSelectedItemPosition()));
            contato.setTipoEmail(String.valueOf(spnEmail.getSelectedItemPosition()));
            contato.setTipoEndereco(String.valueOf(spnEndereco.getSelectedItemPosition()));
            contato.setTipoDatasEspeciais(String.valueOf(spnDatasEspeciais.getSelectedItemPosition()));

            repositorioContato.inserirContato(contato);
        }
        catch (SQLException ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro ao inserir os dados: " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
        catch (Exception ex)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage("Erro inesperado: " + ex.getMessage());
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
    }

    private void editar(){}

    private void exibirData(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        dlg.show();
    }

    private class ExibirDataListener implements View.OnClickListener, View.OnFocusChangeListener
    {
        @Override
        public void onClick(View view) {
            exibirData();
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            exibirData();
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            Date data = calendar.getTime();

            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
            String dt = format.format(data);

            edtDatasEspeciais.setText(dt);
            contato.setDatasEspeciais(data);
        }
    }
}