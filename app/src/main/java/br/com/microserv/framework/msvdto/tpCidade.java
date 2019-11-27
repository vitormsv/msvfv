package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.AIntegerField;
import br.com.microserv.framework.msvannotation.AListIdentifierField;
import br.com.microserv.framework.msvannotation.ALongField;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvannotation.APrimaryKey;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvannotation.AWebapiField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 14/11/2016.
 */
@AClass( table = "Cidade" )
public class tpCidade extends tpBase implements tpInterface {

    // region _id
    @ALongField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = false,
            update = true,
            delete = true
    )
    @APrimaryKey
    public long _id;
    // endregion

    // region IdCidade
    @ALongField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public long IdCidade;
    // endregion

    // region Descricao
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    @AListIdentifierField
    public String Descricao;
    // endregion

    // region EstadoSigla
    @ATextField(
            initialValue = "",
            maxLength = 2,
            minLength = 0,
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String EstadoSigla;
    // endregion
}
