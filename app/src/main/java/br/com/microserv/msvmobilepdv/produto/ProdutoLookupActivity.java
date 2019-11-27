package br.com.microserv.msvmobilepdv.produto;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvdal.dbGrupo;
import br.com.microserv.framework.msvdal.dbLinha;
import br.com.microserv.framework.msvdal.dbProduto;
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
import br.com.microserv.framework.msvinterface.OnSelectedItem;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.GrupoDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.LinhaDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.ProdutoListaAdapter;


public class ProdutoLookupActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_TP_PRODUTO = "tpProduto";
    static final String _KEY_TP_TABLE_PRECO = "tpTabelaPreco";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final int _DETAIL_VALUE = 4;
    static final String _CLIENTE_PEDIDO_MOBILE_ITEM_VALUE = "PedidoMobileItemEditarActivity";
    static final String _PRODUTO_LOOKUP_VALUE = "ProdutoLookupActivity";

    // endregion


    // region Declarando as variáveis que vinculam elementos do layout

    // LinearLayout
    LinearLayout _pnlFiltroCnt = null;
    LinearLayout _pnlTituloCnt = null;
    LinearLayout _pnlTabelaPrecoCnt = null;
    LinearLayout _pnlTabelaPreco = null;
    LinearLayout _pnlLinhaCnt = null;
    LinearLayout _pnlLinha = null;
    LinearLayout _pnlGrupoCnt = null;
    LinearLayout _pnlGrupo = null;
    LinearLayout _pnlProdutoCnt = null;
    LinearLayout _pnlProduto = null;
    LinearLayout _pnlRodapeCnt = null;

    // TextView
    TextView _txtTituloDescricao = null;
    TextView _txtTabelaPrecoDescricao = null;
    TextView _txtLinhaDescricao = null;
    TextView _txtGrupoDescricao = null;
    TextView _txtProdutoDescricao = null;
    TextView _txtRodapeRegistro = null;

    // ListView
    ListView _livProduto;

    // endregion


    // region Declarando objetos e variaveis de uso da activity

    // Adaptadores
    LinhaDialogSearchAdapter _adpLinha = null;
    GrupoDialogSearchAdapter _adpGrupo = null;
    ProdutoListaAdapter _adpProduto = null;


    // Tp
    tpEmpresa _tpEmpresa = null;
    tpTabelaPreco _tpTabelaPreco = null;

    // List
    ArrayList<tpLinha> _lstLinha;
    ArrayList<tpGrupo> _lstGrupo;
    ArrayList<tpProduto> _lstProduto;
    ArrayList<tpProduto> _lstProdutoFiltered;

    // int
    int _iLinha = -1;
    int _iGrupo = -1;
    int _iProduto = -1;
    int _iAux = 0;
    int _metodoEdicao = 0;
    int _itemIndex = 0;

    long _IdLinhaShared = -1;
    long _IdGrupoShared = -1;

    // boolean
    boolean _IsShowFilter;

    // String
    String[] _asLinha;
    String[] _asGrupo;
    String _sProduto = "";
    String _sourceActivity = "";
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // invocando o metodo onCreate da superclasse
        // e construindo a interface da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_lookup);


        // adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);


        // region Selecionando os parametros enviados através do Bundle
        Bundle _extras = getIntent().getExtras();

        if (_extras != null) {

            // region _KEY_METODO_EDICAO
            if (_extras.containsKey(_KEY_METODO_EDICAO)) {
                _metodoEdicao = _extras.getInt(_KEY_METODO_EDICAO);
            } else {
                Toast.makeText(
                        ProdutoLookupActivity.this,
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
                        ProdutoLookupActivity.this,
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
                        ProdutoLookupActivity.this,
                        "O parâmetro _KEY_ID_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_TP_TABLE_PRECO
            if (_extras.containsKey(_KEY_TP_TABLE_PRECO)) {
                _tpTabelaPreco = (tpTabelaPreco) _extras.getSerializable(_KEY_TP_TABLE_PRECO);
            } else {
                Toast.makeText(
                        ProdutoLookupActivity.this,
                        "O parâmetro _KEY_TP_TABLE_PRECO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        }
        // endregion


        // lendo as preferencias do usuário
        _IdLinhaShared = Long.parseLong(
                MSVUtil.readPreference(
                        ProdutoLookupActivity.this,
                        _tpEmpresa.Sigla,
                        "ID_LINHA",
                        "0"
                )
        );

        _IdGrupoShared = Long.parseLong(
                MSVUtil.readPreference(
                        ProdutoLookupActivity.this,
                        _tpEmpresa.Sigla,
                        "ID_GRUPO",
                        "0"
                )
        );


        // invocando os metodos de inicializacao
        bindElements();
        bindEvents();
        initialize();

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

        // LinearLayout
        _pnlFiltroCnt = (LinearLayout) findViewById(R.id.pnlFiltroCnt);
        _pnlTituloCnt = (LinearLayout) findViewById(R.id.pnlTituloCnt);
        _pnlTabelaPrecoCnt = (LinearLayout) findViewById(R.id.pnlTabelaPrecoCnt);
        _pnlTabelaPreco = (LinearLayout) findViewById(R.id.pnlTabelaPreco);
        _pnlLinhaCnt = (LinearLayout) findViewById(R.id.pnlLinhaCnt);
        _pnlLinha = (LinearLayout) findViewById(R.id.pnlLinha);
        _pnlGrupoCnt = (LinearLayout) findViewById(R.id.pnlGrupoCnt);
        _pnlGrupo = (LinearLayout) findViewById(R.id.pnlGrupo);
        _pnlProdutoCnt = (LinearLayout) findViewById(R.id.pnlProdutoCnt);
        _pnlProduto = (LinearLayout) findViewById(R.id.pnlProduto);
        _pnlRodapeCnt = (LinearLayout) findViewById(R.id.pnlRodapeCnt);

        // TextView
        _txtTituloDescricao = (TextView) findViewById(R.id.txtTituloDescricao);
        _txtTabelaPrecoDescricao = (TextView) findViewById(R.id.txtTabelaPrecoDescricao);
        _txtLinhaDescricao = (TextView) findViewById(R.id.txtLinhaDescricao);
        _txtGrupoDescricao = (TextView) findViewById(R.id.txtGrupoDescricao);
        _txtProdutoDescricao = (TextView) findViewById(R.id.txtProdutoDescricao);
        _txtRodapeRegistro = (TextView) findViewById(R.id.txtRodapeRegistro);

        // ListView
        _livProduto = (ListView) findViewById(R.id.livProduto);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Click no TITULO do FILTRO
        _pnlTituloCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _IsShowFilter = !_IsShowFilter;
                refreshFilterContent();
            }
        });
        // endregion


        // region Click em LINHA de PRODUTO
        _pnlLinha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Inicializando o adaptardor
                if (_adpLinha == null) {
                    _adpLinha = new LinhaDialogSearchAdapter(
                            ProdutoLookupActivity.this,
                            _lstLinha
                    );
                } else {
                    _adpLinha.notifyDataSetChanged();
                }
                // endregion


                // region Abrindo a janela de lookup
                MSVMsgBox.getValueFromList(
                        ProdutoLookupActivity.this,
                        "SELECIONAR LINHA",
                        _adpLinha,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                _iLinha = _iAux;

                                refreshLinha();
                                loadGrupo();
                                filterProduto();

                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click em GRUPO de PRODUTO
        _pnlGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Inicializando o adaptador
                if (_adpGrupo == null) {
                    _adpGrupo = new GrupoDialogSearchAdapter(
                            ProdutoLookupActivity.this,
                            _lstGrupo
                    );
                } else {
                    _adpGrupo.notifyDataSetChanged();
                }
                // endregion


                // region Abrindo a janela de lookup
                MSVMsgBox.getValueFromList(
                        ProdutoLookupActivity.this,
                        "SELECIONAR GRUPO",
                        _adpGrupo,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                _iGrupo = _iAux;

                                refreshGrupo();
                                loadProduto();
                                filterProduto();

                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click em ARGUMENTO de PESQUISA do PRODUTO
        _pnlProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Abrindo janela para informar parte do nome
                MSVMsgBox.getStringValue(
                        ProdutoLookupActivity.this,
                        "PESQUISAR PRODUTO",
                        "Informa parte do texto que deseja para buscar o produto. O valor informado irá pesquisar nos campos: Código, Código de Barras e Descrição",
                        _txtProdutoDescricao.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                _sProduto = value;

                                if (MSVUtil.isNullOrEmpty(value)) {
                                    _txtProdutoDescricao.setText("TODOS");
                                } else {
                                    _txtProdutoDescricao.setText(_sProduto.toUpperCase());
                                }

                                filterProduto();

                            }
                        }
                );
                // endregion

            }
        });
        // endregion


        // region Click no ITEM da LISTA de PRODUTO
        // aqui só vamos vincular o evento se a activity foi invocada a partir
        // da activity de itens do pedido
        _livProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // region Salvando as preferencias do usuário
                MSVUtil.savePreference(
                        ProdutoLookupActivity.this,
                        _tpEmpresa.Sigla,
                        "ID_LINHA",
                        String.valueOf(_lstLinha.get(_iLinha).IdLinha)
                );

                MSVUtil.savePreference(
                        ProdutoLookupActivity.this,
                        _tpEmpresa.Sigla,
                        "ID_GRUPO",
                        String.valueOf(_lstGrupo.get(_iGrupo).IdGrupo)
                );
                // endregion

                // region Construindo os parametros de retorno
                Bundle _extras = new Bundle();
                //_extras.putString(_KEY_SOURCE_ACTIVITY, _sourceActivity);
                //_extras.putInt(_KEY_METODO_EDICAO, _metodoEdicao);
                _extras.putSerializable(_KEY_TP_PRODUTO, _lstProdutoFiltered.get(position));
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
        // endregion

    }
    // endregion


    // region initialize
    public void initialize(){

        // region Cuidando de informações iniciais do form
        _txtRodapeRegistro.setText("REGISTROS: 0");
        // endregion

        // region Cuidando da apresentação do filtro para o usuário
        _IsShowFilter = true;
        refreshFilterContent();
        // endregion

        // region Atualizando os dados da tabela de preço
        this.refreshTabelaPreco();
        // endregion

        // region Realizando a leitura de informações no banco
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
            _pnlTituloCnt.setEnabled(false);

            // emitindo mensagem ao usuário
            Toast.makeText(
                    ProdutoLookupActivity.this,
                    "Não existem produtos sincronizados no dispositivo",
                    Toast.LENGTH_SHORT
            ).show();

        }
        // endregion

    }
    // endregion


    // region refreshTabelaPreco
    private void refreshTabelaPreco() {

        _txtTabelaPrecoDescricao.setText(_tpTabelaPreco.Descricao);

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

        _txtLinhaDescricao.setText(_sLinha);

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

        _txtGrupoDescricao.setText(_sGrupo);

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
                    _schTabelaPrecoProduto.addEqualInteger("IdTabelaPreco", _tpTabelaPreco.IdTabelaPreco);
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
            _txtProdutoDescricao.setText("TODOS");
        } else {
            _txtProdutoDescricao.setText(_sProduto.toUpperCase());
        }


        // atualizando a lista
        _adpProduto = new ProdutoListaAdapter(
                ProdutoLookupActivity.this,
                _lstProdutoFiltered
        );

        /*
        if (_adpProduto == null) {
            _adpProduto = new ProdutoListaAdapter(
                    ProdutoLookupActivity.this,
                    _lstProdutoFiltered
            );

            _livProduto.setAdapter(_adpProduto);
        } else {
            _adpProduto._items = _lstProdutoFiltered;
            _adpProduto.notifyDataSetChanged();
        }
        */


        // atualiando a quantidade de registros
        _txtRodapeRegistro.setText("REGISTROS: " + String.valueOf(_lstProdutoFiltered.size()));

    }
    // endregion


    // region refreshFilterContent
    private void refreshFilterContent() {

        if (_IsShowFilter) {
            // mostrar o filtro
            _pnlFiltroCnt.setVisibility(View.VISIBLE);
            _txtTituloDescricao.setText("( - ) ESCONDER FILTRO");
        } else {
            // esconder o filtro
            _pnlFiltroCnt.setVisibility(View.GONE);
            _txtTituloDescricao.setText("( + ) MOSTRAR FILTRO");
        }

    }
    // endregion

}
