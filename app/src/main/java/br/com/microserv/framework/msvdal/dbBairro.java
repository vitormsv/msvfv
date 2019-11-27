package br.com.microserv.framework.msvdal;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpBairro;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class dbBairro extends dbBase implements dbInterface {

    public dbBairro(SQLiteHelper dbHelper) throws Exception{

        super(dbHelper);

    }


    @Override
    public tpInterface newDTO() {

        return new tpBairro();

    }

}
