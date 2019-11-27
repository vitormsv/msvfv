package br.com.microserv.framework.msvdal;

import android.database.Cursor;

import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.WhereItem;
import br.com.microserv.framework.msvutil.eSQLConditionType;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.framework.msvutil.eSQLiteDataType;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbTabelaPreco extends dbBase implements dbInterface {

    // region Constructor
    public dbTabelaPreco(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO(){

        return new tpTabelaPreco();

    }
    // endregion

    // region getListByIdEmpresa
    public List<tpTabelaPreco> getListByIdEmpresa(int id) {

        List<tpTabelaPreco> _out = null;


        WhereItem _wi = new WhereItem();
        _wi.field = "IdEmpresa";
        _wi.dataType = eSQLiteDataType.INTEGER;
        _wi.conditionType = eSQLConditionType.EQUAL;
        _wi.value1 = id;


        try {
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addWhere(_wi);
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            _out = this.getList(tpTabelaPreco.class, _sch);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return _out;

    }
    // endregion

    // region getTabelaDoCliente
    public tpTabelaPreco getTabelaDoCliente(long IdCliente, long IdEmpresa) {

        // region Declarando variaveis locais
        tpTabelaPreco _tp = null;
        Cursor _c = null;
        // endregion


        // region Definindo a sentença para selecionar a tabela do cliente
        StringBuilder _sb = new StringBuilder();
        _sb.append("     SELECT Tpr.* ");
        _sb.append("       FROM TabelaPreco        AS Tpr ");
        _sb.append(" INNER JOIN ClienteTabelaPreco AS Ctp ON Tpr.IdTabelaPreco = Ctp.IdTabelaPreco ");
        _sb.append("      WHERE Tpr.IdEmpresa = " + String.valueOf(IdEmpresa));
        _sb.append("        AND Ctp.IdCliente = " + String.valueOf(IdCliente));
        // endregion


        // region Bloco protegido que irá selecionar a informação no banco de dados
        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null && _c.moveToFirst()) {
                _tp = new tpTabelaPreco();
                this.fill(_c, _tp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false) {
                _c.close();
            }
        }
        // endregion


        // region Devolvendo o retorno a quem invocou o método
        return _tp;
        // endregion

    }
    // endregion

}
