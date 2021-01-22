package br.com.microserv.framework.msvdal;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpProdutoPrecoRow;
import br.com.microserv.framework.msvdto.tpProdutoSearch;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.OrderByItem;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.WhereItem;
import br.com.microserv.framework.msvutil.eSQLConditionType;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.framework.msvutil.eSQLiteDataType;

/**
 * Created by notemsv01 on 17/11/2016.
 */

public class dbProduto extends dbBase implements dbInterface {

    // region Constructor
    public dbProduto(SQLiteHelper dbHelper) throws Exception{
        super(dbHelper);
    }
    // endregion


    // region newDTO
    @Override
    public tpInterface newDTO(){

        return new tpProduto();

    }
    // endregion


    // region getList
    public List<tpProduto> getList(long idEmpresa) {

        // region Declarando variaveis locais
        List<tpProduto> _out = null;
        Cursor _crs = null;
        StringBuilder _sb = null;
        // endregion

        // region Formatando a senteça para seleção de dados no banco
        _sb = new StringBuilder();

        _sb.append("     SELECT * ");
        _sb.append("       FROM Produto        AS Pro ");
        _sb.append(" INNER JOIN ProdutoEmpresa AS Pem ON Pro.IdProduto = Pem.IdProduto ");
        _sb.append("      WHERE Pem.IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("      ORDER BY Pro.Descricao");
        // endregion

        // region Bloco protegido
        try {

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {

                    tpProduto _tp = new tpProduto();

                    _tp._id = _crs.getLong(_crs.getColumnIndexOrThrow("_id"));
                    _tp.IdGrupo = _crs.getLong(_crs.getColumnIndexOrThrow("IdGrupo"));
                    _tp.IdProduto = _crs.getLong(_crs.getColumnIndexOrThrow("IdProduto"));
                    _tp.Codigo = _crs.getString(_crs.getColumnIndexOrThrow("Codigo"));
                    _tp.Ean13 = _crs.getString(_crs.getColumnIndexOrThrow("Ean13"));
                    _tp.Descricao = _crs.getString(_crs.getColumnIndexOrThrow("Descricao"));
                    _tp.UnidadeMedida = _crs.getString(_crs.getColumnIndexOrThrow("UnidadeMedida"));
                    _tp.PackQuantidade = _crs.getInt(_crs.getColumnIndexOrThrow("PackQuantidade"));
                    _tp.VendaQuantidadeMinima = _crs.getInt(_crs.getColumnIndexOrThrow("VendaQuantidadeMinima"));
                    _tp.VendaMultiploDe = _crs.getInt(_crs.getColumnIndexOrThrow("VendaMultiploDe"));
                    _tp.PesoBruto = _crs.getDouble(_crs.getColumnIndexOrThrow("PesoBruto"));
                    _tp.PermiteDecimal = _crs.getInt(_crs.getColumnIndexOrThrow("PermiteDecimal"));

                    if (_out == null) {
                        _out = new ArrayList<tpProduto>();
                    }

                    _out.add(_tp);
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

        // region Final
        return _out;
        // endregion

    }
    // endregion


    // region getListByIdGrupo
    public List<tpProduto> getListByIdGrupo(long idEmpresa, long idGrupo) {

        // region Declarando variaveis locais
        List<tpProduto> _out = null;
        Cursor _crs = null;
        StringBuilder _sb = null;
        // endregion

        // region Formatando a senteça para seleção de dados no banco
        _sb = new StringBuilder();

        _sb.append("     SELECT * ");
        _sb.append("       FROM Produto        AS Pro ");
        _sb.append(" INNER JOIN ProdutoEmpresa AS Pem ON Pro.IdProduto = Pem.IdProduto ");
        _sb.append("      WHERE Pem.IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("        AND Pro.IdGrupo = " + String.valueOf(idGrupo));
        _sb.append("      ORDER BY Pro.Descricao");
        // endregion

        // region Bloco protegido
        try {

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {

                    tpProduto _tp = new tpProduto();

                    _tp._id = _crs.getLong(_crs.getColumnIndexOrThrow("_id"));
                    _tp.IdGrupo = _crs.getLong(_crs.getColumnIndexOrThrow("IdGrupo"));
                    _tp.IdProduto = _crs.getLong(_crs.getColumnIndexOrThrow("IdProduto"));
                    _tp.Codigo = _crs.getString(_crs.getColumnIndexOrThrow("Codigo"));
                    _tp.Ean13 = _crs.getString(_crs.getColumnIndexOrThrow("Ean13"));
                    _tp.Descricao = _crs.getString(_crs.getColumnIndexOrThrow("Descricao"));
                    _tp.UnidadeMedida = _crs.getString(_crs.getColumnIndexOrThrow("UnidadeMedida"));
                    _tp.PackQuantidade = _crs.getInt(_crs.getColumnIndexOrThrow("PackQuantidade"));
                    _tp.VendaQuantidadeMinima = _crs.getInt(_crs.getColumnIndexOrThrow("VendaQuantidadeMinima"));
                    _tp.VendaMultiploDe = _crs.getInt(_crs.getColumnIndexOrThrow("VendaMultiploDe"));
                    _tp.PesoBruto = _crs.getDouble(_crs.getColumnIndexOrThrow("PesoBruto"));
                    _tp.PermiteDecimal = _crs.getInt(_crs.getColumnIndexOrThrow("PermiteDecimal"));

                    if (_out == null) {
                        _out = new ArrayList<tpProduto>();
                    }

                    _out.add(_tp);
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

        // region Final
        return _out;
        // endregion

    }
    // endregion


    // region getPreco
    public List<tpProdutoPrecoRow> getPreco(long idProduto) throws Exception {

        List<tpProdutoPrecoRow> _out = null;
        Cursor _c = null;
        StringBuilder _sb = new StringBuilder();


        _sb.append("    SELECT Tpp._id, ");
        _sb.append("           Emp.Sigla          AS EmpresaSigla, ");
        _sb.append("           Tpr.Descricao      AS TabelaPrecoDescricao, ");
        _sb.append("           Tpp.Preco          AS ProdutoPreco ");
        _sb.append("      FROM TabelaPrecoProduto AS Tpp ");
        _sb.append("INNER JOIN TabelaPreco        AS Tpr ON Tpp.IdTabelaPreco = Tpr.IdTabelaPreco ");
        _sb.append("INNER JOIN Empresa            AS Emp ON Tpr.IdEmpresa     = Emp.IdEmpresa ");
        _sb.append("     WHERE Tpp.IdProduto = " + String.valueOf(idProduto));
        _sb.append("  ORDER BY Tpp.Preco ");


        try {

            _c = this.executeWithResult(_sb.toString());

            if (_c != null){

                while(_c.moveToNext()) {


                    tpProdutoPrecoRow _tp = new tpProdutoPrecoRow();

                    _tp._id = _c.getLong(_c.getColumnIndexOrThrow("_id"));
                    _tp.EmpresaSigla = _c.getString(_c.getColumnIndexOrThrow("EmpresaSigla"));
                    _tp.TabelaPrecoDescricao = _c.getString(_c.getColumnIndexOrThrow("TabelaPrecoDescricao"));
                    _tp.ProdutoPreco = _c.getDouble(_c.getColumnIndexOrThrow("ProdutoPreco"));

                    if (_out == null) {
                        _out = new ArrayList<tpProdutoPrecoRow>();
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


    // region search
    public List<tpProdutoSearch> search(long idEmpresa, long idTabelaPreco, String Descricao, int _IdGrupo, String _LetraInicial) {

        // region Declarando variaveis locais
        List<tpProdutoSearch> _out = null;
        Cursor _crs = null;
        StringBuilder _sb = null;
        // endregion


        // region Preparando o argumento de pesquisa por descrição
        Descricao = "%" + Descricao.replace(' ', '%') + "%";
        // endregion


        // region Formatando a senteça para seleção de dados no banco
        _sb = new StringBuilder();

        _sb.append("     SELECT Pro._id, Pro.IdProduto, Pro.Codigo, Pro.Descricao, Pro.Ean13, Pro.UnidadeMedida, Lin.Descricao AS Linha, Gru.Descricao AS Grupo, Tpp.Preco ");
        _sb.append("       FROM Produto            AS Pro ");
        _sb.append(" INNER JOIN ProdutoEmpresa     AS Pem ON Pro.IdProduto = Pem.IdProduto ");
        _sb.append(" INNER JOIN Grupo              AS Gru ON Pro.IdGrupo   = Gru.IdGrupo ");
        _sb.append(" INNER JOIN Linha              AS Lin ON Gru.IdLinha   = Lin.IdLinha ");
        _sb.append(" INNER JOIN TabelaPrecoProduto AS Tpp ON Pro.IdProduto = Tpp.IdProduto ");
        _sb.append("      WHERE Pro.Descricao LIKE '" + Descricao + "' ");
        _sb.append(" 	    AND Pem.IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("        AND Tpp.IdTabelaPreco = " + String.valueOf(idTabelaPreco));

        if(_LetraInicial != null && _LetraInicial != "")
        {
            _sb.append("        AND Pro.Descricao LIKE '" + _LetraInicial + "%'");
        }

        if(_IdGrupo > 0)
        {
            _sb.append("        AND Gru.IdGrupo = " + String.valueOf(_IdGrupo));
        }

        _sb.append("      ORDER BY Pro.Descricao");
        // endregion

        // region Bloco protegido
        try {

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {

                    tpProdutoSearch _tp = new tpProdutoSearch();

                    _tp._id = _crs.getLong(_crs.getColumnIndexOrThrow("_id"));
                    _tp.IdProduto = _crs.getLong(_crs.getColumnIndexOrThrow("IdProduto"));
                    _tp.Codigo = _crs.getString(_crs.getColumnIndexOrThrow("Codigo"));
                    _tp.Descricao = _crs.getString(_crs.getColumnIndexOrThrow("Descricao"));
                    _tp.Ean13 = _crs.getString(_crs.getColumnIndexOrThrow("Ean13"));
                    _tp.UnidadeMedida = _crs.getString(_crs.getColumnIndexOrThrow("UnidadeMedida"));
                    _tp.Linha = _crs.getString(_crs.getColumnIndexOrThrow("Linha"));
                    _tp.Grupo = _crs.getString(_crs.getColumnIndexOrThrow("Grupo"));
                    _tp.Preco = _crs.getDouble(_crs.getColumnIndexOrThrow("Preco"));

                    if (_out == null) {
                        _out = new ArrayList<tpProdutoSearch>();
                    }

                    _out.add(_tp);
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

        // region Final
        return _out;
        // endregion

    }
    // endregion


    // region search
    public List<tpProdutoSearch> searchByIdGrupo(long idEmpresa, long idTabelaPreco, int _IdGrupo) {

        // region Declarando variaveis locais
        List<tpProdutoSearch> _out = null;
        Cursor _crs = null;
        StringBuilder _sb = null;
        // endregion

        // region Formatando a senteça para seleção de dados no banco
        _sb = new StringBuilder();

        _sb.append("     SELECT Pro._id, Pro.IdProduto, Pro.Codigo, Pro.Descricao, Pro.Ean13, Pro.UnidadeMedida, Lin.Descricao AS Linha, Gru.Descricao AS Grupo, Tpp.Preco ");
        _sb.append("       FROM Produto            AS Pro ");
        _sb.append(" INNER JOIN ProdutoEmpresa     AS Pem ON Pro.IdProduto = Pem.IdProduto ");
        _sb.append(" INNER JOIN Grupo              AS Gru ON Pro.IdGrupo   = Gru.IdGrupo ");
        _sb.append(" INNER JOIN Linha              AS Lin ON Gru.IdLinha   = Lin.IdLinha ");
        _sb.append(" INNER JOIN TabelaPrecoProduto AS Tpp ON Pro.IdProduto = Tpp.IdProduto ");
        _sb.append("      WHERE Pem.IdEmpresa = " + String.valueOf(idEmpresa));
        _sb.append("        AND Tpp.IdTabelaPreco = " + String.valueOf(idTabelaPreco));
        _sb.append("        AND Gru.IdGrupo = " + String.valueOf(_IdGrupo));
        _sb.append("      ORDER BY Pro.Descricao");
        // endregion

        // region Bloco protegido
        try {

            _crs = this.executeWithResult(_sb.toString());

            if (_crs != null) {

                while (_crs.moveToNext()) {

                    tpProdutoSearch _tp = new tpProdutoSearch();

                    _tp._id = _crs.getLong(_crs.getColumnIndexOrThrow("_id"));
                    _tp.IdProduto = _crs.getLong(_crs.getColumnIndexOrThrow("IdProduto"));
                    _tp.Codigo = _crs.getString(_crs.getColumnIndexOrThrow("Codigo"));
                    _tp.Descricao = _crs.getString(_crs.getColumnIndexOrThrow("Descricao"));
                    _tp.Ean13 = _crs.getString(_crs.getColumnIndexOrThrow("Ean13"));
                    _tp.UnidadeMedida = _crs.getString(_crs.getColumnIndexOrThrow("UnidadeMedida"));
                    _tp.Linha = _crs.getString(_crs.getColumnIndexOrThrow("Linha"));
                    _tp.Grupo = _crs.getString(_crs.getColumnIndexOrThrow("Grupo"));
                    _tp.Preco = _crs.getDouble(_crs.getColumnIndexOrThrow("Preco"));

                    if (_out == null) {
                        _out = new ArrayList<tpProdutoSearch>();
                    }

                    _out.add(_tp);
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

        // region Final
        return _out;
        // endregion

    }
    // endregion

}
