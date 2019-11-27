package br.com.microserv.framework.msvapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbFinanceiro;
import br.com.microserv.framework.msvdto.tpFinanceiro;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class FinanceiroApi extends AsyncTask<Void, Void, Void> {

    // region Declarando variáveis locais da classe

    // Delegate
    private OnTaskCompleteListner _onTaskCompleteListner;

    // Objects
    private Context _context = null;
    private StringBuilder _sb = null;
    private BufferedReader _br = null;
    private SQLiteHelper _sqh = null;

    // String
    private String _ip = null;
    private String _vendedor = null;
    private String _url = "http://{ip}/api/cliente/financeiro/{idcliente}";
    private String _line = "";

    // int
    private int _indice = 0;
    private long _out = 0;

    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Obtendo dados do servidor e gravando na tabela Financeiro
        try {

            // region Inicializando objetos e variáveis
            _out = 0;
            // endregion

            // region Abrindo a conexao com o banco de dados
            _sqh = new SQLiteHelper(_context);
            _sqh.open(true);
            // endregion

            // region Instânciando o objeto DB da tabela Financeiro
            dbFinanceiro _dbFinanceiro = new dbFinanceiro(_sqh);
            _dbFinanceiro.truncateTable();
            // endregion

            // region Buscando todos os identificadores de cliente
            List<Integer> _lstCliente = (new dbCliente(_sqh)).getListOfSouceId(null);
            // endregion


            // region Executar enquanto existir cliente
            for (int _id : _lstCliente) {

                // region Inicializar variáveis
                _sb = new StringBuilder();
                _line = "";
                // endregion

                // region Conectando com o webservice e recuperando a string json
                _url = "http://{ip}/api/cliente/financeiro/{idcliente}";
                _url = _url.replace("{ip}", _ip);
                _url = _url.replace("{idcliente}", String.valueOf(_id));

                URL url = new URL(_url);
                URLConnection urlConnection = url.openConnection();

                _br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((_line = _br.readLine()) != null) {
                    _sb.append(_line + "\n");
                }

                _br.close();
                // endregion

                // region Trabalhando com o resultado obtido

                // obtendo o array de objetos json (em texto ainda)
                JSONArray _jsonArray = new JSONArray(_sb.toString());

                for (int i = 0; i < _jsonArray.length(); i++) {

                    // obtendo um objeto json
                    JSONObject _jsonObject = new JSONObject(_jsonArray.getString(i));

                    // obtendo um objeto tpFinanceiro
                    tpFinanceiro _tpFinanceiro = new tpFinanceiro();
                    _tpFinanceiro.fillByJsonObject(_jsonObject);

                    // inserindo o objeto _tpFinanceiro em sua respectiva tabela
                    _dbFinanceiro.insert(_tpFinanceiro);

                }
                // endregion

                // atualizando a variável de retorno
                _out += 1;

                publishProgress();

            }
            // endregion

            // A conexão será encerrada no finally

        } catch (JSONException j) {
            Log.e("FinanceiroApi", "Erro no parsing do JSON", j);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_sqh.isOpen()) {
                _sqh.close();
            }
        }
        // endregion

        return null;

    }
    // endregion


    // region onProgressUpdate
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        _onTaskCompleteListner.onTaskComplete(_indice, eTaskCompleteStatus.REFRESH, _out, null, null);
    }
    // endregion


    // region onPostExecute
    @Override
    protected void onPostExecute(Void result) {
        _onTaskCompleteListner.onTaskComplete(_indice, eTaskCompleteStatus.SUCCESS, _out, null, null);
    }
    // endregion


    // region ProdutoApi
    public FinanceiroApi(
            Context context,
            String ip,
            String vendedor,
            int indice,
            OnTaskCompleteListner onTaskCompleteListner
    ) {

        this._context = context;
        this._ip = ip;
        this._vendedor = vendedor;
        this._indice = indice;
        this._onTaskCompleteListner = onTaskCompleteListner;

    }
    // endregion

}
