package br.com.microserv.msvmobilepdv.pedido;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbClienteHistoricoCompra;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdal.dbTabelaPrecoProduto;
import br.com.microserv.framework.msvdto.tpClienteHistoricoCompra;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpKeyValueRow;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpProdutoSearch;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvdto.tpTabelaPrecoProduto;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.WhereItem;
import br.com.microserv.framework.msvutil.eSQLConditionType;
import br.com.microserv.framework.msvutil.eSQLiteDataType;
import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.msvmobilepdv.Exception.ExceptionHandler;
import br.com.microserv.msvmobilepdv.produto.ProdutoDetalheImagemActivity;
import br.com.microserv.msvmobilepdv.produto.ProdutoSearchActivity;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.HistoricoCompraDialogAdapter;

public class PedidoMobileItemEditarActivity
        extends AppCompatActivity
        implements ActivityInterface {

    // region Declarando constantes

    // RequestCode
    static final int _PRODUTO_LOOKUP_REQUEST_CODE = 100;

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_TP_TABELA_PRECO = "tpTabelaPreco";
    static final String _KEY_TP_PEDIDO_MOBILE_ITEM = "TpPedidoMobileItem";
    static final String _KEY_TP_PRODUTO = "tpProduto";
    static final String _KEY_ITEM_INDEX = "ItemIndex";
    static final String _KEY_LST_PRODUTO_INCLUIDO = "lstProdutoIncluido";
    static final String _KEY_ID_CLIENTE = "IdCliente";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final int _DETAIL_VALUE = 4;
    static final String _CLIENTE_PEDIDO_MOBILE_ITEM_VALUE = "PedidoMobileItemEditarActivity";

    // endregion

    //Parametros
    int _PEDIDO_VALOR_UNITARIO_LIBERADO = 0;
    int _DESCONTO_ITEM_EH_PERMITIDO = 0;
    //endregion

    // region Declarando os objetos de interface

    // LinearLayout
    LinearLayout _pnlProdutoCodigoCnt = null;
    LinearLayout _pnlProdutoCodigoObr = null;
    LinearLayout _pnlProdutoCodigo = null;
    LinearLayout _pnlProdutoDescricaoCnt = null;
    LinearLayout _pnlProdutoDescricaoObr = null;
    LinearLayout _pnlProdutoDescricao = null;
    LinearLayout _pnlEmbalagemQuantidadeCnt = null;
    LinearLayout _pnlEmbalagemQuantidade = null;
    LinearLayout _pnlValorUnitarioCnt = null;
    LinearLayout _pnlValorUnitarioObr = null;
    LinearLayout _pnlValorUnitario = null;
    LinearLayout _pnlDescontoPercentualCnt = null;
    LinearLayout _pnlDescontoPercentual = null;
    LinearLayout _pnlDescontoValorCnt = null;
    LinearLayout _pnlDescontoValor = null;
    LinearLayout _pnlVendaQuantidadeCnt = null;
    LinearLayout _pnlVendaQuantidadeObr = null;
    LinearLayout _pnlVendaQuantidade = null;
    LinearLayout _pnlValorUnitarioLiquidoCnt = null;
    LinearLayout _pnlValorUnitarioLiquido = null;
    LinearLayout _pnlValorUnitarioTotalCnt = null;
    LinearLayout _pnlValorUnitarioTotal = null;
    LinearLayout _pnlObservacaoCnt = null;
    LinearLayout _pnlObservacao = null;
    LinearLayout _pnlProdutoOpcaoCnt = null;
    LinearLayout _pnlProdutoHistoricoCnt = null;
    LinearLayout _pnlProdutoImagemCnt = null;


    // TextView
    TextView _txtTitulo = null;
    TextView _txtProdutoCodigo = null;
    TextView _txtProdutoDescricao = null;
    TextView _txtEmbalagemQuantidade = null;
    TextView _txtValorUnitario = null;
    TextView _txtDescontoPercentual = null;
    TextView _txtDescontoValor = null;
    TextView _txtVendaQuantidade = null;
    TextView _txtValorUnitarioLiquido = null;
    TextView _txtValorUnitarioTotal = null;
    TextView _txtObservacao = null;

    // endregion

    // ImageView

    ImageView _imgValorUnitario = null;

    //endregion


    // region Declarando variáveis locais

    // ObjectModel
    tpEmpresa _tpEmpresa = null;
    tpTabelaPreco _tpTabelaPreco = null;
    tpPedidoMobileItem _tpPedidoMobileItem = null;
    tpTabelaPrecoProduto _tpTabelaPrecoProduto = null;

    // ObjectList
    ArrayList<String> _lstProdutoIncluido = null;
    ArrayList<tpKeyValueRow> _lstProdutoOpcao = null;
    ArrayList<tpClienteHistoricoCompra> _lstHistoricoCompra = null;

    // String
    String _sourceActivity = null;
    String _produtoCodigo = null;

    // Integer
    int _metodoEdicao = 0;
    int _itemIndex = 0;

    // Long
    long _idProduto = 0;
    long _idCliente = 0;

    enum SearchProductField {
        ID,
        CODIGO
    }

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_mobile_item);
        // endregion

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, getIntent().getExtras()));

        // region Dando suporte a ActionBar
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
                        PedidoMobileItemEditarActivity.this,
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
                        PedidoMobileItemEditarActivity.this,
                        "O parâmetro _KEY_SOURCE_ACTIVITY não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_ID_CLIENTE
            if (_extras.containsKey(_KEY_ID_CLIENTE)) {
                _idCliente = _extras.getLong(_KEY_ID_CLIENTE);
            } else {
                Toast.makeText(
                        PedidoMobileItemEditarActivity.this,
                        "O parâmetro _KEY_ID_CLIENTE não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_TP_EMPRESA
            if (_extras.containsKey(_KEY_TP_EMPRESA)) {
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);
            } else {
                Toast.makeText(
                        PedidoMobileItemEditarActivity.this,
                        "O parâmetro _KEY_TP_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_TP_TABELA_PRECO
            if (_extras.containsKey(_KEY_TP_TABELA_PRECO)) {
                _tpTabelaPreco = (tpTabelaPreco) _extras.getSerializable(_KEY_TP_TABELA_PRECO);
            } else {
                Toast.makeText(
                        PedidoMobileItemEditarActivity.this,
                        "O parâmetro _KEY_TP_TABELA_PRECO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_TP_PEDIDO_MOBILE_ITEM
            if (_extras.containsKey(_KEY_TP_PEDIDO_MOBILE_ITEM)) {
                _tpPedidoMobileItem = (tpPedidoMobileItem) _extras.getSerializable(_KEY_TP_PEDIDO_MOBILE_ITEM);
            } else {
                Toast.makeText(
                        PedidoMobileItemEditarActivity.this,
                        "O parâmetro _KEY_TP_PEDIDO_MOBILE_ITEM não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_LST_PRODUTO_INCLUIDO

            if (_metodoEdicao == _INSERT_VALUE) {

                if (_extras.containsKey(_KEY_LST_PRODUTO_INCLUIDO)) {
                    _lstProdutoIncluido = (ArrayList<String>) _extras.getSerializable(_KEY_LST_PRODUTO_INCLUIDO);
                } else {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "O parâmetro _KEY_LST_PRODUTO_INCLUIDO não foi informado",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }

            // endregion

            // region _KEY_ITEM_INDEX
            if (_extras.containsKey(_KEY_ITEM_INDEX)) {
                _itemIndex = _extras.getInt(_KEY_ITEM_INDEX);
            } else {
                Toast.makeText(
                        PedidoMobileItemEditarActivity.this,
                        "O parâmetro KEY_ITEM_INDEX não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion


            // region _KEY_EXCEPTION
            if(_extras.containsKey(ExceptionHandler._KEY_EXCEPTION)){
                Toast.makeText(
                        this,
                        "Erro: " + _extras.getString(ExceptionHandler._KEY_EXCEPTION),
                        Toast.LENGTH_LONG
                ).show();
            }
            // endregion

        }
        // endregion


        // region inicializando a activity
        this.bindElements();
        this.bindEvents();

        this.initialize();

        this.setProperties();

        // endregion

    }
    // endregion


    // region Método setProperties
    private void setProperties() {

        // region Declarando variáveis do método
        SQLiteHelper _sqh = null;
        dbParametro _db = null;
        tpParametro _tpDescontoItemEhPermitido = null;
        tpParametro _tpValorItemEhLiberado = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Cuidando da conexão com o banco
            _sqh = new SQLiteHelper(PedidoMobileItemEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Instânciando os objetos de acesso ao banco
            _db = new dbParametro(_sqh);
            // endregion

            // region Realizando a leitura dos parametros necessários
            _tpDescontoItemEhPermitido = _db.getDescontoItemEhPermitido();
            _tpValorItemEhLiberado = _db.getValorItemEhLiberado();
            // endregion

            // region Fazendo uso dos parâmetros

            // region DescontoItemEhPermitido
            if (_tpDescontoItemEhPermitido != null) {

                if (_tpDescontoItemEhPermitido.ValorInteiro == 0) {
                    _DESCONTO_ITEM_EH_PERMITIDO = 0;
                    _pnlDescontoPercentualCnt.setVisibility(View.GONE);
                    _pnlDescontoValorCnt.setVisibility(View.GONE);
                } else {
                    _DESCONTO_ITEM_EH_PERMITIDO = 1;
                }

            }
            // endregion

            // region ValorItemEhLiberado
            if (_tpValorItemEhLiberado != null) {

                if (_tpValorItemEhLiberado.ValorInteiro == 0) {
                    _PEDIDO_VALOR_UNITARIO_LIBERADO = 0;
                    _imgValorUnitario.setVisibility(View.INVISIBLE);
                    _txtValorUnitario.setTextColor(Color.GRAY);
                } else {
                    _PEDIDO_VALOR_UNITARIO_LIBERADO = 1;
                }
            }
            // endregion

            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "Erro ao tentar realizar a leitura de parâmetros do sistema",
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

        getMenuInflater().inflate(R.menu.menu_pedido_item, menu);
        return true;

    }
    // endregion


    // region onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Executando uma ação de acordo com o menu selecionado
        switch (item.getItemId()) {

            // Voltar
            case android.R.id.home:
                this.cancel();
                break;

            case R.id.mnSalvar:
                this.save();
                break;

            case R.id.mnCancelar:
                this.cancel();
                break;

        }

        // Invocando o metodo da classe base
        return super.onOptionsItemSelected(item);

    }
    // endregion


    // region onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // region Invocando o metodo da classe base
        super.onActivityResult(requestCode, resultCode, data);
        // endregion


        // region Verificando qual foi a activity executada
        if (requestCode == _PRODUTO_LOOKUP_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                if (!data.hasExtra(_KEY_TP_PRODUTO)) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "Atenção, não foi retornado o valor do parâmetro KEY_TP_PRODUTO",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {

                    tpProdutoSearch _tp = (tpProdutoSearch) data.getSerializableExtra(_KEY_TP_PRODUTO);

                    this.searchProductByCode(_tp.Codigo);
                }

            }
        }
        // endregion

    }
    // endregion


    // region onBackPressed
    @Override
    public void onBackPressed() {

        MSVMsgBox.showMsgBoxQuestion(
                PedidoMobileItemEditarActivity.this,
                "Deseja realmente encerrar esta tela ?",
                "Se clicar em OK os dados incluidos ou alterados nesta tela serão perdidos",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {
                            PedidoMobileItemEditarActivity.super.onBackPressed();
                        }

                    }
                }
        );

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // region LinearLayout
        _pnlProdutoCodigoCnt = (LinearLayout) findViewById(R.id.pnlProdutoCodigoCnt);
        _pnlProdutoCodigoObr = (LinearLayout) findViewById(R.id.pnlProdutoCodigoObr);
        _pnlProdutoCodigo = (LinearLayout) findViewById(R.id.pnlProdutoCodigo);
        _pnlProdutoDescricaoCnt = (LinearLayout) findViewById(R.id.pnlProdutoDescricaoCnt);
        _pnlProdutoDescricaoObr = (LinearLayout) findViewById(R.id.pnlProdutoDescricaoObr);
        _pnlProdutoDescricao = (LinearLayout) findViewById(R.id.pnlProdutoDescricao);
        _pnlEmbalagemQuantidadeCnt = (LinearLayout) findViewById(R.id.pnlEmbalagemQuantidadeCnt);
        _pnlEmbalagemQuantidade = (LinearLayout) findViewById(R.id.pnlEmbalagemQuantidade);
        _pnlValorUnitarioCnt = (LinearLayout) findViewById(R.id.pnlValorUnitarioCnt);
        _pnlValorUnitarioObr = (LinearLayout) findViewById(R.id.pnlValorUnitarioObr);
        _pnlValorUnitario = (LinearLayout) findViewById(R.id.pnlValorUnitario);
        _pnlDescontoPercentualCnt = (LinearLayout) findViewById(R.id.pnlDescontoPercentualCnt);
        _pnlDescontoPercentual = (LinearLayout) findViewById(R.id.pnlDescontoPercentual);
        _pnlDescontoValorCnt = (LinearLayout) findViewById(R.id.pnlDescontoValorCnt);
        _pnlDescontoValor = (LinearLayout) findViewById(R.id.pnlDescontoValor);
        _pnlVendaQuantidadeCnt = (LinearLayout) findViewById(R.id.pnlVendaQuantidadeCnt);
        _pnlVendaQuantidadeObr = (LinearLayout) findViewById(R.id.pnlVendaQuantidadeObr);
        _pnlVendaQuantidade = (LinearLayout) findViewById(R.id.pnlVendaQuantidade);
        _pnlValorUnitarioLiquidoCnt = (LinearLayout) findViewById(R.id.pnlValorUnitarioLiquidoCnt);
        _pnlValorUnitarioLiquido = (LinearLayout) findViewById(R.id.pnlValorUnitarioLiquido);
        _pnlValorUnitarioTotalCnt = (LinearLayout) findViewById(R.id.pnlValorUnitarioTotalCnt);
        _pnlValorUnitarioTotal = (LinearLayout) findViewById(R.id.pnlValorUnitarioTotal);
        _pnlObservacaoCnt = (LinearLayout) findViewById(R.id.pnlObservacaoCnt);
        _pnlObservacao = (LinearLayout) findViewById(R.id.pnlObservacao);
        _pnlProdutoOpcaoCnt = (LinearLayout) findViewById(R.id.pnlProdutoOpcaoCnt);
        _pnlProdutoHistoricoCnt = (LinearLayout) findViewById(R.id.pnlProdutoHistoricoCnt);
        _pnlProdutoImagemCnt = (LinearLayout) findViewById(R.id.pnlProdutoImagemCnt);
        // endregion


        // region TextView
        _txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        _txtProdutoCodigo = (TextView) findViewById(R.id.txtProdutoCodigo);
        _txtProdutoDescricao = (TextView) findViewById(R.id.txtProdutoDescricao);
        _txtEmbalagemQuantidade = (TextView) findViewById(R.id.txtEmbalagemQuantidade);
        _txtValorUnitario = (TextView) findViewById(R.id.txtValorUnitario);
        _txtDescontoPercentual = (TextView) findViewById(R.id.txtDescontoPercentual);
        _txtDescontoValor = (TextView) findViewById(R.id.txtDescontoValor);
        _txtVendaQuantidade = (TextView) findViewById(R.id.txtVendaQuantidade);
        _txtValorUnitarioLiquido = (TextView) findViewById(R.id.txtValorUnitarioLiquido);
        _txtValorUnitarioTotal = (TextView) findViewById(R.id.txtValorUnitarioTotal);
        _txtObservacao = (TextView) findViewById(R.id.txtObservacao);
        // endregion

        //region Img
        _imgValorUnitario = (ImageView) findViewById(R.id.imgValorUnitario);
        //endregion

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region click em PRODUTO CODIGO
        _pnlProdutoCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Invalidando o evento caso a operação seja de alteração
                if (_metodoEdicao == _UPDATE_VALUE) {

                    MSVMsgBox.showMsgBoxInfo(
                            PedidoMobileItemEditarActivity.this,
                            "Seleção do produto bloqueada",
                            "Não é permitido alterar o produto quando estamos alterando um item de pedido"
                    );

                    return;

                }
                // endregion

                // region Selecionando o valor anterior para o campo código
                String _codigo = _tpPedidoMobileItem.Produto == null ? "" : _tpPedidoMobileItem.Produto.Codigo;
                // endregion

                // region Abrindo a caixa de dialogo para informar o código
                MSVMsgBox.getStringNumberValue(
                        PedidoMobileItemEditarActivity.this,
                        "PRODUTO (CODIGO)",
                        "Informe o código do produto desejado. Não é necessário informar os zeros a esquerda",
                        _codigo,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (!MSVUtil.isNullOrEmpty(value.trim())) {

                                    searchProductByCode(value);

                                    if (_tpPedidoMobileItem.Produto != null && _tpPedidoMobileItem.IdProduto != 0) {

                                        MSVMsgBox.getIntValue(
                                                PedidoMobileItemEditarActivity.this,
                                                "QUANTIDADE",
                                                "Informe a quantidade vendida do item",
                                                Double.valueOf(_tpPedidoMobileItem.UnidadeVendaQuantidade).intValue(),
                                                new OnCloseDialog() {
                                                    @Override
                                                    public void onCloseDialog(boolean isOk, String value) {

                                                        if (isOk) {

                                                            _tpPedidoMobileItem.UnidadeVendaQuantidade = MSVUtil.parseInt(value);

                                                            afterVendaQuantidade();
                                                            refreshFormData();

                                                        }

                                                    }
                                                }
                                        );
                                    } else {
                                        Toast.makeText(
                                                PedidoMobileItemEditarActivity.this,
                                                "Não existe produto com o Código informado",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }

                                } else {
                                    Toast.makeText(
                                            PedidoMobileItemEditarActivity.this,
                                            "Nada foi informado como agumento de pesquisa do produto",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region click em PRODUTO DESCRICAO
        _pnlProdutoDescricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Invalidando o evento caso a operação seja de alteração
                if (_metodoEdicao == _UPDATE_VALUE) {

                    MSVMsgBox.showMsgBoxInfo(
                            PedidoMobileItemEditarActivity.this,
                            "Seleção do produto bloqueada",
                            "Não é permitido alterar o produto quando estamos alterando um item de pedido"
                    );

                    return;

                }
                // endregion

                // region Criando um objeto de envio de parâmetros
                Bundle _extras = new Bundle();
                _extras.putInt(_KEY_METODO_EDICAO, _LOOKUP_VALUE);
                _extras.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_PEDIDO_MOBILE_ITEM_VALUE);
                _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
                _extras.putSerializable(_KEY_TP_TABELA_PRECO, _tpTabelaPreco);
                // endregion

                // region Abrindo a tela de pesquisa de produto
                //Intent _intent = new Intent(PedidoMobileItemEditarActivity.this, ProdutoLookupActivity.class);
                //_intent.putExtras(_extras);
                Intent _intent = new Intent(PedidoMobileItemEditarActivity.this, ProdutoSearchActivity.class);
                _intent.putExtras(_extras);

                startActivityForResult(_intent, _PRODUTO_LOOKUP_REQUEST_CODE);
                // endregion

            }
        });
        // endregion


        // region click em VALOR UNITARIO
        _pnlValorUnitario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações antes de abrir a janela de dialogo
                //Caso o Parametro de PedidoValorEhLiberado seja igual 0,
                // não deixa abrir nada
                if (_PEDIDO_VALOR_UNITARIO_LIBERADO == 0) {
                    return;
                }

                // Se não foi escolhido o produto então não vamos
                // deixar abrir a janela de digitação
                if (_tpPedidoMobileItem.Produto == null) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "É necessario selecionar o produto antes",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // se não existe registro na tabela de preço para
                // o produto também não vamos deixar informar valor unitário
                /*
                if (_tpTabelaPrecoProduto == null) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "Não é permitido informar preço para este produto",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }*/

                // endregion

                // region Abrindo a janela para informar o valor unitário
                MSVMsgBox.getDoubleValue(
                        PedidoMobileItemEditarActivity.this,
                        "VALOR UNITÁRIO",
                        "Informe o valor unitário do item " + "Máx: R$" + String.format("%.2f", _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMaximo) + " / Mín: R$" + String.format("%.2f", _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMinimo),
                        _tpPedidoMobileItem.UnidadeValor,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {


                                if (isOk) {

                                    //Amarzena o valor antigo
                                    double oldUnidadeValor = _tpPedidoMobileItem.UnidadeValor;

                                    _tpPedidoMobileItem.UnidadeValor = MSVUtil.parseDouble(value);

                                    boolean isValid = validarValorUnitario();

                                    if (isValid == true) {

                                        afterValorUnitario();
                                        refreshFormData();

                                    } else {
                                        //Restaura o valor antigo
                                        _tpPedidoMobileItem.UnidadeValor = oldUnidadeValor;
                                    }

                                }

                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region click em DESCONTO PERCENTUAL
        _pnlDescontoPercentual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações antes de abrir a janela de dialogo

                // Se não foi escolhido o produto então não vamos
                // deixar abrir a janela de digitação
                if (_tpPedidoMobileItem.Produto == null) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "É necessario selecionar o produto antes",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // Se não existir valor unitário informado então não vamos
                // deixar aplicar desconto no item
                if (_tpPedidoMobileItem.UnidadeValor == 0) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "É necessario informar o valor unitário antes",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // endregion


                // region Abrindo a janela para informar o desconto em (%)
                MSVMsgBox.getDoubleValue(
                        PedidoMobileItemEditarActivity.this,
                        "DESCONTO (%)",
                        "Informe o percentual de desconto a ser aplicano no valor unitário do item",
                        _tpPedidoMobileItem.UnidadeDescontoPercentual,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {

                                    //Armazena o valor antigo
                                    double oldUnidadeDescontoPercentual = _tpPedidoMobileItem.UnidadeDescontoPercentual;

                                    _tpPedidoMobileItem.UnidadeDescontoPercentual = MSVUtil.parseDouble(value);

                                    boolean isValid = validarDescontoPercentual();

                                    if (isValid == true) {


                                        afterDescontoPercentual();
                                        refreshFormData();

                                    } else {

                                        _tpPedidoMobileItem.UnidadeDescontoPercentual = oldUnidadeDescontoPercentual;

                                    }

                                }

                            }

                        }

                );
                // endregion

            }
        });
        // endregion


        // region click em DESCONTO VALOR
        _pnlDescontoValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações antes de abrir a janela de dialogo

                // Se não foi escolhido o produto então não vamos
                // deixar abrir a janela de digitação
                if (_tpPedidoMobileItem.Produto == null) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "É necessario selecionar o produto antes",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // Se não existir valor unitário informado então não vamos
                // deixar aplicar desconto no item
                if (_tpPedidoMobileItem.UnidadeValor == 0) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "É necessario informar o valor unitário antes",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // endregion


                // region Abrindo a janela para informar o valor de desconto (R$)
                MSVMsgBox.getDoubleValue(
                        PedidoMobileItemEditarActivity.this,
                        "DESCONTO (R$)",
                        "Informe o valor financeiro de desconto a ser aplicano no valor unitário do item",
                        _tpPedidoMobileItem.UnidadeDescontoValor,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {

                                    //Armazena os valores antigos
                                    double oldUnidadeDescontoValor = _tpPedidoMobileItem.UnidadeDescontoValor;

                                    _tpPedidoMobileItem.UnidadeDescontoValor = MSVUtil.parseDouble(value);

                                    //region validando valor digitado

                                    if (_tpPedidoMobileItem.UnidadeDescontoValor > _tpPedidoMobileItem.UnidadeValor) {

                                        _tpPedidoMobileItem.UnidadeDescontoValor = 0;

                                        Toast.makeText(
                                                PedidoMobileItemEditarActivity.this,
                                                "O Valor do Desconto não pode ser maior que o Valor Unitário",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        return;
                                    }
                                    //end region

                                    boolean isValid = validarDescontoValor();

                                    if (isValid == true) {

                                        afterDescontoValor();
                                        refreshFormData();

                                    } else {
                                        //restaura os valores iniciais
                                        _tpPedidoMobileItem.UnidadeDescontoValor = oldUnidadeDescontoValor;
                                    }

                                }
                            }

                        }
                );
                // endregion

            }
        });
        // endregion


        // region click em VENDA QUANTIDADE
        _pnlVendaQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Validações antes de abrir a janela de dialogo

                // Se não foi escolhido o produto então não vamos
                // deixar abrir a janela de digitação
                if (_tpPedidoMobileItem.Produto == null) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "É necessario selecionar o produto antes",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // Se não existir valor unitário informado então não vamos
                // deixar aplicar desconto no item
                if (_tpPedidoMobileItem.UnidadeValor == 0) {
                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "É necessario informar o valor unitário antes",
                            Toast.LENGTH_SHORT).show();

                    return;
                }

                // endregion

                // region Abrindo a janela para informar a quantidade vendida do item
                if (_tpPedidoMobileItem.Produto.PermiteDecimal == 0) {
                    MSVMsgBox.getIntValue(
                            PedidoMobileItemEditarActivity.this,
                            "QUANTIDADE",
                            "Informe a quantidade vendida do item",
                            Double.valueOf(_tpPedidoMobileItem.UnidadeVendaQuantidade).intValue(),
                            new OnCloseDialog() {
                                @Override
                                public void onCloseDialog(boolean isOk, String value) {

                                    if (isOk) {

                                        _tpPedidoMobileItem.UnidadeVendaQuantidade = MSVUtil.parseInt(value);

                                        afterVendaQuantidade();
                                        refreshFormData();

                                    }

                                }
                            }
                    );
                } else {
                    MSVMsgBox.getDoubleValue(
                            PedidoMobileItemEditarActivity.this,
                            "QUANTIDADE",
                            "Informe a quantidade vendida do item",
                            _tpPedidoMobileItem.UnidadeVendaQuantidade,
                            new OnCloseDialog() {
                                @Override
                                public void onCloseDialog(boolean isOk, String value) {

                                    if (isOk) {

                                        _tpPedidoMobileItem.UnidadeVendaQuantidade = MSVUtil.parseDouble(value);

                                        afterVendaQuantidade();
                                        refreshFormData();

                                    }

                                }
                            }
                    );
                }
                // endregion

            }
        });
        // endregion


        // region click em OBSERVACAO
        _pnlObservacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Abrindo a janela para informar a observação do item
                MSVMsgBox.getStringValue(
                        PedidoMobileItemEditarActivity.this,
                        "OBSERVAÇÃO",
                        "Informe o texto que define a observação para o item do pedido",
                        _tpPedidoMobileItem.Observacao,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                _tpPedidoMobileItem.Observacao = value.trim().toUpperCase();

                                refreshFormData();

                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region click em HISTORICO
        _pnlProdutoHistoricoCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_tpPedidoMobileItem.Produto == null) {
                    MSVMsgBox.showMsgBoxInfo(
                            PedidoMobileItemEditarActivity.this,
                            "Produto não selecionado",
                            "O histórico de compra do cliente so poder visualizado após escolher o mesmo aqui na tela de informação do item do pedido"
                    );
                } else {

                    loadHistoricoCompra();

                    if (_lstHistoricoCompra != null && _lstHistoricoCompra.size() > 0) {

                        HistoricoCompraDialogAdapter _adp = new HistoricoCompraDialogAdapter(
                                PedidoMobileItemEditarActivity.this,
                                _lstHistoricoCompra
                        );

                        MSVMsgBox.showMsgBoxList(
                                PedidoMobileItemEditarActivity.this,
                                "HISTÓRICO DE COMPRA",
                                _adp,
                                new OnCloseDialog() {
                                    @Override
                                    public void onCloseDialog(boolean isOk, String value) {

                                    }
                                }
                        );

                    } else {

                        MSVMsgBox.showMsgBoxInfo(
                                PedidoMobileItemEditarActivity.this,
                                "Histórico de compra não encontrado para este produto"
                        );

                    }

                }

            }
        });
        // endregion


        // region click em IMAGEM
        _pnlProdutoImagemCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_tpPedidoMobileItem.Produto == null) {
                    MSVMsgBox.showMsgBoxInfo(
                            PedidoMobileItemEditarActivity.this,
                            "Produto não selecionado",
                            "A imagem do produto só poder visualizada após escolher o mesmo aqui na tela de informação do item do pedido"
                    );
                } else {

                    if (existeImage()) {

                        Bundle _extras = new Bundle();
                        _extras.putInt(_KEY_METODO_EDICAO, _DETAIL_VALUE);
                        _extras.putInt(_KEY_ITEM_INDEX, 0);
                        _extras.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_PEDIDO_MOBILE_ITEM_VALUE);
                        _extras.putSerializable(_KEY_TP_PRODUTO, _tpPedidoMobileItem.Produto);

                        Intent _i = new Intent(PedidoMobileItemEditarActivity.this, ProdutoDetalheImagemActivity.class);
                        _i.putExtras(_extras);

                        startActivity(_i);

                    } else {

                        MSVMsgBox.showMsgBoxWarning(
                                PedidoMobileItemEditarActivity.this,
                                "Imagem não localizada",
                                "A imagem para este produto não foi localizada no diretório de imagem deste dispositivo, ou ainda não foi dado a permissão para que o aplicativo possa ler o arquivo no diretório especificado"
                        );

                    }


                }

            }
        });
        // endregion

    }
    // endregion


    // region initialize
    @Override
    public void initialize() {

        // region Informando valores default para algumas vies da activity
        _txtProdutoCodigo.setText("...");
        _txtProdutoDescricao.setText("...");
        _txtEmbalagemQuantidade.setText("0");
        _txtValorUnitario.setText("R$ 0,00");
        _txtDescontoPercentual.setText("0%");
        _txtDescontoValor.setText("R$ 0,00");
        _txtVendaQuantidade.setText("0");
        _txtValorUnitarioLiquido.setText("R$ 0,00");
        _txtValorUnitarioTotal.setText("R$ 0,00");
        _txtObservacao.setText("");
        // endregion


        // region Informando ao usuário o metodo de edicao
        if (_metodoEdicao == _INSERT_VALUE) {
            _txtTitulo.setText("INSERINDO ITEM");
        } else {
            _txtTitulo.setText("ALTERANDO ITEM");
        }
        // endregion


        // region Verificando a necessidade de atualizar os dados da activity
        if (_metodoEdicao == _UPDATE_VALUE) {
            this.refreshFormData();
        }
        // endregion

    }
    // endregion


    // region createProdutoOpcao
    private void createProdutoOpcao() {

        tpKeyValueRow _tpOpcao1 = new tpKeyValueRow();
        _tpOpcao1.Key = 0;
        _tpOpcao1.Value = "VISUALIZAR IMAGEM";
        _tpOpcao1.ImageResource = R.drawable.msv_small_photo1_black;

        tpKeyValueRow _tpOpcao2 = new tpKeyValueRow();
        _tpOpcao2.Key = 1;
        _tpOpcao2.Value = "ULTIMAS COMPRAS";
        _tpOpcao2.ImageResource = R.drawable.msv_small_last_buy_black;

        if (_lstProdutoOpcao == null) {
            _lstProdutoOpcao = new ArrayList<tpKeyValueRow>();
        }

        _lstProdutoOpcao.add(_tpOpcao1);
        _lstProdutoOpcao.add(_tpOpcao2);

    }
    // endregion


    // region refreshFormData
    private void refreshFormData() {

        if (_tpPedidoMobileItem != null) {

            if (_tpPedidoMobileItem.Produto != null) {

                // Codigo do produto
                _txtProdutoCodigo.setText(_tpPedidoMobileItem.Produto.Codigo);

                // Descricao do produto
                _txtProdutoDescricao.setText(_tpPedidoMobileItem.Produto.Descricao);

                // Quantidade por embalagem
                _txtEmbalagemQuantidade.setText(
                        String.format(
                                "%s - %s",
                                _tpPedidoMobileItem.Produto.UnidadeMedida,
                                _tpPedidoMobileItem.PackQuantidade
                        )
                );

                if (_tpPedidoMobileItem.Produto.TabelaPrecoProduto != null) {
                    // Valor unitário do prodto
                    _txtValorUnitario.setText(MSVUtil.doubleToText("R$", _tpPedidoMobileItem.UnidadeValor));
                }

            }

            // Desconto em forma de percentual
            _txtDescontoPercentual.setText(MSVUtil.doubleToText(_tpPedidoMobileItem.UnidadeDescontoPercentual) + "%");

            // Desconto em forma de valor
            _txtDescontoValor.setText(MSVUtil.doubleToText("R$", _tpPedidoMobileItem.UnidadeDescontoValor));

            // Valor unitário (LIQUIDO)
            _txtValorUnitarioLiquido.setText(MSVUtil.doubleToText("R$", _tpPedidoMobileItem.UnidadeValorLiquido));

            // Quantidade vendida
            if (_tpPedidoMobileItem.Produto.PermiteDecimal == 0) {
                _txtVendaQuantidade.setText(String.valueOf(MSVUtil.parseInt(_tpPedidoMobileItem.UnidadeVendaQuantidade)));
            } else {
                _txtVendaQuantidade.setText(String.valueOf(_tpPedidoMobileItem.UnidadeVendaQuantidade));
            }

            // Valor total
            _txtValorUnitarioTotal.setText(MSVUtil.doubleToText("R$", _tpPedidoMobileItem.UnidadeValorTotal));

            // Observacao
            _txtObservacao.setText(_tpPedidoMobileItem.Observacao);

        }

    }
    // endregion


    // region searchProductByCode
    private boolean searchProductByCode(String value) {

        boolean _out = false;

        // region Zerando as propriedades do produto
        _tpPedidoMobileItem.IdProduto = 0;
        _tpPedidoMobileItem.Produto = null;
        // endregoin


        // region Completando o código do produto com zeros
        if (value.length() < 5) {
            value = String.format("%05d", Integer.parseInt(value));
        }
        // endregion


        // region Buscando o produto a apartir de seu código
        SQLiteHelper _sqh = null;

        try {

            WhereItem _wiCodigo = new WhereItem();
            _wiCodigo.field = "Codigo";
            _wiCodigo.dataType = eSQLiteDataType.TEXT;
            _wiCodigo.conditionType = eSQLConditionType.EQUAL;
            _wiCodigo.value1 = value;

            SQLClauseHelper _sch01 = new SQLClauseHelper();
            _sch01.addWhere(_wiCodigo);

            _sqh = new SQLiteHelper(PedidoMobileItemEditarActivity.this);
            _sqh.open(false);

            dbProduto _dbProduto = new dbProduto(_sqh);
            _tpPedidoMobileItem.Produto = (tpProduto) _dbProduto.getOne(_sch01);


            if (_tpPedidoMobileItem.Produto != null) {

                // region Verificando se o produto possui saldo
                if (_tpPedidoMobileItem.Produto.SaldoQuantidade <= 0) {
                    MSVMsgBox.showMsgBoxWarning(
                            PedidoMobileItemEditarActivity.this,
                            "Este produto não possui saldo para venda",
                            "O produto selecionado não possui saldo disponível que permita a sua venda"
                    );

                    return false;
                }
                // endregion

                // region 02.04 - Verificando se o produto já existe quanto é INSERT
                if (_metodoEdicao == _INSERT_VALUE) {

                    if ((_lstProdutoIncluido != null) && (_lstProdutoIncluido.size() > 0)) {

                        for (String _item : _lstProdutoIncluido) {
                            if (_item.equals(_tpPedidoMobileItem.Produto.Codigo)) {

                                _tpPedidoMobileItem.IdProduto = 0;
                                _tpPedidoMobileItem.Produto = null;

                                MSVMsgBox.showMsgBoxInfo(
                                        PedidoMobileItemEditarActivity.this,
                                        "Este produto já está incluido como item deste pedido",
                                        "Não é permitido adicionar o mesmo item mais de uma vez para o mesmo pedido"
                                );

                                return false;

                            }
                        }

                    }

                }
                // endregion

                // region Montando WHERE para o novo produto selecionado
                SQLClauseHelper _sch02 = new SQLClauseHelper();
                _sch02.addEqualInteger("IdTabelaPreco", _tpTabelaPreco.IdTabelaPreco);
                _sch02.addEqualInteger("IdProduto", _tpPedidoMobileItem.Produto.IdProduto);
                // endregion

                // region Buscando o preço do produto no banco de dados
                dbTabelaPrecoProduto _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqh);
                _tpPedidoMobileItem.Produto.TabelaPrecoProduto = (tpTabelaPrecoProduto) _dbTabelaPrecoProduto.getOne(_sch02);
                // endregion

                // region Validando se o preço foi encontrado
                if (_tpPedidoMobileItem.Produto.TabelaPrecoProduto != null) {

                    _tpPedidoMobileItem.IdProduto = _tpPedidoMobileItem.Produto.IdProduto;
                    _tpPedidoMobileItem.PackQuantidade = _tpPedidoMobileItem.Produto.PackQuantidade;
                    _tpPedidoMobileItem.UnidadeValor = _tpPedidoMobileItem.Produto.TabelaPrecoProduto.Preco;
                    _tpPedidoMobileItem.UnidadeValorLiquido = _tpPedidoMobileItem.Produto.TabelaPrecoProduto.Preco;

                    this.refreshFormData();

                } else {

                    Toast.makeText(
                            PedidoMobileItemEditarActivity.this,
                            "Não foi possível localizar o preço para o produto " + value,
                            Toast.LENGTH_SHORT
                    );

                }
                // endregion

            } else {

                Toast.makeText(
                        PedidoMobileItemEditarActivity.this,
                        "O produto " + value + " não foi localizado no sistema",
                        Toast.LENGTH_SHORT
                );

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }
        // endregion

        return _out;

    }
    // endregion


    // region loadProduto
    private boolean loadProduto(SearchProductField field, String value) {

        boolean _out = false;


        // region 01 - Preparando o formulário

        // Limpando a referencia para o objeto produto
        _tpPedidoMobileItem = new tpPedidoMobileItem();
        _tpPedidoMobileItem.Produto = null;

        // Limpando a referencia para o objeto tabela de preco
        _tpTabelaPrecoProduto = null;

        // Limpando os campos do form
        _txtProdutoCodigo.setText("...");
        _txtProdutoDescricao.setText("...");
        _txtEmbalagemQuantidade.setText("0");
        _txtValorUnitario.setText("R$ 0,00");
        _txtDescontoPercentual.setText("0%");
        _txtDescontoValor.setText("R$ 0,00");
        _txtVendaQuantidade.setText("0");
        _txtValorUnitarioLiquido.setText("R$ 0,00");
        _txtValorUnitarioTotal.setText("R$ 0,00");
        _txtObservacao.setText("");

        // endregion


        // region 02 - Realizando a pesquisa pelo código do produto
        if (field == SearchProductField.CODIGO) {

            SQLiteHelper _sqh = null;

            try {

                // region 02.01 - Montando a condição where para o campo CODIGO
                WhereItem _wiCodigo = new WhereItem();
                _wiCodigo.field = "Codigo";
                _wiCodigo.dataType = eSQLiteDataType.TEXT;
                _wiCodigo.conditionType = eSQLConditionType.EQUAL;
                _wiCodigo.value1 = value;

                SQLClauseHelper _where = new SQLClauseHelper();
                _where.addWhere(_wiCodigo);
                // endregion

                // region 02.02 - Instânciando e abrindo a conexão com o banco
                _sqh = new SQLiteHelper(PedidoMobileItemEditarActivity.this);
                _sqh.open(false);
                // endregion

                // region 02.03 - Buscando no banco o produto desejado
                dbProduto _dbProduto = new dbProduto(_sqh);
                _tpPedidoMobileItem.Produto = (tpProduto) _dbProduto.getOne(_where);
                // endregion

            } catch (Exception e) {

                return _out;

            } finally {

                if ((_sqh != null) && (_sqh.isOpen())) {
                    _sqh.close();
                }

            }

        }
        // endregion


        // region 03 - Realizando a pesquisa pelo identificador do produto
        if (field == SearchProductField.ID) {

            SQLiteHelper _sqh = null;

            try {

                // region 03.01 - Instânciando e abrindo a conexão com o banco
                _sqh = new SQLiteHelper(PedidoMobileItemEditarActivity.this);
                _sqh.open(false);
                // endregion

                // region 03.02 - Buscando no banco o produto desejado
                dbProduto _dbProduto = new dbProduto(_sqh);
                _tpPedidoMobileItem.Produto = (tpProduto) _dbProduto.getBySourceId(Long.parseLong(value));
                // endregion

            } catch (Exception e) {

                return _out;

            } finally {

                if ((_sqh != null) && (_sqh.isOpen())) {
                    _sqh.close();
                }

            }

        }
        // endregion


        // region 04 - Realizando operações necessárias finais
        if (_tpPedidoMobileItem.Produto != null) {

            // region 04.01 - Buscando o preço do produto
            SQLiteHelper _sqh = null;

            try {

                // region 04.01.01 - Criando a condição where para localizar o preço do produto
                SQLClauseHelper _sch = new SQLClauseHelper();
                _sch.addEqualInteger("IdTabelaPreco", _tpTabelaPreco.IdTabelaPreco);
                _sch.addEqualInteger("IdProduto", _tpPedidoMobileItem.Produto.IdProduto);
                // endregion

                // region 04.01.02 -  Instânciando e abrindo a conexão com o banco de dados
                _sqh = new SQLiteHelper(PedidoMobileItemEditarActivity.this);
                _sqh.open(false);
                // endregion

                // region 04.01.03 - Buscando a tabela de preço no banco
                dbTabelaPrecoProduto _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqh);
                _tpTabelaPrecoProduto = (tpTabelaPrecoProduto) _dbTabelaPrecoProduto.getOne(_sch);
                // endregion

                // region 04.01.04 - Verificando se a tabela de preço foi encontrada
                if (_tpTabelaPrecoProduto == null) {
                    throw new Exception("Tabela de preço não localizada para o produto " + _tpPedidoMobileItem.Produto.Codigo);
                }
                // endregion

            } catch (Exception e) {

                Toast.makeText(
                        PedidoMobileItemEditarActivity.this,
                        "ATENÇÃO, não foi localizado o preço para este produto",
                        Toast.LENGTH_SHORT
                ).show();

                return false;

            } finally {

                if ((_sqh != null) && (_sqh.isOpen())) {
                    _sqh.close();
                }

            }

            // endregion


            // region 04.02 - Atualizando informações do objeto tpPedidoMobileItem
            _tpPedidoMobileItem.IdProduto = _tpPedidoMobileItem.Produto.IdProduto;
            _tpPedidoMobileItem.PackQuantidade = _tpPedidoMobileItem.Produto.PackQuantidade;
            _tpPedidoMobileItem.UnidadeValor = _tpTabelaPrecoProduto.Preco;
            _tpPedidoMobileItem.UnidadeValorLiquido = _tpTabelaPrecoProduto.Preco;
            // endregion


            // region 04.03 - Atualizando os dados na tela
            this.refreshFormData();
            // endregion

        } else {

            Toast.makeText(
                    PedidoMobileItemEditarActivity.this,
                    "ATENÇÃO, não foi localizar o produto desejado",
                    Toast.LENGTH_SHORT
            ).show();

        }
        // endregion


        return _out;

    }
    // endregion


    // region loadHistoricoCompra
    private void loadHistoricoCompra() {

        SQLiteHelper _sqh = null;

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdCliente", _idCliente);
            _sch.addEqualInteger("IdProduto", _tpPedidoMobileItem.IdProduto);


            _sqh = new SQLiteHelper(PedidoMobileItemEditarActivity.this);
            _sqh.open(false);


            dbClienteHistoricoCompra _db = new dbClienteHistoricoCompra(_sqh);
            _lstHistoricoCompra = (ArrayList<tpClienteHistoricoCompra>) _db.getList(tpClienteHistoricoCompra.class, _sch);

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "Erro ao ler as informações de histórico de compra",
                    e.getMessage()
            );

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }

    }
    // endregion


    // region afterValorUnitario
    private void afterValorUnitario() {

        // declarando uma variável de nome menor para o objeto _tpPedidoProduto
        tpPedidoMobileItem _tp = _tpPedidoMobileItem;

        if (_tp.UnidadeValor == 0) {

            // zerando os valores de desconto
            _tp.UnidadeDescontoPercentual = 0;
            _tp.UnidadeDescontoValor = 0;

            // zerando o valor unitário líquido
            _tp.UnidadeValorLiquido = 0;

            // zerando o valor total
            _tp.UnidadeValorTotal = 0;

        } else {

            // invocando o método que irá calcular os
            // descontos e o valor total do item
            this.afterDescontoValor();

        }

    }
    // endregion


    // region afterDescontoPercentual
    private void afterDescontoPercentual() {

        // declarando um variável de nome menor para o objeto _tpPedidoProduto
        tpPedidoMobileItem _tp = _tpPedidoMobileItem;


        if (_tp.UnidadeDescontoPercentual == 0) {

            // zerando o desconto por valor
            _tp.UnidadeDescontoValor = 0;

            // atribuindo o valor unitário para o valor unitário líquido
            _tp.UnidadeValorLiquido = _tp.UnidadeValor;

            // recalculando o valor total
            _tp.UnidadeValorTotal = (_tp.UnidadeVendaQuantidade * _tp.UnidadeValorLiquido);

        } else {

            // calculando o desconto em valor
            _tp.UnidadeDescontoValor = ((_tp.UnidadeValor * _tp.UnidadeDescontoPercentual) / 100);

            // calculando o valor unitário líquido
            _tp.UnidadeValorLiquido = (_tp.UnidadeValor - _tp.UnidadeDescontoValor);

            // calculando o valor total
            _tp.UnidadeValorTotal = (_tp.UnidadeVendaQuantidade * _tp.UnidadeValorLiquido);

        }

    }
    // endregion


    // region afterDescontoValor
    private void afterDescontoValor() {

        // declarando um variável de nome menor para o objeto _tpPedidoProduto
        tpPedidoMobileItem _tp = _tpPedidoMobileItem;

        if (_tp.UnidadeDescontoValor == 0) {

            // zerando o desconto em percentual
            _tp.UnidadeDescontoPercentual = 0;

            // atribuindo o valor unitário para o valor unitário líquido
            _tp.UnidadeValorLiquido = _tp.UnidadeValor;

            // recalculando o valor total
            _tp.UnidadeValorTotal = (_tp.UnidadeVendaQuantidade * _tp.UnidadeValorLiquido);

        } else {

            _tp.UnidadeDescontoPercentual = (_tp.UnidadeDescontoValor / _tp.UnidadeValor) * 100;
            _tp.UnidadeValorLiquido = (_tp.UnidadeValor - _tp.UnidadeDescontoValor);
            _tp.UnidadeValorTotal = (_tp.UnidadeVendaQuantidade * _tp.UnidadeValorLiquido);

        }

    }
    // endregion


    //region validarDescontoPercentual
    private boolean validarDescontoPercentual() {

        boolean _Out = true;

        // calculando o desconto em valor
        double UnidadeDescontoValor = ((_tpPedidoMobileItem.UnidadeValor * _tpPedidoMobileItem.UnidadeDescontoPercentual) / 100);

        // calculando o valor unitário líquido
        double UnidadeValorLiquido = (_tpPedidoMobileItem.UnidadeValor - UnidadeDescontoValor);

        if (this.ceilDecimal(UnidadeValorLiquido) > _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMaximo) {


            MSVMsgBox.showMsgBoxInfo(
                    PedidoMobileItemEditarActivity.this,
                    "Erro de Validação",
                    "Atenção, o preço selecionado é maior que o preço máximo permitido. " + _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMaximo + "."
            );

            _Out = false;

        }

        double teste = this.ceilDecimal(UnidadeValorLiquido);

        if (this.ceilDecimal(UnidadeValorLiquido) < _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMinimo) {

            MSVMsgBox.showMsgBoxInfo(
                    PedidoMobileItemEditarActivity.this,
                    "Erro de Validação",
                    "Atenção, o preço selecionado é menor que o preço mínimo permitido. (R$" + _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMinimo + ")."
            );


            _Out = false;

        }


        return _Out;
    }
    //endregion


    //region validarDescontoValor
    private boolean validarDescontoValor() {

        boolean _Out = true;

        double UnidadeValorLiquido = (_tpPedidoMobileItem.UnidadeValor - _tpPedidoMobileItem.UnidadeDescontoValor);

        if (this.ceilDecimal(UnidadeValorLiquido) > _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMaximo) {


            MSVMsgBox.showMsgBoxInfo(
                    PedidoMobileItemEditarActivity.this,
                    "Erro de Validação",
                    "Atenção, o preço selecionado é maior que o preço máximo permitido. " + _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMaximo + "."
            );

            _Out = false;

        }

        if (this.ceilDecimal(UnidadeValorLiquido) < _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMinimo) {

            MSVMsgBox.showMsgBoxInfo(
                    PedidoMobileItemEditarActivity.this,
                    "Erro de Validação",
                    "Atenção, o preço selecionado é menor que o preço mínimo permitido. (R$" + _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMinimo + ")."
            );


            _Out = false;

        }


        return _Out;
    }

    //endregion


    //region validarValorUnitario
    private boolean validarValorUnitario() {

        return validarDescontoValor();

    }
    //endregion


    // region afterVendaQuantidade
    private void afterVendaQuantidade() {

        // declarando um variável de nome menor para o objeto _tpPedidoProduto
        tpPedidoMobileItem _tp = _tpPedidoMobileItem;


        if (_tp.UnidadeVendaQuantidade == 0) {

            // atualizando o valor total
            _tp.UnidadeValorTotal = 0;

        } else {

            // atualizando o valor total
            _tp.UnidadeValorTotal = (_tp.UnidadeVendaQuantidade * _tp.UnidadeValorLiquido);

        }

    }
    // endregion


    // region isValid
    private boolean isValid() {

        // region Verificando se foi escolhido o produto
        if (_tpPedidoMobileItem.Produto == null) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "PRODUTO",
                    "Atenção, é necessário selecionar um produto antes de tentar salvar o item"
            );

            return false;

        }

        if (_tpPedidoMobileItem.IdProduto == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "PRODUTO",
                    "Atenção, é necessário selecionar um produto antes de tentar salvar o item"
            );

            return false;

        }
        // endregion

        // region Verificando se o item possui valor unitário
        if (_tpPedidoMobileItem.UnidadeValor == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "VALOR UNITÁRIO",
                    "Atenção, este item não possui valor unitário e não poderá ser escolhido para o pedido"
            );

            return false;

        }
        // endregion

        // region Verificando se o valor unitário final é maior que zero
        if (_tpPedidoMobileItem.UnidadeValorLiquido <= 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "VALOR UNITÁRIO LIQUIDO",
                    "Atenção, o valor unitário líquido do item não pode ser menor ou igual a zero"
            );

            return false;

        }
        // endregion

        // region Verificando se existe quantidade de venda informada
        if (_tpPedidoMobileItem.UnidadeVendaQuantidade == 0) {

            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "QUANTIDADE VENDA",
                    "Atenção, é necessário informar a quantidade desejada pelo cliente para este item"
            );

            return false;

        }
        // endregion

        if (_tpPedidoMobileItem.UnidadeValorLiquido < _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMinimo) {
            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "UNIDADE VALOR LIQUIDO",
                    "Atenção, o preço selecionado é menor que o preço mínimo permitido. " + _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMinimo + "."
            );

            return false;
        }

        if (_tpPedidoMobileItem.UnidadeValorLiquido > _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMaximo) {
            MSVMsgBox.showMsgBoxError(
                    PedidoMobileItemEditarActivity.this,
                    "UNIDADE VALOR LIQUIDO",
                    "Atenção, o preço selecionado é maior que o preço máximo permitido. " + _tpPedidoMobileItem.Produto.TabelaPrecoProduto.PrecoMaximo + "."
            );

            return false;
        }

        return true;

    }
    // endregion


    // region save
    private void save() {

        if (isValid()) {

            try {

                // region Criando uma intent para encapsular os parametros de retorno
                Intent _intent = new Intent();
                _intent.putExtra(_KEY_TP_PEDIDO_MOBILE_ITEM, _tpPedidoMobileItem);
                _intent.putExtra(_KEY_METODO_EDICAO, _metodoEdicao);
                _intent.putExtra(_KEY_ITEM_INDEX, _itemIndex);
                // endregion

                // region Marcando o retorno como OK e informando os parametros
                setResult(Activity.RESULT_OK, _intent);
                finish();
                // endregion

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    // endregion


    // region cancel
    private void cancel() {

        MSVMsgBox.showMsgBoxQuestion(
                PedidoMobileItemEditarActivity.this,
                "Deseja realmente encerrar esta tela ?",
                "Se clicar em OK os dados incluidos ou alterados nesta tela serão perdidos",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }

                    }
                }
        );

    }
    // endregion


    // region existeImage
    private boolean existeImage() {

        boolean _out = false;

        // region Se a imagem existir então vamos mostrar
        if (_tpPedidoMobileItem.Produto != null) {

            // region Formatando o nome do arquivo
            String _nome = _tpPedidoMobileItem.Produto.Codigo + ".png";
            // endregion

            // region Se existir o arquivo então vamos mostrar
            File _img = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), _nome);

            if (_img.exists()) {

                _out = true;

            }
            // endregion

        }
        // endregion

        return _out;

    }
    // endregion


    //region formatDecimal
    private double ceilDecimal(Double Numero) {


        if (Numero == null) {
            Numero = 0D;
        }

        return Math.ceil(Numero * 100.0) / 100.0;


    }
    //endregion


}
