package br.com.microserv.framework.msvdal;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpKeyValueRow;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvdto.tpPedidoMobileHistorico;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpPedidoMobileItemHistorico;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpSincronizacao;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvdto.tpTransportadora;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.MSVUtil;
import br.com.microserv.framework.msvutil.SQLClauseHelper;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbPedidoMobile extends dbBase implements dbInterface {

    private SQLiteHelper _dbHelper = null;


    // region dbPedidoMobile (constructor)
    public dbPedidoMobile(SQLiteHelper dbHelper) throws Exception{

        super(dbHelper);
        _dbHelper = dbHelper;

    }
    // endregion


    // region method Override

    // region tpIterface
    @Override
    public tpInterface newDTO(){

        return new tpPedidoMobile();

    }
    // endregion

    // region method loadDependencies
    @Override
    public void loadDepencies(tpInterface tp) throws  Exception{

        // region Declarando as variáveis locais do método
        tpPedidoMobile _tpPedidoMobile = (tpPedidoMobile) tp;

        SQLClauseHelper _schIdPedidoMobile = null;

        dbEmpresa _dbEmpresa = null;
        dbTipoPedido _dbTipoPedido = null;
        dbCliente _dbCliente = null;
        dbTabelaPreco _dbTabelaPreco = null;
        dbCondicaoPagamento _dbCondicaoPagamento = null;
        dbTransportadora _dbTransportadora = null;
        dbVendedor _dbVendedor = null;
        dbPedidoMobileItem _dbPedidoMobileItem = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Buscando as informações da Empresa
            _dbEmpresa = new dbEmpresa(_dbHelper);
            _tpPedidoMobile.Empresa = (tpEmpresa) _dbEmpresa.getBySourceId(_tpPedidoMobile.IdEmpresa);
            // endregion

            // region Buscando as informações do Tipo de Pedido
            _dbTipoPedido = new dbTipoPedido(_dbHelper);
            _tpPedidoMobile.TipoPedido = (tpTipoPedido) _dbTipoPedido.getBySourceId(_tpPedidoMobile.IdTipoPedido);
            // endregion

            // region Buscando as informações do Cliente
            _dbCliente = new dbCliente(_dbHelper);
            _tpPedidoMobile.Cliente = (tpCliente) _dbCliente.getBySourceId(_tpPedidoMobile.IdCliente);
            // endregion

            // region Buscando as informações da Tabela de Preço
            _dbTabelaPreco = new dbTabelaPreco(_dbHelper);
            _tpPedidoMobile.TabelaPreco = (tpTabelaPreco) _dbTabelaPreco.getBySourceId(_tpPedidoMobile.IdTabelaPreco);
            // endregion

            // region Buscando as informações de Condição de Pagamento
            _dbCondicaoPagamento = new dbCondicaoPagamento(_dbHelper);
            _tpPedidoMobile.CondicaoPagamento = (tpCondicaoPagamento) _dbCondicaoPagamento.getBySourceId(_tpPedidoMobile.IdCondicaoPagamento);
            // endregion

            // region Buscando as informações de Transportadora
            _dbTransportadora = new dbTransportadora(_dbHelper);
            _tpPedidoMobile.Transportadora = (tpTransportadora) _dbTransportadora.getBySourceId(_tpPedidoMobile.IdTransportadora);
            // endregion

            // region Buscando as informações do Vendedor
            _dbVendedor = new dbVendedor(_dbHelper);
            _tpPedidoMobile.Vendedor = (tpVendedor) _dbVendedor.getBySourceId(_tpPedidoMobile.IdVendedor);
            // endregion

            // region Buscando as informações dos Itens
            _schIdPedidoMobile = new SQLClauseHelper();
            _schIdPedidoMobile.addEqualInteger("IdPedidoMobile", _tpPedidoMobile.IdPedidoMobile);

            _dbPedidoMobileItem = new dbPedidoMobileItem(_dbHelper);
            _tpPedidoMobile.Itens = _dbPedidoMobileItem.getList(tpPedidoMobileItem.class, _schIdPedidoMobile);
            // endregion

            // region Invocando o método da classe ancestral
            super.loadDepencies(tp);
            // endregion

        } catch (Exception e) {
            throw new Exception("dbCliente|loadDependencies -> " + e.getMessage());
        }
        // endregion

    }
    // endregion

    // endregion


    // region newCode
    public String newCode(String vendedorCodigo, SQLiteHelper sqh) {

        String _out = "";
        String _tabela = "pedido";
        String _coluna = vendedorCodigo;
        boolean _openHere = false;

        try {

            long _l = sqh.nextId(_tabela, _coluna);
            _out = vendedorCodigo + String.format("%1$05d", _l);
            _out = String.valueOf(System.currentTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_openHere) {
                if (sqh.isOpen()) {
                    sqh.close();
                }
            }
        }


        return _out;

    }
    // endregion


    // region setConfirmedOrder
    public boolean setConfirmedOrder(long idPedidoMobile) throws Exception{

        // region Declarando variáveis
        boolean _out = false;

        int _rows = 0;

        String _table = null;
        String _whereClause = null;
        String[] _whereArgs = null;

        ContentValues _values = null;
        // endregion

        // region Montando os valores para informar que o pedido foi sincronizado

        // Tabela
        _table = "PedidoMobile";

        // Valores que deverão ser alterados
        _values = new ContentValues();
        _values.put("EhConfirmado", 1);

        // Condição where
        _whereClause = "IdPedidoMobile=?";

        // Valores para substituição na condição where
        _whereArgs = new String[]{String.valueOf(idPedidoMobile)};

        // endregion

        // region Invocando o método update da classe dbBase
        try {

            _rows = update(_table, _values, _whereClause, _whereArgs);

        } catch (Exception e) {

            throw new Exception("dbPedidoMobile | setConfirmedOrder() | " + e.getMessage());

        }
        // endregion

        // region Devolvendo o resultado do processamento
        return (_rows > 0);
        // endregion

    }
    // endregion


    // region setSynchronizedOrder
    public boolean setSynchronizedOrder(long idPedidoMobile) throws Exception {

        // region Declarando variáveis
        boolean _out = false;

        int _rows = 0;

        String _table = null;
        String _whereClause = null;
        String[] _whereArgs = null;

        ContentValues _values = null;
        // endregion

        // region Montando os valores para informar que o pedido foi sincronizado

        // Tabela
        _table = "PedidoMobile";

        // Valores que deverão ser alterados
        _values = new ContentValues();
        _values.put("EhSincronizado", 1);

        // Condição where
        _whereClause = "IdPedidoMobile=?";

        // Valores para substituição na condição where
        _whereArgs = new String[]{String.valueOf(idPedidoMobile)};

        // endregion

        // region Invocando o método update da classe dbBase
        try {

            _rows = update(_table, _values, _whereClause, _whereArgs);

        } catch (Exception e) {

            throw new Exception("dbPedidoMobile | setSynchronizedOrder() | " + e.getMessage());

        }
        // endregion

        // region Devolvendo o resultado do processamento
        return (_rows > 0);
        // endregion

    }
    // endregion


    // region loadOneById
    public tpPedidoMobile loadOneById(long id) throws Exception {

        // region Instânciando variáveis locais
        boolean _openHere = false;
        // endregion


        // region Declarando variáveis de objetos
        tpPedidoMobile _tpOut = null;

        dbCliente _dbCliente = null;
        dbCidade _dbCidade = null;
        dbEmpresa _dbEmpresa = null;
        dbTipoPedido _dbTipoPedido = null;
        dbTabelaPreco _dbTabelaPreco = null;
        dbCondicaoPagamento _dbCondicaoPagamento = null;
        dbTransportadora _dbTransportadora = null;
        dbPedidoMobileItem _dbPedidoMobileItem = null;
        dbProduto _dbProduto = null;
        dbVendedor _dbVendedor = null;
        // endregion


        // region Bloco protegido
        try {

            // region Verificando o objeto de banco não está nulo
            if (_dbHelper == null) {
                throw new Exception("dbPedidoMobile | loadOne -> O objeto dbHelper não pode ser nulo");
            }
            // endregion


            // region Verificando se a conexão já esta aberta
            if (_dbHelper.isOpen() == false) {
                _dbHelper.open(false);
                _openHere = true;
            }
            // endregion


            // region Buscando a informação do pedido
            _tpOut = (tpPedidoMobile) this.getById(id);
            // endregion


            // region Se o pedido foi localizado então vamos buscar as demais informações
            if (_tpOut != null) {

                // region Instânciando objetos de banco
                _dbEmpresa = new dbEmpresa(_dbHelper);
                _dbTipoPedido = new dbTipoPedido(_dbHelper);
                _dbPedidoMobileItem = new dbPedidoMobileItem(_dbHelper);
                _dbCondicaoPagamento = new dbCondicaoPagamento(_dbHelper);
                _dbTabelaPreco = new dbTabelaPreco(_dbHelper);
                _dbTransportadora = new dbTransportadora(_dbHelper);
                _dbCliente = new dbCliente(_dbHelper);
                _dbCidade = new dbCidade(_dbHelper);
                _dbProduto = new dbProduto(_dbHelper);
                _dbVendedor = new dbVendedor(_dbHelper);
                // endregion

                // region Montando o WHERE para o campo IdPedidoMobil
                SQLClauseHelper _schIdPedidoMobile = new SQLClauseHelper();
                _schIdPedidoMobile.addEqualInteger("IdPedidoMobile", _tpOut.IdPedidoMobile);
                // endregion

                // region Carregando os dados do vendedor
                _tpOut.Vendedor = (tpVendedor) _dbVendedor.getBySourceId(_tpOut.IdVendedor);
                // endregion

                // region Carregando o Cliente
                _tpOut.Cliente = (tpCliente) _dbCliente.getBySourceId(_tpOut.IdCliente);
                // endregion

                // region Carregando os dados da cidade do cliente
                _tpOut.Cliente.Cidade = (tpCidade) _dbCidade.getBySourceId(_tpOut.Cliente.IdCidade);
                // endregion

                // region Carregando a Empresa
                _tpOut.Empresa = (tpEmpresa) _dbEmpresa.getBySourceId(_tpOut.IdEmpresa);
                // endregion

                // region Carregando Tipo de Pedido
                _tpOut.TipoPedido = (tpTipoPedido) _dbTipoPedido.getBySourceId(_tpOut.IdTipoPedido);
                // endregion

                // region Carregando Tabela de Preço
                _tpOut.TabelaPreco = (tpTabelaPreco) _dbTabelaPreco.getBySourceId(_tpOut.IdTabelaPreco);
                // endregion

                // region Carregando Condição de Pagamento
                _tpOut.CondicaoPagamento = (tpCondicaoPagamento) _dbCondicaoPagamento.getBySourceId(_tpOut.IdCondicaoPagamento);
                // endregion

                // region Carregando Transportadora
                _tpOut.Transportadora = (tpTransportadora) _dbTransportadora.getBySourceId(_tpOut.IdTransportadora);
                // endregion

                // region Carregando os itens
                _tpOut.Itens = _dbPedidoMobileItem.getList(tpPedidoMobileItem.class, _schIdPedidoMobile);

                for (tpPedidoMobileItem _tpItem : _tpOut.Itens) {
                    _tpItem.Produto = (tpProduto) _dbProduto.getBySourceId(_tpItem.IdProduto);
                }
                // endregion

            }
            // endregion


            // region Retornando o objeto preenchido
            return _tpOut;
            // endregion

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {

            if (_openHere) {
                if (_dbHelper.isOpen()) {
                    _dbHelper.close();
                }
            }

        }
        // endregion

    }
    // endregion


    // region saveToJson
    public String saveToJson(long id) throws Exception {

        String _out = null;

        try {

            // region Lendo o objeto tpPedidoMobile de forma completa
            tpPedidoMobile _tp = this.loadOneById(id);

            if (_tp != null) {
                _out = _tp.Numero + ".json";
            } else {
                throw new Exception("dbPedidoMobile | saveToJson -> Não foi possível ler o objeto pedido");
            }
            // endregion


            // region Invocando o metodo que escreve o JSON para sincronização
            JSONObject _joSync = _tp.getSyncJson();
            // endregion


            // region Montando o nome do diretório
            String _msvsmart = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/msvsmart/json";
            // endregion


            // region Verificando se o diretorio existe
            File _dir = new File(_msvsmart);

            if (_dir.exists() == false){
                _dir.mkdirs();
            }
            // endregion


            // region Criando a representação do arquivo texto
            File _arq = new File(_msvsmart, _out);

            if (_arq.exists() == true) {
                _arq.delete();
            }

            _arq.createNewFile();
            // endregion


            // region Descarregando os dados do pedido em formato texto
            FileOutputStream _fos = new FileOutputStream(_arq);
            _fos.write(_joSync.toString(2).getBytes());
            _fos.close();
            // endregion

        } catch (Exception e) {
            throw new Exception("dbPedidoMobile | saveToJson ->" + e.getMessage());
        } finally {
            return _out;
        }

    }
    // endregion


    // region saveToHtml
    public String saveToHtml(long id) throws Exception {

        String _out = null;

        try {

            // region Lendo o objeto tpPedidoMobile de forma completa
            tpPedidoMobile _tp = this.loadOneById(id);

            if (_tp != null) {
                _out = _tp.Numero + ".html";
            } else {
                throw new Exception("dbPedidoMobile | saveToHtml -> Não foi possível ler o objeto pedido");
            }
            // endregion


            // region Invocando o metodo que escreve o JSON para sincronização
            StringBuilder _sbHtml = _tp.getHtml();
            // endregion


            // region Montando o nome do diretório
            String _msvsmart = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/msvsmart/html";
            // endregion


            // region Verificando se o diretorio existe
            File _dir = new File(_msvsmart);

            if (_dir.exists() == false){
                _dir.mkdirs();
            }
            // endregion


            // region Criando a representação do arquivo texto
            File _arq = new File(_msvsmart, _out);

            if (_arq.exists() == true) {
                _arq.delete();
            }

            _arq.createNewFile();
            // endregion


            // region Descarregando os dados do pedido em formato texto
            FileOutputStream _fos = new FileOutputStream(_arq);
            _fos.write(_sbHtml.toString().getBytes());
            _fos.close();
            // endregion

        } catch (Exception e) {
            throw new Exception("dbPedidoMobile | saveToHtml ->" + e.getMessage());
        } finally {
            return _out;
        }

    }
    // endregion


    // region clearOldOrder
    public void clearOldOrder() {

        // region Declarando variaveis locais
        Cursor _crs = null;
        StringBuilder _sb = null;
        long _idCliente = 0;
        long _idPedidoMobile = 0;
        long _aux = 0;
        // endregion

        // region Formatando a senteça para seleção de dados no banco
        _sb = new StringBuilder();

        _sb.append("   SELECT IdCliente, IdPedidoMobile ");
        _sb.append("     FROM PedidoMobile ");
        _sb.append(" ORDER BY IdCliente ASC, _id DESC ");
        // endregion

        // region Bloco protegido
        try {

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {

                    _idCliente = _crs.getLong(_crs.getColumnIndexOrThrow("IdCliente"));
                    _idPedidoMobile = _crs.getLong(_crs.getColumnIndexOrThrow("IdPedidoMobile"));

                    if (_aux != _idCliente) {
                        _aux = _idCliente;
                    } else {
                        this.executeWithOutResult("DELETE FROM PedidoMobileItem WHERE IdPedidoMobile = " + String.valueOf(_idPedidoMobile));
                        this.executeWithOutResult("DELETE FROM PedidoMobile WHERE IdPedidoMobile = " + String.valueOf(_idPedidoMobile));
                    }

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if ((_crs != null) && (_crs.isClosed() == false)) {
                _crs.close();
            }

        }
        // endregion

    }
    // endregion


    // region saveHistory
    public void saveHistory(long id) throws Exception {

        StringBuilder _sb = null;
        Cursor _crs = null;
        tpPedidoMobile _tpOrigem = null;
        tpPedidoMobileHistorico _tpDestino = null;
        dbPedidoMobileHistorico _dbPedidoMobileHistorico = null;
        dbPedidoMobileItemHistorico _dbPedidoMobileItemHistorico = null;
        int _idPedidoMobileHistorico = 0;
        int _quantidade = 0;


        try {

            // region Lendo as informações do pedido
            _tpOrigem = this.loadOneById(id);
            // endregion


            // region Verificando quantos pedidos existem para manter sempre os 2 últimos
            _sb = new StringBuilder();
            _sb.append(" SELECT IdPedidoMobileHistorico ");
            _sb.append("   FROM PedidoMobileHistorico ");
            _sb.append("  WHERE IdEmpresa = " + String.valueOf(_tpOrigem.IdEmpresa));
            _sb.append("    AND IdCliente = " + String.valueOf(_tpOrigem.IdCliente));

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {
                    _quantidade += 1;

                    if (_quantidade >= 4) {
                        _idPedidoMobileHistorico = _crs.getInt(_crs.getColumnIndexOrThrow("IdPedidoMobileHistorico"));

                        this.executeWithOutResult("DELETE FROM PedidoMobileItemHistorico WHERE IdPedidoMobileHistorico = " + String.valueOf(_idPedidoMobileHistorico));
                        this.executeWithOutResult("DELETE FROM PedidoMobileHistorico WHERE IdPedidoMobileHistorico = " + String.valueOf(_idPedidoMobileHistorico));
                    }
                }
            }

            if ((_crs != null) && (_crs.isClosed() == false)) {
                _crs.close();
            }
            // endregion


            // region Clonado as informações do pedido

            // region Clonando o registro mestre do pedido
            _tpDestino = new tpPedidoMobileHistorico();
            _tpDestino.IdPedidoMobileHistorico = _tpOrigem.IdPedidoMobile;
            _tpDestino.IdEmpresa = _tpOrigem.IdEmpresa;
            _tpDestino.IdTipoPedido = _tpOrigem.IdTipoPedido;
            _tpDestino.IdCliente = _tpOrigem.IdCliente;
            _tpDestino.Numero = _tpOrigem.Numero;
            _tpDestino.NumeroCliente = _tpOrigem.NumeroCliente;
            _tpDestino.EmissaoDataHora = _tpOrigem.EmissaoDataHora;
            _tpDestino.IdTabelaPreco = _tpOrigem.IdTabelaPreco;
            _tpDestino.IdCondicaoPagamento = _tpOrigem.IdCondicaoPagamento;
            _tpDestino.IdTransportadora = _tpOrigem.IdTransportadora;
            _tpDestino.Observacao = _tpOrigem.Observacao;
            _tpDestino.ItensQuantidade = _tpOrigem.ItensQuantidade;
            _tpDestino.TotalValor = _tpOrigem.TotalValor;
            _tpDestino.DescontoPercentual1 = _tpOrigem.DescontoPercentual1;
            _tpDestino.DescontoValor1 = _tpOrigem.DescontoValor1;
            _tpDestino.TotalValorLiquido = _tpOrigem.TotalValorLiquido;
            _tpDestino.IdVendedor = _tpOrigem.IdVendedor;
            _tpDestino.Empresa = _tpOrigem.Empresa.Sigla;
            _tpDestino.TipoPedido = _tpOrigem.TipoPedido.Descricao;
            _tpDestino.ClienteCodigo = _tpOrigem.Cliente.Codigo;
            _tpDestino.ClienteRazaoSocial = _tpOrigem.Cliente.RazaoSocial;
            _tpDestino.ClienteNomeFantasia = _tpOrigem.Cliente.NomeFantasia;
            _tpDestino.TabelaPreco = _tpOrigem.TabelaPreco.Descricao;
            _tpDestino.CondicaoPagamento = _tpOrigem.CondicaoPagamento.Descricao;
            _tpDestino.Transportadora = _tpOrigem.Transportadora.Descricao;
            // endregion

            // region Clonando os itens do pedido
            for (tpPedidoMobileItem _tpItemOrigem : _tpOrigem.Itens) {
                tpPedidoMobileItemHistorico _tpItemDestino = new tpPedidoMobileItemHistorico();
                _tpItemDestino.IdPedidoMobileItemHistorico = _tpItemOrigem.IdPedidoMobileItem;
                _tpItemDestino.IdPedidoMobileHistorico = _tpItemOrigem.IdPedidoMobile;
                _tpItemDestino.IdProduto = _tpItemOrigem.IdProduto;
                _tpItemDestino.PackQuantidade = _tpItemOrigem.PackQuantidade;
                _tpItemDestino.UnidadeValor = _tpItemOrigem.UnidadeValor;
                _tpItemDestino.UnidadeDescontoPercentual = _tpItemOrigem.UnidadeDescontoPercentual;
                _tpItemDestino.UnidadeDescontoValor = _tpItemOrigem.UnidadeDescontoValor;
                _tpItemDestino.UnidadeValorLiquido = _tpItemOrigem.UnidadeValorLiquido;
                _tpItemDestino.UnidadeVendaQuantidade = _tpItemOrigem.UnidadeVendaQuantidade;
                _tpItemDestino.UnidadeValorTotal = _tpItemOrigem.UnidadeValorTotal;
                _tpItemDestino.Observacao = _tpItemOrigem.Observacao;
                _tpItemDestino.ProdutoCodigo = _tpItemOrigem.Produto.Codigo;
                _tpItemDestino.ProdutoDescricao = _tpItemOrigem.Produto.Descricao;
                _tpItemDestino.UnidadeMedida = _tpItemOrigem.Produto.UnidadeMedida;


                if (_tpDestino.Itens == null) {
                    _tpDestino.Itens = new ArrayList<tpPedidoMobileItemHistorico>();
                }

                _tpDestino.Itens.add(_tpItemDestino);
            }
            // endregion

            // region Gravando os registros no banco de dados
            _dbPedidoMobileHistorico = new dbPedidoMobileHistorico(_dbHelper);
            _dbPedidoMobileHistorico.insert(_tpDestino);

            _dbPedidoMobileItemHistorico = new dbPedidoMobileItemHistorico(_dbHelper);

            for (tpPedidoMobileItemHistorico _item : _tpDestino.Itens) {
                _dbPedidoMobileItemHistorico.insert(_item);
            }
            // endregion

            // endregion

        } catch (Exception e) {
            throw new Exception("dbPedidoMobile | saveHistory() | " + e.getMessage());
        }

    }
    // endregion


    // region getIdSincronizados
    public ArrayList<tpKeyValueRow> getIdSincronizados() throws Exception {

        // region Declarando a variável de retorno
        ArrayList<tpKeyValueRow> _out = null;
        // endregion

        // region Declarando variáveis locais do método
        Cursor _crs = null;
        StringBuilder _sb = null;
        // endregion

        // region Formatando a senteça para seleção de dados no banco
        _sb = new StringBuilder();

        _sb.append(" SELECT _id ");
        _sb.append("   FROM PedidoMobile ");
        _sb.append("  WHERE EhSincronizado = 1 ");
        // endregion

        // region Bloco protegido
        try {

            // region Executando a consulta no banco de dados
            _crs = this.executeWithResult(_sb.toString());
            // endregion

            // region Se existir resultado então vamos formar a lista de retorno
            if (_crs != null) {

                while (_crs.moveToNext()) {

                    tpKeyValueRow _tp = new tpKeyValueRow();
                    _tp.Key = _crs.getLong(_crs.getColumnIndexOrThrow("_id"));

                    if (_out == null) {
                        _out = new ArrayList<tpKeyValueRow>();
                    }

                    _out.add(_tp);

                }

            }
            // endregion

        } catch (Exception e) {

            throw new Exception("dbPedidoMobile | getIdSincronizados() | " + e.getMessage());

        } finally {

            if ((_crs != null) && (_crs.isClosed() == false)) {
                _crs.close();
            }

        }
        // endregion

        return _out;
    }
    // endregion
}
