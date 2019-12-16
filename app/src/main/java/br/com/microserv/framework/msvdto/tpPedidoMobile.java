package br.com.microserv.framework.msvdto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.ADateField;
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
import br.com.microserv.framework.msvutil.MSVUtil;

/**
 * Created by notemsv01 on 22/11/2016.
 */

@AClass( table = "PedidoMobile" )
public class tpPedidoMobile
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

    // region IdTipoPedido
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
    public long IdTipoPedido;
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
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    @AListIdentifierField
    public String Numero;
    // endregion

    // region NumeroCliente
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
    public String NumeroCliente;
    // endregion

    // region EmissaoDataHora
    @ADateField(
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String EmissaoDataHora;
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
            delete = false
    )
    @AWebapiField
    public long IdTabelaPreco;
    // endregion

    // region IdCondicaoPagamento
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
    public long IdCondicaoPagamento;
    // endregion

    // region IdTransportadora
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
    public long IdTransportadora;
    // endregion

    // region Observacao
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
    public String Observacao;
    // endregion

    // region ItensQuantidade
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
    public int ItensQuantidade;
    // endregion

    // region TotalValor
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
    public double TotalValor;
    // endregion

    // region DescontoPercentual1
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
    public double DescontoPercentual1;
    // endregion

    // region DescontoValor1
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
    public double DescontoValor1;
    // endregion

    // region TotalValorLiquido
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
    public double TotalValorLiquido;
    // endregion

    // region IdVendedor
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
    public long IdVendedor;
    // endregion

    // region EhConfirmado
    @AIntegerField(
            initialValue = 0,
            minValue = 0,
            maxValue = 1
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public int EhConfirmado;
    // endregion

    // region EhSincronizado
    @AIntegerField(
            initialValue = 0,
            minValue = 0,
            maxValue = 1
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public int EhSincronizado;
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
            initialValue = "",
            allowNull = true
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String UsuarioAlteracao;
    // endregion


    // --------------------------------------------------- //


    // region Empresa
    public tpEmpresa Empresa;
    // endregion

    // region TipoPedido
    public tpTipoPedido TipoPedido;
    // endregion

    // region Cliente
    public tpCliente Cliente;
    // endregion

    // region TabelaPreco
    public tpTabelaPreco TabelaPreco;
    // endregion

    // region Condicao de Pagamento
    public tpCondicaoPagamento CondicaoPagamento;
    // endregion

    // region Transportadora
    public tpTransportadora Transportadora;
    // endregion

    // region Vendedor
    public tpVendedor Vendedor;
    // endregion

    // region Itens
    public List<tpPedidoMobileItem> Itens;
    // endregion


    // --------------------------------------------------- //


    // region getSyncJson
    public JSONObject getSyncJson() throws Exception {

        // region Verificando se o objeto itens está nulo
        if (this.Itens == null) {
            throw new Exception("tpPedidoMobile | getSyncJson -> O objeto itens está nullo");
        }
        // endregion

        // region Verificando se o objeto cliente está nulo
        if (this.Cliente == null) {
            throw new Exception("tpPedidoMobile | getSyncJson -> O objeto cliente está nullo");
        }
        // endregion

        // region Escrevendo o json para sincronização
        JSONObject _joOut = null;

        try {

            // region 01 - Gerando os dados dos itens do pedido
            JSONArray _jaItens = new JSONArray();

            for (tpPedidoMobileItem _tpPedidoMobileItem : this.Itens) {

                JSONObject _joItem = new JSONObject();
                _joItem.put("IdPedidoMobileItem", _tpPedidoMobileItem.IdPedidoMobileItem);
                _joItem.put("IdPedidoMobile", _tpPedidoMobileItem.IdPedidoMobile);
                _joItem.put("IdProduto", _tpPedidoMobileItem.IdProduto);
                _joItem.put("PackQuantidade", _tpPedidoMobileItem.PackQuantidade);
                _joItem.put("UnidadeValor", _tpPedidoMobileItem.UnidadeValor);
                _joItem.put("UnidadeDescontoPercentual", _tpPedidoMobileItem.UnidadeDescontoPercentual);
                _joItem.put("UnidadeDescontoValor", _tpPedidoMobileItem.UnidadeDescontoValor);
                _joItem.put("UnidadeValorLiquido", _tpPedidoMobileItem.UnidadeValorLiquido);
                _joItem.put("UnidadeVendaQuantidade", _tpPedidoMobileItem.UnidadeVendaQuantidade);
                _joItem.put("UnidadeValorTotal", _tpPedidoMobileItem.UnidadeValorTotal);
                _joItem.put("Observacao", _tpPedidoMobileItem.Observacao);
                _joItem.put("DataAlteracao", _tpPedidoMobileItem.DataAlteracao);
                _joItem.put("UsuarioAlteracao", _tpPedidoMobileItem.UsuarioAlteracao);

                _jaItens.put(_joItem);

            }
            // endregion

            // region 02 - Gerando os dados do cliente

            // region 02.01 - Validando o IdCliente
            if (String.valueOf(this.Cliente.IdCliente).equals(this.Cliente.Codigo)) {
                this.Cliente.IdCliente = 0;
            }
            // endregion

            // region 02.01 - Montando o JSON
            JSONObject _joCliente = new JSONObject();

            _joCliente.put("IdCliente", this.Cliente.IdCliente);
            _joCliente.put("Codigo", this.Cliente.Codigo);
            _joCliente.put("RazaoSocial", this.Cliente.RazaoSocial);
            _joCliente.put("NomeFantasia", this.Cliente.NomeFantasia);
            _joCliente.put("CnpjCpf", this.Cliente.CnpjCpf);
            _joCliente.put("IeRg", this.Cliente.IeRg);
            _joCliente.put("LogradouroTipo", this.Cliente.LogradouroTipo);
            _joCliente.put("LogradouroNome", this.Cliente.LogradouroNome);
            _joCliente.put("LogradouroNumero", this.Cliente.LogradouroNumero);
            _joCliente.put("BairroNome", this.Cliente.BairroNome);
            _joCliente.put("IdCidade", this.Cliente.IdCidade);
            _joCliente.put("IdRegiao", this.Cliente.IdRegiao);
            _joCliente.put("Cep", MSVUtil.onlyNumber(this.Cliente.Cep));
            _joCliente.put("TelefoneFixo", this.Cliente.TelefoneFixo);
            _joCliente.put("TelefoneCelular", this.Cliente.TelefoneCelular);
            _joCliente.put("Email", this.Cliente.Email);
            _joCliente.put("ContatoNome", this.Cliente.ContatoNome);
            // endregion

            // endregion

            // region 03 - Gerando os dados do pedido
            _joOut = new JSONObject();
            _joOut.put("IdPedidoMobile", this.IdPedidoMobile);
            _joOut.put("IdEmpresa", this.IdEmpresa);
            _joOut.put("IdTipoPedido", this.IdTipoPedido);
            _joOut.put("IdCliente", this.Cliente.IdCliente);
            _joOut.put("Numero", this.Numero);
            _joOut.put("NumeroCliente", this.NumeroCliente);
            _joOut.put("EmissaoDataHora", this.EmissaoDataHora);
            _joOut.put("IdTabelaPreco", this.IdTabelaPreco);
            _joOut.put("IdCondicaoPagamento", this.IdCondicaoPagamento);
            _joOut.put("IdTransportadora", this.IdTransportadora);
            _joOut.put("Observacao", this.Observacao);
            _joOut.put("ItensQuantidade", this.ItensQuantidade);
            _joOut.put("TotalValor", this.TotalValor);
            _joOut.put("DescontoPercentual1", this.DescontoPercentual1);
            _joOut.put("DescontoValor1", this.DescontoValor1);
            _joOut.put("TotalValorLiquido", this.TotalValorLiquido);
            _joOut.put("IdVendedor", this.IdVendedor);
            _joOut.put("EhConfirmado", this.EhConfirmado);
            _joOut.put("EhSincronizado", this.EhSincronizado);
            _joOut.put("DataAlteracao", this.DataAlteracao);
            _joOut.put("UsuarioAlteracao", this.UsuarioAlteracao);
            _joOut.put("Cliente", _joCliente);
            _joOut.put("Itens", _jaItens);
            // endregion

        } catch (Exception e) {
            throw new Exception("tpPedidoMobile | getSyncJson -> " + e.getMessage());
        }
        finally {
            return _joOut;
        }
        // endregion

    }
    // endregion


    // region getHtml
    public StringBuilder getHtml() throws Exception {

        // region Declarando o objeto de retorno
        StringBuilder _html = new StringBuilder();
        // endregion

        // region Verificando se os objetos estrangeiros estão instânciados
        if (this.Empresa == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto empresa está nulo");
        }

        if (this.TipoPedido == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto tipo de pedido está nulo");
        }

        if (this.Cliente == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto cliente está nulo");
        }

        if (this.Cliente.Cidade == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto cidade do cliente " + this.Cliente.RazaoSocial + " está nulo");
        }

        if (this.TabelaPreco == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto tabela de preço está nulo");
        }

        if (this.CondicaoPagamento == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto condição de pagamento está nulo");
        }

        if (this.Transportadora == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto transportadora está nulo");
        }

        if (this.Vendedor == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto vendedor está nulo");
        }

        if (this.Itens == null) {
            throw new Exception("tpPedidoMobile | getHtml -> O objeto itens está nulo");
        }

        for(tpPedidoMobileItem _item : this.Itens) {
            if (_item.Produto == null) {
                throw new Exception("tpPedidoMobile | getHtml -> O objeto produto está nulo para idproduto: " + String.valueOf(_item.IdProduto));
            }
        }
        // endregion

        // region Gerando o html do pedido

        // region Iniciando o html
        _html.append("<!DOCTYPE html>");
        _html.append("<html>");
        // endregion

        // region Cabeçalho do html
        _html.append("<head>");
        _html.append("    <title></title>");
        _html.append("    <meta charset=\"utf-8\" />");
        _html.append("    <style type=\"text/css\">");
        _html.append("        * {");
        _html.append("            font-family: 'Trebuchet MS';");
        _html.append("            font-size: 11px;");
        _html.append("        }");
        _html.append("        .al {");
        _html.append("            text-align: right;");
        _html.append("        }");
        _html.append("        .titulo {");
        _html.append("            font-family: 'Trebuchet MS';");
        _html.append("            font-size: 12px;");
        _html.append("            font-weight: bold;");
        _html.append("        }");
        _html.append("        table {");
        _html.append("            border-collapse: collapse;");
        _html.append("            width: 100%;");
        _html.append("            margin-top: 2px;");
        _html.append("        }");
        _html.append("        td[a=\"1\"] {");
        _html.append("            border: 1px solid #d7d3d3;");
        _html.append("            background-color: #eeeded;");
        _html.append("            padding: 3px;");
        _html.append("        }");
        _html.append("        td[a=\"2\"] {");
        _html.append("            border: 1px solid #d7d3d3;");
        _html.append("            padding: 3px;");
        _html.append("        }");
        _html.append("    </style>");
        _html.append("</head>");
        // endregion

        // region Iniciando o corpo do html
        _html.append("<body>");
        // endregion

        // region Cabeçalho do pedido (MONTEIRO ATACADISTA)
        if (this.Empresa.Sigla.equalsIgnoreCase("MA")) {
            _html.append("    <div class=\"titulo\">" + this.Empresa.Descricao + "</div>");
            _html.append("    <div>RUA PORIGUARAS, 518</div>");
            _html.append("    <div>CENTRO - TUPÃ - SP, CEP: 17601-080</div>");
            _html.append("    <div>TELEFONE: (14) 3441-3838</div>");
        }
        // endregion

        // region Cabeçalho do pedido (BIAZON BUGIGANGA)
        if (this.Empresa.Sigla.equalsIgnoreCase("MB")) {
            _html.append("    <div class=\"titulo\">" + this.Empresa.Descricao + "</div>");
            _html.append("    <div>RUA PANAMA, 448</div>");
            _html.append("    <div>JARDIM AMERICA - TUPÃ - SP, CEP: 17605-280</div>");
            _html.append("    <div>TELEFONE: (14) 3441-3278</div>");
        }
        // endregion


        // region Dados do cliente
        _html.append("    <br />");
        _html.append("    <br />");
        _html.append("    <div class=\"titulo\">DADOS DO CLIENTE</div>");

        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Código</td>");
        _html.append("            <td a=\"1\">CNPJ|CPF</td>");
        _html.append("            <td a=\"1\">IE|RG</td>");
        _html.append("            <td a=\"1\">Razão Social</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\">" + this.Cliente.Codigo + "</td>");

        if (this.Cliente.CnpjCpf.length() == 11) {
            _html.append("            <td a=\"2\">" + MSVUtil.formatCpf(this.Cliente.CnpjCpf) + "</td>");
        } else {
            _html.append("            <td a=\"2\">" + MSVUtil.formatCnpj(this.Cliente.CnpjCpf) + "</td>");
        }

        _html.append("            <td a=\"2\">" + this.Cliente.IeRg + "</td>");
        _html.append("            <td a=\"2\">" + this.Cliente.RazaoSocial + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Nome Fantasia</td>");
        _html.append("            <td a=\"1\">Endereço</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\">" + this.Cliente.NomeFantasia + "</td>");
        _html.append("            <td a=\"2\">" + this.Cliente.getEndereco() + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Bairro</td>");
        _html.append("            <td a=\"1\">Cidade</td>");
        _html.append("            <td a=\"1\">Estado</td>");
        _html.append("            <td a=\"1\">CEP</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\">" + this.Cliente.BairroNome + "</td>");
        _html.append("            <td a=\"2\">" + this.Cliente.Cidade.Descricao + "</td>");
        _html.append("            <td a=\"2\">" + this.Cliente.Cidade.EstadoSigla + "</td>");
        _html.append("            <td a=\"2\">" + MSVUtil.formatCep(this.Cliente.Cep) + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Contato</td>");
        _html.append("            <td a=\"1\">e-mail</td>");
        _html.append("            <td a=\"1\">Telefone Celular</td>");
        _html.append("            <td a=\"1\">Telefone Fixo</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\">" + this.Cliente.ContatoNome + "</td>");
        _html.append("            <td a=\"2\">" + this.Cliente.Email + "</td>");
        _html.append("            <td a=\"2\">" + this.Cliente.TelefoneCelular + "</td>");
        _html.append("            <td a=\"2\">" + this.Cliente.TelefoneFixo + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        // endregion

        // region Dados do pedido
        _html.append("    <br />");
        _html.append("    <br />");
        _html.append("    <div class=\"titulo\">DADOS DO PEDIDO</div>");

        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Empresa</td>");
        _html.append("            <td a=\"1\">Número</td>");
        _html.append("            <td a=\"1\">Emissão</td>");
        _html.append("            <td a=\"1\">Tipo de Pedido</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\">" + this.Empresa.Sigla + "</td>");
        _html.append("            <td a=\"2\">" + this.Numero + "</td>");
        _html.append("            <td a=\"2\">" + this.EmissaoDataHora + "</td>");
        _html.append("            <td a=\"2\">" + this.TipoPedido.Descricao + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Vendedor</td>");
        _html.append("            <td a=\"1\">Condição de Pagamento</td>");
        //_html.append("            <td a=\"1\">Tabela de Preço</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\">" + this.Vendedor.Nome + "</td>");
        _html.append("            <td a=\"2\">" + this.CondicaoPagamento.Descricao + "</td>");
        //_html.append("            <td a=\"2\">" + this.TabelaPreco.Descricao + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Transportadora</td>");
        _html.append("            <td a=\"1\">Número Pedido Cliente</td>");
        _html.append("            <td a=\"1\">Confirmado ?</td>");
        _html.append("            <td a=\"1\">Sincronizado ?</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\">" + this.Transportadora.Descricao + "</td>");
        _html.append("            <td a=\"2\">" + this.NumeroCliente + "</td>");
        _html.append("            <td a=\"2\">" + MSVUtil.intToSimNao(this.EhConfirmado) + "</td>");
        _html.append("            <td a=\"2\">" + MSVUtil.intToSimNao(this.EhSincronizado) + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Observação</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\">" + this.Observacao + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        // endregion

        // region Dados dos itens do pedido
        _html.append("    <br />");
        _html.append("    <br />");
        _html.append("    <div class=\"titulo\">ITENS DO PEDIDO</div>");

        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\">Código</td>");
        _html.append("            <td a=\"1\">Descrição</td>");
        _html.append("            <td a=\"1\">U.M.</td>");
        _html.append("            <td a=\"1\" class=\"al\">Valor Unitário</td>");
        _html.append("            <td a=\"1\" class=\"al\">Desconto (R$)</td>");
        _html.append("            <td a=\"1\" class=\"al\">Valor Líquido</td>");
        _html.append("            <td a=\"1\" class=\"al\">Quantidade</td>");
        _html.append("            <td a=\"1\" class=\"al\">Valor Total</td>");
        _html.append("        </tr>");

        for(tpPedidoMobileItem _item : this.Itens) {

            _html.append("        <tr>");
            _html.append("            <td a=\"2\">" + _item.Produto.Codigo + "</td>");
            _html.append("            <td a=\"2\">" + _item.Produto.Descricao + "</td>");
            _html.append("            <td a=\"2\">" + _item.Produto.UnidadeMedida + "</td>"); // + "-" + _item.Produto.PackQuantidade + );
            _html.append("            <td a=\"2\" class=\"al\">" + MSVUtil.doubleToText(_item.UnidadeValor) + "</td>");
            _html.append("            <td a=\"2\" class=\"al\">" + MSVUtil.doubleToText(_item.UnidadeDescontoValor) + "</td>");
            _html.append("            <td a=\"2\" class=\"al\">" + MSVUtil.doubleToText(_item.UnidadeValorLiquido) + "</td>");
            _html.append("            <td a=\"2\" class=\"al\">" + MSVUtil.doubleToText(_item.UnidadeVendaQuantidade) + "</td>");
            _html.append("            <td a=\"2\" class=\"al\">" + MSVUtil.doubleToText(_item.UnidadeValorTotal) + "</td>");
            _html.append("        </tr>");

        }

        _html.append("    </table>");
        // endregion

        // region Resumo final do pedido
        _html.append("    <br />");
        _html.append("    <br />");
        _html.append("    <div class=\"titulo\">RESUMO FINAL DO PEDIDO</div>");

        _html.append("    <table cellpadding=\"0\" cellspacing=\"0\">");
        _html.append("        <tr>");
        _html.append("            <td a=\"1\" class=\"al\">Qtd. Itens</td>");
        _html.append("            <td a=\"1\" class=\"al\">Valor Total</td>");
        _html.append("            <td a=\"1\" class=\"al\">Desconto (%)</td>");
        _html.append("            <td a=\"1\" class=\"al\">Desconto (R$)</td>");
        _html.append("            <td a=\"1\" class=\"al\">Valor Líquido</td>");
        _html.append("        </tr>");
        _html.append("        <tr>");
        _html.append("            <td a=\"2\" class=\"al\">" + String.valueOf(this.ItensQuantidade) + "</td>");
        _html.append("            <td a=\"2\" class=\"al\">" + MSVUtil.doubleToText(this.TotalValor) + "</td>");
        _html.append("            <td a=\"2\" class=\"al\">" + MSVUtil.doubleToText(this.DescontoPercentual1) + "</td>");
        _html.append("            <td a=\"2\" class=\"al\">" + MSVUtil.doubleToText(this.DescontoValor1) + "</td>");
        _html.append("            <td a=\"2\" class=\"al titulo\">" + MSVUtil.doubleToText(this.TotalValorLiquido) + "</td>");
        _html.append("        </tr>");
        _html.append("    </table>");
        // endregion

        // region Finalizando o html
        _html.append("</body>");
        _html.append("</html>");
        // endregion

        // endregion

        // region Devolvendo o objeto para quem invocou o método
        return _html;
        // endregion
    }
    // endregion

}
