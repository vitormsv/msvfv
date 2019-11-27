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
 * Created by notemsv01 on 17/11/2016.
 */

@AClass( table = "Grupo" )
public class tpGrupo extends tpBase implements tpInterface {

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


    // region IdEmpresa
    @AIntegerField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = true
    )
    @AWebapiField
    public int IdEmpresa;
    // endregion


    // region IdLinha
    @AIntegerField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = true
    )
    @AWebapiField
    public int IdLinha;
    // endregion


    // region IdGrupo
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
    public int IdGrupo;
    // endregion


    // region Codigo
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 4
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String Codigo;
    // endregion


    // region Descricao
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 50
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

}
