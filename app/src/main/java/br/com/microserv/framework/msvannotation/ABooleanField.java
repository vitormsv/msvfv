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
public @interface ABooleanField {

    // region initialValue
    // anotação utilizada para armazenar o valor inicial da propriedade que é utilizado
    // quando instânciamentos uma classe do tipo TP e o método initialization() é invocado
    // na classe tpBase()
    boolean initialValue() default true;
    // endregion

}