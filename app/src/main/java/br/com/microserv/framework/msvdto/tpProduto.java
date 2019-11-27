package br.com.microserv.framework.msvdto;

import java.util.ArrayList;

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
 * Created by notemsv01 on 17/11/2016.
 */

@AClass( table = "Produto" )
public class tpProduto extends tpBase implements tpInterface {

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


    // region IdGrupo
    @ALongField(
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
    public long IdGrupo;
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


    // region Codigo
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 10
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String Codigo;
    // endregion


    // region Ean13
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 13,
            maxLength = 13
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String Ean13;
    // endregion


    // region Descricao
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 100
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


    // region UnidadeMedida
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 20
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String UnidadeMedida;
    // endregion


    // region PermiteDecimal
    @AIntegerField(
            initialValue = 0,
            maxValue = 0,
            minValue = 1
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public int PermiteDecimal;
    // endregion


    // region PackQuantidade
    @AIntegerField(
            initialValue = 0,
            minValue = 0,
            maxValue = 1000
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public int PackQuantidade;
    // endregion


    // region VendaQuantidadeMinima
    @AIntegerField(
            initialValue = 0,
            minValue = 1,
            maxValue = 1000
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public int VendaQuantidadeMinima;
    // endregion


    // region VendaMultiploDe
    @AIntegerField(
            initialValue = 0,
            minValue = 1,
            maxValue = 1000
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public int VendaMultiploDe;
    // endregion


    // region PesoBruto
    @ADoubleField(
            initialValue = 0,
            minValue = 1,
            maxValue = 1000
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public double PesoBruto;
    // endregion


    // region SaldoQuantidade
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
    public double SaldoQuantidade;
    // endregion

    // --------------------------- //


    // region Linha
    public tpLinha Linha;
    // endregion

    // region Grupo
    public tpGrupo Grupo;
    // endregion

    //region TabelaPreco
    public tpTabelaPrecoProduto TabelaPrecoProduto;
    // endregion


    // --------------------------- //


    // region Empresas em que o produto está vinculado
    public ArrayList<tpProdutoEmpresa> Empresas;
    // endregion

}
