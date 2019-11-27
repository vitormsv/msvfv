package br.com.microserv.msvmobilepdv;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.UUID;

import br.com.microserv.framework.msvdal.dbSincronizacao;
import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpSincronizacao;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.cliente.ClienteListaActivity;
import br.com.microserv.msvmobilepdv.extra.ActivateDeviceActivity;
import br.com.microserv.msvmobilepdv.extra.RelatorioActivity;
import br.com.microserv.msvmobilepdv.extra.SincronizacaoDadosListaActivity;
import br.com.microserv.msvmobilepdv.extra.SincronizacaoListaActivity;
import br.com.microserv.msvmobilepdv.extra.VendedorRegistrarActivity;
import br.com.microserv.msvmobilepdv.extra.WsEnderecoActivity;
import br.com.microserv.msvmobilepdv.pedido.PedidoListaActivity;
import br.com.microserv.msvmobilepdv.produto.ProdutoListaActivity;
import br.com.microserv.msvmobilepdv.produto.ProdutoRecycleViewActivity;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando variaveis para o bind dos elementos

    // TextView
    private TextView _txtVendedor = null;
    private TextView _txtVendedorRegiao = null;
    private TextView _txtUltimaSincronizacao = null;

    // LinearLayout
    private LinearLayout _pnlRow1 = null;
    private LinearLayout _pnlRow2 = null;
    private LinearLayout _pnlCliente = null;
    private LinearLayout _pnlPedido = null;
    private LinearLayout _pnlProduto = null;
    private LinearLayout _pnlSincronizarDados = null;

    // endregion


    // region Declarando variaveis locais

    // objetos
    tpEmpresa _tpEmpresa = null;
    tpVendedor _tpVendedor = null;
    tpSincronizacao _tpSincronizacao = null;

    // boolean
    boolean _isSyncToday = false;

    // endregion

    
    // region Declarando constantes
    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_ITEM_INDEX = "ItemIndex";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final int _DETAIL_VALUE = 4;
    static final int _LIST_VALUE = 5;
    final int LOGIN_ACTIVITY_RESULT_CODE = 100;
    final int VENDEDOR_ACTIVITY_RESULT_CODE = 200;
    static final String _MAIN_VALUE = "MainActivity";
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);

        // Gerar chave única para o dispositivo
        // String _uuid = UUID.randomUUID().toString();

        this.bindElements();
        this.bindEvents();

        this.initialize();

    }
    // endregion


    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);


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

            case R.id.mnCliente:
                menuCliente();
                break;

            case R.id.mnPedido:
                menuPedido();
                break;

            case R.id.mnProduto:
                menuProduto();
                break;

            case R.id.mnSincronizarDados:
                menuSincronizarDados();
                break;

            case R.id.mnRelatorio:
                menuRelatorio();
                break;

            case R.id.mnConfiguracao:
                menuConfiguracao();
                break;

            case R.id.mnAtualizacao:
                menuAtualizacao();
                break;

            case R.id.mnCopiarDados:
                menuCopiarDados();
                break;

            case R.id.mnLimparTudo:
                menuLimparTudo();
                break;

            case R.id.mnSair:
                menuSair();
                break;

        }

        return super.onOptionsItemSelected(item);

    }
    // endregion


    // region onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VENDEDOR_ACTIVITY_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                // ativando os botões de menu
                _pnlRow1.setVisibility(View.VISIBLE);
                _pnlRow2.setVisibility(View.VISIBLE);

                // lendo novamente os dados do vendedor
                // para apresentar na tela
                this.loadVendedor();
                this.showVendedor();

                // invocando a activity de login do usuário
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(i, LOGIN_ACTIVITY_RESULT_CODE);

            } else {
                finish();
            }
        }

        if (requestCode == LOGIN_ACTIVITY_RESULT_CODE) {

            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }

            if (resultCode == Activity.RESULT_OK) {

                _tpEmpresa = (tpEmpresa) data.getSerializableExtra("tpEmpresa");

                if (!_isSyncToday) {
                    MSVMsgBox.showMsgBoxWarning(
                            MainActivity.this,
                            "Atenção, antes de realizar a venda é necessário sincronizar este dispositivo",
                            "Verifique se seu dispositivo está conectado na internet e então entre na opção de Sincronização no menu principal."
                    );
                }

            }
        }

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // TextView
        _txtVendedor = (TextView) findViewById(R.id.txtVendedor);
        _txtVendedorRegiao = (TextView) findViewById(R.id.txtVendedorRegiao);
        _txtUltimaSincronizacao = (TextView) findViewById(R.id.txtUltimaSincronizacao);

        // Button
        _pnlRow1 = (LinearLayout) findViewById(R.id.pnlRow1);
        _pnlRow2 = (LinearLayout) findViewById(R.id.pnlRow2);
        _pnlCliente = (LinearLayout) findViewById(R.id.pnlCliente);
        _pnlPedido = (LinearLayout) findViewById(R.id.pnlPedido);
        _pnlProduto = (LinearLayout) findViewById(R.id.pnlProduto);
        _pnlSincronizarDados = (LinearLayout) findViewById(R.id.pnlSincronizarDados);
    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Click em _pnlCliente
        _pnlCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCliente();
            }
        });
        // endregion

        // region Click em _pnlPedido
        _pnlPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPedido();
            }
        });
        // endregion

        // region Click em _pnlProduto
        _pnlProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            menuProduto();
            }
        });
        // endregion

        // region Click em _pnlSincronizarDados
        _pnlSincronizarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            menuSincronizarDados();
            }
        });
        // endregion

    }
    // endregion


    // region initialize
    public void initialize() {

        // region Deixando os botões invisíveis
        _pnlRow1.setVisibility(View.GONE);
        _pnlRow2.setVisibility(View.GONE);
        // endregion

        // region Carregando informações necessárias do banco
        this.loadVendedor();
        this.loadSincronizacao();
        // endregion

        this.checkPermissions();

        // region Verificando qual tela vamos apresentar ao usuário final
        if (_tpVendedor == null) {

            // invocando a activity de registro do vendedor
            Intent i = new Intent(MainActivity.this, VendedorRegistrarActivity.class);
            startActivityForResult(i, VENDEDOR_ACTIVITY_RESULT_CODE);

        } else {

            // mostrando todas as oções do menu principal
            _pnlRow1.setVisibility(View.VISIBLE);
            _pnlRow2.setVisibility(View.VISIBLE);

            // mostrando os dados do vendedor e da última sincronização
            this.showVendedor();
            this.showSincronizacao();

            // invocando a activity de login do usuário
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(i, LOGIN_ACTIVITY_RESULT_CODE);

        }
        // endregion

        // endregion

    }
    // endregion


    // region loadVendedor
    private void loadVendedor() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(MainActivity.this);
            _sqh.open(false);

            // buscando os dados do vendedor
            dbVendedor _dbVendedor = new dbVendedor(_sqh);
            _tpVendedor = (tpVendedor) _dbVendedor.getById(1);


        } catch (Exception e) {
            Log.e("MainActivity", "initialize -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region ShowVendedor
    private void showVendedor() {

        if (_tpVendedor != null) {
            _txtVendedor.setText(_tpVendedor.Codigo + " - " + _tpVendedor.Nome);
            _txtVendedorRegiao.setText("MISTA");
        }

    }
    // endregion


    // region refreshDatabase
    private void refreshDatabase() {

        MSVMsgBox.getPasswordValue(
                MainActivity.this,
                "Senha de segurança",
                "Informa a senha de segurança para este procedimento de atualização de banco de dados",
                "",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk && value.equals("msvfv")) {

                            SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
                            _sqh.create();

                            finish();

                        } else {

                            MSVMsgBox.showMsgBoxError(
                                    MainActivity.this,
                                    "Senha inválida",
                                    "A senha informada para este procedimento de atualização de banco de dados é inválida"
                            );

                        }

                    }
                }
        );

    }
    // endregion


    // region loadPermissions
    private void checkPermissions()
    {
        // checar se o app tem as permissões necessárias para executar
        // suas funcionalidades e pedir autorizacao ao usuário caso necessário

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{ Manifest.permission.READ_CONTACTS,
                              Manifest.permission.WRITE_EXTERNAL_STORAGE,
                              Manifest.permission.ACCESS_FINE_LOCATION,
                              Manifest.permission.CAMERA,
                              Manifest.permission.FLASHLIGHT},
                0);

    }
    // endregion


    // region loadSincronizacao
    private void loadSincronizacao() {

        SQLiteHelper _sqh = null;
        dbSincronizacao _dbSincronizacao = null;


        try {

            _sqh = new SQLiteHelper(MainActivity.this);
            _sqh.open(false);

            _dbSincronizacao = new dbSincronizacao(_sqh);
            _tpSincronizacao = _dbSincronizacao.getLast();
            _isSyncToday = _dbSincronizacao.isSyncToday();

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    MainActivity.this,
                    "Erro ao realizar a leitura da última sincronização"
            );

            finish();

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }

    }
    // endregion


    // region showSincronizacao
    private void showSincronizacao() {

        String _dmy;
        String _hms;

        _txtUltimaSincronizacao.setText("");


        try {

            if (_tpSincronizacao != null) {

                _dmy = MSVUtil.ymdhmsTOdmy(_tpSincronizacao.TerminoDataHora);
                _hms = MSVUtil.ymdhmsTOhms(_tpSincronizacao.TerminoDataHora);

                _txtUltimaSincronizacao.setText(
                        String.format("SINCRONIZADO EM: %s AS %s", _dmy, _hms)
                );
            }

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    MainActivity.this,
                    "[MainActivity|showSincronizacao] Erro ao apresentar a data e hora da última sincronização " + e.getMessage()
            );

            finish();

        }

    }
    // endregion


    // region Invocação dos Menus

    // region Menu Clientes
    private void menuCliente() {

        Bundle _extras = new Bundle();
        _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);

        Intent _intent = new Intent(MainActivity.this, ClienteListaActivity.class);
        _intent.putExtras(_extras);
        startActivity(_intent);

    }
    // endregion


    // region Menu Pedidos
    private void menuPedido() {

        Bundle _extras = new Bundle();
        _extras.putInt(_KEY_METODO_EDICAO, _DETAIL_VALUE);
        _extras.putString(_KEY_SOURCE_ACTIVITY, _MAIN_VALUE);
        _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);

        Intent _intent = new Intent(MainActivity.this, PedidoListaActivity.class);
        _intent.putExtras(_extras);
        startActivity(_intent);

    }
    // endregion


    // region Menu Produto
    private void menuProduto() {

        Bundle _extras = new Bundle();
        _extras.putInt(_KEY_METODO_EDICAO, _LIST_VALUE);
        _extras.putString(_KEY_SOURCE_ACTIVITY, _MAIN_VALUE);
        _extras.putSerializable(_KEY_TP_EMPRESA, _tpEmpresa);
        _extras.putInt(_KEY_ITEM_INDEX, 0);

        Intent _intent = new Intent(MainActivity.this, ProdutoListaActivity.class);
        _intent.putExtras(_extras);
        startActivity(_intent);

    }
    // endregion


    // region Menu Sincronizar Dados
    private void menuSincronizarDados() {

        Intent i = new Intent(MainActivity.this, SincronizacaoDadosListaActivity.class);
        startActivity(i);

    }
    // endregion


    // region Menu Relatorio
    private void menuRelatorio() {

        Intent _intent = new Intent(MainActivity.this, RelatorioActivity.class);
        startActivity(_intent);

    }
    // endregion


    // region Menu Configuracao
    private void menuConfiguracao() {

        Intent _intent = new Intent(MainActivity.this, WsEnderecoActivity.class);
        startActivity(_intent);

    }
    // endregion


    // region Menu Atualizacao
    private void menuAtualizacao() {

        Intent _intent = new Intent(MainActivity.this, SincronizacaoListaActivity.class);
        startActivity(_intent);

    }
    // endregion


    // region Menu CopiarDados
    private void menuCopiarDados() {

        MSVMsgBox.showMsgBoxQuestion(
                MainActivity.this,
                "Criar Backup",
                "Deseja realmente gerar um novo backup do sistema?",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {


                            try{

                                File _Destino = Environment.getExternalStorageDirectory();
                                File data = Environment.getDataDirectory();

                                if (_Destino.canWrite()) {

                                    String diretorioBancoSQLite = "//data//" + "br.com.microserv.msvmobilepdv"
                                            + "//databases//" + "microservpdv.db";

                                    String diretorioDestinoBackup = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/msvsmart/backup";

                                    File BancoSQLite = new File(data, diretorioBancoSQLite);
                                    File Backup = new File(diretorioDestinoBackup, "microservpdv.bd");
                                    File DiretorioBackup = new File(diretorioDestinoBackup);

                                    if (DiretorioBackup.exists() == false){
                                        DiretorioBackup.mkdirs();
                                    }

                                    FileChannel channelOrigem = new FileInputStream(BancoSQLite).getChannel();
                                    FileChannel channelDestino = new FileOutputStream(Backup).getChannel();

                                    channelDestino.transferFrom(channelOrigem, 0, channelOrigem.size());

                                    channelOrigem.close();
                                    channelDestino.close();

                                    Toast.makeText(MainActivity.this, "Backup realizado com sucesso!",
                                            Toast.LENGTH_SHORT).show();

                                }

                            } catch (Exception e) {

                                Toast.makeText(MainActivity.this, "Backup Failed!", Toast.LENGTH_SHORT)
                                        .show();

                            }



                        }
                    }

                });

    }
    // endregion


    // region Menu LimparTudo
    private void menuLimparTudo() {

        MSVMsgBox.showMsgBoxQuestion(
                MainActivity.this,
                "Atualizar DB",
                "Deseja realmente atualizar a estrutura do banco de dados do aplicativo MSVFV.\nEsta procedimento irá construir um banco novo e atualizado",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {
                            refreshDatabase();
                        }
                    }
                }
        );

    }
    // endregion


    // region Menu Sair
    private void menuSair() {

        MSVMsgBox.showMsgBoxQuestion(
                MainActivity.this,
                "Deseja realmente sair do aplicativo",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {
                            finish();
                        }

                    }
                }
        );

    }
    // endregion

    // endregion

}
