package br.com.microserv.framework.msvhelper;

import br.com.microserv.framework.msvinterface.DBHelperInterface;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by Ricardo on 11/11/2015.
 */
public class SQLServerHelper implements DBHelperInterface {

    @Override
    public void open(boolean isWritable) {

    }

    @Override
    public boolean isOpen() {
        return false;
    }


    @Override
    public void close() {
        // ToDo
    }

    @Override
    public long insert(tpInterface tp) {
        return 0;
    }


}
