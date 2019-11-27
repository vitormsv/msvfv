package br.com.microserv.msvmobilepdv.pedido;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.PedidoEnviarResultadoAdapter;

public class PedidoEnviarResultadoActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    // Key
    static final String _KEY_METODO_EDICAO = "MetodoEdicao";
    static final String _KEY_SOURCE_ACTIVITY = "SourceActivity";
    static final String _KEY_TP_EMPRESA = "tpEmpresa";
    static final String _KEY_LST_PEDIDO_MOBILE = "lstPedidoMobile";

    // Value
    static final int _INSERT_VALUE = 0;
    static final int _UPDATE_VALUE = 1;
    static final int _LOOKUP_VALUE = 3;
    static final String _PEDIDO_ENVIAR_RESULTADO_VALUE = "PedidoEnviarResultadoActivity";

    // endregion


    // region Declarando variáveis para o bind dos elementos gráficos

    // region TITULO

    // LinearLayout
    LinearLayout _pnlTituloCnt = null;

    // TextView
    TextView _txtTitulo = null;

    // endregion

    // region TOTALIZADORES

    // TextView
    TextView _txtEnviadoQtd = null;
    TextView _txtEnviadoLinha1 = null;
    TextView _txtEnviadoLinha2 = null;
    TextView _txtSucessoQtd = null;
    TextView _txtSucessoLinha1 = null;
    TextView _txtSucessoLinha2 = null;
    TextView _txtErroQtd = null;
    TextView _txtErroLinha1 = null;
    TextView _txtErroLinha2 = null;

    // endregion

    // region RESULTADO

    // LinearLayout
    LinearLayout _pnlResultadoCnt = null;

    // ListView
    ListView _livResultado = null;

    // endregion

    // endregion


    // region Declarando variáveis locais

    // ObjectModels
    tpEmpresa _tpEmpresa = null;

    // ObjectList
    ArrayList<tpPedidoMobile> _lstPedidoMobile = null;

    // ObjectAdapter
    PedidoEnviarResultadoAdapter _adpSincronizacaoResultado = null;

    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacao_resultado);
        // endregion

        // region Adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        // endregion

        // region Selecionando os parametros enviados através do Bundle
        Bundle _extras = getIntent().getExtras();

        if (_extras != null) {

            // region _KEY_TP_EMPRESA
            if (_extras.containsKey(_KEY_TP_EMPRESA)) {
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);
            } else {
                Toast.makeText(
                        PedidoEnviarResultadoActivity.this,
                        "O parâmetro _KEY_TP_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

            // region _KEY_LST_PEDIDO_MOBILE
            if (_extras.containsKey(_KEY_LST_PEDIDO_MOBILE)) {
                _lstPedidoMobile = (ArrayList<tpPedidoMobile>) _extras.getSerializable(_KEY_LST_PEDIDO_MOBILE);
            } else {
                Toast.makeText(
                        PedidoEnviarResultadoActivity.this,
                        "O parâmetro _KEY_LST_PEDIDO_MOBILE não foi informado",
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

        //getMenuInflater().inflate(R.menu.menu_pedido, menu);
        return true;

    }
    // endregion


    // region onOptionItemSelected
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


    // region onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // region Invocando o metodo do objeto ancestral
        super.onActivityResult(requestCode, resultCode, data);
        // endregion

    }
    //endregion


    // region onBackPressed
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
    // endregion


    // region bindElements
    @Override
    public void bindElements() {

        // region TITULO

        // LinearLayout
        _pnlTituloCnt = (LinearLayout) findViewById(R.id.pnlTituloCnt);

        // TextView
        _txtTitulo = (TextView) findViewById(R.id.txtTitulo);

        // endregion

        // region TOTALIZADORES

        // TextView
        _txtEnviadoQtd = (TextView) findViewById(R.id.txtEnviadoQtd);
        _txtEnviadoLinha1 = (TextView) findViewById(R.id.txtEnviadoLinha1);
        _txtEnviadoLinha2 = (TextView) findViewById(R.id.txtEnviadoLinha2);
        _txtSucessoQtd = (TextView) findViewById(R.id.txtSucessoQtd);
        _txtSucessoLinha1 = (TextView) findViewById(R.id.txtSucessoLinha1);
        _txtSucessoLinha2 = (TextView) findViewById(R.id.txtSucessoLinha2);
        _txtErroQtd = (TextView) findViewById(R.id.txtErroQtd);
        _txtErroLinha1 = (TextView) findViewById(R.id.txtErroLinha1);
        _txtErroLinha2 = (TextView) findViewById(R.id.txtErroLinha2);

        // endregion

        // region RESULTADO

        // LinearLayout
        LinearLayout _pnlResultadoCnt = null;

        // ListView
        ListView _livResultado = null;

        // endregion

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {

    }
    // endregion


    // region Initialize
    public void initialize() {

    }
    // endregion


    // region refreshTotalizadores
    private void refreshTotalizadores() {

        // region Declarando variáveis locais
        int _envioQtd = 0;
        int _sucessoQtd = 0;
        int _erroQtd = 0;
        // endregion

        // region Processando as quantidade de envido, sucesso e erro
        for (tpPedidoMobile _item : _lstPedidoMobile) {

            _envioQtd += 1;

            if (_item.EhSincronizado == 1) {
                _sucessoQtd += 1;
            } else {
                _erroQtd += 1;
            }

        }
        // endregion

        // region Impriminto o resultado nos respectivos totalizadores

        // region ENVIADO
        _txtEnviadoQtd.setText(String.valueOf(_envioQtd));
        _txtEnviadoLinha1.setText("pedido");
        _txtEnviadoLinha2.setText("enviado");

        if (_envioQtd > 1) {
            _txtEnviadoLinha1.setText("pedidos");
            _txtEnviadoLinha2.setText("enviados");
        }
        // endregion

        // region SUCESSO
        _txtSucessoQtd.setText(String.valueOf(_sucessoQtd));
        _txtSucessoLinha1.setText("enviado");
        _txtSucessoLinha2.setText("com sucesso");

        if (_sucessoQtd > 1) {
            _txtSucessoLinha1.setText("enviados");
            _txtSucessoLinha2.setText("com sucesso");
        }
        // endregion

        // region ERRO
        _txtErroQtd.setText(String.valueOf(_erroQtd));
        _txtErroLinha1.setText("erro");
        _txtErroLinha2.setText("ocorrido");

        if (_erroQtd > 1) {
            _txtErroLinha1.setText("erros");
            _txtErroLinha2.setText("ocorridos");
        }
        // endregion

        // endregion

    }
    // endregion


    // region regreshLista
    private void refreshLista() {

        // region Criando o adaptador para a lista
        if (_adpSincronizacaoResultado == null) {
            _adpSincronizacaoResultado = new PedidoEnviarResultadoAdapter(
                    PedidoEnviarResultadoActivity.this,
                    _lstPedidoMobile
            );
        }
        // endregion

        // region Aplicando o adaptador a lista
        _livResultado.setAdapter(_adpSincronizacaoResultado);
        // endregion

    }
    // endregion

}
