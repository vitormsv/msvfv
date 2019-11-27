package br.com.microserv.msvmobilepdv.cliente;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvapi.ReceitaWSApi;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvdal.dbCidade;
import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbClienteTabelaPreco;
import br.com.microserv.framework.msvdal.dbEmpresa;
import br.com.microserv.framework.msvdal.dbRegiao;
import br.com.microserv.framework.msvdal.dbTabelaPreco;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteTabelaPreco;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpReceitaWs;
import br.com.microserv.framework.msvdto.tpRegiao;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnCloseDialog;
import br.com.microserv.framework.msvinterface.OnSelectedItem;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.CidadeDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.RegiaoDialogSearchAdapter;
import br.com.microserv.msvmobilepdv.adapter.TabelaPrecoDialogSearchAdapter;

public class ClienteEditarActivity extends AppCompatActivity implements ActivityInterface {

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
    static final String _CLIENTE_EDITAR_VALUE = "ClienteEditarActivity";

    // endregion


    ArrayList<tpCliente> _lstCliente = null;

    // region Declarando objetos da interface

    // LinearLayout
    LinearLayout _pnlCodigo = null;
    LinearLayout _pnlRazaoSocial = null;
    LinearLayout _pnlNomeFantasia = null;
    LinearLayout _pnlCnpjCpf = null;
    LinearLayout _pnlIeRg = null;
    LinearLayout _pnlEmail = null;
    LinearLayout _pnlContatoNome = null;
    LinearLayout _pnlLogradouroTipo = null;
    LinearLayout _pnlLogradouroNome = null;
    LinearLayout _pnlLogradouroNumero = null;
    LinearLayout _pnlBairro = null;
    LinearLayout _pnlCidade = null;
    LinearLayout _pnlEstado = null;
    LinearLayout _pnlCep = null;
    LinearLayout _pnlRegiao = null;
    LinearLayout _pnlArea = null;
    LinearLayout _pnlTelefoneFixo = null;
    LinearLayout _pnlTelefoneCelular = null;
    LinearLayout _pnlTabelaPrecoInclude = null;
    LinearLayout _inc_pnlTabelaPreco = null;

    // TextView
    TextView _txtCodigo = null;
    TextView _txtRazaoSocial = null;
    TextView _txtNomeFantasia = null;
    TextView _txtCnpjCpf = null;
    TextView _txtIeRg = null;
    TextView _txtEmail = null;
    TextView _txtContatoNome = null;
    TextView _txtLogradouroTipo = null;
    TextView _txtLogradouroNome = null;
    TextView _txtLogradouroNumero = null;
    TextView _txtBairro = null;
    TextView _txtCidade = null;
    TextView _txtEstado = null;
    TextView _txtCep = null;
    TextView _txtRegiao = null;
    TextView _txtArea = null;
    TextView _txtTelefoneFixo = null;
    TextView _txtTelefoneCelular = null;
    TextView _inc_txtTabelaPreco = null;

    // ImageView
    ImageView _imgCnpjCpfFindWs = null;

    // Button
    Button _btnCancelar = null;
    Button _btnOk = null;

    // endregion


    // region Declarando variaveis locais

    // Objetos
    Bundle _extras = null;
    RegiaoDialogSearchAdapter _adpRegiao = null;
    tpEmpresa _tpEmpresa = null;
    tpCliente _tpCliente = null;
    tpReceitaWs _tpReceitaWs = null;

    // Dialogs
    ProgressDialog _wait = null;

    // ArrayList
    ArrayList<tpRegiao> _lstRegiao = null;
    ArrayList<tpCidade> _lstCidade = null;
    ArrayList<tpEmpresa> _lstEmpresa = null;
    ArrayList<tpTabelaPreco> _lstTabelaPreco = null;
    ArrayList<View> _lstTabelas = null;

    // String
    String _sourceActivity = "";

    // int
    int _metodoEdicao = 0;
    int _iCidade = 0;
    int _iRegiao = 0;
    int _iTabelaPreco = 0;
    int _iAux = 0;

