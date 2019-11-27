package br.com.microserv.framework.msvdal;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpClienteListaRow;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class dbCidade extends dbBase implements dbInterface {

    public dbCidade(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }


    @Override
    public tpInterface newDTO(){

        return new tpCidade();

    }


    @Override
    public void loadDepencies(tpInterface tp) throws Exception {
        super.loadDepencies(tp);
    }


    // region getByIdRegiao
    public List<tpCidade> getByIdRegiao(int idRegiao){

        List<tpCidade> _out = null;
        Cursor _c = null;

        StringBuilder _sb = new StringBuilder();
        _sb.append("     SELECT DISTINCT ");
        _sb.append("            Cid.* ");
        _sb.append("       FROM Cliente AS Cli ");
        _sb.append(" INNER JOIN Cidade  AS Cid ON Cli.IdCidade = Cid.IdCidade ");
        _sb.append("      WHERE Cli.IdRegiao = " + String.valueOf(idRegiao));
        _sb.append("   ORDER BY Cid.Descricao ");

        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                while(_c.moveToNext()) {

                    // region 01 Preenchendo um objeto tpCidade
                    tpCidade _tp = new tpCidade();
                    _tp._id = _c.getLong(_c.getColumnIndexOrThrow("_id"));
                    _tp.IdCidade = _c.getLong(_c.getColumnIndexOrThrow("IdCidade"));
                    _tp.Descricao = _c.getString(_c.getColumnIndexOrThrow("Descricao"));
                    _tp.EstadoSigla = _c.getString(_c.getColumnIndexOrThrow("EstadoSigla"));
                    // endregion

                    // region 02 Adicionando o objeto preenchido na lista de retorno
                    if (_out == null) {
                        _out = new ArrayList<tpCidade>();
                    }

                    _out.add(_tp);
                    // endregion

                }

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
