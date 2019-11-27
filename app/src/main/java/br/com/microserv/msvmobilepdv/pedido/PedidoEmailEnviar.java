package br.com.microserv.msvmobilepdv.pedido;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbCondicaoPagamento;
import br.com.microserv.framework.msvdal.dbEmpresa;
import br.com.microserv.framework.msvdal.dbPedidoMobile;
import br.com.microserv.framework.msvdal.dbPedidoMobileItem;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdal.dbTipoPedido;
import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.msvmobilepdv.R;

public class PedidoEmailEnviar extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_ID_PEDIDO_MOBILE = "IdPedidoMobile";

    // endregion


    // region Declarando as variáveis ligadas a elementos visuais da tela

    // region CABECALHO

    // Include
    View _inClienteCabecalho = null;
    View _inSemConexao = null;

    // LinearLyout
    LinearLayout _pnlPedidoCnt = null;

    // TextView
    TextView _txtClienteNomeFantasia = null;
    TextView _txtClienteRazaoSocial = null;
    TextView _txtClienteDocumento = null;
    TextView _txtClienteCodigo = null;

    // Button
    Button _btnEnviar = null;

    // Switch
    Switch _swiEmailAtualizar = null;

    // endregion

    // region DADOS DO PEDIDO

    // LinearLayout
    LinearLayout _pnlEmail = null;

    // TextView
    TextView _txtNumero = null;
    TextView _txtDataHora = null;
    TextView _txtEmpresa = null;
    TextView _txtTipoPedido = null;
    TextView _txtItens = null;
    TextView _txtValorTotal = null;
    TextView _txtEmail = null;

    // endregion

    // endregion


    // region Declarando variáveis locais

    // Object
    tpPedidoMobile _tpPedidoMobile = null;
    tpVendedor _tpVendedor = null;

    // long
    long _IdPedidoMobile = 0;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_email_enviar);
        // endregion

        // region Adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);
        // endregion

        // region Selecionando os parametros enviados através do Bundle
        Bundle _extras = getIntent().getExtras();

        if (_extras != null) {

            // region _KEY_ID_PEDIDO_MOBILE
            if (_extras.containsKey(_KEY_ID_PEDIDO_MOBILE)) {
                _IdPedidoMobile = _extras.getLong(_KEY_ID_PEDIDO_MOBILE);
            } else {
                Toast.makeText(
                        PedidoEmailEnviar.this,
                        "O parâmetro _KEY_ID_PEDIDO_MOBILE não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        }
        // endregion

        // region Invocando os metodos que tratam dos elementos da tela
        bindElements();
        bindEvents();

        initialize();
        // endregion

    }
    // endregion


    // region onStart
    @Override
    public void onStart() {

        super.onStart();
        this.initialize();

    }
    // endregion


    // region onOptionItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // region Executando a ação de acordo com o menu selecionado
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }
        // endregion


        // region Invocando o método construtor
        return super.onOptionsItemSelected(item);
        // endregion

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // region Elementos do CABECALHO
        _inClienteCabecalho = findViewById(R.id.inClienteCabecalho);

        _txtClienteNomeFantasia = (TextView) _inClienteCabecalho.findViewById(R.id.txtClienteNomeFantasia);
        _txtClienteRazaoSocial = (TextView) _inClienteCabecalho.findViewById(R.id.txtClienteNomeFantasia);
        _txtClienteDocumento = (TextView) _inClienteCabecalho.findViewById(R.id.txtClienteNomeFantasia);
        _txtClienteCodigo = (TextView) _inClienteCabecalho.findViewById(R.id.txtClienteNomeFantasia);
        // endregion


        // region Elementos do INCLUDE SEM CONEXAO
        _inSemConexao = findViewById(R.id.inSemConexao);
        // endregion


        // region Elementos do PEDIDO

        // LinearLayout
        _pnlPedidoCnt = (LinearLayout) findViewById(R.id.pnlPedidoCnt);
        _pnlEmail = (LinearLayout) findViewById(R.id.pnlEmail);

        // TextView
        _txtNumero = (TextView) findViewById(R.id.txtNumero);
        _txtDataHora = (TextView) findViewById(R.id.txtDataHora);
        _txtEmpresa = (TextView) findViewById(R.id.txtEmpresa);
        _txtTipoPedido = (TextView) findViewById(R.id.txtTipoPedido);
        _txtItens = (TextView) findViewById(R.id.txtItens);
        _txtValorTotal = (TextView) findViewById(R.id.txtValorTotal);
        _txtEmail = (TextView) findViewById(R.id.txtEmail);

        // Button
        _btnEnviar = (Button) findViewById(R.id.btnEnviar);

        // Switch
        _swiEmailAtualizar = (Switch) findViewById(R.id.swiEmailAtualizar);

        // endregion

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Click em EMAIL
        _pnlEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MSVMsgBox.getEmailValue(
                        PedidoEmailEnviar.this,
                        "E-MAIL",
                        "Informe um ou mais email´s (separados por ponto e virgula) para onde o pedido deverá ser enviado",
                        _txtEmail.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _txtEmail.setText(value.trim().toLowerCase());
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Click em ENVIAR
        _btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // region Atualizando o cadastro do cliente com o e-mail informado

                /*
                *
                * Aqui vamos colocar o trecho de código para atualizar o e-mail do cliente
                * conforme solicitado em tela, e depois precisamos ver se ao sincronizar este
                * pedido o ERP irá atualizar as informações do cliente lá do outro lado
                *
                * */

                SQLiteHelper _sqh = null;
                dbCliente _dbCliente = null;
                tpCliente _tpCliente = null;

                String _acao = null;

                try {

                    // region Atualizando o email do cliente caso seja necessário

                    if (_swiEmailAtualizar.isChecked()) {

                        _acao = "Atualizando o e-mail do cliente";

                        _sqh = new SQLiteHelper(PedidoEmailEnviar.this);
                        _sqh.open(true);

                        _dbCliente = new dbCliente(_sqh);
                        _tpCliente = (tpCliente) _dbCliente.getBySourceId(_tpPedidoMobile.IdCliente);

                        if (_tpCliente != null) {
                            _tpCliente.Email = _txtEmail.getText().toString();
                            _dbCliente.update(_tpCliente);
                        }

                    }
                    // endregion


                    // region Montando o texto do e-mail
                    _acao = "Montando o texto para envio do e-mail";

                    StringBuilder _texto = new StringBuilder();
                    _texto.append("Sr. Cliente segue em anexo neste e-mail a cópia do seu pedido.");
                    _texto.append("\n");
                    _texto.append("\n");
                    _texto.append("Att.");
                    _texto.append(_tpVendedor.Nome);
                    // endregion


                    // region Invocanco o método para enviar o e-mail
                    _acao = "Compartilhando a cópia do pedido com outros aplicativos do dispositivo";

                    String _p = _texto.toString();
                    String _to = _txtEmail.getText().toString();
                    String _subject = "DADOS DO PEDIDO " + _tpPedidoMobile.Numero;

                    sendMail(_to, _subject, _p);
                    // endregion

                } catch (Exception e) {

                    MSVMsgBox.showMsgBoxError(
                            PedidoEmailEnviar.this,
                            "Erro ao realizar o compartilhamento da cópia do pedido \n Ação: " + _acao,
                            e.getMessage()
                    );

                } finally {

                    if (_sqh != null && _sqh.isOpen()) {
                        _sqh.close();
                        _sqh = null;
                    }

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

        // region Declarando variáveis locais do método
        boolean _isConnected = false;
        // endregion

        // region Carregando informações do banco de dados
        this.loadVendedor();
        this.loadPedido();
        // endregion

        // region Atualizando elementos da tela
        this.showCliente();
        this.showPedido();
        // endregion

        // region Escondendo elementos na tela
        _inSemConexao.setVisibility(View.GONE);
        _pnlPedidoCnt.setVisibility(View.GONE);
        // endregion

        // region Verificando se existe conexão
        _isConnected = MSVUtil.isConnected(PedidoEmailEnviar.this);
        // endregion

        // region Inicializando a tela de acordo com a conexão
        if (_isConnected) {
            _pnlPedidoCnt.setVisibility(View.VISIBLE);
        } else {
            _inSemConexao.setVisibility(View.VISIBLE);
        }
        // endregion

    }
    // endregion


    // region loadVendedor
    private void loadVendedor() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(PedidoEmailEnviar.this);
            _sqh.open(false);

            // buscando os dados do vendedor
            dbVendedor _dbVendedor = new dbVendedor(_sqh);
            _tpVendedor = (tpVendedor) _dbVendedor.getById(1);


        } catch (Exception e) {
            Log.e("PedidoEmailEnviar", "initialize -> " + e.getMessage());

            MSVMsgBox.showMsgBoxError(
                    PedidoEmailEnviar.this,
                    "Erro ao ler os dados do vendedor",
                    e.getMessage()
            );

        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region loadPedido
    private void loadPedido() {

        SQLiteHelper _sqh = null;

        try {

            SQLClauseHelper _schPedido = new SQLClauseHelper();
            _schPedido.addEqualInteger("IdPedidoMobile", _IdPedidoMobile);

            _sqh = new SQLiteHelper(PedidoEmailEnviar.this);
            _sqh.open(false);

            dbPedidoMobile _dbPedidoMobile = new dbPedidoMobile(_sqh);
            _tpPedidoMobile = (tpPedidoMobile) _dbPedidoMobile.getBySourceId(_IdPedidoMobile);

            if (_tpPedidoMobile != null) {

                dbEmpresa _dbEmpresa = new dbEmpresa(_sqh);
                _tpPedidoMobile.Empresa = (tpEmpresa) _dbEmpresa.getBySourceId(_tpPedidoMobile.IdEmpresa);

                dbTipoPedido _dbTipoPedido = new dbTipoPedido(_sqh);
                _tpPedidoMobile.TipoPedido = (tpTipoPedido) _dbTipoPedido.getBySourceId(_tpPedidoMobile.IdTipoPedido);

                dbCliente _dbCliente = new dbCliente(_sqh);
                _tpPedidoMobile.Cliente = (tpCliente) _dbCliente.getBySourceId(_tpPedidoMobile.IdCliente);

                dbCondicaoPagamento _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);
                _tpPedidoMobile.CondicaoPagamento = (tpCondicaoPagamento) _dbCondicaoPagamento.getBySourceId(_tpPedidoMobile.IdCondicaoPagamento);

                dbPedidoMobileItem _dbPedidoMobileItem = new dbPedidoMobileItem(_sqh);
                _tpPedidoMobile.Itens = (ArrayList<tpPedidoMobileItem>) _dbPedidoMobileItem.getList(tpPedidoMobileItem.class, _schPedido);

                if (_tpPedidoMobile.Itens != null) {

                    dbProduto _dbProduto = new dbProduto(_sqh);

                    for (tpPedidoMobileItem _item : _tpPedidoMobile.Itens) {
                        _item.Produto = (tpProduto) _dbProduto.getBySourceId(_item.IdProduto);
                    }

                }

            }

        } catch (Exception e) {

            Toast.makeText(
                    PedidoEmailEnviar.this,
                    "Erro ao carregar os dados do pedido: " + e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }

    }
    // endregion


    // region showCliente
    private void showCliente() {

        if (_tpPedidoMobile.Cliente != null) {

            _txtClienteNomeFantasia.setText(_tpPedidoMobile.Cliente.NomeFantasia);
            _txtClienteRazaoSocial.setText(_tpPedidoMobile.Cliente.RazaoSocial);
            _txtClienteCodigo.setText(_tpPedidoMobile.Cliente.Codigo);

            if (_tpPedidoMobile.Cliente.CnpjCpf.length() == 11) {
                _txtClienteDocumento.setText(MSVUtil.formatCpf(_tpPedidoMobile.Cliente.CnpjCpf));
            } else {
                _txtClienteDocumento.setText(MSVUtil.formatCnpj(_tpPedidoMobile.Cliente.CnpjCpf));
            }
        }

    }
    // endregion


    // region showPedido
    private void showPedido() {

        _txtNumero.setText(_tpPedidoMobile.Numero);
        _txtDataHora.setText(_tpPedidoMobile.EmissaoDataHora);
        _txtEmpresa.setText(_tpPedidoMobile.Empresa.Descricao);
        _txtTipoPedido.setText(_tpPedidoMobile.TipoPedido.Descricao);
        _txtItens.setText(String.valueOf(_tpPedidoMobile.Itens.size()));
        _txtValorTotal.setText(MSVUtil.doubleToText("R$", _tpPedidoMobile.TotalValorLiquido));
        _txtEmail.setText(_tpPedidoMobile.Cliente.Email);
    }
    // endregion


    // region sendMail
    private void sendMail(String to, String subject, String content) {

        if (MSVUtil.isPackageManeger(PedidoEmailEnviar.this, MSVUtil.GMAIL_INTENT)) {

            if (MSVUtil.isConnected(PedidoEmailEnviar.this)) {

                File _arqHtml = this.getHtmlAttachment();

                Intent _i = new Intent(Intent.ACTION_SEND);
                _i.setType("message/txt");
                _i.setPackage(MSVUtil.GMAIL_INTENT);
                _i.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                _i.putExtra(Intent.EXTRA_SUBJECT, subject);
                _i.putExtra(Intent.EXTRA_TEXT, content);

                if (_arqHtml != null) {
                    _i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(_arqHtml));
                }

                try {
                    startActivity(_i);
                } catch (Exception e) {
                    Toast.makeText(
                            PedidoEmailEnviar.this,
                            e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }

            } else {

                MSVMsgBox.showMsgBoxError(
                        PedidoEmailEnviar.this,
                        "WIFI | 3G",
                        "O seu dispositivo está off-line no momento, não permitindo que seja enviado e-mail"
                );

            }

        } else {

            MSVMsgBox.showMsgBoxError(
                    PedidoEmailEnviar.this,
                    "Aplicativo Gmail não instalado neste dispositivo",
                    "Acesso sua loja de aplicativos e baixe o aplicativo Gmail para compartilhar o pedido com o seu cliente"
            );

        }

    }
    // endregion


    // region getHtmlAttachment
    private File getHtmlAttachment() {

        // region Declarando variáveis do método
        SQLiteHelper _sqh = null;
        String _name = null;

        String _dir = null;
        File _out = null;
        // endregion

        try {

            // region Gravando o arquivo html do pedido no diretório do dispositivo

            // region 01 Realizando a conexão com o banco de dados
            _sqh = new SQLiteHelper(PedidoEmailEnviar.this);
            _sqh.open(false);
            // endregion

            // region 02 Lendo os dados do pedido e escrevendo o html na pasta
            dbPedidoMobile _db = new dbPedidoMobile(_sqh);
            _name = _db.saveToHtml(_tpPedidoMobile._id);
            // endregion

            // endregion

            // region Recuperando o arquivo gravado

            // region 01 Mapeando o diretório e nome do arquivo
            _dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + "/msvsmart/html";
            // endregion

            // region 02 Buscando o arquivo no diretório
            _out = new File(_dir, _name);

            if ((_out != null) && (_out.exists() == false)) {
                _out = null;
            }
            // endregion

            // endregion

        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    PedidoEmailEnviar.this,
                    "Ocorreu um erro ao tentar selecionar o arquivo de anexo do e-mail",
                    e.getMessage()
            );
        } finally {
            return _out;
        }

    }
    // endregion


    // region sendWhatsapp
    private void sendWhatsapp(String content) {

        String smsNumber = "5514997419005"; //without '+'

        try {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch(Exception e) {
            Toast.makeText(this, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    // endregion

}
