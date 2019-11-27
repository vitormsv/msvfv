package br.com.microserv.framework.msvbase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.MSVMsgBox;
import br.com.microserv.framework.msvutil.SQLClauseHelper;
import br.com.microserv.framework.msvutil.WhereItem;
import br.com.microserv.framework.msvutil.eSQLConditionType;
import br.com.microserv.framework.msvutil.eSQLSortType;
import br.com.microserv.framework.msvutil.eSQLiteDataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricardo on 21/10/2016.
 */
public class dbBase implements dbInterface {

    // region Declarando SQLiteHelper
    private SQLiteHelper _dbHelper;
    // endregion


    // region Declarando objetos privados
    private tpInterface _dto;
    // endregion


    // region Declarando variáveis privadas
    private String _tableName;
    // endregion


    // region Métodos que deverão ser sobrescritos nos objetos filhos

    // region method newDTO
    public tpInterface newDTO(){
        return null;
    }
    // endregion

    // region method loadDependencies
    public void loadDepencies(tpInterface tp) throws Exception {}
    // endregion

    // endregion


    // region Metodos privados da classe

    // region getTableName
    private String getTableName(tpInterface tp) throws Exception{

        String _out = "";

        try{

            // recebendo em uma variável do tipo Class a classe
            // intanciada dentro da variável tp
            Class _class = tp.getClass();

            if (_class.isAnnotationPresent(AClass.class)){
                _out = ((AClass) _class.getAnnotation(AClass.class)).table();
            }

        } catch (Exception e){
            throw new Exception("dbBase | getTableName() | " + e.getMessage());
        }

        return _out;

    }
    // endregion


