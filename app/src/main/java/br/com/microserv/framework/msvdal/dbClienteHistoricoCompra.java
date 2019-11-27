package br.com.microserv.framework.msvdal;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpClienteHistoricoCompra;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class dbClienteHistoricoCompra extends dbBase implements dbInterface {

    public dbClienteHistoricoCompra(SQLiteHelper dbHelper) throws Exception {

        super(dbHelper);

    }


    @Override
    public tpInterface newDTO() {

        return new tpClienteHistoricoCompra();

    }

}
