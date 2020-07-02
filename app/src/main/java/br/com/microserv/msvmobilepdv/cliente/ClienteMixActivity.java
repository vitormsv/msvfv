package br.com.microserv.msvmobilepdv.cliente;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbClienteMix;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdal.dbTabelaPreco;
import br.com.microserv.framework.msvdal.dbTabelaPrecoProduto;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteMix;
import br.com.microserv.framework.msvdto.tpEmpresa;
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
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.ClienteMixAdapter;
import br.com.microserv.msvmobilepdv.adapter.TabelaPrecoDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.pedido.PedidoMobileEditarActivity;

public class ClienteMixActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_ID_CLIENTE = "IdCliente";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;

    // endregion

    // region Declarando as variáveis que vinculam elementos do layout

    // View
    View _inCabecalho = null;
    View _inLista = null;
    View _inEditar = null;

    // Menu
    MenuItem _mnClientHide = null;
    MenuItem _mnClientShow = null;
    MenuItem _mnStockList = null;
    MenuItem _mnStockEdit = null;

    // LinearLayout
    LinearLayout _l_llyTitulo = null;
    LinearLayout _l_llyFilterCnt = null;
    LinearLayout _l_llyTabelaPreco = null;
    LinearLayout _e_llyEstoqueQuantidade = null;
    LinearLayout _e_llyEstoqueQuantidadeMenos = null;
    LinearLayout _e_llyEstoqueQuantidadeMais = null;
    LinearLayout _e_llyPedidoQuantidade = null;
    LinearLayout _e_llyPedidoQuantidadeMenos = null;
    LinearLayout _e_llyPedidoQuantidadeMais = null;
    LinearLayout _e_llyAnterior = null;
    LinearLayout _e_llyProximo = null;

    // ListView
    ListView _l_livMix = null;

    // TextView
    TextView _c_txtClienteNomeFantasia = null;
    TextView _c_txtClienteRazaoSocial = null;
    TextView _c_txtClienteDocumento = null;
    TextView _l_txtTitulo = null;
    TextView _l_txtTabelaPreco = null;
    TextView _l_txtRodapeRegistro = null;
    TextView _e_txtProdutoCodigo = null;
    TextView _e_txtProdutoEan13 = null;
    TextView _e_txtProdutoDescricao = null;
    TextView _e_txtProdutoUnidadeMedida = null;
    TextView _e_txtProdutoPreco = null;
    TextView _e_txtUltimaCompraData = null;
    TextView _e_txtUltimaCompraValor = null;
    TextView _e_txtUltimaCompraQuantidade = null;
    TextView _e_txtCompraQuantidadeMaior = null;
    TextView _e_txtEstoqueQuantidade = null;
    TextView _e_txtPedidoQuantidade = null;
    TextView _e_txtContador = null;

    // Switch
    Switch _e_swiGoToNext = null;
    Switch _e_swiEhItemConfirmado = null;

    // endregion

    // region Declarando outras variaveis de uso da activity

    // Objects
    Bundle _extras = null;
    ArrayList<tpClienteMix> _lstClienteMix = null;
    ArrayList<tpClienteMix> _lstClienteMixRemove = null;
    ArrayList<tpTabelaPreco> _lstTabelaPreco = null;
    ClienteMixAdapter _adpClienteMix = null;
    TabelaPrecoDialogSearchAdapter _adpTabelaPreco = null;
    tpEmpresa _tpEmpresa = null;
    tpCliente _tpCliente = null;

    // List of Objects

    // String
    String _sourceActivity = "";
    String _value_tabelaPrecoSelected = "";

    // long
    long _idCliente = 0;
    long _idEmpresa = 0;

    // int
    int _metodoEdicao = 0;
    int _iTabelaPreco = -1;
    int _iClienteMix = -1;
    int _index_tabelaPrecoSelected = 0;

    // Boolean
    boolean _IsShowFilter;

    // endregion

    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_mix);
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
                        ClienteMixActivity.this,
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
                        ClienteMixActivity.this,
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
                        ClienteMixActivity.this,
                        "O parâmetro _KEY_ID_CLIENTE não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_TP_EMPRESA
            if (_extras.containsKey(_KEY_TP_EMPRESA)) {
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);
                _idEmpresa = _tpEmpresa.IdEmpresa;
            } else {
                Toast.makeText(
                        ClienteMixActivity.this,
                        "O parâmetro _KEY_TP_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        }
        // endregion

        // region Invocando os metodos de inicialização activity
        this.bindElements();
        this.bindEvents();

        this.initialize();
        // endregion
    }
    // endregion

    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_mix_produto, menu);

        _mnClientHide = menu.findItem(R.id.mnClientHide);
        _mnClientShow = menu.findItem(R.id.mnClientShow);
        _mnStockList = menu.findItem(R.id.mnStockList);
        _mnStockEdit = menu.findItem(R.id.mnStockEdit);

        // region Cuidando da apresentação dos menus da toolbar
        _mnClientHide.setVisible(true);
        _mnClientShow.setVisible(false);
        _mnStockList.setVisible(false);
        _mnStockEdit.setVisible(true);
        // endregion

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

            case R.id.mnClientHide:
                // region Escondendo os dados do cliente
                _inCabecalho.setVisibility(View.GONE);

                _mnClientHide.setVisible(false);
                _mnClientShow.setVisible(true);
                // endregion
                break;

            case R.id.mnClientShow:
                // region Mostrando os dados do cliente
                _inCabecalho.setVisibility(View.VISIBLE);

                _mnClientHide.setVisible(true);
                _mnClientShow.setVisible(false);
                // endregion
                break;

            case R.id.mnStockList:
                // region Cuidando dos includes LISTA & CONTAGEM
                _inLista.setVisibility(View.GONE);
                _inEditar.setVisibility(View.GONE);

                _inLista.setVisibility(View.VISIBLE);
                // endregion

                // region Cuidando do menu
                _mnStockList.setVisible(false);
                _mnStockEdit.setVisible(true);
                // endregion
                break;

            case R.id.mnStockEdit:
                // region Cuidando dos includes LISTA & CONTAGEM
                _inLista.setVisibility(View.GONE);
                _inEditar.setVisibility(View.GONE);

                _inEditar.setVisibility(View.VISIBLE);
                // endregion

                // region Cuidando do menu
                _mnStockList.setVisible(true);
                _mnStockEdit.setVisible(false);
                // endregion

                // region Atualizando os dados do item do mix
                this.showMixItem();
                // endregion
                break;

            case R.id.mnSummary:
                // region Invocando o método que apresenta o resumo
                this.showSummary();
                // endregion
                break;

            case R.id.mnClear:
                // region Emitindo mensagem de confirmação
                MSVMsgBox.showMsgBoxQuestion(
                        ClienteMixActivity.this,
                        "Deseja realmente limpar as informações de quantidade em estoque, pedido e item confirmado para compra ?",
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    resetMix();
                                }
                            }
                        }
                );
                // endregion
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    // endregion

    // region bindElements
    @Override
    public void bindElements() {

        // region View
        _inCabecalho = (View) findViewById(R.id.inCabecalho);
        _inLista = (View) findViewById(R.id.inLista);
        _inEditar = (View) findViewById(R.id.inEditar);
        // endregion

        // region LinearLayout
        _l_llyTitulo = (LinearLayout) _inLista.findViewById(R.id.llyTitulo);
        _l_llyFilterCnt = (LinearLayout) _inLista.findViewById(R.id.llyFilterCnt);
        _l_llyTabelaPreco = (LinearLayout) _inLista.findViewById(R.id.llyTabelaPreco);
        _e_llyEstoqueQuantidade = (LinearLayout) _inEditar.findViewById(R.id.llyEstoqueQuantidade);
        _e_llyEstoqueQuantidadeMenos = (LinearLayout) _inEditar.findViewById(R.id.llyEstoqueQuantidadeMenos);
        _e_llyEstoqueQuantidadeMais = (LinearLayout) _inEditar.findViewById(R.id.llyEstoqueQuantidadeMais);
        _e_llyPedidoQuantidade = (LinearLayout) _inEditar.findViewById(R.id.llyPedidoQuantidade);
        _e_llyPedidoQuantidadeMenos = (LinearLayout) _inEditar.findViewById(R.id.llyPedidoQuantidadeMenos);
        _e_llyPedidoQuantidadeMais = (LinearLayout) _inEditar.findViewById(R.id.llyPedidoQuantidadeMais);
        _e_llyAnterior = (LinearLayout) _inEditar.findViewById(R.id.llyAnterior);
        _e_llyProximo = (LinearLayout) _inEditar.findViewById(R.id.llyProximo);
        // endregion

        // region ListView
        _l_livMix = (ListView) _inLista.findViewById(R.id.livMix);
        // endregion

        // region TextView
        _c_txtClienteNomeFantasia = (TextView) _inCabecalho.findViewById(R.id.txtClienteNomeFantasia);
        _c_txtClienteRazaoSocial = (TextView) _inCabecalho.findViewById(R.id.txtClienteRazaoSocial);
        _c_txtClienteDocumento = (TextView) _inCabecalho.findViewById(R.id.txtClienteDocumento);
        _l_txtTitulo = (TextView) _inLista.findViewById(R.id.txtTitulo);
        _l_txtTabelaPreco = (TextView) _inLista.findViewById(R.id.txtTabelaPreco);
        _l_txtRodapeRegistro = (TextView) _inLista.findViewById(R.id.txtRodapeRegistro);
        _e_txtProdutoCodigo = (TextView) _inEditar.findViewById(R.id.txtProdutoCodigo);
        _e_txtProdutoEan13 = (TextView) _inEditar.findViewById(R.id.txtProdutoEan13);
        _e_txtProdutoDescricao = (TextView) _inEditar.findViewById(R.id.txtProdutoDescricao);
        _e_txtProdutoUnidadeMedida = (TextView) _inEditar.findViewById(R.id.txtProdutoUnidadeMedida);
        _e_txtProdutoPreco = (TextView) _inEditar.findViewById(R.id.txtProdutoPreco);
        _e_txtUltimaCompraData = (TextView) _inEditar.findViewById(R.id.txtUltimaCompraData);
        _e_txtUltimaCompraValor = (TextView) _inEditar.findViewById(R.id.txtUltimaCompraValor);
        _e_txtUltimaCompraQuantidade = (TextView) _inEditar.findViewById(R.id.txtUltimaCompraQuantidade);
        _e_txtCompraQuantidadeMaior = (TextView) _inEditar.findViewById(R.id.txtCompraQuantidadeMaior);
        _e_txtEstoqueQuantidade = (TextView) _inEditar.findViewById(R.id.txtEstoqueQuantidade);
        _e_txtPedidoQuantidade = (TextView) _inEditar.findViewById(R.id.txtPedidoQuantidade);
        _e_txtContador = (TextView) _inEditar.findViewById(R.id.txtContador);
        // endregion

        // region Switch
        _e_swiEhItemConfirmado = (Switch) _inEditar.findViewById(R.id.swiEhItemConfirmado);
        _e_swiGoToNext = (Switch) _inEditar.findViewById(R.id.swiGoToNext);
        // endregion
    }
    // endregion

    // region bindEvents
    @Override
    public void bindEvents() {

        // region Evento CLICK em _l_llyFilterCnt
        _l_llyTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _IsShowFilter = !_IsShowFilter;
                refreshFilter();
            }
        });
        // endregion

        // region Evento CLICK em _l_llyTabelaPreco
        _l_llyTabelaPreco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_adpTabelaPreco == null) {
                    _adpTabelaPreco = new TabelaPrecoDialogSearchAdapter(
                            ClienteMixActivity.this,
                            _lstTabelaPreco
                    );
                }

                MSVMsgBox.getValueFromList(
                        ClienteMixActivity.this,
                        "Selecione a tabela de preço desejada",
                        _adpTabelaPreco,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _index_tabelaPrecoSelected = position;
                                _value_tabelaPrecoSelected = tp.getListIdentifierValue();
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _l_txtTabelaPreco.setText(_value_tabelaPrecoSelected);
                                _iTabelaPreco = _index_tabelaPrecoSelected;
                                loadMix();
                                refreshMix();
                            }
                        }
                );
            }
        });
        // endregion

        // region Evento CLICK em _l_liv
        _l_livMix.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                _iClienteMix = i;
                showMixItem();

                onOptionsItemSelected(_mnStockEdit);
            }
        });
        // endregion

        // region Evento CLICK em _e_llyEstoqueQuantidade
        _e_llyEstoqueQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_e_swiEhItemConfirmado.isChecked()) {

                    // region Emitindo mensagem de bloqueio para o usuário
                    Toast.makeText(
                            ClienteMixActivity.this,
                            "Não é possível alterar informações de um item confirmado",
                            Toast.LENGTH_SHORT
                    ).show();
                    // endregion

                } else {

                    // region Abrindo janela para digitação da quantidade em estoque
                    MSVMsgBox.getIntValue(
                            ClienteMixActivity.this,
                            "QUANTIDADE ESTOQUE",
                            "Informe abaixo a quantidade total do item em estoque no cliente",
                            _lstClienteMix.get(_iClienteMix).EstoqueQuantidade,
                            new OnCloseDialog() {
                                @Override
                                public void onCloseDialog(boolean isOk, String value) {
                                    if (isOk) {
                                        if (value.trim().isEmpty() == false) {
                                            _lstClienteMix.get(_iClienteMix).EstoqueQuantidade = Integer.parseInt(value);
                                            showMixItem();
                                        } else {
                                            MSVMsgBox.showMsgBoxWarning(
                                                    ClienteMixActivity.this,
                                                    "Nenhum valor foi informado para a quantidade em estoque",
                                                    "Nenhum valor novo foi informado, o valor atual da quantidade em estoque será mantido o mesmo"
                                            );
                                        }
                                    }
                                }
                            }
                    );
                    // endregion
                }
            }
        });
        // endregion

        // region Evento CLICK _e_llyEstoqueQuantidadeMenos
        _e_llyEstoqueQuantidadeMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_e_swiEhItemConfirmado.isChecked()) {

                    // region Emitindo mensagem de bloqueio para o usuário
                    Toast.makeText(
                            ClienteMixActivity.this,
                            "Não é possível alterar informações de um item confirmado",
                            Toast.LENGTH_SHORT
                    ).show();
                    // endregion

                } else {

                    // region Recuperando o item em questão
                    tpClienteMix _tp = _lstClienteMix.get(_iClienteMix);
                    // endregion

                    // region Verificando se é possível subtrair uma unidade para estoque
                    if (_tp.EstoqueQuantidade > 0) {

                        // region Subtraindo uma unidade no campo de estoque
                        _tp.EstoqueQuantidade -= 1;
                        // endregion

                        // region Atualizando o campo de quantidade no pedido
                        if ((_tp.CompraQuantidadeMaior - _tp.EstoqueQuantidade) < 0) {
                            _tp.PedidoQuantidade = 0;
                        } else {
                            _tp.PedidoQuantidade = (_tp.CompraQuantidadeMaior - _tp.EstoqueQuantidade);
                        }
                        // endregion

                    } else {

                        // region Emitindo mensagem ao usuário
                        Toast.makeText(
                                ClienteMixActivity.this,
                                "A quantidade em estoque não pode ser menor do que zero",
                                Toast.LENGTH_SHORT
                        ).show();
                        // endregion
                    }
                    // endregion

                    // region Atualizando os dados dos items na tela
                    showMixItem();
                    // endregion
                }
            }
        });
        // endregion

        // region Evento CLICK _e_llyEstoqueQuantidadeMais
        _e_llyEstoqueQuantidadeMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_e_swiEhItemConfirmado.isChecked()) {

                    // region Emitindo mensagem de bloqueio para o usuário
                    Toast.makeText(
                            ClienteMixActivity.this,
                            "Não é possível alterar informações de um item confirmado",
                            Toast.LENGTH_SHORT
                    ).show();
                    // endregion

                } else {

                    // region Recuperando o item em questão
                    tpClienteMix _tp = _lstClienteMix.get(_iClienteMix);
                    // endregion

                    // region Adicionando mais uma unidade ao campo estoque
                    _tp.EstoqueQuantidade += 1;
                    // endregion

                    // region Atualizando o campo de quantidade no pedido
                    if (_tp.EstoqueQuantidade >= _tp.CompraQuantidadeMaior) {
                        _tp.PedidoQuantidade = 0;
                    } else {
                        _tp.PedidoQuantidade = (_tp.CompraQuantidadeMaior - _tp.EstoqueQuantidade);
                    }
                    // endregion

                    // region Atualizando os dados dos items na tela
                    showMixItem();
                    // endregion
                }
            }
        });
        // endregion

        // region Evento CLICK em _e_llyPedidoQuantidade
        _e_llyPedidoQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_e_swiEhItemConfirmado.isChecked()) {

                    // region Emitindo mensagem de bloqueio para o usuário
                    Toast.makeText(
                            ClienteMixActivity.this,
                            "Não é possível alterar informações de um item confirmado",
                            Toast.LENGTH_SHORT
                    ).show();
                    // endregion

                } else {

                    // region Abrindo a janela para a digitação da quantidade em pedido
                    MSVMsgBox.getIntValue(
                            ClienteMixActivity.this,
                            "PEDIDO QUANTIDADE",
                            "Informe abaixo a quantidade total do item que será adquirida pelo cliente",
                            _lstClienteMix.get(_iClienteMix).PedidoQuantidade,
                            new OnCloseDialog() {
                                @Override
                                public void onCloseDialog(boolean isOk, String value) {
                                    if (isOk) {
                                        if (value.trim().isEmpty() == false) {
                                            _lstClienteMix.get(_iClienteMix).PedidoQuantidade = Integer.parseInt(value);
                                            showMixItem();
                                        } else {
                                            MSVMsgBox.showMsgBoxWarning(
                                                    ClienteMixActivity.this,
                                                    "Nenhum valor informado para o campo quantidade em pedido",
                                                    "Nenhum valor novo foi informado, o valor atual da quantidade em pedido será mantido o mesmo"
                                            );
                                        }
                                    }
                                }
                            }
                    );
                    // endregion
                }
            }
        });
        // endregion

        // region Evento CLICK _e_llyPedidoQuantidadeMenos
        _e_llyPedidoQuantidadeMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_e_swiEhItemConfirmado.isChecked()) {

                    // region Emitindo mensagem de bloqueio para o usuário
                    Toast.makeText(
                            ClienteMixActivity.this,
                            "Não é possível alterar informações de um item confirmado",
                            Toast.LENGTH_SHORT
                    ).show();
                    //  endregion

                } else {

                    // region Recuperando o item em questão
                    tpClienteMix _tp = _lstClienteMix.get(_iClienteMix);
                    // endregion

                    // region Verificando se é possível subtrair uma unidade na quantidade do pedido
                    if (_tp.PedidoQuantidade == 0) {

                        // region Emitindo mensagem de alerta para o usuário
                        Toast.makeText(
                                ClienteMixActivity.this,
                                "Atenção, a quantidade de itens para o pedido já é zero e não pode ficar negativa",
                                Toast.LENGTH_SHORT
                        ).show();
                        // endregion

                    } else {

                        _tp.PedidoQuantidade -= 1;
                    }
                    // endregion

                    // region Imprimindo os dados do items na tela
                    showMixItem();
                    // endregion
                }
            }
        });
        // endregion

        // region Evento CLICK _e_llyPedidoQuantidadeMais
        _e_llyPedidoQuantidadeMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_e_swiEhItemConfirmado.isChecked()) {

                    // region Emitindo mensagem de bloqueio para o usuário
                    Toast.makeText(
                            ClienteMixActivity.this,
                            "Não é possível alterar informações de um item confirmado",
                            Toast.LENGTH_SHORT
                    ).show();
                    // endregion

                } else {

                    // region Recuperando o item em questão
                    tpClienteMix _tp = _lstClienteMix.get(_iClienteMix);
                    // endregion

                    // region Adicionando mais uma qantidade para o campo de estoque
                    _tp.PedidoQuantidade += 1;
                    // endregion

                    // region Imprimendo o item já processado no produto
                    showMixItem();
                    // endregion
                }
            }
        });
        // endregion

        // region Evento CLICK em _e_llyAnterior
        _e_llyAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_iClienteMix == 0) {

                    Toast.makeText(
                            ClienteMixActivity.this,
                            "Este já é o primeiro item do mix de produtos do cliente",
                            Toast.LENGTH_LONG
                    ).show();

                } else {

                    _iClienteMix -= 1;
                    showMixItem();

                    MSVUtil.vibrate(ClienteMixActivity.this);
                }
            }
        });
        // endregion

        // region Evento CLICK em _e_llyProximo
        _e_llyProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_iClienteMix == (_lstClienteMix.size() - 1)) {

                    Toast.makeText(
                            ClienteMixActivity.this,
                            "Este já é o útlimo item do mix de produtos do cliente",
                            Toast.LENGTH_LONG
                    ).show();

                } else {

                    _iClienteMix += 1;
                    showMixItem();

                    MSVUtil.vibrate(ClienteMixActivity.this);

                }
            }
        });
        // endregion

        // region Evento CLICK em EhItemConfirmado
        _e_swiEhItemConfirmado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // region Recuperando o item em questão e tratando a confirmação do mesmo
                tpClienteMix _tp = _lstClienteMix.get(_iClienteMix);
                _tp.EhItemConfirmado = b == true ? 1 : 0;
                // endregion

                // region Tantando atualizar as informações do item no banco de dados
                if (!updateItem(_tp)) {
                    _e_swiEhItemConfirmado.setChecked(!b);
                } else {
                    _adpClienteMix.updateItem(_tp, _iClienteMix);
                }
                // endregion

                // region Verificando se devemos ir para o próximo item
                if (b == true && _e_swiGoToNext.isChecked()) {
                    _e_llyProximo.callOnClick();
                }
                // endregion
            }
        });
        // endregion
    }
    // endregion

    // region initialize
    @Override
    public void initialize() {

        // region Cuidando da apresentação do filtro para o usuário
        _IsShowFilter = false;
        refreshFilter();
        // endregion

        try {

            // region Carregando os dados do cliente
            this.loadCliente();
            this.refreshCliente();
            // endregion

            // region Carregando os dados das tabelas de preço
            this.loadTabelaPreco();
            this.refreshTabelaPreco();
            // endregion

            // region Carregando os dados do mix de produtos
            this.loadMix();
            this.refreshMix();
            // endregion

        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    ClienteMixActivity.this,
                    "Erro ao iniciar o mix de produtos do cliente",
                    e.getMessage()
            );
        }
    }
    // endregion

    // ######################## NOVOS METODOS AQUI ########################

    // region refreshFilter
    private void refreshFilter() {

        if (_IsShowFilter) {
            // mostrar o filtro
            _l_llyFilterCnt.setVisibility(View.VISIBLE);
            _l_txtTitulo.setText("( - ) ESCONDER FILTRO");
        } else {
            // esconder o filtro
            _l_llyFilterCnt.setVisibility(View.GONE);
            _l_txtTitulo.setText("( + ) MOSTRAR FILTRO");
        }
    }
    // endregion

    // region Método loadCliente
    private void loadCliente() {

        // region Declarando as variáveis locais do método
        SQLiteHelper _sqlHelper = null;
        dbCliente _dbCliente = null;
        // endregion

        // region Método protegido de exceção
        try {

            // region Abrindo a conexão com o banco de dados
            _sqlHelper = new SQLiteHelper(ClienteMixActivity.this);
            _sqlHelper.open(false);
            // endregion

            // region Recuperando os dados do cliente
            _dbCliente = new dbCliente(_sqlHelper);
            _tpCliente = (tpCliente) _dbCliente.getBySourceId(_idCliente);
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteMixActivity.this,
                    "Erro ao realizar a leitura dos dados do cliente",
                    e.getMessage()
            );

        } finally {

            if ((_sqlHelper != null) && (_sqlHelper.isOpen())) {
                _sqlHelper.close();
            }
        }
        // endregion
    }
    // endregion

    // region Método loadTabelaPreco
    private void loadTabelaPreco() {

        // region Declarando as variáveis locais do método
        SQLiteHelper _sqlHelper = null;
        dbTabelaPreco _dbTabelaPreco = null;
        SQLClauseHelper _whereTabelaPreco = null;
        // endregion

        // region Método protegido de exceção
        try {

            // region Inicializando variáveis de controle de tabela de preço
            _iTabelaPreco = -1;
            // endregion

            // region Montando o where para a tabela ClienteMix
            _whereTabelaPreco = new SQLClauseHelper();
            _whereTabelaPreco.addEqualInteger("IdEmpresa", _idEmpresa);
            // endregion

            // region Abrindo a conexão com o banco de dados
            _sqlHelper = new SQLiteHelper(ClienteMixActivity.this);
            _sqlHelper.open(false);
            // endregion

            // region Recuperando o mix de produtos do cliente
            _dbTabelaPreco = new dbTabelaPreco(_sqlHelper);
            _lstTabelaPreco = (ArrayList<tpTabelaPreco>) _dbTabelaPreco.getList(tpTabelaPreco.class, _whereTabelaPreco);
            // endregion

            // region Organizando as tabelas por ordem de descrição da tabela
            if ((_lstTabelaPreco != null) && (_lstTabelaPreco.size() > 0)) {

                int indexTabelaPadrao = 0;
                for (tpTabelaPreco item : _lstTabelaPreco) {
                    if (item.IdTabelaPreco == _tpCliente.IdTabelaPreco) {
                        break;
                    }
                    indexTabelaPadrao++;
                }

                _iTabelaPreco = indexTabelaPadrao >= _lstTabelaPreco.size() ? 0 : indexTabelaPadrao;

            } else {

                MSVMsgBox.showMsgBoxError(
                        ClienteMixActivity.this,
                        "Erro ao realizar a leitura das tabelas de preço",
                        "Para que esta funcionalidade possa ser executada de maneira correta é necessário existir pelo menos uma tabela de preço associada a empresa"
                );

                finish();
            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteMixActivity.this,
                    "Erro ao realizar a leitura do mix de produtos do cliente",
                    e.getMessage()
            );

        } finally {

            if ((_sqlHelper != null) && (_sqlHelper.isOpen())) {
                _sqlHelper.close();
            }
        }
        // endregion
    }
    // endregion

    // region Método loadMix
    private void loadMix() {

        // region Declarando as variáveis locais do método
        SQLiteHelper _sqlHelper = null;
        dbClienteMix _dbClienteMix = null;
        dbProduto _dbProduto = null;
        dbTabelaPrecoProduto _dbTabelaPrecoProduto = null;
        SQLClauseHelper _whereClienteMix = null;
        SQLClauseHelper _whereProduto = null;
        SQLClauseHelper _whereTabelaPrecoProduto = null;
        // endregion

        // region Método protegido de exceção
        try {

            // region Inicializando variáveis de controle de tabela de preço
            _iClienteMix = -1;
            _lstClienteMix = null;
            _lstClienteMixRemove = null;
            // endregion

            // region Montando o where para a tabela ClienteMix
            _whereClienteMix = new SQLClauseHelper();
            _whereClienteMix.addEqualInteger("IdCliente", _idCliente);
            _whereClienteMix.addEqualInteger("IdEmpresa", _tpEmpresa.IdEmpresa);
            // endregion

            // region Abrindo a conexão com o banco de dados
            _sqlHelper = new SQLiteHelper(ClienteMixActivity.this);
            _sqlHelper.open(false);
            // endregion

            // region Recuperando o mix de produtos do cliente
            _dbClienteMix = new dbClienteMix(_sqlHelper);
            _lstClienteMix = (ArrayList<tpClienteMix>) _dbClienteMix.getList(tpClienteMix.class, _whereClienteMix);
            // endregion

            // region Recuperando informações acessorias para cada item do mix
            if ((_lstClienteMix != null) && (_lstClienteMix.size() > 0)) {

                for (tpClienteMix _tp : _lstClienteMix) {

                    // region Buscando as informações do produto

                    // region Montando a clausula where
                    if (_whereProduto == null) {
                        _whereProduto = new SQLClauseHelper();
                    } else {
                        _whereProduto.clearAll();
                    }

                    _whereProduto.addEqualInteger("IdProduto", _tp.IdProduto);
                    // endregion

                    // region Submetendo a consulta ao banco
                    if (_dbProduto == null) {
                        _dbProduto = new dbProduto(_sqlHelper);
                    }

                    _tp.Produto = (tpProduto) _dbProduto.getOne(_whereProduto);
                    // endregion

                    // endregion

                    // region Buscando as informações do preço do produto

                    // region Montando a clausula where
                    if (_whereTabelaPrecoProduto == null) {
                        _whereTabelaPrecoProduto = new SQLClauseHelper();
                    } else {
                        _whereTabelaPrecoProduto.clearAll();
                    }

                    _whereTabelaPrecoProduto.addEqualInteger("IdProduto", _tp.IdProduto);
                    _whereTabelaPrecoProduto.addEqualInteger("IdTabelaPreco", _lstTabelaPreco.get(_iTabelaPreco).IdTabelaPreco);
                    // endregion

                    // region Submetendo a consulta ao banco
                    if (_dbTabelaPrecoProduto == null) {
                        _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqlHelper);
                    }

                    _tp.TabelaPrecoProduto = (tpTabelaPrecoProduto) _dbTabelaPrecoProduto.getOne(_whereTabelaPrecoProduto);
                    // endregion

                    // endregion

                    // region verificando se o item deve ser removido da lista
                    if ((_tp.Produto == null) || (_tp.TabelaPrecoProduto == null)) {

                        if (_lstClienteMixRemove == null) {
                            _lstClienteMixRemove = new ArrayList<tpClienteMix>();
                        }

                        _lstClienteMixRemove.add(_tp);
                    }
                    // endregion
                }
            }
            // endregion

            // region Removendo os itens que não tem produto ou preço relacionado
            if ((_lstClienteMixRemove != null) && (_lstClienteMixRemove.size() > 0)) {
                _lstClienteMix.removeAll(_lstClienteMixRemove);
                _lstClienteMixRemove = null;
            }
            // endregion

            // region Organizando o mix do cliente por ordem de descrição do produto
            if ((_lstClienteMix != null) && (_lstClienteMix.size() > 0)) {

                Collections.sort(_lstClienteMix, new Comparator<tpClienteMix>() {
                    public int compare(tpClienteMix p1, tpClienteMix p2) {
                        return p1.Produto.Descricao.compareTo(p2.Produto.Descricao);
                    }
                });

                _iClienteMix = 0;

            } else {

                _l_livMix.setAdapter(null);
                _adpClienteMix = null;

                Toast.makeText(
                        ClienteMixActivity.this,
                        "Não existe um mix de produtos configurados para este cliente",
                        Toast.LENGTH_LONG
                ).show();
            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteMixActivity.this,
                    "Erro ao realizar a leitura do mix de produtos do cliente",
                    e.getMessage()
            );

        } finally {

            if ((_sqlHelper != null) && (_sqlHelper.isOpen())) {
                _sqlHelper.close();
            }
        }
        // endregion
    }
    // endregion

    // region Método refreshCliente
    private void refreshCliente() {

        // region Limpando os dados contidos nas views
        _c_txtClienteNomeFantasia.setText("");
        _c_txtClienteRazaoSocial.setText("");
        _c_txtClienteDocumento.setText("");
        // endregion

        // region Imprimindo nas vies os valores referente ao cliente
        if (_tpCliente != null) {

            _c_txtClienteNomeFantasia.setText(_tpCliente.NomeFantasia);
            _c_txtClienteRazaoSocial.setText(_tpCliente.RazaoSocial);

            if (_tpCliente.CnpjCpf.length() == 14) {
                _c_txtClienteDocumento.setText(MSVUtil.formatCnpj(_tpCliente.CnpjCpf));
            } else {
                _c_txtClienteDocumento.setText(MSVUtil.formatCpf(_tpCliente.CnpjCpf));
            }
        }
        // endregion
    }
    // endregion

    // region Método refreshTabelaPreco
    private void refreshTabelaPreco() {

        _l_txtTabelaPreco.setText("");

        if (_iTabelaPreco >= 0) {
            _l_txtTabelaPreco.setText(_lstTabelaPreco.get(_iTabelaPreco).Descricao);
        }

    }
    // endregion

    // region Método refreshMix
    private void refreshMix() {

        _l_txtRodapeRegistro.setText("REGISTROS: 0");

        if (_lstClienteMix != null) {

            if (_adpClienteMix == null) {

                _adpClienteMix = new ClienteMixAdapter(
                        ClienteMixActivity.this,
                        _lstClienteMix
                );

                _l_livMix.setAdapter(_adpClienteMix);

            } else {

                _adpClienteMix.updateItems(_lstClienteMix);
            }

            _l_txtRodapeRegistro.setText("REGISTROS: " + _lstClienteMix.size());
        }
    }
    // endregion

    // region Método showMixItem
    private void showMixItem() {

        try {

            // region Recuperando o item selecionado
            tpClienteMix _tp = _lstClienteMix.get(_iClienteMix);
            // endregion

            // region Verificando a necessidade de sugerir a quantidade de compra
            if (_tp.EstoqueQuantidade == 0 && _tp.PedidoQuantidade == 0) {
                _tp.PedidoQuantidade = _tp.CompraQuantidadeMaior;
            }
            // endregion

            // region Imprimindo na tela os valores do item selecionado
            _e_txtProdutoCodigo.setText(_tp.Produto.Codigo);
            _e_txtProdutoEan13.setText(_tp.Produto.Ean13);
            _e_txtProdutoDescricao.setText(_tp.Produto.Descricao);
            _e_txtProdutoUnidadeMedida.setText(_tp.Produto.UnidadeMedida + " - " + String.valueOf(_tp.Produto.PackQuantidade));
            _e_txtProdutoPreco.setText(MSVUtil.doubleToText("R$", _tp.TabelaPrecoProduto.Preco));
            _e_txtUltimaCompraData.setText(MSVUtil.ymdhmsTOdmy(_tp.UltimaCompraData));
            _e_txtUltimaCompraValor.setText(MSVUtil.doubleToText("R$", _tp.UltimaCompraValor));
            _e_txtUltimaCompraQuantidade.setText(String.valueOf(_tp.UltimaCompraQuantidade));
            _e_txtCompraQuantidadeMaior.setText(String.valueOf(_tp.CompraQuantidadeMaior));
            _e_txtEstoqueQuantidade.setText(String.valueOf(_tp.EstoqueQuantidade));
            _e_txtPedidoQuantidade.setText(String.valueOf(_tp.PedidoQuantidade));
            _e_swiEhItemConfirmado.setChecked(_tp.EhItemConfirmado == 1);
            // endregion

            // region Atualizando o contador de registros
            _e_txtContador.setText(String.format("[ %d/%d]", _iClienteMix + 1, _lstClienteMix.size()));
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteMixActivity.this,
                    "[ClienteMixActivity|showMixItem()] - Erro ao atualizar as informações do item",
                    e.getMessage()
            );

            finish();
        }
    }
    // endregion

    // region Método updateItem
    private boolean updateItem(tpClienteMix item) {

        boolean _out = false;

        SQLiteHelper _sqh = null;
        dbClienteMix _dbClienteMix = null;

        try {

            _sqh = new SQLiteHelper(ClienteMixActivity.this);
            _sqh.open(true);

            _dbClienteMix = new dbClienteMix(_sqh);
            _dbClienteMix.update(item);

            _out = true;

        } catch (Exception e) {

            _out = false;

            MSVMsgBox.showMsgBoxError(
                    ClienteMixActivity.this,
                    "Erro ao atualizar os valores do item no banco de dados",
                    e.getMessage()
            );

        } finally {

            if (_sqh != null && _sqh.isOpen() == false) {
                _sqh.close();
            }
        }

        return _out;
    }
    // endregion

    // region Método resetMix
    private void resetMix() {

        // region Declarando variáveis locais do método
        SQLiteHelper _sqh = null;
        dbClienteMix _dbClienteMix = null;
        // endregion

        try {

            // region Cuidando da conexão com o banco de dados
            _sqh = new SQLiteHelper(ClienteMixActivity.this);
            _sqh.open(true);
            // endregion

            // region Instânciando objeto de acesso ao banco para o mix de produtos
            _dbClienteMix = new dbClienteMix(_sqh);
            // endregion

            // region Para cada item vamos zerar as informações de estoque e pedido
            for (tpClienteMix _tp : _lstClienteMix) {

                _tp.EstoqueQuantidade = 0;
                _tp.PedidoQuantidade = 0;
                _tp.EhItemConfirmado = 0;

                _dbClienteMix.update(_tp);
            }
            // endregion

            // region Atualizando a lista de itens
            _adpClienteMix.updateItems(_lstClienteMix);
            // endregion

            // region Ativando a guia de lista do mix
            onOptionsItemSelected(_mnStockList);
            // endregion

        } catch (Exception e) {

            // region Emitindo mensagem de erro tratada para o usuário
            MSVMsgBox.showMsgBoxError(
                    ClienteMixActivity.this,
                    "Erro ao limpar a digitação de estoque e pedido no mix de produto do cliente",
                    e.getMessage()
            );
            // endregion
        } finally {

            // region Encerrando a conexão com o banco de dados
            if (_sqh != null && _sqh.isOpen() == false) {
                _sqh.close();
            }
            // endregion
        }
    }
    // endregion

    // region Metodo showSummary
    private void showSummary() {

        // region Gerando a informação que será impressa na caixa de dialogo
        int _quantidadeTotal = 0;
        int _quantidadeOfertada = 0;
        double _valorTotal = 0;

        _quantidadeTotal = _lstClienteMix.size();

        for (tpClienteMix _tp : _lstClienteMix) {
            if (_tp.EhItemConfirmado == 1) {
                _quantidadeOfertada += 1;
                _valorTotal += (_tp.PedidoQuantidade * _tp.TabelaPrecoProduto.Preco);
            }
        }
        // endregion

        // region Inflando o layout customizado para a janela do resumo

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_cliente_mix_resumo, null);
        // endregion

        // region Imprimindo a quantidade de itens selecionados para compra
        TextView _txtQuantidade = (TextView) _v.findViewById(R.id.txtQuantidade);
        _txtQuantidade.setText(String.valueOf(_quantidadeOfertada));
        // endregion

        // region Imprimindo o valor total da compra
        TextView _txtValorTotal = (TextView) _v.findViewById(R.id.txtValorTotal);
        _txtValorTotal.setText(MSVUtil.doubleToText("R$", _valorTotal));
        // endregion

        // region Montando o grafico de itens ofertados
        LinearLayout _llyQuantidadeTotal = (LinearLayout) _v.findViewById(R.id.llyQuantidadeTotal);
        LinearLayout _llyQuantidadeOfertada = (LinearLayout) _v.findViewById(R.id.llyQuantidadeOfertada);

        _llyQuantidadeTotal.getLayoutParams().width = 400;
        _llyQuantidadeOfertada.requestLayout();

        _llyQuantidadeOfertada.getLayoutParams().width = (int) ((400 * _quantidadeOfertada) / _quantidadeTotal);
        _llyQuantidadeOfertada.requestLayout();
        // endregion

        // region Imprimindo a quantidade de itens ofertados dentro do total de itens do mix
        TextView _txtQuantidadeOfertada = (TextView) _v.findViewById(R.id.txtQuantidadeOfertada);
        _txtQuantidadeOfertada.setText(String.valueOf(_quantidadeOfertada) + " de " + String.valueOf(_quantidadeTotal));
        // endregion

        // endregion

        // region Montando a janela de dialogo
        final AlertDialog.Builder _builder = new AlertDialog.Builder(ClienteMixActivity.this);

        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // não faz nada
            }
        });

        AlertDialog _dialog = _builder.create();
        _dialog.show();
        // endregion
    }
    // endregion
}
