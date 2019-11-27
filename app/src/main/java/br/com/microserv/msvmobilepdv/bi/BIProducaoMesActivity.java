package br.com.microserv.msvmobilepdv.bi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.microserv.framework.msvapi.BIVendedor;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;
import br.com.microserv.msvmobilepdv.R;

public class BIProducaoMesActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando os objetos de vinculo com a activity

    // LinearLayout
    LinearLayout _llyTitulo = null;
    LinearLayout _llyFiltroCnt = null;
    LinearLayout _llyVendedor = null;
    LinearLayout _llyAno = null;


    // TextView
    TextView _txtTitulo = null;
    TextView _txtVendedor = null;
    TextView _txtAno = null;

    // ListView
    ListView _livVendedorResumo = null;


    // TextView
    TextView _txtMesDiasTrabalhados = null;
    TextView _txtMesPedidosEmitidos = null;
    TextView _txtMesValorEmitido = null;
    TextView _txtVendaValorTotal = null;
    TextView _txtVendaPedidos = null;
    TextView _txtVendaPercentual = null;
    TextView _txtBonificacaoValorTotal = null;
    TextView _txtBonificacaoPedidos = null;
    TextView _txtBonificacaoPercentual = null;
    TextView _txtTrocaValorTotal = null;
    TextView _txtTrocaPedidos = null;
    TextView _txtTrocaPercentual = null;
    TextView _txtOutrosValorTotal = null;
    TextView _txtOutrosPedidos = null;
    TextView _txtOutrosPercentual = null;

    // endregion


    // region Method onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bi_producao_mes);
    }
    // endregion


    // region Method bindElements
    @Override
    public void bindElements() {

        // LinearLayout
        _llyTitulo = (LinearLayout) findViewById(R.id.llyTitulo);
        _llyFiltroCnt = (LinearLayout) findViewById(R.id.llyFiltroCnt);
        _llyVendedor = (LinearLayout) findViewById(R.id.llyVendedor);
        _llyAno = (LinearLayout) findViewById(R.id.llyAno);


        // TextView
        _txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        _txtVendedor = (TextView) findViewById(R.id.txtVendedor);
        _txtAno = (TextView) findViewById(R.id.txtAno);

        // ListView
        _livVendedorResumo = (ListView) findViewById(R.id.livVendedorResumo);

    }
    // endregion


    // region Method bindEvents
    @Override
    public void bindEvents() {

    }
    // endregion


    // region Method initialize
    @Override
    public void initialize() {

        new BIVendedor(
                BIProducaoMesActivity.this,
                "",
                "",
                0,
                _onVendedorTaskCompleteListner
        );

    }
    // endregion


    // region Declarando os eventos da activity
    private OnTaskCompleteListner _onVendedorTaskCompleteListner = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

        }
    };
    // endregion


    // region Declarando os eventos da activity
    private OnTaskCompleteListner _onAnoMovimentoTaskCompleteListner = new OnTaskCompleteListner() {
        @Override
        public void onTaskComplete(int index, eTaskCompleteStatus status, long out, tpInterface tpOut, ArrayList<tpInterface> lstOut) {

        }
    };
    // endregion
}
