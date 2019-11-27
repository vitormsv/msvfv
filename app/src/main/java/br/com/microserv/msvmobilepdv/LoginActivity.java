package br.com.microserv.msvmobilepdv;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvdal.dbEmpresa;
import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvinterface.OnSelectedItem;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.msvmobilepdv.adapter.EmpresaDialogSearchAdapter;


public class LoginActivity extends AppCompatActivity implements ActivityInterface {

    // region Declaracao de elementos
    private LinearLayout _pnlEmpresa;
    private LinearLayout _pnlSenha;
    private TextView _txtVendedorNome;
    private TextView _txtEmpresa;
    private TextView _txtSenha;
    private Button _btnEntrar;
    // endregion


    // region Declaracao de variáveis
    private boolean loginOk = false;

    // objetos
    ArrayList<tpEmpresa> _lstEmpresa = null;
    private tpVendedor _tp = null;
    EmpresaDialogSearchAdapter _adpEmpresa = null;

    // inteiro
    int _iAux = -1;
    int _iEmpresa = -1;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // region Adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);
        // endregion

        // reconhecendo os elementos contidos no layout
        this.bindElements();

        // associando os eventos aos elementos da tela
        this.bindEvents();

        // inicializando a activity
        this.initialize();

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // LinearLayout
        _pnlEmpresa = (LinearLayout) findViewById(R.id.pnlEmpresa);
        _pnlSenha = (LinearLayout) findViewById(R.id.pnlSenha);

        // TextView
        _txtVendedorNome = (TextView) findViewById(R.id.txtVendedorNome);
        _txtEmpresa = (TextView) findViewById(R.id.txtEmpresa);
        _txtSenha = (TextView) findViewById(R.id.txtSenha);

        // Button
        _btnEntrar = (Button) findViewById(R.id.btnEntrar);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Escolhendo a empresa
        _pnlEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Verificando se existe empresas
                if (_lstEmpresa.size() == 0) {
                    Toast.makeText(LoginActivity.this, "Não existem registros para pesquisa", Toast.LENGTH_SHORT).show();
                    return;
                }
                // endregion


                // region Montando o adaptador se necessário
                if (_adpEmpresa == null) {
                    _adpEmpresa = new EmpresaDialogSearchAdapter(
                            LoginActivity.this,
                            _lstEmpresa
                    );
                }
                // endregion


                // region Atualizando variável auxiliar
                _iAux = _iEmpresa;
                // endregion


                // region Abrindo a janela para escolha do tipo de pedido
                MSVMsgBox.getValueFromList(
                        LoginActivity.this,
                        _lstEmpresa.get(_iEmpresa).Descricao,
                        _adpEmpresa,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iEmpresa = _iAux;
                                _txtEmpresa.setText(_lstEmpresa.get(_iEmpresa).Descricao);
                            }
                        }
                );
                // endregions

            }
        });
        // endregion

        // region Informando a senha
        _pnlSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        LoginActivity.this,
                        "Senha",
                        "Informe a senha para acesso ao sistema Microserv FV",
                        "",
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    _txtSenha.setText(value);
                                } else {
                                    _txtSenha.setText("");
                                }
                            }
                        }
                );

            }
        });
        // endregion

        // region Clicando no botão de entrar
        _btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // region Verificando se a empresa foi selecionada
                if (_iEmpresa < 0) {
                    Toast.makeText(
                            LoginActivity.this,
                            "É necessário selecionar a empresa de trabalho",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                // endregion

                // region fechando a activity
                Intent _i = new Intent();
                _i.putExtra("tpEmpresa", _lstEmpresa.get(_iEmpresa));

                setResult(Activity.RESULT_OK, _i);
                finish();
                // endregion

            }
        });
        // endregion

    }
    // endregion


    // region initialize
    @Override
    public void initialize() {

        this.loadVendedor();
        this.loadEmpresa();

        _txtEmpresa.setText("");
        _txtSenha.setText("");

        if ((_lstEmpresa != null) && (_lstEmpresa.size() > 0)) {
            _iEmpresa = 0;
            _txtEmpresa.setText(_lstEmpresa.get(_iEmpresa).Descricao);
        }

    }
    // endregion


    // region loadVendedor
    private void loadVendedor(){

        try {

            // instânciando o objeto de conexão com o banco

            SQLiteHelper _sqlH = new SQLiteHelper(this.getBaseContext());
            _sqlH.open(false);


            // intanciando a classe dbVendedor
            dbVendedor _db = new dbVendedor(_sqlH);


            // recebendo na variável _tp o objeto contendo os dados
            // do vendedor vinculado ao dispositivo (_id = 1)
            _tp = (tpVendedor) _db.getById(1);


            // informando ao campo da tela o nome
            // do vendedor selecionado
            if (_tp != null) {
                _txtVendedorNome.setText(_tp.Nome);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // endregion


    // region loadEmpresa
    private void loadEmpresa() {

        _iEmpresa = -1;

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());

        try {

            _sqh.open(false);

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addOrderBy("Sigla", eSQLSortType.ASC);

            dbEmpresa _dbEmpresa = new dbEmpresa(_sqh);
            _lstEmpresa = (ArrayList<tpEmpresa>) _dbEmpresa.getList(tpEmpresa.class, _sch);

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

}
