package br.com.microserv.framework.msvdal;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpClienteTabelaPreco;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class dbClienteTabelaPreco extends dbBase implements dbInterface {

    // region Constructor
    public dbClienteTabelaPreco(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO() {
        return new tpClienteTabelaPreco();
    }
    // endregion

}
