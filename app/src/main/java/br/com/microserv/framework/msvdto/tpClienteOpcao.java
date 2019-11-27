package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AIntegerField;
import br.com.microserv.framework.msvannotation.ALongField;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 20/11/2016.
 */

public class tpClienteOpcao extends tpBase implements tpInterface {

    // region Construtor
    public tpClienteOpcao(){
        super();
    }
    // endregion


    // region Propriedades publicadas

    // region _id
    @ALongField(initialValue = 0, minValue = 0, maxValue = 999)
    public long _id;
    // endregion


    // region Descricao
    @ATextField(initialValue = "", minLength = 0, maxLength = 0)
    public String Descricao;
    // endregion


    // region SetorOrigem
    @ATextField(initialValue = "", minLength = 0, maxLength = 0)
    public String SetorOrigem;
    // endregion


    // region IdOrigem
    @ALongField(initialValue = 0, minValue = 0, maxValue = 999)
    public long IdOrigem;
    // endregion

    // endregion

}
