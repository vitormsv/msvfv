package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AWebapiField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class tpBIVendedor extends tpBase implements tpInterface {


    // region _id
    @AWebapiField
    public long _id;
    // endregion


    // region Codigo
    @AWebapiField
    public String Codigo;
    // endregion


    // region RazaoSocial
    @AWebapiField
    public String RazaoSocial;
    // endregion


}
