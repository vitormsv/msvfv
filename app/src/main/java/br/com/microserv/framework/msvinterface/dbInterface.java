package br.com.microserv.framework.msvinterface;

import android.database.Cursor;

import br.com.microserv.framework.msvutil.SQLClauseHelper;

/**
 * Created by Ricardo on 21/10/2016.
 */
public interface dbInterface {

    /*
     * Metodo responsavel por retornar todas
     * as colunas e linha de uma determinada tabela
     */
    public abstract Cursor getAll() throws Exception;


    /*
     * Metodo responsavel por retornar todas
     * as colunas e linha de uma determinada tabela
     */
    public abstract Cursor getAll(SQLClauseHelper sqlClauseHelper) throws Exception;
}
