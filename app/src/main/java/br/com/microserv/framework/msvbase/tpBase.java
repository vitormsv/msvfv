package br.com.microserv.framework.msvbase;

import org.json.JSONObject;

import br.com.microserv.framework.msvannotation.ABooleanField;
import br.com.microserv.framework.msvannotation.ADateField;
import br.com.microserv.framework.msvannotation.ADoubleField;
import br.com.microserv.framework.msvannotation.AFloatField;
import br.com.microserv.framework.msvannotation.AIntegerField;
import br.com.microserv.framework.msvannotation.AListIdentifierField;
import br.com.microserv.framework.msvannotation.ALongField;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvannotation.APrimaryKey;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvannotation.AWebapiField;
import br.com.microserv.framework.msvinterface.tpInterface;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Ricardo on 16/11/2015.
 */
public class tpBase implements Cloneable, Serializable {

    // region constructor method
    public tpBase() {
        this.initialization();
    }
    // endregion


    // region initialization method
    // este metodo tem a finalidade de percorrer todas as propriedades da classe
    // e para os atributos que possuem anotacao definida ele vai inicializar o
    // valor da propriedade de acordo com a informacao do atributo initialValue
    // da anotacao
    public void initialization() {

        Class c = this.getClass();

        for (Field f : c.getDeclaredFields()){
            if (f.isAnnotationPresent(ATextField.class)){
                ATextField a = f.getAnnotation(ATextField.class);

                try{
                    f.set(this, a.initialValue());
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
    }
    // endregion


    // region isValid method
    // metodo utilizado para percorrer as propriedades da classe em busca da utilizacao
    // de anotacoes especificas e assim realizar a validacao do valor contido na propriedade
    // da classe de acordo com as caracteristicas descritas na anotacao
    public boolean isValid() {

        String stringValue;
        boolean booleanValue;
        double doubleValue;
        float floatValue;
        int    intValue;

        Class c = this.getClass();


        try {

            for (Field f : c.getDeclaredFields()) {

                if (f.isAnnotationPresent(ABooleanField.class)) {
                    ABooleanField a = f.getAnnotation(ABooleanField.class);
                    booleanValue = (boolean) f.get(this);

                    this.isValidBooleanField(booleanValue, a);
                }

                if (f.isAnnotationPresent(ADateField.class)) {
                    ADateField a = f.getAnnotation(ADateField.class);
                    stringValue = (String) f.get(this);

                    this.isValidDateField(stringValue, a);
                }

                if (f.isAnnotationPresent(ADoubleField.class)) {
                    ADoubleField a = f.getAnnotation(ADoubleField.class);
                    doubleValue = (double) f.get(this);

                    this.isValidDoubleField(doubleValue, a);
                }

                if (f.isAnnotationPresent(AFloatField.class)) {
                    AFloatField a = f.getAnnotation(AFloatField.class);
                    floatValue = (float) f.get(this);

                    this.isValidFloatField(floatValue, a);
                }

                if (f.isAnnotationPresent(AIntegerField.class)) {
                    AIntegerField i = f.getAnnotation(AIntegerField.class);
                    intValue = (int) f.get(this);

                    this.isValidIntegerField(intValue, i);
                }

                if (f.isAnnotationPresent(ATextField.class)) {
                    ATextField a = f.getAnnotation(ATextField.class);
                    stringValue = (String) f.get(this);

                    this.isValidTextField(stringValue, a);
                }

            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }
    // endregion


    // region isValidBooleanField method
    public boolean isValidBooleanField(boolean value, ABooleanField annotation)
            throws Exception {

        return true;
    }
    // endregion


    // region isValidDateField method
    public boolean isValidDateField(String value, ADateField annotation)
            throws Exception {

        return true;
    }
    // endregion


    // region isValidDoubleField method
    public boolean isValidDoubleField(double value, ADoubleField annotation)
            throws Exception {

        if (value < annotation.minValue()){
            throw new Exception("Valor menor do que o permitido para a propriedade");
        }

        if (value > annotation.maxValue()){
            throw new Exception("Valor maior do que o permitido para a propriedade");
        }

        if (annotation.places() >= 0){

            String f = "#.";

            for (int i = 0; i < annotation.places(); i++){
                f += "#";
            }

            DecimalFormat df = new DecimalFormat(f);
            df.setRoundingMode(RoundingMode.HALF_EVEN);

            value = Double.parseDouble(df.format(value));
        }

        return true;
    }
    // endregion


    // region isValidFloatField method
    public boolean isValidFloatField(float value, AFloatField annotation)
            throws Exception {

        if (value < annotation.minValue()){
            throw new Exception("Valor menor do que o permitido para a propriedade");
        }

        if (value > annotation.maxValue()){
            throw new Exception("Valor maior do que o permitido para a propriedade");
        }

        if (annotation.places() >= 0){

            String f = "#.";

            for (int i = 0; i < annotation.places(); i++){
                f += "#";
            }

            DecimalFormat df = new DecimalFormat(f);
            df.setRoundingMode(RoundingMode.HALF_EVEN);

            value = Float.parseFloat(df.format(value).replace(',','.'));
        }

        return true;
    }
    // endregion


    // region isValidIntegerField method
    public boolean isValidIntegerField(int value, AIntegerField annotation)
            throws Exception {

        if (value < annotation.minValue()){
            throw new Exception("Valor menor do que o permitido para a propriedade");
        }

        if (value > annotation.maxValue()){
            throw new Exception("Valor maior do que o permitido para a propriedade");
        }

        return true;
    }
    // endregion


    // region isValidTextField method
    public boolean isValidTextField(String value, ATextField annotation)
            throws Exception {

        if (value.length() < annotation.minLength()){
            throw new Exception("Quantidade de caracteres menor do que o necessário");
        }

        if (value.length() > annotation.maxLength()){
            throw new Exception("Quantidade de caracteres maior do que o necessário");
        }

        return true;
    }
    // endregion


    // region clone
    public tpInterface clone() throws CloneNotSupportedException {

        return (tpInterface) super.clone();

    }
    // endregion


    // region fillByJsonObject
    public boolean fillByJsonObject(JSONObject jObj) {

        boolean _out = true;


        Class c = this.getClass();

        try {

            for (Field f : c.getDeclaredFields()) {

                if(jObj.has(f.getName())) {

                    if (f.isAnnotationPresent(AWebapiField.class)) {

                        if (f.isAnnotationPresent(ABooleanField.class)) {
                            f.setBoolean(this, jObj.getBoolean(f.getName()));
                        }

                        if (f.isAnnotationPresent(ADateField.class)) {
                            // verificando se o valor contido não é a string null
                            if (jObj.getString(f.getName()).equals("null") == false) {
                                f.set(this, jObj.getString(f.getName()));
                            }
                        }

                        if (f.isAnnotationPresent(ADoubleField.class)) {
                            f.setDouble(this, jObj.getDouble(f.getName()));
                        }

                        if (f.isAnnotationPresent(AFloatField.class)) {
                            f.setFloat(this, (float) jObj.get(f.getName()));
                        }

                        if (f.isAnnotationPresent(AIntegerField.class)) {
                            f.setInt(this, jObj.getInt(f.getName()));
                        }

                        if (f.isAnnotationPresent(ALongField.class)) {
                            f.setLong(this, jObj.getLong(f.getName()));
                        }

                        if (f.isAnnotationPresent(ATextField.class)) {
                            // verificando se o valor contido não é a string null
                            if (jObj.getString(f.getName()).equals("null") == false) {
                                f.set(this, jObj.getString(f.getName()));
                            }
                        }

                    }

                }

            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        //} catch (InvocationTargetException e) {
        //    e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return _out;

    }
    // endregion


    // region getJSONObject
    public JSONObject getJSONObject() {

        JSONObject _jo = null;

        Class c = this.getClass();

        try {

            _jo = new JSONObject();

            for (Field f : c.getDeclaredFields()) {

                if (f.isAnnotationPresent(AWebapiField.class)) {
                    _jo.put(f.getName(), f.get(this));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return _jo;
        }

    }
    // endregion


    // region getListIdentifierValue
    public String getListIdentifierValue() {

        String _out = "";

        try {

            Class _c = this.getClass();

            for (Field _f : _c.getDeclaredFields()) {

                if (_f.isAnnotationPresent(AListIdentifierField.class)) {

                    _out = (String) _f.get(this);

                }

            }

        } catch (IllegalAccessException i) {
            _out = "Identificador de lista não definido";
        } catch (Exception e) {
            _out = "Erro ao tentar buscar o valor de identificação da lista";
        } finally {
            return _out;
        }

    }
    // endregion

}