    // long
    long _IdCliente = 0;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_editar);
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
                        ClienteEditarActivity.this,
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
                        ClienteEditarActivity.this,
                        "O parâmetro _KEY_SOURCE_ACTIVITY não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_ID_CLIENTE
            if (_extras.containsKey(_KEY_ID_CLIENTE)) {
                _IdCliente = _extras.getLong(_KEY_ID_CLIENTE);
            } else {
                Toast.makeText(
                        ClienteEditarActivity.this,
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
                        ClienteEditarActivity.this,
                        "O parâmetro _KEY_TP_EMPRESA não foi informado",
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


    // region onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_cliente_editar, menu);
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
                ClienteEditarActivity.this,
                "Deseja realmente encerrar esta tela ?",
                "Se clicar em OK os dados incluidos ou alterados nesta tela serão perdidos",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {
                            ClienteEditarActivity.super.onBackPressed();
                        }

                    }
                }
        );

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // LinearLayout
        _pnlCodigo = (LinearLayout) findViewById(R.id.pnlCodigo);
        _pnlRazaoSocial = (LinearLayout) findViewById(R.id.pnlRazaoSocial);
        _pnlNomeFantasia = (LinearLayout) findViewById(R.id.pnlNomeFantasia);
        _pnlCnpjCpf = (LinearLayout) findViewById(R.id.pnlCnpjCpf);
        _pnlIeRg = (LinearLayout) findViewById(R.id.pnlIeRg);
        _pnlEmail = (LinearLayout) findViewById(R.id.pnlEmail);
        _pnlContatoNome = (LinearLayout) findViewById(R.id.pnlContatoNome);
        _pnlLogradouroTipo = (LinearLayout) findViewById(R.id.pnlLogradouroTipo);
        _pnlLogradouroNome = (LinearLayout) findViewById(R.id.pnlLogradouroNome);
        _pnlLogradouroNumero = (LinearLayout) findViewById(R.id.pnlLogradouroNumero);
        _pnlBairro = (LinearLayout) findViewById(R.id.pnlBairro);
        _pnlCidade = (LinearLayout) findViewById(R.id.pnlCidade);
        _pnlEstado = (LinearLayout) findViewById(R.id.pnlEstado);
        _pnlCep = (LinearLayout) findViewById(R.id.pnlCep);
        _pnlRegiao = (LinearLayout) findViewById(R.id.pnlRegiao);
        _pnlArea = (LinearLayout) findViewById(R.id.pnlArea);
        _pnlTelefoneFixo = (LinearLayout) findViewById(R.id.pnlTelefoneFixo);
        _pnlTelefoneCelular = (LinearLayout) findViewById(R.id.pnlTelefoneCelular);
        _pnlTabelaPrecoInclude = (LinearLayout) findViewById(R.id.pnlTabelaPrecoInclude);

        // TextView
        _txtCodigo = (TextView) findViewById(R.id.txtCodigo);
        _txtRazaoSocial = (TextView) findViewById(R.id.txtRazaoSocial);
        _txtNomeFantasia = (TextView) findViewById(R.id.txtNomeFantasia);
        _txtCnpjCpf = (TextView) findViewById(R.id.txtCnpjCpf);
        _txtIeRg = (TextView) findViewById(R.id.txtIeRg);
        _txtEmail = (TextView) findViewById(R.id.txtEmail);
        _txtContatoNome = (TextView) findViewById(R.id.txtContatoNome);
        _txtLogradouroTipo = (TextView) findViewById(R.id.txtLogradouroTipo);
        _txtLogradouroNome = (TextView) findViewById(R.id.txtLogradouroNome);
        _txtLogradouroNumero = (TextView) findViewById(R.id.txtLogradouroNumero);
        _txtBairro = (TextView) findViewById(R.id.txtBairro);
        _txtCidade = (TextView) findViewById(R.id.txtCidade);
        _txtEstado = (TextView) findViewById(R.id.txtEstado);
        _txtCep = (TextView) findViewById(R.id.txtCep);
        _txtRegiao = (TextView) findViewById(R.id.txtRegiao);
        _txtArea = (TextView) findViewById(R.id.txtArea);
        _txtTelefoneFixo = (TextView) findViewById(R.id.txtTelefoneFixo);
        _txtTelefoneCelular = (TextView) findViewById(R.id.txtTelefoneCelular);

        // ImageView
        _imgCnpjCpfFindWs = (ImageView) findViewById(R.id.imgCnpjCpfFindWs);

        // Button
        _btnCancelar = (Button) findViewById(R.id.btnCancelar);
        _btnOk = (Button) findViewById(R.id.btnOk);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

        // region Razao Social
        _pnlRazaoSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ClienteEditarActivity.this,
                        "RAZÃO SOCIAL",
                        "Informe a razão social do cliente",
                        _txtRazaoSocial.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.RazaoSocial = value.trim().toUpperCase();
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Nome Fantasia
        _pnlNomeFantasia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ClienteEditarActivity.this,
                        "NOME FANTASIA",
                        "Informe o nome fantasia do cliente",
                        _txtNomeFantasia.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.NomeFantasia = value.trim().toUpperCase();
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Cnpj Cpf
        _pnlCnpjCpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringNumberValue(
                        ClienteEditarActivity.this,
                        "CNPJ ou CPF",
                        "Informe CNPJ ou o CPF do cliente",
                        _txtCnpjCpf.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.CnpjCpf = MSVUtil.onlyNumber(value.trim().toUpperCase());

                                    if (_tpCliente.CnpjCpf.length() == 11) {
                                        if (MSVUtil.isCpfValid(_tpCliente.CnpjCpf) == false) {
                                            MSVMsgBox.showMsgBoxWarning(
                                                    ClienteEditarActivity.this,
                                                    "CPF inválido",
                                                    "O número do CPF é inválido"
                                            );
                                            _tpCliente.CnpjCpf = "";
                                        }
                                    }

                                    if (_tpCliente.CnpjCpf.length() == 14) {
                                        if (MSVUtil.isCnpjValid(_tpCliente.CnpjCpf) == false) {
                                            MSVMsgBox.showMsgBoxWarning(
                                                    ClienteEditarActivity.this,
                                                    "CNPJ inválido",
                                                    "O número do CNPJ é inválido"
                                            );
                                            _tpCliente.CnpjCpf = "";
                                        }
                                    }

                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Inscricao Estadual ou RG
        _pnlIeRg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ClienteEditarActivity.this,
                        "IE ou RG",
                        "Informe a inscrição estadual ou o RG do cliente",
                        _txtIeRg.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.IeRg = MSVUtil.onlyNumber(value.trim().toUpperCase());
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region TelefoneFixo
        _pnlTelefoneFixo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getPhoneValue(
                        ClienteEditarActivity.this,
                        "TELEFONE FIXO",
                        "Informe o telefone fixo do cliente (informar somente os números com o DDD)",
                        _txtTelefoneFixo.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.TelefoneFixo = MSVUtil.onlyNumber(value.trim().toLowerCase());

                                    if (_tpCliente.TelefoneFixo.length() != 10) {

                                        _tpCliente.TelefoneFixo = "";

                                        Toast.makeText(
                                                ClienteEditarActivity.this,
                                                "O telefone fixo informado não contém os 10 caracteres numéricos necessários",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    }

                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region TelefoneCelular
        _pnlTelefoneCelular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getPhoneValue(
                        ClienteEditarActivity.this,
                        "TELEFONE CELULAR",
                        "Informe o telefone celular do cliente (informar somente os números com o DDD)",
                        _txtTelefoneCelular.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.TelefoneCelular = MSVUtil.onlyNumber(value.trim().toLowerCase());

                                    if (_tpCliente.TelefoneCelular.length() != 11) {

                                        _tpCliente.TelefoneCelular = "";

                                        Toast.makeText(
                                                ClienteEditarActivity.this,
                                                "O telefone celular informado não contém os 11 caracteres numéricos necessários",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    }

                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Email
        _pnlEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getEmailValue(
                        ClienteEditarActivity.this,
                        "EMAIL",
                        "Informe o e-mail do cliente para recebimento dos arquivos de notas fiscais XML",
                        _txtEmail.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.Email = value.trim().toLowerCase();
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Contato
        _pnlContatoNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ClienteEditarActivity.this,
                        "CONTATO",
                        "Informe o nome de contato para o cliente",
                        _txtContatoNome.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.ContatoNome = value.trim().toUpperCase();
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Logradouro Tipo
        _pnlLogradouroTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ClienteEditarActivity.this,
                        "LOGRADOURO TIPO",
                        "Informe o tipo de logradouro do cliente (ex: Rua, Avenida, etc...)",
                        _txtLogradouroTipo.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.LogradouroTipo = value.trim().toUpperCase();
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Logradouro Nome
        _pnlLogradouroNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ClienteEditarActivity.this,
                        "LOGRADOURO NOME",
                        "Informe o nome do logradouro",
                        _txtLogradouroNome.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.LogradouroNome = value.trim().toUpperCase();
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Logradouro Numero
        _pnlLogradouroNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ClienteEditarActivity.this,
                        "LOGRADOURO NUMERO",
                        "Informe o número referente ao imóvel no logradouro",
                        _txtLogradouroNumero.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.LogradouroNumero = value.trim().toUpperCase();
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Bairro
        _pnlBairro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringValue(
                        ClienteEditarActivity.this,
                        "BAIRRO",
                        "Informe bairro referente ao endereço do imóvel",
                        _txtBairro.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.BairroNome = value.trim().toUpperCase();
                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Cidade (lookup)
        _pnlCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                _iAux = 0;

                // region Criando o adaptador para a lista de cidades
                final CidadeDialogSearchAdapter _adp = new CidadeDialogSearchAdapter(ClienteEditarActivity.this, _lstCidade);
                // endregion

                // region Inflando o layout customizado para o AlertDialog
                // inflando o layout
                LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);

                // bucando o elemento do título
                final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
                _txtDialogTitle.setText(_lstCidade.get(_iCidade).Descricao);

                // buscando o elemento ListView
                ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

                _lv.setAdapter(_adp);

                _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        _iAux = position;
                        _txtDialogTitle.setText(_lstCidade.get(_iAux).Descricao);
                    }
                });
                // endregion

                // region Criando a janela modal AlertDialog
                final AlertDialog.Builder _builder = new AlertDialog.Builder(ClienteEditarActivity.this);

                _builder.setView(_v);
                _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // aqui vamos realizar o refresh do grupo e do produto
                        _iCidade = _iAux;

                        _tpCliente.IdCidade = _lstCidade.get(_iCidade).IdCidade;
                        _tpCliente.Cidade = _lstCidade.get(_iCidade);

                        refreshCliente();

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
                // endregion

            }
        });
        // endregion

        // region Cep
        _pnlCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MSVMsgBox.getStringNumberValue(
                        ClienteEditarActivity.this,
                        "CEP",
                        "Informe o cep referente ao endereço do cliente.\nNão é necessário informar os caracteres de mascara",
                        _txtCep.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {

                                if (isOk) {
                                    _tpCliente.Cep = MSVUtil.onlyNumber(value.trim().toUpperCase());

                                    if (_tpCliente.Cep.length() != 8) {

                                        MSVMsgBox.showMsgBoxWarning(
                                                ClienteEditarActivity.this,
                                                "CEP inválido",
                                                "O número do CEP deve conter exatamente 8 caracteres"
                                        );

                                        _tpCliente.Cep = "";

                                    }

                                    refreshCliente();
                                }

                            }
                        }
                );

            }
        });
        // endregion

        // region Região (lookup)
        _pnlRegiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _iAux = 0;

                // region Montando o adaptador se necessário
                if (_adpRegiao == null) {
                    _adpRegiao = new RegiaoDialogSearchAdapter(
                            ClienteEditarActivity.this,
                            _lstRegiao
                    );
                }
                // endregion

                // region Abrindo a janela para escolha do tipo de pedido
                MSVMsgBox.getValueFromList(
                        ClienteEditarActivity.this,
                        _lstRegiao.get(_iRegiao).Descricao,
                        _adpRegiao,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iRegiao = _iAux;

                                _tpCliente.IdRegiao = _lstRegiao.get(_iRegiao).IdRegiao;
                                _tpCliente.Regiao = _lstRegiao.get(_iRegiao);

                                refreshCliente();
                            }
                        }
                );
                // endregion

            }
        });
        // endregion

        // region Buscar Cnpj no API da receita federal
        _imgCnpjCpfFindWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MSVUtil.isConnected(ClienteEditarActivity.this) == false) {

                    MSVMsgBox.showMsgBoxWarning(
                            ClienteEditarActivity.this,
                            "Dispositivo sem conexão com internet",
                            "Para consultar os dados do cliente através do servidor da Receita Federal, é necessário que o dispositivo esteja conectado na internet via WI-FI ou conexão 3|4G"
                    );

                } else {

                    // region Mostrando a caixa de dialogo de espera
                    _wait = ProgressDialog.show(
                            ClienteEditarActivity.this,
                            "Aguarde",
                            "Buscando os dados do cliente no servidor da Receita Federal...",
                            true,
                            false
                    );
                    // endregion

                    new ReceitaWSApi(
                            ClienteEditarActivity.this,
                            _tpCliente.CnpjCpf,
                            onTaskCompleteListner
                    ).execute();

                }

            }
        });
        // endregion

    }
    // endregion


    // region initialize
    public void initialize() {

        // region Cuidando do estado inicial da activity

        _imgCnpjCpfFindWs.setVisibility(View.GONE);

        this.clearComponents();

        // endregion


        // region Realizando a leitura dos dados necessários

        this.loadEmpresa();
        this.loadCliente();
        this.loadCidade();
        this.loadRegiao();

        // endregion


        // region Atualizando o layout da activity

        this.generateComponents();
        this.refreshCliente();

        // endregion

    }
    // endregion


    // region clearComponents
    private void clearComponents() {

        // TextView
        _txtCodigo.setText("");
        _txtRazaoSocial.setText("");
        _txtNomeFantasia.setText("");
        _txtCnpjCpf.setText("");
        _txtIeRg.setText("");
        _txtEmail.setText("");
        _txtContatoNome.setText("");
        _txtLogradouroTipo.setText("");
        _txtLogradouroNome.setText("");
        _txtLogradouroNumero.setText("");
        _txtBairro.setText("");
        _txtCidade.setText("");
        _txtEstado.setText("");
        _txtCep.setText("");
        _txtRegiao.setText("");
        _txtArea.setText("");

    }
    // endregion


    // region generateComponents
    private void generateComponents() {

        if ((_lstEmpresa != null) && (_lstEmpresa.size() > 0)) {

            if (_lstTabelas == null) {
                _lstTabelas = new ArrayList<View>();
            }

            for (tpEmpresa _tp : _lstEmpresa) {

                Object _o = (long) _tp.IdEmpresa;
                View _view = null;

                LayoutInflater _li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                _view = (View) _li.inflate(R.layout.include_tabela_preco_lookup, null);

                TextView _txtTabelaPrecoTitle = (TextView) _view.findViewById(R.id.txtTabelaPrecoTitle);
                _txtTabelaPrecoTitle.setText(_tp.Sigla);

                LinearLayout _pnlTabelaPreco = (LinearLayout) _view.findViewById(R.id.pnlTabelaPreco);
                _pnlTabelaPreco.setTag(_o);
                _pnlTabelaPreco.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lookupTabelaPreco(v);
                    }
                });

                _lstTabelas.add(_view);
                _pnlTabelaPrecoInclude.addView(_view);
            }

        }

    }
    // endregion


    // region loadEmpresa
    private void loadEmpresa() {

        SQLiteHelper _sqh = null;

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addOrderBy("Sigla", eSQLSortType.ASC);


            _sqh = new SQLiteHelper(ClienteEditarActivity.this);
            _sqh.open(false);


            dbEmpresa _dbEmpresa = new dbEmpresa(_sqh);
            _lstEmpresa = (ArrayList<tpEmpresa>) _dbEmpresa.getList(tpEmpresa.class, _sch);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region loadTabelaPreco
    private void loadTabelaPreco(long idEmpresa) {

        SQLiteHelper _sqh = null;

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addEqualInteger("IdEmpresa", idEmpresa);
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);


            _sqh = new SQLiteHelper(ClienteEditarActivity.this);
            _sqh.open(false);


            dbTabelaPreco _dbTabelaPreco = new dbTabelaPreco(_sqh);
            _lstTabelaPreco = (ArrayList<tpTabelaPreco>) _dbTabelaPreco.getList(tpTabelaPreco.class, _sch);


            if ((_lstTabelaPreco != null) && (_lstTabelaPreco.size() > 0)) {
                _iTabelaPreco = 0;
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


    // region loadCliente
    private void loadCliente() {

        // region OPERACAO == INSERT
        if (_metodoEdicao == _INSERT_VALUE) {

            _tpCliente = new tpCliente();
            _tpCliente.Tabelas = new ArrayList<tpTabelaPreco>();

        }
        // endregion


        // region OPERACAO == UPDATE
        if (_metodoEdicao == _UPDATE_VALUE) {

            SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());

            try {

                _sqh.open(false);

                // region Recuperando os dados do cliente
                dbCliente _dbCliente = new dbCliente(_sqh);
                _tpCliente = (tpCliente) _dbCliente.getBySourceId(_IdCliente);

                if (_tpCliente == null) {
                    Toast.makeText(
                            ClienteEditarActivity.this,
                            "Não foi possível localizar o cliente",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                // endregion

                // region Recuperando o objeto Cidade vinculado
                dbCidade _dbCidade = new dbCidade(_sqh);
                _tpCliente.Cidade = (tpCidade) _dbCidade.getBySourceId(_tpCliente.IdCidade);
                // endregion

                // region Recuperando o objeto Regiao vinculado
                dbRegiao _dbRegiao = new dbRegiao(_sqh);
                _tpCliente.Regiao = (tpRegiao) _dbRegiao.getBySourceId(_tpCliente.IdRegiao);
                // endregion

                // region Recuperando o vinculo de tabelas de preço
                ArrayList<tpClienteTabelaPreco> _al = null;

                SQLClauseHelper _sch = new SQLClauseHelper();
                _sch.addEqualInteger("IdCliente", _IdCliente);

                dbClienteTabelaPreco _dbClienteTabelaPreco = new dbClienteTabelaPreco(_sqh);
                _al = (ArrayList<tpClienteTabelaPreco>) _dbClienteTabelaPreco.getList(tpClienteTabelaPreco.class, _sch);
                // endregion

                // region Recuperando as tabelas de preço
                dbTabelaPreco _dbTabelaPreco = null;

                for (tpClienteTabelaPreco _tp : _al) {

                    if (_dbTabelaPreco == null) {
                        _dbTabelaPreco = new dbTabelaPreco(_sqh);
                    }

                    if (_tpCliente.Tabelas == null) {
                        _tpCliente.Tabelas = new ArrayList<tpTabelaPreco>();
                    }

                    _tpCliente.Tabelas.add((tpTabelaPreco) _dbTabelaPreco.getBySourceId(_tp.IdTabelaPreco));
                }
                // endregion

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

    // endregion


    // region refreshCliente
    private void refreshCliente() {

        // escondendo o botão de busca de cnpj na receita federal
        _imgCnpjCpfFindWs.setVisibility(View.GONE);

        if (_tpCliente != null) {

            // region Atualizando os dados base do cliente
            if (_metodoEdicao == _INSERT_VALUE) {
                _txtCodigo.setText("AUTOMÁTICO");
            } else {
                _txtCodigo.setText(_tpCliente.Codigo);
            }

            _txtRazaoSocial.setText(_tpCliente.RazaoSocial);
            _txtNomeFantasia.setText(_tpCliente.NomeFantasia);


            // apresentnado o Cnpj ou Cpf
            switch (_tpCliente.CnpjCpf.length()) {

                case 11:
                    _txtCnpjCpf.setText(MSVUtil.formatCpf(_tpCliente.CnpjCpf));
                    break;

                case 14:
                    _txtCnpjCpf.setText(MSVUtil.formatCnpj(_tpCliente.CnpjCpf));
                    _imgCnpjCpfFindWs.setVisibility(View.VISIBLE);
                    break;

                default:
                    _txtCnpjCpf.setText("");
                    break;

            }


            _txtIeRg.setText(_tpCliente.IeRg);
            _txtEmail.setText(_tpCliente.Email);
            _txtContatoNome.setText(_tpCliente.ContatoNome);
            _txtLogradouroTipo.setText(_tpCliente.LogradouroTipo);
            _txtLogradouroNome.setText(_tpCliente.LogradouroNome);
            _txtLogradouroNumero.setText(_tpCliente.LogradouroNumero);
            _txtBairro.setText(_tpCliente.BairroNome);
            _txtCep.setText(_tpCliente.Cep);
            _txtTelefoneFixo.setText(MSVUtil.formatTelefoneFixo(_tpCliente.TelefoneFixo));
            _txtTelefoneCelular.setText(MSVUtil.formatTelefoneCelular(_tpCliente.TelefoneCelular));
            // endregion

            // region Atualizando as informações referente a cidade/estado
            _txtCidade.setText("");
            _txtEstado.setText("");

            if (_tpCliente.Cidade != null) {
                _txtCidade.setText(_tpCliente.Cidade.Descricao);
                _txtEstado.setText(_tpCliente.Cidade.EstadoSigla);
            }
            // endregion

            // region Atualizando as informações de região/área
            _txtRegiao.setText("");
            _txtArea.setText("");

            if (_tpCliente.Regiao != null) {
                _txtRegiao.setText(_tpCliente.Regiao.Descricao);
                _txtArea.setText(_tpCliente.Regiao.AreaDescricao);
            }
            // endregion

            // region Atualizando as informações de tabelas de preço
            if ((_tpCliente.Tabelas != null) && (_tpCliente.Tabelas.size() > 0)) {

                for (tpTabelaPreco _tp : _tpCliente.Tabelas) {

                    for (View _view : _lstTabelas) {

                        _inc_pnlTabelaPreco = (LinearLayout) _view.findViewById(R.id.pnlTabelaPreco);

                        if (_inc_pnlTabelaPreco != null) {

                            // recuperando o IdEmpresa
                            long _l = Long.parseLong(_inc_pnlTabelaPreco.getTag().toString());

                            if (_l == _tp.IdEmpresa) {
                                _inc_txtTabelaPreco = (TextView) _view.findViewById(R.id.txtTabelaPreco);

                                if (_inc_txtTabelaPreco != null) {
                                    _inc_txtTabelaPreco.setText(_tp.Descricao);
                                }
                            }

                            break;

                        }

                    }

                }

            }
            // endregion

        }

    }
    // endregion


    // region loadCidade
    private void loadCidade() {

        _iCidade = -1;

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
        _sqh.open(false);

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            dbCidade _dbCidade = new dbCidade(_sqh);
            _lstCidade = (ArrayList<tpCidade>) _dbCidade.getList(tpCidade.class, _sch);

            if ((_lstCidade != null) && _lstCidade.size() > 0) {
                _iCidade = 0;
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


    // region refreshCidade
    private void refreshCidade() {

        String _sCidade;

        if (_iCidade > -1) {
            _sCidade = String.format(
                    "%1s (%2s)",
                    _lstCidade.get(_iCidade).Descricao,
                    _lstCidade.get(_iCidade).EstadoSigla
            );
        } else {
            _sCidade = "NÃO EXISTE CIDADE";
        }

        _txtCidade.setText(_sCidade);

    }
    // endregion


    // region loadRegiao
    private void loadRegiao() {

        _iRegiao = -1;

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
        _sqh.open(false);

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            dbRegiao _dbRegiao = new dbRegiao(_sqh);
            _lstRegiao = (ArrayList<tpRegiao>) _dbRegiao.getList(tpRegiao.class, _sch);

            if ((_lstRegiao != null) && _lstRegiao.size() > 0) {
                _iRegiao = 0;
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


    // region refreshRegiao
    private void refreshRegiao() {

        _txtRegiao.setText("");
        _txtArea.setText("");

        if (_iRegiao > -1) {
            _txtRegiao.setText(_lstRegiao.get(_iRegiao).Descricao);
            _txtArea.setText(_lstRegiao.get(_iRegiao).AreaDescricao);
        }

    }
    // endregion


    // region lookupTabelaPreco
    private void lookupTabelaPreco(final View v) {

        // region Selecionando o IdEmpresa
        final long _idEmpresa = Long.parseLong(v.getTag().toString());
        // endregion


        // region Primeiro passo é carregar a lista de tabelas de preço de acordo com a empresa
        this.loadTabelaPreco(_idEmpresa);
        // endregion


        // region Se a operação for de update então precisamos posicionar o item na lista
        if (_metodoEdicao == _UPDATE_VALUE) {

        }
        // endregion


        // region Gerando a lista de escolha da tabela

        // region Criando o adaptador para a lista de cidades
        final TabelaPrecoDialogSearchAdapter _adp = new TabelaPrecoDialogSearchAdapter(ClienteEditarActivity.this, _lstTabelaPreco);
        // endregion

        // region Inflando o layout customizado para o AlertDialog
        // inflando o layout
        LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
        View _v = (View) _inflater.inflate(R.layout.dialog_personalizado_lista, null);

        // bucando o elemento do título
        final TextView _txtDialogTitle = (TextView) _v.findViewById(R.id.txtDialogTitle);
        _txtDialogTitle.setText(_lstTabelaPreco.get(_iTabelaPreco).Descricao);

        // buscando o elemento ListView
        ListView _lv = (ListView) _v.findViewById(R.id.livDialogData);

        _lv.setAdapter(_adp);

        _lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _iAux = position;
                _txtDialogTitle.setText(_lstTabelaPreco.get(_iAux).Descricao);
            }
        });
        // endregion

        // region Criando a janela modal AlertDialog
        final AlertDialog.Builder _builder = new AlertDialog.Builder(ClienteEditarActivity.this);

        _builder.setView(_v);
        _builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // region Recupando a posição (index) do registro selecionado
                _iTabelaPreco = _iAux;
                // endregion

                // region Removendo a tabela vinculada ao cliente de acordo com a empresa
                for (tpTabelaPreco _tp : _tpCliente.Tabelas) {
                    if (_tp.IdEmpresa == _idEmpresa) {
                        _tpCliente.Tabelas.remove(_tp);
                    }
                }
                // endregion

                // region Informando a nova tabela para o cliente de acordo com a empresa
                try {

                    // cloando a tabela de preço selecionada pelo usuário
                    tpTabelaPreco _tpTabelaPrecoClone = (tpTabelaPreco) _lstTabelaPreco.get(_iTabelaPreco).clone();

                    // adicionando a tabela selecionada na lista do cliente
                    _tpCliente.Tabelas.add(_tpTabelaPrecoClone);

                    // atualizando a tela do dispositivo
                    _inc_txtTabelaPreco = (TextView) v.findViewById(R.id.txtTabelaPreco);

                    if (_inc_txtTabelaPreco != null) {
                        _inc_txtTabelaPreco.setText(_lstTabelaPreco.get(_iTabelaPreco).Descricao);
                    }

                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                // endregion
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
        // endregion

        // endregion

    }
    // endregion


    // region cancelar
    private void cancelar() {

        MSVMsgBox.showMsgBoxQuestion(
                ClienteEditarActivity.this,
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

        // region Validando o campo Razao Social
        if (MSVUtil.isNullOrEmpty(_tpCliente.RazaoSocial)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Razão Social",
                    "O campo razão social deve ser preenchido"
            );

            return;
        }
        // endregion


        // region Validando o campo Nome Fantasia
        if (MSVUtil.isNullOrEmpty(_tpCliente.NomeFantasia)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Nome Fantasia",
                    "O campo nome fantasia deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Validando o campo CnpjCpf
        if (MSVUtil.isNullOrEmpty(_tpCliente.CnpjCpf)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "CNPJ/CPF",
                    "O campo de documento (CNPJ/CPF) deve ser preenchido"
            );

            return;

        } else {

            String _documento = MSVUtil.onlyNumber(_tpCliente.CnpjCpf);

            // aqui deverá ser validado o número do CPF
            if (_documento.length() == 11) {
                /*
                if (MSVUtil.isCpfValid(_documento) == false) {

                    MSVMsgBox.showMsgBoxWarning(
                            ClienteEditarActivity.this,
                            "CPF",
                            "O valor do CPF informado não é válido"
                    );

                    return;

                }
                */
            }


            // aqui deverá ser validado o número do CNPJ
            if (_documento.length() == 14) {
                /*
                if (MSVUtil.isCnpjValid(_documento) == false) {

                    MSVMsgBox.showMsgBoxWarning(
                            ClienteEditarActivity.this,
                            "CNPJ",
                            "O valor do CNPJ informado não é válido"
                    );

                    return;

                }
                */
            }

        }
        // endregion


        // region Validando o campo IeRg
        if (MSVUtil.isNullOrEmpty(_tpCliente.IeRg)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "IE/RG",
                    "O campo IE/RG deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Verificando se já existe cliente com o mesmo Cnpj ou Cpf
        if(this.ValidarCnpjCpfJaCadastrado(_tpCliente))
        {
            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "CNPJ/CPF",
                    "Já existe um cliente com o mesmo CNPJ/CPF"
            );

            return;

        }
        // endregion


        // region Validando o campo de Telefone
        if (MSVUtil.isNullOrEmpty(_tpCliente.TelefoneFixo) && MSVUtil.isNullOrEmpty(_tpCliente.TelefoneCelular)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Telefone Fixo/Celular",
                    "Ao menos um telefone deve ser preenchido (fixo/celular)"
            );

            return;

        }
        // endregion


        // region Validando o campo Logradouro Tipo
        if (MSVUtil.isNullOrEmpty(_tpCliente.LogradouroTipo)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Tipo de Logradouro",
                    "O campo tipo de logradouro deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Validando o campo Logradouro Nome
        if (MSVUtil.isNullOrEmpty(_tpCliente.LogradouroNome)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Nome do Logradouro",
                    "O campo nome do logradouro deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Validando o campo Logradouro Numero
        if (MSVUtil.isNullOrEmpty(_tpCliente.LogradouroNumero)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Número do Logradouro",
                    "O campo número do logradouro deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Validando o campo Bairro
        if (MSVUtil.isNullOrEmpty(_tpCliente.BairroNome)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Bairro",
                    "O campo bairro deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Validando o campo Cidade
        if (_tpCliente.IdCidade == 0) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Cidade",
                    "O campo cidade deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Validando o campo CEP
        if (MSVUtil.isNullOrEmpty(_tpCliente.Cep)) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "CEP",
                    "O campo CEP deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Validando o campo Regiao
        if (_tpCliente.IdRegiao == 0) {

            MSVMsgBox.showMsgBoxInfo(
                    ClienteEditarActivity.this,
                    "Região",
                    "O campo região deve ser preenchido"
            );

            return;

        }
        // endregion


        // region Verificando se o usuário quer realmente efetivar a operação em andamento
        MSVMsgBox.showMsgBoxQuestion(
                ClienteEditarActivity.this,
                "Deseja realmente salvar os dados da operação em andamento ?",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {

                            SQLiteHelper _sqh = null;
                            dbCliente _dbCliente = null;
                            dbClienteTabelaPreco _dbClienteTabelaPreco = null;

                            try {

                                // region Iniciando o helper do banco de dados
                                _sqh = new SQLiteHelper(ClienteEditarActivity.this);
                                _sqh.open(true);
                                // endregion

                                // region Iniciando a transação com o banco de dados
                                _sqh.initTransaction();
                                // endregion

                                // region Operacao de inclusão
                                if (_metodoEdicao == _INSERT_VALUE) {

                                    // region Gerando um código para o cliente
                                    _tpCliente.IdCliente = System.currentTimeMillis();
                                    _tpCliente.Codigo = String.valueOf(_tpCliente.IdCliente);
                                    // endregion

                                    // region Inserindo os dados do cliente
                                    if (_dbCliente == null) {
                                        _dbCliente = new dbCliente(_sqh);
                                    }

                                    _dbCliente.insert(_tpCliente);
                                    // endregion

                                }
                                // endregion

                                // region Operacao de alteração
                                if (_metodoEdicao == _UPDATE_VALUE) {

                                    // region Alterando os dados do cliente
                                    if (_dbCliente == null) {
                                        _dbCliente = new dbCliente(_sqh);
                                    }

                                    _dbCliente.update(_tpCliente);
                                    // endregion

                                }
                                // endregion

                                // region Inserindo as tabelas de preço vinculadas
                                // instânciando objeto db se necessário
                                if (_dbClienteTabelaPreco == null) {
                                    _dbClienteTabelaPreco = new dbClienteTabelaPreco(_sqh);
                                }

                                // criando WHERE para o campo IDCLIENTE
                                SQLClauseHelper _schDelete = new SQLClauseHelper();
                                _schDelete.addEqualInteger("IdCliente", _IdCliente);

                                // removendo todas as tabelas do cliente
                                _dbClienteTabelaPreco.delete(_schDelete);

                                // agora vamos INSERIR as novas tabelas do cliente
                                if (_tpCliente.Tabelas != null) {
                                    for (tpTabelaPreco _tp : _tpCliente.Tabelas) {

                                        tpClienteTabelaPreco _tpClienteTabelaPreco = new tpClienteTabelaPreco();

                                        _tpClienteTabelaPreco.IdEmpresa = _tp.IdEmpresa;
                                        _tpClienteTabelaPreco.IdCliente = _tpCliente.IdCliente;
                                        _tpClienteTabelaPreco.IdTabelaPreco = _tp.IdTabelaPreco;

                                        _dbClienteTabelaPreco.insert(_tpClienteTabelaPreco);

                                    }
                                }
                                // endregion


                                // region Realizando o commit da transação
                                _sqh.commitTransaction();
                                // endregion

                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            } finally {
                                _sqh.endTransaction();

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


    // region OnTaskCompleteListner
    private OnTaskCompleteListner onTaskCompleteListner = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

            _wait.dismiss();

            if (status == eTaskCompleteStatus.SUCCESS) {

                if (tpOut != null) {

                    _tpReceitaWs = (tpReceitaWs) tpOut;

                    _tpCliente.RazaoSocial = _tpReceitaWs.Nome;
                    _tpCliente.NomeFantasia = _tpReceitaWs.Fantasia;
                    _tpCliente.TelefoneFixo = _tpReceitaWs.Telefone;
                    _tpCliente.Email = _tpReceitaWs.Email;
                    _tpCliente.LogradouroNome = _tpReceitaWs.Logradouro;
                    _tpCliente.LogradouroNumero = _tpReceitaWs.Numero;
                    _tpCliente.BairroNome = _tpReceitaWs.Bairro;
                    _tpCliente.Cep = _tpReceitaWs.Cep;

                    // region Validando o campo TelefoneFixo
                    _tpCliente.TelefoneFixo = MSVUtil.onlyNumber(_tpReceitaWs.Telefone);

                    if (_tpCliente.TelefoneFixo.length() != 10) {
                        _tpCliente.TelefoneFixo = "";
                    }
                    // endregion

                    // region Validando o campo Cep
                    _tpCliente.Cep = MSVUtil.onlyNumber(_tpReceitaWs.Cep);

                    if (_tpCliente.Cep.length() != 8) {
                        _tpCliente.Cep = "";

                    }
                    // endregion

                    refreshCliente();

                }

            }

        }
    };
    // endregion


    // region ValidarCnpjCpfJaCadastrado
    private boolean ValidarCnpjCpfJaCadastrado(tpCliente _tpCliente)
    {

        // region Declarando variável de retorno
        boolean _Out = false;
        // endregion

        // region Declarando demais variáveis do método
        SQLiteHelper _sqh = null;
        SQLClauseHelper _schCnpjCpf = null;

        tpCliente _tpClienteFound = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Trabalhando a condição WHERE de consulta
            _schCnpjCpf = new SQLClauseHelper();
            _schCnpjCpf.addEqualInteger("CnpjCpf", _tpCliente.CnpjCpf);
            // endregion

            // region Tratando a conexão com o banco de dados
            _sqh = new SQLiteHelper(ClienteEditarActivity.this);
            _sqh.open(false);
            // endregion

            // region Realizando a consulta no banco de dados
            _tpClienteFound = (tpCliente) (new dbCliente(_sqh)).getOne(_schCnpjCpf);
            // endregion

            // region Realizando as verificações necessárias
            if(_tpClienteFound != null && _metodoEdicao == _INSERT_VALUE)
            {
                if(_tpClienteFound.IdCliente > 0)
                {
                    _Out = true;
                }
            }

            if(_tpClienteFound != null && _metodoEdicao == _UPDATE_VALUE)
            {
                if(_tpClienteFound.IdCliente != _tpCliente.IdCliente)
                {
                    _Out = true;
                }
            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(
                    ClienteEditarActivity.this,
                    "Erro ao tentar validar se o cliente já está cadastrado",
                    e.getMessage()
            );

            finish();

        } finally {
            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }
        }
        // endregion

        // region Retornando o valor processado
        return _Out;
        // endregion

    }
    // endregion

}