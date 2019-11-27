package br.com.microserv.framework.msvinterface;

/**
 * Created by Ricardo on 15/11/2015.
 */
public interface tpInterface {

    /*
     * Metodo responsavel por realizar a inicialização
     * dos valores das propriedades da classe
     */
    public abstract void initialization();


    /*
     * Metodo responsavel por realizar a validacao
     * das informações contidas nos atributos da classe
     */
    public abstract boolean isValid();


    /*
     * Metodo responsavel realizar seu próprio
     * clone quando está instanciado
     */
    public abstract tpInterface clone() throws CloneNotSupportedException;
}
