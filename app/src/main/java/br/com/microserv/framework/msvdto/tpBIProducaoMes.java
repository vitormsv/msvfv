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

public class tpBIProducaoMes extends tpBase implements tpInterface {

    // region PRINCIPAL

    // region _id
    @AWebapiField
    public long _id;
    // endregion


    // region Mes
    @AWebapiField
    public int Mes;
    // endregion


    // region MesExtenso
    @AWebapiField
    public String MesExtenso;
    // endregion


    // region DiasTrabalhados
    @AWebapiField
    public int DiasTrabalhados;
    // endregion


    // region PedidosEmitidos
    @AWebapiField
    public int PedidosEmitidos;
    // endregion


    // region ValorTotalEmitido
    @AWebapiField
    public double ValorTotalEmitido;
    // endregion

    // endregion


    // region VENDA

    // region VendaValorTotal
    @AWebapiField
    public double VendaValorTotal;
    // endregion


    // region VendaQuantidadePedidos
    @AWebapiField
    public int VendaQuantidadePedidos;
    // endregion


    // region VendaPercentual
    @AWebapiField
    public double VendaPercentual;
    // endregion

    // endregion


    // region BONIFICACAO

    // region BonificacaoValorTotal
    @AWebapiField
    public double BonificacaoValorTotal;
    // endregion


    // region BonificacaoQuantidadePedidos
    @AWebapiField
    public int BonificacaoQuantidadePedidos;
    // endregion


    // region BonificacaoPercentual
    @AWebapiField
    public double BonificacaoPercentual;
    // endregion

    // endregion


    // region TROCA

    // region TrocaValorTotal
    @AWebapiField
    public double TrocaValorTotal;
    // endregion


    // region TrocaQuantidadePedidos
    @AWebapiField
    public int TrocaQuantidadePedidos;
    // endregion


    // region TrocaPercentual
    @AWebapiField
    public double TrocaPercentual;
    // endregion

    // endregion


    // region OUTROS

    // region OutrosValorTotal
    @AWebapiField
    public double OutrosValorTotal;
    // endregion


    // region OutrosQuantidadePedidos
    @AWebapiField
    public int OutrosQuantidadePedidos;
    // endregion


    // region OutrosPercentual
    @AWebapiField
    public double OutrosPercentual;
    // endregion

    // endregion

}
