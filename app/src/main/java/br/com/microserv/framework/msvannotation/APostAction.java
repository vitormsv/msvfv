package br.com.microserv.framework.msvannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Ricardo on 23/11/2015.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface APostAction {

    // region insert
    // atributo que vai classificar um FIELD informando se o valor do mesmo
    // será utilizado para operação de INSERT no banco de dados
    boolean insert();
    // endregion

    // region update
    // atributo que vai classificar um FIELD informando se o valor do mesmo
    // será utilizado para operação de UPDATE no banco de dados
    boolean update();
    // endregion

    // region update
    // atributo que vai classificar um FIELD informando se o valor do mesmo
    // será utilizado para operação de DELETE no banco de dados
    boolean delete();
    // endregion

}
