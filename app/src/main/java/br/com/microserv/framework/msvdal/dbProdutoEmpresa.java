package br.com.microserv.framework.msvdal;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpBairro;
import br.com.microserv.framework.msvdto.tpProdutoEmpresa;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class dbProdutoEmpresa extends dbBase implements dbInterface {

    // region Constructor
    public dbProdutoEmpresa(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO() {

        return new tpProdutoEmpresa();

    }
    // endregion

}
