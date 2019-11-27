package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class tpProdutoPrecoRow extends tpBase implements tpInterface {

    public long _id;
    public String EmpresaSigla;
    public String TabelaPrecoDescricao;
    public double ProdutoPreco;

}
