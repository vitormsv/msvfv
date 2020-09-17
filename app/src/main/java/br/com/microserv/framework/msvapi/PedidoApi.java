package br.com.microserv.framework.msvapi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import br.com.microserv.framework.msvdal.dbCliente;
import br.com.microserv.framework.msvdal.dbPedidoMobile;
import br.com.microserv.framework.msvdal.dbPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.OnTaskCompleteListner;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.eTaskCompleteStatus;


/**
 * Created by notemsv01 on 18/01/2017.
 */

public class PedidoApi extends AsyncTask<Void, Void, Void> {


    // region Declarando variáveis locais da classe

    // Delegate
    private OnTaskCompleteListner _onTaskCompleteListner;

    // Objects
    private Context _context = null;
    private StringBuilder _sb = null;
    private BufferedReader _br = null;
    private SQLiteHelper _sqh = null;
    private tpCliente _tpCliente = null;
    private dbCliente _dbCliente = null;
    private dbPedidoMobile _dbPedidoMobile = null;
    private ArrayList<Long> _lstPedidoMobile = null;
    private ArrayList<tpPedidoMobile> _lst = null;
    private ArrayList<tpPedidoMobile> _lstPedidoUpdate = null;

    // String
    private String _ip = null;
    private String _vendedor = null;
    private String _url = "";
    private String _line = "";
    private String _key = null;
    private String _value = null;

    // int
    private int _indice = 0;
    private long _idPedidoMobile = 0;
    private long _newIdCliente = 0;
    private long _oldIdCliente = 0;
    private long _out = 0;
    // endregion


