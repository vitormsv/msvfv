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
public @interface AFloatField {

    // region initialValue
    // anotação utilizada para armazenar o valor inicial da propriedade que é utilizado
    // quando instânciamentos uma classe do tipo TP e o método initialization() é invocado
    // na classe tpBase()
    float initialValue() default 0;
    // endregion

    // region places
    // anotacao utilizada pada configurar a quantidade de casas decimais
    // que a propriedade ira manter quando o metodo isValid() for executado
    // na tpBase()
    int places() default 0;
    // endregion

    // region minValue
    // anotacao utilizada para configurar o valor minimo contido
    // dentro da propriedade da classe
    float minValue() default 0;
    // endregion

    // region maxValue
    // anotacao utilizada para configurar o valor maximo contido
    // dentro da propriedade da classe
    float maxValue() default 0;
    // endregion

}
