package br.com.microserv.msvmobilepdv.cliente;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbClienteMix;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteProdutoMixRow;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.ClienteMixAdapter;

public class ClienteProdutoMixListaActivity extends AppCompatActivity implements ActivityInterface {

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
    View _inLista = null;
    View _inEditar = null;

    // LinearLayout
    LinearLayout _l_pnlResumo = null;
    LinearLayout _l_pnlRodape = null;
    LinearLayout _e_pnlEstoqueQuantidade = null;
    LinearLayout _e_pnlComprarQuantidade = null;


    // ListView
    ListView _l_livProduto = null;


    // TextView
    TextView _txtClienteNomeFantasia = null;
    TextView _txtClienteRazaoSocial = null;
    TextView _txtClienteDocumento = null;

    TextView _l_txtResumoQuantidade = null;
    TextView _l_txtResumoTotal = null;
    TextView _l_txtRodape = null;

    TextView _e_txtProdutoCodigo = null;
    TextView _e_txtProdutoEan13 = null;
    TextView _e_txtProdutoDescricao = null;
    TextView _e_txtUltimaCompraData = null;
    TextView _e_txtCompraQuantidadeMaior = null;
    TextView _e_txtEstoqueQuantidade = null;
    TextView _e_txtComprarQuantidade = null;
    TextView _e_txtUnidadeValor = null;
    TextView _e_txtTotalValor = null;


    // Button
    Button _e_btnCancelar = null;
    Button _e_btnOk = null;

    // endregion


    // region Declarando outras variaveis de uso da activity

    // Objects
    Bundle _extras = null;
    tpEmpresa _tpEmpresa = null;
    tpCliente _tpCliente = null;
    tpClienteProdutoMixRow _tpSelected = null;

    // List of Objects
    ArrayList<tpClienteProdutoMixRow> _lstProdutoMix = null;

    // String
    String _sourceActivity = "";

    // long
    long _idCliente = 0;

