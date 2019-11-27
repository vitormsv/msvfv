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

import br.com.microserv.framework.msvdal.dbBairro;
import br.com.microserv.framework.msvdto.tpBairro;
import br.com.microserv.framework.msvdto.tpReceitaWs;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class ReceitaWSApi extends AsyncTask<Void, Void, Void> {


    // region Declarando variáveis locais da classe

    // Delegate
    private OnTaskCompleteListner _onTaskCompleteListner;

    // Objects
    private Context _context = null;
    private StringBuilder _sb = null;
    private BufferedReader _br = null;
    tpReceitaWs _tpOut = null;

    // String
    private String _cnpj = null;
    private String _url = "";
    private String _line = "";

    // int
    private long _out = 0;

    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Obtendo dados do servidor e gravando na tabela Bairro
        try {

            // region Inicializando objetos e variáveis
            _line = "";
            _sb = new StringBuilder();

            _out = 0;
            // endregion

            // region Montando a url de requisição para o webservice
            _url = "https://www.receitaws.com.br/v1/cnpj/{cnpj}";
            _url = _url.replace("{cnpj}", _cnpj);
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

            // obtendo o array de objetos json (em texto ainda)
            JSONObject _oRoot = new JSONObject(_sb.toString());

            // resgatando os valores
            if (_oRoot != null) {

                // region Instânciando o objeto de retorno
                _tpOut = new tpReceitaWs();
                // endregion

                // region Razao Social
                if (_oRoot.has("nome")) {
                    _tpOut.Nome = _oRoot.getString("nome");
                }
                // endregion

                // region Nome Fantasia
                if (_oRoot.has("fantasia")) {
                    _tpOut.Fantasia = _oRoot.getString("fantasia");
                }
                // endregion

                // region Email
                if (_oRoot.has("email")) {
                    _tpOut.Email = _oRoot.getString("email");
                }
                // endregion

                // region Telefone
                if (_oRoot.has("telefone")) {
                    _tpOut.Telefone = _oRoot.getString("telefone");
                }
                // endregion

                // region Logradouro
                if (_oRoot.has("logradouro")) {
                    _tpOut.Logradouro = _oRoot.getString("logradouro");
                }
                // endregion

                // region Numero
                if (_oRoot.has("numero")) {
                    _tpOut.Numero = _oRoot.getString("numero");
                }
                // endregion

                // region Complemento
                if (_oRoot.has("complemento")) {
                    _tpOut.Complemento = _oRoot.getString("complemento");
                }
                // endregion

                // region Bairro
                if (_oRoot.has("bairro")) {
                    _tpOut.Bairro = _oRoot.getString("bairro");
                }
                // endregion

                // region Municipio
                if (_oRoot.has("municipio")) {
                    _tpOut.Municipio = _oRoot.getString("municipio");
                }
                // endregion

                // region Uf
                if (_oRoot.has("uf")) {
                    _tpOut.Uf = _oRoot.getString("uf");
                }
                // endregion

                // region Cep
                if (_oRoot.has("cep")) {
                    _tpOut.Cep = _oRoot.getString("cep");
                }
                // endregion

            }


            /*
            for (int i = 0; i < _jsonArray.length(); i++) {

                // obtendo um objeto json
                JSONObject _jsonObject = new JSONObject(_jsonArray.getString(i));

                // obtendo um objeto tpBairro
                tpBairro _tpBairro = new tpBairro();
                _tpBairro.fillByJsonObject(_jsonObject);

                // inserindo o objeto _tpEmpresa na tabela Bairro
                _dbBairro.insert(_tpBairro);

                // atualizando a variável de retorno
                _out += 1;
            }
            */

            // endregion

            _out = 1;

        } catch (JSONException j) {
            Log.e("Erro", "Erro no parsing do JSON", j);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        // endregion

        return null;

    }
    // endregion


    // region onPostExecute
    @Override
    protected void onPostExecute(Void result) {
        _onTaskCompleteListner.onTaskComplete(0, eTaskCompleteStatus.SUCCESS, _out, _tpOut, null);
    }
    // endregion


    // region ReceitaWSApi
    public ReceitaWSApi(
            Context context,
            String cnpj,
            OnTaskCompleteListner onTaskCompleteListner
    ) {

        this._context = context;
        this._cnpj = cnpj;
        this._onTaskCompleteListner = onTaskCompleteListner;

    }
    // endregion

}
