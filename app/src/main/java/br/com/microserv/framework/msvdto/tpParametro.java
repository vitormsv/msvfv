package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.AIntegerField;
import br.com.microserv.framework.msvannotation.ALongField;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvannotation.APrimaryKey;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvannotation.AWebapiField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

@AClass( table = "Parametro" )
public class tpParametro extends tpBase implements tpInterface {

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


    // region Nome
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
    public String Nome;
    // endregion


    // region ValorTexto
    @ATextField(
            initialValue = "",
            allowNull = true
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String ValorTexto;
    // endregion


    // region ValorInteiro
    @AIntegerField(
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
    public int ValorInteiro;
    // endregion


    // region ValorReal
    @AIntegerField(
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
    public double ValorReal;
    // endregion

}
