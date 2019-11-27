package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.ALongField;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvannotation.APrimaryKey;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

@AClass( table = "ReceitaWs" )
public class tpReceitaWs extends tpBase implements tpInterface {

    // region Nome
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Nome;
    // endregion


    // region Fantasia
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Fantasia;
    // endregion


    // region Email
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Email;
    // endregion


    // region Telefone
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Telefone;
    // endregion


    // region Logradouro
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Logradouro;
    // endregion


    // region Numero
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Numero;
    // endregion


    // region Complemento
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Complemento;
    // endregion


    // region Bairro
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Bairro;
    // endregion


    // region Municipio
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Municipio;
    // endregion


    // region Uf
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Uf;
    // endregion


    // region Cep
    @ATextField(
            initialValue = "",
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    public String Cep;
    // endregion

}
