package br.com.microserv.framework.msvdal;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpTransportadora;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbTransportadora extends dbBase implements dbInterface {

    // region Constructor
    public dbTransportadora(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO(){
        return new tpTransportadora();
    }
    // endregion

}
