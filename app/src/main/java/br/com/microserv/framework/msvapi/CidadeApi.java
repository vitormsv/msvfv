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

import br.com.microserv.framework.msvdal.dbCidade;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class CidadeApi extends AsyncTask<Void, Void, Void> {
    
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
    private String _url = "";
    private String _line = "";

    // int
    private int _indice = 0;
    private long _out = 0;

    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Obtendo dados do servidor e gravando na tabela Cidade
        try {

            // region Inicializando objetos e variáveis
            _line = "";
            _sb = new StringBuilder();

            _out = 0;
            // endregion

            // region Montando a url de requisição para o webservice
            _url = "http://{ip}/api/cliente/cidade/{vendedor}";
            _url = _url.replace("{ip}", _ip);
            _url = _url.replace("{vendedor}", _vendedor);
            // endregion

            // region Conectando com o webservice e recuperando a string json
            URL url = new URL(_url);
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
            dbCidade _dbCidade = new dbCidade(_sqh);
            _dbCidade.truncateTable();

            // obtendo o array de objetos json (em texto ainda)
            JSONArray _jsonArray = new JSONArray(_sb.toString());

            for (int i = 0; i < _jsonArray.length(); i++) {

                // obtendo um objeto json
                JSONObject _jsonObject = new JSONObject(_jsonArray.getString(i));

                // obtendo um objeto tpCidade
                tpCidade _tpCidade = new tpCidade();
                _tpCidade.fillByJsonObject(_jsonObject);

                // inserindo o objeto _tpEmpresa na tabela Cidade
                _dbCidade.insert(_tpCidade);

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


    // region CidadeApi
    public CidadeApi(
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
