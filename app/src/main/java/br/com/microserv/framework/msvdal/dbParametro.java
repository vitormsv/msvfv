package br.com.microserv.framework.msvdal;

import android.util.Log;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpBairro;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.WhereItem;
import br.com.microserv.framework.msvutil.eSQLConditionType;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.framework.msvutil.eSQLiteDataType;

/**
 * Created by notemsv01 on 22/11/2016.
 */

public class dbParametro extends dbBase implements dbInterface {

    // region Delcarando constantes da classe
    public final static String VENDEDOR_CODIGO = "vendedor_codigo";
    public final static String VENDEDOR_HASH = "vendedor_hash";
    public final static String SERVIDOR_REST_IP = "servidor_rest_ip";
    public final static String UTILIZA_MIX_PRODUTO = "utiliza_mix_produto";
    public final static String DESCONTO_FINAL_EH_PERMITIDO = "desconto_final_eh_permitido";
    public final static String DESCONTO_ITEM_EH_PERMITIDO = "desconto_item_eh_permitido";
    public final static String VALOR_ITEM_EH_LIBERADO = "pedido_valor_unitario_liberado";
    public final static String QUANTIDADE_REGISTRO_LEITURA_PRODUTO = "quantidade_registro_por_leitura_produto";
    public final static String QUANTIDADE_REGISTRO_LEITURA_CLIENTE = "quantidade_registro_por_leitura_cliente";
    public final static String PERMITE_ADICIONAR_CLIENTE = "permite_adicionar_cliente";
    public final static String PERMITE_ALTERAR_CLIENTE = "permite_alterar_cliente";
    // endregion


    // region Constructor
    public dbParametro(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
    }
    // endregion


    // region newDTO
    @Override
    public tpInterface newDTO() {

        return new tpParametro();

    }
    // endregion


    // region getParametro
    // nome: Nome do parametro desejado
    private tpParametro getParametro(String nome) {

        tpInterface _out = null;

        WhereItem _wi = new WhereItem();
        _wi.field = "Nome";
        _wi.dataType = eSQLiteDataType.TEXT;
        _wi.conditionType = eSQLConditionType.EQUAL;
        _wi.value1 = nome;

        try {
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addWhere(_wi);

            _out = this.getOne(_sch);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_out != null) {
            return (tpParametro) _out;
        } else {
            return null;
        }

    }
    // endregion


