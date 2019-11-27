package br.com.microserv.framework.msvdal;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpClienteListaRow;
import br.com.microserv.framework.msvdto.tpClienteProdutoMixRow;
import br.com.microserv.framework.msvdto.tpFinanceiro;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpRegiao;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.SQLClauseHelper;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class dbCliente extends dbBase implements dbInterface {

    SQLiteHelper _dbHelper = null;


    // region dbCliente (constructor)
    public dbCliente(SQLiteHelper dbHelper) throws Exception {

        super(dbHelper);
        _dbHelper = dbHelper;

    }
    // endregion


    // region newDTO
    @Override
    public tpInterface newDTO(){

        return new tpCliente();

    }
    // endregion


    // region method loadDependencies
    @Override
    public void loadDepencies(tpInterface tp) throws  Exception{

        // region Declarando as variáveis locais do método
        tpCliente _tpCliente = (tpCliente) tp;

        dbCidade _dbCidade = null;
        dbRegiao _dbRegiao = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Buscando as informações da Cidade
            _dbCidade = new dbCidade(_dbHelper);
            _tpCliente.Cidade = (tpCidade) _dbCidade.getBySourceId(_tpCliente.IdCidade);
            // endregion

            // region Buscando as informações da Região
            _dbRegiao = new dbRegiao(_dbHelper);
            _tpCliente.Regiao = (tpRegiao) _dbRegiao.getBySourceId(_tpCliente.IdRegiao);
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


    // region getAllCliente
    public List<tpCliente> getAllCliente() {

        List<tpCliente> _out = null;
        Cursor _c = null;
        StringBuilder _sb = new StringBuilder();


        _sb.append(" SELECT * ");
        _sb.append("   FROM Cliente");


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                while(_c.moveToNext()) {

                    tpCliente _tp = new tpCliente();

                    _tp._id = _c.getLong(_c.getColumnIndexOrThrow("_id"));
                    _tp.IdCliente = _c.getInt(_c.getColumnIndexOrThrow("IdCliente"));
                    _tp.Codigo = _c.getString(_c.getColumnIndexOrThrow("Codigo"));
                    _tp.RazaoSocial = _c.getString(_c.getColumnIndexOrThrow("RazaoSocial"));

                    if (_out == null) {
                        _out = new ArrayList<tpCliente>();
                    }

                    _out.add(_tp);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;

    }
    // endregion


    // region getForClienteLista
    public List<tpClienteListaRow> getForClienteLista(long idCidade){

        List<tpClienteListaRow> _out = new ArrayList<tpClienteListaRow>();
        Cursor _c = null;

        StringBuilder _sb = new StringBuilder();
        _sb.append("     SELECT Cli._id, ");
        _sb.append("            Cli.IdCliente, ");
        _sb.append("            Cli.Codigo, ");
        _sb.append("            Cli.RazaoSocial, ");
        _sb.append("            Cli.NomeFantasia, ");
        _sb.append("            Cid.Descricao AS CidadeNome, ");
        _sb.append("            Cid.EstadoSigla, ");
        _sb.append("            Cli.Cep, ");
        _sb.append("                ( ");
        _sb.append("                  SELECT COUNT(1) ");
        _sb.append("                    FROM PedidoMobile AS Pmo ");
        _sb.append("                   WHERE Pmo.IdCliente = Cli.IdCliente ");
        _sb.append("                ) AS PedidoQuantidade, ");
        _sb.append("                ( ");
        _sb.append("                  SELECT COUNT(1) ");
        _sb.append("                    FROM PedidoMobileHistorico AS Phi ");
        _sb.append("                   WHERE Phi.IdCliente = Cli.IdCliente ");
        _sb.append("                ) AS HistoricoQuantidade ");
        _sb.append("       FROM Cliente AS Cli ");
        _sb.append(" INNER JOIN Cidade  AS Cid ON Cli.IdCidade = Cid.IdCidade ");
        _sb.append("      WHERE Cli.IdCidade = " + String.valueOf(idCidade));
        _sb.append("   ORDER BY Cli.NomeFantasia ");

        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                while(_c.moveToNext()) {

                    tpClienteListaRow _tp = new tpClienteListaRow();

                    _tp._id = _c.getLong(_c.getColumnIndexOrThrow("_id"));
                    _tp.IdCliente = _c.getLong(_c.getColumnIndexOrThrow("IdCliente"));
                    _tp.Codigo = _c.getString(_c.getColumnIndexOrThrow("Codigo"));
                    _tp.RazaoSocial = _c.getString(_c.getColumnIndexOrThrow("RazaoSocial"));
                    _tp.NomeFantasia = _c.getString(_c.getColumnIndexOrThrow("NomeFantasia"));
                    _tp.CidadeNome = _c.getString(_c.getColumnIndexOrThrow("CidadeNome"));
                    _tp.EstadoSigla = _c.getString(_c.getColumnIndexOrThrow("EstadoSigla"));
                    _tp.Cep = _c.getString(_c.getColumnIndexOrThrow("Cep"));
                    _tp.PedidoQuantidade = _c.getInt(_c.getColumnIndexOrThrow("PedidoQuantidade"));
                    _tp.HistoricoQuantidade = _c.getInt(_c.getColumnIndexOrThrow("HistoricoQuantidade"));

                    _out.add(_tp);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }

        return _out;
    }
    // endregion


    // region Métodos relacionados a questão FINANCEIRA

    // region parcelasTotal
    public double parcelasTotal(long idEmpresa, long idCliente) {

        double _out = 0;
        Cursor _c = null;

        StringBuilder _sb = new StringBuilder();
        _sb.append(" SELECT SUM( Valor ) AS Valor ");
        _sb.append("   FROM Financeiro ");
        _sb.append("  WHERE IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("    AND IdCliente = " + String.valueOf(idCliente));

        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                _c.moveToFirst();
                _out = _c.getDouble(_c.getColumnIndexOrThrow("Valor"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;

    }
    // endregion


    // region parcelasQuantidade
    public int parcelasQuantidade(long idEmpresa, long idCliente) {

        int _out = 0;
        Cursor _c = null;

        StringBuilder _sb = new StringBuilder();
        _sb.append(" SELECT COUNT( _id ) AS Quantidade ");
        _sb.append("   FROM Financeiro ");
        _sb.append("  WHERE IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("    AND IdCliente = " + String.valueOf(idCliente));


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                _c.moveToFirst();
                _out = _c.getInt(_c.getColumnIndexOrThrow("Quantidade"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;

    }
    // endregion


    // region parcelasTotalVencido
    public double parcelasTotalVencido(long idEmpresa, long idCliente, String ymd) {

        double _out = 0;
        Cursor _c = null;

        StringBuilder _sb = new StringBuilder();
        _sb.append(" SELECT SUM( Valor ) AS Valor ");
        _sb.append("   FROM Financeiro ");
        _sb.append("  WHERE IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("    AND IdCliente = " + String.valueOf(idCliente));
        _sb.append("    AND DATE( VencimentoData ) < '" + ymd + "'");


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                _c.moveToFirst();
                _out = _c.getDouble(_c.getColumnIndexOrThrow("Valor"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;

    }
    // endregion


    // region parcelasQuantidadeVencida
    public int parcelasQuantidadeVencida(long idEmpresa, long idCliente, String ymd) {

        int _out = 0;
        Cursor _c = null;

        StringBuilder _sb = new StringBuilder();
        _sb.append(" SELECT COUNT( _id ) AS Quantidade ");
        _sb.append("   FROM Financeiro ");
        _sb.append("  WHERE IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("    AND IdCliente = " + String.valueOf(idCliente));
        _sb.append("    AND DATE( VencimentoData ) < '" + ymd + "'");


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                _c.moveToFirst();
                _out = _c.getInt(_c.getColumnIndexOrThrow("Quantidade"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;

    }
    // endregion


    // region parcelas
    public List<tpFinanceiro> parcelas(long idEmpresa, long idCliente) {

        List<tpFinanceiro> _out = null;
        Cursor _c = null;
        StringBuilder _sb = new StringBuilder();


        _sb.append("   SELECT _id, ");
        _sb.append("          IdCliente, ");
        _sb.append("          Numero, ");
        _sb.append("          Parcela, ");
        _sb.append("          Valor, ");
        _sb.append("          EmissaoData, ");
        _sb.append("          VencimentoData ");
        _sb.append("     FROM Financeiro ");
        _sb.append("    WHERE IdCliente = " + String.valueOf(idCliente));
        _sb.append("      AND IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append(" ORDER BY VencimentoData DESC ");


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                while(_c.moveToNext()) {

                    tpFinanceiro _tp = new tpFinanceiro();

                    _tp._id = _c.getLong(_c.getColumnIndexOrThrow("_id"));
                    _tp.IdCliente = _c.getInt(_c.getColumnIndexOrThrow("IdCliente"));
                    _tp.Numero = _c.getString(_c.getColumnIndexOrThrow("Numero"));
                    _tp.Parcela = _c.getString(_c.getColumnIndexOrThrow("Parcela"));
                    _tp.Valor = _c.getDouble(_c.getColumnIndexOrThrow("Valor"));
                    _tp.EmissaoData = _c.getString(_c.getColumnIndexOrThrow("EmissaoData"));
                    _tp.VencimentoData = _c.getString(_c.getColumnIndexOrThrow("VencimentoData"));

                    if (_out == null) {
                        _out = new ArrayList<tpFinanceiro>();
                    }

                    _out.add(_tp);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;

    }
    // endregion

    // endregion


    // region Métodos relacionados ao MIX de PRODUTOS

    // region getProdutoMix
    public List<tpClienteProdutoMixRow> getProdutoMix(long idCliente) {

        List<tpClienteProdutoMixRow> _out = null;
        Cursor _c = null;
        StringBuilder _sb = new StringBuilder();


        _sb.append("    SELECT Mix._id, ");
        _sb.append("           Mix.IdCliente, ");
        _sb.append("           Mix.IdProduto, ");
        _sb.append("           Pro.Codigo        AS ProdutoCodigo, ");
        _sb.append("           Pro.Ean13         AS ProdutoEan13, ");
        _sb.append("           Pro.Descricao     AS ProdutoDescricao, ");
        _sb.append("           Pro.UnidadeMedida AS ProdutoUnidadeMedida, ");
        _sb.append("           Gru.Descricao     AS GrupoDescricao, ");
        _sb.append("           Lin.Descricao     AS LinhaDescricao, ");
        _sb.append("           Mix.CompraQuantidadeVezes, ");
        _sb.append("           Mix.CompraQuantidadeMaior, ");
        _sb.append("           Mix.UltimaCompraData, ");
        _sb.append("           Mix.UltimaCompraQuantidade, ");
        _sb.append("           Mix.UltimaCompraValor, ");
        _sb.append("           Tpp.Preco, ");
        _sb.append("           Mix.EstoqueQuantidade, ");
        _sb.append("           Mix.EstoqueDataHora, ");
        _sb.append("           Mix.ComprarQuantidade ");
        _sb.append("      FROM ClienteProdutoMix  AS Mix ");
        _sb.append("INNER JOIN Cliente            AS Cli ON Mix.IdCliente     = Cli.IdCliente ");
        _sb.append("INNER JOIN Produto            AS Pro ON Mix.IdProduto     = Pro.IdProduto ");
        _sb.append("INNER JOIN Grupo              AS Gru ON Pro.IdGrupo       = Gru.IdGrupo ");
        _sb.append("INNER JOIN Linha              AS Lin ON Gru.IdLinha       = Lin.IdLinha ");
        _sb.append("INNER JOIN TabelaPrecoProduto AS Tpp ON Cli.IdTabelaPreco = Tpp.IdTabelaPreco ");
        _sb.append("                                    AND Pro.IdProduto     = Tpp.IdProduto ");
        _sb.append("     WHERE Mix.IdCliente = " + String.valueOf(idCliente));
        _sb.append("  ORDER BY Gru.Descricao, Pro.Descricao ");


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                while(_c.moveToNext()) {

                    tpClienteProdutoMixRow _tp = new tpClienteProdutoMixRow();

                    _tp._id = _c.getLong(_c.getColumnIndexOrThrow("_id"));
                    _tp.IdCliente = _c.getLong(_c.getColumnIndexOrThrow("IdCliente"));
                    _tp.IdProduto = _c.getLong(_c.getColumnIndexOrThrow("IdProduto"));
                    _tp.ProdutoCodigo = _c.getString(_c.getColumnIndexOrThrow("ProdutoCodigo"));;
                    _tp.ProdutoEan13 = _c.getString(_c.getColumnIndexOrThrow("ProdutoEan13"));
                    _tp.ProdutoDescricao = _c.getString(_c.getColumnIndexOrThrow("ProdutoDescricao"));
                    _tp.ProdutoUnidadeMedida = _c.getString(_c.getColumnIndexOrThrow("ProdutoUnidadeMedida"));
                    _tp.GrupoDescricao = _c.getString(_c.getColumnIndexOrThrow("GrupoDescricao"));
                    _tp.LinhaDescricao = _c.getString(_c.getColumnIndexOrThrow("LinhaDescricao"));
                    _tp.CompraQuantidadeVezes = _c.getInt(_c.getColumnIndexOrThrow("CompraQuantidadeVezes"));
                    _tp.CompraQuantidadeMaior = _c.getInt(_c.getColumnIndexOrThrow("CompraQuantidadeMaior"));
                    _tp.UltimaCompraData = _c.getLong(_c.getColumnIndexOrThrow("UltimaCompraData"));;
                    _tp.UltimaCompraQuantidade = _c.getInt(_c.getColumnIndexOrThrow("UltimaCompraQuantidade"));
                    _tp.UltimaCompraValor = _c.getDouble(_c.getColumnIndexOrThrow("UltimaCompraValor"));
                    _tp.Preco = _c.getDouble(_c.getColumnIndexOrThrow("Preco"));
                    _tp.EstoqueQuantidade = _c.getInt(_c.getColumnIndexOrThrow("EstoqueQuantidade"));
                    _tp.EstoqueDataHora = _c.getLong(_c.getColumnIndexOrThrow("EstoqueDataHora"));
                    _tp.ComprarQuantidade = _c.getInt(_c.getColumnIndexOrThrow("ComprarQuantidade"));

                    if (_out == null) {
                        _out = new ArrayList<tpClienteProdutoMixRow>();
                    }

                    _out.add(_tp);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;

    }
    // endregion


    // region getProdutoMixForPedido
    public List<tpPedidoMobileItem> getProdutoMixForPedido(long idCliente, long idTabelaPreco) throws Exception {

        List<tpPedidoMobileItem> _out = null;
        Cursor _c = null;
        StringBuilder _sb = new StringBuilder();


        _sb.append("    SELECT Mix._id, ");
        _sb.append("           Mix.IdCliente, ");
        _sb.append("           Mix.IdProduto, ");
        _sb.append("           Mix.ComprarQuantidade, ");
        _sb.append("           Pro.EmbalagemQuantidade, ");
        _sb.append("           Tpp.Preco ");
        _sb.append("      FROM ClienteProdutoMix  AS Mix ");
        _sb.append("INNER JOIN Produto            AS Pro ON Mix.IdProduto     = Pro.IdProduto ");
        _sb.append("INNER JOIN Grupo              AS Gru ON Pro.IdGrupo       = Gru.IdGrupo ");
        _sb.append("INNER JOIN TabelaPrecoProduto AS Tpp ON Pro.IdProduto     = Tpp.IdProduto ");
        _sb.append("INNER JOIN TabelaPreco        AS Tpr ON Tpp.IdTabelaPreco = Tpr.IdTabelaPreco ");
        _sb.append("     WHERE Mix.IdCliente       = " + String.valueOf(idCliente));
        _sb.append("       AND Mix.EstoqueDataHora > 0 ");
        _sb.append("       AND Tpr.IdTabelaPreco   = " + String.valueOf(idTabelaPreco));
        _sb.append("  ORDER BY Gru.Descricao, Pro.Descricao ");


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                while(_c.moveToNext()) {


                    tpPedidoMobileItem _tp = new tpPedidoMobileItem();

                    _tp._id = 0;
                    _tp.IdPedidoMobileItem = 0;
                    _tp.IdPedidoMobile = 0;
                    _tp.IdProduto = _c.getLong(_c.getColumnIndexOrThrow("IdProduto"));
                    _tp.PackQuantidade = _c.getInt(_c.getColumnIndexOrThrow("EmbalagemQuantidade"));
                    _tp.UnidadeValor = _c.getDouble(_c.getColumnIndexOrThrow("Preco"));
                    _tp.UnidadeDescontoPercentual = 0;
                    _tp.UnidadeDescontoValor = 0;
                    _tp.UnidadeValorLiquido = _tp.UnidadeValor;
                    _tp.UnidadeVendaQuantidade = _c.getInt(_c.getColumnIndexOrThrow("ComprarQuantidade"));
                    _tp.UnidadeValorTotal = _tp.UnidadeValorLiquido * _tp.UnidadeVendaQuantidade;

                    _tp.Produto = (tpProduto) (new dbProduto(_dbHelper)).getBySourceId(_tp.IdProduto);

                    if (_out == null) {
                        _out = new ArrayList<tpPedidoMobileItem>();
                    }

                    _out.add(_tp);

                }

            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
            //e.printStackTrace();
        } finally {
            if (_c != null && _c.isClosed() == false){
                _c.close();
            }
        }


        return _out;

    }
    // endregion

    // endregion

}
