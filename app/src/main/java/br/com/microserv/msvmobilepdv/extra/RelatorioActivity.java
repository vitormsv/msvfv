package br.com.microserv.msvmobilepdv.extra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.msvmobilepdv.R;

public class RelatorioActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando variaveis para o bind dos elementos

    // TextView
    private TextView _txtVendedor = null;
    private TextView _txtVendedorRegiao = null;

    // LinearLayout
    private LinearLayout _pnlFlex = null;
    //private LinearLayout _pnlSincronizacao = null;
    //private LinearLayout _pnlConfiguacao = null;
    //private LinearLayout _pnlBackup = null;
    //private LinearLayout _pnlResetApp = null;
    private LinearLayout _pnlVoltar = null;

    // endregion


    // region Declarando variaveis locais

    // objetos
    tpEmpresa _tpEmpresa = null;
    tpVendedor _tpVendedor = null;

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
    static final String _MAIS_OPCOES_VALUE = "RelatorioActivity";
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);

        this.bindElements();
        this.bindEvents();
        
        this.initialize();
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


    // region bindElements
    @Override
    public void bindElements() {

        // TextView
        _txtVendedor = (TextView) findViewById(R.id.txtVendedor);
        _txtVendedorRegiao = (TextView) findViewById(R.id.txtVendedorRegiao);

        // LinearLayout
        _pnlFlex = (LinearLayout) findViewById(R.id.pnlFlex);
        //_pnlSincronizacao = (LinearLayout) findViewById(R.id.pnlSincronizacao);
        //_pnlConfiguacao = (LinearLayout) findViewById(R.id.pnlConfiguracao);
        //_pnlBackup = (LinearLayout) findViewById(R.id.pnlBackup);
        //_pnlResetApp = (LinearLayout) findViewById(R.id.pnlResetApp);
        _pnlVoltar = (LinearLayout) findViewById(R.id.pnlVoltar);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Click em _pnlRelatorio
        // endregion

        // region Click em _pnlSincronizacao
        /*
        _pnlSincronizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(RelatorioActivity.this, SincronizacaoListaActivity.class);
                startActivity(_intent);
            }
        });
        */
        // endregion

        // region Click em _pnlConfiguracao
        /*
        _pnlConfiguacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent _intent = new Intent(RelatorioActivity.this, WsEnderecoActivity.class);
                startActivity(_intent);

            }
        });
        */
        // endregion

        // region Click em _pnlBackup
        /*
        _pnlBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.showMsgBoxQuestion(
                        RelatorioActivity.this,
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

                                            Toast.makeText(RelatorioActivity.this, "Backup realizado com sucesso!",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (Exception e) {

                                        Toast.makeText(RelatorioActivity.this, "Backup Failed!", Toast.LENGTH_SHORT)
                                                .show();

                                    }



                                }
                            }

                        });

            }

        });
        */
        // endregion

        // region Click em _pnlResetApp
        /*
        _pnlResetApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.showMsgBoxQuestion(
                        RelatorioActivity.this,
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
        });
        */
        // endregion

        // region Click em _pnlFlex
        _pnlFlex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(RelatorioActivity.this, RobinHoodListaActivity.class);
                startActivity(_intent);
            }
        });
        // endregion

        // region Click em _pnlVoltar
        _pnlVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // endregion

    }
    // endregion


    // region initialize
    public void initialize() {

        // region Carregando informações necessárias do banco
        this.loadVendedor();
        // endregion

        // mostrando os dados do vendedor
        this.showVendedor();

    }
    // endregion


    // region loadVendedor
    private void loadVendedor() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(RelatorioActivity.this);
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
                RelatorioActivity.this,
                "Senha de segurança",
                "Informa a senha de segurança para este procedimento de atualização de banco de dados",
                "",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk && value.equals("msvfv")) {

                            SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
                            _sqh.create();

                            // fechando todas as activitys abertas
                            finishAffinity();

                        } else {

                            MSVMsgBox.showMsgBoxError(
                                    RelatorioActivity.this,
                                    "Senha inválida",
                                    "A senha informada para este procedimento de atualização de banco de dados é inválida"
                            );

                        }

                    }
                }
        );

    }
    // endregion

}
