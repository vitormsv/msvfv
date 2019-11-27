package br.com.microserv.framework.msvdal;

import android.database.Cursor;

import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpFinanceiro;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.WhereItem;
import br.com.microserv.framework.msvutil.eSQLConditionType;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.framework.msvutil.eSQLiteDataType;

/**
 * Created by notemsv01 on 17/11/2016.
 */

public class dbFinanceiro extends dbBase implements dbInterface {

    // region Constructor
    public dbFinanceiro(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO(){

        return new tpFinanceiro();

    }
    // endregion

    // region getListByIdCliente
    public List<tpFinanceiro> getListByIdCliente(int id) {

        List<tpFinanceiro> _out = null;

        WhereItem _wi = new WhereItem();
        _wi.field = "IdCliente";
        _wi.dataType = eSQLiteDataType.INTEGER;
        _wi.conditionType = eSQLConditionType.EQUAL;
        _wi.value1 = id;


        try {
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addWhere(_wi);
            _sch.addOrderBy("EmissaoVencimento", eSQLSortType.ASC);
            _sch.addOrderBy("Numero", eSQLSortType.ASC);

            _out = this.getList(tpFinanceiro.class, _sch);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    // region
    public List<tpFinanceiro> getListaDeDevedores(int idEmpresa) {

        List<tpFinanceiro> _out = null;
        Cursor _c = null;

        StringBuilder _sb = new StringBuilder();
        _sb.append("     SELECT Fin._id, ");
        _sb.append(" 	        Cli.Codigo, ");
        _sb.append("            Cli.RazaoSocial, ");
        _sb.append(" 		    Cli.NomeFantasia, ");
        _sb.append(" 		    Cid.Descricao, ");
        _sb.append(" 		    Fin.Numero, ");
        _sb.append(" 		    Fin.Parcela, ");
        _sb.append(" 		    Fin.Valor, ");
        _sb.append(" 		    Fin.EmissaoData, ");
        _sb.append(" 		    Fin.VencimentoData, ");
        _sb.append(" 		        CAST( JULIANDAY(VencimentoData) - JULIANDAY('now') as int ) AS VencidoEmDias ");
        _sb.append(" 	   FROM Financeiro AS Fin ");
        _sb.append(" INNER JOIN Cliente    AS Cli ON Fin.IdCliente = Cli.IdCliente ");
        _sb.append(" INNER JOIN Cidade     AS Cid ON Cli.IdCidade  = Cid.IdCidade ");
        _sb.append(" 	  WHERE Fin.IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("        AND DATE( Fin.VencimentoData ) < DATE( 'now' ) ");
        _sb.append("   ORDER BY Fin.VencimentoData ASC ");


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                _c.moveToFirst();
                //_out = _c.getInt(_c.getColumnIndexOrThrow("Quantidade"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;


    }
    // endregion
}
