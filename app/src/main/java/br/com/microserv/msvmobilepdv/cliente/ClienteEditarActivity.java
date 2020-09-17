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
import br.com.microserv.framework.msvdal.dbCondicaoPagamento;
import br.com.microserv.framework.msvdal.dbEmpresa;
import br.com.microserv.framework.msvdal.dbRegiao;
import br.com.microserv.framework.msvdal.dbTabelaPreco;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
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
import br.com.microserv.msvmobilepdv.adapter.CondicaoPagamentoDialogSearchAdapter;
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
    // endregion


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
    LinearLayout _pnlCondicaoPagamento = null;
    LinearLayout _pnlTabelaPreco = null;

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
    TextView _txtCondicaoPagamento = null;
    TextView _txtTabelaPreco = null;

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
    CondicaoPagamentoDialogSearchAdapter _adpCondicaoPagamento = null;
    TabelaPrecoDialogSearchAdapter _adpTabelaPreco = null;
    tpEmpresa _tpEmpresa = null;
    tpCliente _tpCliente = null;
    tpReceitaWs _tpReceitaWs = null;

    // Dialogs
    ProgressDialog _wait = null;

    // ArrayList
    ArrayList<tpRegiao> _lstRegiao = null;
    ArrayList<tpCidade> _lstCidade = null;
    ArrayList<tpEmpresa> _lstEmpresa = null;
    ArrayList<tpCondicaoPagamento> _lstCondicaoPagamento = null;
    ArrayList<tpTabelaPreco> _lstTabelaPreco = null;

    // String
    String _sourceActivity = "";

    // int
    int _metodoEdicao = 0;
    int _iCidade = 0;
    int _iRegiao = 0;
    int _iCondicaoPagamento = 0;
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
                Toast.makeText(ClienteEditarActivity.this, "O parâmetro _KEY_METODO_EDICAO não foi informado", Toast.LENGTH_SHORT).show();
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
        _pnlCondicaoPagamento = (LinearLayout) findViewById(R.id.pnlCondicaoPagamento);
        _pnlTabelaPreco = (LinearLayout) findViewById(R.id.pnlTabelaPreco);

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
        _txtCondicaoPagamento = (TextView) findViewById(R.id.txtCondicaoPagamentoDescricao);
        _txtTabelaPreco = (TextView) findViewById(R.id.txtTabelaPrecoDescricao);

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

                MSVMsgBox.getStringValue(ClienteEditarActivity.this, "RAZÃO SOCIAL", "Informe a razão social do cliente", _txtRazaoSocial.getText().toString(),
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

                MSVMsgBox.getStringValue(ClienteEditarActivity.this, "NOME FANTASIA", "Informe o nome fantasia do cliente", _txtNomeFantasia.getText().toString(),
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

                MSVMsgBox.getStringNumberValue(ClienteEditarActivity.this, "CNPJ ou CPF", "Informe CNPJ ou o CPF do cliente", _txtCnpjCpf.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    _tpCliente.CnpjCpf = MSVUtil.onlyNumber(value.trim().toUpperCase());

                                    if (_tpCliente.CnpjCpf.length() == 11) {
                                        if (MSVUtil.isCpfValid(_tpCliente.CnpjCpf) == false) {
                                            MSVMsgBox.showMsgBoxWarning(ClienteEditarActivity.this, "CPF inválido", "O número do CPF é inválido");
                                            _tpCliente.CnpjCpf = "";
                                        }
                                    }

                                    if (_tpCliente.CnpjCpf.length() == 14) {
                                        if (MSVUtil.isCnpjValid(_tpCliente.CnpjCpf) == false) {
                                            MSVMsgBox.showMsgBoxWarning(ClienteEditarActivity.this, "CNPJ inválido", "O número do CNPJ é inválido");
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

                MSVMsgBox.getStringValue(ClienteEditarActivity.this, "IE ou RG", "Informe a inscrição estadual ou o RG do cliente", _txtIeRg.getText().toString(),
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

                MSVMsgBox.getPhoneValue(ClienteEditarActivity.this, "TELEFONE FIXO", "Informe o telefone fixo do cliente (informar somente os números com o DDD)",
                        _txtTelefoneFixo.getText().toString(),
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                if (isOk) {
                                    _tpCliente.TelefoneFixo = MSVUtil.onlyNumber(value.trim().toLowerCase());

                                    if (_tpCliente.TelefoneFixo.length() != 10) {
                                        _tpCliente.TelefoneFixo = "";

                                        Toast.makeText(ClienteEditarActivity.this, "O telefone fixo informado não contém os 10 caracteres numéricos necessários",
                                                Toast.LENGTH_SHORT).show();
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

                MSVMsgBox.getPhoneValue(ClienteEditarActivity.this, "TELEFONE CELULAR", "Informe o telefone celular do cliente (informar somente os números com o DDD)",
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
                View _v = _inflater.inflate(R.layout.dialog_personalizado_lista, null);

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

        // region Condicao Pagamento (lookup)
        _pnlCondicaoPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _iAux = 0;

                // region Montando o adaptador se necessário
                if (_adpCondicaoPagamento == null) {
                    _adpCondicaoPagamento = new CondicaoPagamentoDialogSearchAdapter(ClienteEditarActivity.this, _lstCondicaoPagamento);
                }
                // endregion

                // region Abrindo a janela para escolha da condicao de pagamento
                MSVMsgBox.getValueFromList(ClienteEditarActivity.this, _lstCondicaoPagamento.get(_iCondicaoPagamento).Descricao, _adpCondicaoPagamento,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iCondicaoPagamento = _iAux;
                                _tpCliente.IdCondicaoPagamentoPadrao = _lstCondicaoPagamento.get(_iCondicaoPagamento).IdCondicaoPagamento;
                                _tpCliente.CondicaoPagamentoPadrao = _lstCondicaoPagamento.get(_iCondicaoPagamento);

                                refreshCliente();
                            }
                        }
                );
                // endregion
            }
        });
        // endregion

        // region Tabela Preco (lookup)
        _pnlTabelaPreco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _iAux = 0;

                // region Montando o adaptador se necessário
                if (_adpTabelaPreco == null) {
                    _adpTabelaPreco = new TabelaPrecoDialogSearchAdapter(ClienteEditarActivity.this, _lstTabelaPreco);
                }
                // endregion

                // region Abrindo a janela para escolha da condicao de pagamento
                MSVMsgBox.getValueFromList(ClienteEditarActivity.this, _lstTabelaPreco.get(_iTabelaPreco).Descricao, _adpTabelaPreco,
                        new OnSelectedItem() {
                            @Override
                            public void onSelectedItem(int position, tpBase tp) {
                                _iAux = position;
                            }
                        },
                        new OnCloseDialog() {
                            @Override
                            public void onCloseDialog(boolean isOk, String value) {
                                _iTabelaPreco = _iAux;
                                _tpCliente.IdTabelaPreco = _lstTabelaPreco.get(_iTabelaPreco).IdTabelaPreco;
                                _tpCliente.TabelaPrecoEmpresa = _lstTabelaPreco.get(_iTabelaPreco);
                                refreshCliente();
                            }
                        }
                );
                // endregion
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
        this.loadCondicaoPagamento();
        this.loadTabelaPreco();
        // endregion

        // region Atualizando o layout da activity
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
        _txtCondicaoPagamento.setText("");
        _txtTabelaPreco.setText("");
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


    // region loadCondicaoPagamento
    private void loadCondicaoPagamento() {

        _iCondicaoPagamento = -1;

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
        _sqh.open(false);

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            dbCondicaoPagamento _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);
            _lstCondicaoPagamento = (ArrayList<tpCondicaoPagamento>) _dbCondicaoPagamento.getList(tpCondicaoPagamento.class, _sch);

            if ((_lstCondicaoPagamento != null) && _lstCondicaoPagamento.size() > 0) {
                _iCondicaoPagamento = 0;
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


    // region loadTabelaPreco
    private void loadTabelaPreco() {

        _iTabelaPreco = -1;

        SQLiteHelper _sqh = new SQLiteHelper(getBaseContext());
        _sqh.open(false);

        try {

            SQLClauseHelper _sch = new SQLClauseHelper();
            _sch.addOrderBy("Descricao", eSQLSortType.ASC);

            dbTabelaPreco _dbTabelaPreco = new dbTabelaPreco(_sqh);
            _lstTabelaPreco = (ArrayList<tpTabelaPreco>) _dbTabelaPreco.getList(tpTabelaPreco.class, _sch);

            if ((_lstTabelaPreco != null) && _lstTabelaPreco.size() > 0) {
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
                    Toast.makeText(ClienteEditarActivity.this, "Não foi possível localizar o cliente", Toast.LENGTH_SHORT).show();
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

                // region Recuperando o objeto tabela preco vinculado
                dbTabelaPreco _dbTabelaPreco = new dbTabelaPreco(_sqh);
                _tpCliente.TabelaPrecoEmpresa = (tpTabelaPreco) _dbTabelaPreco.getBySourceId(_tpCliente.IdTabelaPreco);
                // endregion

                // region Recuperando o objeto Codicao de Pagamento vinculado
                dbCondicaoPagamento _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);
                _tpCliente.CondicaoPagamentoPadrao = (tpCondicaoPagamento) _dbCondicaoPagamento.getBySourceId(_tpCliente.IdCondicaoPagamentoPadrao);
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

            // region Atualizando as informações da tabela preco
            _txtTabelaPreco.setText("");

            if (_tpCliente.TabelaPrecoEmpresa != null) {
                _txtTabelaPreco.setText(_tpCliente.TabelaPrecoEmpresa.Descricao);
            }
            // endregion

            // region Atualizando as informações de condicao pagamento
            _txtCondicaoPagamento.setText("");

            if (_tpCliente.CondicaoPagamentoPadrao != null) {
                _txtCondicaoPagamento.setText(_tpCliente.CondicaoPagamentoPadrao.Descricao);
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


    // region cancelar
    private void cancelar() {

        MSVMsgBox.showMsgBoxQuestion(ClienteEditarActivity.this, "Deseja realmente cancelar a operação em andamento ?",
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

            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "CNPJ/CPF", "O campo de documento (CNPJ/CPF) deve ser preenchido");

            return;

        } else {

            String _documento = MSVUtil.onlyNumber(_tpCliente.CnpjCpf);

            // aqui deverá ser validado o número do CPF
            if (_documento.length() == 11) {
              /*  if (MSVUtil.isCpfValid(_documento) == false) {
                    MSVMsgBox.showMsgBoxWarning(ClienteEditarActivity.this, "CPF", "O valor do CPF informado não é válido"
                    );
                    return;
                }    */
            }

            // aqui deverá ser validado o número do CNPJ
            if (_documento.length() == 14) {
                /*if (MSVUtil.isCnpjValid(_documento) == false) {
                    MSVMsgBox.showMsgBoxWarning(ClienteEditarActivity.this, "CNPJ", "O valor do CNPJ informado não é válido");
                    return;
                }*/
            }
        }
        // endregion

        // region Validando o campo IeRg
        if (MSVUtil.isNullOrEmpty(_tpCliente.IeRg)) {

            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "IE/RG", "O campo IE/RG deve ser preenchido");
            return;
        }
        // endregion

        // region Verificando se já existe cliente com o mesmo Cnpj ou Cpf
        if (this.ValidarCnpjCpfJaCadastrado(_tpCliente)) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "CNPJ/CPF", "Já existe um cliente com o mesmo CNPJ/CPF");
            return;
        }
        // endregion

        // region Validando o campo de Telefone
        if (MSVUtil.isNullOrEmpty(_tpCliente.TelefoneFixo) && MSVUtil.isNullOrEmpty(_tpCliente.TelefoneCelular)) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "Telefone Fixo/Celular", "Ao menos um telefone deve ser preenchido (fixo/celular)");
            return;
        }
        // endregion

        // region Validando o campo Logradouro Tipo
        if (MSVUtil.isNullOrEmpty(_tpCliente.LogradouroTipo)) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "Tipo de Logradouro", "O campo tipo de logradouro deve ser preenchido");
            return;
        }
        // endregion

        // region Validando o campo Logradouro Nome
        if (MSVUtil.isNullOrEmpty(_tpCliente.LogradouroNome)) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "Nome do Logradouro", "O campo nome do logradouro deve ser preenchido");
            return;
        }
        // endregion

        // region Validando o campo Logradouro Numero
        if (MSVUtil.isNullOrEmpty(_tpCliente.LogradouroNumero)) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "Número do Logradouro", "O campo número do logradouro deve ser preenchido");
            return;
        }
        // endregion

        // region Validando o campo Bairro
        if (MSVUtil.isNullOrEmpty(_tpCliente.BairroNome)) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "Bairro", "O campo bairro deve ser preenchido");
            return;
        }
        // endregion

        // region Validando o campo Cidade
        if (_tpCliente.IdCidade == 0) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "Cidade", "O campo cidade deve ser preenchido");
            return;
        }
        // endregion

        // region Validando o campo CEP
        if (MSVUtil.isNullOrEmpty(_tpCliente.Cep)) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "CEP", "O campo CEP deve ser preenchido");
            return;
        }
        // endregion

        // region Validando o campo Regiao
        if (_tpCliente.IdRegiao == 0) {
            MSVMsgBox.showMsgBoxInfo(ClienteEditarActivity.this, "Região", "O campo região deve ser preenchido");
            return;
        }
        // endregion

        // region Verificando se o usuário quer realmente efetivar a operação em andamento
        MSVMsgBox.showMsgBoxQuestion(ClienteEditarActivity.this, "Deseja realmente salvar os dados da operação em andamento ?",
                new OnCloseDialog() {
                    @Override
                    public void onCloseDialog(boolean isOk, String value) {

                        if (isOk) {

                            SQLiteHelper _sqh = null;
                            dbCliente _dbCliente = null;

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
        // endregions
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
    private boolean ValidarCnpjCpfJaCadastrado(tpCliente _tpCliente) {

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
            if (_tpClienteFound != null && _metodoEdicao == _INSERT_VALUE) {
                if (_tpClienteFound.IdCliente > 0) {
                    _Out = true;
                }
            }

            if (_tpClienteFound != null && _metodoEdicao == _UPDATE_VALUE) {
                if (_tpClienteFound.IdCliente != _tpCliente.IdCliente) {
                    _Out = true;
                }
            }
            // endregion

        } catch (Exception e) {

            MSVMsgBox.showMsgBoxError(ClienteEditarActivity.this, "Erro ao tentar validar se o cliente já está cadastrado", e.getMessage());
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