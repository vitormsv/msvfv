package br.com.microserv.framework.msvdto;

import br.com.microserv.framework.msvbase.tpBase;
import br.com.microserv.framework.msvinterface.tpInterface;

/**
 * Created by notemsv01 on 14/11/2016.
 */

public class tpClienteListaRow extends tpBase implements tpInterface {

    public long _id;
    public long IdCliente;
    public String Codigo;
    public String RazaoSocial;
    public String NomeFantasia;
    public String CidadeNome;
    public String EstadoSigla;
    public String Cep;
    public int PedidoQuantidade;
    public int HistoricoQuantidade;

}
