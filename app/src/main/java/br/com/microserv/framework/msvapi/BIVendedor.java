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
import java.util.ArrayList;

import br.com.microserv.framework.msvdto.tpBIAnoMovimento;
import br.com.microserv.framework.msvdto.tpBIVendedor;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class BIVendedor extends AsyncTask<Void, Void, Void> {


    // region Declarando variáveis locais da classe

    // Delegate
    private OnTaskCompleteListner _onTaskCompleteListner;

    // Objects
    private Context _context = null;
    private StringBuilder _sb = null;
    private BufferedReader _br = null;
    private ArrayList<tpInterface> _lstOut = null;

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

        _lstOut = new ArrayList<tpInterface>();

        tpBIVendedor _tp1 = new tpBIVendedor();
        _tp1._id = 1;
        _tp1.Codigo = "006993";
        _tp1.RazaoSocial = "RICARO LUIS BERETTA E FELIPIN";
        _lstOut.add(_tp1);

        /*
        // region Obtendo dados do servidor e gravando na tabela Bairro
        try {

            // region Inicializando objetos e variáveis
            _line = "";
            _sb = new StringBuilder();

            _out = 0;
            // endregion

            // region Montando a url de requisição para o webservice
            _url = "http://{ip}/api/bi/vendedor/{vendedor}";
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

            // obtendo o array de objetos json (em texto ainda)
            JSONArray _jsonArray = new JSONArray(_sb.toString());

            for (int i = 0; i < _jsonArray.length(); i++) {

                // region Convertendo um registro de String para Json Object
                JSONObject _jsonObject = new JSONObject(_jsonArray.getString(i));
                // endregion

                // region Convertendo Json Object em tpBIAnoMovimento Object
                tpBIVendedor _tpBIVendedor = new tpBIVendedor();
                _tpBIVendedor.fillByJsonObject(_jsonObject);
                // endregion

                // region Verificando a necessidade de instanciar a lista de objetos
                if (_lstOut == null) {
                    _lstOut = new ArrayList<tpInterface>();
                }
                // endregion

                // region Adicionando o registro à lista
                _lstOut.add(_tpBIVendedor);
                // endregion

                // region Atualizando a variável de retorno
                _out += 1;
                // endregion
            }

            // endregion

        } catch (JSONException j) {
            Log.e("Erro", "Erro no parsing do JSON", j);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        // endregion
        */

        return null;

    }
    // endregion


    // region onPostExecute
    @Override
    protected void onPostExecute(Void result) {
        _onTaskCompleteListner.onTaskComplete(_indice, eTaskCompleteStatus.SUCCESS, _out, null, _lstOut);
    }
    // endregion


    // region BIProducaoMesApi
    public BIVendedor(
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
