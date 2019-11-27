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

import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdal.dbPedidoMobile;
import br.com.microserv.framework.msvdal.dbPedidoMobileItem;
import br.com.microserv.framework.msvdal.dbProduto;
import br.com.microserv.framework.msvdal.dbProdutoEmpresa;
import br.com.microserv.framework.msvdal.dbTabelaPrecoProduto;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpProdutoEmpresa;
import br.com.microserv.framework.msvdto.tpTabelaPrecoProduto;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class ProdutoApi extends AsyncTask<Void, Void, Void> {

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
    private int _aux = 0;
    private int _top = 0;

    // long
    private long _last = 0;
    private long _out = 0;


    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Obtendo dados do servidor e gravando na tabela Produto
        try {

            // region Inicializando objetos e variáveis
            _aux = 0;
            _out = 0;
            _last = 0;
            _top = 0;
            // endregion

            // region Abrindo a conexao com o banco de dados
            _sqh = new SQLiteHelper(_context);
            _sqh.open(true);
            // endregion

            // region Instânciando o objeto da tabela Produto
            dbProduto _dbProduto = new dbProduto(_sqh);
            _dbProduto.truncateTable();
            // endregion

            // region Instânciando o objeto da tabela produto X empresa
            dbProdutoEmpresa _dbProdutoEmpresa = new dbProdutoEmpresa(_sqh);
            _dbProdutoEmpresa.truncateTable();
            // endregion

            // region Instânciando o objeto dos preços dos produtos
            dbTabelaPrecoProduto _dbTabelaPrecoProduto = new dbTabelaPrecoProduto(_sqh);
            _dbTabelaPrecoProduto.truncateTable();
            // endregion

            // region Instânciando o objeto dos itens de pedido
            dbPedidoMobileItem _dbPedidoMobileItem = new dbPedidoMobileItem(_sqh);
            _dbPedidoMobileItem.truncateTable();
            // endregion

            // region Instânciando o objeto dos pedidos
            dbPedidoMobile _dbPedidoMobile = new dbPedidoMobile(_sqh);
            _dbPedidoMobile.truncateTable();
            // region

            // region Executar enquanto o retorno traga 100 registros
            do {

                // region Inicializar variáveis
                _sb = new StringBuilder();

                _line = "";
                _aux = 0;
                _top = this.getQuantidadeRegistrosProduto();
                // endregion

                // region Conectando com o webservice e recuperando a string json
                _url = "http://{ip}/api/produto/top/{top}/{last}";
                _url = _url.replace("{ip}", _ip);
                _url = _url.replace("{top}", String.valueOf(_top));
                _url = _url.replace("{last}", String.valueOf(_last));

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

                    // obtendo um objeto tpProduto
                    tpProduto _tpProduto = new tpProduto();
                    _tpProduto.fillByJsonObject(_jsonObject);

                    // inserindo o objeto _tpEmpresa na tabela Produto
                    _dbProduto.insert(_tpProduto);


                    // region Inserindo os empresas
                    if (_jsonObject.has("Empresas")) {

                        JSONArray _aEmpresas = _jsonObject.getJSONArray("Empresas");

                        if (_aEmpresas != null && _aEmpresas.length() > 0) {

                            for (int j = 0; j < _aEmpresas.length(); j++) {

                                JSONObject _oEmpresa = new JSONObject(_aEmpresas.getString(j));

                                tpProdutoEmpresa _tpProdutoEmpresa = new tpProdutoEmpresa();
                                _tpProdutoEmpresa.fillByJsonObject(_oEmpresa);

                                _dbProdutoEmpresa.insert(_tpProdutoEmpresa);

                            }

                        }

                    }
                    // endregion


                    // region Inserindo os preços
                    if (_jsonObject.has("Precos")) {

                        JSONArray _aPrecos = _jsonObject.getJSONArray("Precos");

                        if (_aPrecos != null && _aPrecos.length() > 0) {

                            for (int j = 0; j < _aPrecos.length(); j++) {

                                JSONObject _oPreco = new JSONObject(_aPrecos.getString(j));

                                tpTabelaPrecoProduto _tpTabelaPrecoProduto = new tpTabelaPrecoProduto();
                                _tpTabelaPrecoProduto.fillByJsonObject(_oPreco);

                                _dbTabelaPrecoProduto.insert(_tpTabelaPrecoProduto);

                            }

                        }

                    }
                    // endregion


                    // atualizando o valor do último produto sincronizado
                    _last += 1;

                    // atualizando a variável de retorno
                    _out += 1;

                    // atualizando a variável auxiliar
                    _aux += 1;
                }
                // endregion

                publishProgress();

            } while (_aux == _top);
            // endregion

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
    public ProdutoApi(
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



    //region getQuantidadeRegistrosProduto

    private int getQuantidadeRegistrosProduto() throws Exception {

        // region Declarando variável de retorno do método
        int _out = 0;
        // endregion

        // region Declarando demais variáveis
        SQLiteHelper _sqh = null;
        dbParametro _db = null;
        tpParametro _tp = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Cuidando da conexão com o banco de dados
            _sqh = new SQLiteHelper(this._context);
            _sqh.open(true);
            // endregion

            // region Cuidando da instância da classe dbParametro
            _db = new dbParametro(_sqh);
            _tp = _db.getQuantidadeRegistrosProduto();
            // endregion

            // region Cuidando do retorno do processamento
            if (_tp != null) {
                _out = _tp.ValorInteiro;
            }
            // endregion

        } catch (Exception e) {

            throw new Exception("ClienteApi | getQuantidadeRegistrosCliente() | " + e.getMessage());

        } finally {

            if ((_sqh != null) && (_sqh.isOpen())) {
                _sqh.close();
            }

        }
        // endregion

        // region Retornando o resultado do processamento
        return _out;
        // endregion

    }
    //endregion

}
