package br.com.microserv.msvmobilepdv.extra;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvapi.WsIsOk;
import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;
import br.com.microserv.msvmobilepdv.R;

public class WsEnderecoActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando objetos da interface

    // TextView
    TextView _txtWsOld = null;
    TextView _txtWsNew = null;
    TextView _txtWsIsOk = null;

    // ImageView
    ImageView _imgWsIsOk = null;

    // endregion


    // region Declarando variáveis locais da activity

    // Object
    tpParametro _tpIpOld = null;
    tpParametro _tpIpNew = null;

    // boolean
    boolean _isWsOk = false;

    // endregion


    // region onCredate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando a layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ws_endereco);
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


    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_ws_trocar, menu);
        return true;

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

            case R.id.mnCancelar:
                this.cancelar();
                break;

            case R.id.mnSalvar:
                this.salvar();
                break;

        }
        // endregion


        // region Invocando o método construtor
        return super.onOptionsItemSelected(item);
        // endregion

    }
    // endregion


    // region onBackPressed
    @Override
    public void onBackPressed() {

        MSVMsgBox.showMsgBoxQuestion(
                WsEnderecoActivity.this,
                "Deseja realmente encerrar esta tela ?",
                "Se clicar em OK os dados incluidos ou alterados nesta tela serão perdidos",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {
                            WsEnderecoActivity.super.onBackPressed();
                        }

                    }
                }
        );

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // TextView
        _txtWsOld = (TextView) findViewById(R.id.txtWsOld);
        _txtWsNew = (TextView) findViewById(R.id.txtWsNew);
        _txtWsIsOk = (TextView) findViewById(R.id.txtWsIsOk);

        // ImageView
        _imgWsIsOk = (ImageView) findViewById(R.id.imgWsIsOk);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Click em txtWsNew
        _txtWsNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _imgWsIsOk.setVisibility(View.GONE);
                _txtWsIsOk.setVisibility(View.GONE);

                MSVMsgBox.getStringValue(
                        WsEnderecoActivity.this,
                        "Alterar endereço do servidor de sincronização",
                        "Informe o novo endereço IP para o servidor responsável por sincronizar os dados do dispositivo",
                        _tpIpOld.ValorTexto,
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {

                                    // region Se não foi informado nada então abandonamos o código abaixo
                                    if (MSVUtil.isNullOrEmpty(value)) {
                                        return;
                                    }
                                    // endregion

                                    // region Se o ip digitado for válido então...
                                    if (MSVUtil.isIpValid(value)) {

                                        _tpIpNew.ValorTexto = value;

                                        new WsIsOk(
                                                WsEnderecoActivity.this,
                                                _tpIpNew.ValorTexto,
                                                onTaskCompleteListner
                                        ).execute();

                                    } else {

                                        _tpIpNew.ValorTexto = "";

                                        MSVMsgBox.showMsgBoxWarning(
                                                WsEnderecoActivity.this,
                                                "Endereço IP inválido",
                                                "O número informado para o endereço ip está em um formato inválido"
                                        );

                                    }
                                    // endregion

                                    // region Atualizando as informações da activity
                                    refreshActivity();
                                    // endregion

                                }

                            }
                        }
                );


            }
        });
        // endregion

    }
    // endregion


    // region initialize
    @Override
    public void initialize() {

        // region Escondento alguns elementos da tela
        _txtWsIsOk.setVisibility(View.GONE);
        _imgWsIsOk.setVisibility(View.GONE);
        // endregion


        // region Lendo os parametros
        this.loadParametro();
        // endregion


        // region Se o parametro foi lido corretamente...
        if (_tpIpOld != null) {

            try {

                _tpIpNew = (tpParametro) _tpIpOld.clone();
                _tpIpNew.ValorTexto = "";

            } catch (CloneNotSupportedException ex1) {
                ex1.printStackTrace();
            }

            this.refreshActivity();

        }
        // endregion

    }
    // endregion


    // region loadParametro
    private void loadParametro() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(WsEnderecoActivity.this);
            _sqh.open(false);

            dbParametro _dbParametro = new dbParametro(_sqh);

            _tpIpOld = _dbParametro.getServidorRestIp();

            _sqh.close();

        } catch (Exception e) {
            Log.e("WsEnderecoActivity", "loadParametro -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region refreshActivity
    private void refreshActivity(){

        _txtWsOld.setText(_tpIpOld.ValorTexto);
        _txtWsNew.setText(_tpIpNew.ValorTexto);

    }
    // endregion


    // region cancelar
    private void cancelar() {

        MSVMsgBox.showMsgBoxQuestion(
                WsEnderecoActivity.this,
                "Deseja realmente cancelar a operação em andamento ?",
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


    // region salvar
    private void salvar() {

        // region Validando o endereço IP informado
        if (MSVUtil.isNullOrEmpty(_tpIpNew.ValorTexto)) {

            MSVMsgBox.showMsgBoxInfo(
                    WsEnderecoActivity.this,
                    "Valor requerido",
                    "O campo novo endereço deve ser preenchido antes de clicar em salvar"
            );

            return;

        }
        // endregion


        // region Validando se o novo IP é diferente do IP antigo
        String _ipOld = _tpIpOld.ValorTexto;
        String _ipNew = _tpIpNew.ValorTexto;

        if (_ipOld.equals(_ipNew)) {

            MSVMsgBox.showMsgBoxInfo(
                    WsEnderecoActivity.this,
                    "Endereço inválido",
                    "O novo endereço não pode ser igual ao endereço anterior"
            );

            return;

        }
        // endregion


        // region Validando se o WS foi encontrado
        if (_isWsOk == false) {

            MSVMsgBox.showMsgBoxError(
                    WsEnderecoActivity.this,
                    "Endereço inválido",
                    "O novo endereço informado não pode ser localizado"
            );

            return;

        }
        // endregion


        // region Verificando se o usuário quer realmente efetivar a operação em andamento
        MSVMsgBox.showMsgBoxQuestion(
                WsEnderecoActivity.this,
                "Deseja realmente salvar os dados da operação em andamento ?",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {

                            SQLiteHelper _sqh = null;
                            dbParametro _dbParametro = null;

                            try {

                                // region Iniciando o helper do banco de dados
                                _sqh = new SQLiteHelper(WsEnderecoActivity.this);
                                _sqh.open(true);
                                // endregion

                                // region Iniciando a transação com o banco de dados
                                _dbParametro = new dbParametro(_sqh);
                                _dbParametro.update(_tpIpNew);
                                // endregion

                                // region Avisando o usuário
                                Toast.makeText(
                                        WsEnderecoActivity.this,
                                        "Endereço atualizado com sucesso",
                                        Toast.LENGTH_SHORT
                                ).show();
                                // endregion

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if ((_sqh != null) && (_sqh.isOpen())) {
                                    _sqh.close();
                                }
                            }


                            // region fechando a activity
                            setResult(Activity.RESULT_OK);
                            finish();
                            // endregion

                        }

                    }
                }
        );
        // endregion

    }
    // endregion


    // region onTaskCompleteListner
    private OnTaskCompleteListner onTaskCompleteListner = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

            // region Endereco não foi localizado
            if (out == 0) {

                _isWsOk = false;

                _imgWsIsOk.setImageResource(R.drawable.msv_cancel_64_gray);
                _txtWsIsOk.setText("SINCRONIZADOR NÃO LOCALIZADO NESTE ENDEREÇO");

            }
            // endregion

            // region Encontrou o vendedor
            if (out == 1) {

                _isWsOk = true;

                _imgWsIsOk.setImageResource(R.drawable.msv_ok_64_gray);
                _txtWsIsOk.setText("SINCRONIZADOR LOCALIZADO COM SUCESSO !!!");

            }
            // endregion

            // region Apresentando os elementos na tela
            _imgWsIsOk.setVisibility(View.VISIBLE);
            _txtWsIsOk.setVisibility(View.VISIBLE);
            // endregion
        }
    };
    // endregion

}
