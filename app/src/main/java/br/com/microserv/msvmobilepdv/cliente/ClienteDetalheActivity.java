package br.com.microserv.msvmobilepdv.cliente;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import br.com.microserv.framework.msvapi.PedidoApi;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvdal.dbCidade;
import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbCondicaoPagamento;
import br.com.microserv.framework.msvdal.dbEmpresa;
import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdal.dbPedidoMobile;
import br.com.microserv.framework.msvdal.dbPedidoMobileItem;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdal.dbRegiao;
import br.com.microserv.framework.msvdal.dbSincronizacao;
import br.com.microserv.framework.msvdal.dbTabelaPreco;
import br.com.microserv.framework.msvdal.dbTipoPedido;
import br.com.microserv.framework.msvdal.dbTransportadora;
import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpAndroidContact;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteProdutoMixRow;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpFinanceiro;
import br.com.microserv.framework.msvdto.tpKeyValueRow;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpRegiao;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvdto.tpTransportadora;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvinterface.OnSelectedItem;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;
import br.com.microserv.msvmobilepdv.pedido.PedidoEmailEnviar;
import br.com.microserv.msvmobilepdv.pedido.PedidoMobileEditarActivity;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.ClienteFinanceiroParcelaAdapter;
import br.com.microserv.msvmobilepdv.adapter.KeyValueDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.PedidoMobileListaAdapter;

