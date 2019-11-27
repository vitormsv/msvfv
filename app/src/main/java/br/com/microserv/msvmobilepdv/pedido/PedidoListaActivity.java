package br.com.microserv.msvmobilepdv.pedido;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
import br.com.microserv.framework.msvdal.dbTipoPedido;
import br.com.microserv.framework.msvdal.dbTabelaPreco;
import br.com.microserv.framework.msvdal.dbTransportadora;
import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpKeyValueRow;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
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
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.CidadeDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.KeyValueDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.PedidoListaAdapter;

public class PedidoListaActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // RequestCode
    static final int _PEDIDO_REQUEST_CODE = 100;

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_ID_CLIENTE = "IdCliente";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_ID_PEDIDO_MOBILE = "IdPedidoMobile";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final String _PEDIDO_LISTA_VALUE = "PedidoListaActivity";

    // endregion


    // region Declarando variaveis para conter as views da activity

    // LinearLayout
    LinearLayout _pnlTituloCnt = null;
    LinearLayout _pnlFiltroCnt = null;
    LinearLayout _pnlRodapeCnt = null;
    LinearLayout _pnlCidade = null;

    // ListView
    ListView _livPedido = null;

    // TextView
    TextView _txtTitulo = null;
    TextView _txtCidade = null;
    TextView _txtRodape = null;

    // Switch
    Switch _swiHideSync = null;

    // endregion


    // region Declarando variáveis locais

    // Objects
    tpEmpresa _tpEmpresa = null;
    tpParametro _tpServidorRestIp = null;
    tpParametro _tpVendedorCodigo = null;
    tpKeyValueRow _tpOption = null;
    tpPedidoMobile _tpPedidoMobileSelected = null;

    // Dialogs
    ProgressDialog _wait = null;

    // Adapters
    CidadeDialogSearchAdapter _adpCidade = null;
    PedidoListaAdapter _adpPedidoMobile = null;
    KeyValueDialogSearchAdapter _adpKeyValue = null;

    // ArrayList
    ArrayList<tpCidade> _lstCidade = null;
    ArrayList<tpPedidoMobile> _lstPedidoMobile = null;
    ArrayList<tpPedidoMobile> _lstPedidoMobileFlt = null;
    ArrayList<tpKeyValueRow> _lstItemOptions = null;

    // String
    String _sourceActivity = "";

    // int
    int _metodoEdicao = 0;
    int _iPedidoMobileSelected = 0;
    int _iCidade = -1;
    int _iAux = -1;

    // boolean
    boolean _IsShowFilter;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando os elementos da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_lista);
        // endregion

        // region Adicionando suporte a action bar
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
                        PedidoListaActivity.this,
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
                        PedidoListaActivity.this,
                        "O parâmetro _KEY_SOURCE_ACTIVITY não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_TP_EMPRESA
            if (_extras.containsKey(_KEY_TP_EMPRESA)) {
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);
            } else {
                Toast.makeText(
                        PedidoListaActivity.this,
                        "O parâmetro _KEY_TP_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        }
        // endregion

        // region Invocando os métodos da interface
        bindElements();
        bindEvents();

        initialize();
        // endregion

    }
    // endregion


    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_pedido_lista, menu);
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

            case R.id.mnSincronizarTodos:
                syncronize();
                break;

            case R.id.mnTipoPedidoValorTotal:
                showSumary();
                break;

        }


        return super.onOptionsItemSelected(item);

    }
    // endregion


    // region onBackPressed
    @Override
    public void onBackPressed() {

        if (_wait == null) {

            super.onBackPressed();

        } else {

            MSVMsgBox.showMsgBoxWarning(
                    PedidoListaActivity.this,
                    "Não é possível encerrar esta atividade enquanto os pedidos estão sendo enviados"
            );

        }

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // LinearLayout
        _pnlTituloCnt = (LinearLayout) findViewById(R.id.pnlTituloCnt);
        _pnlFiltroCnt = (LinearLayout) findViewById(R.id.pnlFiltroCnt);
        _pnlRodapeCnt = (LinearLayout) findViewById(R.id.pnlRodapeCnt);

        _pnlCidade = (LinearLayout) findViewById(R.id.pnlCidade);

        // Switch
        _swiHideSync = (Switch) findViewById(R.id.swiHideSync);

        // ListView
        _livPedido = (ListView) findViewById(R.id.livPedido);

        // TextView
        _txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        _txtCidade = (TextView) findViewById(R.id.txtCidade);
        _txtRodape = (TextView) findViewById(R.id.txtRodape);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region _llyTitulo
        _pnlTituloCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _IsShowFilter = !_IsShowFilter;
                refreshFilterContent();
            }
        });
        // endregion

        // region Click em CIDADE
        _pnlCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // region Verificando quantas cidades existem na lista
                if (_lstCidade.size() == 1) {

                    Toast.makeText(
                            PedidoListaActivity.this,
                            "Não é possível abrir a lista de cidades, pois não existem pedido registrados",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }
                // endregion

                // region Verificando a necessidade de criar o adapter
                _adpCidade = new CidadeDialogSearchAdapter(
                        PedidoListaActivity.this,
                        _lstCidade
                );
                // endregion

                // region Abrindo a janela para escolha da cidade
                MSVMsgBox.getValueFromList(
                        PedidoListaActivity.this,
                        _lstCidade.get(_iCidade).Descricao,
                        _adpCidade,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iCidade = _iAux;

                                refreshCidade();

                                filterPedido();
                                refreshPedido();
                            }
                        }
                );
                // endregion

            }
        });
        // endregion

        // region Click em NAO SINCRONIZADO
        _swiHideSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                filterPedido();
                refreshPedido();
            }
        });
        // endregion

        // region Click no item da lista de PEDIDO
        _livPedido.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                // region Carregando as informações do pedido selecionado
                _iPedidoMobileSelected = position;
                _tpPedidoMobileSelected = _lstPedidoMobile.get(position);
                // endregion

                // region Verificando a necessidade de instânciar o adapter
                if (_adpKeyValue == null) {
                    _adpKeyValue = new KeyValueDialogSearchAdapter(
                            PedidoListaActivity.this,
                            _lstItemOptions
                    );
                }
                // endregion

                // region Montando a janela de escolha de opções
                _iAux = -1;
                _tpOption = null;

                MSVMsgBox.getValueFromList(
                        PedidoListaActivity.this,
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
                                                PedidoListaActivity.this,
                                                "Nenhuma opção foi selecionada pelo usuário",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        return;

                                    }
                                    // endregion

                                    // region Processando a opção do usuário

                                    // region EDITAR
                                    if (_tpOption.Key == 0) {
                                        editPedido();
                                    }
                                    // endregion

                                    // region CONFIRMAR PEDIDO
                                    if (_tpOption.Key == 1) {
                                        confirmPedido(_iPedidoMobileSelected);
                                    }
                                    // endregion

                                    // region SINCRONIZAR
                                    if (_tpOption.Key == 2) {
                                        sincronizarPedido(_iPedidoMobileSelected);
                                    }
                                    // endregion

                                    // region COMPARTILHAR
                                    if (_tpOption.Key == 3) {
                                        compartilharPedido(_iPedidoMobileSelected);
                                    }
                                    // endregion

                                    // region EXCLUIR
                                    if (_tpOption.Key == 4) {

                                        MSVMsgBox.showMsgBoxQuestion(
                                                PedidoListaActivity.this,
                                                "Deseja realmente excluir este pedido ?",
                                                "Se clicar em OK este pedido será removido do dispositivo",
                                                new OnCloseDialog() {
                                                    @Override
                                                    public void onCloseDialog(boolean isOk, String value) {
                                                        if (isOk) {
                                                            deletePedido(_iPedidoMobileSelected);
                                                        }
                                                    }
                                                }
                                        );

                                    }
                                    // endregion

                                    // region SALVAR NO DIRETORIO
                                    if (_tpOption.Key == 5) {

                                        MSVMsgBox.showMsgBoxQuestion(
                                                PedidoListaActivity.this,
                                                "Deseja realmente salvar este pedido no diretório do seu dispositivo ?",
                                                "Se clicar em OK este pedido será gerado nos formatos *.json e *.html dentro do diretóio [DOCUMENTS] do seu dispositivo",
                                                new OnCloseDialog() {
                                                    @Override
                                                    public void onCloseDialog(boolean isOk, String value) {
                                                        if (isOk) {
                                                            salvarFiles(_iPedidoMobileSelected);
                                                        }
                                                    }
                                                }
                                        );

                                    }
                                    // endregion

                                    // endregion

                                }

                            }
                        }
                );
                // endregion

                return true;

            }
        });
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

                    // region Declarando variáveis
                    SQLiteHelper _sqh = null;
                    dbPedidoMobile _dbPedidoMobile = null;
                    tpPedidoMobile _tpPedidoMobile = null;
                    // endregion

                    // region Recuperando um novo objeto de pedido
                    try {

                        // region Abrindo a conexão com o banco de dados
                        _sqh = new SQLiteHelper(PedidoListaActivity.this);
                        _sqh.open(false);
                        // endregion

                        // region Cuidando da instância dos objetos que fazem acesso ao banco de dados
                        _dbPedidoMobile = new dbPedidoMobile(_sqh);
                        // endregion

                        // region Realizando a leitura do registro
                        _tpPedidoMobile = _dbPedidoMobile.loadOneById(_tpPedidoMobileSelected._id);
                        // endregion

                    } catch (Exception e) {

                        MSVMsgBox.showMsgBoxError(
                                PedidoListaActivity.this,
                                "Erro ao tentar um objeto de pedido",
                                e.getMessage()
                        );

                        finish();

                    } finally {

                        if ((_sqh != null) && (_sqh.isOpen())) {
                            _sqh.close();
                        }

                    }
                    // endregion

                    // region Substituindo o pedido atual da lista pelo novo
                    _lstPedidoMobileFlt.set(_iPedidoMobileSelected, _tpPedidoMobile);
                    // endregion

                    // region Atualizando a lista de pedido
                    _adpPedidoMobile.notifyDataSetChanged();
                    // endregion

                }

                break;

        }
        // endregion

    }
    //endregion


    // region initialize
    @Override
    public void initialize() {

        // region Limpando os totalizadoes da activity
        _txtRodape.setText("REGISTROS: 0");
        // endregion


        // region Cuidando da apresentação do filtro para o usuário
        _IsShowFilter = true;

        refreshFilterContent();
        // endregion


        // region Criando as opções do usuário
        this.loadItemOptions();
        // endregion

        // region Montando o item TODAS AS CIDADES
        if (_lstCidade == null) {

            tpCidade _tpCidade = new tpCidade();
            _tpCidade._id = 0;
            _tpCidade.IdCidade = 0;
            _tpCidade.Descricao = "TODAS AS CIDADES";
            _tpCidade.EstadoSigla = "";

            _lstCidade = new ArrayList<tpCidade>();
            _lstCidade.add(_tpCidade);

        }
        // endregion


        // region Carregando a lista de pedidos
        this.loadPedido();
        // endregion


        // region Adicionando CIDADES onde existe pedido
        this.loadCidade();
        this.refreshCidade();
        // endregion


        // region Atualizando a lista de pedidos
        if ((_lstPedidoMobile != null) && (_lstPedidoMobile.size() > 0)) {


            this.filterPedido();
            this.refreshPedido();
        }
        // endregion

    }
    // endregion


    // region loadPedido
    private void loadPedido() {

        // region Declarando objetos
        SQLiteHelper _sqh = null;

        dbPedidoMobile _dbPedidoMobile = null;
        dbEmpresa _dbEmpresa = null;
        dbTipoPedido _dbTipoPedido = null;
        dbCliente _dbCliente = null;
        dbCidade _dbCidade = null;
        dbTabelaPreco _dbTabelaPreco = null;
        dbCondicaoPagamento _dbCondicaoPagamento = null;
        dbTransportadora _dbTransportadora = null;
        dbVendedor _dbVendedor = null;
        dbParametro _dbParametro = null;
        // endregion

        try {

            // region Instanciando o objeto de bando e abrindo a conexão
            _sqh = new SQLiteHelper(PedidoListaActivity.this);
            _sqh.open(false);
            // endregion

            // region Instânciando os objetos para leitura do banco
            _dbPedidoMobile = new dbPedidoMobile(_sqh);
            _dbEmpresa = new dbEmpresa(_sqh);
            _dbTipoPedido = new dbTipoPedido(_sqh);
            _dbCliente = new dbCliente(_sqh);
            _dbCidade = new dbCidade(_sqh);
            _dbTabelaPreco = new dbTabelaPreco(_sqh);
            _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);
            _dbTransportadora = new dbTransportadora(_sqh);
            _dbVendedor = new dbVendedor(_sqh);
            _dbParametro = new dbParametro(_sqh);
            // endregion

            // region Lendo os parametros necessários para o envio do pedido
            _tpServidorRestIp = _dbParametro.getServidorRestIp();
            _tpVendedorCodigo = _dbParametro.getVendedorCodigo();
            // endregion

            // region Carregando o objeto lista de pedidos
            _lstPedidoMobile = (ArrayList<tpPedidoMobile>) _dbPedidoMobile.getList(tpPedidoMobile.class);

            // endregion

            // region Para cada pedido carregado vamos buscar no banco as informações...
            if (_lstPedidoMobile != null && _lstPedidoMobile.size() > 0) {

                for (tpPedidoMobile _tpPedidoMobile : _lstPedidoMobile) {

                    // carregando os dados da empresa
                    _tpPedidoMobile.Empresa = (tpEmpresa) _dbEmpresa.getBySourceId(_tpPedidoMobile.IdEmpresa);

                    // carregando os dados do tipo de pedido
                    _tpPedidoMobile.TipoPedido = (tpTipoPedido) _dbTipoPedido.getBySourceId(_tpPedidoMobile.IdTipoPedido);

                    // carregando os dados do cliente
                    _tpPedidoMobile.Cliente = (tpCliente) _dbCliente.getBySourceId(_tpPedidoMobile.IdCliente);

                    // carregando os dados de tabela de preço
                    _tpPedidoMobile.TabelaPreco = (tpTabelaPreco) _dbTabelaPreco.getBySourceId(_tpPedidoMobile.IdTabelaPreco);

                    // carregando os dados de condição de pagamento
                    _tpPedidoMobile.CondicaoPagamento = (tpCondicaoPagamento) _dbCondicaoPagamento.getBySourceId(_tpPedidoMobile.IdCondicaoPagamento);

                    // carregando os dados de tranportadora
                    _tpPedidoMobile.Transportadora = (tpTransportadora) _dbTransportadora.getBySourceId(_tpPedidoMobile.IdTransportadora);

                    // carregando os dados de vendedor
                    _tpPedidoMobile.Vendedor = (tpVendedor) _dbVendedor.getBySourceId(_tpPedidoMobile.IdVendedor);

                }

            }
            // endregion

            ValidarIntegridadeDosPedidos();

            // region Encerrando a conexão com o banco de dados
            _sqh.close();
            // endregion

        } catch (Exception e) {
            Log.e("PedidoListaActivity", "initialize -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region editPedido
    private void editPedido() {

        // region Verificano se o mesmo já está sincronizado
        if (_tpPedidoMobileSelected.EhSincronizado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    PedidoListaActivity.this,
                    "ATENÇÃO",
                    "Não é permitido alterar dados de um pedido que já foi sincronizado com a base de pedidos da empresa"
            );

            return;

        }
        // endregion

        if (MSVUtil.checkSinchronization(PedidoListaActivity.this) == false) {
            MSVMsgBox.showMsgBoxWarning(
                    PedidoListaActivity.this,
                    "Atenção, antes de realizar a venda é necessário sincronizar este dispositivo",
                    "Verifique se seu dispositivo está conectado na internet e então entre na opção de Sincronização no menu principal."
            );
        } else {
            // region Montando os parâmetros para invocar a tela de edição
            Bundle _extras = new Bundle();
            _extras.putInt(_KEY_METODO_EDICAO, _UPDATE_VALUE);
            _extras.putString(_KEY_SOURCE_ACTIVITY, _PEDIDO_LISTA_VALUE);
            _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
            _extras.putLong(_KEY_ID_CLIENTE, _tpPedidoMobileSelected.IdCliente);
            _extras.putLong(_KEY_ID_PEDIDO_MOBILE, _tpPedidoMobileSelected.IdPedidoMobile);
            // endregion

            // region Invocando a activity apropriada
            Intent _intent = new Intent(PedidoListaActivity.this, PedidoMobileEditarActivity.class);
            _intent.putExtras(_extras);

            startActivityForResult(_intent, _PEDIDO_REQUEST_CODE);
            // endregion
        }

    }
    // endregion


    // region confirmPedido
    private void confirmPedido(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = (tpPedidoMobile)_adpPedidoMobile.getItem(itemIndex);
        // endregion

        // region Verificano se o mesmo já está confirmado
        if (_tp.EhSincronizado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    PedidoListaActivity.this,
                    "ATENÇÃO",
                    "Este pedido já está sincronizado com a base de pedidos da empresa.\nConfirmação negada"
            );

            return;

        }
        // endregion

        // region Verificano se o mesmo já está confirmado
        if (_tp.EhConfirmado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    PedidoListaActivity.this,
                    "ATENÇÃO",
                    "Este pedido já está confirmado"
            );

            return;

        }
        // endregion

        // region Realizando a confirmação do pedido

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(PedidoListaActivity.this);

            dbPedidoMobile _db = new dbPedidoMobile(_sqh);
            _db.setConfirmedOrder(_tp.IdPedidoMobile);

            _sqh.close();

            _tp.EhConfirmado = 1;
            _adpPedidoMobile.notifyDataSetChanged();

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoListaActivity.this,
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
        final tpPedidoMobile _tp = (tpPedidoMobile) _adpPedidoMobile.getItem(itemIndex);
        // endregion

        // region Verificano se o mesmo já está sincronizado
        if (_tp.EhSincronizado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    PedidoListaActivity.this,
                    "ATENÇÃO",
                    "Este pedido já está sincronizado com a base de pedidos da empresa.\nSincronização negada"
            );

            return;

        }
        // endregion

        // region Verificano se o mesmo já está confirmado
        if (_tp.EhConfirmado == 0) {

            MSVMsgBox.showMsgBoxWarning(
                    PedidoListaActivity.this,
                    "ATENÇÃO",
                    "Não é permitido sincronizar um pedido que não está confirmado.\nPor favor edite o pedido e confirme o mesmo"
            );

            return;

        }
        // endregion

        // region verificando se existe conexão no aparelho
        if (MSVUtil.isConnected(PedidoListaActivity.this) == false) {

            MSVMsgBox.showMsgBoxError(
                    PedidoListaActivity.this,
                    "WI-FI | 3G",
                    "O seu dispostivo não está conectado a nenhuma rede wi-fi ou rede 3G"
            );

            return;

        }
        // endregion

        // region Selecionando os pedidos que serão sincronizados
        final ArrayList<Long> _lstPedidoMobileSync = new ArrayList<Long>();
        _lstPedidoMobileSync.add(_tp.IdPedidoMobile);
        // endregion

        // region Sincronizando os pedidos selecionados
        MSVMsgBox.showMsgBoxQuestion(
                PedidoListaActivity.this,
                "SINCRONIZAR PEDIDO",
                "Deseja realmente enviar este pedido para faturamento ?",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {

                            // region Mostrando a caixa de dialogo de espera
                            _wait = ProgressDialog.show(PedidoListaActivity.this, "Aguarde", "Enviando o pedido selecionado...");
                            // endregion

                            // region Invocando o método da API para envio dos pedidos
                            new PedidoApi(
                                    PedidoListaActivity.this,
                                    _tpServidorRestIp.ValorTexto,
                                    _tpVendedorCodigo.ValorTexto,
                                    _lstPedidoMobileSync,
                                    0,
                                    onTaskCompleteListner
                            ).execute();
                            // endregion


                            _tp.EhSincronizado = 1;
                            _adpPedidoMobile.notifyDataSetChanged();


                        }

                    }
                }
        );
        // endregion

    }
    // endregion


    // region compartilharPedido
    private void compartilharPedido(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = (tpPedidoMobile) _adpPedidoMobile.getItem(itemIndex);
        // endregion

        // region Montando os parâmetros de destino da activity
        Bundle _extras = new Bundle();
        _extras.putLong(_KEY_ID_PEDIDO_MOBILE, _tp.IdPedidoMobile);
        // endregion

        // region Invocando a activity de envio de email
        Intent _intent = new Intent(PedidoListaActivity.this, PedidoEmailEnviar.class);
        _intent.putExtras(_extras);

        startActivity(_intent);
        // endregion

    }
    // endregion


    // region deletePedido
    private void deletePedido(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = (tpPedidoMobile)_adpPedidoMobile.getItem(itemIndex);
        // endregion

        // region Verificano se o mesmo já está sincronizado
        if (_tp.EhSincronizado == 1) {

            MSVMsgBox.showMsgBoxWarning(
                    PedidoListaActivity.this,
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
            _sqh = new SQLiteHelper(PedidoListaActivity.this);
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
            this.filterPedido();
            this.refreshPedido();


            // endregion

            // region Emitindo mensagem ao usuário
            Toast.makeText(
                    PedidoListaActivity.this,
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
                    PedidoListaActivity.this,
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


    // region salvarFiles
    private void salvarFiles(int itemIndex) {

        // region Lendo o objeto do pedido selecionado
        tpPedidoMobile _tp = (tpPedidoMobile)_adpPedidoMobile.getItem(itemIndex);
        // endregion


        // region Verificando ou solicitando permissão do usuário para gravar na memória
        if (ContextCompat.checkSelfPermission(PedidoListaActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(PedidoListaActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1234);

        }
        // endregion


        // region Gerando o arquivo JSON no diretório DOCUMENTS do dispositivo
        SQLiteHelper _sqh = null;
        String _jsonName = null;
        String _htmlName = null;

        try {

            // region Realizando a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoListaActivity.this);
            _sqh.open(false);
            // endregion

            // region Lendo os dados do pedido e escrevendo o json na pasta
            dbPedidoMobile _db = new dbPedidoMobile(_sqh);
            _jsonName = _db.saveToJson(_tp.IdPedidoMobile);
            _htmlName = _db.saveToHtml(_tp.IdPedidoMobile);
            // endregion

            // region Avisando o usuário sobre o término da operação solicitada
            MSVMsgBox.showMsgBoxInfo(
                    PedidoListaActivity.this,
                    "Arquivo gerado com sucesso !!!",
                    "Os arquivos foram gerados nos seguintes diretórios respectivamente documents/msvsmart/json " + _jsonName + " e documents/msvsmart/html " + _htmlName
            );
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    PedidoListaActivity.this,
                    "Erro ao gerar o arquivo texto do pedido",
                    e.getMessage()
            );

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }
        // endregion

    }
    // endregion


    // region filterPedido
    private void filterPedido() {

        // region Selecionando o identificador do registro da cidade
        long _id = -1;

        if (_lstCidade != null && _lstCidade.size() > 0) {
            _id = _lstCidade.get(_iCidade).IdCidade;
        }
        // endregion

        // region Inicializando a lista de pedidos filtrados
        if (_lstPedidoMobileFlt == null) {
            _lstPedidoMobileFlt = new ArrayList<tpPedidoMobile>();
        } else {
            _lstPedidoMobileFlt.clear();
        }
        // endregion

        // region Filtrando os pedidos
        boolean _flt1 = false;
        boolean _flt2 = false;

        if (_lstPedidoMobile != null && _lstPedidoMobile.size() > 0) {

            for (tpPedidoMobile _tpPedidoMobile : _lstPedidoMobile) {

                _flt1 = false;
                _flt2 = false;

                if (_iCidade == 0) {
                    _flt1 = true;
                } else {
                    if (_tpPedidoMobile.Cliente.IdCidade == _id) {
                        _flt1 = true;
                    }
                }

                if (_swiHideSync.isChecked() == false) {
                    _flt2 = true;
                } else {
                    if (_tpPedidoMobile.EhSincronizado == 0) {
                        _flt2 = true;
                    }
                }

                if (_flt1 == true && _flt2 == true) {
                    _lstPedidoMobileFlt.add(_tpPedidoMobile);
                }

            }

        }
        // endregion

    }
    // endregion


    // region refreshPedido
    private void refreshPedido() {

        _txtRodape.setText("REGISTRO: 0");

        try {

            if ((_lstPedidoMobileFlt != null)) {

                _adpPedidoMobile = new PedidoListaAdapter(
                        PedidoListaActivity.this,
                        _lstPedidoMobileFlt
                );

                _livPedido.setAdapter(_adpPedidoMobile);

                _txtRodape.setText("REGISTROS: " + String.valueOf(_lstPedidoMobileFlt.size()));
            }

        } catch (Exception e) {
            Log.e("PedidoListaActivity", "show -> " + e.getMessage());
            e.printStackTrace();
        }

    }
    // endregion


    // region loadCidade
    private void loadCidade() {

        SQLiteHelper _sqh = null;
        dbCidade _dbCidade = null;

        try {

            // region 01 Abrindo a conexão com o banco
            _sqh = new SQLiteHelper(PedidoListaActivity.this);
            _sqh.open(false);
            // endregion

            // region 02 Instânciando o objeto dbCidade
            _dbCidade = new dbCidade(_sqh);
            // endregion

            // region 03 Carregando as cidades onde tem pedido registrado
            if (_lstPedidoMobile != null && _lstPedidoMobile.size() > 0) {

                for (tpPedidoMobile _tpPedidoMobile : _lstPedidoMobile) {

                    boolean _achou = false;

                    for (tpCidade _tpCidade : _lstCidade) {

                        if (_tpPedidoMobile.Cliente.IdCidade == _tpCidade.IdCidade) {
                            _achou = true;
                            break;
                        }

                    }

                    if (_achou == false) {

                        SQLClauseHelper _sch = new SQLClauseHelper();
                        _sch.addEqualInteger("IdCidade", _tpPedidoMobile.Cliente.IdCidade);

                        _lstCidade.add((tpCidade) _dbCidade.getOne(_sch));

                    }
                }

            }
            // endregion

            // region 04 Atualizando o índice de região selecionada
            _iCidade = -1;

            if (_lstCidade != null && _lstCidade.size() > 0) {
                _iCidade = 0;
            }
            // endregion

        } catch (Exception e) {
            Toast.makeText(
                    PedidoListaActivity.this,
                    "Erro ao carregar os dados das cidades: " + e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region refreshCidade
    private void refreshCidade() {

        if (_iCidade == -1) {

            Toast.makeText(
                    PedidoListaActivity.this,
                    "Não existem cidades registradas do banco de dados",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            _txtCidade.setText(_lstCidade.get(_iCidade).Descricao);

        }

    }
    // endregion


    // region onTaskCompleteListner
    private OnTaskCompleteListner onTaskCompleteListner = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

            // region Acoes que serão executadas quando o status for Error
            if (status == eTaskCompleteStatus.ERROR) {

                _wait.dismiss();
                _wait = null;

                Toast.makeText(
                        PedidoListaActivity.this,
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
                _wait = null;

                if (out == 1) {

                    Toast.makeText(
                            PedidoListaActivity.this,
                            "Pedido sincronizado com sucesso mas não confirmado no dispositivo !!!",
                            Toast.LENGTH_SHORT
                    ).show();

                }

                if (out == 2) {

                    Toast.makeText(
                            PedidoListaActivity.this,
                            "Pedido sincronizado com sucesso !!!",
                            Toast.LENGTH_SHORT
                    ).show();

                    loadPedido();
                    filterPedido();
                    refreshPedido();

                }

                if (out == 3) {

                    Toast.makeText(
                            PedidoListaActivity.this,
                            "Pedido já sincronizado com a empresa !!!",
                            Toast.LENGTH_SHORT
                    ).show();

                    loadPedido();
                    filterPedido();
                    refreshPedido();

                }

            }
            // endregion

        }
    };
    // endregion


    // region syncronize
    private void syncronize() {

        // region Verificando a necessidade de instânciar a lista de pedidos filtrados
        if (_lstPedidoMobileFlt == null) {
            _lstPedidoMobileFlt = new ArrayList<tpPedidoMobile>();
        }
        // endregion


        // region Verificando se existem pedidos para sincronização
        if (_lstPedidoMobileFlt.size() == 0) {

            // region Imprimindo mensagem para o usuário
            MSVMsgBox.showMsgBoxInfo(
                    PedidoListaActivity.this,
                    "SINCRONIZAR PEDIDOS",
                    "Não exitem pedidos aguardando sincronização"
            );
            // endregion

            return;
        }
        // endregion


        // region verificando se existe conexão no aparelho
        if (MSVUtil.isConnected(PedidoListaActivity.this) == false) {

            MSVMsgBox.showMsgBoxError(
                    PedidoListaActivity.this,
                    "WI-FI | 3G",
                    "O seu dispostivo não está conectado a nenhuma rede wi-fi ou rede 3G"
            );

            return;

        }
        // endregion

        // region Selecionando os pedidos que serão sincronizados
        final ArrayList<Long> _lstPedidoMobileSync = new ArrayList<Long>();

        for (tpPedidoMobile _tp : _lstPedidoMobileFlt) {

            if (_tp.EhSincronizado == 0) {
                _lstPedidoMobileSync.add(_tp.IdPedidoMobile);
            }

        }
        // endregion

        // region Sincronizando os pedidos selecionados
        if (_lstPedidoMobileSync.size() == 0) {

            // region Imprimindo mensagem para o usuário
            MSVMsgBox.showMsgBoxInfo(
                    PedidoListaActivity.this,
                    "SINCRONIZAR PEDIDOS",
                    "Não exite pedido aguardando sincronização para o filtro de cidade realizado"
            );
            // endregion

            // region Impedindo a continuidade da execução
            return;
            // endregion

        } else {

            // region Verificando se o usuário quer realmente sincronizar os pedidos
            MSVMsgBox.showMsgBoxQuestion(
                    PedidoListaActivity.this,
                    "SINCRONIZAR PEDIDOS",
                    "Deseja realmente sincronizar os " + String.valueOf(_lstPedidoMobileSync.size()) + " não enviados para faturamento ?",
                    new OnCloseDialog() {
                        @Override
                        public void onCloseDialog(boolean isOk, String value) {

                            if (isOk) {

                                // region Mostrando a caixa de dialogo de espera
                                _wait = ProgressDialog.show(PedidoListaActivity.this, "Aguarde", "Enviando os pedidos selecionados...");
                                // endregion

                                // region Invocando o método da API para envio dos pedidos
                                new PedidoApi(
                                        PedidoListaActivity.this,
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

    }
    // endregion


    // region refreshFilterContent
    private void refreshFilterContent() {

        if (_IsShowFilter) {
            // mostrar o filtro
            _pnlFiltroCnt.setVisibility(View.VISIBLE);
            _txtTitulo.setText("( - ) ESCONDER FILTRO");
        } else {
            // esconder o filtro
            _pnlFiltroCnt.setVisibility(View.GONE);
            _txtTitulo.setText("( + ) MOSTRAR FILTRO");
        }

    }
    // endregion


    // region loadItemOptions
    private void loadItemOptions() {

        // region Criando a opção de EDITAR
        tpKeyValueRow _tpEditar = new tpKeyValueRow();
        _tpEditar.Key = 0;
        _tpEditar.Value = "EDITAR";
        _tpEditar.ImageResource = R.drawable.msv_small_edit_black;
        // endregion

        // region Criando a opção de CONFIRMAR PEDIDO
        tpKeyValueRow _tpConfirmar = new tpKeyValueRow();
        _tpConfirmar.Key = 1;
        _tpConfirmar.Value = "CONFIRMAR";
        _tpConfirmar.ImageResource = R.drawable.msv_small_checked_black;
        // endregion

        // region Criando a opção de SINCRONIZACAO
        tpKeyValueRow _tpSincronizar = new tpKeyValueRow();
        _tpSincronizar.Key = 2;
        _tpSincronizar.Value = "SINCRONIZAR";
        _tpSincronizar.ImageResource = R.drawable.msv_small_synchronize_black;
        // endregion

        // region Criando a opção de COMPARTILHAR
        tpKeyValueRow _tpCompartilhar = new tpKeyValueRow();
        _tpCompartilhar.Value = "COMPARTILHAR";
        _tpCompartilhar.Key = 3;
        _tpCompartilhar.ImageResource = R.drawable.msv_small_share_black;
        // endregion

        // region Criando a opção de EXCLUIR
        tpKeyValueRow _tpExcluir = new tpKeyValueRow();
        _tpExcluir.Key = 4;
        _tpExcluir.Value = "EXCLUIR";
        _tpExcluir.ImageResource = R.drawable.msv_small_trash_black;
        // endregion

        // region Criando a opção de SALVAR TEXTO
        tpKeyValueRow _tpSalvarTexto = new tpKeyValueRow();
        _tpSalvarTexto.Key = 5;
        _tpSalvarTexto.Value = "SALVAR NO DIRETÓRIO";
        _tpSalvarTexto.ImageResource = R.drawable.msv_small_save_black;
        // endregion


        // region Adicionando as opções na lista
        if (_lstItemOptions == null) {
            _lstItemOptions = new ArrayList<tpKeyValueRow>();
        }

        _lstItemOptions.add(_tpEditar);
        _lstItemOptions.add(_tpConfirmar);
        _lstItemOptions.add(_tpSincronizar);
        _lstItemOptions.add(_tpCompartilhar);
        _lstItemOptions.add(_tpExcluir);
        _lstItemOptions.add(_tpSalvarTexto);
        // endregion

    }
    // endregion


    //region ValidarIntegridadeDosPedidos
    private void ValidarIntegridadeDosPedidos()
    {

        ArrayList<tpPedidoMobile> _lstPedidoMobileToRemove = new ArrayList<tpPedidoMobile>();

        if (_lstPedidoMobile != null && _lstPedidoMobile.size() > 0) {

            for (tpPedidoMobile _tpPedidoMobile : _lstPedidoMobile) {

                boolean EhValido = true;

                //Validando integridada com a tabela Empresa
                if (_tpPedidoMobile.Empresa == null) {
                    EhValido = false;
                }

                //Validando integridada com a tabela TipoPedido
                if (_tpPedidoMobile.TipoPedido == null) {
                    EhValido = false;
                }

                //Validando integridada com a tabela TabelaPreco
                if (_tpPedidoMobile.TabelaPreco == null) {
                    EhValido = false;
                }

                //Validando integridada com a tabela CondicaoPagamento
                if (_tpPedidoMobile.CondicaoPagamento == null) {
                    EhValido = false;
                }


                //Validando integridada com a tabela Cliente
                if (_tpPedidoMobile.Cliente == null) {
                    EhValido = false;
                }


                //Validando integridada com a tabela Vendedor
                if (_tpPedidoMobile.Vendedor == null) {
                    EhValido = false;
                }


                if (EhValido == false) {
                    _lstPedidoMobileToRemove.add(_tpPedidoMobile);
                }

            }

        }

        _lstPedidoMobile.removeAll(_lstPedidoMobileToRemove);

    }
    //endregion


    // region showResumo
    private void showSumary() {

        Bundle _extras = new Bundle();
        _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);

        Intent _intent = new Intent(PedidoListaActivity.this, TipoPedidoTotalGrupoActivity.class);
        _intent.putExtras(_extras);

        startActivity(_intent);


    }
    // endregion

}
