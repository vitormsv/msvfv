package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class tpClienteProdutoMixRow extends tpBase implements tpInterface {

    public long _id;
    public long IdCliente;
    public long IdProduto;
    public String ProdutoCodigo;
    public String ProdutoEan13;
    public String ProdutoDescricao;
    public String ProdutoUnidadeMedida;
    public String GrupoDescricao;
    public String LinhaDescricao;
    public int CompraQuantidadeVezes;
    public int CompraQuantidadeMaior;
    public long UltimaCompraData;
    public int UltimaCompraQuantidade;
    public double UltimaCompraValor;
    public double Preco;
    public int EstoqueQuantidade;
    public long EstoqueDataHora;
    public int ComprarQuantidade;

}
