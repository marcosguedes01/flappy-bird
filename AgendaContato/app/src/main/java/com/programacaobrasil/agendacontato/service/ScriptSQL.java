package com.programacaobrasil.agendacontato.service;

/**
 * Created by marcos.guedes on 05/10/2017.
 */

public class ScriptSQL {

    public static String getCreateContato()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(" CREATE TABLE IF NOT EXISTS CONTATO ( ");
        sb.append(" _id 				INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" NOME			    VARCHAR(200), ");
        sb.append(" TELEFONE			VARCHAR(14), ");
        sb.append(" TIPOTELEFONE		VARCHAR(1), ");
        sb.append(" EMAIL				VARCHAR(255), ");
        sb.append(" TIPOEMAIL			VARCHAR(1), ");
        sb.append(" ENDERECO			VARCHAR(255), ");
        sb.append(" TIPOENDERECO		VARCHAR(1), ");
        sb.append(" DATASESPECIAIS		DATE ");
        sb.append(" TIPODATASESPECIAIS	VARCHAR(1), ");
        sb.append(" GRUPOS				VARCHAR(255) ");
        sb.append(" ); ");

        return sb.toString();
    }
}
