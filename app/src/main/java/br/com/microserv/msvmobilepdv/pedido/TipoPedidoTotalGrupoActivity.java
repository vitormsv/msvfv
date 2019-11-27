package br.com.microserv.msvmobilepdv.pedido;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbTipoPedido;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.TipoPedidoTotalGrupoAdapter;

public class TipoPedidoTotalGrupoActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando constantes

    static final String _KEY_TP_EMPRESA = "tpEmpresa";

    // endregion

    // region Declarando variaveis para as views da activity

    // LienarLayout
    LinearLayout _llyTitulo = null;
    LinearLayout _llyItens = null;

    // ListView
    ListView _lvItens = null;

    // TextView
    TextView _txtRodape = null;
    TextView _txtQuantidade = null;
    TextView _txtValorTotal = null;
    // endregion


    // region Declarando as variáveis locais da activity

    // Objetos
    TipoPedidoTotalGrupoAdapter _adp = null;
    ArrayList<tpTipoPedido> _lstTipoPeddo = null;
    tpEmpresa _tpEmpresa = null;
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_pedido_total_grupo);
        // endregion

        // region Adicionando suporte a ActionBar
        ActionBar _ab = getSupportActionBar();
        _ab.setDisplayHomeAsUpEnabled(true);
        _ab.setElevation(0);
        // endregion


        // region Selecionando os parametros enviados através do Bundle
        Bundle _extras = getIntent().getExtras();

        if (_extras != null) {

            // region _KEY_TP_EMPRESA
            if (_extras.containsKey(_KEY_TP_EMPRESA)) {
                _tpEmpresa = (tpEmpresa) _extras.getSerializable(_KEY_TP_EMPRESA);
            } else {
                Toast.makeText(
                        TipoPedidoTotalGrupoActivity.this,
                        "O parâmetro _KEY_TP_EMPRESA não foi informado",
                        Toast.LENGTH_SHORT
                ).show();
            }
            // endregion

        }
        // endregion


        // region Invocando os métodos da interface
        this.bindElements();
        this.bindEvents();

        this.initialize();
        // endregion

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


    // region bindElements
    @Override
    public void bindElements() {

        // LienarLayout
        _llyTitulo = (LinearLayout) findViewById(R.id.llyTitulo);
        _llyItens = (LinearLayout) findViewById(R.id.llyItens);

        // ListView
        _lvItens = (ListView) findViewById(R.id.lvItens);

        // TextView
        _txtRodape = (TextView) findViewById(R.id.txtRodape);
        _txtQuantidade = (TextView) findViewById(R.id.txtQuantidade);
        _txtValorTotal = (TextView) findViewById(R.id.txtValorTotal);

    }
    // endregion


    // region bindEvents
    @Override
    public void bindEvents() {
    }
    // endregion


    // region initialize
    public void initialize() {

        _txtRodape.setText("REGISTROS: 0" );

        this.loadData();
        this.refreshData();

    }
    // endregion


    // region loadData
    public void loadData() {

        SQLiteHelper _sqh = null;
        dbTipoPedido _dbTipoPedido = null;


        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(getBaseContext());
            _sqh.open(false);
            // endregion

            // region Istânciando os objetos de acesso ao banco
            _dbTipoPedido = new dbTipoPedido(_sqh);
            _lstTipoPeddo = (ArrayList<tpTipoPedido>) _dbTipoPedido.getTotalPorTipoPedido(2);
            // endregion

        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    TipoPedidoTotalGrupoActivity.this,
                    "Ocorreram erros ao tentar buscar os totais por tipo de pedido",
                    e.getMessage()
            );

            finish();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region refreshList
    public void refreshData() {

        int _quantidade= 0;
        double _total = 0;

        if (_lstTipoPeddo != null) {

            for(tpTipoPedido _item : _lstTipoPeddo) {
                _quantidade += _item.PedidoQuantidade;
                _total += _item.ValorTotal;
            }

            _adp = new TipoPedidoTotalGrupoAdapter(
                    TipoPedidoTotalGrupoActivity.this,
                    _lstTipoPeddo
            );

            _lvItens.setAdapter(_adp);

            _txtRodape.setText("REGISTROS: " + _lstTipoPeddo.size() );
            _txtQuantidade.setText(String.valueOf(_quantidade));
            _txtValorTotal.setText(MSVUtil.doubleToText("R$", _total));

        } else {

            Toast.makeText(
                    TipoPedidoTotalGrupoActivity.this,
                    "Não existem totais por tipo de pedido no banco de dados",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }
    // endregion

}
