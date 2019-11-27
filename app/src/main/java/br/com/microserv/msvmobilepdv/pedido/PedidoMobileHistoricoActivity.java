package br.com.microserv.msvmobilepdv.pedido;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbPedidoMobileHistorico;
import br.com.microserv.framework.msvdal.dbPedidoMobileItemHistorico;
import br.com.microserv.framework.msvdal.dbTabelaPreco;
import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpPedidoMobileHistorico;
import br.com.microserv.framework.msvdto.tpPedidoMobileItemHistorico;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.PedidoMobileProdutoHistoricoAdapter;

public class PedidoMobileHistoricoActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_ID_CLIENTE = "IdCliente";
    static final String _KEY_ID_PEDIDO_MOBILE_HISTORICO = "IdPedidoMobileHistorico";

    // endregion


    // region Declarando os objetos de interface

    // region INCLUDE

    // View
    View _inClienteCabecalho = null;
    View _inPedido = null;
    View _inItens = null;
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
    // endregion


    // region PEDIDO

    // LinearLayout
    LinearLayout _ped_pnlNumeroDataEmissaoCnt = null;
    LinearLayout _ped_pnlNumero = null;
    LinearLayout _ped_pnlEmissaoData = null;
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
    LinearLayout _ped_pnlItensQuantidadeCnt = null;
    LinearLayout _ped_pnlItensQuantidade = null;
    LinearLayout _ped_pnlItensValorTotalCnt = null;
    LinearLayout _ped_pnlItensValorTotal = null;
    LinearLayout _ped_pnlDescontoPercentualCnt = null;
    LinearLayout _ped_pnlDescontoPercentual = null;
    LinearLayout _ped_pnlDescontoValorCnt = null;
    LinearLayout _ped_pnlDescontoValor = null;
    LinearLayout _ped_pnlItensValorTotalLiquidoCnt = null;
    LinearLayout _ped_pnlItensValorTotalLiquido = null;

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
    TextView _ped_txtItensQuantidade = null;
    TextView _ped_txtItensValorTotal = null;
    TextView _ped_txtDescontoPercentual = null;
    TextView _ped_txtDescontoValor = null;
    TextView _ped_txtItensValorTotalLiquido = null;

    // endregion


    // region ITENS

    // ListView
    ListView _ite_livItens = null;

    // TextView
    TextView _ite_txtRegistro = null;
    TextView _ite_txtValorTotal = null;

    // endregion

    // endregion


    // region Declarando variáveis locias

    // objetos
    Bundle _extras = null;

    // adapters
    PedidoMobileProdutoHistoricoAdapter _adpItens = null;

    // tp
    tpEmpresa _tpEmpresa = null;
    tpPedidoMobileHistorico _tpPedidoMobileHistorico = null;
    tpCliente _tpCliente = null;
    tpVendedor _tpVendedor = null;

    // Long
    long _idCliente = 0;
    long _idPedidoMobileHistorico = 0;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Invocando o método construtor ancestral
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_mobile_historico);
        // endregion

        // region Aplicando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);
        // endregion

        // region Recuperando parametros extras
        _extras = getIntent().getExtras();

        if (_extras != null) {

            if (_extras.containsKey(_KEY_TP_EMPRESA)){
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);
            } else {
                Toast.makeText(
                        PedidoMobileHistoricoActivity.this,
                        "O parâmetro _KEY_ID_CLIENTE não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_ID_CLIENTE)){
                _idCliente = _extras.getLong(_KEY_ID_CLIENTE);
            } else {
                Toast.makeText(
                        PedidoMobileHistoricoActivity.this,
                        "O parâmetro _KEY_ID_CLIENTE não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }

            if (_extras.containsKey(_KEY_ID_PEDIDO_MOBILE_HISTORICO)){
                _idPedidoMobileHistorico = _extras.getLong(_KEY_ID_PEDIDO_MOBILE_HISTORICO);
            } else {
                Toast.makeText(
                        PedidoMobileHistoricoActivity.this,
                        "O parâmetro _KEY_ID_PEDIDO_MOBILE_HISTORICO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
        // endregion

        // region Invocando os métodos da interface
        bindElements();
        bindEvents();
        // endregion

        // region Incovando o método de inicializacao da activity
        initialize();
        // endregion

    }
    // endregion


    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pedido_historico, menu);
        return true;

    }
    // endregion


    // region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.mnPedido:
                _inPedido.setVisibility(View.VISIBLE);
                _inItens.setVisibility(View.GONE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_300);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_400);
                break;

            case R.id.mnItens:
                this.refreshItens();

                _inPedido.setVisibility(View.GONE);
                _inItens.setVisibility(View.VISIBLE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_400);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_300);
                break;

        }

        return super.onOptionsItemSelected(item);

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
        _inPedido = (View) findViewById(R.id.inPedido);
        _inItens = (View) findViewById(R.id.inItens);
        // endregion


        // region NAVEGADOR
        _pnlPrincipalMnt = (LinearLayout) findViewById(R.id.pnlPrincipalMnt);
        _pnlItensMnt = (LinearLayout) findViewById(R.id.pnlItensMnt);
        // endregion


        // region PEDIDO

        // LinearLayout
        _ped_pnlNumeroDataEmissaoCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlNumeroEmissaoDataCnt);
        _ped_pnlNumero = (LinearLayout) _inPedido.findViewById(R.id.pnlNumero);
        _ped_pnlEmissaoData = (LinearLayout) _inPedido.findViewById(R.id.pnlEmissaoData);
        _ped_pnlEmpresaCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlEmpresaCnt);
        _ped_pnlEmpresa = (LinearLayout) _inPedido.findViewById(R.id.pnlEmpresa);
        _ped_pnlTipoPedidoCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlTipoPedidoCnt);
        _ped_pnlTipoPedido = (LinearLayout) _inPedido.findViewById(R.id.pnlTipoPedido);
        _ped_pnlTabelaPrecoCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlTabelaPrecoCnt);
        _ped_pnlTabelaPreco = (LinearLayout) _inPedido.findViewById(R.id.pnlTabelaPreco);
        _ped_pnlCondicaoPagamentoCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlCondicaoPagamentoCnt);
        _ped_pnlCondicaoPagamento = (LinearLayout) _inPedido.findViewById(R.id.pnlCondicaoPagamento);
        _ped_pnlTransportadoraCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlTransportadoraCnt);
        _ped_pnlTransportadora = (LinearLayout) _inPedido.findViewById(R.id.pnlTransportadora);
        _ped_pnlNumeroClienteCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlNumeroClienteCnt);
        _ped_pnlNumeroCliente = (LinearLayout) _inPedido.findViewById(R.id.pnlNumeroCliente);
        _ped_pnlObservacaoCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlObservacaoCnt);
        _ped_pnlObservacao = (LinearLayout) _inPedido.findViewById(R.id.pnlObservacao);
        _ped_pnlItensQuantidadeCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlItensQuantidadeCnt);
        _ped_pnlItensQuantidade = (LinearLayout) _inPedido.findViewById(R.id.pnlItensQuantidade);
        _ped_pnlItensValorTotalCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlItensValorTotalCnt);
        _ped_pnlItensValorTotal = (LinearLayout) _inPedido.findViewById(R.id.pnlItensValorTotal);
        _ped_pnlDescontoPercentualCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlDescontoPercentualCnt);
        _ped_pnlDescontoPercentual = (LinearLayout) _inPedido.findViewById(R.id.pnlDescontoPercentual);
        _ped_pnlDescontoValorCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlDescontoValorCnt);
        _ped_pnlDescontoValor = (LinearLayout) _inPedido.findViewById(R.id.pnlDescontoValor);
        _ped_pnlItensValorTotalLiquidoCnt = (LinearLayout) _inPedido.findViewById(R.id.pnlItensValorTotalLiquidoCnt);
        _ped_pnlItensValorTotalLiquido = (LinearLayout) _inPedido.findViewById(R.id.pnlItensValorTotalLiquido);

        // TextView
        _ped_txtNumero = (TextView) _inPedido.findViewById(R.id.txtNumero);
        _ped_txtEmissaoDataHora = (TextView) _inPedido.findViewById(R.id.txtEmissaoDataHora);
        _ped_txtEmpresaDescricao = (TextView) _inPedido.findViewById(R.id.txtEmpresaDescricao);
        _ped_txtTipoPedidoDescricao = (TextView) _inPedido.findViewById(R.id.txtTipoPedidoDescricao);
        _ped_txtTabelaPrecoDescricao = (TextView) _inPedido.findViewById(R.id.txtTabelaPrecoDescricao);
        _ped_txtCondicaoPagamentoDescricao = (TextView) _inPedido.findViewById(R.id.txtCondicaoPagamentoDescricao);
        _ped_txtTransportadoraDescricao = (TextView) _inPedido.findViewById(R.id.txtTransportadoraDescricao);
        _ped_txtNumeroCliente = (TextView) _inPedido.findViewById(R.id.txtNumeroCliente);
        _ped_txtObservacao = (TextView) _inPedido.findViewById(R.id.txtObservacao);
        _ped_txtItensQuantidade = (TextView) _inPedido.findViewById(R.id.txtItensQuantidade);
        _ped_txtItensValorTotal = (TextView) _inPedido.findViewById(R.id.txtItensValorTotal);
        _ped_txtDescontoPercentual = (TextView) _inPedido.findViewById(R.id.txtDescontoPercentual);
        _ped_txtDescontoValor = (TextView) _inPedido.findViewById(R.id.txtDescontoValor);
        _ped_txtItensValorTotalLiquido = (TextView) _inPedido.findViewById(R.id.txtItensValorTotalLiquido);

        // endregion


        // region ITENS

        // ListView
        _ite_livItens = (ListView) _inItens.findViewById(R.id.livItens);

        // TextView
        _ite_txtRegistro = (TextView) _inItens.findViewById(R.id.txtRegistro);
        _ite_txtValorTotal = (TextView) _inItens.findViewById(R.id.txtValorTotal);

        // endregion

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region NAVEGADOR

        // region Click em PEDIDO
        _pnlPrincipalMnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _inPedido.setVisibility(View.VISIBLE);
                _inItens.setVisibility(View.GONE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_300);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_500);

            }
        });
        // endregion

        // region Click em ITENS
        _pnlItensMnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refreshItens();

                _inPedido.setVisibility(View.GONE);
                _inItens.setVisibility(View.VISIBLE);

                _pnlPrincipalMnt.setBackgroundResource(R.color.indigo_500);
                _pnlItensMnt.setBackgroundResource(R.color.indigo_300);

            }
        });
        // endregion

        // endregion

    }
    // endregion


    // region Initialize
    public void initialize() {

        // region Trabalhando a apresentação dos includes
        _inPedido.setVisibility(View.VISIBLE);
        _inItens.setVisibility(View.GONE);
        // endregion


        // region Limpando o conteúdos dos elementos da activity
        this.clearActivityData();
        // endregion

        // region Buscando informações no banco de dadoss
        this.loadCliente();
        this.loadPedidoMobileHistorico();
        // endregion


        // region Atualizando os dados do activity
        this.refreshCabecalho();
        this.refreshPedido();
        this.refreshItens();
        // endregion

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
        _ped_txtItensQuantidade.setText("");
        _ped_txtItensValorTotal.setText("");
        _ped_txtDescontoPercentual.setText("");
        _ped_txtDescontoValor.setText("");
        _ped_txtItensValorTotalLiquido.setText("");
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


    // region refreshPedido
    private void refreshPedido() {

        double _itensValorTotal = 0;
        double _itensValorTotalLiquido = 0;

        for (tpPedidoMobileItemHistorico _tp : _tpPedidoMobileHistorico.Itens) {
            _itensValorTotal += _tp.UnidadeVendaQuantidade * (_tp.UnidadeValor - _tp.UnidadeDescontoValor);
        }

        _itensValorTotalLiquido = _itensValorTotal - _tpPedidoMobileHistorico.DescontoValor1;


        _ped_txtNumero.setText(_tpPedidoMobileHistorico.Numero);
        _ped_txtEmissaoDataHora.setText(_tpPedidoMobileHistorico.EmissaoDataHora);
        _ped_txtEmpresaDescricao.setText(_tpPedidoMobileHistorico.Empresa);
        _ped_txtTipoPedidoDescricao.setText(_tpPedidoMobileHistorico.TipoPedido);
        _ped_txtTabelaPrecoDescricao.setText(_tpPedidoMobileHistorico.TabelaPreco);
        _ped_txtCondicaoPagamentoDescricao.setText(_tpPedidoMobileHistorico.CondicaoPagamento);
        _ped_txtTransportadoraDescricao.setText(_tpPedidoMobileHistorico.Transportadora);
        _ped_txtNumeroCliente.setText(_tpPedidoMobileHistorico.NumeroCliente);
        _ped_txtObservacao.setText(_tpPedidoMobileHistorico.Observacao);
        _ped_txtItensQuantidade.setText(String.valueOf(_tpPedidoMobileHistorico.ItensQuantidade));
        _ped_txtItensValorTotal.setText(MSVUtil.doubleToText("R$", _itensValorTotal));
        _ped_txtDescontoPercentual.setText(MSVUtil.doubleToText(_tpPedidoMobileHistorico.DescontoPercentual1));
        _ped_txtDescontoValor.setText(MSVUtil.doubleToText(_tpPedidoMobileHistorico.DescontoValor1));
        _ped_txtItensValorTotalLiquido.setText(MSVUtil.doubleToText("R$", _itensValorTotalLiquido));

    }
    // endregion


    // region refreshItens
    private void refreshItens() {

        // region Formando o adaptador do itens
        if (_adpItens == null) {
            _adpItens = new PedidoMobileProdutoHistoricoAdapter(
                    PedidoMobileHistoricoActivity.this,
                    (ArrayList<tpPedidoMobileItemHistorico>) _tpPedidoMobileHistorico.Itens
            );
        } else {
            _adpItens.notifyDataSetChanged();
        }
        // endregion


        // region Ligando o adaptardor na lista de itens
        _ite_livItens.setAdapter(_adpItens);
        // endregion


        // region Atualizando campos calculados do pedido de acordo com os itens
        _tpPedidoMobileHistorico.ItensQuantidade = 0;
        _tpPedidoMobileHistorico.TotalValor = 0;
        _tpPedidoMobileHistorico.DescontoPercentual1 = 0;
        _tpPedidoMobileHistorico.DescontoValor1 = 0;
        _tpPedidoMobileHistorico.TotalValorLiquido = 0;

        for(tpPedidoMobileItemHistorico _tp : _tpPedidoMobileHistorico.Itens) {

            _tpPedidoMobileHistorico.ItensQuantidade += 1;
            _tpPedidoMobileHistorico.TotalValor += _tp.UnidadeValorTotal;
            _tpPedidoMobileHistorico.TotalValorLiquido += _tp.UnidadeValorTotal;

        }

        _ite_txtRegistro.setText("REGISTROS: " + String.valueOf(_tpPedidoMobileHistorico.ItensQuantidade));
        _ite_txtValorTotal.setText(MSVUtil.doubleToText("R$", _tpPedidoMobileHistorico.TotalValor));
        // endregion

    }
    //endregion


    // region loadVendedor
    private void loadVendedor() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(PedidoMobileHistoricoActivity.this);
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


    // region loadCliente
    private void loadCliente() {

        // region Declarando variáveis locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido
        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileHistoricoActivity.this);
            _sqh.open(false);
            // endregion

            // region Lendo as informações do cliente
            dbCliente _dbCliente = new dbCliente(_sqh);
            _tpCliente = (tpCliente) _dbCliente.getBySourceId(_idCliente);
            // endregion

            // region Lendo a tabela de preço de acordo com a empresa
            if (_tpCliente != null) {

                dbTabelaPreco _dbTabelaPreco = new dbTabelaPreco(_sqh);
                _tpCliente.TabelaPrecoEmpresa = _dbTabelaPreco.getTabelaDoCliente(_tpCliente.IdCliente, _tpEmpresa.IdEmpresa);

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


    // region loadPedidoMobileHistorico
    private void loadPedidoMobileHistorico() {

        // region Declarando variáveis locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido
        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoMobileHistoricoActivity.this);
            _sqh.open(false);
            // endregion


            // region Montado WHERE para o campo IdPedidoMobile
            SQLClauseHelper _schIdPedidoMobile = new SQLClauseHelper();
            _schIdPedidoMobile.addEqualInteger("IdPedidoMobileHistorico", _idPedidoMobileHistorico);
            _schIdPedidoMobile.addOrderBy("_id", eSQLSortType.ASC);
            // endregion


            // region Lendo as informações do pedido
            dbPedidoMobileHistorico _dbPedidoMobileHistorico = new dbPedidoMobileHistorico(_sqh);
            _tpPedidoMobileHistorico = (tpPedidoMobileHistorico) _dbPedidoMobileHistorico.getOne(_schIdPedidoMobile);
            // endregion

            // region Lendo os itens do pedido se necessário
            if (_tpPedidoMobileHistorico != null) {

                // region Lendo as informações dos itens do pedido
                dbPedidoMobileItemHistorico _dbPedidoMobileItemHistorico = new dbPedidoMobileItemHistorico(_sqh);
                _tpPedidoMobileHistorico.Itens = (ArrayList<tpPedidoMobileItemHistorico>) _dbPedidoMobileItemHistorico.getList(
                        tpPedidoMobileItemHistorico.class,
                        _schIdPedidoMobile
                );
                // endregion
            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileHistoricoActivity.this,
                    "Erro ao tentar ler as informações do pedido",
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

}
