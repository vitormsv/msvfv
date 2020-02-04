package br.com.microserv.msvmobilepdv.pedido;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbClienteMix;
import br.com.microserv.framework.msvdal.dbCondicaoPagamento;
import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdal.dbPedidoMobile;
import br.com.microserv.framework.msvdal.dbPedidoMobileItem;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdal.dbTabelaPrecoProduto;
import br.com.microserv.framework.msvdal.dbTipoPedido;
import br.com.microserv.framework.msvdal.dbTabelaPreco;
import br.com.microserv.framework.msvdal.dbTransportadora;
import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteMix;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpKeyValueRow;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpTabelaPrecoProduto;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvdto.tpTransportadora;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvinterface.OnSelectedItem;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.CondicaoPagamentoDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.KeyValueDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.PedidoMobileProdutoAdapter;
import br.com.microserv.msvmobilepdv.adapter.TabelaPrecoDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.TipoPedidoDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.TransportadoraDialogSearchAdapter;

public class PedidoMobileEditarActivity extends AppCompatActivity implements ActivityInterface {

    // region Variavel para pegar valor pesquisa produto
    public static String _PESQUISA_VALUE = "";
    // endregion

    // region Declarando constantes

    // RequestCode
    static final int _ITEM_REQUEST_CODE = 100;

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_ITEM_INDEX = "ItemIndex";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_TP_TABELA_PRECO = "tpTabelaPreco";
    static final String _KEY_TP_PEDIDO_MOBILE = "TpPedidoMobile";
    static final String _KEY_TP_PEDIDO_MOBILE_ITEM = "TpPedidoMobileItem";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_ID_CLIENTE = "IdCliente";
    static final String _KEY_ID_PEDIDO_MOBILE = "IdPedidoMobile";
    static final String _KEY_LST_PRODUTO_INCLUIDO = "lstProdutoIncluido";
    static final String _AUTOMATICO = "AUTOMÁTICO";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final String _CLIENTE_PEDIDO_MOBILE_VALUE = "PedidoMobileEditarActivity";
    // endregion


    // region Declarando os objetos de interface

    // region INCLUDE

    // View
    View _inClienteCabecalho = null;
    View _inPrincipal = null;
    View _inItens = null;
    View _inResumo = null;
    // endregion


    // region CABECALHO

    // TextView
    TextView _txtClienteNomeFantasia = null;
    TextView _txtClienteRazaoSocial = null;
    TextView _txtClienteDocumento = null;
    TextView _txtClienteCodigo = null;

    // endregion


    // region NAVEGADOR
    LinearLayout _pnlPrincipalMnt = null;
    LinearLayout _pnlItensMnt = null;
    LinearLayout _pnlResumoMnt = null;
    // endregion


    // region PRINCIPAL

    // LinearLayout
    LinearLayout _ped_pnlNumeroEmissaoDataCnt = null;
    LinearLayout _ped_pnlEmpresaCnt = null;
    LinearLayout _ped_pnlEmpresa = null;
    LinearLayout _ped_pnlTipoPedidoCnt = null;
    LinearLayout _ped_pnlTipoPedido = null;
    LinearLayout _ped_pnlTabelaPrecoCnt = null;
    LinearLayout _ped_pnlTabelaPreco = null;
    LinearLayout _ped_pnlCondicaoPagamentoCnt = null;
    LinearLayout _ped_pnlCondicaoPagamento = null;
    LinearLayout _ped_pnlTransportadoraCnt = null;
    LinearLayout _ped_pnlTransportadora = null;
    LinearLayout _ped_pnlNumeroClienteCnt = null;
    LinearLayout _ped_pnlNumeroCliente = null;
    LinearLayout _ped_pnlObservacaoCnt = null;
    LinearLayout _ped_pnlObservacao = null;

    // TextView
    TextView _ped_txtNumero = null;
    TextView _ped_txtEmissaoDataHora = null;
    TextView _ped_txtEmpresaDescricao = null;
    TextView _ped_txtTipoPedidoDescricao = null;
    TextView _ped_txtTabelaPrecoDescricao = null;
    TextView _ped_txtCondicaoPagamentoDescricao = null;
    TextView _ped_txtTransportadoraDescricao = null;
    TextView _ped_txtNumeroCliente = null;
    TextView _ped_txtObservacao = null;

    // endregion


    // region ITENS

    // ListView
    ListView _ite_livItens = null;

    // TextView
    TextView _ite_txtNovoItem = null;
    TextView _ite_txtRegistro = null;
    TextView _ite_txtValorTotal = null;

    // endregion


    // region RESUMO

    // LinearLayout
    LinearLayout _res_pnlNumeroDataEmissaoCnt = null;
    LinearLayout _res_pnlNumero = null;
    LinearLayout _res_pnlEmissaoData = null;
    LinearLayout _res_pnlEmpresaCnt = null;
    LinearLayout _res_pnlEmpresa = null;
    LinearLayout _res_pnlTipoPedidoCnt = null;
    LinearLayout _res_pnlTipoPedido = null;
    LinearLayout _res_pnlTabelaPrecoCnt = null;
    LinearLayout _res_pnlTabelaPreco = null;
    LinearLayout _res_pnlCondicaoPagamentoCnt = null;
    LinearLayout _res_pnlCondicaoPagamento = null;
    LinearLayout _res_pnlTransportadoraCnt = null;
    LinearLayout _res_pnlTransportadora = null;
    LinearLayout _res_pnlNumeroClienteCnt = null;
    LinearLayout _res_pnlNumeroCliente = null;
    LinearLayout _res_pnlObservacaoCnt = null;
    LinearLayout _res_pnlObservacao = null;
    LinearLayout _res_pnlItensQuantidadeCnt = null;
    LinearLayout _res_pnlItensQuantidade = null;
    LinearLayout _res_pnlItensValorTotalCnt = null;
    LinearLayout _res_pnlItensValorTotal = null;
    LinearLayout _res_pnlDescontoPercentualCnt = null;
    LinearLayout _res_pnlDescontoPercentual = null;
    LinearLayout _res_pnlDescontoValorCnt = null;
    LinearLayout _res_pnlDescontoValor = null;
    LinearLayout _res_pnlItensValorTotalLiquidoCnt = null;
    LinearLayout _res_pnlItensValorTotalLiquido = null;
    LinearLayout _res_pnlPedidoConfirmadoCnt = null;
    LinearLayout _res_pnlPedidoConfirmado = null;

    // TextView
    TextView _res_txtNumero = null;
    TextView _res_txtEmissaoDataHora = null;
    TextView _res_txtEmpresaDescricao = null;
    TextView _res_txtTipoPedidoDescricao = null;
    TextView _res_txtTabelaPrecoDescricao = null;
    TextView _res_txtCondicaoPagamentoDescricao = null;
    TextView _res_txtTransportadoraDescricao = null;
    TextView _res_txtNumeroCliente = null;
    TextView _res_txtObservacao = null;
    TextView _res_txtItensQuantidade = null;
    TextView _res_txtItensValorTotal = null;
    TextView _res_txtDescontoPercentual = null;
    TextView _res_txtDescontoValor = null;
    TextView _res_txtItensValorTotalLiquido = null;
    TextView _res_txtPedidoConfirmado = null;

    // endregion

    // endregion


    // region Declarando variáveis locias

    // objetos
    Bundle _extras = null;

    // Adapters
    TipoPedidoDialogSearchAdapter _adpTipoPedido = null;
    TabelaPrecoDialogSearchAdapter _adpTabelaPreco = null;
    CondicaoPagamentoDialogSearchAdapter _adpCondicaoPagamento = null;
    TransportadoraDialogSearchAdapter _adpTransportadora = null;
    PedidoMobileProdutoAdapter _adpItens = null;
    KeyValueDialogSearchAdapter _adpSimNao = null;
    KeyValueDialogSearchAdapter _adpOption = null;

    // lst
    ArrayList<tpEmpresa> _lstEmpresa = null;
    ArrayList<tpTipoPedido> _lstTipoPedido = null;
    ArrayList<tpTabelaPreco> _lstTabelaPreco = null;
    ArrayList<tpCondicaoPagamento> _lstCondicaoPagamento = null;
    ArrayList<tpTransportadora> _lstTransportadora = null;
    ArrayList<tpKeyValueRow> _lstSimNao = null;
    ArrayList<tpKeyValueRow> _lstOption = null;

    // tp
    tpEmpresa _tpEmpresa = null;
    tpPedidoMobile _tpPedidoMobile = null;
    tpCliente _tpCliente = null;
    tpVendedor _tpVendedor = null;
    tpKeyValueRow _tpSelectedOptoin = null;

    // String
    String _sourceActivity = null;

    // int
    int _iAux = -1;
    int _iEmpresa = -1;
    int _iTipoPedido = -1;
    int _iTabelaPreco = -1;
    int _iCondicaoPagamento = -1;
    int _iTransportadora = -1;
    int _iEhConfirmado = 0;
    int _metodoEdicao = 0;


