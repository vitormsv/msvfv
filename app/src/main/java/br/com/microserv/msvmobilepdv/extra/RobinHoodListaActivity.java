package br.com.microserv.msvmobilepdv.extra;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbRobinHood;
import br.com.microserv.framework.msvdto.tpRobinHood;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.RobinHoodListaAdapter;

public class RobinHoodListaActivity extends AppCompatActivity implements ActivityInterface {

    // region Declarando variaveis para as views da activity

    // LienarLayout
    LinearLayout _llyTitulo = null;
    LinearLayout _llyItens = null;

    // ListView
    ListView _lvItens = null;

    // TextView
    TextView _txtRodape = null;
    // endregion


    // region Declarando as variáveis locais da activity

    // Objetos
    RobinHoodListaAdapter _adp = null;
    ArrayList<tpRobinHood> _lstRobinHood = null;
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robin_hood_lista);
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
        this.refreshSincronizacao();

    }
    // endregion


    // region loadData
    public void loadData() {

        SQLiteHelper _sqh = null;
        dbRobinHood _dbRobinHood = null;


        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(getBaseContext());
            _sqh.open(false);
            // endregion

            // region Istânciando os objetos de acesso ao banco
            _dbRobinHood = new dbRobinHood(_sqh);
            _lstRobinHood = (ArrayList<tpRobinHood>) _dbRobinHood.getList(tpRobinHood.class);
            // endregion

        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    RobinHoodListaActivity.this,
                    "Ocorreram erros ao tentar buscar as informações do valor FLEX",
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


    // region refreshSincronizacao
    public void refreshSincronizacao() {

        if (_lstRobinHood != null) {
            _adp = new RobinHoodListaAdapter(
                    RobinHoodListaActivity.this,
                    _lstRobinHood
            );

            _lvItens.setAdapter(_adp);

            _txtRodape.setText("REGISTROS: " + _lstRobinHood.size() );

        } else {

            Toast.makeText(
                    RobinHoodListaActivity.this,
                    "Não existem informações de FLEX registradas neste dispositivo",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }
    // endregion

}