    // region fill
    protected void fill(Cursor c, tpInterface tp) throws Exception{

        try {

            // pegando o ponteiro da classe através do
            // método getClass()
            Class _class = tp.getClass();


            // Realizando um loop nos campos públicos para identificar
            // o seu típo e a partir daí selecionar a informação do cursor
            // e preencher o campo
            //region
            for (Field _field : _class.getDeclaredFields()) {

                if (_field.isAnnotationPresent(APostAction.class)) {

                    if (_field.getType().getName().equals("java.lang.String")){

                        // copiando o valor da coluna para o field da classe
                        _field.set(tp, c.getString(c.getColumnIndex(_field.getName())));

                    } else if (_field.getType().getName().equals("java.lang.boolean")){

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

            }
            //endregion

        } catch (Exception e){
            throw new Exception("dbBase | fill() | " + e.getMessage());
        }

    }
    // endregion

    // endregion


    // region Metodo construtor
    public dbBase(SQLiteHelper dbHelper) throws Exception{

        this._dbHelper = dbHelper;


        // invocando os métodos virtuais
        _dto = newDTO();


        // invocando outros métodos de inicialização da classe
        try {
            _tableName = getTableName(_dto);
        } catch (Exception e) {
            throw new Exception("dbBase | constructor() | " + e.getMessage());
        }

    }
    // endregion


    // region getAll

    // region getAll (only invocation)
    public Cursor getAll() throws Exception {

        // region Declarando variáveis do método
        SQLClauseHelper _sch = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            _sch = new SQLClauseHelper();
            _sch.addOrderBy("_id", eSQLSortType.ASC);

            return this.getAll(_sch);

        } catch (Exception e) {

            // Aqui não precisamos identificar o nome da classe e nem o nome
            // do método na mensagem de erro, isso por que invocamos o mesmo
            // método sobrecarregado que esta abaixo, e este tratamento será
            // adicionado no método sobrecarregado
            throw new Exception(e.getMessage());

        }
        // endregion

    }
    // endregion

    // region getAll (with code)
    public Cursor getAll(SQLClauseHelper sqlClauseHelper) throws Exception{

        // region Declarando variável de retorno do método
        boolean _openHere = false;
        // endregion

        // region Declarando demais variáveis
        Cursor _out = null;
        String _where = null;
        String _orderBy = null;
        String _stmt = null;
        // endregion

        // region Bloco protegido de exceção
        try{

            // region Verificando a conexão com o banco de dados
            if (_dbHelper.isOpen() == false){
                _dbHelper.open(false);
                _openHere = true;
            }
            // endregion

            // region Montando a sentença principal de seleção dos dados
            _stmt = "SELECT * FROM " + _tableName;
            // endregion

            // region Adicionando a clausula WHERE se necessário
            _where = sqlClauseHelper.getWhereClause();

            if (_where.isEmpty() == false){
                _stmt += " WHERE " + _where;
            }
            // endregion

            // region Adicionando a clausula ORDER BY se necessário
            _orderBy = sqlClauseHelper.getOrderByClause();

            if (_orderBy.isEmpty() == false){
                _stmt += " ORDER BY " + _orderBy;
            }
            // endregion

            // region Executando a consulta no banco de dados
            _out = _dbHelper.select(_stmt);
            // endregion

        } catch (Exception e){

            throw new Exception("dbBase | getAll() | " + e.getMessage());

        } finally {

          if ((_dbHelper != null) && (_openHere)){
              _dbHelper.close();
          }

        }
        // endregion

        // region Retornando a resposta do processamento
        return _out;
        // endregion
    }
    // endregion

    // endregion


    // region getList

    // region getList (only invocation)
    public <T> List<T> getList(Class<T> tp) throws Exception {

        // region Declarando variáveis do método
        SQLClauseHelper _sch = null;
        // endregion

        // region Bloco protogido por exceção
        try {

            _sch = new SQLClauseHelper();
            _sch.addOrderBy("_id", eSQLSortType.ASC);

            return this.getList(tp, _sch);

        } catch (Exception e) {

            // Aqui não precisamos identificar o nome da classe e nem o nome
            // do método na mensagem de erro, isso por que invocamos o mesmo
            // método sobrecarregado que esta abaixo, e este tratamento será
            // adicionado no método sobrecarregado
            throw new Exception(e.getMessage());

        }
        // endregion

    }
    // endregion

    // region getList (with code)
    public <T> List<T> getList(Class<T> tp, SQLClauseHelper sqlClauseHelper) throws Exception {

        // region Declarando variável de retorno do método
        List<T> _out = null;
        // endregion

        // region Declarando demais variáveis do método
        Cursor _c = null;
        // endregion

        // region Bloco protegido por exceção
        try {

            // region Invocando o método getAll para buscar as informações no banco
            _c = this.getAll(sqlClauseHelper);
            // endregion

            // region Construindo a lista de retorno
            if (_c != null && _c.getCount() != 0){

                while (_c.moveToNext()){

                    // region Instânciando um objeto do tipo "tp" e preenchendo o mesmo
                    tpInterface _tp = newDTO();
                    this.fill(_c, _tp);
                    // endregion

                    // region Verificano se a lista de retorno já foi instânciada
                    if (_out == null) {
                        _out = new ArrayList<T>();
                    }
                    // endregion

                    // region Adicionando o objeto "tp" na lista de retorno
                    _out.add(tp.cast(_tp));
                    // endregion
                }

            }
            // endregion

        } catch (Exception e) {

            throw new Exception("dbBase | getList() | " + e.getMessage());

        } finally {

            if ((_c != null) && (_c.isClosed() == false)){
                _c.close();
            }

        }
        // endregion

        // region Retornando a resposta do processo realizado
        return _out;
        // endregion
    }
    // endregion

    // endregion


    // region getListOfSouceId
    public List<Integer> getListOfSouceId(SQLClauseHelper sqlClauseHelper) throws Exception {

        // region Declarando variável de retorno do método
        List<Integer> _out = null;
        // endregion

        // region Declarando demais variáveis do método
        Cursor _c = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Verificando a necessidade de adicionar ORDER BY padrão
            if (sqlClauseHelper == null) {
                sqlClauseHelper = new SQLClauseHelper();
                sqlClauseHelper.addOrderBy("Id" + _tableName, eSQLSortType.ASC);
            }
            // endregion

            // region Invocando o método getAll para buscar as informações no banco de dados
            _c = this.getAll(sqlClauseHelper);
            // endregion

            // region Construindo a lista de retorno
            if (_c != null){

                while (_c.moveToNext()){

                    // region Verificando se a lista de retorno está instânciada
                    if (_out == null) {
                        _out = new ArrayList<Integer>();
                    }
                    // endregion

                    // region Adicionando o identificador na lista de retorno
                    _out.add(_c.getInt(_c.getColumnIndexOrThrow("Id" + _tableName)));
                    // endregion
                }

            }
            // endregion

        } catch (Exception e) {

            throw new Exception("dbBase | getListOfSourceId() | " + e.getMessage());

        } finally {

            if ((_c != null) && (_c.isClosed() == false)){
                _c.close();
            }

        }
        // endregion

        // region Devolvendo a resposta do processo realizado
        return _out;
        // endregion
    }
    // endregion


