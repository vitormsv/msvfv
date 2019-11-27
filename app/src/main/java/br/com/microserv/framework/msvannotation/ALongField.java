package br.com.microserv.framework.msvannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Ricardo on 16/11/2015.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ALongField {

    // region initialValue
    // anotação utilizada para armazenar o valor inicial da propriedade que é utilizado
    // quando instânciamentos uma classe do tipo TP e o método initialization() é invocado
    // na classe tpBase()
    long initialValue() default 0;
    // endregion

    // region minValue
    // anotacao utilizada para configurar o valor minimo contido
    // dentro da propriedade da classe
    long minValue() default 0;
    // endregion

    // region maxValue
    // anotacao utilizada para configurar o valor maximo contido
    // dentro da propriedade da classe
    long maxValue() default 0;
    // endregion

}
