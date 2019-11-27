package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.ADateField;
import br.com.microserv.framework.msvannotation.ADoubleField;
import br.com.microserv.framework.msvannotation.AIntegerField;
import br.com.microserv.framework.msvannotation.ALongField;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvannotation.APrimaryKey;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvannotation.AWebapiField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 23/11/2016.
 */

@AClass( table = "Financeiro" )
public class tpFinanceiro extends tpBase implements tpInterface {

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
    public long IdEmpresa;
    // endregion

    // region IdDocumento
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
    public long IdDocumento;
    // endregion

    // region IdCliente
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
    public long IdCliente;
    // endregion

    // region Numero
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
    public String Numero;
    // endregion

    // region Parcela
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
    public String Parcela;
    // endregion

    // region Valor
    @ADoubleField(
            initialValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public double Valor;
    // endregion

    // region EmissaoData
    @ADateField(
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String EmissaoData;
    // endregion

    // region VencimentoData
    @ADateField(
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String VencimentoData;
    // endregion

}
