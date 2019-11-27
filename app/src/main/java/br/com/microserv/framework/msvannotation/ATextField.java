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
public @interface ATextField {

    // region initialValue
    // anotação utilizada para armazenar o valor inicial da propriedade que é utilizado
    // quando instânciamentos uma classe do tipo TP e o método initialization() é invocado
    // na classe tpBase()
    String initialValue() default "";
    // endregion

    // region
    // anotacao utilizada para configurar se a propriedade pode aceitar valor
    // nulo nas operacoes de insert e update no banco de dados
    boolean allowNull() default false;
    // endregion

    // region minLength
    // anotação utilizada para caracterizar a quantidade mínima de caracteres que
    // o valor de uma determinada propriedade deverá conter
    int minLength() default 0;
    // endregion

    // region maxLength
    // anotação utilizada para caracterizar a quantidade máxima de caracteres que
    // o valor de uma determinada propriedade deverá conter. este valor nunca deverá
    // ser menor do que o valor da anotação minLength()
    int maxLength() default 0;
    // endregion

    // region regularExpression
    // anotação utilizada para fornecer suporte a validação do valor contido dentro
    // da propriedade da classe através de expressão regular
    String regularExpression() default "";
    // endregion

}
