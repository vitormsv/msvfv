package br.com.microserv.framework.msvinterface;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ricardo on 11/11/2015.
 */
public interface DBHelperInterface {

    // region method open
    /*
     * Metodo responsavel por abrir a conexao com o
     * banco de dados
     */
    public void open(boolean isWritable);
    // endregion

    // region method isOpen
    /*
     * Metodo responsavel por verificar se a conexao
     * com o banco de dados esta aberta
     */
    public boolean isOpen();
    // endregion

    // region method close
    /*
     * Metodo responsavel por fechar uma conexao que esta
     * aberta com o banco de dados
     */
    public void close();
    // endregion

    // region method insert
    /*
     * Metodo responsavel por postar os dados de um
     * objeto de transporte em uma determinada tabela
     */
    public long insert(tpInterface tp);
    // endregion
}
