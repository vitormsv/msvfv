package br.com.microserv.framework.msvdal;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by Ricardo on 27/10/2016.
 */
public class dbVendedor extends dbBase implements dbInterface {

    // region Constructor
    public dbVendedor(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO(){
        return new tpVendedor();
    }
    // endregion

}
