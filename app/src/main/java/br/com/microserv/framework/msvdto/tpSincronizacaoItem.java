package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.AIntegerField;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvannotation.APrimaryKey;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class tpSincronizacaoItem {

    // region _id
    public long _id;
    // endregion


    // region Tabela
    public String Tabela;
    // endregion


    // region Registro
    public long Registro;
    // endregion


    // region Ok
    public boolean Ok;
    // endregion


    // region Constructor
    public tpSincronizacaoItem(int id, String tabela) {

        this._id = id;
        this.Tabela = tabela;
        this.Registro = 0;
        this.Ok = false;

    }
    // endregion
}
