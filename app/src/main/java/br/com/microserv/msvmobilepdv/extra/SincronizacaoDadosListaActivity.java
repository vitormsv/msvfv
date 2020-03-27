package br.com.microserv.msvmobilepdv.extra;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvapi.CidadeApi;
import br.com.microserv.framework.msvapi.ClienteApi;
import br.com.microserv.framework.msvapi.ClientePutApi;
import br.com.microserv.framework.msvapi.CondicaoPagamentoApi;
import br.com.microserv.framework.msvapi.EmpresaApi;
import br.com.microserv.framework.msvapi.GrupoApi;
import br.com.microserv.framework.msvapi.LinhaApi;
import br.com.microserv.framework.msvapi.ParametroApi;
import br.com.microserv.framework.msvapi.RobinHoodApi;
import br.com.microserv.framework.msvapi.TipoPedidoApi;
import br.com.microserv.framework.msvapi.ProdutoApi;
import br.com.microserv.framework.msvapi.TabelaPrecoApi;
import br.com.microserv.framework.msvapi.TransportadoraApi;
import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdal.dbPedidoMobile;
import br.com.microserv.framework.msvdal.dbPedidoMobileItem;
import br.com.microserv.framework.msvdal.dbSincronizacao;
import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpKeyValueRow;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpSincronizacao;
import br.com.microserv.framework.msvdto.tpSincronizacaoItem;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.AsyncTaskResult;
import br.com.microserv.framework.msvutil.MSVGPSTracker;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.SincronizacaoDadosListaAdapter;

public class SincronizacaoDadosListaActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando variaveis para as views da activity

    // LienarLayout
    LinearLayout _llyTitulo = null;
    LinearLayout _pnlMensagemCnt = null;
    LinearLayout _llyItens = null;
    LinearLayout _llyOpcoes = null;

    // ListView
    ListView _lvItens = null;

    // TextView
    TextView _txtTitulo = null;
    TextView _txtMensagem = null;

    // Button
    Button _btnIniciar = null;
    Button _btnFechar = null;

    // endregion


    // region Declarando as variáveis locais da activity

    // Objetos
    tpParametro _tpServidorRestIp = null;
    tpParametro _tpVendedorCodigo = null;
    tpSincronizacao _tpSincronizacao = null;
    tpVendedor _tpVendedor = null;
    SincronizacaoDadosListaAdapter _adp = null;
    ArrayList<tpSincronizacaoItem> _lstItens = null;
    ArrayList<tpPedidoMobile> _lstPedidoMobileNaoSincronizado = null;
    MSVGPSTracker _gpsTracker = null;
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacao_dados_lista);
        // endregion

        // region Adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);
        // endregion

        // region Invocando os métodos da interface
        this.bindElements();
        this.bindEvents();

        this.initialize();
        // endregion

    }
    // endregion


    // region onOptionItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // LienarLayout
        _llyTitulo = (LinearLayout) findViewById(R.id.llyTitulo);
        _pnlMensagemCnt = (LinearLayout) findViewById(R.id.pnlMensagemCnt);
        _llyItens = (LinearLayout) findViewById(R.id.llyItens);
        _llyOpcoes = (LinearLayout) findViewById(R.id.llyOpcoes);

        // ListView
        _lvItens = (ListView) findViewById(R.id.lvItens);

        // TextView
        _txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        _txtMensagem = (TextView) findViewById(R.id.txtMensagem);

        // Button
        _btnIniciar = (Button) findViewById(R.id.btnIniciar);
        _btnFechar = (Button) findViewById(R.id.btnFechar);

    }
    // endregion


    // region onTaskCompleteListner
    private OnTaskCompleteListner onTaskCompleteListner = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

            tpSincronizacaoItem _tp = null;


            // region Acoes que serão executadas quando o status for Error
            if (status == eTaskCompleteStatus.ERROR) {
                Toast.makeText(
                        SincronizacaoDadosListaActivity.this,
                        "Erro ao realizar a sincronização",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion


            // region Acoes que serão executadas quando o status for refresh
            if (status == eTaskCompleteStatus.REFRESH) {

                switch (index) {

                    // region Empresa
                    case 0:
                        break;
                    // endregion

                    // region Linha de produtos
                    case 1:
                        break;
                    // endregion

                    // region Grupo de produtos
                    case 2:
                        break;
                    // endregion

                    // region Tipos de pedido
                    case 3:
                        break;
                    // endregion

                    // region Condicao de pagamento
                    case 4:
                        break;
                    // endregion

                    // region Transportadora
                    case 5:
                        break;
                    // endregion

                    // region Tabela de Preco
                    case 6:
                        break;
                    // endregion

                    // region Produto
                    case 7:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = false;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        break;
                    // endregion

                    // region Cidade
                    case 8:
                        break;
                    // endregion

                    // region Cliente
                    case 9:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = false;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        break;
                    // endregion

                    // region Parametros
                    case 10:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = false;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        break;
                    // endregion

                    // region RobinHood
                    case 11:
                        break;
                    // endregion

                }

            }
            // endregion


            // region Acoes que serão executadas quando o status for sucesso
            if (status == eTaskCompleteStatus.SUCCESS) {

                switch (index) {

                    // region Linha
                    case 0:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new LinhaApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                1,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Grupo Produto
                    case 1:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new GrupoApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                2,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Tipo de Pedido
                    case 2:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new TipoPedidoApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                3,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Condicao de Pagamento
                    case 3:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new CondicaoPagamentoApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                4,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Transportadora
                    case 4:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new TransportadoraApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                5,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Tabela de Preco
                    case 5:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new TabelaPrecoApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                6,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Produto
                    case 6:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new ProdutoApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                7,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Cidade
                    case 7:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new CidadeApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                8,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Cliente
                    case 8:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new ClienteApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                9,
                                EmpresaApi.IDEMPRESA,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region Parametro
                    case 9:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new ParametroApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                10,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region RobinHood
                    case 10:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();

                        new RobinHoodApi(
                                SincronizacaoDadosListaActivity.this,
                                _tpServidorRestIp.ValorTexto,
                                _tpVendedorCodigo.ValorTexto,
                                11,
                                onTaskCompleteListner
                        ).execute();

                        break;
                    // endregion

                    // region RobinHood
                    case 11:

                        _tp = _lstItens.get(index);

                        _tp.Registro = out;
                        _tp.Ok = true;

                        ((SincronizacaoDadosListaAdapter) _lvItens.getAdapter()).notifyDataSetChanged();


                        try {

                            // region Finalizando e gravando o log de sincronização
                            if (_gpsTracker == null) {
                                _gpsTracker = new MSVGPSTracker(SincronizacaoDadosListaActivity.this);
                            }

                            _tpSincronizacao.TerminoDataHora = MSVUtil.sqliteHojeHora();
                            _tpSincronizacao.Latitude = _gpsTracker.getLatitude();
                            _tpSincronizacao.Longitude = _gpsTracker.getLongitude();

                            SQLiteHelper _sqlHelper = new SQLiteHelper(SincronizacaoDadosListaActivity.this);
                            _sqlHelper.open(true);

                            dbSincronizacao _dbSincronizacao = new dbSincronizacao(_sqlHelper);
                            _dbSincronizacao.insert(_tpSincronizacao);

                            _sqlHelper.close();
                            _sqlHelper = null;
                            // endregion


                            // region Enviando mensagem ao usuário
                            MSVMsgBox.showMsgBoxInfo(
                                    SincronizacaoDadosListaActivity.this,
                                    "Sincronização realizada com sucesso !!!",
                                    "Boas Vendas"
                            );
                            // endregion

                        } catch (Exception e) {

                            // region Emitindo mensagem de erro ao usuário
                            MSVMsgBox.showMsgBoxError(
                                    SincronizacaoDadosListaActivity.this,
                                    "Erro ao finalizar a sincronização de informações",
                                    e.getMessage()
                            );
                            // endregion

                        }

                        // ativando o botão fechar
                        _btnFechar.setVisibility(View.VISIBLE);

                        break;
                    // endregion

                }

            }
            // endregion

        }
    };
    // endregion

    // region onTaskCompleteListner_ClientePut
    private OnTaskCompleteListner onTaskCompleteListner_ClientePut = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

            // region Acoes que serão executadas quando o status for sucesso
            if (status == eTaskCompleteStatus.SUCCESS) {

                Toast.makeText(
                        SincronizacaoDadosListaActivity.this,
                        "Clientes sincronizados com sucesso.",
                        Toast.LENGTH_LONG
                ).show();

            }
            // endregion

        }
    };
    // endregion

    // region bindEvents
    @Override
    public void bindEvents() {

        // region Click em _btnIniciar
        _btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MSVUtil.isConnected(SincronizacaoDadosListaActivity.this) == false) {

                    MSVMsgBox.showMsgBoxError(
                            SincronizacaoDadosListaActivity.this,
                            "Não existe conexão disponível no momento",
                            "O seu dispostivo não está conectado a nenhuma rede wi-fi ou rede 3G"
                    );

                    return;

                }


                MSVMsgBox.showMsgBoxQuestion(
                        SincronizacaoDadosListaActivity.this,
                        "Deseja realmente iniciar a sincronização deste dispositivo ?",
                        "Se clicar em OK os dados atuais do aparelho serão removidos e uma nova carga de dados será carregada",
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {

                                    // region Escondento o botão de início
                                    _btnIniciar.setVisibility(View.GONE);
                                    // endregion

                                    Sincronizar();

                                }

                            }
                        }
                );

            }
        });
        // endregion


        // region Click em _btnFechar
        _btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // endregion

    }
    // endregion


    // region initialize
    public void initialize() {

        // region Cuidando de alguns elementos da activity

        // painel de mensagem
        _pnlMensagemCnt.setVisibility(View.GONE);

        // painel dos itens
        _llyItens.setVisibility(View.GONE);
        _llyOpcoes.setVisibility(View.GONE);

        // botões de ação da activity
        _btnIniciar.setVisibility(View.VISIBLE);
        _btnFechar.setVisibility(View.GONE);

        // endregion


        // region Lendo informações do banco de dados
        this.loadParametros();
        this.loadPedidoNaoSincronizado();
        this.loadVendedor();
        // endregion


        this.sendClientesBeforeSync();


        // region Verificando o que será apresentado ao usuário

        // Se existir pedido ainda a serem sincronizados o sistema não irá
        // permitir que o usuário final realize o sincronismo de informações,
        // isso por na sincronização o usuário irá perder todos os pedidos
        // que já foram feitos

        if (_lstPedidoMobileNaoSincronizado == null) {

            // montando os itens de sincronização
            buildItens();

            // mostrando na tela os paineis corretos
            _llyItens.setVisibility(View.VISIBLE);
            _llyOpcoes.setVisibility(View.VISIBLE);

        } else {

            StringBuilder _sbMsg = new StringBuilder();
            _sbMsg.append("Não é permitido realizar a sincronização dos dados pois existem pedido ainda não enviados");
            _sbMsg.append("\n");
            _sbMsg.append("Realize o envido dos pedidos pendentes antes de buscar as informações atualizadas na empresa");

            _txtMensagem.setText(_sbMsg.toString());

            _pnlMensagemCnt.setVisibility(View.VISIBLE);

        }

        // endregion

    }
    // endregion


    // region loadParametros
    private void loadParametros() {

        SQLiteHelper _sqh = new SQLiteHelper(SincronizacaoDadosListaActivity.this);

        try {

            _sqh.open(false);

            dbParametro _dbParametro = new dbParametro(_sqh);
            _tpServidorRestIp = _dbParametro.getServidorRestIp();
            _tpVendedorCodigo = _dbParametro.getVendedorCodigo();

            _sqh.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion

    //region Sincronizar
    private void Sincronizar()
    {
        // region Gerando histórico e excluindo os pedidos sincronizados
        deleteAndSaveHistorySinc();
        // endregion

        // region Inicializando o objeto log de sincronização
        if (_tpSincronizacao == null) {
            _tpSincronizacao = new tpSincronizacao();
            _tpSincronizacao.InicioDataHora = MSVUtil.sqliteHojeHora();
            _tpSincronizacao.IdVendedor = _tpVendedor.IdVendedor;
        }
        // endregion

        // region Invocando o método da API para o início da sincronização
        new EmpresaApi(
                SincronizacaoDadosListaActivity.this,
                _tpServidorRestIp.ValorTexto,
                _tpVendedorCodigo.ValorTexto,
                0,
                onTaskCompleteListner
        ).execute();
        // endregion

    }
    //endregion

    // region loadPedidoNaoSincronizado
    private void loadPedidoNaoSincronizado() {

        // region Declarando as variávies locais
        SQLiteHelper _sqh = null;
        // endregion

        // region Bloco protegido
        try {

            // region 01 - Criando WHERE para os pedidos não sincronizados
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("EhSincronizado", 0);
            // endregion

            // region 02 - Instânciando a conexão com o banco de dados
            _sqh = new SQLiteHelper(SincronizacaoDadosListaActivity.this);
            _sqh.open(false);
            // endregion

            // region 03 - Carregando a listsa de pedidos não sincronizados
            dbPedidoMobile _dbPedidoMobile = new dbPedidoMobile(_sqh);
            _lstPedidoMobileNaoSincronizado = (ArrayList<tpPedidoMobile>) _dbPedidoMobile.getList(tpPedidoMobile.class, _sch);
            // endregion

        } catch (Exception e) {
            Toast.makeText(
                    SincronizacaoDadosListaActivity.this,
                    "Erro ao carregar os dados dos pedidos não sincronizados " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }
        // endregion

    }
    // endregion


    // region buildItems
    private void buildItens() {

        // region Criando os itens de sincronização
        tpSincronizacaoItem _tp0 = new tpSincronizacaoItem(0, "Empresa");
        tpSincronizacaoItem _tp1 = new tpSincronizacaoItem(1, "Linha de produto");
        tpSincronizacaoItem _tp2 = new tpSincronizacaoItem(2, "Grupo de produto");
        tpSincronizacaoItem _tp3 = new tpSincronizacaoItem(3, "Tipo de pedido");
        tpSincronizacaoItem _tp4 = new tpSincronizacaoItem(4, "Condicao de pagamento");
        tpSincronizacaoItem _tp5 = new tpSincronizacaoItem(5, "Transportadora");
        tpSincronizacaoItem _tp6 = new tpSincronizacaoItem(6, "Tabela de preço");
        tpSincronizacaoItem _tp7 = new tpSincronizacaoItem(7, "Produto");
        tpSincronizacaoItem _tp8 = new tpSincronizacaoItem(8, "Cidade");
        tpSincronizacaoItem _tp9 = new tpSincronizacaoItem(9, "Cliente");
        tpSincronizacaoItem _tp10 = new tpSincronizacaoItem(10, "Parametros");
        tpSincronizacaoItem _tp11 = new tpSincronizacaoItem(11, "RobinHood");

        /*
        tpSincronizacaoItem _tp9 = new tpSincronizacaoItem(9, "Cliente");
        tpSincronizacaoItem _tp10 = new tpSincronizacaoItem(10, "Tabela de preço");
        tpSincronizacaoItem _tp11 = new tpSincronizacaoItem(11, "Itens da tabela de preço");
        tpSincronizacaoItem _tp12 = new tpSincronizacaoItem(12, "Mix de produtos");
        tpSincronizacaoItem _tp13 = new tpSincronizacaoItem(13, "Financeiro");
        */
        // endregion

        // region Criando a lista e adicionando os itens
        _lstItens = new ArrayList<tpSincronizacaoItem>();
        _lstItens.add(_tp0);
        _lstItens.add(_tp1);
        _lstItens.add(_tp2);
        _lstItens.add(_tp3);
        _lstItens.add(_tp4);
        _lstItens.add(_tp5);
        _lstItens.add(_tp6);
        _lstItens.add(_tp7);
        _lstItens.add(_tp8);
        _lstItens.add(_tp9);
        _lstItens.add(_tp10);
        _lstItens.add(_tp11);
        /*
        _lstItens.add(_tp10);
        _lstItens.add(_tp11);
        _lstItens.add(_tp12);
        _lstItens.add(_tp13);
        */
        // endregion

        // region Realizando o bind dos elementos na lista
        _adp = new SincronizacaoDadosListaAdapter(SincronizacaoDadosListaActivity.this, _lstItens);
        _lvItens.setAdapter(_adp);
        // endregion

    }
    // endregion


    // region loadVendedor
    private void loadVendedor() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(SincronizacaoDadosListaActivity.this);
            _sqh.open(false);

            // buscando os dados do vendedor
            dbVendedor _dbVendedor = new dbVendedor(_sqh);
            _tpVendedor = (tpVendedor) _dbVendedor.getById(1);


        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    SincronizacaoDadosListaActivity.this,
                    "Erro ao realizar a leitura dos dados do vendedor",
                    e.getMessage()
            );

            finish();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion

    //region sendBeforeSync
    //Envia para o servidor as informações que precisam ser enviadas
    // antes da sincronização limpar o banco de dados do celular
    private void sendClientesBeforeSync() {

        try {

            SQLiteHelper _sqh = new SQLiteHelper(SincronizacaoDadosListaActivity.this);
            _sqh.open(false);

            List<tpCliente> _lstCliente = (new dbCliente(_sqh)).getClientesNaoSincronizados();

            if(_lstCliente.size() > 0)

                MSVMsgBox.showMsgBoxQuestion(
                        SincronizacaoDadosListaActivity.this,
                        "Sincronização",
                        "Deseja sincronizar os clientes que não foram sincronizados?",
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if(isOk)
                                {

                                    SQLiteHelper _sqh = new SQLiteHelper(SincronizacaoDadosListaActivity.this);
                                    _sqh.open(false);

                                    try {

                                        List<tpCliente> _lstCliente = (new dbCliente(_sqh)).getClientesNaoSincronizados();

                                    for (tpCliente _tpCliente : _lstCliente) {

                                        ClientePutApi _ClientePutApi = new ClientePutApi(
                                                SincronizacaoDadosListaActivity.this,
                                                _tpServidorRestIp.ValorTexto,
                                                _tpCliente,
                                                EmpresaApi.IDEMPRESA,
                                                onTaskCompleteListner_ClientePut);

                                        _ClientePutApi.execute();

                                    }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        _sqh.close();
                                    }
                                }

                            }

                        });


        } catch (Exception b) {

            b.getLocalizedMessage();

        }

    }
    //endregion

    // region deleteAndSaveHistorySinc
    private boolean deleteAndSaveHistorySinc() {

        SQLiteHelper _sqh = null;
        dbPedidoMobile _dbPedidoMobile = null;
        dbPedidoMobileItem _dbPedidoMobileItem = null;
        tpPedidoMobile _tpPedidoMobile = null;

        boolean _out = true;
        ArrayList<tpKeyValueRow> _lstId = null;

        try {

            // region Iniciando a conexão com o banco de dados
            _sqh = new SQLiteHelper(SincronizacaoDadosListaActivity.this);
            _sqh.open(true);
            _sqh.initTransaction();
            // endregion

            // region Instanciando os objetos DB
            _dbPedidoMobile = new dbPedidoMobile(_sqh);
            _dbPedidoMobileItem = new dbPedidoMobileItem(_sqh);
            // endregion

            // region Buscando os identificadores internos dos pedidos sincronizados
            _lstId = _dbPedidoMobile.getIdSincronizados();
            // endregion

            // region Percorrendo a lista de pedidos encontrados para salvar o histórico e excluir o principal
            if (_lstId != null && _lstId.size() > 0) {

                for (tpKeyValueRow _item : _lstId) {

                    // region Salvando o histórico do pedido
                    _dbPedidoMobile.saveHistory(_item.Key);
                    // endregion

                    // region Buscando os dados completos do pedido para realizar a sua exclusão
                    _tpPedidoMobile = (tpPedidoMobile) _dbPedidoMobile.loadOneById(_item.Key);

                    if (_tpPedidoMobile.Itens != null) {

                        for (tpPedidoMobileItem _tpItem : _tpPedidoMobile.Itens) {
                            _dbPedidoMobileItem.delete(_tpItem);
                        }

                    }

                    _dbPedidoMobile.delete(_tpPedidoMobile);
                    // endregion

                }

            }
            // endregion

            // region Efetivando as movimentações no banco de dados
            _sqh.commitTransaction();
            _sqh.endTransaction();
            // endregion

        } catch (Exception e) {

            _sqh.endTransaction();
            _out = false;

            MSVMsgBox.showMsgBoxError(
                    SincronizacaoDadosListaActivity.this,
                    "Erro na rotina de exclusão e geração do histórico do pedido",
                    e.getMessage()
            );

        } finally {
            if (_sqh != null && _sqh.isOpen()) {
                _sqh.close();
            }
        }

        return _out;
    }
    // endregion

}