    // region executeWithResult
    public Cursor executeWithResult(String stmt) throws Exception{

        // region Declarando variável de retorno do método
        Cursor _out = null;
        // endregion

        // region Declarando demais variáveis do método
        boolean _openHere = false;
        // endregion

        // region Bloco protegido por execação
        try {

            // region Verificando a conexão com obanco de dados
            if (!_dbHelper.isOpen()) {
                _dbHelper.open(false);
                _openHere = true;
            }
            // endregion

            // region Executando a senteça no banco de dados
            _out = _dbHelper.select(stmt);
            // endregion

        } catch (Exception e) {

            throw new Exception("dbBase | executeWithResult() | " + e.getMessage());

        } finally {

            if ((_dbHelper != null) && (_dbHelper.isOpen()) && (_openHere)){
                _dbHelper.close();
            }

        }
        // endregion

        // region Devolvendo o resultado processado
        return _out;
        // endregion

    }
    // endregion


    // region executeWithOutResult
    public void executeWithOutResult(String stmt) throws Exception {

        // region Declarando demais variáveis do método
        boolean _openHere = false;
        // endregion

        // region Bloco protegido por execação
        try {

            // region Verificando a conexão com obanco de dados
            if (!_dbHelper.isOpen()) {
                _dbHelper.open(false);
                _openHere = true;
            }
            // endregion

            // region Executando a senteça no banco de dados
            _dbHelper.executeSQL(stmt);
            // endregion

        } catch (Exception e) {

            throw new Exception("dbBase | executeWithOutResult() | " + e.getMessage());

        } finally {

            if ((_dbHelper != null) && (_dbHelper.isOpen()) && (_openHere)){
                _dbHelper.close();
            }

        }
        // endregion

    }

    // endregion


    // region method getById

    // region getById method with no choose dependency loading
    public tpInterface getById(long value) throws Exception {
        return getById(value, false);
    }
    // endregion

    // region getById method with choose dependency loading
    public tpInterface getById(long value, boolean loadDependencies) throws Exception {

        // region Declarando variável de retorno do método
        tpInterface _out = null;
        // endregion

        // region Declarando demais variáveis do método
        SQLClauseHelper _sch = null;
        WhereItem _where1 = null;
        Cursor _crs = null;
        // endregion

        // region Bloco protegido por exceção
        try {

            // region Montando a condição WHERE para seleção do registro
            _where1 = new WhereItem();
            _where1.field = "_id";
            _where1.dataType = eSQLiteDataType.INTEGER;
            _where1.conditionType = eSQLConditionType.EQUAL;
            _where1.value1 = value;

            _sch = new SQLClauseHelper();
            _sch.addWhere(_where1);
            // endregion

            // region Invocando o método getAll para buscar os registros no banco
            _crs = this.getAll(_sch);
            // endregion

            // region Se o registro foi encontrado então vamos preencher o objeto de retorno
            if ((_crs != null) && (_crs.getCount() > 0)) {

                _crs.moveToFirst();

                _out = _dto.clone();
                this.fill(_crs, _out);

                if (loadDependencies) {
                    this.loadDepencies(_out);
                }

            }
            // endregion

        } catch (Exception e) {

            throw new Exception("dbBase | getById() | " + e.getMessage());

        } finally {

            if ((_crs != null) && (_crs.isClosed() == false)) {
                _crs.close();
            }

        }
        // endregion

        // region Retornando o resultado processado
        return _out;
        // endregion
    }
    // endregion

