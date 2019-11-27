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

import br.com.microserv.framework.msvdal.dbSincronizacao;
import br.com.microserv.framework.msvdto.tpSincronizacao;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.ActivityInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.msvmobilepdv.R;
import br.com.microserv.msvmobilepdv.adapter.SincronizacaoListaAdapter;

public class SincronizacaoListaActivity extends AppCompatActivity implements ActivityInterface {

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
    SincronizacaoListaAdapter _adp = null;
    ArrayList<tpSincronizacao> _lstSincronizacao = null;
    // endregion


    // region onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // region Inflando o layout da tela
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacao_lista);
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

        this.loadSincronizacao();
        this.refreshSincronizacao();

    }
    // endregion


    // region loadSincronizacao
    public void loadSincronizacao() {

        SQLiteHelper _sqh = null;
        dbSincronizacao _dbSincronizacao = null;


        try {

            // region Abrindo a conexão com o banco de dados
            _sqh = new SQLiteHelper(getBaseContext());
            _sqh.open(false);
            // endregion

            // region Istânciando os objetos de acesso ao banco
            _dbSincronizacao = new dbSincronizacao(_sqh);
            _lstSincronizacao = (ArrayList<tpSincronizacao>) _dbSincronizacao.getTop20();
            // endregion

        } catch (Exception e) {
            MSVMsgBox.showMsgBoxError(
                    SincronizacaoListaActivity.this,
                    "Ocorreram erros ao tentar buscar as sincronizações realizadas pelo usuário",
                    e.getMessage()
            );
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }

    }
    // endregion


    // region refreshSincronizacao
    public void refreshSincronizacao() {

        if (_lstSincronizacao != null) {
            _adp = new SincronizacaoListaAdapter(
                    SincronizacaoListaActivity.this,
                    _lstSincronizacao
            );

            _lvItens.setAdapter(_adp);

            _txtRodape.setText("REGISTROS: " + _lstSincronizacao.size() );

        } else {

            Toast.makeText(
                    SincronizacaoListaActivity.this,
                    "Não existem informações de sincronização registradas neste dispositivo",
                    Toast.LENGTH_SHORT
            ).show();

        }

    }
    // endregion

}
