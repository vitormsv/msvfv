package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AListIdentifierField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class tpKeyValueRow extends tpBase implements tpInterface {

    public long Key;

    @AListIdentifierField
    public String Value;

    public int ImageResource;

    public tpKeyValueRow() {
        this.ImageResource = 0;
    }

}
