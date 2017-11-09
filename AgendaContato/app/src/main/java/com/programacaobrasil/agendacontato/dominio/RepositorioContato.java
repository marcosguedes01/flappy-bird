package com.programacaobrasil.agendacontato.dominio;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.widget.*;

import com.programacaobrasil.agendacontato.dominio.entidades.Contato;

public class RepositorioContato {

    private SQLiteDatabase conn;

    public RepositorioContato(SQLiteDatabase conn)
    {
        this.conn = conn;
    }

    public void inserirContato(Contato contato){
        ContentValues values = new ContentValues();

        values.put("NOME", contato.getNome());
        values.put("TELEFONE", contato.getTelefone());
        values.put("TIPOTELEFONE", contato.getTipoTelefone());
        values.put("EMAIL", contato.getEmail());
        values.put("TIPOEMAIL", contato.getTipoEmail());
        values.put("ENDERECO", contato.getEndereco());
        values.put("TIPOENDERECO", contato.getTipoEndereco());
        values.put("DATASESPECIAIS", contato.getDatasEspeciais().getTime());
        //values.put("TIPODATASESPECIAIS", contato.getTipoDatasEspeciais());
        values.put("GRUPOS", contato.getGrupos());

        conn.insertOrThrow("CONTATO", null, values);
    }

    public ArrayAdapter<Contato> buscarContatos(Context context){
        ArrayAdapter<Contato> adpContatos = new ArrayAdapter<Contato>(context, android.R.layout.simple_list_item_1);

        Cursor cursor = conn.query("CONTATO", null, null, null, null, null, null);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();

            do {
                Contato contato = new Contato();
                contato.setNome(cursor.getString(1));
                contato.setTelefone(cursor.getString(2));

                adpContatos.add(contato);
            }
            while (cursor.moveToNext());
        }

        return  adpContatos;
    }
}
