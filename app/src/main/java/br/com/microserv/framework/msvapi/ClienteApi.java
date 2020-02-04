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

import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbClienteHistoricoCompra;
import br.com.microserv.framework.msvdal.dbClienteMix;
import br.com.microserv.framework.msvdal.dbClienteTabelaPreco;
import br.com.microserv.framework.msvdal.dbFinanceiro;
import br.com.microserv.framework.msvdal.dbParametro;
import br.com.microserv.framework.msvdal.dbRegiao;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteHistoricoCompra;
import br.com.microserv.framework.msvdto.tpClienteMix;
import br.com.microserv.framework.msvdto.tpClienteTabelaPreco;
import br.com.microserv.framework.msvdto.tpFinanceiro;
import br.com.microserv.framework.msvdto.tpParametro;
import br.com.microserv.framework.msvdto.tpRegiao;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class ClienteApi extends AsyncTask<Void, Void, Void> {

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

    // long
    private long _last = 0;
    private long _out = 0;
    private long _top = 0;
    private long _idEmpresa = 0;
    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Obtendo dados do servidor e gravando na tabela Cliente
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

            // region Instânciando o objeto da tabela Cliente
            dbCliente _dbCliente = new dbCliente(_sqh);
            _dbCliente.truncateTable();
            // endregion

            // region Instanciando o objeto da tabela Regiao
            dbRegiao _dbRegiao = new dbRegiao(_sqh);
            _dbRegiao.truncateTable();
            // endregion

            // region Instânciando o objeto da tabela Tabela de Preço
            dbClienteTabelaPreco _dbClienteTabelaPreco = new dbClienteTabelaPreco(_sqh);
            _dbClienteTabelaPreco.truncateTable();
            // endregion

            // region Instânciando o objeto da tabela Financeiro
            dbFinanceiro _dbFinanceiro = new dbFinanceiro(_sqh);
            _dbFinanceiro.truncateTable();
            // endregion

            // region Instânciando o objeto da tabela ClienteProdutoMix
            dbClienteMix _dbClienteMix = new dbClienteMix(_sqh);
            _dbClienteMix.truncateTable();
            // endregion

            // region Instânciando o objeto da tabela ClienteHistoricoCompra
            dbClienteHistoricoCompra _dbClienteHistoricoCompra = new dbClienteHistoricoCompra(_sqh);
            _dbClienteHistoricoCompra.truncateTable();
            // endregion

            // region Executar enquanto o retorno traga 100 registros
            do {

                // region Inicializar variáveis
                _sb = new StringBuilder();

                _line = "";
                _aux = 0;
                //_top = 25;
                _top = this.getQuantidadeRegistrosCliente();
                // endregion

                // region Conectando com o webservice e recuperando a string json
                _url = "http://{ip}/api/cliente/{vendedor}/top/{top}/{last}/{idempresa}";
                _url = _url.replace("{ip}", _ip);
                _url = _url.replace("{vendedor}", _vendedor);
                _url = _url.replace("{top}", String.valueOf(_top));
                _url = _url.replace("{last}", String.valueOf(_last));
                _url = _url.replace("{idempresa}", String.valueOf(_idEmpresa));

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

                    // obtendo um objeto tpCliente
                    tpCliente _tpCliente = new tpCliente();
                    _tpCliente.fillByJsonObject(_jsonObject);

                    // inserindo o objeto _tpEmpresa na tabela Cliente
                    _dbCliente.insert(_tpCliente);


                    // region Inserindo os dados da região do cliente
                    if (_jsonObject.has("Regiao")) {

                        // region Recuperando o objeto JSON da região
                        JSONArray _aRegiao = _jsonObject.getJSONArray("Regiao");
                        // endregion

                        // region Se existir a informação da região
                        if (_aRegiao != null && _aRegiao.length() > 0) {

                            for (int j = 0; j < _aRegiao.length(); j++) {

                                JSONObject _oRegiao = new JSONObject(_aRegiao.getString(j));

                                // region Preenchendo o objeto tpRegiao com os dados o JSON
                                tpRegiao _tpRegiao = new tpRegiao();
                                _tpRegiao.fillByJsonObject(_oRegiao);
                                // endregion

                                // region Montando WHERE para verificar se a região já existe
                                SQLClauseHelper _schIdRegiao = new SQLClauseHelper();
                                _schIdRegiao.addEqualInteger("IdRegiao", _tpRegiao.IdRegiao);
                                // endregion

                                // region Se a região não existe então vamos incluir
                                if (_dbRegiao.getOne(_schIdRegiao) == null) {
                                    _dbRegiao.insert(_tpRegiao);
                                }
                                // endregion

                            }

                        }
                        // endregion

                    }
                    // endregion


                    // region Inserindo as tabelas de preço do cliente
                    if (_jsonObject.has("Tabelas")) {

                        JSONArray _aTabelas = _jsonObject.getJSONArray("Tabelas");

                        if (_aTabelas != null && _aTabelas.length() > 0) {

                            for (int j = 0; j < _aTabelas.length(); j++) {

                                JSONObject _oTabela = new JSONObject(_aTabelas.getString(j));

                                tpClienteTabelaPreco _tpClienteTabelaPreco = new tpClienteTabelaPreco();
                                _tpClienteTabelaPreco.fillByJsonObject(_oTabela);

                                _dbClienteTabelaPreco.insert(_tpClienteTabelaPreco);

                            }

                        }

                    }
                    // endregion


                    // region Inserindo a condição financeira do cliente
                    if (_jsonObject.has("Parcelas")) {

                        JSONArray _aParcelas = _jsonObject.getJSONArray("Parcelas");

                        if (_aParcelas != null && _aParcelas.length() > 0) {

                            for (int j = 0; j < _aParcelas.length(); j++) {

                                JSONObject _oParcela = new JSONObject(_aParcelas.getString(j));

                                tpFinanceiro _tpFinanceiro = new tpFinanceiro();
                                _tpFinanceiro.fillByJsonObject(_oParcela);

                                _dbFinanceiro.insert(_tpFinanceiro);

                            }

                        }

                    }
                    // endregion


                    // region Inserindo o historico de compra do cliente
                    if (_jsonObject.has("Compras")) {

                        JSONArray _aCompras = _jsonObject.getJSONArray("Compras");

                        if (_aCompras != null && _aCompras.length() > 0) {

                            for (int j = 0; j < _aCompras.length(); j++) {

                                JSONObject _oCompra = new JSONObject(_aCompras.getString(j));

                                tpClienteHistoricoCompra _tpClienteHistoricoCompra = new tpClienteHistoricoCompra();
                                _tpClienteHistoricoCompra.fillByJsonObject(_oCompra);

                                _dbClienteHistoricoCompra.insert(_tpClienteHistoricoCompra);

                            }

                        }

                    }
                    // endregion


                    // region Inserindo o historico de compra do cliente
                    if (_jsonObject.has("Mix")) {

                        JSONArray _aMix = _jsonObject.getJSONArray("Mix");

                        if (_aMix != null && _aMix.length() > 0) {

                            for (int j = 0; j < _aMix.length(); j++) {

                                JSONObject _oMix = new JSONObject(_aMix.getString(j));

                                tpClienteMix _tpClienteMix = new tpClienteMix();
                                _tpClienteMix.fillByJsonObject(_oMix);

                                _dbClienteMix.insert(_tpClienteMix);

                            }

                        }

                    }
                    // endregion


                    // atualizando o valor do último Cliente sincronizado
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
            Log.e("Erro", "Erro no método de sincronização de clientes " + e.getMessage());
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


    //region getQuantidadeRegistrosCliente
    private int getQuantidadeRegistrosCliente() throws Exception {

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
            _tp = _db.getQuantidadeRegistrosCliente();
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


    // region ClienteApi
    public ClienteApi(
            Context context,
            String ip,
            String vendedor,
            int indice,
            long idEmpresa,
            OnTaskCompleteListner onTaskCompleteListner
    ) {

        this._context = context;
        this._ip = ip;
        this._vendedor = vendedor;
        this._indice = indice;
        this._onTaskCompleteListner = onTaskCompleteListner;
        this._idEmpresa = idEmpresa;
    }
    // endregion

}
