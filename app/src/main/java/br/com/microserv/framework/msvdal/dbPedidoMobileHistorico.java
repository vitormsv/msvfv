package br.com.microserv.framework.msvdal;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import br.com.microserv.framework.msvbase.dbBase;
import br.com.microserv.framework.msvdto.tpCidade;
import br.com.microserv.framework.msvdto.tpCliente;
import br.com.microserv.framework.msvdto.tpCondicaoPagamento;
import br.com.microserv.framework.msvdto.tpEmpresa;
import br.com.microserv.framework.msvdto.tpPedidoMobile;
import br.com.microserv.framework.msvdto.tpPedidoMobileHistorico;
import br.com.microserv.framework.msvdto.tpPedidoMobileItem;
import br.com.microserv.framework.msvdto.tpProduto;
import br.com.microserv.framework.msvdto.tpTabelaPreco;
import br.com.microserv.framework.msvdto.tpTipoPedido;
import br.com.microserv.framework.msvdto.tpTransportadora;
import br.com.microserv.framework.msvdto.tpVendedor;
import br.com.microserv.framework.msvhelper.SQLiteHelper;
import br.com.microserv.framework.msvinterface.dbInterface;
import br.com.microserv.framework.msvinterface.tpInterface;
import br.com.microserv.framework.msvutil.SQLClauseHelper;

/**
 * Created by notemsv01 on 21/11/2016.
 */

public class dbPedidoMobileHistorico extends dbBase implements dbInterface {

    private SQLiteHelper _dbHelper = null;


    // region Constructor
    public dbPedidoMobileHistorico(SQLiteHelper dbHelper) throws Exception {
        super(dbHelper);
        _dbHelper = dbHelper;
    }
    // endregion


    // region newDTO
    @Override
    public tpInterface newDTO(){

        return new tpPedidoMobileHistorico();

    }
    // endregion

}