    // endregion


    // region getOne

    // region getOne method with no choose dependency loading
    public tpInterface getOne(SQLClauseHelper sqlClauseHelper) throws Exception {
        return getOne(sqlClauseHelper, false);
    }
    // endregion

    // region getOne method with choose dependency loading
    public tpInterface getOne(SQLClauseHelper sqlClauseHelper, boolean loadDependencies) throws Exception {

        // region Declarando variável de retorno do método
        tpInterface _out = null;
        // endregion

        // region Declarando demais variáveis do método
        Cursor _c = null;
        // endregion

        // region Bloco protegido de exceção
        try {

            // region Invocando o método getAll para buscar informações no banco de dados
            _c = this.getAll(sqlClauseHelper);
            // endregion

            // region Preenchendo o objeto do tipo tpInterface
            if ((_c != null) && (_c.moveToFirst())){
                _out = newDTO();
                this.fill(_c, _out);
            }
            // endregion

        } catch (Exception e) {

            throw new Exception("dbBase | getOne() | " + e.getMessage());

        } finally {

            if (_c != null && _c.isClosed() == false){
                _c.close();
            }

        }
        // endregion

        // region Retornando o resultado processado
        return _out;
        // endregion

    }
    // endregion

    // endregion


    // region getBySourceId
    public tpInterface getBySourceId(long value) throws Exception {

        // region Declarando variável de retorno do método
        tpInterface _out = null;
        // endregion

        // region Declarando demais variáveis
        SQLClauseHelper _sch = null;
        // endregion

        // region Bloco protegido por exceção
        try {

            // region Montando o WHERE para o identificador
            _sch = new SQLClauseHelper();
            _sch.addEqualInteger("Id" + _tableName, value);
            // endregion

            // region Invocando o método getOne para buscar o registro no banco de dados
            _out = this.getOne(_sch);
            // endregion

        } catch (Exception e) {

            throw new Exception("dbBase | getBySourceId() | " + e.getMessage());
        }
        // endregion

        // region Retornando o resultado processado
        return _out;
        // endregion

    }
    // endregion


    // region insert
    public long insert(tpInterface tp) throws Exception {

        // region Declarando variável de retorno do método
        long _out = 0;
        // endregion

        // region Declarando demais variáveis do método
        boolean _openHere = false;
        // endregion

        // region Bloco protegido de exceção
        try{

            // region Verificando a conexão com o banco de dados
            if (!_dbHelper.isOpen()){
                _dbHelper.open(false);
                _openHere = true;
            }
            // endregion

            // region Executando o INSERT no banco de dados
            _out = _dbHelper.insert(tp);
            // endregion

        } catch (Exception e){

            throw new Exception("dbBase | insert() | " + e.getMessage());

        } finally {

            if ((_dbHelper != null) && (_dbHelper.isOpen()) && (_openHere)) {
                _dbHelper.close();
            }

        }
        // endregion

        // region Retornando o resultado processado
        return _out;
        // endregion

    }
    // endregion


    // region update

    // region update (with tpInterface)
    public long update(tpInterface tp) throws Exception {

        // region Declarando variável de retorno do método
        long _out = 0;
        // endregion

        // region Declarando demais variáveis do método
        boolean _openHere = false;
        // endregion

        // region Bloco protegido de exceção
        try{

            // region Verificando a conexão com o banco de dados
            if (!_dbHelper.isOpen()){
                _dbHelper.open(false);
                _openHere = true;
            }
            // endregion

            // region Executando o UPDATE no banco de dados
            _out = _dbHelper.update(tp);
            // endregion

        } catch (Exception e){

            throw new Exception("dbBase | update() | " + e.getMessage());

        } finally {

            if ((_dbHelper != null) && (_dbHelper.isOpen()) && (_openHere)){
                _dbHelper.close();
            }

        }
        // endregion

        // region Retornando o resultado processado
        return _out;
        // endregion

    }
    // endregion