    // region getVendedorCodigo
    public tpParametro getVendedorCodigo() {

        tpParametro _out = null;

        try {
            _out = getParametro(VENDEDOR_CODIGO);
        } catch (Exception e) {
            Log.e("dbParametro", "getVendedorCodigo -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;
    }
    // endregion


    // region getVendedorHash
    public tpParametro getVendedorHash() {

        tpParametro _out = null;

        try {
            _out = getParametro(VENDEDOR_HASH);
        } catch (Exception e) {
            Log.e("dbParametro", "getVendedorHash -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    // region exists
    public boolean exists(String nome) {

        tpParametro _out = null;

        try {
            _out = getParametro(nome);
        } catch (Exception e) {
            Log.e("dbParametro", "exists -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out != null;

    }
    // endregion


    //region getServidorRestIp
    public tpParametro getServidorRestIp() {

        tpParametro _out = null;

        try {
            _out = getParametro(SERVIDOR_REST_IP);
        } catch (Exception e) {
            Log.e("dbParametro", "getServidorRestIp -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    //region getUtilizaMixProduto
    public tpParametro getUtilizaMixProduto() {

        tpParametro _out = null;

        try {

            // region Lendo o objeto parametro
            _out = getParametro(UTILIZA_MIX_PRODUTO);
            // endregion

            // region Atribuindo valor padrão caso o parametro não exista ainda
            if (_out == null) {
                _out = new tpParametro();
                _out.Nome = UTILIZA_MIX_PRODUTO;
                _out.ValorInteiro = 0;
            }
            // endregion

        } catch (Exception e) {
            Log.e("dbParametro", "getUtilizaMixProduto -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    //region getDescontoFinalEhPermitido
    public tpParametro getDescontoFinalEhPermitido() {

        tpParametro _out = null;

        try {

            // region Lendo o objeto parametro
            _out = getParametro(DESCONTO_FINAL_EH_PERMITIDO);
            // endregion

            // region Atribuindo valor padrão caso o parametro não exista ainda
            if (_out == null) {
                _out = new tpParametro();
                _out.Nome = DESCONTO_FINAL_EH_PERMITIDO;
                _out.ValorInteiro = 0;
            }
            // endregion

        } catch (Exception e) {
            Log.e("dbParametro", "getDescontoFinalEhPermitido -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    //region getDescontoItemEhPermitido
    public tpParametro getDescontoItemEhPermitido() {

        tpParametro _out = null;

        try {

            // region Lendo o objeto parametro
            _out = getParametro(DESCONTO_ITEM_EH_PERMITIDO);
            // endregion

            // region Atribuindo valor padrão caso o parametro não exista ainda
            if (_out == null) {
                _out = new tpParametro();
                _out.Nome = DESCONTO_ITEM_EH_PERMITIDO;
                _out.ValorInteiro = 0;
            }
            // endregion

        } catch (Exception e) {
            Log.e("dbParametro", "getDescontoItemEhPermtido -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    //region getValorItemEhLiberado
    public tpParametro getValorItemEhLiberado() {

        tpParametro _out = null;

        try {

            // region Lendo o objeto parametro
            _out = getParametro(VALOR_ITEM_EH_LIBERADO);
            // endregion

            // region Atribuindo valor padrão caso o parametro não exista ainda
            if (_out == null) {
                _out = new tpParametro();
                _out.Nome = VALOR_ITEM_EH_LIBERADO;
                _out.ValorInteiro = 0;
            }
            // endregion

        } catch (Exception e) {
            Log.e("dbParametro", "getDescontoItemEhPermtido -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    //region getQuantidadeRegistrosProduto
    public tpParametro getQuantidadeRegistrosProduto() {

        tpParametro _out = null;

        try {

            // region Lendo o objeto parametro
            _out = getParametro(QUANTIDADE_REGISTRO_LEITURA_PRODUTO);
            // endregion

            // region Atribuindo valor padrão caso o parametro não exista ainda
            if (_out == null) {
                _out = new tpParametro();
                _out.Nome = QUANTIDADE_REGISTRO_LEITURA_PRODUTO;
                _out.ValorInteiro = 100;
            }
            // endregion

        } catch (Exception e) {
            Log.e("dbParametro", "getQuantidadeRegistrosProdutos -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    //region getQuantidadeRegistrosCliente
    public tpParametro getQuantidadeRegistrosCliente() {

        tpParametro _out = null;

        try {

            // region Lendo o objeto parametro
            _out = getParametro(QUANTIDADE_REGISTRO_LEITURA_CLIENTE);
            // endregion

            // region Atribuindo valor padrão caso o parametro não exista ainda
            if (_out == null) {
                _out = new tpParametro();
                _out.Nome = QUANTIDADE_REGISTRO_LEITURA_CLIENTE;
                _out.ValorInteiro = 100;
            }
            // endregion

        } catch (Exception e) {
            Log.e("dbParametro", "getQuantidadeRegistrosCliente -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    //region getPermiteAdicionarCliente
    public tpParametro getPermiteAdicionarCliente() {

        tpParametro _out = null;

        try {

            // region Lendo o objeto parametro
            _out = getParametro(PERMITE_ADICIONAR_CLIENTE);
            // endregion

            // region Atribuindo valor padrão caso o parametro não exista ainda
            if (_out == null) {
                _out = new tpParametro();
                _out.Nome = PERMITE_ADICIONAR_CLIENTE;
                _out.ValorInteiro = 0;
            }
            // endregion

        } catch (Exception e) {
            Log.e("dbParametro", "getPermiteAdicionarCliente -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    //region getPermiteAlterarCliente
    public tpParametro getPermiteAlterarCliente() {

        tpParametro _out = null;

        try {

            // region Lendo o objeto parametro
            _out = getParametro(PERMITE_ALTERAR_CLIENTE);
            // endregion

            // region Atribuindo valor padrão caso o parametro não exista ainda
            if (_out == null) {
                _out = new tpParametro();
                _out.Nome = PERMITE_ALTERAR_CLIENTE;
                _out.ValorInteiro = 0;
            }
            // endregion

        } catch (Exception e) {
            Log.e("dbParametro", "getPermiteAlterarCliente -> " + e.getMessage());
            e.printStackTrace();
        }

        return _out;

    }
    // endregion

}
