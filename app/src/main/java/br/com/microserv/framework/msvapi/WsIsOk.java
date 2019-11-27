package br.com.microserv.framework.msvapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class WsIsOk extends AsyncTask<Void, Void, Void> {

    // region Declarando variáveis locais da classe

    // Delegate
    private OnTaskCompleteListner _onTaskCompleteListner;

    // Objects
    private Context _context = null;
    private ProgressDialog _dialog = null;
    private StringBuilder _sb = null;
    private BufferedReader _br = null;
    private SQLiteHelper _sqh = null;
    private tpVendedor _tpVendedor = null;

    // String
    private String _ip = null;

    // int
    int _out = 0;
    int _httpStatus = 0;

    // endregion


    // region onPreExecute
    @Override
    protected void onPreExecute() {

        // region Criando a janela de progresso indeterminado
        _dialog = new ProgressDialog(_context);
        _dialog.setTitle("Aguarde...");
        _dialog.setMessage("Estamos validando o novo endereço do sincronizador informado");
        _dialog.show();
        // endregion

        super.onPreExecute();

    }
    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Invocando método da Api

        try {

            // region Inicializando objetos e variáveis
            _out = 0;
            // endregion


            // region Realizando a requisição na API
             URL url = new URL("http://" + _ip + "/ws/api/isok");
             URLConnection urlConnection = url.openConnection();
            // endregion


            // region Verificando o HttpStatus de retorno para sabermos o que fazer
             _httpStatus = ((HttpURLConnection)urlConnection).getResponseCode();

             if (_httpStatus == 200) {
                 _out = 1;
             }
            // endregion

        } catch(Exception e) {

            Log.e("Erro", "Erro ao verificar o endereço do WS", e);
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

        // region Encerrando a exibição da janela de progresso
        if (_dialog != null) {
            _dialog.dismiss();
        }
        // endregion

        // region Invocando o evento na activity pai
        _onTaskCompleteListner.onTaskComplete(0, eTaskCompleteStatus.SUCCESS, _out, null, null);
        // endregion

    }
    // endregion


    // region VendedorApi
    public WsIsOk(
            Context context,
            String ip,
            OnTaskCompleteListner onTaskCompleteListner
    ) {

        this._context = context;
        this._ip = ip;
        this._onTaskCompleteListner = onTaskCompleteListner;

    }
    // endregion

}
