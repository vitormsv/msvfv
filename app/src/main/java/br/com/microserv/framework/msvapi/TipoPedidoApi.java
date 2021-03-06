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

import br.com.microserv.framework.msvdal.dbTipoPedido;
import br.com.microserv.framework.msvdal.dbTipoPedidoCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpProdutoEmpresa;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvdto.tpTipoPedidoCondicaoPagamento;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class TipoPedidoApi extends AsyncTask<Void, Void, Void> {

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
    private String _url = "http://{ip}/api/tipopedido";
    private String _line = "";

    // int
    private int _indice = 0;
    private long _out = 0;

    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Obtendo dados do servidor e gravando na tabela PedidoTipo
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

            // instânciando um objeto de banco para a tabela TipoPedido
            dbTipoPedido _dbTipoPedido = new dbTipoPedido(_sqh);
            _dbTipoPedido.truncateTable();

            // instânciando um objeto de banco para vinculo do tipo de pedido e condição de pagamento
            dbTipoPedidoCondicaoPagamento _dbTipoPedidoCondicaoPagamento = new dbTipoPedidoCondicaoPagamento(_sqh);
            _dbTipoPedidoCondicaoPagamento.truncateTable();

            // obtendo o array de objetos json (em texto ainda)
            JSONArray _jsonArray = new JSONArray(_sb.toString());

            for (int i = 0; i < _jsonArray.length(); i++) {

                // obtendo um objeto json
                JSONObject _jsonObject = new JSONObject(_jsonArray.getString(i));

                // obtendo um objeto tpPedidoTipo
                tpTipoPedido _tpTipoPedido = new tpTipoPedido();
                _tpTipoPedido.fillByJsonObject(_jsonObject);

                // inserindo o objeto _tpEmpresa na tabela PedidoTipo
                _dbTipoPedido.insert(_tpTipoPedido);


                // region Verificando se existe o relacionamento com as condições de pagamento
                if (_jsonObject.has("CondicoesPagamento")) {

                    JSONArray _aCondicoesPagamento = _jsonObject.getJSONArray("CondicoesPagamento");

                    if (_aCondicoesPagamento != null && _aCondicoesPagamento.length() > 0) {

                        for (int j = 0; j < _aCondicoesPagamento.length(); j++) {

                            JSONObject _oCondicaoPagamento = new JSONObject(_aCondicoesPagamento.getString(j));

                            tpTipoPedidoCondicaoPagamento _tpTipoPedidoCondicaoPagamento = new tpTipoPedidoCondicaoPagamento();
                            _tpTipoPedidoCondicaoPagamento.fillByJsonObject(_oCondicaoPagamento);

                            _dbTipoPedidoCondicaoPagamento.insert(_tpTipoPedidoCondicaoPagamento);

                        }

                    }

                }
                // endregion


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


    // region PedidoTipoApi
    public TipoPedidoApi(
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
