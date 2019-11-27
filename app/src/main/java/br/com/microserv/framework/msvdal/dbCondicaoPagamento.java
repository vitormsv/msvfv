package br.com.microserv.framework.msvdal;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpClienteListaRow;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.MSVUtil;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbCondicaoPagamento extends dbBase implements dbInterface {

    // region Constructor
    public dbCondicaoPagamento(SQLiteHelper dbHelper) throws Exception{
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO(){

        return new tpCondicaoPagamento();

    }
    // endregion

}
