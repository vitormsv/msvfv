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

import br.com.microserv.framework.msvdal.dbCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class CondicaoPagamentoApi extends AsyncTask<Void, Void, Void> {

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
    private String _url = "http://{ip}/api/condicaopagamento";
    private String _line = "";

    // int
    private int _indice = 0;
    private long _out = 0;

    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Obtendo dados do servidor e gravando na tabela CondicaoPagamento
        try {

            // region Inicializando objetos e variáveis
            _line = "";
            _sb = new StringBuilder();

            _out = 0;
            // endregion

            // region Conectando com o webservice e recuperando a string json
            URL url = new URL(_url.replace("{ip}", _ip));
            URLConnection urlConnection = url.openConnection();

            _br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while ((_line = _br.readLine()) != null) {
                _sb.append(_line + "\n");
            }

            _br.close();
            // endregion

            // region Trabalhando com o resultado obtido

            // iniciando a conexao com o banco de dados
            _sqh = new SQLiteHelper(_context);
            _sqh.open(true);

            // instânciando um objeto de banco para a tabela Linha
            dbCondicaoPagamento _dbCondicaoPagamento = new dbCondicaoPagamento(_sqh);
            _dbCondicaoPagamento.truncateTable();

            // obtendo o array de objetos json (em texto ainda)
            JSONArray _jsonArray = new JSONArray(_sb.toString());

            for (int i = 0; i < _jsonArray.length(); i++) {

                // obtendo um objeto json
                JSONObject _jsonObject = new JSONObject(_jsonArray.getString(i));

                // obtendo um objeto tpCondicaoPagamento
                tpCondicaoPagamento _tpCondicaoPagamento = new tpCondicaoPagamento();
                _tpCondicaoPagamento.fillByJsonObject(_jsonObject);

                // inserindo o objeto _tpEmpresa na tabela CondicaoPagamento
                _dbCondicaoPagamento.insert(_tpCondicaoPagamento);

                // atualizando a variável de retorno
                _out += 1;
            }

            // A conexão será encerrada no finally

            // endregion

        } catch (JSONException j) {
            Log.e("Erro", "Erro no parsing do JSON", j);
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


    // region onPostExecute
    @Override
    protected void onPostExecute(Void result) {
        _onTaskCompleteListner.onTaskComplete(_indice, eTaskCompleteStatus.SUCCESS, _out, null, null);
    }
    // endregion


    // region CondicaoPagamentoApi
    public CondicaoPagamentoApi(
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
