package br.com.microserv.msvmobilepdv.produto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvdal.dbGrupo;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpGrupo;
import br.com.microserv.framework.msvdto.tpLinha;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpProdutoSearch;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.GrupoDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.LetraInicialAdapter;
import br.com.microserv.msvmobilepdv.adapter.LinhaDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.ProdutoSearchAdapter;
import br.com.microserv.msvmobilepdv.pedido.PedidoMobileEditarActivity;
import br.com.microserv.msvmobilepdv.pedido.PedidoMobileItemEditarActivity;

public class ProdutoSearchActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_TP_PRODUTO = "tpProduto";
    static final String _KEY_TP_TABLE_PRECO = "tpTabelaPreco";
    static final String _KEY_LST_PEDIDO_MOBILE_ITEM = "lstPedidoMobileItem";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final int _DETAIL_VALUE = 4;
    static final String _PRODUTO_SEARCH_VALUE = "ProdutoSearchActivity";

    // endregion

    // region Listas
    List<tpPedidoMobileItem> _itens;
    // endregion


    // region Declarando as variáveis que vinculam elementos do layout

    // LinearLayout
    LinearLayout _pnlFiltroCnt = null;
    LinearLayout _pnlTituloCnt = null;
    LinearLayout _pnlTabelaPrecoCnt = null;
    LinearLayout _pnlTabelaPreco = null;
    LinearLayout _pnlLetraInicialCnt = null;
    LinearLayout _pnlLetraInicial = null;
    LinearLayout _pnlProdutoCnt = null;
    LinearLayout _pnlProdutoGrupo = null;
    LinearLayout _pnlProduto = null;
    LinearLayout _pnlRodapeCnt = null;

    // TextView
    TextView _txtTituloDescricao = null;
    TextView _txtTabelaPrecoDescricao = null;
    TextView _txtLetraInicial = null;
    TextView _txtProdutoGrupo = null;
    TextView _txtProdutoDescricao = null;
    TextView _txtRodapeRegistro = null;

    // ListView
    ListView _livProduto;

    // endregion


    // region Declarando objetos e variaveis de uso da activity

    // Adaptadores
    LinhaDialogSearchAdapter _adpLinha = null;
    GrupoDialogSearchAdapter _adpGrupo = null;
    ProdutoSearchAdapter _adpProduto = null;


    // Tp
    tpEmpresa _tpEmpresa = null;
    tpTabelaPreco _tpTabelaPreco = null;

    // List
    ArrayList<tpLinha> _lstLinha;
    ArrayList<tpGrupo> _lstGrupo;
    ArrayList<tpProdutoSearch> _lstProdutoSearchResult;
    ArrayList<tpProdutoSearch> _lstProdutoSearch;

    // int
    int _iLinha = -1;
    int _iGrupo = -1;
    String _sLetraInicial = "";
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
    String _sGrupo = "";
    String _sProduto = "";
    String _sourceActivity = "";
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // invocando o metodo onCreate da superclasse
        // e construindo a interface da activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_search);


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
                        ProdutoSearchActivity.this,
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
                        ProdutoSearchActivity.this,
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
                        ProdutoSearchActivity.this,
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
                        ProdutoSearchActivity.this,
                        "O parâmetro _KEY_TP_TABLE_PRECO não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_LST_PEDIDO_MOBILE_ITEM
            if (_extras.containsKey(_KEY_LST_PEDIDO_MOBILE_ITEM)) {

                _itens = (List<tpPedidoMobileItem>) _extras.getSerializable(_KEY_LST_PEDIDO_MOBILE_ITEM);
            } else {
                Toast.makeText(
                        ProdutoSearchActivity.this,
                        "O parâmetro _KEY_LST_PEDIDO_MOBILE_ITEM não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        }
        // endregion


        // invocando os metodos de inicializacao
        bindElements();
        bindEvents();
        initialize();

        if (!PedidoMobileEditarActivity._PESQUISA_VALUE.isEmpty()) {
            _sProduto = PedidoMobileEditarActivity._PESQUISA_VALUE;
            this.loadProduto();
        }
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
        _pnlLetraInicial = (LinearLayout) findViewById(R.id.pnlLetraInicial);
        _pnlProdutoCnt = (LinearLayout) findViewById(R.id.pnlProdutoCnt);
        _pnlProdutoGrupo = (LinearLayout) findViewById(R.id.pnlProdutoGrupo);
        _pnlProduto = (LinearLayout) findViewById(R.id.pnlProduto);
        _pnlRodapeCnt = (LinearLayout) findViewById(R.id.pnlRodapeCnt);

        // TextView
        _txtTituloDescricao = (TextView) findViewById(R.id.txtTituloDescricao);
        _txtTabelaPrecoDescricao = (TextView) findViewById(R.id.txtTabelaPrecoDescricao);
        _txtLetraInicial = (TextView) findViewById(R.id.txtLetraInicial);
        _txtProdutoGrupo = (TextView) findViewById(R.id.txtProdutoGrupoDescricao);
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



        // region Click em ARGUMENTO de PESQUISA do GRUPO DE PRODUTO
        _pnlLetraInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 final ArrayList<String> _lstLetraInicial = new ArrayList<String>();

                _lstLetraInicial.add("");
                _lstLetraInicial.add("A");
                _lstLetraInicial.add("B");
                _lstLetraInicial.add("C");
                _lstLetraInicial.add("D");
                _lstLetraInicial.add("E");
                _lstLetraInicial.add("F");
                _lstLetraInicial.add("G");
                _lstLetraInicial.add("H");
                _lstLetraInicial.add("I");
                _lstLetraInicial.add("J");
                _lstLetraInicial.add("K");
                _lstLetraInicial.add("L");
                _lstLetraInicial.add("M");
                _lstLetraInicial.add("N");
                _lstLetraInicial.add("O");
                _lstLetraInicial.add("P");
                _lstLetraInicial.add("Q");
                _lstLetraInicial.add("R");
                _lstLetraInicial.add("S");
                _lstLetraInicial.add("T");
                _lstLetraInicial.add("U");
                _lstLetraInicial.add("V");
                _lstLetraInicial.add("W");
                _lstLetraInicial.add("X");
                _lstLetraInicial.add("Y");
                _lstLetraInicial.add("Z");

                // region Criando o adaptador para alista de linha de produto
                final LetraInicialAdapter _adp = new LetraInicialAdapter(ProdutoSearchActivity.this, _lstLetraInicial);
                // endregion

                // region Inflando o layout customizado para o AlertDialog
                // inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);


                // bucando o elemento do título
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);

                _txtDialogTitle.setText(_sLetraInicial);

                // buscando o elemento ListView
                ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

                _lv.setAdapter(_adp);

                _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _sLetraInicial = _lstLetraInicial.get(position);
                        _txtDialogTitle.setText(_lstLetraInicial.get(position));
                    }
                });
                // endregion

                // region Criando a janela modal AlertDialog
                final AlertDialog.Builder _builder = new AlertDialog.Builder(ProdutoSearchActivity.this);

                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // aqui vamos realizar o refresh do grupo e do produto
                        refreshLetraInicial();
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

            }
        });
        // endregion



        // region Click em ARGUMENTO de PESQUISA do GRUPO DE PRODUTO
        _pnlProdutoGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Criando o adaptador para alista de linha de produto
                final GrupoDialogSearchAdapter _adp = new GrupoDialogSearchAdapter(ProdutoSearchActivity.this, _lstGrupo);
                // endregion

                // region Inflando o layout customizado para o AlertDialog
                // inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);


                // bucando o elemento do título
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);

                if(_iGrupo != -1) {

                    _txtDialogTitle.setText(_lstGrupo.get(_iGrupo).Descricao);

                }
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
                final AlertDialog.Builder _builder = new AlertDialog.Builder(ProdutoSearchActivity.this);

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

            }
        });
        // endregion

        // region Click em ARGUMENTO de PESQUISA do PRODUTO
        _pnlProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Abrindo janela para informar parte do nome
                MSVMsgBox.getStringValue(
                        ProdutoSearchActivity.this,
                        "PESQUISAR PRODUTO",
                        "Será pesquisado pelos campos: Código, Código de Barras e Descrição",
                        _txtProdutoDescricao.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                _sProduto = value.trim().toUpperCase();

                                PedidoMobileEditarActivity._PESQUISA_VALUE = _sProduto;

                                if (MSVUtil.isNullOrEmpty(_sProduto)) {

                                    _txtProdutoDescricao.setText("");

                                    Toast.makeText(
                                            ProdutoSearchActivity.this,
                                            "Nenhum argumento de pesquisa foi informado",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                } else {
                                    _txtProdutoDescricao.setText(_sProduto.toUpperCase());
                                    loadProduto();
                                }

                            }
                        }
                );
                // endregion

            }
        });

        // region Click no ITEM da LISTA de PRODUTO
        // aqui só vamos vincular o evento se a activity foi invocada a partir
        // da activity de itens do pedido
        _livProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // region Construindo os parametros de retorno
                Bundle _extras = new Bundle();
                //_extras.putString(_KEY_SOURCE_ACTIVITY, _sourceActivity);
                //_extras.putInt(_KEY_METODO_EDICAO, _metodoEdicao);
                _extras.putSerializable(_KEY_TP_PRODUTO, _lstProdutoSearch.get(position));
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
    public void initialize() {

        // region Cuidando de informações iniciais do form
        _txtProdutoDescricao.setText("");
        _txtRodapeRegistro.setText("REGISTROS: 0");
        // endregion

        // region Cuidando da apresentação do filtro para o usuário
        _IsShowFilter = true;
        refreshFilterContent();
        // endregion

        // region Atualizando os dados do grupo de produto
        this.loadGrupo();
        //endregion

        // region Atualizando os dados da tabela de preço
        this.refreshTabelaPreco();
        // endregion

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

        _txtProdutoGrupo.setText(_sGrupo);

    }
    // endregion

    // region refreshLetraInicial
    private void refreshLetraInicial() {

        _txtLetraInicial.setText(_sLetraInicial);

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
            _sch.addEqualInteger("IdEmpresa", _tpEmpresa.IdEmpresa);
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            dbGrupo _dbGrupo = new dbGrupo(_sqh);
            _lstGrupo = (ArrayList<tpGrupo>) _dbGrupo.getList(tpGrupo.class, _sch);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region refreshTabelaPreco
    private void refreshTabelaPreco() {

        _txtTabelaPrecoDescricao.setText(_tpTabelaPreco.Descricao);

    }
    // endregion


    // region loadProduto
    private void loadProduto() {

        // region Inicializando variáveis
        _iProduto = -1;
        int _idGrupoProduto = -1;
        // endregion

        // region Declarando objetos para uso do banco de dados
        SQLiteHelper _sqh = null;
        dbProduto _dbProduto = null;
        // endregion

        // region Bloco protegido
        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(getBaseContext());
            _sqh.open(false);
            // endregion

            // region Istânciando os objetos de acesso ao banco
            _dbProduto = new dbProduto(_sqh);
            // endregion

            // region Carregando a lista de produtos de acordo com a empresa e grupo

            if(_iGrupo != -1)
            {
                _idGrupoProduto = _lstGrupo.get(_iGrupo).IdGrupo;
            }

            //if (_sProduto.isEmpty() && _idGrupoProduto != -1) {
            //    _lstProdutoSearchResult = (ArrayList<tpProdutoSearch>) _dbProduto.searchByIdGrupo(_tpEmpresa.IdEmpresa, _tpTabelaPreco.IdTabelaPreco, _idGrupoProduto);
            //} else {
            //    _lstProdutoSearchResult = (ArrayList<tpProdutoSearch>) _dbProduto.search(_tpEmpresa.IdEmpresa, _tpTabelaPreco.IdTabelaPreco, _sProduto,_idGrupoProduto );
            //}

            _lstProdutoSearchResult = (ArrayList<tpProdutoSearch>) _dbProduto.search(_tpEmpresa.IdEmpresa, _tpTabelaPreco.IdTabelaPreco, _sProduto, _idGrupoProduto, _sLetraInicial);


            // endregion

            // region Trabalhando com a lista de produtos recuperada
            if (_lstProdutoSearchResult != null && _lstProdutoSearchResult.size() != 0) {

                // region Instânciando uma lista de produtos filtrados
                _lstProdutoSearch = _lstProdutoSearchResult;
                // endregion

                // region Verificando os itens existentes para alterar a cor
                if (_itens != null && _itens.size() > 0) {
                    setColorItensExistente();
                }
                // endregion

                // region Atualizando as informações do produto na tela
                this.refreshProduto();
                // endregion

            }else{
                _lstProdutoSearch = new ArrayList<tpProdutoSearch>();

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


    // region refreshProduto
    private void refreshProduto() {

        // atualizando o texto de pesquisa do filtro
        if (_sProduto.equalsIgnoreCase("")) {
            _txtProdutoDescricao.setText("TODOS");
        } else {
            _txtProdutoDescricao.setText(_sProduto.toUpperCase());
        }

        // atualizando a lista
        _adpProduto = new ProdutoSearchAdapter(
                ProdutoSearchActivity.this,
                _lstProdutoSearch
        );

        _livProduto.setAdapter(_adpProduto);

        /*
        if (_adpProduto == null) {
            _adpProduto = new ProdutoSearchAdapter(
                    ProdutoSearchActivity.this,
                    _lstProdutoSearch
            );

            _livProduto.setAdapter(_adpProduto);
        } else {
            _adpProduto._items = _lstProdutoSearch;
            _adpProduto.notifyDataSetChanged();
        }
        */

        // atualiando a quantidade de registros
        _txtRodapeRegistro.setText("REGISTROS: " + String.valueOf(_lstProdutoSearch.size()));
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


    // region
    private void setColorItensExistente() {

        for (int i = 0; i < _itens.size(); i++) {
            for (int p = 0; p < _lstProdutoSearch.size(); p++) {
                if (_itens.get(i).Produto.IdProduto == _lstProdutoSearch.get(p).IdProduto) {
                    _lstProdutoSearch.get(p).color = Color.rgb(35, 142, 35);
                    break;
                }
            }
        }
    }
    // endregion

}
