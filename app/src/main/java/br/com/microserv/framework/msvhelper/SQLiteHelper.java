package br.com.microserv.framework.msvhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvannotation.APrimaryKey;
import br.com.microserv.framework.msvdto.tpPost;
import br.com.microserv.framework.msvinterface.DBHelperInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.ePostAction;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricardo on 11/11/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper implements DBHelperInterface {

    // region local variables
    private SQLiteDatabase _db = null;
    // endregion


    // region constructor
    public SQLiteHelper(Context context) {
        super(context, "microservpdv.db", null, 1);
    }
    // endregion


    // region metodos Override

    // region onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Atenção, este método recebe como parametro um objeto do tipo SQLiteDatabase que já
        // está conectado e aberto para o bampo "microservpdv" não fechar a conexão deste
        // objeto aqui neste método

        try {

            // recebendo a conexao do parametro de
            // entrada do método, assin não vamos invocar
            // este método recursivamente
            _db = db;

            // invocando o método para a criação da estrutura
            // de tabelas do projeto
            this.create();

        } catch (Exception e) {
            Log.e("SQLiteHelper", "onCreate -> " + e.getMessage());
            e.printStackTrace();
        } finally {

        }

    }
    // endregion

    // region onUpdate
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("DROP TABLE IF EXISTS Estado");
            db.execSQL("DROP TABLE IF EXISTS Regiao");
            db.execSQL("DROP TABLE IF EXISTS Ids");
            onCreate(db);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    // endregion

    // region open
    @Override
    public void open(boolean isWritable) {

        boolean canOpen = false;

        // aqui vamos verificar se podemos invocar o metodo
        // para abertura do banco de dados
        if (_db == null){
            canOpen = true;
        } else {
            if (_db.isOpen() == false){
                canOpen = true;
            }
        }

        if (canOpen) {
            _db = isWritable ? this.getWritableDatabase() : this.getReadableDatabase();
        }

    }
    // endregion

    // region close
    @Override
    public void close() {

        if (_db != null){

            if (_db.isOpen()){
                _db.close();
                _db = null;
            }

        }

    }
    // endregion

    // region isOpen
    @Override
    public boolean isOpen() {

        boolean _out = false;

        if (_db != null) {
            _out = _db.isOpen();
        }

        return _out;
    }
    // endregion

    // region initTransaction
    public void initTransaction() {

        if ((_db != null) && (_db.isOpen())) {
            _db.beginTransaction();
        }

    }
    // endregion

    // region commitTransaction
    public void commitTransaction() {

        if ((_db != null) && (_db.isOpen()) && (_db.inTransaction())) {
            _db.setTransactionSuccessful();
        }

    }
    // endregion

    // region rollbackTransaction
    public void endTransaction() {

        if ((_db != null) && (_db.isOpen()) && (_db.inTransaction())) {
            _db.endTransaction();
        }

    }
    // endregion

    // endregion


    // region metodos de manutenção do banco

    // region create
    public void create(){

        boolean _openHere = false;

        try{

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            String sql = null;

            // region SISTEMA

            // region Ids
            sql = "DROP TABLE IF EXISTS Ids";
            _db.execSQL(sql);

            sql = " CREATE TABLE Ids ( "
                    + "   _id    INTEGER NOT NULL PRIMARY KEY, "
                    + "   Tabela TEXT    NOT NULL, "
                    + "   Coluna TEXT    NOT NULL, "
                    + "   Valor  INTEGER NOT NULL  "
                    + " )";

            _db.execSQL(sql);
            // endregion

            // region Parametro
            sql = "DROP TABLE IF EXISTS Parametro";
            _db.execSQL(sql);

            sql = "CREATE TABLE Parametro ( "
                    + "  _id          INTEGER NOT NULL PRIMARY KEY, "
                    + "  Nome         TEXT    NOT NULL COLLATE NOCASE, "
                    + "  ValorTexto   TEXT    NULL COLLATE NOCASE, "
                    + "  ValorInteiro INTEGER NULL, "
                    + "  ValorReal    REAL NULL "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region Sincronizacao
            sql = "DROP TABLE IF EXISTS Sincronizacao";
            _db.execSQL(sql);

            sql = "CREATE TABLE Sincronizacao ( "
                    + "  _id                 INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdVendedor          INTEGER NOT NULL, "
                    + "  InicioDataHora      TEXT    NOT NULL COLLATE NOCASE, "
                    + "  TerminoDataHora     TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Latitude            REAL    NOT NULL, "
                    + "  Longitude           REAL    NOT NULL "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // endregion

            // region EMPRESA

            // region Vendedor
            sql = "DROP TABLE IF EXISTS Vendedor";
            _db.execSQL(sql);

            sql = "CREATE TABLE Vendedor ( "
                    + "  _id          INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdVendedor   INTEGER NOT NULL, "
                    + "  Codigo       TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Nome         TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Senha        TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Regiao       TEXT    NOT NULL COLLATE NOCASE  "
                    + ") ";

            _db.execSQL(sql);
            //_db.execSQL("INSERT INTO Vendedor VALUES(1, 1, '901', 'RICARDO LUIS BERETTA E FELIPIN', '1234', 'TUPA')");
            // endregion

            // region Empresa
            sql = "DROP TABLE IF EXISTS Empresa";
            _db.execSQL(sql);

            sql = "CREATE TABLE Empresa ( "
                    + "  _id       INTEGER NOT NULL PRIMARY KEY    , "
                    + "  IdEmpresa INTEGER NOT NULL                , "
                    + "  Descricao TEXT    NOT NULL COLLATE NOCASE , "
                    + "  Sigla     TEXT    NOT NULL COLLATE NOCASE , "
                    + "  UNIQUE( IdEmpresa )                         "
                    + ") ";

            _db.execSQL(sql);
            //_db.execSQL("INSERT INTO Empresa VALUES(1, 1, 'DOCES MORENO', 'DM')");
            // endregion

            // endregion

            // region PRODUTO

            // region Linha
            sql = "DROP TABLE IF EXISTS Linha";
            _db.execSQL(sql);

            sql = "CREATE TABLE Linha ( "
                    + "  _id       INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdEmpresa INTEGER NOT NULL, "
                    + "  IdLinha   INTEGER NOT NULL, "
                    + "  Codigo    TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Descricao TEXT    NOT NULL COLLATE NOCASE "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region Grupo
            sql = "DROP TABLE IF EXISTS Grupo";
            _db.execSQL(sql);

            sql = "CREATE TABLE Grupo ( "
                    + "  _id       INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdEmpresa INTEGER NOT NULL, "
                    + "  IdLinha   INTEGER NOT NULL, "
                    + "  IdGrupo   INTEGER NOT NULL, "
                    + "  Codigo    TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Descricao TEXT    NOT NULL COLLATE NOCASE "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region PedidoTipo
            sql = "DROP TABLE IF EXISTS TipoPedido";
            _db.execSQL(sql);

            sql = "CREATE TABLE TipoPedido ( "
                    + "  _id           INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdEmpresa     INTEGER NOT NULL, "
                    + "  IdTipoPedido  INTEGER NOT NULL, "
                    + "  Descricao     TEXT    NOT NULL COLLATE NOCASE, "
                    + "  EhVenda       INTEGER NOT NULL, "
                    + "  EhTroca       INTEGER NOT NULL, "
                    + "  EhBonificacao INTEGER NOT NULL  "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region CondicaoPagamento
            sql = "DROP TABLE IF EXISTS CondicaoPagamento";
            _db.execSQL(sql);

            sql = "CREATE TABLE CondicaoPagamento ( "
                    + "  _id                 INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdEmpresa           INTEGER NOT NULL, "
                    + "  IdCondicaoPagamento INTEGER NOT NULL, "
                    + "  Descricao           TEXT    NOT NULL COLLATE NOCASE "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region TipoPedidoCondicaoPagamento
            sql = "DROP TABLE IF EXISTS TipoPedidoCondicaoPagamento";
            _db.execSQL(sql);

            sql = "CREATE TABLE TipoPedidoCondicaoPagamento ( "
                    + "  _id                 INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdEmpresa           INTEGER NOT NULL, "
                    + "  IdTipoPedido        INTEGER NOT NULL, "
                    + "  IdCondicaoPagamento INTEGER NOT NULL "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region Transportadora
            sql = "DROP TABLE IF EXISTS Transportadora";
            _db.execSQL(sql);

            sql = "CREATE TABLE Transportadora ( "
                    + "  _id               INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdTransportadora  INTEGER NOT NULL, "
                    + "  Descricao         TEXT    NOT NULL COLLATE NOCASE "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region TabelaPreco
            sql = "DROP TABLE IF EXISTS TabelaPreco";
            _db.execSQL(sql);

            sql = "CREATE TABLE TabelaPreco ( "
                    + "  _id           INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdTabelaPreco INTEGER NOT NULL, "
                    + "  IdEmpresa     INTEGER NOT NULL, "
                    + "  Descricao     TEXT    NOT NULL COLLATE NOCASE "
                    + ")";

            _db.execSQL(sql);
            //_db.execSQL("INSERT INTO TabelaPreco VALUES(1, 1, 1, 'SP - SAO PAULO', '1')");
            // endregion

            // region TabelaPrecoCondicaoPagamento (AINDA NAO EXISTE NO ERP)
            sql = "DROP TABLE IF EXISTS TabelaPrecoCondicaoPagamento";
            _db.execSQL(sql);

            sql = "CREATE TABLE TabelaPrecoCondicaoPagamento ( "
                    + "  _id                 INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdTabelaPreco       INTEGER NOT NULL, "
                    + "  IdCondicaoPagamento INTEGER NOT NULL "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // region Produto
            sql = "DROP TABLE IF EXISTS Produto";
            _db.execSQL(sql);

            sql = "CREATE TABLE Produto ( "
                    + "  _id                   INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdGrupo               INTEGER NOT NULL, "
                    + "  IdProduto             INTEGER NOT NULL, "
                    + "  Codigo                TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Ean13                 TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Descricao             TEXT    NOT NULL COLLATE NOCASE, "
                    + "  UnidadeMedida         TEXT    NOT NULL COLLATE NOCASE, "
                    + "  PermiteDecimal        INTEGER NOT NULL, "
                    + "  PackQuantidade        INTEGER NOT NULL, "
                    + "  VendaQuantidadeMinima INTEGER NOT NULL, "
                    + "  VendaMultiploDe       INTEGER NOT NULL, "
                    + "  PesoBruto             REAL    NOT NULL, "
                    + "  SaldoQuantidade       REAL    NOT NULL "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region ProdutoEmpresa
            sql = "DROP TABLE IF EXISTS ProdutoEmpresa";
            _db.execSQL(sql);

            sql = "CREATE TABLE ProdutoEmpresa ( "
                    + "  _id        INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdProduto  INTEGER NOT NULL, "
                    + "  IdEmpresa  INTEGER NOT NULL "
                    + ") ";

            _db.execSQL(sql);
            // endregion

            // region TabelaPrecoProduto
            // (VEM INCLUIDO DENTRO DE PRODUTO NA SINCRONIZACAO)
            sql = "DROP TABLE IF EXISTS TabelaPrecoProduto";
            _db.execSQL(sql);

            sql = "CREATE TABLE TabelaPrecoProduto ( "
                    + "  _id                  INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdTabelaPrecoProduto INTEGER NOT NULL, "
                    + "  IdTabelaPreco        INTEGER NOT NULL, "
                    + "  IdProduto            INTEGER NOT NULL, "
                    + "  PrecoMinimo          REAL    NOT NULL, "
                    + "  Preco                REAL    NOT NULL, "
                    + "  PrecoMaximo          REAL    NOT NULL "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // endregion

            // region CLIENTE

            // region Cidade
            sql = "DROP TABLE IF EXISTS Cidade";
            _db.execSQL(sql);

            sql = "CREATE TABLE Cidade ("
                    + " _id         INTEGER NOT NULL PRIMARY KEY, "
                    + " IdCidade    INTEGER NOT NULL, "
                    + " Descricao   TEXT    NOT NULL COLLATE NOCASE, "
                    + " EstadoSigla TEXT    NOT NULL COLLATE NOCASE "
                    + ") ";

            _db.execSQL(sql);
            //_db.execSQL("INSERT INTO Cidade VALUES(1, 1, 'TUPA', 'SP')");
            // endregion

            // region Regiao
            sql = "DROP TABLE IF EXISTS Regiao";
            _db.execSQL(sql);

            sql = "CREATE TABLE Regiao ("
                    + " _id           INTEGER NOT NULL PRIMARY KEY, "
                    + " IdRegiao      INTEGER NOT NULL, "
                    + " Descricao     TEXT    NOT NULL COLLATE NOCASE, "
                    + " AreaDescricao TEXT    NOT NULL COLLATE NOCASE "
                    + ") ";

            _db.execSQL(sql);
            //_db.execSQL("INSERT INTO Cidade VALUES(1, 1, 'TUPA', 'SP')");
            // endregion

            // region Cliente
            sql = "DROP TABLE IF EXISTS Cliente";
            _db.execSQL(sql);

            sql = "CREATE TABLE Cliente ( "
                    + "  _id                        INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdCliente                  INTEGER NOT NULL, "
                    + "  Codigo                     TEXT    NOT NULL COLLATE NOCASE, "
                    + "  RazaoSocial                TEXT    NOT NULL COLLATE NOCASE, "
                    + "  NomeFantasia               TEXT    NOT NULL COLLATE NOCASE, "
                    + "  CnpjCpf                    TEXT    NOT NULL COLLATE NOCASE, "
                    + "  IeRg                       TEXT    NOT NULL COLLATE NOCASE, "
                    + "  LogradouroTipo             TEXT    NOT NULL COLLATE NOCASE, "
                    + "  LogradouroNome             TEXT    NOT NULL COLLATE NOCASE, "
                    + "  LogradouroNumero           TEXT    NOT NULL COLLATE NOCASE, "
                    + "  BairroNome                 TEXT    NOT NULL COLLATE NOCASE, "
                    + "  IdCidade                   INTEGER NOT NULL, "
                    + "  Cep                        TEXT    NOT NULL COLLATE NOCASE, "
                    + "  TelefoneFixo               TEXT        NULL COLLATE NOCASE, "
                    + "  TelefoneCelular            TEXT        NULL COLLATE NOCASE, "
                    + "  Email                      TEXT        NULL COLLATE NOCASE, "
                    + "  ContatoNome                TEXT        NULL COLLATE NOCASE, "
                    + "  IdRegiao                   INTEGER NOT NULL, "
                    + "  IdTabelaPreco              INTEGER     NULL,"
                    + "  IdCondicaoPagamentoPadrao  INTEGER     NULL,"
                    + "  DescontoPadrao             REAL        NULL"
                    + ") ";

            _db.execSQL(sql);
            //_db.execSQL("INSERT INTO Cliente VALUES(1, 1, '0000001', 'CLIENTE DE TESTE 1', 'CLIENTE DE TESTE 2', '05754099000130', '697976789234', 'RUA', 'ESTADOS UNIDOS', '676', 'JARDIM AMERICA', 1, '17603-235', 'RLBFELIPIN@GMAIL.COM', 'RICARDO LUIS BERETTA E FELIPIN')");
            // endregion

            // region ClienteTabelaPreco
            // Vem junto com o json do cliente
            sql = "DROP TABLE IF EXISTS ClienteTabelaPreco";
            _db.execSQL(sql);

            sql = "CREATE TABLE ClienteTabelaPreco ( "
                    + "  _id               INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdEmpresa         INTEGER NOT NULL, "
                    + "  IdCliente         INTEGER NOT NULL, "
                    + "  IdTabelaPreco     INTEGER NOT NULL "
                    + ") ";

            _db.execSQL(sql);
            //_db.execSQL("INSERT INTO ClienteTabelaPreco VALUES(1, 1, 1, 1)");
            // endregion

            // region Financeiro
            // Vem junto com o json do cliente
            sql = "DROP TABLE IF EXISTS Financeiro";
            _db.execSQL(sql);

            sql = "CREATE TABLE Financeiro ( "
                    + " _id                 INTEGER NOT NULL PRIMARY KEY, "
                    + " IdEmpresa           INTEGER NOT NULL, "
                    + " IdDocumento         INTEGER NOT NULL, "
                    + " IdCliente           INTEGER NOT NULL, "
                    + " Numero              TEXT    NOT NULL, "
                    + " Parcela             TEXT    NOT NULL, "
                    + " Valor               REAL    NOT NULL, "
                    + " EmissaoData         TEXT    NOT NULL COLLATE NOCASE, "
                    + " VencimentoData      TEXT    NOT NULL COLLATE NOCASE "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // region ClienteProdutoMix
            // Vem junto com o json do cliente
            sql = "DROP TABLE IF EXISTS ClienteMix";
            _db.execSQL(sql);

            sql = "CREATE TABLE ClienteMix ( "
                    + "  _id                    INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdEmpresa              INTEGER NOT NULL, "
                    + "  IdCliente              INTEGER NOT NULL, "
                    + "  IdProduto              INTEGER NOT NULL, "
                    + "  CompraQuantidadeVezes  INTEGER NOT NULL, "
                    + "  CompraQuantidadeMaior  INTEGER NOT NULL, "
                    + "  UltimaCompraData       TEXT    NOT NULL COLLATE NOCASE, "
                    + "  UltimaCompraQuantidade INTEGER NOT NULL, "
                    + "  UltimaCompraValor      REAL    NOT NULL, "
                    + "  EstoqueQuantidade      INTEGER NOT NULL, "
                    + "  PedidoQuantidade       INTEGER NOT NULL, "
                    + "  DescontoPadrao         REAL    NOT NULL, "
                    + "  EhItemConfirmado       INTEGER NOT NULL  "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // region ClienteHistoricoCompra
            // Vem junto com o json do cliente
            sql = "DROP TABLE IF EXISTS ClienteHistoricoCompra";
            _db.execSQL(sql);

            sql = "CREATE TABLE ClienteHistoricoCompra ( "
                    + "  _id                    INTEGER NOT NULL PRIMARY KEY, "
                    + "  IdCliente              INTEGER NOT NULL, "
                    + "  IdProduto              INTEGER NOT NULL, "
                    + "  Data                   TEXT    NOT NULL COLLATE NOCASE, "
                    + "  PedidoNumero           TEXT    NOT NULL COLLATE NOCASE, "
                    + "  Quantidade             REAL    NOT NULL, "
                    + "  ValorUnitario          REAL    NOT NULL, "
                    + "  ValorDesconto          REAL    NOT NULL, "
                    + "  ValorUnitarioLiquido   REAL    NOT NULL, "
                    + "  ValorTotal             REAL    NOT NULL "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // endregion

            // region PEDIDO

            // region PedidoMobile
            sql = "DROP TABLE IF EXISTS PedidoMobile";
            _db.execSQL(sql);

            sql = "CREATE TABLE PedidoMobile ( "
                    + " _id                 INTEGER NOT NULL PRIMARY KEY, "
                    + " IdPedidoMobile      INTEGER NOT NULL, "
                    + " IdEmpresa           INTEGER NOT NULL, "
                    + " IdTipoPedido        INTEGER NOT NULL, "
                    + " IdCliente           INTEGER NOT NULL, "
                    + " Numero              TEXT    NOT NULL COLLATE NOCASE , "
                    + " NumeroCliente       TEXT    NULL COLLATE NOCASE , "
                    + " EmissaoDataHora     TEXT    NOT NULL COLLATE NOCASE, "
                    + " IdTabelaPreco       INTEGER NOT NULL, "
                    + " IdCondicaoPagamento INTEGER NOT NULL, "
                    + " IdTransportadora    INTEGER NOT NULL, "
                    + " Observacao          TEXT    NULL, "
                    + " ItensQuantidade     INTEGER NOT NULL, "
                    + " TotalValor          REAL    NOT NULL, "
                    + " DescontoPercentual1 REAL    NOT NULL, "
                    + " DescontoValor1      REAL    NOT NULL, "
                    + " TotalValorLiquido   REAL    NOT NULL, "
                    + " IdVendedor          INTEGER NOT NULL, "
                    + " EhConfirmado        INTEGER NOT NULL, "
                    + " EhSincronizado      INTEGER NOT NULL, "
                    + " DataAlteracao       TEXT    NOT NULL, "
                    + " UsuarioAlteracao    TEXT    NOT NULL, "
                    + " UNIQUE( IdEmpresa, Numero ) "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // region PedidoMobileItem
            sql = "DROP TABLE IF EXISTS PedidoMobileItem";
            _db.execSQL(sql);

            sql = "CREATE TABLE PedidoMobileItem ( "
                    + " _id                       INTEGER NOT NULL PRIMARY KEY, "
                    + " IdPedidoMobileItem        INTEGER NOT NULL, "
                    + " IdPedidoMobile            INTEGER NOT NULL, "
                    + " IdProduto                 INTEGER NOT NULL, "
                    + " PackQuantidade            INTEGER NOT NULL, "
                    + " UnidadeValor              REAL    NOT NULL, "
                    + " UnidadeDescontoPercentual REAL    NOT NULL, "
                    + " UnidadeDescontoValor      REAL    NOT NULL, "
                    + " UnidadeValorLiquido       REAL    NOT NULL, "
                    + " UnidadeVendaQuantidade    REAL    NOT NULL, "
                    + " UnidadeValorTotal         REAL    NOT NULL, "
                    + " Observacao                TEXT    NULL COLLATE NOCASE , "
                    + " DataAlteracao             TEXT    NOT NULL, "
                    + " UsuarioAlteracao          TEXT    NOT NULL COLLATE NOCASE "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // region PedidoMobileHistorico
            sql = "DROP TABLE IF EXISTS PedidoMobileHistorico";
            _db.execSQL(sql);

            sql = "CREATE TABLE PedidoMobileHistorico ( "
                    + " _id                          INTEGER NOT NULL PRIMARY KEY    , "
                    + " IdPedidoMobileHistorico      INTEGER NOT NULL                , "
                    + " IdEmpresa                    INTEGER NOT NULL                , "
                    + " IdTipoPedido                 INTEGER NOT NULL                , "
                    + " IdCliente                    INTEGER NOT NULL                , "
                    + " Numero                       TEXT    NOT NULL COLLATE NOCASE , "
                    + " NumeroCliente                TEXT    NULL     COLLATE NOCASE , "
                    + " EmissaoDataHora              TEXT    NOT NULL                , "
                    + " IdTabelaPreco                INTEGER NOT NULL                , "
                    + " IdCondicaoPagamento          INTEGER NOT NULL                , "
                    + " IdTransportadora             INTEGER NOT NULL                , "
                    + " Observacao                   TEXT    NULL                    , "
                    + " ItensQuantidade              INTEGER NOT NULL                , "
                    + " TotalValor                   REAL    NOT NULL                , "
                    + " DescontoPercentual1          REAL    NOT NULL                , "
                    + " DescontoValor1               REAL    NOT NULL                , "
                    + " TotalValorLiquido            REAL    NOT NULL                , "
                    + " IdVendedor                   INTEGER NOT NULL                , "
                    + " Empresa                      TEXT    NULL     COLLATE NOCASE , "
                    + " TipoPedido                   TEXT    NOT NULL COLLATE NOCASE , "
                    + " ClienteCodigo                TEXT    NOT NULL COLLATE NOCASE , "
                    + " ClienteRazaoSocial           TEXT    NOT NULL COLLATE NOCASE , "
                    + " ClienteNomeFantasia          TEXT    NOT NULL COLLATE NOCASE , "
                    + " TabelaPreco                  TEXT    NOT NULL COLLATE NOCASE , "
                    + " CondicaoPagamento            TEXT    NOT NULL COLLATE NOCASE , "
                    + " Transportadora               TEXT    NOT NULL COLLATE NOCASE , "
                    + " UNIQUE( IdPedidoMobileHistorico ), "
                    + " UNIQUE( IdEmpresa, Numero ) "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // region PedidoMobileItemHistorico
            sql = "DROP TABLE IF EXISTS PedidoMobileItemHistorico";
            _db.execSQL(sql);

            sql = "CREATE TABLE PedidoMobileItemHistorico ( "
                    + " _id                          INTEGER NOT NULL PRIMARY KEY    , "
                    + " IdPedidoMobileItemHistorico  INTEGER NOT NULL                , "
                    + " IdPedidoMobileHistorico      INTEGER NOT NULL                , "
                    + " IdProduto                    INTEGER NOT NULL                , "
                    + " PackQuantidade               INTEGER NOT NULL                , "
                    + " UnidadeValor                 REAL    NOT NULL                , "
                    + " UnidadeDescontoPercentual    REAL    NOT NULL                , "
                    + " UnidadeDescontoValor         REAL    NOT NULL                , "
                    + " UnidadeValorLiquido          REAL    NOT NULL                , "
                    + " UnidadeVendaQuantidade       REAL    NOT NULL                , "
                    + " UnidadeValorTotal            REAL    NOT NULL                , "
                    + " Observacao                   TEXT    NULL     COLLATE NOCASE , "
                    + " ProdutoCodigo                TEXT    NOT NULL COLLATE NOCASE , "
                    + " ProdutoDescricao             TEXT    NOT NULL COLLATE NOCASE , "
                    + " UnidadeMedida                TEXT    NOT NULL COLLATE NOCASE   "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // endregion

            // region VENDEDOR

            // region RobinHood
            sql = "DROP TABLE IF EXISTS RobinHood";
            _db.execSQL(sql);

            sql = "CREATE TABLE RobinHood ( "
                    + " _id                       INTEGER NOT NULL PRIMARY KEY    , "
                    + " IdRobinHood               INTEGER NOT NULL                , "
                    + " LancamentoData            TEXT    NOT NULL                , "
                    + " Historico                 TEXT    NOT NULL COLLATE NOCASE , "
                    + " PedidoNumero              TEXT    NOT NULL COLLATE NOCASE , "
                    + " PedidoMobileNumero        TEXT    NOT NULL COLLATE NOCASE , "
                    + " ClienteRazaoSocial        TEXT    NOT NULL COLLATE NOCASE , "
                    + " ClienteNomeFantasia       TEXT    NOT NULL COLLATE NOCASE , "
                    + " CreditoValor              REAL    NOT NULL                , "
                    + " DebitoValor               REAL    NOT NULL                , "
                    + " SaldoValor                REAL    NOT NULL                  "
                    + ")";

            _db.execSQL(sql);
            // endregion

            // endregion

        } catch (Exception e){
            Log.e("SQLiteHelper", "create -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

    }
    // endregion

    // region drop
    public void drop(){

        boolean _openHere = false;

        try{

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            List<String> _tables = this.getTables();

            for (String _table : _tables){
                _db.execSQL("DROP TABLE IF EXISTS " + _table);
            }

        } catch (Exception e){
            Log.e("SQLiteHelper", "drop -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

    }
    // endregion

    // region truncate
    public void truncate() {

        boolean _openHere = false;

        try {

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            List<String> _tables = this.getTables();

            for (String _table : _tables){
                _db.execSQL("DELETE FROM " + _table);
            }

        } catch (Exception e) {
            Log.e("SQLiteHelper", "truncate -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

    }
    // endregion

    // region truncateTable
    public void truncateTable(String tableName) {

        boolean _openHere = false;

        try {

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            _db.execSQL("DELETE FROM " + tableName);

        } catch (Exception e) {
            Log.e("SQLiteHelper", "truncate -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

    }
    // endregion

    // endregion


    //region métodos do CRUD

    // region insert
    public long insert(tpInterface tp){

        long _out = 0;
        boolean _openHere = false;


        try{

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }


            tpPost _p = this.preparePost(tp, ePostAction.INSERT);

            _out = _db.insertOrThrow(_p.table, null, _p.values);

            this.refreshPrimaryKeyValue(tp, _out);


        } catch (Exception e){
            Log.e("SQLiteHelper", "insert -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

        return _out;
    }
    // endregion

    // region update
    public long update(tpInterface tp){

        long _out = 0;
        boolean _openHere = false;


        try{

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }


            tpPost _p = this.preparePost(tp, ePostAction.UPDATE);

            String _where = _p.keyName + "=?";
            String[] _whereValues = new String[]{String.valueOf(_p.keyValue)};

            _out = _db.update(_p.table, _p.values, _where, _whereValues);


        } catch (Exception e){
            Log.e("SQLiteHelper", "update -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

        return _out;

    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {

        int _out = 0;
        boolean _openHere = false;

        try{

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            _out = _db.update(table, values, whereClause, whereArgs);


        } catch (Exception e){
            Log.e("SQLiteHelper", "update -> " + e.getMessage());
        } finally {
            if (_openHere){
                this.close();
            }
        }

        return _out;

    }
    // endregion

    // region delete
    public long delete(tpInterface tp){

        long _out = 0;
        boolean _openHere = false;


        try{

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }


            tpPost _p = this.preparePost(tp, ePostAction.DELETE);

            String _where = _p.keyName + "=?";
            String[] _whereValues = new String[]{String.valueOf(_p.keyValue)};

            _out = _db.delete(_p.table, _where, _whereValues);


        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

        return _out;
    }
    // endregion

    // endregion


    // region métodos de seleção de dados

    // region select
    public Cursor select(String table, String[] fields, String where, String[] whereArgs, String groupBy, String having, String orderBy){

        boolean _openHere = false;
        Cursor _crs = null;

        //this.open();

        try {
            Log.i("MyMRXClass.SQLiteHelper", "iniciando busca dos registros");

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            _crs = _db.query(table, fields, where, whereArgs, groupBy, having, orderBy);


            Log.i("MyMRXClass.SQLiteHelper", "terminando a busca dos registros");

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if ((_openHere == true) &&  (_db.isOpen())) {
                _db.close();
            }
        }

        return _crs;
    }
    // endregion

    // region select
    public Cursor select(String sql){

        boolean _openHere = false;
        Cursor _crs = null;

        //this.open();

        try {
            Log.i("MyMRXClass.SQLiteHelper", "iniciando busca dos registros");

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            _crs = _db.rawQuery(sql, null);

            Log.i("MyMRXClass.SQLiteHelper", "terminando a busca dos registros");

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if ((_openHere == true) &&  (_db.isOpen())) {
                _db.close();
            }
        }

        return _crs;

    }
    // endregion

    // region selectById
    public boolean selectById(tpInterface tp, int id) {

        boolean _out = false;
        boolean _openHere = false;
        String _tableName = "";
        String _where = "";
        String[] _whereArgs;

        try {

            // buscando a partir da interface o nome da tabela
            _tableName = this.getTableName(tp);

            // preparando a condição WHERE
            _where =  "_id = ?";
            _whereArgs = new String[]{String.valueOf(id)};

            if (this.isOpen() == false){
                this.open(false);
                _openHere = true;
            }

            Cursor _c = this.select(_tableName, null, _where, _whereArgs, null, null, null);

            this.fill(_c, tp);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (_openHere == true && _db.isOpen()){
                this.close();
            }
        }


        return true;
    }
    // endregion

    // endregion


    // region private methods

    // region preparePost
    private tpPost preparePost(tpInterface tp, ePostAction action){

        // declarando a variavel de retorno
        tpPost _out = new tpPost();


        // intanciando um objeto do tipo Class e recebendo
        // a referencia do objeto contido dentro da interface tpInterface
        Class _class = tp.getClass();


        try {

            // region localizando o nome da tabela
            // aqui vamos pegar o nome da tabela onde as informações
            // extraidas da classe tp serão postadas
            if (_class.isAnnotationPresent(AClass.class)) {
                _out.table = ((AClass) _class.getAnnotation(AClass.class)).table();
            }
            // endregion


            // region localizando as colunas e valores que serao inseridos
            // agora vamos percorrer todos os fields que estão marcados
            // com a anotação APostAction e que estão setados para a ação
            // de insert para pegar os seus nomes e valores
            for (Field _field : _class.getDeclaredFields()){

                if (_field.isAnnotationPresent(APrimaryKey.class)){
                    _out.keyName = _field.getName();

                    if (action == ePostAction.DELETE) {
                        _out.keyValue = _field.getLong(tp);
                    }

                    if (action == ePostAction.UPDATE) {
                        _out.keyValue = _field.getLong(tp);
                    }

                    /*
                    if (action == ePostAction.INSERT) {
                        // se o valor do campo que representa a chave primária for diferente
                        // de zero, então vamos manter o seu valor, se for igual a zero então
                        // vamos gerar um valor para ele
                        if (_field.getInt(tp) == 0) {
                            _out.keyValue = this.nextId(_out.table, _out.keyName);
                            _field.setLong(tp, _out.keyValue);

                            // agora vamos adicionar o campo no ContentValues
                            _out.values.put(_out.keyName, _out.keyValue);
                        } else {
                            _out.keyValue = _field.getLong(tp);
                        }
                    } else {
                        _out.keyValue = _field.getLong(tp);
                    }
                    */
                }

                if (_field.isAnnotationPresent(APostAction.class)){
                    APostAction _action = (APostAction) _field.getAnnotation(APostAction.class);

                    if (_action.insert() && action == ePostAction.INSERT){

                        if (_field.getType().getName().equals("java.lang.boolean")){
                            _out.values.put(_field.getName(), (boolean) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("double")){
                            _out.values.put(_field.getName(), (Double) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("float")){
                            _out.values.put(_field.getName(), (Float) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("int")){
                            _out.values.put(_field.getName(), (int) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("long")){
                            _out.values.put(_field.getName(), (long) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("java.lang.String")){
                            _out.values.put(_field.getName(), (String) _field.get(tp));
                        }

                    } else if (_action.update() && action == ePostAction.UPDATE){

                        if (_field.getType().getName().equals("java.lang.boolean")){
                            _out.values.put(_field.getName(), (boolean) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("double")){
                            _out.values.put(_field.getName(), (Double) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("float")){
                            _out.values.put(_field.getName(), (Float) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("int")){
                            _out.values.put(_field.getName(), (int) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("long")){
                            _out.values.put(_field.getName(), (long) _field.get(tp));
                        }

                        if (_field.getType().getName().equals("java.lang.String")){
                            _out.values.put(_field.getName(), (String) _field.get(tp));
                        }

                    }
                }

            }
            // endregion

        } catch (Exception e){
            e.printStackTrace();
        }


        return _out;

    }
    // endregion

    // region getTableName
    private String getTableName(tpInterface tp){

        String _out = "";

        try{

            // recebendo em uma variável do tipo Class a classe
            // intanciada dentro da variável tp
            Class _class = tp.getClass();

            if (_class.isAnnotationPresent(AClass.class)){
                _out = ((AClass) _class.getAnnotation(AClass.class)).table();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return _out;

    }
    // endregion

    // region refreshPrimaryKeyValue
    private void refreshPrimaryKeyValue(tpInterface tp, long primaryKeyValue) {

        try {

            // pegando o ponteiro da classe através do
            // método getClass()
            Class _class = tp.getClass();

            // Realizando um loop nos campos públicos para identificar
            // o seu típo e a partir daí selecionar a informação do cursor
            // e preencher o campo
            //region
            for (Field _field : _class.getDeclaredFields()) {

                if (_field.isAnnotationPresent(APrimaryKey.class)){
                    _field.setLong(tp, primaryKeyValue);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // endregion

    // region fill
    private void fill(Cursor c, tpInterface tp){

        try {

            // se a variável que contém o cursor não estiver nula
            // então vamos mover o ponteiro para o primeiro registro
            if (c != null) {
                if (c.isBeforeFirst()) {
                    c.moveToFirst();
                }
            }


            // pegando o ponteiro da classe através do
            // método getClass()
            Class _class = tp.getClass();


            // Realizando um loop nos campos públicos para identificar
            // o seu típo e a partir daí selecionar a informação do cursor
            // e preencher o campo
            //region
            for (Field _field : _class.getDeclaredFields()) {

                if (_field.getType().getName().equals("java.lang.String")){

                    // copiando o valor da coluna para o field da classe
                    _field.set(tp, c.getString(c.getColumnIndex(_field.getName())));

                } if (_field.getType().getName().equals("java.lang.boolean")){

                    // pegando o valor da coluna que está gravado no formato inteiro
                    int _aux = c.getInt(c.getColumnIndex(_field.getName()));

                    // copiando o valor da variável para o field da classe
                    _field.setBoolean(tp, _aux == 1 ? true : false);

                } else if (_field.getType().getName().equals("double")){

                    // copiando o valor da coluna para o field da classe
                    _field.setDouble(tp, c.getDouble(c.getColumnIndex(_field.getName())));

                } else if (_field.getType().getName().equals("float")){

                    // copiando o valor da coluna para o field da classe
                    _field.setFloat(tp, c.getFloat(c.getColumnIndex(_field.getName())));

                } else if (_field.getType().getName().equals("int")){

                    // copiando o valor da coluna para o field da classe
                    _field.setInt(tp, c.getInt(c.getColumnIndex(_field.getName())));

                } else if (_field.getType().getName().equals("long")){

                    // copiando o valor da coluna para o field da classe
                    _field.setLong(tp, c.getLong(c.getColumnIndex(_field.getName())));

                }

            }
            //endregion

        } catch (Exception e){
            e.printStackTrace();
        }

    }
    // endregion

    // region getTables
    private List<String> getTables() {

        // region Declarando a variável de retorno
        List<String> _out = null;
        // endregion


        // region Declarando as demais variáves do método
        boolean _openHere = false;
        String _sql = "";
        Cursor _cursor = null;
        // endregion


        // region Iniciando o corpo do método
        try{

            // iniciando a senteça para selecionar todas as colunas
            // da tabela de sistema SQLITE_MASTER onde o tipo de registro
            // se refere a uma tabela
            _sql = "SELECT * "
                 + "  FROM sqlite_master "
                 + " WHERE type = 'table' ";


            // verificando a conexão com o banco de dados
            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }


            // executando a consulta no banco, carregando o curosr
            // e a partir dele vamos preencher a variável de retorno
            // despresando o nome de tabelas de sistema
            _cursor = _db.rawQuery(_sql, null);

            if ((_cursor != null) && (_cursor.getCount() > 0)) {

                while (_cursor.moveToNext()) {

                    String _tableName = _cursor.getString(1);

                    if ((!_tableName.equals("android_metadata")) && (!_tableName.equals("sqlite_sequence"))){

                        if (_out == null) {
                            _out = new ArrayList<String>();
                        }

                        _out.add(_tableName);

                    }

                }

            }

            _cursor.close();


        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }
        // endregion


        // region finalizando o método e enviando o retorno
        return  _out;
        // endregion

    }
    // endregion

    // endregion


    // region public methods

    // region nextId
    public long nextId(String table, String column){

        long _out = 0;
        boolean _openHere = false;


        // aqui vamos converter os parametros table e column para
        // texto no formato minusculo.
        table = table.toLowerCase();
        column = column.toLowerCase();


        try{

            // verificando a necessidade de abrir a conexao
            // com o banco de dados
            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }


            // montando a sentença para selecionar o ultimo
            // valor de identificador utilizado
            String _sql1 = " SELECT Valor "
                    + "   FROM Ids "
                    + "  WHERE Tabela = '{Tabela}' "
                    + "    AND Coluna = '{Coluna}' ";

            _sql1 = _sql1.replace("{Tabela}", table);
            _sql1 = _sql1.replace("{Coluna}", column);


            // executando a sentença no banco de dados e verificando se
            // existe resultado
            Cursor _c = _db.rawQuery(_sql1, null);

            if (_c != null){
                if (_c.moveToFirst()){
                    _out = _c.getLong(0) + 1;
                } else {
                    _out = 1;

                    ContentValues _cv = new ContentValues();
                    _cv.put("Tabela", table);
                    _cv.put("Coluna", column);
                    _cv.put("Valor", _out);

                    _db.insert("Ids", null, _cv);
                }
            }


            // montando a sentença para atualizar o ultimo valor
            // gerado para a tabela e coluna
            String _sql2 = " UPDATE Ids "
                    + "    SET Valor  = {Valor} "
                    + "  WHERE Tabela = '{Tabela}' "
                    + "    AND Coluna = '{Coluna}' ";

            _sql2 = _sql2.replace("{Valor}", String.valueOf(_out));
            _sql2 = _sql2.replace("{Tabela}", table);
            _sql2 = _sql2.replace("{Coluna}", column);

            _db.execSQL(_sql2);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if ((_openHere == true) && (this.isOpen() == true)){
                this.close();
            }
        }

        return _out;
    }
    // endregion

    // endregion


    // region other methods

    // region executeSQL
    public void executeSQL(String sql) {

        boolean _openHere = false;

        try {

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            _db.execSQL(sql);

        } catch (Exception e) {
            Log.e("SQLiteHelper", "executeSQL -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

    }
    // endregion


    // region executeSQL
    public void executeSQL(ArrayList<String> sqls) {

        boolean _openHere = false;

        try {

            if (this.isOpen() == false){
                this.open(true);
                _openHere = true;
            }

            _db.beginTransaction();

            for (String sql : sqls) {
                _db.execSQL(sql);
            }

            _db.setTransactionSuccessful();

        } catch (Exception e) {
            _db.endTransaction();
            Log.e("SQLiteHelper", "executeSQL -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (_openHere){
                this.close();
            }
        }

    }
    // endregion

    // endregion

}
