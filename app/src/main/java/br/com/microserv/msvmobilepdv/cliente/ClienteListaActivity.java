package br.com.microserv.msvmobilepdv.cliente;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvdal.dbCidade;
import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdal.dbPedidoMobileHistorico;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteListaRow;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpKeyValueRow;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvdto.tpPedidoMobileHistorico;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvinterface.OnSelectedItem;
import br.com.microserv.framework.msvutil.MSVMaps;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.msvmobilepdv.pedido.PedidoMobileHistoricoActivity;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.CidadeDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.ClienteListaAdapter;
import br.com.microserv.msvmobilepdv.adapter.KeyValueDialogSearchAdapter;

public class ClienteListaActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Request
    static final int _CLIENTE_EDITAR_REQUEST_CODE = 100;
    static final int _CLIENTE_DETALHE_REQUEST_CODE = 101;

    // Key
    static final String _KEY_ID_PEDIDO_MOBILE_HISTORICO = "IdPedidoMobileHistorico";
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_ID_CLIENTE = "IdCliente";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_CLIENTE_id = "_id";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final String _CLIENTE_LISTA_VALUE = "ClienteListaActivity";
    static final String _CLIENTE_DETALHE_VALUE = "ClienteDetalheActivity";

    // endregion


    // region Declarando as variáveis que vinculam elementos do layout

    // TextView
    TextView _txtTitulo;
    TextView _txtNovo;
    TextView _txtCidade;
    TextView _txtCliente;
    TextView _txtRodape;

    // ListView
    ListView _livCliente;

    // LinearLayou
    LinearLayout _llyTitulo;
    LinearLayout _llyFiltroCnt;
    LinearLayout _llyCidade;
    LinearLayout _llyCliente;
    LinearLayout _llyRodape;

    // endregion


    // region Declarando outras variaveis de uso da activity

    // Objeto
    tpEmpresa _tpEmpresa = null;
    tpClienteListaRow _tpSelected = null;
    tpKeyValueRow _tpOption = null;
    tpParametro _tpPermiteAdicionarCliente = null;

    // Parcelable
    Parcelable _livClienteState = null;

    // Adapters
    KeyValueDialogSearchAdapter _adpKeyValue = null;
    ClienteListaAdapter _adpCliente = null;

    // ArrayList
    ArrayList<tpKeyValueRow> _lstItemOptions = null;
    ArrayList<tpCidade> _lstCidade = null;
    ArrayList<tpClienteListaRow> _lstCliente = null;
    ArrayList<tpClienteListaRow> _lstClienteFiltered = null;
    ArrayList<tpPedidoMobileHistorico> _lstPedidoMobileHistorico = null;

    // int
    int _iCidade = -1;
    Long _idCidadeShared = -1l;
    int _iSelected = 0;
    int _iAux = 0;

    // String
    String _sCliente = "";

    // boolean
    boolean _IsShowFilter;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // gerando o layout da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_lista);

        // adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);

        // region Selecionando os parametros enviados através do Bundle
        Bundle _extras = getIntent().getExtras();

        if (_extras != null) {

            // region _KEY_EMPRESA
            if (_extras.containsKey(_KEY_TP_EMPRESA)) {
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);

                if(_tpEmpresa.Descricao.toLowerCase().contains("distribuidora") && _tpEmpresa.Descricao.toLowerCase().contains("biazom")) {

                    _ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTitleBackground)));

                    _ab.setTitle(_ab.getTitle() + " - Distribuidora");

                }

            } else {
                Toast.makeText(
                        ClienteListaActivity.this,
                        "O parâmetro _KEY_ID_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        }
        // endregion


        // region Lendo as preferencias do usuário para esta activity
        _idCidadeShared = Long.parseLong(
                MSVUtil.readPreference(
                        ClienteListaActivity.this,
                        _tpEmpresa.Sigla,
                        "ID_CIDADE",
                        "0"
                )
        );
        // endregion

        // invocando os metodos da interface e de
        // inicializacao
        this.bindElements();
        this.bindEvents();
        this.initialize();

    }
    // endregion


    // region onSaveInstanceState
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // region Posição de listas
        _livClienteState = _livCliente.onSaveInstanceState();
        // endregion

        super.onSaveInstanceState(outState);
    }
    // endregion


    // region onOptionsItemSelected
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


    // region onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // region Invocando o metodo da classe base
        super.onActivityResult(requestCode, resultCode, data);
        // endregion

        // region Verificando qual foi a activity executada
        if (requestCode == _CLIENTE_EDITAR_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                this.loadCliente();
                this.filterCliente();

            }

        }
        if (requestCode == _CLIENTE_DETALHE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                this.loadCliente();
                this.filterCliente();

            }

        }



    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // TextView
        _txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        _txtNovo   = (TextView) findViewById(R.id.txtNovo);
        _txtCidade = (TextView) findViewById(R.id.txtCidade);
        _txtCliente = (TextView) findViewById(R.id.txtCliente);
        _txtRodape = (TextView) findViewById(R.id.txtRodape);

        // ListView
        _livCliente = (ListView) findViewById(R.id.livCliente);

        // LinearLayout
        _llyTitulo = (LinearLayout) findViewById(R.id.llyTitulo);
        _llyFiltroCnt = (LinearLayout) findViewById(R.id.llyFiltroCnt);
        _llyCidade = (LinearLayout) findViewById(R.id.llyCidade);
        _llyCliente = (LinearLayout) findViewById(R.id.llyCliente);
        _llyRodape = (LinearLayout) findViewById(R.id.llyRodape);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region _llyTitulo
        _llyTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _IsShowFilter = !_IsShowFilter;
                refreshFilterContent();
            }
        });
        // endregion


        // region _llyCidade
        _llyCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Criando o adaptador para a lista de cidades
                final CidadeDialogSearchAdapter _adp = new CidadeDialogSearchAdapter(ClienteListaActivity.this, _lstCidade);
                // endregion

                // region Inflando o layout customizado para o AlertDialog
                // inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);

                // bucando o elemento do título
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
                _txtDialogTitle.setText(_lstCidade.get(_iCidade).Descricao);

                // buscando o elemento ListView
                ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

                _lv.setAdapter(_adp);

                _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _iAux = position;
                        _txtDialogTitle.setText(_lstCidade.get(_iAux).Descricao);
                    }
                });
                // endregion

                // region Criando a janela modal AlertDialog
                final AlertDialog.Builder _builder = new AlertDialog.Builder(ClienteListaActivity.this);

                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // aqui vamos realizar o refresh do grupo e do produto
                        _iCidade = _iAux;

                        // region Salvando as preferencias do usuário
                        MSVUtil.savePreference(
                                ClienteListaActivity.this,
                                _tpEmpresa.Sigla,
                                "ID_CIDADE",
                                String.valueOf(_lstCidade.get(_iCidade).IdCidade)
                        );

                        refreshCidade();
                        loadCliente();
                    }
                });
                _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // não faz nada
                    }
                });

                AlertDialog _dialog = _builder.create();
                _dialog.show();
                // endregion

            }
        });
        // endregion


        // region _llyCliente
        _llyCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_valor, null);

                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
                _txtDialogTitle.setText("PESQUISAR PARTE");

                final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
                _txtDialogMessage.setText("Informe o valor de pesquisa ou deixe em branco para todos");

                final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
                _txtDialogCurrentValue.setText(_sCliente.isEmpty() ? "TODOS" : _sCliente.toUpperCase());

                final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
                _edtDialogNewValue.setText("");
                // endregion

                // region Criando a janela de dialogo
                AlertDialog.Builder _builder = new AlertDialog.Builder(ClienteListaActivity.this);
                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!_sCliente.equalsIgnoreCase(_edtDialogNewValue.getText().toString())) {

                            _sCliente = _edtDialogNewValue.getText().toString();

                            if (_sCliente.isEmpty()) {
                                _txtCliente.setText("TODOS");
                            } else {
                                _txtCliente.setText(_sCliente.toUpperCase());
                            }

                            filterCliente();

                        }

                    }
                });
                _builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog _dialog = _builder.create();
                _dialog.show();
                // endregion

            }
        });
        // endregion


        // region Novo cliente
        _txtNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVUtil.vibrate(ClienteListaActivity.this);

                // instânciando um objeto de parametro e preenchendo o mesmo
                Bundle _extras = new Bundle();
                _extras.putInt(_KEY_METODO_EDICAO, _INSERT_VALUE);
                _extras.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_LISTA_VALUE);
                _extras.putLong(_KEY_ID_CLIENTE, 0);
                _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);

                // invocando a nova activity
                Intent _i = new Intent(ClienteListaActivity.this, ClienteEditarActivity.class);
                _i.putExtras(_extras);
                startActivityForResult(_i, _CLIENTE_EDITAR_REQUEST_CODE);

            }
        });
        // endregion


        // region _livCliente
        _livCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // region Recuperando informações do item selecionado
                _iSelected = position;
                _tpSelected = (tpClienteListaRow) _livCliente.getAdapter().getItem(position);
                // endregion

                // region Invocando o método apropriado
                detailCliente();
                // endregion

            }
        });
        // endregion


        // region _livCliente (toque longo)
        _livCliente.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // region Recuperando informações do item selecionado
                _iSelected = position;
                _tpSelected = (tpClienteListaRow) _livCliente.getAdapter().getItem(position);
                // endregion


                // region Construindo as opções de contexto da activity
                buildItemOptions(_tpSelected.IdCliente);
                // endregion

                // region Verificando a necessidade de instânciar o adapter
                if (_adpKeyValue == null) {
                    _adpKeyValue = new KeyValueDialogSearchAdapter(
                            ClienteListaActivity.this,
                            _lstItemOptions
                    );
                }
                // endregion

                // region Montando a janela de escolha de opções
                _iAux = -1;
                _tpOption = null;

                MSVMsgBox.getValueFromList(
                        ClienteListaActivity.this,
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
                                                ClienteListaActivity.this,
                                                "Nenhuma opção foi selecionada pelo usuário",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        return;

                                    }
                                    // endregion

                                    // region Processando a opção do usuário

                                    // region EDITAR
                                    if (_tpOption.Key == 0) {
                                        editCliente();
                                        onActivityResult(102, -1, null);
                                    }
                                    // endregion

                                    // region DETALHES
                                    if (_tpOption.Key == 1) {
                                        detailCliente();
                                    }
                                    // endregion

                                    // region Navegar com WAZE
                                    if (_tpOption.Key == 2) {
                                        showOnWaze();
                                    }
                                    // endregion

                                    // region Navegar com GOOGLE MAPS
                                    if (_tpOption.Key == 3) {
                                        showOnGoogleMaps();
                                    }
                                    // endregion

                                    // region PEDIDO SELECIONADO
                                    if (_tpOption.Key > 3) {
                                        // region Selecionando o identificador do pedido selecionado
                                        // lembrando que aqui temos que descontar o valor 1000 da chave
                                        // selecionada, pois este mesmo valor foi adicionado no momento de gerar
                                        // as opoções dinâmicas para não conflitar com as opções fixas
                                        long _idPedidoMobileHistorico = _tpOption.Key - 1000;
                                        // region

                                        // region Gerando os parametros
                                        Bundle _extras = new Bundle();
                                        _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
                                        _extras.putLong(_KEY_ID_CLIENTE, _tpSelected.IdCliente);
                                        _extras.putLong(_KEY_ID_PEDIDO_MOBILE_HISTORICO, _idPedidoMobileHistorico);
                                        // endregion

                                        // region Invocando a tela de consulta do histórico do pedido
                                        Intent _intent = new Intent(ClienteListaActivity.this, PedidoMobileHistoricoActivity.class);
                                        _intent.putExtras(_extras);
                                        startActivity(_intent);
                                        // endregion
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


    // region buildItemOptions
    private void buildItemOptions(long idCliente) {

        // region Cuidando da instância da lista de itens
        if (_lstItemOptions == null) {
            _lstItemOptions = new ArrayList<tpKeyValueRow>();
        } else {
            _lstItemOptions.clear();
        }
        // endregion


        // region Criando a opção de EDITAR
        tpKeyValueRow _tpEditar = new tpKeyValueRow();
        _tpEditar.Key = 0;
        _tpEditar.Value = "EDITAR";
        _tpEditar.ImageResource = R.drawable.msv_small_edit_black;

        _lstItemOptions.add(_tpEditar);
        // endregion


        // region Criando a opção de DETALHES
        tpKeyValueRow _tpConfirmar = new tpKeyValueRow();
        _tpConfirmar.Key = 1;
        _tpConfirmar.Value = "DETALHES";
        _tpConfirmar.ImageResource = R.drawable.msv_small_customer_black;

        _lstItemOptions.add(_tpConfirmar);
        // endregion


        // region Criando a opção de NAVEGAR COM WAZE
        tpKeyValueRow _tpNavegarWaze = new tpKeyValueRow();
        _tpNavegarWaze.Key = 2;
        _tpNavegarWaze.Value = "NAVEGAR COM WAZE";
        _tpNavegarWaze.ImageResource = R.drawable.msv_small_map_maker_black;

        _lstItemOptions.add(_tpNavegarWaze);
        // endregion


        // region Criando a opção de NAVEGAR COM GOOGLE MAPS
        tpKeyValueRow _tpNavegarGoogleMaps = new tpKeyValueRow();
        _tpNavegarGoogleMaps.Key = 3;
        _tpNavegarGoogleMaps.Value = "NAVEGAR COM GOOGLE MAPS";
        _tpNavegarGoogleMaps.ImageResource = R.drawable.msv_small_map_maker_black;

        _lstItemOptions.add(_tpNavegarGoogleMaps);
        // endregion


        // region Criando os itens dinâmicos para os pedidos em histórico
        _lstPedidoMobileHistorico = this.loadPedidoMobileHistorico(idCliente);

        if (_lstPedidoMobileHistorico != null && _lstPedidoMobileHistorico.size() > 0) {
            for (tpPedidoMobileHistorico _tp : _lstPedidoMobileHistorico) {

                tpKeyValueRow _tpPedido = new tpKeyValueRow();
                _tpPedido.Key = 1000 + _tp.IdPedidoMobileHistorico;
                _tpPedido.Value = "PEDIDO " + _tp.Numero;
                _tpPedido.ImageResource = R.drawable.msv_small_paste;

                _lstItemOptions.add(_tpPedido);
            }
        }
        // endregion

    }
    // endregion


    // region initialize
    public void initialize() {

        // region Realizando a leitura dos parametros necessários
        this.loadParameters();
        // endregion

        // region Desativando/Escondento opções da activity de acordo com os parametros
        _txtNovo.setVisibility(View.GONE);

        if (_tpPermiteAdicionarCliente.ValorInteiro == 1) {
            _txtNovo.setVisibility(View.VISIBLE);
        }
        // endregion

        // region Cuidando da apresentação do filtro
        _IsShowFilter = true;
        refreshFilterContent();
        // endregion

        // region Cuidando da apresentação inicial do rodapé
        _txtRodape.setText("REGISTROS: 0");
        // endregion

        // region Carregando os dados do banco
        this.loadCidade();
        // endregion

        // region Emitindo mensagem ao usuário quando não existir cliente cadastrado
        boolean _existeCliente = false;

        if ((_lstCliente != null) && (_lstCliente.size() > 0)) {
            _existeCliente = true;
        }

        if (_existeCliente == false) {

            // escondendo o filtro
            _IsShowFilter = true;
            refreshFilterContent();

            // deixando o filtro desabilitado
            _llyTitulo.setEnabled(false);

            // emitindo mensagem ao usuário
            Toast.makeText(
                    ClienteListaActivity.this,
                    "Não existem clientes sincronizados no dispositivo",
                    Toast.LENGTH_SHORT
            ).show();
        }
        // endregion

    }
    // endregion


    // region loadParameters
    private void loadParameters() {

        SQLiteHelper _sqh = null;
        dbParametro _dbParametro = null;

        try {

            _sqh = new SQLiteHelper(ClienteListaActivity.this);
            _sqh.open(false);

            _dbParametro = new dbParametro(_sqh);

            _tpPermiteAdicionarCliente = _dbParametro.getPermiteAdicionarCliente();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }

    }
    // endregion


    // region loadCidade
    private void loadCidade() {

        _iCidade = -1;

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
        _sqh.open(false);

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addOrderBy("EstadoSigla", eSQLSortType.ASC);
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            dbCidade _dbCidade = new dbCidade(_sqh);
            _lstCidade = (ArrayList<tpCidade>) _dbCidade.getList(tpCidade.class, _sch);

            if (_lstCidade.size() != 0) {
                _iCidade = 0;

                // aplicando a preferencia do usuário para
                // a linha do produto
                if (_idCidadeShared != 0) {
                    for (int i = 0; i < _lstCidade.size(); i++) {
                        if (_lstCidade.get(i).IdCidade == _idCidadeShared) {
                            _iCidade = i;
                        }
                    }
                }

                this.refreshCidade();
                this.loadCliente();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region refreshCidade
    private void refreshCidade() {

        String _sCidade;

        if (_iCidade > -1) {
            _sCidade = String.format(
                    "%1s (%2s)",
                    _lstCidade.get(_iCidade).Descricao,
                    _lstCidade.get(_iCidade).EstadoSigla
            );
        } else {
            _sCidade = "NÃO EXISTE CIDADE";
        }

        _txtCidade.setText(_sCidade);

    }
    // endregion


    // region loadCliente
    private void loadCliente() {

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
        _sqh.open(false);

        try {

            dbCliente _dbCliente = new dbCliente(_sqh);
            _lstCliente = (ArrayList<tpClienteListaRow>) _dbCliente.getForClienteLista(_lstCidade.get(_iCidade).IdCidade);

                _lstClienteFiltered = new ArrayList<tpClienteListaRow>();

                for (tpClienteListaRow _tp : _lstCliente) {
                    _lstClienteFiltered.add(_tp);
                }

                this.refreshCliente();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region refreshCliente
    private void refreshCliente() {

        // atualizando o texto de pesquisa do filtro
        if (_sCliente.equalsIgnoreCase("")) {
            _txtCliente.setText("TODOS");
        } else {
            _txtCliente.setText(_sCliente.toUpperCase());
        }


        // atualizando a lista
        _adpCliente = new ClienteListaAdapter(ClienteListaActivity.this, _lstClienteFiltered);
        _livCliente.setAdapter(_adpCliente);

        if (_livClienteState != null) {
            _livCliente.onRestoreInstanceState(_livClienteState);
        }

        /*
        if (_adpCliente == null) {
            _adpCliente = new ClienteListaAdapter(ClienteListaActivity.this, _lstClienteFiltered);
            _livCliente.setAdapter(_adpCliente);
        } else {
            _adpCliente._items = _lstClienteFiltered;
            _adpCliente.notifyDataSetChanged();
        }
        */


        // atualiando a quantidade de registros
        _txtRodape.setText("REGISTROS: " + String.valueOf(_lstClienteFiltered.size()));

    }
    // endregion


    // region filterCliente
    public void filterCliente() {

        // region Removendo todos os itens da lista atual
        _lstClienteFiltered.clear();
        // endregion

        // region Aplicando o filtro de acordo com a expressao
        if (_sCliente.isEmpty()) {

            for(tpClienteListaRow _tp : _lstCliente) {
                _lstClienteFiltered.add(_tp);
            }

        } else {

            String _expr = _sCliente.toLowerCase();

            for (tpClienteListaRow _tp : _lstCliente) {

                String _p1 = _tp.NomeFantasia.toLowerCase();
                String _p2 = _tp.RazaoSocial.toLowerCase();
                String _p3 = _tp.Codigo.toLowerCase();

                if (_p1.contains(_expr) || _p2.contains(_expr) || _p3.contains(_expr)) {
                    _lstClienteFiltered.add(_tp);
                }
            }

        }
        // endregion

        // region invocando o método para atualizar a lista de produto
        this.refreshCliente();
        // endregion

    }
    // endregion


    // region editCliente
    private void editCliente() {

        // region Montando os parametros para envio a proxima activity
        Bundle _params = new Bundle();
        _params.putInt(_KEY_METODO_EDICAO, _UPDATE_VALUE);
        _params.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_LISTA_VALUE);
        _params.putLong(_KEY_ID_CLIENTE, _tpSelected.IdCliente);
        _params.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
        // endregion


        // region Invocando a proxima activity (ClienteOpcaoActivity)
        Intent _i = new Intent(ClienteListaActivity.this, ClienteEditarActivity.class);
        _i.putExtras(_params);
        startActivityForResult(_i, _CLIENTE_EDITAR_REQUEST_CODE);
        // endregion

    }
    // endregion


    // region detailCliente
    private void detailCliente() {

        // region Gerando os parametros
        Bundle _extras = new Bundle();
        _extras.putInt(_KEY_METODO_EDICAO, _UPDATE_VALUE);
        _extras.putString(_KEY_SOURCE_ACTIVITY, _CLIENTE_LISTA_VALUE);
        _extras.putLong(_KEY_ID_CLIENTE, _tpSelected.IdCliente);
        _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
        _extras.putLong(_KEY_CLIENTE_id, _tpSelected._id);
        // endregion

        // region Invocando a nova activity
        Intent _i = new Intent(ClienteListaActivity.this, ClienteDetalheActivity.class);
        _i.putExtras(_extras);
        startActivityForResult(_i, _CLIENTE_DETALHE_REQUEST_CODE);
        // endregion

    }
    // endregion


    // region refreshFilterContent
    private void refreshFilterContent() {

        if (_IsShowFilter) {
            // mostrar o filtro
            _llyFiltroCnt.setVisibility(View.VISIBLE);
            _txtTitulo.setText("( - ) ESCONDER FILTRO");
        } else {
            // esconder o filtro
            _llyFiltroCnt.setVisibility(View.GONE);
            _txtTitulo.setText("( + ) MOSTRAR FILTRO");
        }

    }
    // endregion


    // region loadPedidoMobileHistorico
    private ArrayList<tpPedidoMobileHistorico> loadPedidoMobileHistorico(long idCliente) {

        ArrayList<tpPedidoMobileHistorico> _out = null;

        SQLiteHelper _sqh = null;
        dbPedidoMobileHistorico _dbPedidoMobileHistorico = null;

        try {

            // region Montando o WHERE para o campo IdCliente
            SQLClauseHelper _schIdCliente = new SQLClauseHelper();
            _schIdCliente.addEqualInteger("IdCliente", idCliente);
            // endregion

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(ClienteListaActivity.this);
            _sqh.open(false);
            // endregion

            // region Carregando as informações dos pedidos em histórico
            _dbPedidoMobileHistorico = new dbPedidoMobileHistorico(_sqh);
            _out = (ArrayList<tpPedidoMobileHistorico>) _dbPedidoMobileHistorico.getList(tpPedidoMobileHistorico.class, _schIdCliente);
            // endregion

        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    ClienteListaActivity.this,
                    "Erro ao tentar carregas as informações de histórico de pedidos para o cliente",
                    e.getMessage()
            );
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
                _sqh = null;
            }
        }


        return _out;

    }
    // endregion


    // region showOnWaze
    private void showOnWaze() {

        SQLiteHelper _sqh = null;
        dbCliente _dbCliente = null;
        dbCidade _dbCidade = null;
        tpCliente _tpCliente = null;

        try
        {
            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(ClienteListaActivity.this);
            _sqh.open(false);
            // endregion


            // region Instânciando os objetos que farão acesso ao banco
            _dbCliente = new dbCliente(_sqh);
            _dbCidade = new dbCidade(_sqh);
            // endregion


            // region Buscando as informações no banco de dados e preenchendo o objeto tpCliente
            _tpCliente = (tpCliente) _dbCliente.getBySourceId(_tpSelected.IdCliente);
            _tpCliente.Cidade = (tpCidade) _dbCidade.getBySourceId(_tpCliente.IdCidade);
            // endregion


            // region Finalizando a conexão com o banco de dados
            _sqh.close();
            // endregion


            // region Invocando o método para iniciar a navegação via mapa do aplicativo selecionado
            if (_tpCliente != null && _tpCliente.Cidade != null) {
                MSVMaps.navigationOnWaze(ClienteListaActivity.this, _tpCliente.getEnderecoCompleto());
            }
            // endregion
        }
        catch ( Exception ex  )
        {

            MSVMsgBox.showMsgBoxError(
                    ClienteListaActivity.this,
                    "[ClienteListaActivity|showOnWaze] -> Erro ao iniciar a nagevação no aplicativo Waze",
                    ex.getMessage()
            );
        }
        finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region showOnGoogleMaps
    private void showOnGoogleMaps() {

        SQLiteHelper _sqh = null;
        dbCliente _dbCliente = null;
        dbCidade _dbCidade = null;
        tpCliente _tpCliente = null;

        try
        {
            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(ClienteListaActivity.this);
            _sqh.open(false);
            // endregion


            // region Instânciando os objetos que farão acesso ao banco
            _dbCliente = new dbCliente(_sqh);
            _dbCidade = new dbCidade(_sqh);
            // endregion


            // region Buscando as informações no banco de dados e preenchendo o objeto tpCliente
            _tpCliente = (tpCliente) _dbCliente.getBySourceId(_tpSelected.IdCliente);
            _tpCliente.Cidade = (tpCidade) _dbCidade.getBySourceId(_tpCliente.IdCidade);
            // endregion


            // region Finalizando a conexão com o banco de dados
            _sqh.close();
            // endregion


            // region Invocando o método para iniciar a navegação via mapa do aplicativo selecionado
            if (_tpCliente != null && _tpCliente.Cidade != null) {
                MSVMaps.navigationOnGoogleMaps(ClienteListaActivity.this, _tpCliente.getEnderecoCompleto());
            }
            // endregion
        }
        catch ( Exception ex  )
        {

            MSVMsgBox.showMsgBoxError(
                    ClienteListaActivity.this,
                    "[ClienteListaActivity|showOnWaze] -> Erro ao iniciar a nagevação no aplicativo Waze",
                    ex.getMessage()
            );
        }
        finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion

}