    // long
    long _IdCliente = 0;
    long _IdPedidoMobile = 0;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Invocando o método construtor ancestral
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_mobile_editar);
        // endregion

        // region Aplicando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);
        // endregion

        // region Recuperando parametros extras
        _extras = getIntent().getExtras();

        if (_extras != null) {

            if (_extras.containsKey(_KEY_METODO_EDICAO)) {
                _metodoEdicao = _extras.getInt(_KEY_METODO_EDICAO);
            } else {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "O parâmetro _KEY_METODO_EDICAO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_SOURCE_ACTIVITY)) {
                _sourceActivity = _extras.getString(_KEY_SOURCE_ACTIVITY);
            } else {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "O parâmetro _KEY_SOURCE_ACTIVITY não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_TP_EMPRESA)) {
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);
            } else {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "O parâmetro _KEY_ID_CLIENTE não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_ID_CLIENTE)) {
                _IdCliente = _extras.getLong(_KEY_ID_CLIENTE);
            } else {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "O parâmetro _KEY_ID_CLIENTE não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_ID_PEDIDO_MOBILE)) {
                _IdPedidoMobile = _extras.getLong(_KEY_ID_PEDIDO_MOBILE);
            } else {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "O parâmetro _KEY_ID_PEDIDO_MOBILE não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
        // endregion

        // region Invocando os métodos da interface
        bindElements();
        bindEvents();
        // endregion

        // region Invocando demais métodos
        setProperties();
        // endregion

        // region Incovando o método de inicializacao da activity
        initialize();
        // endregion

    }
    // endregion


    // region setProperties
    private void setProperties() {

        // region Declarando variáveis do método
        SQLiteHelper _sqh = null;
        dbParametro _db = null;
        tpParametro _tp = null;
        // endregion

        // region Bloco protegido por exceção
        try {

            // region Cuidando da conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Instânciando os objetos de acesso ao banco
            _db = new dbParametro(_sqh);
            // endregion

            // region Buscando a informação no banco de dados
            _tp = _db.getDescontoFinalEhPermitido();
            // endregion

            // region Fazendo uso do parâmetro selecionado
            if (_tp != null) {
                if (_tp.ValorInteiro == 0) {
                    _res_pnlDescontoPercentualCnt.setVisibility(View.GONE);
                }
            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "Erro ao selecionar o valor do parâmetro que indica se é permitido informar desconto final no pedido",
                    e.getMessage()
            );

            finish();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }
        // endregion

    }
    // endregion


    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pedido, menu);
        return true;

    }
    // endregion


    // region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.cancel();
                break;

            case R.id.mnPrincipal:
                _inPrincipal.setVisibility(View.VISIBLE);
                _inItens.setVisibility(View.GONE);
                _inResumo.setVisibility(View.GONE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_300);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_400);
                _pnlResumoMnt.setBackgroundResource(R.color.indigo_400);
                break;

            case R.id.mnItens:
                this.refreshItens();

                _inPrincipal.setVisibility(View.GONE);
                _inItens.setVisibility(View.VISIBLE);
                _inResumo.setVisibility(View.GONE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_400);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_300);
                _pnlResumoMnt.setBackgroundResource(R.color.indigo_400);
                break;

            case R.id.mnResumo:
                this.refreshResumo();

                _inPrincipal.setVisibility(View.GONE);
                _inItens.setVisibility(View.GONE);
                _inResumo.setVisibility(View.VISIBLE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_400);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_400);
                _pnlResumoMnt.setBackgroundResource(R.color.indigo_300);
                break;

            case R.id.mnSalvar:
                this.save();
                break;

            case R.id.mnCancelar:
                this.cancel();
                break;

        }

        return super.onOptionsItemSelected(item);

    }
    // endregion


    // region onBackPressed
    @Override
    public void onBackPressed() {

        MSVMsgBox.showMsgBoxQuestion(
                PedidoMobileEditarActivity.this,
                "Deseja realmente encerrar esta tela ?",
                "Se clicar em OK os dados incluidos ou alterados nesta tela serão perdidos",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {
                            PedidoMobileEditarActivity.super.onBackPressed();
                        }

                    }
                }
        );

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // region Topo
        _inClienteCabecalho = findViewById(R.id.inClienteCabecalho);

        _txtClienteNomeFantasia = (TextView) _inClienteCabecalho.findViewById(R.id.txtClienteNomeFantasia);
        _txtClienteRazaoSocial = (TextView) _inClienteCabecalho.findViewById(R.id.txtClienteRazaoSocial);
        _txtClienteDocumento = (TextView) _inClienteCabecalho.findViewById(R.id.txtClienteDocumento);
        _txtClienteCodigo = (TextView) _inClienteCabecalho.findViewById(R.id.txtClienteCodigo);
        // endregion


        // region INCLUDE
        _inClienteCabecalho = (View) findViewById(R.id.inClienteCabecalho);
        _inPrincipal = (View) findViewById(R.id.inPrincipal);
        _inItens = (View) findViewById(R.id.inItens);
        _inResumo = (View) findViewById(R.id.inResumo);
        // endregion


        // region NAVEGADOR
        _pnlPrincipalMnt = (LinearLayout) findViewById(R.id.pnlPrincipalMnt);
        _pnlItensMnt = (LinearLayout) findViewById(R.id.pnlItensMnt);
        _pnlResumoMnt = (LinearLayout) findViewById(R.id.pnlResumoMnt);
        // endregion

        // region PRINCIPAL

        // LinearLayout
        _ped_pnlNumeroEmissaoDataCnt = (LinearLayout) _inPrincipal.findViewById(R.id.pnlNumeroEmissaoDataCnt);
        _ped_pnlEmpresaCnt = (LinearLayout) _inPrincipal.findViewById(R.id.pnlEmpresaCnt);
        _ped_pnlEmpresa = (LinearLayout) _inPrincipal.findViewById(R.id.pnlEmpresa);
        _ped_pnlTipoPedidoCnt = (LinearLayout) _inPrincipal.findViewById(R.id.pnlTipoPedidoCnt);
        _ped_pnlTipoPedido = (LinearLayout) _inPrincipal.findViewById(R.id.pnlTipoPedido);
        _ped_pnlTabelaPrecoCnt = (LinearLayout) _inPrincipal.findViewById(R.id.pnlTabelaPrecoCnt);
        _ped_pnlTabelaPreco = (LinearLayout) _inPrincipal.findViewById(R.id.pnlTabelaPreco);
        _ped_pnlCondicaoPagamentoCnt = (LinearLayout) _inPrincipal.findViewById(R.id.pnlCondicaoPagamentoCnt);
        _ped_pnlCondicaoPagamento = (LinearLayout) _inPrincipal.findViewById(R.id.pnlCondicaoPagamento);
        _ped_pnlTransportadoraCnt = (LinearLayout) _inPrincipal.findViewById(R.id.pnlTransportadoraCnt);
        _ped_pnlTransportadora = (LinearLayout) _inPrincipal.findViewById(R.id.pnlTransportadora);
        _ped_pnlNumeroClienteCnt = (LinearLayout) _inPrincipal.findViewById(R.id.pnlNumeroClienteCnt);
        _ped_pnlNumeroCliente = (LinearLayout) _inPrincipal.findViewById(R.id.pnlNumeroCliente);
        _ped_pnlObservacaoCnt = (LinearLayout) _inPrincipal.findViewById(R.id.pnlObservacaoCnt);
        _ped_pnlObservacao = (LinearLayout) _inPrincipal.findViewById(R.id.pnlObservacao);

        // TextView
        _ped_txtNumero = (TextView) _inPrincipal.findViewById(R.id.txtNumero);
        _ped_txtEmissaoDataHora = (TextView) _inPrincipal.findViewById(R.id.txtEmissaoDataHora);
        _ped_txtEmpresaDescricao = (TextView) _inPrincipal.findViewById(R.id.txtEmpresaDescricao);
        _ped_txtTipoPedidoDescricao = (TextView) _inPrincipal.findViewById(R.id.txtTipoPedidoDescricao);
        _ped_txtTabelaPrecoDescricao = (TextView) _inPrincipal.findViewById(R.id.txtTabelaPrecoDescricao);
        _ped_txtCondicaoPagamentoDescricao = (TextView) _inPrincipal.findViewById(R.id.txtCondicaoPagamentoDescricao);
        _ped_txtTransportadoraDescricao = (TextView) _inPrincipal.findViewById(R.id.txtTransportadoraDescricao);
        _ped_txtNumeroCliente = (TextView) _inPrincipal.findViewById(R.id.txtNumeroCliente);
        _ped_txtObservacao = (TextView) _inPrincipal.findViewById(R.id.txtObservacao);

        // endregion


        // region ITENS

        // ListView
        _ite_livItens = (ListView) _inItens.findViewById(R.id.livItens);

        // TextView
        _ite_txtNovoItem = (TextView) _inItens.findViewById(R.id.txtNovoItem);
        _ite_txtRegistro = (TextView) _inItens.findViewById(R.id.txtRegistro);
        _ite_txtValorTotal = (TextView) _inItens.findViewById(R.id.txtValorTotal);

        // endregion


        // region RESUMO

        // LinearLayout
        _res_pnlNumeroDataEmissaoCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlNumeroEmissaoDataCnt);
        _res_pnlNumero = (LinearLayout) _inResumo.findViewById(R.id.pnlNumero);
        _res_pnlEmissaoData = (LinearLayout) _inResumo.findViewById(R.id.pnlEmissaoData);
        _res_pnlEmpresaCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlEmpresaCnt);
        _res_pnlEmpresa = (LinearLayout) _inResumo.findViewById(R.id.pnlEmpresa);
        _res_pnlTipoPedidoCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlTipoPedidoCnt);
        _res_pnlTipoPedido = (LinearLayout) _inResumo.findViewById(R.id.pnlTipoPedido);
        _res_pnlTabelaPrecoCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlTabelaPrecoCnt);
        _res_pnlTabelaPreco = (LinearLayout) _inResumo.findViewById(R.id.pnlTabelaPreco);
        _res_pnlCondicaoPagamentoCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlCondicaoPagamentoCnt);
        _res_pnlCondicaoPagamento = (LinearLayout) _inResumo.findViewById(R.id.pnlCondicaoPagamento);
        _res_pnlTransportadoraCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlTransportadoraCnt);
        _res_pnlTransportadora = (LinearLayout) _inResumo.findViewById(R.id.pnlTransportadora);
        _res_pnlNumeroClienteCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlNumeroClienteCnt);
        _res_pnlNumeroCliente = (LinearLayout) _inResumo.findViewById(R.id.pnlNumeroCliente);
        _res_pnlObservacaoCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlObservacaoCnt);
        _res_pnlObservacao = (LinearLayout) _inResumo.findViewById(R.id.pnlObservacao);
        _res_pnlItensQuantidadeCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlItensQuantidadeCnt);
        _res_pnlItensQuantidade = (LinearLayout) _inResumo.findViewById(R.id.pnlItensQuantidade);
        _res_pnlItensValorTotalCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlItensValorTotalCnt);
        _res_pnlItensValorTotal = (LinearLayout) _inResumo.findViewById(R.id.pnlItensValorTotal);
        _res_pnlDescontoPercentualCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlDescontoPercentualCnt);
        _res_pnlDescontoPercentual = (LinearLayout) _inResumo.findViewById(R.id.pnlDescontoPercentual);
        _res_pnlDescontoValorCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlDescontoValorCnt);
        _res_pnlDescontoValor = (LinearLayout) _inResumo.findViewById(R.id.pnlDescontoValor);
        _res_pnlItensValorTotalLiquidoCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlItensValorTotalLiquidoCnt);
        _res_pnlItensValorTotalLiquido = (LinearLayout) _inResumo.findViewById(R.id.pnlItensValorTotalLiquido);
        _res_pnlPedidoConfirmadoCnt = (LinearLayout) _inResumo.findViewById(R.id.pnlPedidoConfirmadoCnt);
        _res_pnlPedidoConfirmado = (LinearLayout) _inResumo.findViewById(R.id.pnlPedidoConfirmado);

        // TextView
        _res_txtNumero = (TextView) _inResumo.findViewById(R.id.txtNumero);
        _res_txtEmissaoDataHora = (TextView) _inResumo.findViewById(R.id.txtEmissaoDataHora);
        _res_txtEmpresaDescricao = (TextView) _inResumo.findViewById(R.id.txtEmpresaDescricao);
        _res_txtTipoPedidoDescricao = (TextView) _inResumo.findViewById(R.id.txtTipoPedidoDescricao);
        _res_txtTabelaPrecoDescricao = (TextView) _inResumo.findViewById(R.id.txtTabelaPrecoDescricao);
        _res_txtCondicaoPagamentoDescricao = (TextView) _inResumo.findViewById(R.id.txtCondicaoPagamentoDescricao);
        _res_txtTransportadoraDescricao = (TextView) _inResumo.findViewById(R.id.txtTransportadoraDescricao);
        _res_txtNumeroCliente = (TextView) _inResumo.findViewById(R.id.txtNumeroCliente);
        _res_txtObservacao = (TextView) _inResumo.findViewById(R.id.txtObservacao);
        _res_txtItensQuantidade = (TextView) _inResumo.findViewById(R.id.txtItensQuantidade);
        _res_txtItensValorTotal = (TextView) _inResumo.findViewById(R.id.txtItensValorTotal);
        _res_txtDescontoPercentual = (TextView) _inResumo.findViewById(R.id.txtDescontoPercentual);
        _res_txtDescontoValor = (TextView) _inResumo.findViewById(R.id.txtDescontoValor);
        _res_txtItensValorTotalLiquido = (TextView) _inResumo.findViewById(R.id.txtItensValorTotalLiquido);
        _res_txtPedidoConfirmado = (TextView) _inResumo.findViewById(R.id.txtPedidoConfirmado);

        // endregion

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region NAVEGADOR

        // region Click em PRINCIPAL
        _pnlPrincipalMnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _inPrincipal.setVisibility(View.VISIBLE);
                _inItens.setVisibility(View.GONE);
                _inResumo.setVisibility(View.GONE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_300);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_500);
                _pnlResumoMnt.setBackgroundResource(R.color.indigo_500);

            }
        });
        // endregion

        // region Click em ITENS
        _pnlItensMnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refreshItens();

                _inPrincipal.setVisibility(View.GONE);
                _inItens.setVisibility(View.VISIBLE);
                _inResumo.setVisibility(View.GONE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_500);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_300);
                _pnlResumoMnt.setBackgroundResource(R.color.indigo_500);

            }
        });
        // endregion

        // region Click em RESUMO
        _pnlResumoMnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refreshResumo();

                _inPrincipal.setVisibility(View.GONE);
                _inItens.setVisibility(View.GONE);
                _inResumo.setVisibility(View.VISIBLE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_500);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_500);
                _pnlResumoMnt.setBackgroundResource(R.color.indigo_300);

            }
        });
        // endregion

        // endregion


        // region Guia PRINCIPAL

        // region Click em TIPO de PEDIDO
        _ped_pnlTipoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Montando o adaptador se necessário
                if (_adpTipoPedido == null) {
                    _adpTipoPedido = new TipoPedidoDialogSearchAdapter(
                            PedidoMobileEditarActivity.this,
                            _lstTipoPedido
                    );
                }
                // endregion

                // region Atualizando variável auxiliar
                _iAux = _iTipoPedido;
                // endregion

                // region Abrindo a janela para escolha do tipo de pedido
                MSVMsgBox.getValueFromList(
                        PedidoMobileEditarActivity.this,
                        _lstTipoPedido.get(_iTipoPedido).Descricao,
                        _adpTipoPedido,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iTipoPedido = _iAux;
                                _tpPedidoMobile.IdTipoPedido = _lstTipoPedido.get(_iTipoPedido).IdTipoPedido;
                                refreshPrincipal();
                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click em TABELA de PRECO
        _ped_pnlTabelaPreco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações iniciais

                // region Verificando se existem tabelas de preço no sistema
                if (_lstTabelaPreco.size() == 0) {
                    Toast.makeText(PedidoMobileEditarActivity.this, "Não existem registros para pesquisa", Toast.LENGTH_SHORT).show();
                    return;
                }
                // endregion

                // region Verificando se já existe item cadastrado
                /*

                # Este trecho de código foi bloqueado a pedido da empresa MINARE bebidas
                # onde vamos permitir que o vendedor altere a tabela de preço e então o
                # App irá reprocessar o preço de cada produto

                if ((_tpPedidoMobile.Itens != null) && (_tpPedidoMobile.Itens.size() > 0)) {

                    Toast.makeText(
                            PedidoMobileEditarActivity.this,
                            "Para alterar a tabela de preço do pedido é necessário remover todos os itens",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;

                }
                */
                // endregion

                // endregion

                // region Montando o adaptardor se necessário
                if (_adpTabelaPreco == null) {
                    _adpTabelaPreco = new TabelaPrecoDialogSearchAdapter(
                            PedidoMobileEditarActivity.this,
                            _lstTabelaPreco
                    );
                }
                // endregion

                // region Atualizando a variável auxiliar
                _iAux = _iTabelaPreco;
                // endregion

                // region Abrindo a janela para escolha da tabela de preço
                MSVMsgBox.getValueFromList(
                        PedidoMobileEditarActivity.this,
                        _lstTabelaPreco.get(_iTabelaPreco).Descricao,
                        _adpTabelaPreco,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iTabelaPreco = _iAux;
                                _tpPedidoMobile.IdTabelaPreco = _lstTabelaPreco.get(_iTabelaPreco).IdTabelaPreco;
                                recalculatePrice();
                                refreshPrincipal();
                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click em CONDICAO de PAGAMENTO
        _ped_pnlCondicaoPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações iniciais
                if (_lstCondicaoPagamento.size() == 0) {
                    Toast.makeText(PedidoMobileEditarActivity.this, "Não existem registros para pesquisa", Toast.LENGTH_SHORT).show();
                    return;
                }
                // endregion

                // region Montando o adaptador se necessário
                if (_adpCondicaoPagamento == null) {
                    _adpCondicaoPagamento = new CondicaoPagamentoDialogSearchAdapter(
                            PedidoMobileEditarActivity.this,
                            _lstCondicaoPagamento
                    );
                }
                // endregion

                // region Atualizando variável auxiliar
                _iAux = _iCondicaoPagamento;
                // endregion

                // region Abrindo a janela de escolha da CONDICAO de PAGAMENTO
                MSVMsgBox.getValueFromList(
                        PedidoMobileEditarActivity.this,
                        _lstCondicaoPagamento.get(_iCondicaoPagamento).Descricao,
                        _adpCondicaoPagamento,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iCondicaoPagamento = _iAux;
                                _tpPedidoMobile.IdCondicaoPagamento = _lstCondicaoPagamento.get(_iCondicaoPagamento).IdCondicaoPagamento;
                                refreshPrincipal();
                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click em TRANSPORTADORA
        _ped_pnlTransportadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações iniciais
                if (_lstTransportadora.size() == 0) {
                    Toast.makeText(PedidoMobileEditarActivity.this, "Não existem registros para pesquisa", Toast.LENGTH_SHORT).show();
                    return;
                }
                // endregion

                // region Montnado o adaptardor se necessário
                if (_adpTransportadora == null) {
                    _adpTransportadora = new TransportadoraDialogSearchAdapter(
                            PedidoMobileEditarActivity.this,
                            _lstTransportadora
                    );
                }
                // endregion

                // region Atualizando variável auxiliar
                _iAux = _iTransportadora;
                // endregion

                // region Abrindo a janela para escolha da transportadora
                MSVMsgBox.getValueFromList(
                        PedidoMobileEditarActivity.this,
                        _lstTransportadora.get(_iTransportadora).Descricao,
                        _adpTransportadora,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iTransportadora = _iAux;
                                _tpPedidoMobile.IdTransportadora = _lstTransportadora.get(_iTransportadora).IdTransportadora;
                                refreshPrincipal();
                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click em NUMERO PEDIDO do CLIENTE
        _ped_pnlNumeroCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Abrindo a janela para informar o número do pedido do cliente
                MSVMsgBox.getStringValue(
                        PedidoMobileEditarActivity.this,
                        "NUMERO PEDIDO CLIENTE",
                        "Informar o número do pedido do cliente",
                        _tpPedidoMobile.NumeroCliente,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    _tpPedidoMobile.NumeroCliente = value.trim().toUpperCase();
                                    refreshPrincipal();
                                }
                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region click em OBSERVACAO
        _ped_pnlObservacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Abrindo a janela para informar a observação referente ao pedido
                MSVMsgBox.getStringValue(
                        PedidoMobileEditarActivity.this,
                        "OBSERVACAO",
                        "Informar aqui observações referente ao pedido em geral",
                        _tpPedidoMobile.Observacao,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    _tpPedidoMobile.Observacao = value.trim().toUpperCase();
                                    refreshPrincipal();
                                }
                            }
                        }
                );
                // endregion

            }
        });
        // endregion

        // endregion


        // region Guia ITEM **************************************************

        // region Click em NOVO ITEM
        _ite_txtNovoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Vibrando o aparelho para indicar ao usuário o toque
                MSVUtil.vibrate(PedidoMobileEditarActivity.this);
                // endregion

                // region Instânciando um objeto do tipo tpPedidoProduto
                tpPedidoMobileItem _tpPedidoMobileItem = new tpPedidoMobileItem();
                // endregion

                // region Montando uma lista com os códigos de produtos já inclusos no pedido
                ArrayList<String> _lstProdutoIncluido = new ArrayList<String>();

                for (tpPedidoMobileItem _item : _tpPedidoMobile.Itens) {
                    _lstProdutoIncluido.add(_item.Produto.Codigo);
                }
                // endregion

                // region Instânciando um objeto de parametro e preenchendo o mesmo
                Bundle _extras = new Bundle();
                _extras.putInt(_KEY_METODO_EDICAO, _INSERT_VALUE);
                _extras.putInt(_KEY_ITEM_INDEX, -1);
                _extras.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_PEDIDO_MOBILE_VALUE);
                _extras.putLong(_KEY_ID_CLIENTE, _tpCliente.IdCliente);
                _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
                _extras.putSerializable(_KEY_TP_TABELA_PRECO, _lstTabelaPreco.get(_iTabelaPreco));
                _extras.putSerializable(_KEY_TP_PEDIDO_MOBILE_ITEM, _tpPedidoMobileItem);

                // enviamos esta lista para verificar na tela de inclusão do item
                // se o produto já está vinculado no pedido
                if (_lstProdutoIncluido != null) {
                    _extras.putSerializable(_KEY_LST_PRODUTO_INCLUIDO, _lstProdutoIncluido);
                }

                // endregion

                // region Invocando a nova activity
                Intent _i = new Intent(PedidoMobileEditarActivity.this, PedidoMobileItemEditarActivity.class);
                _i.putExtras(_extras);
                startActivityForResult(_i, _ITEM_REQUEST_CODE);
                // endregion

            }
        });
        // endregion


        // region Click Longo em NOVO ITEM
        _ite_txtNovoItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                MSVMsgBox.showMsgBoxQuestion(
                        PedidoMobileEditarActivity.this,
                        "Deseja realmente importar os itens confirmados do mix de produto do cliente durante a contagem de estoque",
                        "Atenção, ao clicar em OK os itens que já existem no pedido serão excluidos para dar lugar a composição do MIX do cliente",
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    importarMix();
                                }
                            }
                        }
                );

                return true;
            }
        });
        // endregion


        // region Click longo no ITEM do PEDIDO (excluir)
        _ite_livItens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // region Selecionando o objeto do item clicado pelo usuário
                final int _itemIndex = position;
                final tpPedidoMobileItem _tpItem = _tpPedidoMobile.Itens.get(position);
                // endregion

                // region Verificando a necessidade de criar o adapter de opções
                if (_adpOption == null) {
                    _adpOption = new KeyValueDialogSearchAdapter(
                            PedidoMobileEditarActivity.this,
                            _lstOption
                    );
                }
                // endregion

                // region Apresentando as opções ao usuário
                _iAux = -1;

                MSVMsgBox.getValueFromList(
                        PedidoMobileEditarActivity.this,
                        "OPÇÕES",
                        _adpOption,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                if (tp != null) {
                                    _iAux = position;
                                    _tpSelectedOptoin = (tpKeyValueRow) tp;
                                }
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {

                                    if (_iAux == -1) {

                                        Toast.makeText(
                                                PedidoMobileEditarActivity.this,
                                                "Nenhuma opção foi selecionada pelo usuário",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        return;
                                    }

                                    if (_tpSelectedOptoin.Key == 0) {
                                        edit(_itemIndex);
                                    } else if (_tpSelectedOptoin.Key == 1) {
                                        delete(_itemIndex);
                                    }

                                }

                            }
                        }

                );
                // endregion

                return true;
            }
        });
        // endregion

        // endregion


        // region Guia RESUMO

        // region Click em DESCONTO PERCENTUAL FINAL
        _res_pnlDescontoPercentual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações antes de abrir a janela de dialogo

                // Se não foi escolhido o produto então não vamos
                // deixar abrir a janela de digitação
                if (_tpPedidoMobile.Itens.size() == 0) {
                    Toast.makeText(
                            PedidoMobileEditarActivity.this,
                            "Não existem itens para conceder desconto",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // endregion

                // region Abrindo a janela para digitação do desconto final em %
                MSVMsgBox.getDoubleValue(
                        PedidoMobileEditarActivity.this,
                        "DESCONTO (%) FINAL",
                        "Informe o percentual final de desconto concedido para este pedido",
                        _tpPedidoMobile.DescontoPercentual1,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    _tpPedidoMobile.DescontoPercentual1 = MSVUtil.parseDouble(value.trim());

                                    afterDescontoPercentual();
                                    refreshResumo();
                                }
                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click em DESCONTO VALOR FINAL
        _res_pnlDescontoValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações antes de abrir a janela de dialogo

                // Se não foi escolhido o produto então não vamos
                // deixar abrir a janela de digitação
                if (_tpPedidoMobile.Itens.size() == 0) {
                    Toast.makeText(
                            PedidoMobileEditarActivity.this,
                            "Não existem itens para conceder desconto",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // endregion

                // region Abrindo a janela para a digitação do desconto final em R$
                MSVMsgBox.getDoubleValue(
                        PedidoMobileEditarActivity.this,
                        "DESCONTO (R$) FINAL",
                        "Informe o valor final de desconto concedido para este pedido",
                        _tpPedidoMobile.DescontoValor1,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    _tpPedidoMobile.DescontoValor1 = MSVUtil.parseDouble(value.trim());

                                    afterDescontoValor();
                                    refreshResumo();
                                }
                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click em CONFIRMACAO do PEDIDO
        _res_pnlPedidoConfirmado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Montando o adaptador se necessário
                if (_adpSimNao == null) {
                    _adpSimNao = new KeyValueDialogSearchAdapter(
                            PedidoMobileEditarActivity.this,
                            _lstSimNao
                    );
                }
                // endregion

                // region Atualizando variável auxiliar
                _iAux = _iEhConfirmado;
                // endregion

                // region Abrindo a janela para escolha da opção SIM ou NAO
                MSVMsgBox.getValueFromList(
                        PedidoMobileEditarActivity.this,
                        _lstSimNao.get(_iEhConfirmado).Value,
                        _adpSimNao,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iEhConfirmado = _iAux;
                                _tpPedidoMobile.EhConfirmado = _iEhConfirmado;

                                refreshResumo();
                            }
                        }
                );
                // endregion

            }
        });
        //

        // endregion


        // endregion

    }
    // endregion


    // region onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // region Invocando o metodo do objeto ancestral
        super.onActivityResult(requestCode, resultCode, data);
        // endregion


        // region Declarando variaveis locais

        // Object type
        tpPedidoMobileItem _aux_tpPedidoMobileItem = null;

        // int
        int _aux_MetodoEdicao = 0;
        int _aux_ItemIndex = 0;

        // endregion


        // region De acordo com o código de retorno fazer...
        switch (requestCode) {

            case _ITEM_REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {

                    // region Capturando os parametros retornados pela activity

                    // region _KEY_TP_PEDIDO_MOBILE_ITEM
                    if (!data.hasExtra(_KEY_TP_PEDIDO_MOBILE_ITEM)) {
                        Toast.makeText(
                                PedidoMobileEditarActivity.this,
                                "O parâmetro KEY_TP_PEDIDO_MOBILE_ITEM não foi informado",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        _aux_tpPedidoMobileItem = (tpPedidoMobileItem) data.getSerializableExtra(_KEY_TP_PEDIDO_MOBILE_ITEM);
                    }
                    // endregion

                    // region _KEY_METODO_EDICAO
                    if (!data.hasExtra(_KEY_METODO_EDICAO)) {
                        Toast.makeText(
                                PedidoMobileEditarActivity.this,
                                "O parâmetro KEY_METODO_EDICAO não foi informado",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        _aux_MetodoEdicao = data.getIntExtra(_KEY_METODO_EDICAO, -1);
                    }
                    // endregion

                    // region _KEY_ITEM_INDEX
                    if (!data.hasExtra(_KEY_ITEM_INDEX)) {
                        Toast.makeText(
                                PedidoMobileEditarActivity.this,
                                "O parâmetro KEY_ITEM_INDEX não foi informado",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        _aux_ItemIndex = data.getIntExtra(_KEY_ITEM_INDEX, -1);
                    }
                    // endregion

                    // endregion

                    // region Se a operação de retorno foi INSERT então...
                    if (_aux_MetodoEdicao == _INSERT_VALUE) {

                        // verificando a necessidade de instânciar uma lista de itens de pedido
                        if (_tpPedidoMobile.Itens == null) {
                            _tpPedidoMobile.Itens = new ArrayList<tpPedidoMobileItem>();
                        }

                        // adicionando o item a lista
                        _tpPedidoMobile.Itens.add(_aux_tpPedidoMobileItem);

                    }
                    // endregion

                    // region Se a operação de retorno foi UPDATE então...
                    if (_aux_MetodoEdicao == _UPDATE_VALUE) {

                        _tpPedidoMobile.Itens.set(_aux_ItemIndex, _aux_tpPedidoMobileItem);

                    }
                    // endregion

                    // region Atualizando a lista de itens do pedido
                    refreshItens();
                    // endregion

                }

                break;

        }
        // endregion

    }
    //endregion


    // region Initialize
    public void initialize() {

        try {

            // region Trabalhando a apresentação dos includes
            _inPrincipal.setVisibility(View.VISIBLE);
            _inItens.setVisibility(View.GONE);
            _inResumo.setVisibility(View.GONE);
            // endregion

            // region Limpando o conteúdos dos elementos da activity
            this.clearActivityData();
            // endregion

            // region Lendo todas as listas necessárias
            this.buildSimNao();
            this.buildOption();
            this.loadVendedor();
            this.loadTipoPedido();
            this.loadCondicaoPagamento();
            this.loadTransportadora();
            this.loadCliente();
            this.loadTabelaPreco();
            // endregion

            // region Lendo os dados do pedido
            if (_metodoEdicao == _INSERT_VALUE) {
                _tpPedidoMobile = new tpPedidoMobile();
                _tpPedidoMobile.Numero = _AUTOMATICO;
                _tpPedidoMobile.EmissaoDataHora = MSVUtil.sqliteHojeHora();
                _tpPedidoMobile.Itens = new ArrayList<tpPedidoMobileItem>();
            } else {
                this.loadPedidoMobile();
            }
            // endregion

            // region Atualizando a posição das listas
            this.refreshListPosition();
            // endregion

            // region Atualizando os dados do activity
            this.refreshCabecalho();
            this.refreshPrincipal();
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "[PedidoMobileEditarActivity|initialize()] - Erro ao inicializar um novo pedido",
                    e.getMessage()
            );

            finish();

        }

    }
    // endregion


    // region clearActivityData
    private void clearActivityData() {

        // region Include PRINCIPAL
        _ped_txtNumero.setText("");
        _ped_txtEmissaoDataHora.setText("");
        _ped_txtEmpresaDescricao.setText("");
        _ped_txtTipoPedidoDescricao.setText("");
        _ped_txtTabelaPrecoDescricao.setText("");
        _ped_txtCondicaoPagamentoDescricao.setText("");
        _ped_txtTransportadoraDescricao.setText("");
        _ped_txtNumeroCliente.setText("");
        _ped_txtObservacao.setText("");
        // endregion

        // region Include ITENS
        _ite_txtRegistro.setText("REGISTROS: 0");
        _ite_txtValorTotal.setText("R$ 0,00");
        // endregion

    }
    // endregion


    // region refreshCabecalho
    private void refreshCabecalho() {

        // region Cabecalho
        _txtClienteNomeFantasia.setText(_tpCliente.NomeFantasia);
        _txtClienteRazaoSocial.setText(_tpCliente.RazaoSocial);

        switch (_tpCliente.CnpjCpf.length()) {
            case 11:
                _txtClienteDocumento.setText(
                        MSVUtil.formatCpf(_tpCliente.CnpjCpf)
                );
                break;

            case 14:
                _txtClienteDocumento.setText(
                        MSVUtil.formatCnpj(_tpCliente.CnpjCpf)
                );
                break;

            default:
                _txtClienteDocumento.setText("NÃO INFORMADO");
        }

        _txtClienteCodigo.setText(_tpCliente.Codigo);
        // endregion

    }
    // endregion


    // region refreshPrincipal
    private void refreshPrincipal() {

        try {

            // region Numero
            _ped_txtNumero.setText(_tpPedidoMobile.Numero);
            // endregion

            // region Data de Emissão
            _ped_txtEmissaoDataHora.setText(MSVUtil.ymdhmsTOdmy(_tpPedidoMobile.EmissaoDataHora));
            // endregion

            // region Empresa
            _ped_txtEmpresaDescricao.setText(_tpEmpresa.Descricao);
            // endregion

            // region Tipo de Pedido
            _ped_txtTipoPedidoDescricao.setText("...");

            // Se existir tipo de pedido selecionado então...
            if (_iTipoPedido > -1) {
                _ped_txtTipoPedidoDescricao.setText(_lstTipoPedido.get(_iTipoPedido).Descricao);
            }
            // endregion

            // region Tabela de Preço
            _ped_txtTabelaPrecoDescricao.setText("...");

            if (_iTabelaPreco > -1) {
                _ped_txtTabelaPrecoDescricao.setText(_lstTabelaPreco.get(_iTabelaPreco).Descricao);
            }
            // endregion

            // region Condição de Pagamento
            _ped_txtCondicaoPagamentoDescricao.setText("...");

            if (_iCondicaoPagamento > -1) {
                _ped_txtCondicaoPagamentoDescricao.setText(_lstCondicaoPagamento.get(_iCondicaoPagamento).Descricao);
            }
            // endregion

            // region Transportadora
            _ped_txtTransportadoraDescricao.setText("...");

            if (_iTransportadora > -1) {
                _ped_txtTransportadoraDescricao.setText(_lstTransportadora.get(_iTransportadora).Descricao);
            }
            // endregion

            // region Numero Pedido Cliente
            _ped_txtNumeroCliente.setText(_tpPedidoMobile.NumeroCliente);
            // endregion

            // region Observação
            _ped_txtObservacao.setText(_tpPedidoMobile.Observacao);
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "[PedidoMobileEditarActivity|refreshPrincipal()] - Erro ao atualiza os valores da guia principal",
                    e.getMessage()
            );

            finish();

        }

    }
    // endregion


    // region recalculatePrice
    private void recalculatePrice() {

        SQLiteHelper _sqh = null;
        dbTabelaPrecoProduto _dbTabelaPrecoProduto = null;
        tpTabelaPrecoProduto _tpTabelaPrecoProduto = null;

        try {

            if ((_tpPedidoMobile.Itens != null) && (_tpPedidoMobile.Itens.size() > 0)) {

                _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
                _sqh.open(false);


                _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqh);


                for (tpPedidoMobileItem _tp : _tpPedidoMobile.Itens) {

                    // region Criando o WHERE para o campo IdTabelaPreco e IdProduto
                    SQLClauseHelper _sch = new SQLClauseHelper();
                    _sch.addEqualInteger("IdTabelaPreco", _tpPedidoMobile.IdTabelaPreco);
                    _sch.addEqualInteger("IdProduto", _tp.IdProduto);
                    // endregion


                    // region Buscando a informação no banco de dados
                    _tpTabelaPrecoProduto = (tpTabelaPrecoProduto) _dbTabelaPrecoProduto.getOne(_sch);
                    // endregion


                    // region Se o preço foi encontrado então vamos reprocessar os valores
                    if (_tpTabelaPrecoProduto != null) {
                        _tp.UnidadeValor = _tpTabelaPrecoProduto.Preco;
                        _tp.UnidadeValorLiquido = _tpTabelaPrecoProduto.Preco;
                        _tp.UnidadeValorTotal = _tp.UnidadeValorLiquido * _tp.UnidadeVendaQuantidade;
                    }
                    // endregion

                }

                // avisando o usuário que os preços dos produtos já inclusos no pedido
                // foram recalculados de acordo com a tabela de preço escolhida
                Toast.makeText(PedidoMobileEditarActivity.this, "Os preços dos produtos foram recalculados, verifique", Toast.LENGTH_SHORT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region refreshItens
    private void refreshItens() {

        // region Formando o adaptador do itens
        if (_adpItens == null) {
            _adpItens = new PedidoMobileProdutoAdapter(
                    PedidoMobileEditarActivity.this,
                    (ArrayList<tpPedidoMobileItem>) _tpPedidoMobile.Itens
            );
        } else {
            _adpItens.notifyDataSetChanged();
        }
        // endregion


        // region Ligando o adaptardor na lista de itens
        _ite_livItens.setAdapter(_adpItens);
        // endregion


        // region Atualizando campos calculados do pedido de acordo com os itens
        _tpPedidoMobile.ItensQuantidade = 0;
        _tpPedidoMobile.TotalValor = 0;
        _tpPedidoMobile.DescontoPercentual1 = 0;
        _tpPedidoMobile.DescontoValor1 = 0;
        _tpPedidoMobile.TotalValorLiquido = 0;

        for (tpPedidoMobileItem _tp : _tpPedidoMobile.Itens) {

            _tpPedidoMobile.ItensQuantidade += 1;
            _tpPedidoMobile.TotalValor += _tp.UnidadeValorTotal;
            _tpPedidoMobile.TotalValorLiquido += _tp.UnidadeValorTotal;

        }

        _ite_txtRegistro.setText("REGISTROS: " + String.valueOf(_tpPedidoMobile.ItensQuantidade));
        _ite_txtValorTotal.setText(MSVUtil.doubleToText("R$", _tpPedidoMobile.TotalValor));
        // endregion

    }
    //endregion


    // region refreshResumo
    private void refreshResumo() {

        try {

            // region Numero
            _res_txtNumero.setText(_tpPedidoMobile.Numero);
            // endregion

            // region Data de Emissão
            _res_txtEmissaoDataHora.setText(MSVUtil.ymdhmsTOdmy(_tpPedidoMobile.EmissaoDataHora));
            // endregion

            // region Empresa
            _res_txtEmpresaDescricao.setText(_tpEmpresa.Descricao);
            // endregion

            // region Tipo de Pedido
            _res_txtTipoPedidoDescricao.setText("...");

            // Se existir tipo de pedido selecionado então...
            if (_iTipoPedido > -1) {
                _res_txtTipoPedidoDescricao.setText(_lstTipoPedido.get(_iTipoPedido).Descricao);
            }
            // endregion

            // region Tabela de Preço
            _res_txtTabelaPrecoDescricao.setText("...");

            if (_iTabelaPreco > -1) {
                _res_txtTabelaPrecoDescricao.setText(_lstTabelaPreco.get(_iTabelaPreco).Descricao);
            }
            // endregion

            // region Condição de Pagamento
            _res_txtCondicaoPagamentoDescricao.setText("...");

            if (_iCondicaoPagamento > -1) {
                _res_txtCondicaoPagamentoDescricao.setText(_lstCondicaoPagamento.get(_iCondicaoPagamento).Descricao);
            }
            // endregion

            // region Transportadora
            _res_txtTransportadoraDescricao.setText("...");

            if (_iTransportadora > -1) {
                _res_txtTransportadoraDescricao.setText(_lstTransportadora.get(_iTransportadora).Descricao);
            }
            // endregion

            // region Numero Pedido Cliente
            _res_txtNumeroCliente.setText(_tpPedidoMobile.NumeroCliente);
            // endregion

            // region Observação
            _res_txtObservacao.setText(_tpPedidoMobile.Observacao);
            // endregion

            // region Quantidade de Itens
            _res_txtItensQuantidade.setText("0");

            if ((_tpPedidoMobile.Itens != null) && (_tpPedidoMobile.Itens.size() > 0)) {
                _res_txtItensQuantidade.setText(String.valueOf(_tpPedidoMobile.Itens.size()));
            }
            // endregion

            // region Valor total do pedido
            _res_txtItensValorTotal.setText(MSVUtil.doubleToText("R$", 0));

            if ((_tpPedidoMobile.Itens != null) && (_tpPedidoMobile.Itens.size() > 0)) {

                double _itensValorTotal = 0;

                for (tpPedidoMobileItem _tp : _tpPedidoMobile.Itens) {
                    _itensValorTotal += _tp.UnidadeValorTotal;
                }

                _res_txtItensValorTotal.setText(MSVUtil.doubleToText("R$", _itensValorTotal));
            }
            // endregion

            // region Desconto final percentual
            _res_txtDescontoPercentual.setText(MSVUtil.doubleToText(0) + " (%)");

            if (_tpPedidoMobile.DescontoPercentual1 != 0) {
                _res_txtDescontoPercentual.setText(
                        MSVUtil.doubleToText(_tpPedidoMobile.DescontoPercentual1) + " (%)"
                );
            }
            // endregion

            // region Desconto final em valor
            _res_txtDescontoValor.setText(MSVUtil.doubleToText(0) + " ($)");

            if (_tpPedidoMobile.DescontoValor1 != 0) {
                _res_txtDescontoValor.setText(
                        MSVUtil.doubleToText(_tpPedidoMobile.DescontoValor1) + " ($)"
                );
            }
            // endregion

            // region Valor total liquido do pedido
            _res_txtItensValorTotalLiquido.setText(MSVUtil.doubleToText("R$", 0));

            if ((_tpPedidoMobile.Itens != null) && (_tpPedidoMobile.Itens.size() > 0)) {
                _res_txtItensValorTotalLiquido.setText(
                        MSVUtil.doubleToText("R$", _tpPedidoMobile.TotalValorLiquido)
                );
            }
            // endregion

            // region Pedido confirmado
            _res_txtPedidoConfirmado.setText("...");

            if (_iEhConfirmado != -1) {
                _res_txtPedidoConfirmado.setText(_lstSimNao.get(_iEhConfirmado).Value);
            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "[PedidoMobileEditarActivity|refreshResumo()] - Erro ao atualiza os valores da guia resumo",
                    e.getMessage()
            );

            finish();

        }

    }
    // endregion


    // region buildSimNao
    private void buildSimNao() {

        tpKeyValueRow _tpNao = new tpKeyValueRow();
        _tpNao.Key = 0;
        _tpNao.Value = "NÃO";

        tpKeyValueRow _tpSim = new tpKeyValueRow();
        _tpSim.Key = 1;
        _tpSim.Value = "SIM";

        _lstSimNao = new ArrayList<tpKeyValueRow>();
        _lstSimNao.add(_tpNao);
        _lstSimNao.add(_tpSim);

    }
    // endregion


    // region buildOption
    private void buildOption() {

        tpKeyValueRow _tpEdit = new tpKeyValueRow();
        _tpEdit.Key = 0;
        _tpEdit.Value = "EDITAR ITEM";
        _tpEdit.ImageResource = R.drawable.msv_small_edit_black;

        tpKeyValueRow _tpDelete = new tpKeyValueRow();
        _tpDelete.Key = 1;
        _tpDelete.Value = "EXCLUIR ITEM";
        _tpDelete.ImageResource = R.drawable.msv_small_trash_black;

        _lstOption = new ArrayList<tpKeyValueRow>();
        _lstOption.add(_tpEdit);
        _lstOption.add(_tpDelete);

    }
    // endregion


    // region loadVendedor
    private void loadVendedor() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);

            // buscando os dados do vendedor
            dbVendedor _dbVendedor = new dbVendedor(_sqh);
            _tpVendedor = (tpVendedor) _dbVendedor.getById(1);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region loadTipoPedido
    private void loadTipoPedido() {

        // region Declarando variáveis para objetos locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Criando o WHERE para o campo IdEmpresa
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdEmpresa", _tpEmpresa.IdEmpresa);
            //_sch.addOrderBy("IdTipoPedido", eSQLSortType.ASC);
            // endregion

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Lendo os tipos de pedido de acordo com a empresa logada
            dbTipoPedido _dbTipoPedido = new dbTipoPedido(_sqh);
            _lstTipoPedido = (ArrayList<tpTipoPedido>) _dbTipoPedido.getList(tpTipoPedido.class, _sch);
            // endregion

            // region Emitindo mensagem ao usuário se não existir registro
            if (_lstTipoPedido == null) {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "A lista de tipos de pedido não foi carregada corretamente",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }
        // endregion

    }
    // endregion


    // region loadTabelaPreco
    private void loadTabelaPreco() {

        // region Declarando variáveis para objetos locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Criando o WHERE para o campo IdEmpresa
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdEmpresa", _tpEmpresa.IdEmpresa);
            //_sch.addOrderBy("IdTabelaPreco", eSQLSortType.ASC);
            // endregion

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Lendo as tabelas de preço de acordo com a empresa logada
            dbTabelaPreco _dbTabelaPreco = new dbTabelaPreco(_sqh);
            _lstTabelaPreco = (ArrayList<tpTabelaPreco>) _dbTabelaPreco.getList(tpTabelaPreco.class, _sch);
            // endregion

            _iTabelaPreco = 0;

            // Definindo a Tabela de Preço padrão
            if (Long.valueOf(_tpCliente.IdTabelaPreco) != 0) {

                for (tpTabelaPreco _tpTabelaPreco : _lstTabelaPreco) {

                    if (_tpTabelaPreco.IdTabelaPreco == _tpCliente.IdTabelaPreco) {

                        _tpTabelaPreco.Descricao += " ( Padrão )";

                        _iTabelaPreco = _lstTabelaPreco.indexOf(_tpTabelaPreco);

                    }

                }

            }
            //end region

            // region Emitindo mensagem ao usuário se não existir registro
            if (_lstTabelaPreco == null) {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "A lista de tabela de preço não foi carregada corretamente",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }
        // endregion

    }
    // endregion


    // region loadCondicaoPagamento
    private void loadCondicaoPagamento() {

        // region Declarando variáveis para objetos locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Criando o WHERE para o campo IdEmpresa
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdEmpresa", _tpEmpresa.IdEmpresa);
            //_sch.addOrderBy("Descricao", eSQLSortType.ASC);
            // endregion

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Lendo as tabelas de preço de acordo com a empresa logada
            dbCondicaoPagamento _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);
            _lstCondicaoPagamento = (ArrayList<tpCondicaoPagamento>) _dbCondicaoPagamento.getList(tpCondicaoPagamento.class, _sch);
            // endregion


            // region Emitindo mensagem ao usuário se não existir registro
            if (_lstCondicaoPagamento == null) {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "A lista de condições de pagamento não foi carregada corretamente",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }
        // endregion

    }
    // endregion


    // region loadTransportadora
    private void loadTransportadora() {

        // region Declarando variáveis para objetos locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Lendo os registros de transportadora
            dbTransportadora _dbTransportadora = new dbTransportadora(_sqh);
            _lstTransportadora = (ArrayList<tpTransportadora>) _dbTransportadora.getList(tpTransportadora.class);
            // endregion

            // region Emitindo mensagem ao usuário se não existir registro
            if (_lstTransportadora == null) {
                Toast.makeText(
                        PedidoMobileEditarActivity.this,
                        "A lista de transportadoras não foi carregada corretamente",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }
        // endregion

    }
    // endregion


    // region loadCliente
    private void loadCliente() {

        // region Declarando variáveis locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido
        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Lendo as informações do cliente
            dbCliente _dbCliente = new dbCliente(_sqh);
            _tpCliente = (tpCliente) _dbCliente.getBySourceId(_IdCliente);
            // endregion

            // region Lendo a tabela de preço de acordo com a empresa
            if (_tpCliente != null) {

                dbTabelaPreco _dbTabelaPreco = new dbTabelaPreco(_sqh);
                _tpCliente.TabelaPrecoEmpresa = _dbTabelaPreco.getTabelaDoCliente(_tpCliente.IdCliente, _tpEmpresa.IdEmpresa);

                if (_tpCliente.IdCondicaoPagamentoPadrao != 0) {
                    dbCondicaoPagamento _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);
                    _tpCliente.CondicaoPagamentoPadrao = (tpCondicaoPagamento) _dbCondicaoPagamento.getById(_tpCliente.IdCondicaoPagamentoPadrao);
                }
            }
            // endregion

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }
        // endregion

    }
    // endregion


    // region loadPedidoMobile
    private void loadPedidoMobile() {

        // region Declarando variáveis locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido
        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Lendo as informações do pedido
            dbPedidoMobile _dbPedidoMobile = new dbPedidoMobile(_sqh);
            _tpPedidoMobile = (tpPedidoMobile) _dbPedidoMobile.getBySourceId(_IdPedidoMobile);
            // endregion

            // region Lendo os itens do pedido se necessário
            if (_tpPedidoMobile != null) {

                // region Setando o pedido como não confirmado
                _tpPedidoMobile.EhConfirmado = 0;
                // endregion

                // region Montado WHERE para o campo IdPedidoMobile
                SQLClauseHelper _schIdPedidoMobile = new SQLClauseHelper();
                _schIdPedidoMobile.addEqualInteger("IdPedidoMobile", _tpPedidoMobile.IdPedidoMobile);
                _schIdPedidoMobile.addOrderBy("_id", eSQLSortType.ASC);
                // endregion

                // region Lendo as informações dos itens do pedido
                dbPedidoMobileItem _dbPedidoMobileItem = new dbPedidoMobileItem(_sqh);
                _tpPedidoMobile.Itens = (ArrayList<tpPedidoMobileItem>) _dbPedidoMobileItem.getList(
                        tpPedidoMobileItem.class,
                        _schIdPedidoMobile
                );
                // endregion

                // region Carregando as informações do produto
                if ((_tpPedidoMobile.Itens != null) && (_tpPedidoMobile.Itens.size() > 0)) {

                    dbProduto _dbProduto = new dbProduto(_sqh);
                    dbTabelaPrecoProduto _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqh);
                    SQLClauseHelper _where = new SQLClauseHelper();


                    for (tpPedidoMobileItem _tp : _tpPedidoMobile.Itens) {
                        _tp.Produto = (tpProduto) _dbProduto.getBySourceId(_tp.IdProduto);
                        _where.clearAll();
                        _where.addEqualInteger("IdProduto", _tp.IdProduto);
                        _where.addEqualInteger("IdTabelaPreco", _tpPedidoMobile.IdTabelaPreco);

                        _tp.Produto.TabelaPrecoProduto = (tpTabelaPrecoProduto) _dbTabelaPrecoProduto.getOne(_where);
                    }
                }
                // endregion
            }
            // endregion

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }
        // endregion

    }
    // endregion


    // region refreshListPosition
    private void refreshListPosition() {

        // region Metodo INSERT
        if (_metodoEdicao == _INSERT_VALUE) {

            // region Tipo de Pedido
            if ((_lstTipoPedido != null) && (_lstTipoPedido.size() > 0)) {
                _iTipoPedido = 0;
                _tpPedidoMobile.IdTipoPedido = _lstTipoPedido.get(_iTipoPedido).IdTipoPedido;
            }
            // endregion

            // region Condicao de Pagamento
            if ((_lstCondicaoPagamento != null) && (_lstCondicaoPagamento.size() > 0)) {
                _iCondicaoPagamento = 0;
                if (_tpCliente != null && _tpCliente.IdCondicaoPagamentoPadrao != 0) {
                    _tpPedidoMobile.IdCondicaoPagamento = _tpCliente.IdCondicaoPagamentoPadrao;

                    for (int i = 0; i < _lstCondicaoPagamento.size(); i++) {
                       if(_tpCliente.IdCondicaoPagamentoPadrao ==  _lstCondicaoPagamento.get(i).IdCondicaoPagamento){
                           _lstCondicaoPagamento.get(i).Descricao += " ( Padrão )";
                            _iCondicaoPagamento = i;
                            break;
                       }
                    }

                } else {
                    _tpPedidoMobile.IdCondicaoPagamento = _lstCondicaoPagamento.get(_iCondicaoPagamento).IdCondicaoPagamento;
                }
            }
            // endregion

            if ((_lstTabelaPreco != null) && (_lstTabelaPreco.size() > 0)) {
                _tpPedidoMobile.IdTabelaPreco = _lstTabelaPreco.get(_iTabelaPreco).IdTabelaPreco;
            }

            // region Transportadora
            if ((_lstTransportadora != null) && (_lstTransportadora.size() > 0)) {
                _iTransportadora = 0;
                _tpPedidoMobile.IdTransportadora = _lstTransportadora.get(_iTransportadora).IdTransportadora;
            }
            // endregion

            // region Pedido Confirmado
            if ((_lstSimNao != null) && (_lstSimNao.size() > 0)) {
                _iEhConfirmado = 0;
                _tpPedidoMobile.EhConfirmado = (int) _lstSimNao.get(_iEhConfirmado).Key;
            }
            // endregion

        }
        // endregion


        // region Metodo UPDATE
        if (_metodoEdicao == _UPDATE_VALUE) {

            // region Tipo de Pedido
            if ((_lstTipoPedido != null) && (_lstTipoPedido.size() > 0)) {

                for (int i = 0; i < _lstTipoPedido.size(); i++) {

                    long _a = _lstTipoPedido.get(i).IdTipoPedido;
                    long _b = _tpPedidoMobile.IdTipoPedido;

                    if (_a == _b) {
                        _iTipoPedido = i;
                        break;
                    }

                }

            }
            // endregion

            // region Tabela de Preço
            if ((_lstTabelaPreco != null) && (_lstTabelaPreco.size() > 0)) {

                for (int i = 0; i < _lstTabelaPreco.size(); i++) {

                    long _a = _lstTabelaPreco.get(i).IdTabelaPreco;
                    long _b = _tpPedidoMobile.IdTabelaPreco;

                    if (_a == _b) {
                        _iTabelaPreco = i;
                    }

                }

            }
            // endregion

            // region Condicao de Pagamento
            if ((_lstCondicaoPagamento != null) && (_lstCondicaoPagamento.size() > 0)) {

                for (int i = 0; i < _lstCondicaoPagamento.size(); i++) {

                    long _a = _lstCondicaoPagamento.get(i).IdCondicaoPagamento;
                    long _b = _tpPedidoMobile.IdCondicaoPagamento;

                    if (_a == _b) {
                        _iCondicaoPagamento = i;
                    }

                }

            }
            // endregion

            // region Transportadora
            if ((_lstTransportadora != null) && (_lstTransportadora.size() > 0)) {

                for (int i = 0; i < _lstTransportadora.size(); i++) {

                    long _a = _lstTransportadora.get(i).IdTransportadora;
                    long _b = _tpPedidoMobile.IdTransportadora;

                    if (_a == _b) {
                        _iTransportadora = i;
                    }

                }

            }
            // endregion

            // region Pedido Confirmado
            if ((_lstSimNao != null) && (_lstSimNao.size() > 0)) {

                for (int i = 0; i < _lstSimNao.size(); i++) {

                    int _a = (int) _lstSimNao.get(i).Key;
                    int _b = _tpPedidoMobile.EhConfirmado;

                    if (_a == _b) {
                        _iEhConfirmado = i;
                    }

                }

            }
            // endregion

        }
        // endregion

    }
    // endregion


    // region afterDescontoPercentual
    private void afterDescontoPercentual() {

        if (_tpPedidoMobile.DescontoPercentual1 == 0) {

            // zerando o desconto por valor
            _tpPedidoMobile.DescontoValor1 = 0;

            // atribuindo o valor unitário para o valor unitário líquido
            _tpPedidoMobile.TotalValorLiquido = _tpPedidoMobile.TotalValor;

        } else {

            // calculando o desconto em valor
            _tpPedidoMobile.DescontoValor1 = ((_tpPedidoMobile.TotalValor * _tpPedidoMobile.DescontoPercentual1) / 100);

            // calculando o valor unitário líquido
            _tpPedidoMobile.TotalValorLiquido = (_tpPedidoMobile.TotalValor - _tpPedidoMobile.DescontoValor1);

        }

    }
    // endregion


    // region afterDescontoValor
    private void afterDescontoValor() {

        if (_tpPedidoMobile.DescontoValor1 == 0) {

            // zerando o desconto em percentual
            _tpPedidoMobile.DescontoPercentual1 = 0;

            // atribuindo o valor unitário para o valor unitário líquido
            _tpPedidoMobile.TotalValorLiquido = _tpPedidoMobile.TotalValor;

        } else {

            // calculando o desconto em percentual
            _tpPedidoMobile.DescontoPercentual1 = (_tpPedidoMobile.DescontoValor1 / _tpPedidoMobile.TotalValor) * 100;

            // calculando o valor unitário líquido
            _tpPedidoMobile.TotalValorLiquido = (_tpPedidoMobile.TotalValor - _tpPedidoMobile.DescontoValor1);

        }

    }
    // endregion


    // region isValid
    private boolean isValid() {

        // region Verificando se foi informado a empresa
        if (_tpPedidoMobile.IdEmpresa == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "EMPRESA",
                    "Não foi possível localizar internamente o identificador da empresa para este pedido"
            );

            return false;

        }
        // endregion


        // region Verificando se existe número no pedido
        if ("".equalsIgnoreCase(_tpPedidoMobile.Numero)) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "NUMERO",
                    "É necessário informar um número antes de tentar salvar este pedido"
            );

            return false;

        }

        if (_AUTOMATICO.equalsIgnoreCase(_tpPedidoMobile.Numero)) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "NUMERO",
                    "É necessário informar um número antes de tentar salvar este pedido"
            );

            return false;

        }
        // endregion


        // region Verificando se foi informado o cliente
        if (_tpPedidoMobile.IdCliente == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "CLIENTE",
                    "Não foi possível localizar internamente o identificador do cliente para este pedido"
            );

            return false;

        }
        // endregion


        // region Verificando se foi informado o tipo de pedido
        if (_tpPedidoMobile.IdTipoPedido == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "TIPO DE PEDIDO",
                    "É necessário selecionar um tipo de pedido antes de tentar salvar este pedido"
            );

            return false;

        }
        // endregion


        // region Verificando se foi informado o tabela de preço
        if (_tpPedidoMobile.IdTabelaPreco == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "TABELA DE PREÇO",
                    "É necessário selecionar uma tabela de preço antes de salvar este pedido"
            );

            return false;

        }
        // endregion


        // region Verificando se foi informado o condição de pagamento
        if (_tpPedidoMobile.IdCondicaoPagamento == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "CONDIÇÃO DE PAGAMENTO",
                    "É necessário selecionar uma condição de pagamento antes de salvar este pedido"
            );

            return false;

        }
        // endregion


        // region Verificando se foi informado o transportadora
        if (_tpPedidoMobile.IdTransportadora == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "TRANSPORTADORA",
                    "É necessário selecionar uma transportadora antes de salvar este pedido"
            );

            return false;

        }
        // endregion


        // region Verificando se existem itens no pedido
        if ((_tpPedidoMobile.Itens != null) && (_tpPedidoMobile.Itens.size() == 0)) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "ITENS",
                    "É necessário registrar um ou mais itens antes de salvar o pedido"
            );

            return false;


        }
        // endregion


        return true;

    }
    // endregion


    // region save
    private void save() {

        // region Verificando se o usuário quer realmente salvar
        MSVMsgBox.showMsgBoxQuestion(
                PedidoMobileEditarActivity.this,
                "Deseja realmente salvar as informações do pedido",
                "Ao confirmar esta opção os dados do pedido serão salvos no banco de dados",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {
                        if (isOk) {

                            // region Declarando variáveis locais
                            SQLiteHelper _sqh = null;
                            dbPedidoMobile _dbPedidoMobile = null;
                            dbPedidoMobileItem _dbPedidoMobileItem = null;
                            // endregion

                            // region Bloco protegido de exceção
                            try {

                                // region Instânciando o objeto de acesso ao banco
                                _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
                                _sqh.open(true);

                                _dbPedidoMobile = new dbPedidoMobile(_sqh);
                                _dbPedidoMobileItem = new dbPedidoMobileItem(_sqh);
                                // endregion


                                // region Informações adicionais para o método INSERT
                                if (_metodoEdicao == _INSERT_VALUE) {

                                    _tpPedidoMobile.IdPedidoMobile = _sqh.nextId("PedidoMobile", "IdPedidoMobile");
                                    _tpPedidoMobile.IdEmpresa = _tpEmpresa.IdEmpresa;
                                    _tpPedidoMobile.IdCliente = _tpCliente.IdCliente;
                                    _tpPedidoMobile.Numero = _dbPedidoMobile.newCode(_tpVendedor.Codigo, _sqh);
                                    _tpPedidoMobile.EmissaoDataHora = MSVUtil.sqliteHojeHora();
                                    _tpPedidoMobile.EhSincronizado = 0;
                                    _tpPedidoMobile.IdVendedor = _tpVendedor.IdVendedor;

                                }
                                // endregion

                                // region Informações adicionais para os demais métodos
                                _tpPedidoMobile.DataAlteracao = MSVUtil.sqliteHojeHora();
                                _tpPedidoMobile.UsuarioAlteracao = _tpVendedor.Codigo;
                                // endregion


                                // region Se o objeto TP for validado então...
                                if (isValid()) {

                                    // region Iniciando a transação com o banco de dados
                                    //_sqh.initTransaction();
                                    // endregion

                                    // region Postando o registro meste
                                    if (_metodoEdicao == _INSERT_VALUE) {
                                        _dbPedidoMobile.insert(_tpPedidoMobile);
                                    } else {
                                        _dbPedidoMobile.update(_tpPedidoMobile);
                                    }
                                    // endregion

                                    // region Removendo os itens existentes no banco para o pedido
                                    if (_metodoEdicao == _UPDATE_VALUE) {

                                        // region Montando WHERE para o campo ID_PEDIDO_MOBILE
                                        SQLClauseHelper _sch = new SQLClauseHelper();
                                        _sch.addEqualInteger("IdPedidoMobile", _tpPedidoMobile.IdPedidoMobile);
                                        // endregion

                                        // region Recuperando a lista de itens
                                        ArrayList<tpPedidoMobileItem> _lstItens =
                                                (ArrayList<tpPedidoMobileItem>) _dbPedidoMobileItem.getList(
                                                        tpPedidoMobileItem.class,
                                                        _sch
                                                );
                                        // endregion

                                        // region Removendo os itens do banco de dados
                                        for (tpPedidoMobileItem _item : _lstItens) {
                                            _dbPedidoMobileItem.delete(_item);
                                        }
                                        // endregion

                                    }
                                    // endregion

                                    // region Postando os itens do pedido
                                    for (tpPedidoMobileItem _tpPedidoMobileItem : _tpPedidoMobile.Itens) {

                                        // postando as informações restantes para o item
                                        _tpPedidoMobileItem.IdPedidoMobileItem = _sqh.nextId("PedidoMobileItem", "IdPedidoMobileItem");
                                        _tpPedidoMobileItem.IdPedidoMobile = _tpPedidoMobile.IdPedidoMobile;
                                        _tpPedidoMobileItem.DataAlteracao = MSVUtil.sqliteHojeHora();
                                        _tpPedidoMobileItem.UsuarioAlteracao = _tpVendedor.Codigo;

                                        // invocando o método de gravação do item
                                        _dbPedidoMobileItem.insert(_tpPedidoMobileItem);

                                    }
                                    // endregion

                                    // region Postando as informações no banco de dados
                                    //_sqh.commitTransaction();
                                    // endregion

                                    // region fechando a activity
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                    // endregion

                                }
                                // endregion

                            } catch (Exception e) {

                                // region Descartar os dados alterados no banco de dados
                                if (_sqh != null && _sqh.isOpen()) {
                                    //_sqh.endTransaction();
                                }
                                // endregion

                                // region Mostrar o erro ao usuário
                                Toast.makeText(
                                        PedidoMobileEditarActivity.this,
                                        "ERRO: " + e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                                // endregion

                            } finally {

                                // region Encerrando a conexão com o banco de dados
                                if (_sqh != null && _sqh.isOpen()) {
                                    _sqh.close();
                                }
                                // endregion

                            }
                            // endregion

                        }
                    }
                }
        );
        // endregion

    }
    // endregion


    // region cancel
    private void cancel() {

        String _op = _metodoEdicao == _INSERT_VALUE ? "INCLUSÃO" : "ALTERAÇÃO";

        MSVMsgBox.showMsgBoxQuestion(
                PedidoMobileEditarActivity.this,
                "Deseja realmente cancelar a operação ?",
                "Ao clicar em Ok você irá cancelar a operação de " + _op + " em andamento",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {
                        if (isOk) {

                            // region fechando a activity
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                            // endregion

                        }
                    }
                }
        );

    }
    // endregion


    // region edit
    private void edit(final int itemIndex) {

        // selecionando o objeto tpPedidoProduto de acordo com o click do usuário
        tpPedidoMobileItem _tpPedidoMobileItem = _tpPedidoMobile.Itens.get(itemIndex);

        // instânciando um objeto de parametro e preenchendo o mesmo
        Bundle _extras = new Bundle();
        _extras.putInt(_KEY_METODO_EDICAO, _UPDATE_VALUE);
        _extras.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_PEDIDO_MOBILE_VALUE);
        _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
        _extras.putSerializable(_KEY_TP_TABELA_PRECO, _lstTabelaPreco.get(_iTabelaPreco));
        _extras.putSerializable(_KEY_TP_PEDIDO_MOBILE_ITEM, _tpPedidoMobileItem);
        _extras.putSerializable(_KEY_ID_CLIENTE, _tpCliente);
        _extras.putInt(_KEY_ITEM_INDEX, itemIndex);

        // invocando a nova activity
        Intent _i = new Intent(PedidoMobileEditarActivity.this, PedidoMobileItemEditarActivity.class);
        _i.putExtras(_extras);
        startActivityForResult(_i, _ITEM_REQUEST_CODE);

    }
    // endregion


    // region delete
    private void delete(final int itemIndex) {

        MSVMsgBox.showMsgBoxQuestion(
                PedidoMobileEditarActivity.this,
                "EXCLUIR ITEM",
                "Deseja realmente excluir este item do pedido ?",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {
                            _tpPedidoMobile.Itens.remove(itemIndex);
                            refreshItens();
                        }

                    }
                }
        );

    }
    // endregion


    // ---------------------------------------------------------- //

    // region importarMix
    private void importarMix() {

        SQLiteHelper _sqh = null;
        dbClienteMix _dbClienteMix = null;
        dbProduto _dbProduto = null;
        dbTabelaPrecoProduto _dbTabelaPrecoProduto = null;
        SQLClauseHelper _whereClienteMix = null;
        SQLClauseHelper _whereProduto = null;
        SQLClauseHelper _wherePreco = null;
        List<tpClienteMix> _lstClienteMix;
        tpPedidoMobileItem _tpItem = null;
        tpProduto _tpProduto = null;
        tpTabelaPrecoProduto _tpTabelaPrecoProduto = null;


        try {

            // region Montando WHERE para o mix de produtos do cliente
            _whereClienteMix = new SQLClauseHelper();
            _whereClienteMix.addEqualInteger("IdCliente", _IdCliente);
            _whereClienteMix.addEqualInteger("IdEmpresa", _tpEmpresa.IdEmpresa);
            _whereClienteMix.addEqualInteger("EhItemConfirmado", 1);
            // endregion


            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileEditarActivity.this);
            _sqh.open(false);
            // endregion


            // region Recuperando o mix de produtos do cliente
            _dbClienteMix = new dbClienteMix(_sqh);
            _lstClienteMix = _dbClienteMix.getList(tpClienteMix.class, _whereClienteMix);
            // endregion

            // region Incluindo a Tabela de preco
            _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqh);
            SQLClauseHelper _whereTabelaPrecoProduto = new SQLClauseHelper();
            // endregion


            // region Recuperando informações acessorias para cada item do mix
            if ((_lstClienteMix != null) && (_lstClienteMix.size() > 0)) {

                // Aqui vamos excluir os itens já anexados no pedido para dar
                // lugar ao mix de produtos confirmados para compra
                _tpPedidoMobile.Itens.clear();

                // Percorrendo os itens do mix confirmados para formar a nova
                // lista de itens do pedido
                for (tpClienteMix _tp : _lstClienteMix) {

                    // region Buscando informações do produto **************************************
                    // region Montando WHERE para o produto
                    _whereProduto = new SQLClauseHelper();
                    _whereProduto.addEqualInteger("IdProduto", _tp.IdProduto);
                    // endregion

                    // region Executando a consulta no banco de dados
                    if (_dbProduto == null) {
                        _dbProduto = new dbProduto(_sqh);
                    }

                    _tpProduto = (tpProduto) _dbProduto.getOne(_whereProduto);
                    // endregion
                    // endregion

                    // region Buscando informações do preço do produto *****************************
                    // region Montando WHERE para a tabela de preço
                    _wherePreco = new SQLClauseHelper();
                    _wherePreco.addEqualInteger("IdProduto", _tp.IdProduto);
                    _wherePreco.addEqualInteger("IdTabelaPreco", _lstTabelaPreco.get(_iTabelaPreco).IdTabelaPreco);
                    // endregion

                    // region Executando a consulta no banco de dados
                    if (_dbTabelaPrecoProduto == null) {
                        _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqh);
                    }

                    _tpTabelaPrecoProduto = (tpTabelaPrecoProduto) _dbTabelaPrecoProduto.getOne(_wherePreco);
                    // endregion
                    // endregion

                    // region Montando um objeto tpPedidoMobileItem ********************************
                    _tpItem = new tpPedidoMobileItem();
                    _tpItem.IdPedidoMobileItem = 0;
                    _tpItem.IdPedidoMobile = 0;
                    _tpItem.IdProduto = _tp.IdProduto;
                    _tpItem.PackQuantidade = _tpProduto.PackQuantidade;
                    _tpItem.UnidadeValor = _tpTabelaPrecoProduto.Preco;
                    _tpItem.UnidadeDescontoPercentual = 0;
                    _tpItem.UnidadeDescontoValor = 0;
                    _tpItem.UnidadeValorLiquido = _tpTabelaPrecoProduto.Preco;
                    _tpItem.UnidadeVendaQuantidade = _tp.PedidoQuantidade;
                    _tpItem.UnidadeValorTotal = _tpItem.UnidadeValor * _tpItem.UnidadeVendaQuantidade;
                    _tpItem.Observacao = null;
                    _tpItem.DataAlteracao = MSVUtil.sqliteHojeHora();
                    _tpItem.UsuarioAlteracao = _tpVendedor.Codigo;

                    _tpItem.Produto = (tpProduto) _tpProduto.clone();
                    // endregion

                    // region Incluindo a Tabela de preco ao produto
                    _whereTabelaPrecoProduto.clearAll();
                    _whereTabelaPrecoProduto.addEqualInteger("IdProduto", _tpItem.IdProduto);
                    _whereTabelaPrecoProduto.addEqualInteger("IdTabelaPreco", _tpPedidoMobile.IdTabelaPreco);

                    _tpItem.Produto.TabelaPrecoProduto = (tpTabelaPrecoProduto) _dbTabelaPrecoProduto.getOne(_whereTabelaPrecoProduto);
                    // endregion

                    // region Adicionando o item na lista do pedido ********************************
                    _tpPedidoMobile.Itens.add(_tpItem);
                    // endregion
                }
            }
            // endregion


            // region Atualizando a lista de produtos do pedido
            this.refreshItens();
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileEditarActivity.this,
                    "Erro ao importar os itens confirmados do mix de produto do cliente",
                    e.getMessage()
            );

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region onDestroy
    @Override
    protected void onDestroy(){
        super.onDestroy();
        _PESQUISA_VALUE = "";
    }
    // endregion
}
