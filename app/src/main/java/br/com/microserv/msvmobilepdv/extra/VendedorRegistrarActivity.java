package br.com.microserv.msvmobilepdv.extra;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.microserv.framework.msvapi.VendedorApi;
import br.com.microserv.framework.msvdal.dbEmpresa;
import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdto.tpEmpresa;
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

public class VendedorRegistrarActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando os elementos da activity

    // LinearLayout
    LinearLayout _pnlCodigo = null;
    LinearLayout _pnlChave = null;
    LinearLayout _pnlIp = null;

    // TextView
    TextView _txtCodigo = null;
    TextView _txtChave = null;
    TextView _txtIp = null;

    // Button
    Button _btnVerificar = null;

    // endregion


    // region Declarando objetos da activity

    // Objetos
    tpParametro _tpCodigo = null;
    tpParametro _tpChave = null;
    tpParametro _tpIp = null;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor_registrar);

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

        // LinearLayout
        _pnlCodigo = (LinearLayout) findViewById(R.id.pnlCodigo);
        _pnlChave = (LinearLayout) findViewById(R.id.pnlChave);
        _pnlIp = (LinearLayout) findViewById(R.id.pnlIp);

        // TextView
        _txtCodigo = (TextView) findViewById(R.id.txtCodigo);
        _txtChave = (TextView) findViewById(R.id.txtChave);
        _txtIp = (TextView) findViewById(R.id.txtIp);

        // Button
        _btnVerificar = (Button) findViewById(R.id.btnVerificar);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Click em CODIGO
        _pnlCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MSVMsgBox.getStringNumberValue(
                        VendedorRegistrarActivity.this,
                        "CÓDIGO",
                        "Informe o código de cadastro do vendedor",
                        _txtCodigo.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _txtCodigo.setText(value.trim());
                                }

                            }
                        }
                );

            }
        });
        // endregion


        // region Click em CHAVE DE ACESSO
        _pnlChave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MSVMsgBox.getStringValue(
                        VendedorRegistrarActivity.this,
                        "CHAVE DE ACESSO",
                        "Informe os caracteres da chave de acesso que foi gerada no cadastro de usuário do vendedor \n [AAAA-AAAA-AAAA]",
                        _txtChave.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _txtChave.setText(value.trim());
                                }

                            }
                        }
                );

            }
        });
        // endregion


        // region Click em IP
        _pnlIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MSVMsgBox.getStringValue(
                        VendedorRegistrarActivity.this,
                        "IP / PORTA",
                        "Informe o endereço IP e PORTA (se existir) de comunicação com o sincronizador de informações",
                        _txtIp.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _txtIp.setText(value.trim());
                                }

                            }
                        }
                );

            }
        });
        // endregion


        // region Click em _btnVerificar
        _btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Verificando se o vendedor existe no ERP
                if (isValid()) {

                    try {

                        // region Selecionando as informaçoes da activity
                        String _codigo = _txtCodigo.getText().toString().trim();
                        String _hash = _txtChave.getText().toString().trim();
                        String _server = _txtIp.getText().toString().trim();
                        // endregion

                        // region Invocando o método WS para validar os dados do vendedor
                        VendedorApi _api = new VendedorApi(
                                VendedorRegistrarActivity.this,
                                _server,
                                _codigo,
                                _hash,
                                onTaskCompleteListner
                        );

                        _api.execute();
                        // endregion

                    } catch (Exception e) {
                        Log.e("VendedorRegistrarAct", "_btnVerificar.setOnClickListner -> " + e.getMessage());
                        e.printStackTrace();
                    }

                }
                // endregion

            }
        });
        // endregion

    }
    // endregion


    // region initialize
    public void initialize() {

        this.loadParametro();
        this.showData();

        if (_txtChave.getText().toString().isEmpty()) {
            _txtChave.setText("aaaa-bbbb-cccc");
        }

    }
    // endregion


    // region loadParametro
    private void loadParametro() {

        SQLiteHelper _sqh = null;

        try {

            _sqh = new SQLiteHelper(VendedorRegistrarActivity.this);
            _sqh.open(false);

            dbParametro _dbParametro = new dbParametro(_sqh);

            _tpCodigo = _dbParametro.getVendedorCodigo();
            _tpChave = _dbParametro.getVendedorHash();
            _tpIp = _dbParametro.getServidorRestIp();

            _sqh.close();

        } catch (Exception e) {
            Log.e("VendedorRegistrarAct", "loadParametro -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region showData
    private void showData() {

        // region Limpando os dados da activity
        _txtCodigo.setText("");
        _txtChave.setText("");
        _txtIp.setText("");
        // endregion

        // region Imprimindo nos elementos os valores dos parâmetros
        if (_tpCodigo != null) {
            _txtCodigo.setText(_tpCodigo.ValorTexto);
        }

        if (_tpChave != null) {
            _txtChave.setText(_tpChave.ValorTexto);
        }

        if (_tpIp != null) {
            _txtIp.setText(_tpIp.ValorTexto);
        }
        // endregion

    }
    // endregion


    // region isValid
    private boolean isValid() {

        boolean _out = true;


        // region Verificando o preenchimento do [código do vendedor]
        String _codigo = _txtCodigo.getText().toString().trim();

        if (MSVUtil.isNullOrEmpty(_codigo)) {

            MSVMsgBox.showMsgBoxError(
                    VendedorRegistrarActivity.this,
                    "CÓDIGO VENDEDOR",
                    "O preenchimento do campo [Código do Vendedor] é obrigatório"
            );

            return false;

        }
        // endregion


        // region Verificando o preenchimento da [chave de acesso]

        // region 01 - Selecionando o valor informado
        String _chave = _txtChave.getText().toString().trim();
        // endregion

        // region 02 - Verificando se o mesmo foi preenchido
        if (MSVUtil.isNullOrEmpty(_chave)) {

            MSVMsgBox.showMsgBoxError(
                    VendedorRegistrarActivity.this,
                    "CHAVE DE ACESSO",
                    "O preenchimento do campo [Chave de Acesso] é obrigatório"
            );

            return false;

        }
        // endregion

        // region 03 - Verificando se o mesmo possui 14 caracteres
        if (_chave.length() != 14) {

            MSVMsgBox.showMsgBoxError(
                    VendedorRegistrarActivity.this,
                    "CHAVE DE ACESSO",
                    "O campo [Chave de Acesso] deve conter exatamente 14 caracteres"
            );

            return false;

        }
        // endregion

        // region 04 - Aplicando expressão regular para verificar o formato
        Pattern _p = Pattern.compile("[a-zA-Z]{4,4}-[a-zA-Z]{4,4}-[a-zA-Z]{4,4}");
        Matcher _m = _p.matcher(_chave);

        if (!_m.find()) {

            MSVMsgBox.showMsgBoxError(
                    VendedorRegistrarActivity.this,
                    "CHAVE DE ACESSO",
                    "O campo [Chave de Acesso] deve ser preenchido no seguinte formato [AAAA-AAAA-AAAA]"
            );

            return false;

        }
        // endregion

        // endregion


        // region Verificando o preenchimento do [Ip/Porta]

        // region 01 - Selecionando o valor informado
        String _ip = _txtIp.getText().toString().trim();
        // endregion

        // region 02 - Verificando se o campo foi preenchido
        if (MSVUtil.isNullOrEmpty(_ip)) {

            MSVMsgBox.showMsgBoxError(
                    VendedorRegistrarActivity.this,
                    "IP e PORTA",
                    "O preenchimento do campo [Endereço IP do Servidor] é obrigatório"
            );

            return false;

        }
        // endregion

        // region 03 - Aplicando expressão regular para verificar o formato
//        Pattern _p1 = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
//        Matcher _m1 = _p1.matcher(_ip);
//
//        Pattern _p2 = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\:[0-9]{1,5}");
//        Matcher _m2 = _p2.matcher(_ip);
//
//        int _teste = 0;
//
//        if (_m1.find() == true) {
//            _teste += 1;
//        }
//
//        if (_m2.find() == true) {
//            _teste += 1;
//        }
//
//        if (_teste == 0) {
//
//            MSVMsgBox.showMsgBoxError(
//                    VendedorRegistrarActivity.this,
//                    "IP e PORTA",
//                    "O preenchimento do campo [Endereço IP do Servidor] deve respeitar o formato convencional"
//            );
//
//            return false;
//
//        }
        // endregion

        // endregion


        return _out;

    }
    // endregion


    // region onTaskCompleteListner
    private OnTaskCompleteListner onTaskCompleteListner = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

            // region Não encontrou o vendedor
            if (out == 0) {

                Toast.makeText(
                        VendedorRegistrarActivity.this,
                        "Acesso negado, vendedor inexistente ou os dados de acesso não conferem",
                        Toast.LENGTH_SHORT
                ).show();

            }
            // endregion

            // region Encontrou o vendedor
            if (out == 1) {

                // region Declarando variáveis do método
                SQLiteHelper _sqh = null;
                dbParametro _dbParametro = null;
                // endregion

                // region Bloco protegido para processamento dos dados
                try {

                    // region Instânciando o objeto de banco e abrindo a conexão
                    _sqh = new SQLiteHelper(VendedorRegistrarActivity.this);
                    _sqh.open(true);
                    // endregion

                    // region Armazenando no banco os valores informados no parâmetro

                    // region Instânciando o objeto de banco Parametro
                    _dbParametro = new dbParametro(_sqh);
                    // endregion

                    // region Cuidando do parametro VENDEDOR_CODIGO
                    if (_tpCodigo != null) {
                        _tpCodigo.ValorTexto = _txtCodigo.getText().toString().trim();

                        _dbParametro.update(_tpCodigo);
                    } else {
                        _tpCodigo = new tpParametro();
                        _tpCodigo.Nome = dbParametro.VENDEDOR_CODIGO;
                        _tpCodigo.ValorTexto = _txtCodigo.getText().toString().trim();

                        _dbParametro.insert(_tpCodigo);
                    }
                    // endregion

                    // region Cuidando do parametro VENDEDOR_HASH
                    if (_tpChave != null) {
                        _tpChave.ValorTexto = _txtChave.getText().toString().trim();

                        _dbParametro.update(_tpChave);
                    } else {
                        _tpChave = new tpParametro();
                        _tpChave.Nome = dbParametro.VENDEDOR_HASH;
                        _tpChave.ValorTexto = _txtChave.getText().toString().trim();

                        _dbParametro.insert(_tpChave);
                    }
                    // endregion

                    // region Cuidando do parametro SERVIDOR_REST_IP
                    if (_tpIp != null) {
                        _tpIp.ValorTexto = _txtIp.getText().toString().trim();

                        _dbParametro.update(_tpIp);
                    } else {
                        _tpIp = new tpParametro();
                        _tpIp.Nome = dbParametro.SERVIDOR_REST_IP;
                        _tpIp.ValorTexto = _txtIp.getText().toString().trim();

                        _dbParametro.insert(_tpIp);
                    }
                    // endregion

                    // endregion

                    // region Inserindo uma empresa inicial para poder efetuar login
                    tpEmpresa _tpEmpresa = new tpEmpresa();
                    _tpEmpresa.IdEmpresa = 1;
                    _tpEmpresa.Descricao = "EMPRESA SINCRONIZAÇÃO";
                    _tpEmpresa.Sigla = "ES";

                    dbEmpresa _dbEmpresa = new dbEmpresa(_sqh);
                    _dbEmpresa.insert(_tpEmpresa);
                    // endregion

                    // region Fechando a conexão com o banco de dados
                    _sqh.close();
                    // endregion

                } catch (Exception e) {
                    Log.e("VendedorRegistrarAct", "_btnVerificar.setOnClickListner -> " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if ((_sqh != null) && (_sqh.isOpen())) {
                        _sqh.close();
                    }
                }
                // endregion

                // region Informando o parametro de retorno e finalizando a activity
                setResult(Activity.RESULT_OK);
                finish();
                // endregion

            }
            // endregion

        }
    };
    // endregion

}