    // region doInBackground
    @Override
    protected Void doInBackground(Void... params) {

        // region Obtendo dados do servidor e gravando na tabela Bairro
        try {

            // region Buscando no banco os dados do pedido e seus itens

            // region 01 - Iniciando a conexao com o banco de dados
            _sqh = new SQLiteHelper(_context);
            _sqh.open(true);
            // endregion

            // region 02 - Instânciando os objetos da camada DAL
            dbPedidoMobile _dbPedidoMobile = new dbPedidoMobile(_sqh);
            dbPedidoMobileItem _dbPedidoMobileItem = new dbPedidoMobileItem(_sqh);
            dbCliente _dbCliente = new dbCliente(_sqh);
            // endregion

            // region 03 - Carregando os dados dos pedidos
            for (int i = 0; i < _lstPedidoMobile.size(); i++) {

                // region 03.01 - Se a lista ainda foi criada, então...
                if (_lst == null) {
                    _lst = new ArrayList<tpPedidoMobile>();
                }
                // endregion

                // region 03.02 - Carregando o pedido
                tpPedidoMobile _tpPedidoMobile = (tpPedidoMobile) _dbPedidoMobile.getBySourceId(_lstPedidoMobile.get(i));
                // endregion

                // region 03.03 - Carregando os dados dos cliente
                _tpPedidoMobile.Cliente = (tpCliente) _dbCliente.getBySourceId(_tpPedidoMobile.IdCliente);
                // endregion

                // region 03.04 - Carregando os itens do pedido

                // region 03.04.01 - Criando um WHERE para o campo IdPedidoMobile
                SQLClauseHelper _sch = new SQLClauseHelper();
                _sch.addEqualInteger("IdPedidoMobile", _tpPedidoMobile.IdPedidoMobile);
                // endregion

                // region 03.04.02 - Buscando os itens no banco de dados
                _tpPedidoMobile.Itens = _dbPedidoMobileItem.getList(tpPedidoMobileItem.class, _sch);
                // endregion

                // endregion

                // region 03.05 - Adicionando o pedido na lista
                _lst.add(_tpPedidoMobile);
                // endregion

            }
            // endregion

            // endregion

            // region Gerando o objeto JSON do pedido e seus itens
            _indice = 0;

            for (tpPedidoMobile _tp : _lst) {

                // region 01 - Gerando os dados dos itens do pedido
                JSONArray _jaItens = new JSONArray();

                for (tpPedidoMobileItem _tpPedidoMobileItem : _tp.Itens) {

                    JSONObject _joItem = new JSONObject();
                    _joItem.put("IdPedidoMobileItem", _tpPedidoMobileItem.IdPedidoMobileItem);
                    _joItem.put("IdPedidoMobile", _tpPedidoMobileItem.IdPedidoMobile);
                    _joItem.put("IdProduto", _tpPedidoMobileItem.IdProduto);
                    _joItem.put("PackQuantidade", _tpPedidoMobileItem.PackQuantidade);
                    _joItem.put("UnidadeValor", _tpPedidoMobileItem.UnidadeValor);
                    _joItem.put("UnidadeDescontoPercentual", _tpPedidoMobileItem.UnidadeDescontoPercentual);
                    _joItem.put("UnidadeDescontoValor", _tpPedidoMobileItem.UnidadeDescontoValor);
                    _joItem.put("UnidadeValorLiquido", _tpPedidoMobileItem.UnidadeValorLiquido);
                    _joItem.put("UnidadeVendaQuantidade", _tpPedidoMobileItem.UnidadeVendaQuantidade);
                    _joItem.put("UnidadeValorTotal", _tpPedidoMobileItem.UnidadeValorTotal);
                    _joItem.put("Observacao", _tpPedidoMobileItem.Observacao);
                    _joItem.put("DataAlteracao", _tpPedidoMobileItem.DataAlteracao);
                    _joItem.put("UsuarioAlteracao", _tpPedidoMobileItem.UsuarioAlteracao);

                    _jaItens.put(_joItem);

                }
                // endregion

                // region 02 - Gerando os dados do cliente

                // region 02.01 - Validando o IdCliente
                if (String.valueOf(_tp.Cliente.IdCliente).equals(_tp.Cliente.Codigo)) {
                    _tp.Cliente.IdCliente = 0;
                }
                // endregion

                // region 02.01 - Montando o JSON
                JSONObject _joCliente = new JSONObject();

                _joCliente.put("IdCliente", _tp.Cliente.IdCliente);
                _joCliente.put("Codigo", _tp.Cliente.Codigo);
                _joCliente.put("RazaoSocial", _tp.Cliente.RazaoSocial);
                _joCliente.put("NomeFantasia", _tp.Cliente.NomeFantasia);
                _joCliente.put("CnpjCpf", _tp.Cliente.CnpjCpf);
                _joCliente.put("IeRg", _tp.Cliente.IeRg);
                _joCliente.put("LogradouroTipo", _tp.Cliente.LogradouroTipo);
                _joCliente.put("LogradouroNome", _tp.Cliente.LogradouroNome);
                _joCliente.put("LogradouroNumero", _tp.Cliente.LogradouroNumero);
                _joCliente.put("BairroNome", _tp.Cliente.BairroNome);
                _joCliente.put("IdCidade", _tp.Cliente.IdCidade);
                _joCliente.put("IdRegiao", _tp.Cliente.IdRegiao);
                _joCliente.put("Cep", MSVUtil.onlyNumber(_tp.Cliente.Cep));
                _joCliente.put("TelefoneFixo", _tp.Cliente.TelefoneFixo);
                _joCliente.put("TelefoneCelular", _tp.Cliente.TelefoneCelular);
                _joCliente.put("Email", _tp.Cliente.Email);
                _joCliente.put("ContatoNome", _tp.Cliente.ContatoNome);
                _joCliente.put("IdCondicaoPagamentoPadrao", _tp.Cliente.IdCondicaoPagamentoPadrao);
                _joCliente.put("IdTabelaPreco", _tp.Cliente.IdTabelaPreco);
                // endregion

                // endregion

                // region 03 - Gerando os dados do pedido
                JSONObject _joPedido = new JSONObject();
                _joPedido.put("IdPedidoMobile", _tp.IdPedidoMobile);
                _joPedido.put("IdEmpresa", _tp.IdEmpresa);
                _joPedido.put("IdTipoPedido", _tp.IdTipoPedido);
                _joPedido.put("IdCliente", _tp.Cliente.IdCliente);
                _joPedido.put("Numero", _tp.Numero);
                _joPedido.put("NumeroCliente", _tp.NumeroCliente);
                _joPedido.put("EmissaoDataHora", _tp.EmissaoDataHora);
                _joPedido.put("IdTabelaPreco", _tp.IdTabelaPreco);
                _joPedido.put("IdCondicaoPagamento", _tp.IdCondicaoPagamento);
                _joPedido.put("IdTransportadora", _tp.IdTransportadora);
                _joPedido.put("Observacao", _tp.Observacao);
                _joPedido.put("ItensQuantidade", _tp.ItensQuantidade);
                _joPedido.put("TotalValor", _tp.TotalValor);
                _joPedido.put("DescontoPercentual1", _tp.DescontoPercentual1);
                _joPedido.put("DescontoValor1", _tp.DescontoValor1);
                _joPedido.put("TotalValorLiquido", _tp.TotalValorLiquido);
                _joPedido.put("IdVendedor", _tp.IdVendedor);
                _joPedido.put("EhConfirmado", _tp.EhConfirmado);
                _joPedido.put("EhSincronizado", _tp.EhSincronizado);
                _joPedido.put("DataAlteracao", _tp.DataAlteracao);
                _joPedido.put("UsuarioAlteracao", _tp.UsuarioAlteracao);
                _joPedido.put("Cliente", _joCliente);
                _joPedido.put("Itens", _jaItens);
                // endregion

                // region 04 - Descarregando o objeto json para o texto
                String _joTexto = _joPedido.toString();
                // endregion

                // region 05 - Realizando o envido dos dados do pedido para o webservice

                // region 05.01 - Montando a url de requisição para o webservice
                _url = "http://{ip}/api/pedido/cadastrar";
                _url = _url.replace("{ip}", _ip);
                _url = _url.replace("{vendedor}", _vendedor);
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
                _osw.write(_joPedido.toString());
                _osw.flush();
                // endregion

                // region 05.04 - verificando o resultado de envio do pedido
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    // region Capturando a mensagem do response
                    InputStream responseBody = connection.getInputStream();
                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    // endregion

                    // region Montando um objeto json com o que foi capturado do retorno
                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                    jsonReader.beginObject();
                    // endregion

                    // region Realizando a leitura dos elementos dentro do json
                    while (jsonReader.hasNext()) {

                        // region Selecionando o nome e o valor do nó do elemento em questão
                        _key = jsonReader.nextName();
                        _value = jsonReader.nextString();
                        // endregion

                        // region Se o nome do nó selecionado for igual a "idcliente" então...
                        if (_key.toLowerCase().equals("idcliente")) {

                            // region Convertendo o valor de retorno em um tipo "long"
                            _newIdCliente = Long.parseLong(_value);
                            // endregion

                            // region Tratando o identificador do cliente
                            // Se o valor que foi retornado pela API for diferente do valor persistido dentro do
                            // objeto _tp, teremos então que substituir o valor do identificador neste objeto e
                            // também em todos os outros pedidos que existam para o cliente em nosso banco
                            if(_newIdCliente != _tp.IdCliente)
                            {
                                _oldIdCliente = _tp.IdCliente;

                                // region Atualizando o cliente no banco do dispositivo
                                if (_dbCliente == null) {
                                    _dbCliente = new dbCliente(_sqh);
                                }

                                _tpCliente = (tpCliente) _dbCliente.getBySourceId(_tp.IdCliente);
                                _tpCliente.IdCliente = _newIdCliente;

                                _dbCliente.update(_tpCliente);
                                // endregion

                                // region Atualizando os demais pedidos
                                SQLClauseHelper _sch =  new SQLClauseHelper();
                                _sch.addEqualInteger("IdCliente", _tp.IdCliente);

                                if (_dbPedidoMobile == null) {
                                    _dbPedidoMobile = new dbPedidoMobile(_sqh);
                                }

                                _lstPedidoUpdate = (ArrayList<tpPedidoMobile>) _dbPedidoMobile.getList(tpPedidoMobile.class, _sch);

                                for (tpPedidoMobile _item : _lstPedidoUpdate)
                                {
                                    _item.IdCliente = _newIdCliente;
                                    _dbPedidoMobile.update(_item);
                                }
                                // endregion
                            }
                            // endregion

                            break;

                        } else {
                            jsonReader.skipValue();
                        }
                        // endregion
                    }
                    // endregion

                    jsonReader.close();

                    _out = 1;

                    if (_dbPedidoMobile.setSynchronizedOrder(_tp.IdPedidoMobile)) {
                        _out = 2;
                    }

                } else if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {

                    String _msg = new BufferedReader(new InputStreamReader((connection.getErrorStream()))).readLine();

                    if (_msg.equals("\"DUPLICATE_KEY\"")) {
                        _dbPedidoMobile.setSynchronizedOrder(_tp.IdPedidoMobile);
                        _out = 3;
                    } else {
                        _out = 0;
                    }

                } else {

                    _out = 0;

                }
                // endregion

                // endregion

                // region 06 - Invocando o método de atualização da tela
                publishProgress();
                // endregion

            }

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

        if (_out == 0) {

            _onTaskCompleteListner.onTaskComplete(_indice, eTaskCompleteStatus.ERROR, _out, null, null);

        } else if (_out >= 1) {

            _onTaskCompleteListner.onTaskComplete(_indice, eTaskCompleteStatus.SUCCESS, _out, null, null);

        }

    }
    // endregion


    // region PedidoApi
    public PedidoApi(
            Context context,
            String ip,
            String vendedor,
            ArrayList<Long> lstPedidoMobile,
            int indice,
            OnTaskCompleteListner onTaskCompleteListner
    ) {

        this._context = context;
        this._ip = ip;
        this._vendedor = vendedor;
        this._lstPedidoMobile = lstPedidoMobile;
        this._indice = indice;
        this._onTaskCompleteListner = onTaskCompleteListner;

    }
    // endregion

    //region AtualizarIdCliente

    private void AtualizarIdCliente()
    {



    }

    //endregion


}

