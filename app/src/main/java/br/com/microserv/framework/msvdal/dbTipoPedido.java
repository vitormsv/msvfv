package br.com.microserv.framework.msvdal;

import android.database.Cursor;

import java.lang.reflect.Array;
import java.util.ArrayList;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbTipoPedido extends dbBase implements dbInterface {

    // region Constructor
    public dbTipoPedido(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion


    // region newDTO
    @Override
    public tpInterface newDTO(){
        return new tpTipoPedido();
    }
    // endregion


    // region getTotalPorTipoPedido
    public ArrayList<tpTipoPedido> getTotalPorTipoPedido(long idEmpresa) {

        // region Declarando variável de retorno
        ArrayList<tpTipoPedido> _out = null;
        // endregion

        // region Declarando locais do método
        Cursor _crs = null;
        StringBuilder _sb = null;
        // endregion


        // region Montando a sentença que deverá ser executada no banco de dados
        _sb = new StringBuilder();
        _sb.append("     SELECT Tpe._id, ");
        _sb.append("            Tpe.IdTipoPedido, ");
        _sb.append("            Tpe.Descricao, ");
        _sb.append("                COUNT( 1 ) AS PedidoQuantidade, ");
        _sb.append("                SUM( Pmo.TotalValorLiquido ) AS ValorTotal ");
        _sb.append("       FROM PedidoMobile AS Pmo ");
        _sb.append(" INNER JOIN TipoPedido   AS Tpe ON Pmo.IdTipoPedido = Tpe.IdTipoPedido ");
        _sb.append("      WHERE Pmo.IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("   GROUP BY Tpe._id, ");
        _sb.append("            Tpe.IdTipoPedido, ");
        _sb.append("            Tpe.Descricao ");
        _sb.append("   ORDER BY Tpe.Descricao ");
        // endregion


        // region Bloco protegido
        try {

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {

                    tpTipoPedido _tp = new tpTipoPedido();

                    _tp._id = _crs.getLong(_crs.getColumnIndexOrThrow("_id"));
                    _tp.IdTipoPedido = _crs.getLong(_crs.getColumnIndexOrThrow("IdTipoPedido"));
                    _tp.Descricao = _crs.getString(_crs.getColumnIndexOrThrow("Descricao"));
                    _tp.PedidoQuantidade = _crs.getInt(_crs.getColumnIndexOrThrow("PedidoQuantidade"));
                    _tp.ValorTotal = _crs.getDouble(_crs.getColumnIndexOrThrow("ValorTotal"));

                    if (_out == null) {
                        _out = new ArrayList<tpTipoPedido>();
                    }

                    _out.add(_tp);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if ((_crs != null) && (_crs.isClosed() == false)) {
                _crs.close();
            }

        }
        // endregion

        return _out;

    }
    // endregion
}
