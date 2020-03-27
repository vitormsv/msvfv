package br.com.microserv.framework.msvapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.AsyncTaskResult;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class ClientePutApi extends AsyncTask<Void, Void, AsyncTaskResult<Object>> {

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
    private String _url = "";
    private tpCliente _tpCliente = null;
    private String _line = "";
    private AsyncTaskResult<Object> _Result = null;

    // int
    private int _indice = 0;
    private int _aux = 0;

    // long
    private long _out = 0;
    private long _idEmpresa = 0;
    // endregion


    // region doInBackground
    @Override

    protected AsyncTaskResult<Object> doInBackground(Void... params){

        boolean isOpenedHere = false;

        try {

            if (_sqh == null) {

                // region 01 - Iniciando a conexao com o banco de dados
                _sqh = new SQLiteHelper(_context);
                _sqh.open(true);
                isOpenedHere = true;
                // endregion

            }

            // region 01 - Gerando os dados dos itens do pedido
            JSONObject _jsCliente = new JSONObject();

            _jsCliente.put("IdCliente", 0);
            _jsCliente.put("Codigo", _tpCliente.Codigo);
            _jsCliente.put("RazaoSocial", _tpCliente.RazaoSocial);
            _jsCliente.put("NomeFantasia", _tpCliente.NomeFantasia);
            _jsCliente.put("CnpjCpf", _tpCliente.CnpjCpf);
            _jsCliente.put("IeRg", _tpCliente.IeRg);
            _jsCliente.put("LogradouroTipo", _tpCliente.LogradouroTipo);
            _jsCliente.put("LogradouroNome", _tpCliente.LogradouroNome);
            _jsCliente.put("LogradouroNumero", _tpCliente.LogradouroNumero);
            _jsCliente.put("BairroNome", _tpCliente.BairroNome);
            _jsCliente.put("IdCidade", _tpCliente.IdCidade);
            _jsCliente.put("IdRegiao", _tpCliente.IdRegiao);
            _jsCliente.put("Cep", MSVUtil.onlyNumber(_tpCliente.Cep));
            _jsCliente.put("TelefoneFixo", _tpCliente.TelefoneFixo);
            _jsCliente.put("TelefoneCelular", _tpCliente.TelefoneCelular);
            _jsCliente.put("Email", _tpCliente.Email);
            _jsCliente.put("ContatoNome", _tpCliente.ContatoNome);
            // endregion

            // region 05 - Realizando o envido dos dados do pedido para o webservice

            // region 05.01 - Montando a url de requisição para o webservice
            _url = "http://{ip}/api/cliente/cadastrar";
            _url = _url.replace("{ip}", _ip);
            // endregion

            // region 05.02 - Instancianco um objeto URL
            // este objeto é responsável por estabelecer a comunicação
            // entre o dispositivo e o webapi
            URL url = new URL(_url);
            // endregion

            // region 05.03 - Parametrizando a forma de envio dos dados e enviando
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter _osw = new OutputStreamWriter(connection.getOutputStream());
            _osw.write(_jsCliente.toString());
            _osw.flush();
            // endregion

            // region 05.04 - verificando o resultado de envio do pedido
            if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {

                String _msg = new BufferedReader(new InputStreamReader((connection.getErrorStream()))).readLine();

                throw new Exception(_msg);

            }
            // endregion

            // endregion
            _Result = new AsyncTaskResult<>((Object)"ok");

        } catch (JSONException j) {

            _Result = new AsyncTaskResult<>(new Exception("Erro ao tentar parsear o JSON"));

        } catch (Exception e) {

            _Result = new AsyncTaskResult<>(new Exception(e.getLocalizedMessage()));

        } finally {

            if (isOpenedHere) {
                if (_sqh.isOpen()) {
                    _sqh.close();
                }
            }
        }
        // endregion

        return null;
    }
    // endregion

    // region onPostExecute
    @Override
    protected void onPostExecute(AsyncTaskResult<Object> _Result) {
        _onTaskCompleteListner.onTaskComplete(_indice, eTaskCompleteStatus.SUCCESS, _out, null, null);
    }
    // endregion

    // region ClienteApi
    public ClientePutApi(
            Context context,
            String ip,
            tpCliente _tpCliente,
            long idEmpresa,
            OnTaskCompleteListner onTaskCompleteListner
    ) {

        this._context = context;
        this._ip = ip;
        this._tpCliente = _tpCliente;
        this._onTaskCompleteListner = onTaskCompleteListner;
        this._idEmpresa = idEmpresa;
    }
    // endregion

}