    // region update
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) throws Exception {

        // region Declarando variáveis do método
        boolean _openHere = false;
        // endregion

        // region Bloco protegido de exceção
        try{

            // region Verificando a conexão com o banco de dados
            if (!_dbHelper.isOpen()){
                _dbHelper.open(true);
                _openHere = true;
            }
            // endregion

            // region Executando o UPDATE no banco de dados
            return _dbHelper.update(table, values, whereClause, whereArgs);
            // endregion

        } catch (Exception e){

            throw new Exception("dbBase | update() " + e.getMessage());

        } finally {

            if ((_dbHelper != null) && (_dbHelper.isOpen()) && (_openHere)) {
                _dbHelper.close();
            }
        }
        // endregion

    }
    // endregion

    // endregion


    // region delete

    // region delete (with tpInterface)
    public long delete(tpInterface tp) throws Exception {

        // region Declarando variável de retorno do método
        long _out = 0;
        // endregion

        // region Declarando demais variáveis do método
        boolean _openHere = false;
        // endregion

        // region Bloco protegido de exceção
        try{

            // region Verificando a conexão com o banco de dados
            if (!_dbHelper.isOpen()){
                _dbHelper.open(false);
                _openHere = true;
            }
            // endregion

            // region Executando o DELETE no banco de dados
            _out = _dbHelper.delete(tp);
            // endregion

        } catch (Exception e){

            throw new Exception("dbBase | delete() | " + e.getMessage());

        } finally {

            if ((_dbHelper != null) && (_dbHelper.isOpen()) && (_openHere)){
                _dbHelper.close();
            }

        }
        // endregion

        // region Retornando o resultado processado
        return _out;
        // endregion

    }
    // endregion

    // region delete ( with SQLClauseHelper)
    public void delete(SQLClauseHelper sqlClauseHelper) throws Exception {

        // region Declarando variáveis do método
        boolean _openHere = false;
        Cursor _out = null;
        String _where = null;
        String _stmt = null;
        // endregion

        // region Bloco protegido de exceção
        try{

            // region Verificando a conexão com o banco de dados
            if (!_dbHelper.isOpen()){
                _dbHelper.open(false);
                _openHere = true;
            }
            // endregion


            // region Carregando a condição where que foi enviada no objeto de parametro
            _where = sqlClauseHelper.getWhereClause();
            // endregion


            // region Verificando se existe condição WHERE para a exclusão
            if (_where != null && _where.isEmpty() == false) {

                // region Associando a condição WHERE a senteça SQL
                _stmt = "DELETE FROM " + _tableName;
                _stmt += " WHERE " + _where;
                // endregion

                // region Executando o DELETE no banco de dados
                _dbHelper.executeSQL(_stmt);
                // endregion

            }
            // endregion

        } catch (Exception e){

            throw new Exception("dbBase | delete() | " + e.getMessage());

        } finally {

            if ((_dbHelper != null) && (_dbHelper.isOpen()) && (_openHere)){
                _dbHelper.close();
            }

        }
        // endregion

    }
    // endregion

    // endregion


    // region truncateTable
    public void truncateTable() throws Exception {

        // region Declarando variáveis do método
        boolean _openHere = false;
        // endregion

        // region Bloco protegido de exceção
        try{

            // region Verificando a conexão com o banco de dados
            if (!_dbHelper.isOpen()){
                _dbHelper.open(true);
                _openHere = true;
            }
            // endregion

            // region Removendo todos os dados da tabela
            _dbHelper.truncateTable(_tableName);
            // endregion

        } catch (Exception e){

            throw new Exception("dbBase | truncateTable() " + e.getMessage());

        } finally {

            if ((_dbHelper != null) && (_dbHelper.isOpen()) && (_openHere)) {
                _dbHelper.close();
            }
        }
        // endregion

    }
    // endregion

}
