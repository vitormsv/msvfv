package br.com.microserv.framework.msvdto;

import android.net.Uri;

import java.lang.reflect.Array;
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
 * Created by notemsv01 on 21/11/2016.
 */

@AClass( table = "Cliente" )
public class tpCliente extends tpBase implements tpInterface {

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


    // region Codigo
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String Codigo;
    // endregion


    // region RazaoSocial
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String RazaoSocial;
    // endregion


    // region NomeFantasia
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    @AListIdentifierField
    public String NomeFantasia;
    // endregion


    // region CnpjCpf
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String CnpjCpf;
    // endregion


    // region IeRg
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String IeRg;
    // endregion


    // region LogradouroTipo
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String LogradouroTipo;
    // endregion


    // region LogradouroNome
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String LogradouroNome;
    // endregion


    // region LogradouroNumero
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String LogradouroNumero;
    // endregion


    // region BairroNome
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String BairroNome;
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
            delete = true
    )
    @AWebapiField
    public long IdCidade;
    // endregion


    // region Cep
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String Cep;
    // endregion


    // region IdRegiao
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
    public int IdRegiao;
    // endregion


    // region TelefoneFixo
    @ATextField(
            initialValue = "",
            allowNull = true,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String TelefoneFixo;
    // endregion


    // region TelefoneCelular
    @ATextField(
            initialValue = "",
            allowNull = true,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String TelefoneCelular;
    // endregion


    // region Email
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String Email;
    // endregion


    // region ContatoNome
    @ATextField(
            initialValue = "",
            allowNull = false,
            minLength = 0,
            maxLength = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String ContatoNome;
    // endregion


    // region IdTabelaPreco
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
    public long IdTabelaPreco;
    // endregion


    // region IdCondicaoPagamentoPadrao
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
    public long IdCondicaoPagamentoPadrao;
    // endregion


    // region DescontoPadrao
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
    public double DescontoPadrao;
    // endregion


    // --------------------------------------------------- //


    // region Cidade
    public tpCidade Cidade;
    // endregion

    // region Regiao
    public tpRegiao Regiao;
    // endregion

    // region CondicaoPagamentoPadrao
    public tpCondicaoPagamento CondicaoPagamentoPadrao;
    // endregion

    // region Tabela de preço da empresa
    public tpTabelaPreco TabelaPrecoEmpresa;
    // endregion

    // region Tabelas de Preço
    public ArrayList<tpTabelaPreco> Tabelas;
    // endregion


    // --------------------------------------------------- //


    // region getEndereco
    public String getEndereco() {

        String _out = "";

        _out = this.LogradouroTipo
                + " "
                + this.LogradouroNome
                + ", "
                + this.LogradouroNumero;

        return _out;

    }
    // endregion


    // region getEnderecoEncoded
    public String getEnderecoCompleto() throws Exception {

        String _out = "";

        if (Cidade == null) {
            throw new Exception("[tpCliente|getEnderecoEncoded] Objeto Cidade não instânciado");
        }

        _out = this.LogradouroTipo
                + " "
                + this.LogradouroNome
                + ", "
                + this.LogradouroNumero
                + " "
                + this.BairroNome
                + " "
                + this.Cidade.Descricao
                + " "
                + this.Cidade.EstadoSigla;

        return _out;

    }
    // endregion

}
