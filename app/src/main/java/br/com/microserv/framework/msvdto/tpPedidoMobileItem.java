package br.com.microserv.framework.msvdto;

import java.io.Serializable;
import java.util.ArrayList;

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
 * Created by notemsv01 on 21/11/2016.
 */

@AClass( table = "PedidoMobileItem" )
public class tpPedidoMobileItem
        extends tpBase
        implements tpInterface {

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

    // region IdPedidoMobileItem
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
    public long IdPedidoMobileItem;
    // endregion

    // region IdPedidoMobile
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
    public long IdPedidoMobile;
    // endregion

    // region IdProduto
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
    public long IdProduto;
    // endregion

    // region PackQuantidade
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
    public int PackQuantidade;
    // endregion

    // region UnidadeValor
    @ADoubleField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public double UnidadeValor;
    // endregion

    // region UnidadeDescontoPercentual
    @ADoubleField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public double UnidadeDescontoPercentual;
    // endregion

    // region UnidadeDescontoValor
    @ADoubleField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public double UnidadeDescontoValor;
    // endregion

    // region UnidadeValorLiquido
    @ADoubleField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public double UnidadeValorLiquido;
    // endregion

    // region UnidadeVendaQuantidade
    @ADoubleField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public double UnidadeVendaQuantidade;
    // endregion

    // region UnidadeValorTotal
    @ADoubleField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public double UnidadeValorTotal;
    // endregion

    // region Observacao
    @ATextField(
            allowNull = true,
            initialValue = "",
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Observacao;
    // endregion

    // region DataAlteracao
    @ADateField(
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String DataAlteracao;
    // endregion

    // region UsuarioAlteracao
    @ATextField(
            allowNull = true,
            initialValue = "",
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String UsuarioAlteracao;
    // endregion


    // ----------------------------------------------- //


    // region tpProduto
    public tpProduto Produto;
    // endregion

}