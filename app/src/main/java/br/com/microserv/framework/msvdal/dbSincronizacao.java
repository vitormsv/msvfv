package br.com.microserv.framework.msvdal;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpSincronizacao;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.MSVUtil;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class dbSincronizacao extends dbBase implements dbInterface {

    // region Constructor
    public dbSincronizacao(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion


    // region newDTO
    @Override
    public tpInterface newDTO() {
        return new tpSincronizacao();
    }
    // endregion


    // region getTop20
    public List<tpSincronizacao> getTop20() {

        // region Declarando variaveis locais
        List<tpSincronizacao> _out = null;
        Cursor _crs = null;
        StringBuilder _sb = null;
        // endregion

        // region Formatando a senteça para seleção de dados no banco
        _sb = new StringBuilder();

        _sb.append("   SELECT * ");
        _sb.append("     FROM Sincronizacao ");
        _sb.append(" ORDER BY _id DESC ");
        _sb.append("    LIMIT 10 ");
        // endregion

        // region Bloco protegido
        try {

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {

                    tpSincronizacao _tp = new tpSincronizacao();

                    _tp._id = _crs.getLong(_crs.getColumnIndexOrThrow("_id"));
                    _tp.IdVendedor = _crs.getLong(_crs.getColumnIndexOrThrow("IdVendedor"));
                    _tp.InicioDataHora = _crs.getString(_crs.getColumnIndexOrThrow("InicioDataHora"));
                    _tp.TerminoDataHora = _crs.getString(_crs.getColumnIndexOrThrow("TerminoDataHora"));
                    _tp.Latitude = _crs.getDouble(_crs.getColumnIndexOrThrow("Latitude"));
                    _tp.Longitude = _crs.getDouble(_crs.getColumnIndexOrThrow("Longitude"));

                    if (_out == null) {
                        _out = new ArrayList<tpSincronizacao>();
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

        // region Final
        return _out;
        // endregion

    }
    // endregion


    // region getLast
    public tpSincronizacao getLast() {

        // region Declarando variaveis locais
        tpSincronizacao _out = null;
        Cursor _crs = null;
        StringBuilder _sb = null;
        // endregion

        // region Formatando a senteça para seleção de dados no banco
        _sb = new StringBuilder();

        _sb.append("   SELECT * ");
        _sb.append("     FROM Sincronizacao ");
        _sb.append(" ORDER BY _id DESC ");
        _sb.append("    LIMIT 1 ");
        // endregion

        // region Bloco protegido
        try {

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {

                    _out = new tpSincronizacao();

                    _out._id = _crs.getLong(_crs.getColumnIndexOrThrow("_id"));
                    _out.IdVendedor = _crs.getLong(_crs.getColumnIndexOrThrow("IdVendedor"));
                    _out.InicioDataHora = _crs.getString(_crs.getColumnIndexOrThrow("InicioDataHora"));
                    _out.TerminoDataHora = _crs.getString(_crs.getColumnIndexOrThrow("TerminoDataHora"));
                    _out.Latitude = _crs.getDouble(_crs.getColumnIndexOrThrow("Latitude"));
                    _out.Longitude = _crs.getDouble(_crs.getColumnIndexOrThrow("Longitude"));

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

        // region Final
        return _out;
        // endregion

    }
    // endregion


    // region isSyncToday
    public boolean isSyncToday() throws Exception {

        boolean _out = false;

        /*
        String _ymdToday = null;
        String _ymdSinc = null;

        tpSincronizacao _tp = null;


        try {

            _tp = this.getLast();

            if (_tp != null) {

                _ymdToday = MSVUtil.ymdhmsTOymd(MSVUtil.sqliteHojeHora());
                _ymdSinc = MSVUtil.ymdhmsTOymd(_tp.TerminoDataHora);

                if (_ymdSinc.compareTo(_ymdToday) == 0) {
                    _out = true;
                }

            }

        } catch (Exception e) {

            throw new Exception("[dbSincronizacao|isSyncToday] Erro ao validar a data de sincronização " + e.getMessage());

        }
        */

        _out = true;


        return _out;
    }
    // endregion

}
