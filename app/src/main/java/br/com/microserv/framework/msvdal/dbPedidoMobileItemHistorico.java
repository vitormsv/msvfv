package br.com.microserv.framework.msvdal;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpPedidoMobileItemHistorico;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class dbPedidoMobileItemHistorico extends dbBase implements dbInterface {

    private SQLiteHelper _dbHelper = null;

    // region Constructor
    public dbPedidoMobileItemHistorico(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
        _dbHelper = dbHelper;
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO() {
        return new tpPedidoMobileItemHistorico();
    }
    // endregion

}
