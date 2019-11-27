package br.com.microserv.framework.msvinterface;

import android.database.Cursor;

/**
 * Created by notemsv01 on 11/11/2016.
 */

public interface ActivityInterface {

    /*
     * Contastantes utilizadas para troca de informações
     * entre activitys
     */
    String s_KEY_METODO_EDICAO = "MetodoEdicao";
    String s_KEY_SOURCE_ACTIVITY = "SourceActivity";

    int i_INSERT_VALUE = 0;
    int i_UPDATE_VALUE = 1;
    int i_DELETE_VALUE = 2;
    int i_BROWSE_VALUE = 3;


    /*
     * Metodo responsavel realizar o vinculo entre
     * os elementos descritos no xml e as classes
     * definidas na activity
     */
    void bindElements();


    /*
     * Metodo responsável por anexar eventos ao
     * elementos contidos dentro da activity
     */
    void bindEvents();


    /*
     * Metodo responsável por inicializar uma
     * activity
     */
    void initialize();
}
