package br.com.microserv.framework.msvapi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvdal.dbVendedor;
import br.com.microserv.framework.msvdto.tpBairro;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;

/**
 * Created by notemsv01 on 18/01/2017.
 */

public class VendedorApi extends AsyncTask<Void, Void, Void> {

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
    private String _vendedor = null;
    private String _hash = null;
    private String _url = "";
    private String _line = "";


    int _out = 0;
    int _httpStatus = 0;

    // endregion


    // region onPreExecute
    @Override
    protected void onPreExecute() {

        // region Criando a janela de progresso indeterminado
        _dialog = new ProgressDialog(_context);
        _dialog.setTitle("Aguarde...");
        _dialog.setMessage("Estamos validando suas credenciais para ativar o uso deste dispositivo");
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
            _line = "";
            _sb = new StringBuilder();

            _out = 0;
            // endregion


            // region Realizando a requisição na API
            URL url = new URL("http://" + _ip + "/api/vendedor/vincular/" + _vendedor + "/aaaa-bbbb-cccc");
            URLConnection urlConnection = url.openConnection();
            // endregion


            // region Verificando o HttpStatus de retorno para sabermos o que fazer
            _httpStatus = ((HttpURLConnection)urlConnection).getResponseCode();

            if (_httpStatus == 200) {

                // region Preenchendo um StringBuilder com o resultado da API
                _br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                while ((_line = _br.readLine()) != null) {
                    _sb.append(_line + "\n");
                }

                _br.close();
                // endregion

                // region Deserializando o texto recuperado da API
                JSONObject _oJson = new JSONObject(_sb.toString());
                _tpVendedor = new tpVendedor();
                _tpVendedor.fillByJsonObject(_oJson);
                // endregion

                // region Gravando os dados do vendedor
                if ((_tpVendedor != null) && (_vendedor.equals(_tpVendedor.Codigo))) {

                    _sqh = new SQLiteHelper(_context);
                    _sqh.open(true);

                    dbVendedor _dbVendedor = new dbVendedor(_sqh);
                    _dbVendedor.insert(_tpVendedor);

                    _sqh.close();

                }
                // endregion

                // region Informando que o vendedor foi anexado com sucesso
                _out = 1;
                // endregion

            }
            // endregion

        } catch (JSONException j) {

            Log.e("Erro", "Erro no parsing do JSON", j);
            j.printStackTrace();

        } catch(Exception e) {

            Log.e("Erro", "Erro no parsing do JSON", e);
            e.printStackTrace();

        } finally {

            if ((_sqh!= null) && (_sqh.isOpen())) {
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
    public VendedorApi(
            Context context,
            String ip,
            String vendedor,
            String hash,
            OnTaskCompleteListner onTaskCompleteListner
    ) {

        this._context = context;
        this._ip = ip;
        this._vendedor = vendedor;
        this._hash = hash;
        this._onTaskCompleteListner = onTaskCompleteListner;

    }
    // endregion

}