    // int
    int _metodoEdicao = 0;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_produto_mix_lista);
        // endregion


        // region Adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        // endregion


        // region Selecionando os parametros enviados através do Bundle
        Bundle _extras = getIntent().getExtras();

        if (_extras != null) {

            // region _KEY_METODO_EDICAO
            if (_extras.containsKey(_KEY_METODO_EDICAO)) {
                _metodoEdicao = _extras.getInt(_KEY_METODO_EDICAO);
            } else {
                Toast.makeText(
                        ClienteProdutoMixListaActivity.this,
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
                        ClienteProdutoMixListaActivity.this,
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
                        ClienteProdutoMixListaActivity.this,
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
                        ClienteProdutoMixListaActivity.this,
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

            case R.id.mnClear:

                this.limparDigitacao();

                break;

        }

        return super.onOptionsItemSelected(item);

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // region View
        _inLista = (View) findViewById(R.id.inLista);
        _inEditar = (View) findViewById(R.id.inEditar);
        // endregion


        // region LinearLayout
        _l_pnlResumo = (LinearLayout) _inLista.findViewById(R.id.pnlResumo);
        _l_pnlRodape = (LinearLayout) _inLista.findViewById(R.id.pnlRodape);
        _e_pnlEstoqueQuantidade = (LinearLayout) _inEditar.findViewById(R.id.pnlEstoqueQuantidade);
        _e_pnlComprarQuantidade = (LinearLayout) _inEditar.findViewById(R.id.pnlComprarQuantidade);
        // endregion


        // region TextView
        _txtClienteNomeFantasia = (TextView) findViewById(R.id.txtClienteNomeFantasia);
        _txtClienteRazaoSocial = (TextView) findViewById(R.id.txtClienteRazaoSocial);
        _txtClienteDocumento = (TextView) findViewById(R.id.txtClienteDocumento);

        _l_txtResumoQuantidade = (TextView) _inLista.findViewById(R.id.txtResumoQuantidade);
        _l_txtResumoTotal = (TextView) _inLista.findViewById(R.id.txtResumoTotal);
        _l_txtRodape = (TextView) _inLista.findViewById(R.id.txtRodape);

        _e_txtProdutoCodigo = (TextView) _inEditar.findViewById(R.id.txtProdutoCodigo);
        _e_txtProdutoEan13 = (TextView) _inEditar.findViewById(R.id.txtProdutoEan13);
        _e_txtProdutoDescricao = (TextView) _inEditar.findViewById(R.id.txtProdutoDescricao);
        _e_txtUltimaCompraData = (TextView) _inEditar.findViewById(R.id.txtUltimaCompraData);
        _e_txtCompraQuantidadeMaior = (TextView) _inEditar.findViewById(R.id.txtCompraQuantidadeMaior);
        _e_txtEstoqueQuantidade = (TextView) _inEditar.findViewById(R.id.txtEstoqueQuantidade);
        _e_txtComprarQuantidade = (TextView) _inEditar.findViewById(R.id.txtComprarQuantidade);
        _e_txtUnidadeValor = (TextView) _inEditar.findViewById(R.id.txtUnidadeValor);
        _e_txtTotalValor = (TextView) _inEditar.findViewById(R.id.txtTotalValor);
        // endregion


        // region Button
        _e_btnCancelar = (Button) _inEditar.findViewById(R.id.btnCancelar);
        _e_btnOk = (Button) _inEditar.findViewById(R.id.btnOk);
        // endregion


        // region ListView
        _l_livProduto = (ListView) _inLista.findViewById(R.id.livProduto);
        // endregion

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Click no item da lista
        _l_livProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                _tpSelected = (tpClienteProdutoMixRow) _l_livProduto.getAdapter().getItem(position);

                _e_txtProdutoCodigo.setText(_tpSelected.ProdutoCodigo);
                _e_txtProdutoEan13.setText(_tpSelected.ProdutoEan13);
                _e_txtProdutoDescricao.setText(_tpSelected.ProdutoDescricao);
                _e_txtUltimaCompraData.setText(MSVUtil.timeMillisToText(_tpSelected.UltimaCompraData));
                _e_txtCompraQuantidadeMaior.setText(String.valueOf(_tpSelected.CompraQuantidadeMaior));
                _e_txtEstoqueQuantidade.setText(String.valueOf(_tpSelected.EstoqueQuantidade));
                _e_txtComprarQuantidade .setText(String.valueOf(_tpSelected.ComprarQuantidade));
                _e_txtUnidadeValor.setText(MSVUtil.doubleToText("R$", _tpSelected.Preco));
                _e_txtTotalValor.setText(MSVUtil.doubleToText("R$", _tpSelected.Preco * _tpSelected.ComprarQuantidade));

                _inLista.setVisibility(View.GONE);
                _inEditar.setVisibility(View.VISIBLE);

            }
        });
        // endregion


        // region Click em quantidade no estoque
        _e_pnlEstoqueQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_inteiro, null);

                // informando o título da janela
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
                _txtDialogTitle.setText("QUANTIDADE");

                // informando a mensagem referente ao campo
                final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
                _txtDialogMessage.setText("Informe a quantidade do item que está no estoque do cliente");

                // preenchendo o valor corrente
                final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
                _txtDialogCurrentValue.setText(MSVUtil.doubleToText(_tpSelected.EstoqueQuantidade));

                // cuidando o campo que permitie a digitação do novo valor
                final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
                _edtDialogNewValue.setText("");
                // endregion

                // region Criando a janela de dialogo
                AlertDialog.Builder _builder = new AlertDialog.Builder(ClienteProdutoMixListaActivity.this);
                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        _tpSelected.EstoqueQuantidade = MSVUtil.parseInt(_edtDialogNewValue.getText().toString().trim());
                        _tpSelected.EstoqueDataHora = MSVUtil.hojeToLong();

                        afterEstoqueQuantidade();

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


        // region Click em quantidade a comprar
        _e_pnlComprarQuantidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_inteiro, null);

                // informando o título da janela
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
                _txtDialogTitle.setText("QUANTIDADE");

                // informando a mensagem referente ao campo
                final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
                _txtDialogMessage.setText("Informe a quantidade do item que o cliente deseja comprar");

                // preenchendo o valor corrente
                final TextView _txtDialogCurrentValue = (TextView) _v.findViewById(R.id.txtDialogCurrentValue);
                _txtDialogCurrentValue.setText(MSVUtil.doubleToText(_tpSelected.ComprarQuantidade));

                // cuidando o campo que permitie a digitação do novo valor
                final EditText _edtDialogNewValue = (EditText) _v.findViewById(R.id.edtDialogNewValue);
                _edtDialogNewValue.setText("");
                // endregion

                // region Criando a janela de dialogo
                AlertDialog.Builder _builder = new AlertDialog.Builder(ClienteProdutoMixListaActivity.this);
                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        _tpSelected.ComprarQuantidade = MSVUtil.parseInt(_edtDialogNewValue.getText().toString().trim());
                        _tpSelected.EstoqueDataHora = MSVUtil.hojeToLong();

                        afterComprarQuantidade();

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


        // region Click no botão cancelar
        _e_btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _inEditar.setVisibility(View.GONE);
                _inLista.setVisibility(View.VISIBLE);

            }
        });
        // endregion


        // region Click no botão ok
        _e_btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (save()) {

                    ((ClienteMixAdapter) _l_livProduto.getAdapter()).notifyDataSetChanged();


                    refreshResumo();


                    _inEditar.setVisibility(View.GONE);
                    _inLista.setVisibility(View.VISIBLE);


                    Toast.makeText(
                            ClienteProdutoMixListaActivity.this,
                            "Estoque atualizado com sucesso !!!",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    Toast.makeText(
                            ClienteProdutoMixListaActivity.this,
                            "Não foi possível gravar as informações digitadas",
                            Toast.LENGTH_SHORT
                    ).show();

                }

            }
        });
        // endregion


    }
    // endregion


    // region initialize
    @Override
    public void initialize() {

        this.loadCliente();
        this.showCliente();
        this.showProdutoMix();
        this.refreshResumo();

    }
    // endregion


    // region loadCliente
    private void loadCliente() {

        SQLiteHelper _sqh = new SQLiteHelper(ClienteProdutoMixListaActivity.this);

        try {

            _sqh.open(false);

            dbCliente _dbCliente = new dbCliente(_sqh);
            _tpCliente = (tpCliente) _dbCliente.getBySourceId(_idCliente);
            _lstProdutoMix = (ArrayList<tpClienteProdutoMixRow>) _dbCliente.getProdutoMix(_idCliente);

            _sqh.close();

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

        // region Limpando os dados contidos nas views
        _txtClienteNomeFantasia.setText("");
        _txtClienteRazaoSocial.setText("");
        _txtClienteDocumento.setText("");
        // endregion

        // region Imprimindo nas vies os valores referente ao cliente
        if (_tpCliente != null) {

            _txtClienteNomeFantasia.setText(_tpCliente.NomeFantasia);
            _txtClienteRazaoSocial.setText(_tpCliente.RazaoSocial);

            if (_tpCliente.CnpjCpf.length() == 14) {
                _txtClienteDocumento.setText(MSVUtil.formatCnpj(_tpCliente.CnpjCpf));
            } else {
                _txtClienteDocumento.setText(MSVUtil.formatCpf(_tpCliente.CnpjCpf));
            }

        }
        // endregion

    }
    // endregion


    // region showProdutoMix
    private void showProdutoMix() {

        _l_txtRodape.setText("REGISTROS: 0");

        if (_lstProdutoMix != null) {

            /*
            ClienteMixAdapter _adapter = new ClienteMixAdapter(ClienteProdutoMixListaActivity.this, _lstProdutoMix);
            _l_livProduto.setAdapter(_adapter);

            _l_txtRodape.setText("REGISTROS: " + String.valueOf(_lstProdutoMix.size()));
            */

        } else {

            Toast.makeText(
                    ClienteProdutoMixListaActivity.this,
                    "Não existe mix de produto definido para este cliente",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }
    // endregion


    // region refreshResumo
    private void refreshResumo() {

        int _quantidade = 0;
        double _total = 0;


        if (_l_livProduto.getAdapter() != null) {

            ClienteMixAdapter _adapter = (ClienteMixAdapter)_l_livProduto.getAdapter();

            for (int i = 0; i < _adapter.getCount(); i++) {

                tpClienteProdutoMixRow _tp = (tpClienteProdutoMixRow) _adapter.getItem(i);

                if (_tp.EstoqueDataHora > 0 ) {
                    _quantidade += 1;
                    _total += (_tp.ComprarQuantidade * _tp.Preco);
                }

            }

        }


        _l_pnlResumo.setVisibility(View.GONE);

        if (_quantidade > 0) {
            _l_txtResumoQuantidade.setText(String.valueOf(_quantidade));
            _l_txtResumoTotal.setText(MSVUtil.doubleToText("R$", _total));
            _l_pnlResumo.setVisibility(View.VISIBLE);
        }

    }
    // endregion


    // region afterEstoqueQuantidade
    private void afterEstoqueQuantidade() {

        if (_tpSelected.CompraQuantidadeMaior > _tpSelected.EstoqueQuantidade) {
            _tpSelected.ComprarQuantidade = _tpSelected.CompraQuantidadeMaior - _tpSelected.EstoqueQuantidade;
        }

        _e_txtEstoqueQuantidade.setText(String.valueOf(_tpSelected.EstoqueQuantidade));
        _e_txtComprarQuantidade.setText(String.valueOf(_tpSelected.ComprarQuantidade));
        _e_txtTotalValor.setText(MSVUtil.doubleToText("R$", _tpSelected.ComprarQuantidade * _tpSelected.UltimaCompraValor));

    }
    // endregion


    // region afterComprarQuantidade
    private void afterComprarQuantidade() {

        _e_txtComprarQuantidade.setText(String.valueOf(_tpSelected.ComprarQuantidade));
        _e_txtTotalValor.setText(MSVUtil.doubleToText("R$", _tpSelected.ComprarQuantidade * _tpSelected.UltimaCompraValor));

    }
    // endregion


    // region save
    private boolean save() {

        boolean _out = false;
        SQLiteHelper _sqh = null;


        try {

            _sqh = new SQLiteHelper(ClienteProdutoMixListaActivity.this);
            _sqh.open(true);

            /*
            dbClienteMix _db = new dbClienteMix(_sqh);
            _out = _db.setQuantityStock(_tpSelected);
            */

        } catch (Exception e) {

            Toast.makeText(
                    ClienteProdutoMixListaActivity.this,
                    "Erro ao salvar as informações de estoque",
                    Toast.LENGTH_SHORT
            ).show();

        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }


        return _out;
    }
    // endregion


    // region clearStockInformation
    private boolean clearStockInformation() {

        boolean _out = false;
        SQLiteHelper _sqh = null;


        try {

            _sqh = new SQLiteHelper(ClienteProdutoMixListaActivity.this);
            _sqh.open(true);

            dbClienteMix _db = new dbClienteMix(_sqh);
            _out = _db.clearStockInformation(_idCliente);

        } catch (Exception e) {

            Toast.makeText(
                    ClienteProdutoMixListaActivity.this,
                    "Erro ao tentar limpar as informações anotadas do estoque do cliente",
                    Toast.LENGTH_SHORT
            ).show();

        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }


        return _out;
    }
    // endregion


    // region limparDigitacao
    private void limparDigitacao() {

        // region inflando o layout
        LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_alert, null);

        // informando o título da janela
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText("ATENÇÃO");

        // informando a mensagem referente ao campo
        final TextView _txtDialogMessage = (TextView) _v.findViewById(R.id.txtDialogMessage);
        _txtDialogMessage.setText("Limpar a digitação de estoque ?");

        // informando o complemento da mensagem
        final TextView _txtDialogComplement = (TextView) _v.findViewById(R.id.txtDialogComplement);
        _txtDialogComplement.setText("Ao clicar em OK o aplicativo irá limpar toda a informação de estoque do cliente");

        // region Criando a janela de dialogo
        AlertDialog.Builder _builder = new AlertDialog.Builder(ClienteProdutoMixListaActivity.this);
        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                clearStockInformation();
                initialize();

                Toast.makeText(
                        ClienteProdutoMixListaActivity.this,
                        "Informação de estoque removida com sucesso !!!",
                        Toast.LENGTH_SHORT
                ).show();

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
    // endregion

}
