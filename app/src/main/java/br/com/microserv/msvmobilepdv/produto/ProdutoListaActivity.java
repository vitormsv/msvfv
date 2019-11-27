package br.com.microserv.msvmobilepdv.produto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbGrupo;
import br.com.microserv.framework.msvdal.dbLinha;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdal.dbTabelaPreco;
import br.com.microserv.framework.msvdal.dbTabelaPrecoProduto;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpGrupo;
import br.com.microserv.framework.msvdto.tpLinha;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvdto.tpTabelaPrecoProduto;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.GrupoDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.LinhaDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.ProdutoListaAdapter;
import br.com.microserv.msvmobilepdv.adapter.TabelaPrecoDialogSearchAdapter;


public class ProdutoListaActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_ITEM_INDEX = "ItemIndex";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_ID_PRODUTO = "IdProduto";
    static final String _KEY_TP_PRODUTO = "tpProduto";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final int _DETAIL_VALUE = 4;
    static final String _CLIENTE_PEDIDO_MOBILE_ITEM_VALUE = "PedidoMobileItemEditarActivity";
    static final String _PRODUTO_LISTA_VALUE = "ProdutoListaActivity";

    // endregion


    // region Declarando as variáveis que vinculam elementos do layout

    // TextView
    TextView _txtTitulo;
    TextView _txtTabelaPreco;
    TextView _txtLinha;
    TextView _txtGrupo;
    TextView _txtProduto;
    TextView _txtRodape;

    // LinearLayout
    LinearLayout _llyTitulo;
    LinearLayout _llyFiltroCnt;
    LinearLayout _llyTabelaPreco;
    LinearLayout _llyLinha;
    LinearLayout _llyGrupo;
    LinearLayout _llyProduto;

    // ListView
    ListView _livProduto;

    // endregion


    // region Declarando objetos e variaveis de uso da activity

    // Tp
    tpEmpresa _tpEmpresa = null;

    // List
    ArrayList<tpTabelaPreco> _lstTabelaPreco;
    ArrayList<tpLinha> _lstLinha;
    ArrayList<tpGrupo> _lstGrupo;
    ArrayList<tpProduto> _lstProduto;
    ArrayList<tpProduto> _lstProdutoFiltered;

    // int
    int _iTabelaPreco = -1;
    int _iLinha = -1;
    int _iGrupo = -1;
    int _iProduto = -1;
    int _iAux = 0;
    int _metodoEdicao = 0;
    int _itemIndex = 0;

    long _IdTabelaPrecoShared = -1;
    long _IdLinhaShared = -1;
    long _IdGrupoShared = -1;

    // boolean
    boolean _IsShowFilter;
    boolean _IsClientePedidoMobileItemActivity = false;

    // String
    String[] _asLinha;
    String[] _asGrupo;
    String _sProduto = "";
    String _sourceActivity = "";
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_lista);
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
                        ProdutoListaActivity.this,
                        "O parâmetro _KEY_METODO_EDICAO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_SOURCE_ACTIVITY
            if (_extras.containsKey(_KEY_SOURCE_ACTIVITY)) {

                _sourceActivity = _extras.getString(_KEY_SOURCE_ACTIVITY);

                switch (_sourceActivity) {
                    case _CLIENTE_PEDIDO_MOBILE_ITEM_VALUE:
                        _IsClientePedidoMobileItemActivity = true;
                        break;
                }

            } else {
                Toast.makeText(
                        ProdutoListaActivity.this,
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
                        ProdutoListaActivity.this,
                        "O parâmetro _KEY_ID_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_ITEM_INDEX
            if (_extras.containsKey(_KEY_ITEM_INDEX)) {
                _itemIndex = _extras.getInt(_KEY_ITEM_INDEX);
            } else {
                Toast.makeText(
                        ProdutoListaActivity.this,
                        "O parâmetro _KEY_ITEM_INDEX não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        }
        // endregion


        // region Lendo as preferencias do usuário
        _IdLinhaShared = Long.parseLong(
                MSVUtil.readPreference(
                        ProdutoListaActivity.this,
                        _tpEmpresa.Sigla,
                        "ID_LINHA",
                        "0"
                )
        );

        _IdGrupoShared = Long.parseLong(
                MSVUtil.readPreference(
                        ProdutoListaActivity.this,
                        _tpEmpresa.Sigla,
                        "ID_GRUPO",
                        "0"
                )
        );
        // endregion


        // region Invocando os métodos da interface
        bindElements();
        bindEvents();
        initialize();
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

        /// TextView
        _txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        _txtTabelaPreco = (TextView) findViewById(R.id.txtTabelaPreco);
        _txtLinha = (TextView) findViewById(R.id.txtLinha);
        _txtGrupo = (TextView) findViewById(R.id.txtGrupo);
        _txtProduto = (TextView) findViewById(R.id.txtProduto);
        _txtRodape = (TextView) findViewById(R.id.txtRodape);

        // LinearLayout
        _llyTitulo = (LinearLayout) findViewById(R.id.llyTitulo);
        _llyFiltroCnt = (LinearLayout) findViewById(R.id.llyFiltroCnt);
        _llyTabelaPreco = (LinearLayout) findViewById(R.id.llyTabelaPreco);
        _llyLinha = (LinearLayout) findViewById(R.id.llyLinha);
        _llyGrupo = (LinearLayout) findViewById(R.id.llyGrupo);
        _llyProduto = (LinearLayout) findViewById(R.id.llyProduto);

        // ListView
        _livProduto = (ListView) findViewById(R.id.livProduto);

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


        // region _llyTabelaPreco
        _llyTabelaPreco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Criando o adaptador para a lista de tabelas de preço
                final TabelaPrecoDialogSearchAdapter _adp = new TabelaPrecoDialogSearchAdapter(ProdutoListaActivity.this, _lstTabelaPreco);
                // endregion

                // region Inflando o layout customizado para o AlertDialog
                LayoutInflater _inflanter = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflanter.inflate(R.layout.dialog_personalizado_lista, null);
                // endregion

                // region Trabalhando o elemento de título
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
                _txtDialogTitle.setText(_lstTabelaPreco.get(_iTabelaPreco).Descricao);
                // endregion

                // region Trabalhando o elemento ListView
                ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

                _lv.setAdapter(_adp);

                _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _iTabelaPreco = position;
                        _txtDialogTitle.setText(_lstTabelaPreco.get(position).Descricao);
                    }
                });
                // endregion

                // region Criando a janela modal AlertDialog
                final AlertDialog.Builder _builder = new AlertDialog.Builder(ProdutoListaActivity.this);

                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        refreshTabelaPreco();

                        if (_lstProdutoFiltered != null && _lstProdutoFiltered.size() > 0) {
                            loadProduto();
                        }

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


        // region _llyLinha
        _llyLinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Criando o adaptador para alista de linha de produto
                final LinhaDialogSearchAdapter _adp = new LinhaDialogSearchAdapter(ProdutoListaActivity.this, _lstLinha);
                // endregion

                // region Inflando o layout customizado para o AlertDialog
                // inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);

                // bucando o elemento do título
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
                _txtDialogTitle.setText(_lstLinha.get(_iLinha).Descricao);

                // buscando o elemento ListView
                ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

                _lv.setAdapter(_adp);

                _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _iLinha = position;
                        _txtDialogTitle.setText(_lstLinha.get(position).Descricao);
                    }
                });
                // endregion

                // region Criando a janela modal AlertDialog
                final AlertDialog.Builder _builder = new AlertDialog.Builder(ProdutoListaActivity.this);

                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // aqui vamos realizar o refresh do grupo e do produto
                        refreshLinha();
                        loadGrupo();
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


        // region _llyGrupo
        _llyGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Criando o adaptador para alista de linha de produto
                final GrupoDialogSearchAdapter _adp = new GrupoDialogSearchAdapter(ProdutoListaActivity.this, _lstGrupo);
                // endregion

                // region Inflando o layout customizado para o AlertDialog
                // inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);

                // bucando o elemento do título
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
                _txtDialogTitle.setText(_lstGrupo.get(_iGrupo).Descricao);

                // buscando o elemento ListView
                ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

                _lv.setAdapter(_adp);

                _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _iGrupo = position;
                        _txtDialogTitle.setText(_lstGrupo.get(position).Descricao);
                    }
                });
                // endregion

                // region Criando a janela modal AlertDialog
                final AlertDialog.Builder _builder = new AlertDialog.Builder(ProdutoListaActivity.this);

                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // aqui vamos realizar o refresh do grupo e do produto
                        refreshGrupo();
                        loadProduto();
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


        // region _llyProdutoDescricao
        _llyProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ProdutoListaActivity.this,
                        "PESQUISAR PRODUTO",
                        "Informe o argumento de pesquisa para selecionar o produto",
                        _txtProduto.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {

                                    if (!_sProduto.equalsIgnoreCase(value.trim().toString())) {

                                        _sProduto = value.trim().toString();

                                        if (_sProduto.isEmpty()) {
                                            _txtProduto.setText("TODOS");
                                        } else {
                                            _txtProduto.setText(_sProduto.toUpperCase());
                                        }

                                        filterProduto();

                                    }

                                }

                            }
                        }

                );

            }
        });
        // endregion


        // region _livProduto
        // vinculando o evento de detalhes do produto quando a activity
        // for iniciada sem estar com o pedido em aberto
        if (_IsClientePedidoMobileItemActivity == false) {
            _livProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // region Construindo os parametros de retorno
                    Bundle _extras = new Bundle();
                    _extras.putString(_KEY_SOURCE_ACTIVITY, _PRODUTO_LISTA_VALUE);
                    _extras.putInt(_KEY_METODO_EDICAO, _DETAIL_VALUE);
                    _extras.putInt(_KEY_ITEM_INDEX, position);
                    _extras.putLong(_KEY_ID_PRODUTO, _lstProdutoFiltered.get(position).IdProduto);
                    // endregion

                    // region Construindo um intent de retorno
                    Intent _intent = new Intent(ProdutoListaActivity.this, ProdutoDetalheActivity.class);
                    _intent.putExtras(_extras);
                    // endregion

                    // region Invocando a nova activity
                    startActivity(_intent);
                    // endregion

                }
            });
        }

        // aqui só vamos vincular o evento se a activity foi invocada a partir
        // da activity de itens do pedido
        if (_IsClientePedidoMobileItemActivity) {
            _livProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // region Salvando as preferencias do usuário
                    MSVUtil.savePreference(
                            ProdutoListaActivity.this,
                            _tpEmpresa.Sigla,
                            "ID_LINHA",
                            String.valueOf(_lstLinha.get(_iLinha).IdLinha)
                    );

                    MSVUtil.savePreference(
                            ProdutoListaActivity.this,
                            _tpEmpresa.Sigla,
                            "ID_GRUPO",
                            String.valueOf(_lstGrupo.get(_iGrupo).IdGrupo)
                    );
                    // endregion

                    // region Construindo os parametros de retorno
                    Bundle _extras = new Bundle();
                    _extras.putString(_KEY_SOURCE_ACTIVITY, _sourceActivity);
                    _extras.putInt(_KEY_METODO_EDICAO, _metodoEdicao);
                    _extras.putInt(_KEY_ITEM_INDEX, position);
                    _extras.putLong(_KEY_ID_PRODUTO, _lstProdutoFiltered.get(position).IdProduto);
                    // endregion

                    // region Construindo uma intent de retorno
                    Intent _intent = new Intent();
                    _intent.putExtras(_extras);
                    // endregion

                    // region Aplicando os parametros a intent e finalizando a activity
                    setResult(Activity.RESULT_OK, _intent);
                    finish();
                    // endregion

                }
            });
        }
        // endregion

    }
    // endregion


    // region initialize
    public void initialize(){

        // region Cuidando de informações iniciais do form
        _txtRodape.setText("REGISTROS: 0");
        // endregion

        // region Cuidando da apresentação do filtro para o usuário
        _IsShowFilter = false;

        if (_IsClientePedidoMobileItemActivity) {
            _IsShowFilter = true;
        }

        refreshFilterContent();
        // endregion

        // region Realizando a leitura de informações no banco
        this.loadTabelaPreco();
        this.loadLinha();
        // endregion

        // region Emitindo mensagem ao usuário quando não existir produtos sincronizados
        boolean _vazio = false;

        if (_lstProduto == null) {
            _vazio = true;
        } else {
            if (_lstProduto.size() == 0) {
                _vazio = true;
            }
        }

        if (_vazio) {

            // deixando o filtro de produto inativo
            _llyTitulo.setEnabled(false);

            // emitindo mensagem ao usuário
            Toast.makeText(
                    ProdutoListaActivity.this,
                    "Não existem produtos sincronizados no dispositivo",
                    Toast.LENGTH_SHORT
            ).show();

        }
        // endregion

    }
    // endregion


    // region loadTabelaPreco
    private void loadTabelaPreco() {

        _iTabelaPreco = -1;

        SQLiteHelper _sqh = null;

        try {

            SQLClauseHelper _schTabelaPreco = new SQLClauseHelper();
            _schTabelaPreco.addOrderBy("Descricao", eSQLSortType.ASC);

            _sqh = new SQLiteHelper(ProdutoListaActivity.this);
            _sqh.open(false);

            dbTabelaPreco _dbTabelaPreco = new dbTabelaPreco(_sqh);
            _lstTabelaPreco = (ArrayList<tpTabelaPreco>) _dbTabelaPreco.getList(tpTabelaPreco.class, _schTabelaPreco);

            if ((_lstTabelaPreco != null) && (_lstTabelaPreco.size() > 0)) {

                _iTabelaPreco = 0;

                // aplicando a preferencia do usuário para
                // a linha do produto
                if (_IdTabelaPrecoShared > -1) {
                    for (int i = 0; i < _lstTabelaPreco.size(); i++) {
                        if (_lstTabelaPreco.get(i).IdTabelaPreco == _IdTabelaPrecoShared) {
                            _iTabelaPreco = i;
                        }
                    }
                }

                refreshTabelaPreco();

                if ((_lstProdutoFiltered != null) && (_lstProdutoFiltered.size() > 0)) {
                    loadProduto();
                }

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


    // region refreshTabelaPreco
    private void refreshTabelaPreco() {

        String _sTabelaPreco;

        if (_iTabelaPreco > -1) {
            _sTabelaPreco = _lstTabelaPreco.get(_iTabelaPreco).Descricao;
        } else {
            _sTabelaPreco = "NÃO EXISTE TABELA DE PRECO";
        }

        _txtTabelaPreco.setText(_sTabelaPreco);

    }
    // endregion


    // region loadLinha
    private void loadLinha() {

        _iLinha = -1;

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
        _sqh.open(false);

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdEmpresa", _tpEmpresa.IdEmpresa);
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            dbLinha _dbLinha = new dbLinha(_sqh);
            _lstLinha = (ArrayList<tpLinha>) _dbLinha.getList(tpLinha.class, _sch);

            if (_lstLinha.size() != 0) {
                _iLinha = 0;

                // aplicando a preferencia do usuário para
                // a linha do produto
                if (_IdLinhaShared != 0) {
                    for (int i = 0; i < _lstLinha.size(); i++) {
                        if (_lstLinha.get(i).IdLinha == _IdLinhaShared) {
                            _iLinha = i;
                        }
                    }
                }

                this.refreshLinha();
                this.loadGrupo();
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


    // region refreshLinha
    private void refreshLinha() {

        String _sLinha;

        if (_iLinha > -1) {
            _sLinha = _lstLinha.get(_iLinha).Descricao;
        } else {
            _sLinha = "NÃO EXISTE LINHA";
        }

        _txtLinha.setText(_sLinha);

    }
    // endregion


    // region loadGrupo
    private void loadGrupo() {

        _iGrupo = -1;

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
        _sqh.open(false);

        try {

            // preenchendo o objeto helper
            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdEmpresa", _lstLinha.get(_iLinha).IdEmpresa);
            _sch.addEqualInteger("IdLinha", _lstLinha.get(_iLinha).IdLinha);
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            dbGrupo _dbGrupo = new dbGrupo(_sqh);
            _lstGrupo = (ArrayList<tpGrupo>) _dbGrupo.getList(tpGrupo.class, _sch);

            if (_lstGrupo.size() != 0) {
                _iGrupo = 0;

                // aplicando a preferencia do usuário para
                // o grupo do produto
                if (_IdGrupoShared != 0) {
                    for (int i = 0; i < _lstGrupo.size(); i++) {
                        if (_lstGrupo.get(i).IdGrupo == _IdGrupoShared) {
                            _iGrupo = i;
                        }
                    }
                }

                this.refreshGrupo();
                this.loadProduto();
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


    // region refreshGrupo
    private void refreshGrupo() {

        String _sGrupo;

        if (_iGrupo > -1) {
            _sGrupo = _lstGrupo.get(_iGrupo).Descricao;
        } else {
            _sGrupo = "NÃO EXISTE GRUPO";
        }

        _txtGrupo.setText(_sGrupo);

    }
    // endregion


    // region loadProduto
    private void loadProduto() {

        // region Inicializando variáveis
        _iProduto = -1;
        // endregion

        // region Declarando objetos para uso do banco de dados
        SQLiteHelper _sqh = null;
        dbProduto _dbProduto = null;
        dbTabelaPrecoProduto _dbTabelaPrecoProduto = null;
        // endregion

        // region Bloco protegido
        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(getBaseContext());
            _sqh.open(false);
            // endregion

            // region Istânciando os objetos de acesso ao banco
            _dbProduto = new dbProduto(_sqh);
            _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqh);
            // endregion

            // region Carregando a lista de produtos de acordo com a empresa e grupo
            _lstProduto = (ArrayList<tpProduto>) _dbProduto.getListByIdGrupo(_tpEmpresa.IdEmpresa, _lstGrupo.get(_iGrupo).IdGrupo);
            // endregion

            // region Trabalhando com a lista de produtos recuperada
            if (_lstProduto.size() != 0) {

                // region Instânciando uma lista de produtos filtrados
                _lstProdutoFiltered = new ArrayList<tpProduto>();
                // endregion

                // region Para cada produto vamos buscar o preço
                // isso de acordo com a tabela selecionada no filtro da tela
                for (tpProduto _tp : _lstProduto) {

                    // region Montando o WHERE para recuperar o preço do produto
                    SQLClauseHelper _schTabelaPrecoProduto = new SQLClauseHelper();
                    _schTabelaPrecoProduto.addEqualInteger("IdTabelaPreco", _lstTabelaPreco.get(_iTabelaPreco).IdTabelaPreco);
                    _schTabelaPrecoProduto.addEqualInteger("IdProduto", _tp.IdProduto);
                    // endregion

                    // region Buscando o preço no banco de dados
                    _tp.TabelaPrecoProduto = (tpTabelaPrecoProduto) _dbTabelaPrecoProduto.getOne(_schTabelaPrecoProduto);
                    // endregion

                    // region Adicionando o produto na lista de produtos filtrados
                    _lstProdutoFiltered.add(_tp);
                    // endregion

                }
                // endregion

                // region Atualizando as informações do produto na tela
                this.refreshProduto();
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
        // endregion

    }
    // endregion


    // region filterProduto
    public void filterProduto() {

        // region Removendo todos os itens da lista atual
        _lstProdutoFiltered.clear();
        // endregion

        // region Aplicando o filtro de acordo com a expressao
        if (_sProduto.isEmpty()) {

            for(tpProduto _tp : _lstProduto) {
                _lstProdutoFiltered.add(_tp);
            }

        } else {

            _sProduto = _sProduto.trim().toLowerCase();
            String[] _expr = _sProduto.split(" ");

            for (tpProduto _tp : _lstProduto) {

                String _p1 = _tp.Descricao.toLowerCase();
                String _p2 = _tp.Codigo.toLowerCase();
                String _p3 = _tp.Ean13.toLowerCase();

                int _p1Qtd = 0;
                int _p2Qtd = 0;
                int _p3Qtd = 0;

                for (int i = 0; i < _expr.length; i++) {

                    if (_p1.contains(_expr[i])) {
                        _p1Qtd += 1;
                    }

                    if(_p2.contains(_expr[i])) {
                        _p2Qtd += 1;
                    }

                    if (_p3.contains(_expr[i])) {
                        _p3Qtd += 1;
                    }

                }

                if ((_p1Qtd == _expr.length) || (_p2Qtd == _expr.length) || (_p3Qtd == _expr.length)) {
                    _lstProdutoFiltered.add(_tp);
                }

            }

        }
        // endregion

        // region invocando o método para atualizar a lista de produto
        this.refreshProduto();
        // endregion

    }
    // endregion


    // region refreshProduto
    private void refreshProduto() {

        // atualizando o texto de pesquisa do filtro
        if (_sProduto.equalsIgnoreCase("")) {
            _txtProduto.setText("TODOS");
        } else {
            _txtProduto.setText(_sProduto.toUpperCase());
        }


        // atualizando a lista
        ProdutoListaAdapter _ada = new ProdutoListaAdapter(ProdutoListaActivity.this, _lstProdutoFiltered);
        _livProduto.setAdapter(_ada);


        // atualiando a quantidade de registros
        _txtRodape.setText("REGISTROS: " + String.valueOf(_lstProdutoFiltered.size()));

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

}
