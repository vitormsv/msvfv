package br.com.microserv.framework.msvdal;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpGrupo;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbEmpresa extends dbBase implements dbInterface {

    // region Constructor
    public dbEmpresa(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO(){

        return new tpEmpresa();

    }
    // endregion

}
