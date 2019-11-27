package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvannotation.AListIdentifierField;
import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class tpItemCheck extends tpBase implements tpInterface {

    public long Key;

    public tpInterface Dto;

    public boolean Checked;

    public tpItemCheck() {
        Checked = false;
    }

    public tpItemCheck(
            long key,
            tpInterface dto,
            boolean checked
    ) {
        this.Key = key;
        this.Dto = dto;
        this.Checked = checked;
    }

}
