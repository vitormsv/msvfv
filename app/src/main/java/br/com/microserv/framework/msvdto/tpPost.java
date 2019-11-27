package br.com.microserv.framework.msvdto;

import android.content.ContentValues;

import br.com.microserv.framework.msvannotation.AIntegerField;
import br.com.microserv.framework.msvannotation.ALongField;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by Ricardo on 29/11/2015.
 */
public class tpPost extends tpBase implements tpInterface {

    @ATextField(initialValue = "")
    public String table;

    @ATextField(initialValue = "")
    public String keyName;

    @ALongField(initialValue = 0)
    public long keyValue;


    public ContentValues values;


    public tpPost(){

        super();

    }


    @Override
    public void initialization(){

        // limpando o conteudo do objeto
        // ContentValues
        if (values == null) {
            values = new ContentValues();
        } else {
            values.clear();
        }


        // invocando o metodo de inicializacao da classe
        // base antes de inicializar os demais valores
        // esta classe
        super.initialization();

    }
}
