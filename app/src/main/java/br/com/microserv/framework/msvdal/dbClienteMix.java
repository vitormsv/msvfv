package br.com.microserv.framework.msvdal;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpClienteMix;
import br.com.microserv.framework.msvdto.tpClienteProdutoMixRow;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.SQLClauseHelper;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbClienteMix extends dbBase implements dbInterface {


    // region Método Construtor
    public dbClienteMix(SQLiteHelper dbHelper) throws Exception {

        super(dbHelper);

    }
    // endregion


    // region Método newDTO
    @Override
    public tpInterface newDTO(){

        return new tpClienteMix();

    }
    // endregion


    // region setQuantityStock
    // ######## CORRIGIR ESTE METODO ########
    public boolean setQuantityStock(tpClienteMix tp) throws Exception {

        // region Declarando variáveis
        boolean _out = false;

        int _rows = 0;

        String _table = null;
        String _whereClause = null;
        String[] _whereArgs = null;

        ContentValues _values = null;
        // endregion


        // region Montando os valores para informar que o pedido foi sincronizado

        // Tabela
        _table = "ClienteProdutoMix";

        // Valores que deverão ser alterados
        _values = new ContentValues();
        _values.put("EstoqueQuantidade", tp.EstoqueQuantidade);
        _values.put("PedidoQuantidade", tp.PedidoQuantidade);
        _values.put("EhItemConfirmado", tp.EhItemConfirmado);

        // Condição where
        _whereClause = "IdCliente=? AND IdProduto=?";

        // Valores para substituição na condição where
        _whereArgs = new String[]{
                String.valueOf(tp.IdCliente),
                String.valueOf(tp.IdProduto)
        };

        // endregion


        // region Invocando o método update da classe dbBase
        try {

            _rows = update(_table, _values, _whereClause, _whereArgs);

        } catch (Exception e) {

            throw new Exception("dbClienteMix | setQuantityStock | " + e.getMessage());

        }
        // endregion


        // region Devolvendo o resultado do processamento
        return (_rows > 0);
        // endregion

    }
    // endregion


    // region clearStockInformation
    // ######## CORRIGIR ESTE METODO ########
    public boolean clearStockInformation(long idCliente) throws Exception {

        // region Declarando variáveis
        boolean _out = false;

        int _rows = 0;

        String _table = null;
        String _whereClause = null;
        String[] _whereArgs = null;

        ContentValues _values = null;
        // endregion


        // region Montando os valores para informar que o pedido foi sincronizado

        // Tabela
        _table = "ClienteProdutoMix";

        // Valores que deverão ser alterados
        _values = new ContentValues();
        _values.put("EstoqueQuantidade", 0);
        _values.put("EstoqueDataHora", 0);
        _values.put("ComprarQuantidade", 0);

        // Condição where
        _whereClause = "IdCliente=?";

        // Valores para substituição na condição where
        _whereArgs = new String[]{
                String.valueOf(idCliente)
        };

        // endregion


        // region Invocando o método update da classe dbBase
        try {
            _rows = update(_table, _values, _whereClause, _whereArgs);
        } catch (Exception e) {
            throw new Exception("dbClienteMix | clearStockInformation() | " + e.getMessage());
        }
        // endregion


        // region Devolvendo o resultado do processamento
        return (_rows > 0);
        // endregion

    }
    // endregion

}