public class ClienteDetalheActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // RequestCode
    static final int _PEDIDO_REQUEST_CODE = 100;
    static final int _CONTACT_ADD_REQUEST_CODE = 101;
    static final int _CLIENTE_EDITAR_REQUEST_CODE = 102;

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_ID_CLIENTE = "IdCliente";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_TP_CLIENTE = "tpCliente";
    static final String _KEY_ID_PEDIDO_MOBILE = "IdPedidoMobile";
    static final String _KEY_CLIENTE_id = "_id";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final String _CLIENTE_DETALHE_VALUE = "ClienteDetalheActivity";
    // endregion


    // region Declarando objetos da activity

    // View
    View _inCabecalho = null;
    View _inClienteDetalhePedido = null;
    View _inClienteDetalheMix = null;
    View _inClienteDetalheFinanceiro = null;
    View _inClienteDetalheFicha = null;

    // region Cabecalho
    // TextView
    TextView _cab_txtClienteNomeFantasia = null;
    TextView _cab_txtClienteRazaoSocial = null;
    TextView _cab_txtClienteDocumento = null;
    TextView _cab_txtClienteCodigo = null;
    // endregion

    // region Guias

    // LinearLayout
    LinearLayout _pnlPedidoMnt = null;
    LinearLayout _pnlFinanceiroMnt = null;
    LinearLayout _pnlFichaMnt = null;

    // endregion

    // region Pedidos
    // TextView
    TextView _ped_txtTitulo = null;
    TextView _ped_txtNovoPedido = null;
    TextView _ped_txtRegistro = null;
    TextView _ped_txtTotal = null;

    // ListView
    ListView _ped_livPedidoLista = null;
    // endregion

    // region Mix de Produtos
    // TextView
    TextView _mix_txtContarEstoque = null;
    TextView _mix_txtRegistro = null;

    // ListView
    ListView _mix_livMixLista = null;
    // endregion

    // region Situação Financeira
    // TextView
    TextView _fin_txtRegistro = null;
    TextView _fin_txtTotalDevidoGeral = null;
    TextView _fin_txtTotalDevidoVencido = null;

    // ListView
    ListView _fin_livParcelaLista = null;
    // endregion

    // region Ficha cadastral
    // LinearLayout
    LinearLayout _fca_pnlAgendaContent = null;

    // TextView
    TextView _fca_txtEditar = null;

    TextView _fca_txtCodigo = null;
    TextView _fca_txtRazaoSocial = null;
    TextView _fca_txtNomeFantasia = null;
    TextView _fca_txtCnpjCpf = null;
    TextView _fca_txtIeRg = null;
    TextView _fca_txtTabelaPrecoPadrao = null;
    TextView _fca_txtTelefoneFixo = null;
    TextView _fca_txtTelefoneCelular = null;
    TextView _fca_txtEmail = null;
    TextView _fca_txtContatoNome = null;
    TextView _fca_txtAgenda = null;
    TextView _fca_txtLogradouroTipo = null;
    TextView _fca_txtLogradouroNome = null;
    TextView _fca_txtLogradouroNumero = null;
    TextView _fca_txtBairro = null;
    TextView _fca_txtCidade = null;
    TextView _fca_txtEstado = null;
    TextView _fca_txtCep = null;
    TextView _fca_txtRegiao = null;
    TextView _fca_txtArea = null;

    // ImageView
    ImageView _fca_imgEmail = null;
    ImageView _fca_imgTelefoneFixo = null;
    ImageView _fca_imgTelefoneCelular = null;
    ImageView _fca_imgAgendaOk = null;
    ImageView _fca_imgAgendaAdd = null;
    // endregion

    // endregion


    // region Declarando variaveis locais

    // Objetos
    Bundle _extras = null;
    ArrayList<tpPedidoMobile> _lstPedidoMobile = null;
    ArrayList<tpClienteProdutoMixRow> _lstMix = null;
    ArrayList<tpFinanceiro> _lstFinanceiro = null;
    ArrayList<tpKeyValueRow> _lstItemOptions = null;
    ArrayList<Long> _lstPedidoMobileSync = null;
    tpEmpresa _tpEmpresa = null;
    tpCliente _tpCliente = null;
    tpParametro _tpUtilizaMixProduto = null;
    tpParametro _tpServidorRestIp = null;
    tpParametro _tpVendedorCodigo = null;
    tpKeyValueRow _tpOption = null;
    tpAndroidContact _tpContactFixo = null;
    tpAndroidContact _tpContactCelular = null;
    tpParametro _tpPermiteAdicionarCliente = null;
    tpParametro _tpPermiteEditarCliente = null;
    KeyValueDialogSearchAdapter _adpKeyValue = null;
    PedidoMobileListaAdapter _adpPedidoMobile = null;

    // Dialogs
    ProgressDialog _wait = null;

    // String
    String _sourceActivity = "";

    // int
    int _metodoEdicao = 0;
    int _iAux = -1;
    int _parcelasQuantidadeVencida = 0;

    // long
    long _IdCliente = 0;
    long _id = 0;

    // double
    double _parcelasTotal = 0;
    double _parcelasTotalVencido = 0;

    // boolean
    boolean _isSyncToday = false;
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detalhe);
        // endregion

        // region Adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);
        // endregion

        // region Selecionando os parametros enviados através do Bundle
        Bundle _extras = getIntent().getExtras();

        if (_extras != null) {

            // region _KEY_METODO_EDICAO
            if (_extras.containsKey(_KEY_METODO_EDICAO)) {
                _metodoEdicao = _extras.getInt(_KEY_METODO_EDICAO);
            } else {
                Toast.makeText(
                        ClienteDetalheActivity.this,
                        "O parâmetro _KEY_METODO_EDICAO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_SOURCE_ACTIVITY
            if (_extras.containsKey(_KEY_SOURCE_ACTIVITY)) {
                _sourceActivity = _extras.getString(_KEY_SOURCE_ACTIVITY);
            } else {
                Toast.makeText(
                        ClienteDetalheActivity.this,
                        "O parâmetro _KEY_SOURCE_ACTIVITY não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_ID_CLIENTE
            if (_extras.containsKey(_KEY_ID_CLIENTE)) {
                _IdCliente = _extras.getLong(_KEY_ID_CLIENTE);
            } else {
                Toast.makeText(
                        ClienteDetalheActivity.this,
                        "O parâmetro _KEY_ITEM_INDEX não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_TP_EMPRESA
            if (_extras.containsKey(_KEY_TP_EMPRESA)) {
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);
            } else {
                Toast.makeText(
                        ClienteDetalheActivity.this,
                        "O parâmetro _KEY_TP_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_CLIENTE_id
            if (_extras.containsKey(_KEY_CLIENTE_id)) {
                _id = _extras.getLong(_KEY_CLIENTE_id);
            } else {
                Toast.makeText(
                        ClienteDetalheActivity.this,
                        "O parâmetro _KEY_CLIENTE_id não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion
        }
        // endregion

        // region Por padrão vamos informar que o formulário resultou OK
        // isto é necessário para informar a activity ClienteListaActivity que a
        // activity resultou ok em seu processamento e assim vamos atualizar a lista
        // de clientes quando esta activity for encerrada
        setResult(Activity.RESULT_OK);
        // endregion

        // region Invocando os metodos que tratam dos elementos da tela
        bindElements();
        bindEvents();
        initialize();
        // endregion
    }
    // endregion


    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_cliente_detalhe, menu);

        /*
        if ((_tpUtilizaMixProduto != null) && (_tpUtilizaMixProduto.ValorInteiro == 0)) {
            MenuItem _mnMix = menu.findItem(R.id.mnMix);

            if (_mnMix != null) {
                _mnMix.setVisible(false);
            }
        }*/
        return true;
    }
    // endregion


    // region onOptionItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // region Executando a ação de acordo com o menu selecionado
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.mnPedido:
                _inClienteDetalhePedido.setVisibility(View.GONE);
                _inClienteDetalheMix.setVisibility(View.GONE);
                _inClienteDetalheFinanceiro.setVisibility(View.GONE);
                _inClienteDetalheFicha.setVisibility(View.GONE);

                this.loadPedido();
                this.showPedido();

                _inClienteDetalhePedido.setVisibility(View.VISIBLE);

                _pnlPedidoMnt.setBackgroundResource(R.color.indigo_300);
                _pnlFinanceiroMnt.setBackgroundResource(R.color.indigo_500);
                _pnlFichaMnt.setBackgroundResource(R.color.indigo_500);
                break;

            case R.id.mnMix:
                Bundle _params = new Bundle();
                _params.putInt(_KEY_METODO_EDICAO, _LOOKUP_VALUE);
                _params.putString(_KEY_SOURCE_ACTIVITY, "ClienteDetalheActivity");
                _params.putLong(_KEY_ID_CLIENTE, _IdCliente);
                _params.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);

                Intent _i = new Intent(ClienteDetalheActivity.this, ClienteMixActivity.class);
                _i.putExtras(_params);
                startActivity(_i);
                break;

            case R.id.mnFinanceiro:
                _inClienteDetalhePedido.setVisibility(View.GONE);
                _inClienteDetalheMix.setVisibility(View.GONE);
                _inClienteDetalheFinanceiro.setVisibility(View.GONE);
                _inClienteDetalheFicha.setVisibility(View.GONE);

                this.loadFinanceiro();
                this.showFinanceiro();

                _inClienteDetalheFinanceiro.setVisibility(View.VISIBLE);

                _pnlPedidoMnt.setBackgroundResource(R.color.indigo_500);
                _pnlFinanceiroMnt.setBackgroundResource(R.color.indigo_300);
                _pnlFichaMnt.setBackgroundResource(R.color.indigo_500);
                break;

            case R.id.mnFicha:
                _inClienteDetalhePedido.setVisibility(View.GONE);
                _inClienteDetalheMix.setVisibility(View.GONE);
                _inClienteDetalheFinanceiro.setVisibility(View.GONE);
                _inClienteDetalheFicha.setVisibility(View.GONE);

                _inClienteDetalheFicha.setVisibility(View.VISIBLE);

                _pnlPedidoMnt.setBackgroundResource(R.color.indigo_500);
                _pnlFinanceiroMnt.setBackgroundResource(R.color.indigo_500);
                _pnlFichaMnt.setBackgroundResource(R.color.indigo_300);
                break;
        }
        // endregion

        // region Invocando o método construtor
        return super.onOptionsItemSelected(item);
        // endregion
    }
    // endregion


    // region onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // region Invocando o metodo do objeto ancestral
        super.onActivityResult(requestCode, resultCode, data);
        // endregion

        // region De acordo com o código de retorno fazer...
        switch (requestCode) {

            case _PEDIDO_REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {

                    // region Capturando os parametros retornados pela activity
                    this.loadPedido();
                    this.showPedido();
                    // endregion
                }
                break;

            case _CONTACT_ADD_REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {
                    this.showAgendaContent();
                }
                break;

            case _CLIENTE_EDITAR_REQUEST_CODE:

                if (resultCode == Activity.RESULT_OK) {
                    this.loadCliente();
                    this.showCliente();
                }
                break;
        }
        // endregion
    }
    //endregion


    // region bindElements
    @Override
    public void bindElements() {

        // View
        _inCabecalho = (View) findViewById(R.id.inCabecalho);
        _inClienteDetalhePedido = (View) findViewById(R.id.inClienteDetalhePedido);
        _inClienteDetalheMix = (View) findViewById(R.id.inClienteDetalheMix);
        _inClienteDetalheFinanceiro = (View) findViewById(R.id.inClienteDetalheFinanceiro);
        _inClienteDetalheFicha = (View) findViewById(R.id.inClienteDetalheFicha);

        // region Include do cabecalho
        // TextView
        _cab_txtClienteNomeFantasia = (TextView) _inCabecalho.findViewById(R.id.txtClienteNomeFantasia);
        _cab_txtClienteRazaoSocial = (TextView) _inCabecalho.findViewById(R.id.txtClienteRazaoSocial);
        _cab_txtClienteDocumento = (TextView) _inCabecalho.findViewById(R.id.txtClienteDocumento);
        _cab_txtClienteCodigo = (TextView) _inCabecalho.findViewById(R.id.txtClienteCodigo);
        // endregion

        // region Guias
        // LinearLayout
        _pnlPedidoMnt = (LinearLayout) findViewById(R.id.pnlPedidoMnt);
        _pnlFinanceiroMnt = (LinearLayout) findViewById(R.id.pnlFinanceiroMnt);
        _pnlFichaMnt = (LinearLayout) findViewById(R.id.pnlFichaMnt);
        // endregion

        // region Include de pedidos
        // TextView
        _ped_txtTitulo = (TextView) _inClienteDetalhePedido.findViewById(R.id.txtTitulo);
        _ped_txtNovoPedido = (TextView) _inClienteDetalhePedido.findViewById(R.id.txtNovoPedido);
        _ped_txtRegistro = (TextView) _inClienteDetalhePedido.findViewById(R.id.txtRegistro);
        _ped_txtTotal = (TextView) _inClienteDetalhePedido.findViewById(R.id.txtTotal);

        // ListView
        _ped_livPedidoLista = (ListView) _inClienteDetalhePedido.findViewById(R.id.livPedidoLista);
        // endregion

        // region Include de Mix de Produtos
        // TextView
        _mix_txtContarEstoque = (TextView) _inClienteDetalheMix.findViewById(R.id.txtContarEstoque);
        _mix_txtRegistro = (TextView) _inClienteDetalheMix.findViewById(R.id.txtRegistro);

        // ListView
        _mix_livMixLista = (ListView) _inClienteDetalheMix.findViewById(R.id.livMixLista);
        // endregion

        // region Situação Financeira
        // TextView
        _fin_txtRegistro = (TextView) _inClienteDetalheFinanceiro.findViewById(R.id.txtRegistro);
        _fin_txtTotalDevidoGeral = (TextView) _inClienteDetalheFinanceiro.findViewById(R.id.txtTotalDevidoGeral);
        _fin_txtTotalDevidoVencido = (TextView) _inClienteDetalheFinanceiro.findViewById(R.id.txtTotalDevidoVencido);

        // ListView
        _fin_livParcelaLista = (ListView) _inClienteDetalheFinanceiro.findViewById(R.id.livParcelaLista);
        // endregion

        // region Ficha cadastral
        // LinearLayout
        _fca_pnlAgendaContent = (LinearLayout) _inClienteDetalheFicha.findViewById(R.id.pnlAgendaContent);

        // TextView
        _fca_txtEditar = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtEditar);

        _fca_txtCodigo = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtCodigo);
        _fca_txtRazaoSocial = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtRazaoSocial);
        _fca_txtNomeFantasia = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtNomeFantasia);
        _fca_txtCnpjCpf = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtCnpjCpf);
        _fca_txtIeRg = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtIeRg);
        _fca_txtTabelaPrecoPadrao = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtTabelaPreco);
        _fca_txtTelefoneFixo = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtTelefoneFixo);
        _fca_txtTelefoneCelular = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtTelefoneCelular);
        _fca_txtEmail = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtEmail);
        _fca_txtContatoNome = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtContatoNome);
        _fca_txtAgenda = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtAgenda);
        _fca_txtLogradouroTipo = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtLogradouroTipo);
        _fca_txtLogradouroNome = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtLogradouroNome);
        _fca_txtLogradouroNumero = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtLogradouroNumero);
        _fca_txtBairro = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtBairro);
        _fca_txtCidade = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtCidade);
        _fca_txtEstado = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtEstado);
        _fca_txtCep = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtCep);
        _fca_txtRegiao = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtRegiao);
        _fca_txtArea = (TextView) _inClienteDetalheFicha.findViewById(R.id.txtArea);

        // ImageView
        _fca_imgEmail = (ImageView) _inClienteDetalheFicha.findViewById(R.id.imgEmail);
        _fca_imgTelefoneFixo = (ImageView) _inClienteDetalheFicha.findViewById(R.id.imgTelefoneFixo);
        _fca_imgTelefoneCelular = (ImageView) _inClienteDetalheFicha.findViewById(R.id.imgTelefoneCelular);
        _fca_imgAgendaOk = (ImageView) _inClienteDetalheFicha.findViewById(R.id.imgAgendaOk);
        _fca_imgAgendaAdd = (ImageView) _inClienteDetalheFicha.findViewById(R.id.imgAgendaAdd);
        // endregion
    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region NAVEGADOR

        // region Click em PEDIDO
        _pnlPedidoMnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _inClienteDetalhePedido.setVisibility(view.VISIBLE);
                _inClienteDetalheFinanceiro.setVisibility(view.GONE);
                _inClienteDetalheFicha.setVisibility(view.GONE);

                _pnlPedidoMnt.setBackgroundResource(R.color.indigo_300);
                _pnlFinanceiroMnt.setBackgroundResource(R.color.indigo_500);
                _pnlFichaMnt.setBackgroundResource(R.color.indigo_500);
            }
        });
        // endregion

        // region Click em FINANCEIRO
        _pnlFinanceiroMnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _inClienteDetalhePedido.setVisibility(view.GONE);
                _inClienteDetalheFinanceiro.setVisibility(view.VISIBLE);
                _inClienteDetalheFicha.setVisibility(view.GONE);

                _pnlPedidoMnt.setBackgroundResource(R.color.indigo_500);
                _pnlFinanceiroMnt.setBackgroundResource(R.color.indigo_300);
                _pnlFichaMnt.setBackgroundResource(R.color.indigo_500);
            }
        });
        // endregion

        // region Click em FICHA
        _pnlFichaMnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _inClienteDetalhePedido.setVisibility(view.GONE);
                _inClienteDetalheFinanceiro.setVisibility(view.GONE);
                _inClienteDetalheFicha.setVisibility(view.VISIBLE);

                _pnlPedidoMnt.setBackgroundResource(R.color.indigo_500);
                _pnlFinanceiroMnt.setBackgroundResource(R.color.indigo_500);
                _pnlFichaMnt.setBackgroundResource(R.color.indigo_300);
            }
        });
        // endregion
        // endregion

        // region Clique em NOVO PEDIDO
        _ped_txtNovoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_isSyncToday == false) {
                    MSVMsgBox.showMsgBoxWarning(
                            ClienteDetalheActivity.this,
                            "Atenção, antes de realizar a venda é necessário sincronizar este dispositivo",
                            "Verifique se seu dispositivo está conectado na internet e então entre na opção de Sincronização no menu principal."
                    );
                } else {

                    MSVUtil.vibrate(ClienteDetalheActivity.this);

                    Bundle _extras = new Bundle();
                    _extras.putInt(_KEY_METODO_EDICAO, _INSERT_VALUE);
                    _extras.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_DETALHE_VALUE);
                    _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
                    _extras.putLong(_KEY_ID_CLIENTE, _tpCliente.IdCliente);
                    _extras.putLong(_KEY_ID_PEDIDO_MOBILE, 0);

                    Intent _intent = new Intent(ClienteDetalheActivity.this, PedidoMobileEditarActivity.class);
                    _intent.putExtras(_extras);

                    startActivityForResult(_intent, _PEDIDO_REQUEST_CODE);
                }
            }
        });
        // endregion

        // region Clique em ITEM da LISTA de PEDIDO
        _ped_livPedidoLista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // region Carregando as informações do pedido selecionado
                final int _itemIndex = position;
                final tpPedidoMobile _tp = _lstPedidoMobile.get(position);
                // endregion

                // region Verificando a necessidade de instânciar o adapter
                if (_adpKeyValue == null) {
                    _adpKeyValue = new KeyValueDialogSearchAdapter(
                            ClienteDetalheActivity.this,
                            _lstItemOptions
                    );
                }
                // endregion

                // region Montando a janela de escolha de opções
                _iAux = -1;
                _tpOption = null;

                MSVMsgBox.getValueFromList(
                        ClienteDetalheActivity.this,
                        "OPÇÕES",
                        _adpKeyValue,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                                _tpOption = (tpKeyValueRow) tp;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {

                                    // region Quando o usuário não seleciona nada e clica em Ok
                                    if (_iAux == -1) {

                                        Toast.makeText(
                                                ClienteDetalheActivity.this,
                                                "Nenhuma opção foi selecionada pelo usuário",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        return;
                                    }
                                    // endregion

                                    // region Processando a opção do usuário

                                    // region 0 - EDITAR
                                    if (_tpOption.Key == 0) {
                                        editPedido(_itemIndex);
                                    }
                                    // endregion

                                    // region 1 - CONFIRMAR PEDIDO
                                    if (_tpOption.Key == 1) {
                                        confirmPedido(_itemIndex);
                                    }
                                    // endregion

                                    // region 2 - SINCRONIZAR
                                    if (_tpOption.Key == 2) {
                                        sincronizarPedido(_itemIndex);
                                    }
                                    // endregion

                                    // region 3 - ENVIAR POR E-MAIL
                                    if (_tpOption.Key == 3) {
                                        emailEnviar(_itemIndex);
                                    }
                                    // endregion

                                    // region 4 - ENVIAR POR WHATSAPP
                                    if (_tpOption.Key == 4) {
                                        whatsappEnviar(_itemIndex);
                                    }
                                    // endregion

                                    // region 5 - EXCLUIR
                                    if (_tpOption.Key == 5) {

                                        MSVMsgBox.showMsgBoxQuestion(
                                                ClienteDetalheActivity.this,
                                                "Deseja realmente excluir este pedido ?",
                                                "Se clicar em OK este pedido será removido do dispositivo",
                                                new OnCloseDialog() {
                                                    @Override
                                                    public void onCloseDialog(boolean isOk, String value) {
                                                        if (isOk) {
                                                            deletePedido(_itemIndex);
                                                        }
                                                    }
                                                }
                                        );
                                    }
                                    // endregion
                                    // endregion
                                }
                                // endregion
                            }
                        }
                );
                // endregion

                return true;
            }
        });
        // endregion

        // region Clique em EDITAR
        _fca_txtEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Montando os parametros para envio a proxima activity
                Bundle _params = new Bundle();
                _params.putInt(_KEY_METODO_EDICAO, _UPDATE_VALUE);
                _params.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_DETALHE_VALUE);
                _params.putLong(_KEY_ID_CLIENTE, _IdCliente);
                _params.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
                // endregion

                // region Invocando a proxima activity (ClienteOpcaoActivity)
                Intent _i = new Intent(ClienteDetalheActivity.this, ClienteEditarActivity.class);
                _i.putExtras(_params);
                startActivityForResult(_i, _CLIENTE_EDITAR_REQUEST_CODE);
                // endregion
            }
        });
        // endregion

        // region Clique em EMAIL (SEND)
        _fca_imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String _to = _tpCliente.Email;
                String _subject = "Assunto...";

                sendMail(_to, _subject, "Texto do e-mail...");
            }
        });
        // endregion

        // region Clique em TELEFONE FIXO (CALL)
        _fca_imgTelefoneFixo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent _i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", _tpCliente.TelefoneFixo, null));
                startActivity(_i);
            }
        });
        // endregion

        // region Clique em TELEFONE CELULAR (CALL)
        _fca_imgTelefoneCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent _i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", _tpCliente.TelefoneCelular, null));
                startActivity(_i);
            }
        });
        // endregion

        // region Clique em AGENDA (ADD)
        _fca_imgAgendaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addContact();
            }
        });
        // endregion
    }
    // endregion


    // region initialize
    public void initialize() {

        // region Cuidando para mostrar o include de pedidos
        _inClienteDetalhePedido.setVisibility(View.GONE);
        _inClienteDetalheMix.setVisibility(View.GONE);
        _inClienteDetalheFinanceiro.setVisibility(View.GONE);
        _inClienteDetalheFicha.setVisibility(View.GONE);
        _inClienteDetalhePedido.setVisibility(View.VISIBLE);
        // endregion

        // region Lendo informações necessárias para o funcionamento da activity
        this.loadParameters();
        this.loadSincronizacao();
        // endregion

        // region Desabilitando componentes da tela de acordo os parametro do APP
        _fca_txtEditar.setVisibility(View.GONE);

        if (_tpPermiteEditarCliente.ValorInteiro == 1) {
            _fca_txtEditar.setVisibility(View.VISIBLE);
        }
        // endregion

        // region Cuidando para mostrar os dados do cliente selecionado
        this.loadCliente();

        if (_tpCliente != null) {

            this.loadFinanceiro();
            this.showCliente();
            this.showAgendaContent();
            this.showFinanceiro();

        } else {
            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "Não foi possível recuperar os dados do cliente selecionado"
            );
        }
        // endregion

        // region Cuidando para mostrar os dados do include de pedidos
        this.loadPedido();
        this.showPedido();
        // endregion

        // region Lendo as opções do pedido selecionado
        this.loadItemOptions();
        // endregion
    }
    // endregion


    // region loadParameters
    private void loadParameters() {

        SQLiteHelper _sqh = null;
        dbParametro _dbParametro = null;

        try {

            _sqh = new SQLiteHelper(ClienteDetalheActivity.this);
            _sqh.open(false);

            _dbParametro = new dbParametro(_sqh);

            _tpUtilizaMixProduto = (tpParametro) _dbParametro.getUtilizaMixProduto();
            _tpServidorRestIp = _dbParametro.getServidorRestIp();
            _tpVendedorCodigo = _dbParametro.getVendedorCodigo();
            _tpPermiteAdicionarCliente = _dbParametro.getPermiteAdicionarCliente();
            _tpPermiteEditarCliente = _dbParametro.getPermiteAlterarCliente();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }
    }
    // endregion


    // region loadCliente
    private void loadCliente() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(ClienteDetalheActivity.this);
            _sqh.open(false);

            // region Carregando os dados de cliente
            dbCliente _dbCliente = new dbCliente(_sqh);

            // region Montando WHERE para o campo _id
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("_id", _id);

            // region Selecionando o cliente pelo campo _id
            _tpCliente = (tpCliente) _dbCliente.getOne(_sch);

            if (_tpCliente != null) {

                // region 01 - Carregando as informações de cidade/estado
                dbCidade _dbCidade = new dbCidade(_sqh);
                _tpCliente.Cidade = (tpCidade) _dbCidade.getBySourceId(_tpCliente.IdCidade);
                // endregion

                // region 02 - Carregando as informações de região/área
                dbRegiao _dbRegiao = new dbRegiao(_sqh);
                _tpCliente.Regiao = (tpRegiao) _dbRegiao.getBySourceId(_tpCliente.IdRegiao);
                // endregion

                // region 03 - Carregando Tabela de Preco
                dbTabelaPreco _dbTabelaPreco = new dbTabelaPreco(_sqh);
                _tpCliente.TabelaPrecoEmpresa = (tpTabelaPreco) _dbTabelaPreco.getBySourceId(_tpCliente.IdTabelaPreco);
                // endregion
            }
            // endregion

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }
    }
    // endregion


    // region showCliente
    private void showCliente() {

        if (_tpCliente != null) {

            // region Imprimindo as informações do cabeçalho
            _cab_txtClienteNomeFantasia.setText(_tpCliente.NomeFantasia);
            _cab_txtClienteRazaoSocial.setText(_tpCliente.RazaoSocial);
            _cab_txtClienteCodigo.setText(_tpCliente.Codigo);

            if (_tpCliente.CnpjCpf.length() == 11) {
                _cab_txtClienteDocumento.setText(MSVUtil.formatCpf(_tpCliente.CnpjCpf));
            } else {
                _cab_txtClienteDocumento.setText(MSVUtil.formatCnpj(_tpCliente.CnpjCpf));
            }
            // endregion

            // region Imprimindo as informações da ficha cadastral

            // region 01 - Imprimindo os dados básicos do objeto _tpCliente
            _fca_txtCodigo.setText(_tpCliente.Codigo);
            _fca_txtRazaoSocial.setText(_tpCliente.RazaoSocial);
            _fca_txtNomeFantasia.setText(_tpCliente.NomeFantasia);
            _fca_txtIeRg.setText(_tpCliente.IeRg);
            _fca_txtTelefoneFixo.setText(MSVUtil.formatTelefoneFixo(_tpCliente.TelefoneFixo));
            _fca_txtTelefoneCelular.setText(MSVUtil.formatTelefoneCelular(_tpCliente.TelefoneCelular));
            _fca_txtEmail.setText(_tpCliente.Email);
            _fca_txtContatoNome.setText(_tpCliente.ContatoNome);
            _fca_txtLogradouroTipo.setText(_tpCliente.LogradouroTipo);
            _fca_txtLogradouroNome.setText(_tpCliente.LogradouroNome);
            _fca_txtLogradouroNumero.setText(_tpCliente.LogradouroNumero);
            _fca_txtBairro.setText(_tpCliente.BairroNome);
            _fca_txtCep.setText(_tpCliente.Cep);

            // tratando o número de documento do cliente
            if (_tpCliente.CnpjCpf.length() == 11) {
                _fca_txtCnpjCpf.setText(MSVUtil.formatCpf(_tpCliente.CnpjCpf));
            } else {
                _fca_txtCnpjCpf.setText(MSVUtil.formatCnpj(_tpCliente.CnpjCpf));
            }

            if (_tpCliente.TabelaPrecoEmpresa != null) {
                _fca_txtTabelaPrecoPadrao.setText(_tpCliente.TabelaPrecoEmpresa.Descricao);
            }

            // verificando a necessidade de mostrar a imagem de email
            _fca_imgEmail.setVisibility(View.GONE);

            if (MSVUtil.isNullOrEmpty(_tpCliente.Email) == false) {
                _fca_imgEmail.setVisibility(View.VISIBLE);
            }

            // verificando a necessidade de mostrar a imagem de telefone fixo
            _fca_imgTelefoneFixo.setVisibility(View.GONE);

            if (MSVUtil.isNullOrEmpty(_tpCliente.TelefoneFixo) == false) {
                _fca_imgTelefoneFixo.setVisibility(View.VISIBLE);
            }

            // verificando a necessidade de mostrar a imagem de telefone celular
            _fca_imgTelefoneCelular.setVisibility(View.GONE);

            if (MSVUtil.isNullOrEmpty(_tpCliente.TelefoneCelular) == false) {
                _fca_imgTelefoneCelular.setVisibility(View.VISIBLE);
            }
            // endregion

            // region 02 - Imprimindo os dados de cidade/estado
            if (_tpCliente.Cidade != null) {
                _fca_txtCidade.setText(_tpCliente.Cidade.Descricao);
                _fca_txtEstado.setText(_tpCliente.Cidade.EstadoSigla);
            }
            // endregion

            // region 03 - Imprimindo os dados de região/área
            if (_tpCliente.Regiao != null) {
                _fca_txtRegiao.setText(_tpCliente.Regiao.Descricao);
                _fca_txtArea.setText(_tpCliente.Regiao.AreaDescricao);
            }
            // endregion
            // endregion
        }
    }
    // endregion


    // region loadPedido
    private void loadPedido() {

        SQLiteHelper _sqh = null;

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdEmpresa", _tpEmpresa.IdEmpresa);
            _sch.addEqualInteger("IdCliente", _tpCliente.IdCliente);
            _sch.addOrderBy("Numero", eSQLSortType.DESC);

            _sqh = new SQLiteHelper(ClienteDetalheActivity.this);
            _sqh.open(false);

            dbPedidoMobile _dbPedidoMobile = new dbPedidoMobile(_sqh);
            _lstPedidoMobile = (ArrayList<tpPedidoMobile>) _dbPedidoMobile.getList(tpPedidoMobile.class, _sch);

            if ((_lstPedidoMobile != null) && (_lstPedidoMobile.size() != 0)) {

                dbTipoPedido _dbTipoPedido = new dbTipoPedido(_sqh);
                dbCondicaoPagamento _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);

                for (tpPedidoMobile _tp : _lstPedidoMobile) {
                    _tp.TipoPedido = (tpTipoPedido) _dbTipoPedido.getBySourceId(_tp.IdTipoPedido);
                    _tp.CondicaoPagamento = (tpCondicaoPagamento) _dbCondicaoPagamento.getBySourceId(_tp.IdCondicaoPagamento);
                }
            }

        } catch (Exception e) {

            Log.e("SQLite", "PedidoMobileListaActivity -> loadPedido -> " + e.getMessage());

            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "Erro ao buscar os pedidos vinculados ao cliente",
                    e.getMessage()
            );

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }
    }
    // endregion


    // region editPedido
    private void editPedido(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = _lstPedidoMobile.get(itemIndex);
        // endregion

        // region Verificano se o mesmo já está sincronizado
        if (_tp.EhSincronizado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    ClienteDetalheActivity.this,
                    "ATENÇÃO",
                    "Não é permitido alterar dados de um pedido que já foi sincronizado com a base de pedidos da empresa"
            );

            return;
        }
        // endregion

        if (_isSyncToday == false) {

            MSVMsgBox.showMsgBoxWarning(
                    ClienteDetalheActivity.this,
                    "Atenção, antes de realizar a venda é necessário sincronizar este dispositivo",
                    "Verifique se seu dispositivo está conectado na internet e então entre na opção de Sincronização no menu principal."
            );

        } else {

            // region Montando os parâmetros para invocar a tela de edição
            Bundle _extras = new Bundle();
            _extras.putInt(_KEY_METODO_EDICAO, _UPDATE_VALUE);
            _extras.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_DETALHE_VALUE);
            _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
            _extras.putLong(_KEY_ID_CLIENTE, _tp.IdCliente);
            _extras.putLong(_KEY_ID_PEDIDO_MOBILE, _tp.IdPedidoMobile);
            // endregion

            // region Invocando a activity apropriada
            Intent _intent = new Intent(ClienteDetalheActivity.this, PedidoMobileEditarActivity.class);
            _intent.putExtras(_extras);

            startActivityForResult(_intent, _PEDIDO_REQUEST_CODE);
            // endregion
        }
    }
    // endregion


    // region confirmPedido
    private void confirmPedido(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = ((tpPedidoMobile) _adpPedidoMobile.getItem(itemIndex));
        // endregion

        // region Verificano se o mesmo já está confirmado
        if (_tp.EhConfirmado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    ClienteDetalheActivity.this,
                    "ATENÇÃO",
                    "Este pedido já está confirmado"
            );

            return;
        }
        // endregion

        // region Realizando a confirmação do pedido
        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(ClienteDetalheActivity.this);

            dbPedidoMobile _db = new dbPedidoMobile(_sqh);
            _db.setConfirmedOrder(_tp.IdPedidoMobile);

            _sqh.close();

            //((tpPedidoMobile)_adpPedidoMobile.getItem(itemIndex)).EhConfirmado = 1;
            _tp.EhConfirmado = 1;
            _adpPedidoMobile.notifyDataSetChanged();

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "ERRO",
                    e.getMessage()
            );

        } finally {

            // region Finalizando a conexão com o banco
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
            // endregion
        }
        // endregion
    }
    // endregion


    // region sincronizarPedido
    private void sincronizarPedido(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = _lstPedidoMobile.get(itemIndex);
        // endregion

        // region Verificano se o mesmo já está sincronizado
        if (_tp.EhSincronizado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    ClienteDetalheActivity.this,
                    "ATENÇÃO",
                    "Este pedido já está sincronizado com a base de pedidos da empresa.\nSincronização negada"
            );

            return;
        }
        // endregion

        // region Verificano se o mesmo já está confirmado
        if (_tp.EhConfirmado == 0) {

            MSVMsgBox.showMsgBoxWarning(
                    ClienteDetalheActivity.this,
                    "ATENÇÃO",
                    "Não é permitido sincronizar um pedido que não está confirmado.\nPor favor edite o pedido e confirme o mesmo"
            );

            return;
        }
        // endregion

        // region verificando se existe conexão no aparelho
        if (MSVUtil.isConnected(ClienteDetalheActivity.this) == false) {

            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "WI-FI | 3G",
                    "O seu dispostivo não está conectado a nenhuma rede wi-fi ou rede 3G"
            );

            return;
        }
        // endregion

        // region Selecionando os pedidos que serão sincronizados
        _lstPedidoMobileSync = new ArrayList<Long>();
        _lstPedidoMobileSync.add(_tp.IdPedidoMobile);
        // endregion

        // region Sincronizando os pedidos selecionados
        MSVMsgBox.showMsgBoxQuestion(
                ClienteDetalheActivity.this,
                "SINCRONIZAR PEDIDO",
                "Deseja realmente enviar este pedido para faturamento ?",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {

                            // region Mostrando a caixa de dialogo de espera
                            _wait = ProgressDialog.show(ClienteDetalheActivity.this, "Aguarde", "Enviando o pedido selecionado...");
                            // endregion

                            // region Invocando o método da API para envio dos pedidos
                            new PedidoApi(
                                    ClienteDetalheActivity.this,
                                    _tpServidorRestIp.ValorTexto,
                                    _tpVendedorCodigo.ValorTexto,
                                    _lstPedidoMobileSync,
                                    0,
                                    onTaskCompleteListner
                            ).execute();
                            // endregion
                        }
                    }
                }
        );
        // endregion
    }
    // endregion


    // region emailEnviar
    private void emailEnviar(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = _lstPedidoMobile.get(itemIndex);
        // endregion

        // region Montando os parâmetros de destino da activity
        Bundle _extras = new Bundle();
        _extras.putLong(_KEY_ID_PEDIDO_MOBILE, _tp.IdPedidoMobile);
        // endregion

        // region Invocando a activity de envio de email
        Intent _intent = new Intent(ClienteDetalheActivity.this, PedidoEmailEnviar.class);
        _intent.putExtras(_extras);
        startActivity(_intent);
        // endregion
    }
    // endregion


    // region whatsappEnviar
    private void whatsappEnviar(int itemIndex) {

        // region Declarando as variáveis locias do método
        SQLiteHelper _sqh = null;
        dbEmpresa _dbEmpresa = null;
        dbTipoPedido _dbTipoPedido = null;
        dbCliente _dbCliente = null;
        dbCidade _dbCidade = null;
        dbTabelaPreco _dbTabelaPreco = null;
        dbCondicaoPagamento _dbCondicaoPagamento = null;
        dbTransportadora _dbTransportadora = null;
        dbVendedor _dbVendedor = null;
        dbPedidoMobileItem _dbPedidoMobileItem = null;
        dbProduto _dbProduto = null;
        SQLClauseHelper _schPedido = null;
        File _htmlFile = null;
        String _htmlName = null;
        String _directory = null;
        // endregion

        // region Tratando as informações do produto
        try {

            // region Buscando as informações do pedido selecionado
            tpPedidoMobile _tp = _lstPedidoMobile.get(itemIndex);
            // endregion

            // region Verificando a necessidade de buscar as informações vinculadas do pedido
            if (_tp != null) {

                // region Abrindo a conexão com o banco de dados
                _sqh = new SQLiteHelper(ClienteDetalheActivity.this);
                _sqh.open(false);
                // endregion

                // region Buscando as informações para a empresa
                if (_tp.Empresa == null) {
                    _dbEmpresa = new dbEmpresa(_sqh);
                    _tp.Empresa = (tpEmpresa) _dbEmpresa.getBySourceId(_tp.IdEmpresa);
                }
                // endregion

                // region Buscando as informações para o tipo de pedido
                if (_tp.TipoPedido == null) {
                    _dbTipoPedido = new dbTipoPedido(_sqh);
                    _tp.TipoPedido = (tpTipoPedido) _dbTipoPedido.getBySourceId(_tp.IdTipoPedido);
                }
                // endregion

                // region Buscando as informações para o cliente
                if (_tp.Cliente == null) {
                    _dbCliente = new dbCliente(_sqh);
                    _tp.Cliente = (tpCliente) _dbCliente.getBySourceId(_tp.IdCliente);
                }
                // endregion

                // region Buscando as informações para a cidade do cliente
                if (_tp.Cliente.Cidade == null) {
                    _dbCidade = new dbCidade(_sqh);
                    _tp.Cliente.Cidade = (tpCidade) _dbCidade.getBySourceId(_tp.Cliente.IdCidade);
                }
                // endregion

                // region Buscando as informações para a tabela de preço
                if (_tp.TabelaPreco == null) {
                    _dbTabelaPreco = new dbTabelaPreco(_sqh);
                    _tp.TabelaPreco = (tpTabelaPreco) _dbTabelaPreco.getBySourceId(_tp.IdTabelaPreco);
                }
                // endregion

                // region Buscando as informações para a condição de pagamento
                if (_tp.CondicaoPagamento == null) {
                    _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);
                    _tp.CondicaoPagamento = (tpCondicaoPagamento) _dbCondicaoPagamento.getBySourceId(_tp.IdCondicaoPagamento);
                }
                // endregion

                // region Buscando as informações para a transportadora
                if (_tp.Transportadora == null) {
                    _dbTransportadora = new dbTransportadora(_sqh);
                    _tp.Transportadora = (tpTransportadora) _dbTransportadora.getBySourceId(_tp.IdTransportadora);
                }
                // endregion

                // region Buscando as informações para o vendedor
                if (_tp.Vendedor == null) {
                    _dbVendedor = new dbVendedor(_sqh);
                    _tp.Vendedor = (tpVendedor) _dbVendedor.getBySourceId(_tp.IdVendedor);
                }
                // endregion

                // region Buscando as informações para os itens do pedido

                // region Buscando a lista de itens
                if (_tp.Itens == null) {
                    _schPedido = new SQLClauseHelper();
                    _schPedido.addEqualInteger("IdPedidoMobile", _tp.IdPedidoMobile);

                    _dbPedidoMobileItem = new dbPedidoMobileItem(_sqh);
                    _tp.Itens = (ArrayList<tpPedidoMobileItem>) _dbPedidoMobileItem.getList(tpPedidoMobileItem.class, _schPedido);
                }
                // endregion

                // region Buscando os produtos dos itens do pedido
                if (_tp.Itens != null) {

                    if (_dbProduto == null) {
                        _dbProduto = new dbProduto(_sqh);
                    }

                    for (tpPedidoMobileItem _item : _tp.Itens) {
                        _item.Produto = (tpProduto) _dbProduto.getBySourceId(_item.IdProduto);
                    }
                }
                // endregion
                // endregion
            }
            // endregion

            // region Salvando o anexo que será enviado no próprio dispositivo
            dbPedidoMobile _db = new dbPedidoMobile(_sqh);
            _htmlName = _db.saveToHtml(_tp._id);
            // endregion

            // region Buscando o anexo em forma de arquivo
            _directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/msvsmart/html";
            _htmlFile = new File(_directory, _htmlName);

            if ((_htmlFile != null) && (_htmlFile.exists() == false)) {
                _htmlFile = null;
            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "Erro ao gerar o conteúdo do pedido para envio ao cliente",
                    e.getMessage()
            );
        }
        // endregion

        // region Realizando o envido do pedido pelo whatsapp
        if (MSVUtil.isPackageManeger(ClienteDetalheActivity.this, MSVUtil.WHATSAPP_INTENT)) {

            if (MSVUtil.isConnected(ClienteDetalheActivity.this)) {

                Intent _i = new Intent(Intent.ACTION_SEND);
                _i.setType("message/txt");
                _i.setPackage(MSVUtil.WHATSAPP_INTENT);

                if (_htmlFile != null) {
                    _i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(_htmlFile));
                }

                try {
                    startActivity(_i);
                } catch (Exception e) {
                    Toast.makeText(
                            ClienteDetalheActivity.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }

            } else {

                MSVMsgBox.showMsgBoxError(
                        ClienteDetalheActivity.this,
                        "WIFI | 3G",
                        "O seu dispositivo está off-line no momento, e por isso não será possível enviar o pedido via whatsapp"
                );
            }

        } else {

            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "Aplicativo Whatsapp não instalado neste dispositivo",
                    "Acesso sua loja de aplicativos e baixe o aplicativo Whatsapp para compartilhar o pedido com o seu cliente"
            );
        }
        // endregion
    }
    // endregion


    // region deletePedido
    private void deletePedido(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = ((tpPedidoMobile) _adpPedidoMobile.getItem(itemIndex));
        // endregion

        // region Verificano se o mesmo já está sincronizado
        if (_tp.EhSincronizado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    ClienteDetalheActivity.this,
                    "ATENÇÃO",
                    "Não é permitido exculir um pedido que já foi sincronizado com a base de pedidos da empresa"
            );

            return;
        }
        // endregion

        // region Processo de exclusão do pedido

        // region Instânciando variáveis locais
        SQLiteHelper _sqh = null;
        dbPedidoMobile _dbPedidoMobile = null;
        dbPedidoMobileItem _dbPedidoMobileItem = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Montando WHERE para o campo IdPedidoMobile
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdPedidoMobile", _tp.IdPedidoMobile);
            // endregion

            // region Instânciando e abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(ClienteDetalheActivity.this);
            _sqh.open(true);
            // endregion

            // region Instânciando os objetos db para o pedido e seus itens
            _dbPedidoMobile = new dbPedidoMobile(_sqh);
            _dbPedidoMobileItem = new dbPedidoMobileItem(_sqh);
            // endregion

            // region Recuperando os itens do pedido
            ArrayList<tpPedidoMobileItem> _lstItens = (ArrayList<tpPedidoMobileItem>) _dbPedidoMobileItem.getList(tpPedidoMobileItem.class, _sch);
            // endregion

            // region Iniciando a transação com o banco de dados
            //_sqh.initTransaction();
            // endregion

            // region Excluindo os itens do pedido
            if (_lstItens != null && _lstItens.size() > 0) {

                for (tpPedidoMobileItem _item : _lstItens) {
                    _dbPedidoMobileItem.delete(_item);
                }
            }
            // endregion

            // region Excluindo o pedido
            _dbPedidoMobile.delete(_tp);
            // endregion

            // region Encerrando a transação
            //_sqh.commitTransaction();
            _sqh.close();
            // endregion

            // region Atualizando a lista de pedidos
            this.loadPedido();
            this.showPedido();
            // endregion

            // region Emitindo mensagem ao usuário
            Toast.makeText(
                    ClienteDetalheActivity.this,
                    "Exclusão realizada com sucesso !!!",
                    Toast.LENGTH_SHORT
            ).show();
            // endregion

        } catch (Exception e) {

            // region Encerrando a tranzação com o banco de dados
            if (_sqh != null && _sqh.isOpen()) {
                //_sqh.endTransaction();
            }
            // endregion

            // region Emitindo mensagem de erro ao usuário
            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "ERRO",
                    e.getMessage()
            );
            // endregion

        } finally {

            // region Encerrando a conexão com o banco de dados
            if (_sqh != null && _sqh.isOpen()) {
                _sqh.close();
            }
            // endregion
        }
        // endregion
        // endregion
    }
    // endregion


    // region showPedido
    private void showPedido() {

        // region Limpando o conteúdo do ListView
        _ped_livPedidoLista.setAdapter(null);
        // endregion

        // region Limpando os valores dos totalizadores
        _ped_txtRegistro.setText("REGISTROS: 0");
        _ped_txtTotal.setText("TOTAL " + MSVUtil.doubleToText("R$:", 0));
        // endregion

        // region Verificando a necessidade de realizar o bind de informações
        if ((_lstPedidoMobile != null) && (_lstPedidoMobile.size() > 0)) {

            // region Vinculando a lista ao adaptador de informações
            _adpPedidoMobile = new PedidoMobileListaAdapter(
                    ClienteDetalheActivity.this,
                    _lstPedidoMobile
            );

            _ped_livPedidoLista.setAdapter(_adpPedidoMobile);
            // endregion

            // region Calculando o valor total dos pedidos
            double _total = 0;

            for (tpPedidoMobile _tp : _lstPedidoMobile) {
                _total += _tp.TotalValorLiquido;
            }
            // endregion

            // region Atualizando as informações da tela
            _ped_txtRegistro.setText("REGISTROS: " + String.valueOf(_lstPedidoMobile.size()));
            _ped_txtTotal.setText("TOTAL " + MSVUtil.doubleToText("R$:", _total));
            // endregion
        }
        // endregion
    }
    // endregion


    // region loadFinanceiro
    public void loadFinanceiro() {

        SQLiteHelper _sqh = null;
        long _p1 = 0;
        long _p2 = 0;
        String _p3 = null;

        try {

            // region Preenchendo os parâmetros
            _p1 = _tpEmpresa.IdEmpresa;
            _p2 = _IdCliente;
            _p3 = MSVUtil.sqliteHoje();
            // endregion

            // region Instânciando objeto de conexão com o banco
            _sqh = new SQLiteHelper(ClienteDetalheActivity.this);
            _sqh.open(false);
            // endregion

            // region Instânciando objeto de acesso a dados do cliente
            dbCliente _dbCliente = new dbCliente(_sqh);
            // endregion

            // region Lendo a lista de parcelas em aberto
            _lstFinanceiro = (ArrayList<tpFinanceiro>) _dbCliente.parcelas(_p1, _p2);
            // endregion

            // region Atualizando os totais e contadores
            _parcelasTotal = 0;
            _parcelasTotalVencido = 0;
            _parcelasQuantidadeVencida = 0;

            if (_lstFinanceiro != null && _lstFinanceiro.size() > 0) {

                _parcelasTotal = _dbCliente.parcelasTotal(_p1, _p2);
                _parcelasTotalVencido = _dbCliente.parcelasTotalVencido(_p1, _p2, _p3);
                _parcelasQuantidadeVencida = _dbCliente.parcelasQuantidadeVencida(_p1, _p2, _p3);

            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "[ClienteDetalheActivity|loadFinanceiro] - Erro ao selecionar os dados financeiros do cliente",
                    e.getMessage()
            );

        } finally {

            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }
        // endregion
    }
    // endregion


    // region showFinanceiro
    private void showFinanceiro() {

        _fin_txtTotalDevidoGeral.setText("R$ 0,00");
        _fin_txtTotalDevidoVencido.setText("R$ 0,00");
        _fin_txtRegistro.setText("REGISTROS: 0");

        if ((_lstFinanceiro != null) && (_lstFinanceiro.size() > 0)) {

            ClienteFinanceiroParcelaAdapter _adp = new ClienteFinanceiroParcelaAdapter(
                    ClienteDetalheActivity.this,
                    _lstFinanceiro
            );

            _fin_livParcelaLista.setAdapter(_adp);
            _fin_txtTotalDevidoGeral.setText(MSVUtil.doubleToText("R$", _parcelasTotal));
            _fin_txtTotalDevidoVencido.setText(MSVUtil.doubleToText("R$", _parcelasTotalVencido));
            _fin_txtRegistro.setText("REGISTROS: " + String.valueOf(_lstFinanceiro.size()));

            if (_parcelasTotalVencido > 0) {

                MSVMsgBox.showMsgBoxInfo(
                        ClienteDetalheActivity.this,
                        "SITUAÇÃO FINANCEIRA",
                        "Existem " + _parcelasQuantidadeVencida + " parcelas já vencidas que somam o total de R$" + MSVUtil.doubleToText("R$", _parcelasTotalVencido));
            }
        }
    }
    // endregion


    // region loadItemOptions
    private void loadItemOptions() {

        int _i = 0;

        // region Cuidando da instância do objeto lista
        if (_lstItemOptions == null) {
            _lstItemOptions = new ArrayList<tpKeyValueRow>();
        }
        // endregion

        // region Criando a opção de EDITAR
        tpKeyValueRow _tpEditar = new tpKeyValueRow();
        _tpEditar.Key = _i;
        _tpEditar.Value = "EDITAR";
        _tpEditar.ImageResource = R.drawable.msv_small_edit_black;

        _lstItemOptions.add(_tpEditar);
        _i += 1;
        // endregion

        // region Criando a opção de CONFIRMAR PEDIDO
        tpKeyValueRow _tpConfirmar = new tpKeyValueRow();
        _tpConfirmar.Key = _i;
        _tpConfirmar.Value = "CONFIRMAR";
        _tpConfirmar.ImageResource = R.drawable.msv_small_checked_black;

        _lstItemOptions.add(_tpConfirmar);
        _i += 1;
        // endregion

        // region Criando a opção de SINCRONIZACAO
        tpKeyValueRow _tpSincronizar = new tpKeyValueRow();
        _tpSincronizar.Key = _i;
        _tpSincronizar.Value = "SINCRONIZAR";
        _tpSincronizar.ImageResource = R.drawable.msv_small_synchronize_black;

        _lstItemOptions.add(_tpSincronizar);
        _i += 1;
        // endregion

        // region Criando a opção de ENVIAR POR EMAIL
        tpKeyValueRow _tpEmailEnviar = new tpKeyValueRow();
        _tpEmailEnviar.Key = _i;
        _tpEmailEnviar.Value = "ENVIAR POR E-MAIL";
        _tpEmailEnviar.ImageResource = R.drawable.msv_small_email_black;

        _lstItemOptions.add(_tpEmailEnviar);
        _i += 1;
        // endregion

        // region Criando a opção de ENVIAR POR WHATSAPP
        tpKeyValueRow _tpWhatsappEnviar = new tpKeyValueRow();
        _tpWhatsappEnviar.Key = _i;
        _tpWhatsappEnviar.Value = "ENVIAR POR WHATSAPP";
        _tpWhatsappEnviar.ImageResource = R.drawable.msv_small_whatsapp_black;

        _lstItemOptions.add(_tpWhatsappEnviar);
        _i += 1;
        // endregion

        // region Criando a opção de EXCLUIR
        tpKeyValueRow _tpExcluir = new tpKeyValueRow();
        _tpExcluir.Key = _i;
        _tpExcluir.Value = "EXCLUIR";
        _tpExcluir.ImageResource = R.drawable.msv_small_trash_black;

        _lstItemOptions.add(_tpExcluir);
        _i += 1;
        // endregion
    }
    // endregion


    // region loadSincronizacao
    private void loadSincronizacao() {

        SQLiteHelper _sqh = null;
        dbSincronizacao _dbSincronizacao = null;

        try {

            _sqh = new SQLiteHelper(ClienteDetalheActivity.this);
            _sqh.open(false);

            _dbSincronizacao = new dbSincronizacao(_sqh);
            _isSyncToday = _dbSincronizacao.isSyncToday();

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "Erro ao realizar a leitura da última sincronização"
            );

            finish();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }
    }
    // endregion


    // region addContact
    private void addContact() {

        // region Validando as informações antes de invocar a activity
        if (_tpCliente != null) {

            int _Count = 0;

            if (MSVUtil.isNullOrEmpty(_tpCliente.ContatoNome) == false) {
                _Count += 5;
            }

            if (MSVUtil.isNullOrEmpty(_tpCliente.TelefoneFixo) == false) {
                _Count += 1;
            }

            if (MSVUtil.isNullOrEmpty(_tpCliente.TelefoneCelular) == false) {
                _Count += 1;
            }

            if (_Count < 6) {

                MSVMsgBox.showMsgBoxWarning(
                        ClienteDetalheActivity.this,
                        "Nome do contato não informado",
                        "Para adicionar este cliente como contato na agenda do dispositivo é necessário editar o mesmo e informar o nome de contato e um número de telefone"
                );

                return;

            } else {

                // region Criando a itente para registro do novo contato

                // aqui instânciamos um nova intent
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);

                // setamos o tipo da intent
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                // agora passamos as informaçoes adicionais para a intent
                intent.putExtra(ContactsContract.Intents.Insert.NAME, _tpCliente.ContatoNome);
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, _tpCliente.RazaoSocial);

                intent.putExtra(ContactsContract.Intents.Insert.PHONE, _tpCliente.TelefoneCelular);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE_ISPRIMARY, true);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, _tpCliente.TelefoneFixo);
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);

                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, _tpCliente.Email);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL_ISPRIMARY, true);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);

                intent.putExtra(ContactsContract.Intents.Insert.NOTES, "Adicionado por MSVFV em: " + MSVUtil.hojeToText());

                intent.putExtra("finishActivityOnSaveCompleted", true);

                // endregion

                // region Invocando a activity
                startActivityForResult(intent, _CONTACT_ADD_REQUEST_CODE);
                // endregion
            }
        }
        // endregion
    }
    // endregion


    // region onTaskCompleteListner
    private OnTaskCompleteListner onTaskCompleteListner = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

            // region Acoes que serão executadas quando o status for Error
            if (status == eTaskCompleteStatus.ERROR) {

                _wait.dismiss();

                Toast.makeText(
                        ClienteDetalheActivity.this,
                        "Erro ao realizar a sincronização de pedido",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region Acoes que serão executadas quando o status for refresh
            if (status == eTaskCompleteStatus.REFRESH) {
            }
            // endregion

            // region Acoes que serão executadas quando o status for sucesso
            if (status == eTaskCompleteStatus.SUCCESS) {

                _wait.dismiss();

                if (out == 1) {

                    Toast.makeText(
                            ClienteDetalheActivity.this,
                            "Pedido sincronizado com sucesso mas não confirmado no dispositivo !!!",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                if (out == 2) {

                    Toast.makeText(
                            ClienteDetalheActivity.this,
                            "Pedido sincronizado com sucesso !!!",
                            Toast.LENGTH_SHORT
                    ).show();

                    loadCliente();
                    loadPedido();
                    showPedido();
                }

                if (out == 3) {

                    Toast.makeText(
                            ClienteDetalheActivity.this,
                            "Pedido já sincronizado com a empresa !!!",
                            Toast.LENGTH_SHORT
                    ).show();

                    loadPedido();
                    showPedido();
                }
            }
            // endregion
        }
    };
    // endregion


    // region sendMail
    private void sendMail(String to, String subject, String content) {

        Intent _i = new Intent(Intent.ACTION_VIEW);
        Uri dadosEmail = Uri.parse("mailto:" + to + "?subject=" + subject + "&body=" + content);

        _i.setData(dadosEmail);

        try {
            startActivity(_i);
        } catch (Exception e) {
            Toast.makeText(
                    ClienteDetalheActivity.this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
    // endregion


    // region showAgendaContent
    private void showAgendaContent() {

        // region Inicializando variáveis
        _tpContactFixo = null;
        _tpContactCelular = null;
        // endregion

        // region Escondendo as imagens referente ao painel de contato
        _fca_imgAgendaOk.setVisibility(View.GONE);
        _fca_imgAgendaAdd.setVisibility(View.GONE);
        // endregion

        // region Se o cliente está selecionado então...
        try {
            if (_tpCliente != null) {

                // region Verificando se existe contato com o telefone fixo registrado
                if (MSVUtil.isNullOrEmpty(_tpCliente.TelefoneFixo) == false) {
                    _tpContactFixo = MSVUtil.contactExist(ClienteDetalheActivity.this, _tpCliente.TelefoneFixo);
                }
                // endregion

                // region Verificando se existe contato com o telefone celular registrado
                if (MSVUtil.isNullOrEmpty(_tpCliente.TelefoneCelular) == false) {
                    _tpContactCelular = MSVUtil.contactExist(ClienteDetalheActivity.this, _tpCliente.TelefoneCelular);
                }
                // endregion

                // region Aqui mostramos a imagem e o texto de acordo se existe contato ou não
                if ((_tpContactFixo != null) || (_tpContactCelular != null)) {
                    _fca_imgAgendaOk.setVisibility(View.VISIBLE);
                    _fca_txtAgenda.setText("CONTATO JÁ ADICIONADO NA AGENDA");
                } else {
                    _fca_imgAgendaAdd.setVisibility(View.VISIBLE);
                    _fca_txtAgenda.setText("ADICIONAR CONTATO NA AGENDA");
                }
                // endregion
            }
        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    ClienteDetalheActivity.this,
                    "Ocorreram erros no processo de localização do cliente",
                    e.getMessage()
            );
        }
        // endregion
    }
    // endregion
}
