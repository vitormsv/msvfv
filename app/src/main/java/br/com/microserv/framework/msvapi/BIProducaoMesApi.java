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

import br.com.microserv.framework.msvdal.dbBairro;
import br.com.microserv.framework.msvdto.tpBIProducaoMes;
import br.com.microserv.framework.msvdto.tpBairro;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class BIProducaoMesApi extends AsyncTask<Void, Void, Void> {


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
    private String _ano = null;
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

        tpBIProducaoMes _tp1 = new tpBIProducaoMes();
        _tp1._id = 1;
        _tp1.Mes = 0;
        _tp1.MesExtenso = "RESUMO GERAL";
        _tp1.DiasTrabalhados = 65;
        _tp1.PedidosEmitidos = 1132;
        _tp1.ValorTotalEmitido = 103890.36;
        _tp1.VendaValorTotal = 89550.80;
        _tp1.VendaQuantidadePedidos = 890;
        _tp1.VendaPercentual = 80;
        _tp1.BonificacaoValorTotal = 20630.60;
        _tp1.BonificacaoQuantidadePedidos = 19;
        _tp1.BonificacaoPercentual = 13;
        _tp1.TrocaValorTotal = 265.00;
        _tp1.TrocaQuantidadePedidos = 3;
        _tp1.TrocaPercentual = 2;
        _tp1.OutrosValorTotal = 0;
        _tp1.OutrosQuantidadePedidos = 0;
        _tp1.OutrosPercentual = 0;
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
            _url = "http://{ip}/api/bi/producaomes/{vendedor}/{ano}";
            _url = _url.replace("{ip}", _ip);
            _url = _url.replace("{vendedor}", _vendedor);
            _url = _url.replace("{ano}", _ano);
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

                // region Convertendo Json Object em tpBIProdutoMes Object
                tpBIProducaoMes _tpBIProducaoMes = new tpBIProducaoMes();
                _tpBIProducaoMes.fillByJsonObject(_jsonObject);
                // endregion

                // region Verificando a necessidade de instanciar a lista de objetos
                if (_lstOut == null) {
                    _lstOut = new ArrayList<tpInterface>();
                }
                // endregion

                // region Adicionando o registro à lista
                _lstOut.add(_tpBIProducaoMes);
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
    public BIProducaoMesApi(
            Context context,
            String ip,
            String vendedor,
            String ano,
            int indice,
            OnTaskCompleteListner onTaskCompleteListner
    ) {

        this._context = context;
        this._ip = ip;
        this._vendedor = vendedor;
        this._ano = ano;
        this._indice = indice;
        this._onTaskCompleteListner = onTaskCompleteListner;

    }
    // endregion

}
