package br.com.microserv.framework.msvdal;

import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpTabelaPrecoProduto;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.WhereItem;
import br.com.microserv.framework.msvutil.eSQLConditionType;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.framework.msvutil.eSQLiteDataType;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbTabelaPrecoProduto extends dbBase implements dbInterface {

    // region Constructor
    public dbTabelaPrecoProduto(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion

    // region newDTO
    @Override
    public tpInterface newDTO(){

        return new tpTabelaPrecoProduto();

    }
    // endregion

}
