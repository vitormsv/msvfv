package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AClass;
import br.com.microserv.framework.msvannotation.ADateField;
import br.com.microserv.framework.msvannotation.ADoubleField;
import br.com.microserv.framework.msvannotation.AIntegerField;
import br.com.microserv.framework.msvannotation.ALongField;
import br.com.microserv.framework.msvannotation.APostAction;
import br.com.microserv.framework.msvannotation.APrimaryKey;
import br.com.microserv.framework.msvannotation.ATextField;
import br.com.microserv.framework.msvannotation.AWebapiField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 22/11/2016.
 */

@AClass( table = "Sincronizacao" )
public class tpSincronizacao extends tpBase implements tpInterface {

    // region _id
    @ALongField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = false,
            update = true,
            delete = true
    )
    @APrimaryKey
    public long _id;
    // endregion


    // region IdVendedor
    @ALongField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public long IdVendedor;
    // endregion


    // region InicioDataHora
    @ADateField(
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String InicioDataHora;
    // endregion


    // region TerminoDataHora
    @ADateField(
            allowNull = false
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public String TerminoDataHora;
    // endregion


    // region Latitude
    @ADoubleField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public double Latitude;
    // endregion


    // region Longitude
    @ADoubleField(
            initialValue = 0,
            minValue = 0,
            maxValue = 0
    )
    @APostAction(
            insert = true,
            update = true,
            delete = false
    )
    @AWebapiField
    public double Longitude;
    // endregion

}
