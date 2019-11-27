package br.com.microserv.framework.msvutil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Ricardo on 21/10/2016.
 */
public class SQLClauseHelper {

    private ArrayList<WhereItem> _whereItems;
    private ArrayList<OrderByItem> _orderByItems;


    public SQLClauseHelper(){

        _whereItems = new ArrayList<WhereItem>();
        _orderByItems = new ArrayList<OrderByItem>();

    }


    // region Clear
    public void clearWhere(){
        this._whereItems.clear();
    }

    public void clearOrderBy(){
        this._orderByItems.clear();
    }

    public void clearAll(){
        this.clearWhere();
        this.clearOrderBy();
    }
    // endregion


    // region Methods of OrderBy
    public void addOrderBy(String field, eSQLSortType sortType){

        OrderByItem _item = new OrderByItem();
        _item.field = field;
        _item.sortType = sortType;

        this._orderByItems.add(_item);

    }


    public String getOrderByClause(){

        String _out = "";

        Iterator _itr = _orderByItems.iterator();

        while (_itr.hasNext()){

            if (_out.isEmpty() == false){
                _out += ", ";
            }

            _out += ((OrderByItem)_itr.next()).field;

        }

        return _out;

    }
    // endregion


    // region Methods of Where
    public void addEqualInteger(String field, Object value){

        WhereItem _wi = new WhereItem();
        _wi.field = field;
        _wi.dataType = eSQLiteDataType.INTEGER;
        _wi.conditionType = eSQLConditionType.EQUAL;
        _wi.value1 = value;

        try {
            this.addWhere(_wi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWhere(WhereItem item) throws Exception {

        // region Verificando se já existe filtro informado para o campo
        Iterator _itr = _whereItems.iterator();

        while (_itr.hasNext()) {
            WhereItem _whereItem = (WhereItem) _itr.next();

            if (_whereItem.field.equals(item.field)){
                throw new Exception("Já existe filtro configurado para o campo " + item.field);
            }
        }
        // endregion

        // region Verificando o tipo de pesquisa associado ao tipo do dado
        if (item.dataType.equals(eSQLiteDataType.INTEGER)){

            if (item.conditionType.equals(eSQLConditionType.EQUAL_AT_THE_BEGINNING)){
                throw new Exception("O tipo de pesquisa EQUAL_AT_THE_GEGINNING não combina com o tipo de dado inteiro");
            }

            if (item.conditionType.equals(eSQLConditionType.EQUAL_EVERY_WHERE)){
                throw new Exception("O tipo de pesquisa EQUAL_EVERY_WHERE não combina com o tipo de dado inteiro");
            }

        }

        if (item.dataType.equals(eSQLiteDataType.REAL)){

            if (item.conditionType.equals(eSQLConditionType.EQUAL_AT_THE_BEGINNING)){
                throw new Exception("O tipo de pesquisa EQUAL_AT_THE_GEGINNING não combina com o tipo de dado real");
            }

            if (item.conditionType.equals(eSQLConditionType.EQUAL_EVERY_WHERE)){
                throw new Exception("O tipo de pesquisa EQUAL_EVERY_WHERE não combina com o tipo de dado real");
            }

        }

        if (item.dataType.equals(eSQLiteDataType.DATE)){

            if (item.conditionType.equals(eSQLConditionType.EQUAL_AT_THE_BEGINNING)){
                throw new Exception("O tipo de pesquisa EQUAL_AT_THE_GEGINNING não combina com o tipo de dado data");
            }

            if (item.conditionType.equals(eSQLConditionType.EQUAL_EVERY_WHERE)){
                throw new Exception("O tipo de pesquisa EQUAL_EVERY_WHERE não combina com o tipo de dado data");
            }

        }
        // endregion

        this._whereItems.add(item);
    }


    public String getWhereClause(){

        String _out = "";

        Iterator _itr = _whereItems.iterator();

        while (_itr.hasNext()){

            WhereItem _item = (WhereItem)_itr.next();


            if (_out.isEmpty() == false){
                _out += " AND ";
            }


            switch (_item.conditionType){

                case EQUAL:
                    _out += this.buildEqual(_item);
                    break;

                case EQUAL_AT_THE_BEGINNING:
                    _out += this.buildEqualAtTheBeginning(_item);
                    break;

                case EQUAL_EVERY_WHERE:
                    _out += this.buildEqualEveryWhere(_item);
                    break;

                case GREATER_THAN:
                    _out += this.buildGreaterThan(_item);
                    break;

                case GREATER_OR_EQUAL_THAN:
                    _out += this.buildGreaterOrEqualThan(_item);
                    break;

                case LESS_THAN:
                    _out += this.buildLessThan(_item);
                    break;

                case LESS_OR_EQUAL_THAN:
                    _out += this.buildLessOrEqualThan(_item);
                    break;

                case BETWEEN:
                    _out += this.buildBetween(_item);
                    break;

                case IS_NULL:
                    _out += this.buildIsNull(_item);
                    break;
                    
            }

        }

        return  _out;

    }


    private String buildEqual(WhereItem item){

        String _out = "%1s = %2s";

        try {

            switch (item.dataType) {

                case TEXT:
                    _out = String.format(_out, item.field, MSVUtil.toSqlText(item.value1));
                    break;

                case INTEGER:
                    _out = String.format(_out, item.field, MSVUtil.toSqlInt(item.value1));
                    break;

                case DATE:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDate(item.value1));
                    break;

                case REAL:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDecimal(item.value1));
                    break;
            }

        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return _out;
    }


    private String buildEqualAtTheBeginning(WhereItem item){

        String _out = "%1s LIKE %2s";

        _out = String.format(_out, item.field, MSVUtil.toSqlText(item.value1, "", "%"));

        return _out;

    }


    private String buildEqualEveryWhere(WhereItem item){

        String _out = "%1s LIKE %2s";

        _out = String.format(_out, item.field, MSVUtil.toSqlText(item.value1, "%", "%"));

        return _out;
    }


    private String buildGreaterThan(WhereItem item){

        String _out = "%1s > %2s";

        try{

            switch (item.dataType){
                case TEXT:
                    _out = String.format(_out, item.field, MSVUtil.toSqlText(item.value1));
                    break;

                case INTEGER:
                    _out = String.format(_out, item.field, MSVUtil.toSqlInt(item.value1));
                    break;

                case DATE:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDate(item.value1));
                    break;

                case REAL:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDecimal(item.value1));
                    break;
            }

        } catch (ParseException pe){
            pe.printStackTrace();
        }

        return _out;

    }


