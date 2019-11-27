package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.ADoubleField;
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
 * Created by notemsv01 on 22/11/2016.
 */

@AClass( table = "PedidoMobileItemHistorico" )
public class tpPedidoMobileItemHistorico
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

    // region IdPedidoMobileItemHistorico
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
    public long IdPedidoMobileItemHistorico;
    // endregion

    // region IdPedidoMobileHistorico
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
    public long IdPedidoMobileHistorico;
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
    @AWebapiField
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
    @AWebapiField
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
    @AWebapiField
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
    @AWebapiField
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
    @AWebapiField
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
    @AWebapiField
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
    @AWebapiField
    public double UnidadeValorTotal;
    // endregion

    // region Observacao
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
    public String Observacao;
    // endregion


    // ------------------------------------------------- //
    // Campos destinados as descrições auxiliáres
    // ------------------------------------------------- //


    // region ProdutoCodigo
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
    public String ProdutoCodigo;
    // endregion

    // region ProdutoDescricao
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
    public String ProdutoDescricao;
    // endregion

    // region UnidadeMedida
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
    public String UnidadeMedida;
    // endregion

}