    private String buildGreaterOrEqualThan(WhereItem item){

        String _out = "%1s >= %2s";

        try{

            switch (item.dataType){
                case TEXT:
                    _out = String.format(_out, item.field, MSVUtil.toSqlText(item.value1));
                    break;

                case INTEGER:
                    _out = String.format(_out, item.field, MSVUtil.toSqlInt(item.value1));
                    break;

                case DATE:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDate(item.value1));
                    break;

                case REAL:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDecimal(item.value1));
                    break;
            }

        } catch (ParseException pe){
            pe.printStackTrace();
        }

        return _out;

    }


    private String buildLessThan(WhereItem item){

        String _out = "%1s < %2s";

        try{

            switch (item.dataType){
                case TEXT:
                    _out = String.format(_out, item.field, MSVUtil.toSqlText(item.value1));
                    break;

                case INTEGER:
                    _out = String.format(_out, item.field, MSVUtil.toSqlInt(item.value1));
                    break;

                case DATE:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDate(item.value1));
                    break;

                case REAL:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDecimal(item.value1));
                    break;
            }

        } catch (ParseException pe){
            pe.printStackTrace();
        }

        return _out;

    }


    private String buildLessOrEqualThan(WhereItem item){

        String _out = "%1s <= %2s";

        try{

            switch (item.dataType){
                case TEXT:
                    _out = String.format(_out, item.field, MSVUtil.toSqlText(item.value1));
                    break;

                case INTEGER:
                    _out = String.format(_out, item.field, MSVUtil.toSqlInt(item.value1));
                    break;

                case DATE:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDate(item.value1));
                    break;

                case REAL:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDecimal(item.value1));
                    break;
            }

        } catch (ParseException pe){
            pe.printStackTrace();
        }

        return _out;

    }


    private String buildBetween(WhereItem item){

        String _out = "%1s BETWEEN %2s AND %3s";

        try{

            switch (item.dataType){
                case TEXT:
                    _out = String.format(_out, item.field, MSVUtil.toSqlText(item.value1), MSVUtil.toSqlText(item.value2));
                    break;

                case INTEGER:
                    _out = String.format(_out, item.field, MSVUtil.toSqlInt(item.value1), MSVUtil.toSqlInt(item.value2));
                    break;

                case DATE:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDate(item.value1), MSVUtil.toSqlDate(item.value2));
                    break;

                case REAL:
                    _out = String.format(_out, item.field, MSVUtil.toSqlDecimal(item.value1), MSVUtil.toSqlDecimal(item.value2));
                    break;
            }

        } catch (ParseException pe){
            pe.printStackTrace();
        }

        return _out;

    }


    private String buildIsNull(WhereItem item){

        String _out = "%1s IS NULL";

        try{

            switch (item.dataType){
                case TEXT:
                    _out = String.format(_out, item.field);
                    break;

                case INTEGER:
                    _out = String.format(_out, item.field);
                    break;

                case DATE:
                    _out = String.format(_out, item.field);
                    break;

                case REAL:
                    _out = String.format(_out, item.field);
                    break;
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return _out;

    }
    // endregion
}
