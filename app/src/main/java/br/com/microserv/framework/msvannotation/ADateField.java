package br.com.microserv.framework.msvannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

/**
 * Created by Ricardo on 23/11/2015.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ADateField {

    // region initialValue
    // anotação utilizada para armazenar o valor inicial da propriedade que é utilizado
    // quando instânciamentos uma classe do tipo TP e o método initialization() é invocado
    // na classe tpBase()
    String initialValue() default "";
    // endregion

    // region displayFormat
    // anotacao utilizada para configurar o formato de como o valor
    // de um campo data que vem do banco de dados sera armazenado na
    // propriedade da classe
    String displayFormat() default "dd/MM/yyyy";
    // endregion

    // region allowNull
    // anotacao utilizada para configurar se a propriedade pode aceitar valor
    // nulo nas operacoes de insert e update no banco de dados
    boolean allowNull() default false;
    // endregion

    // region minValue
    // anotacao utilizada para configurar a data minima contida
    // dentro da propriedade da classe
    String minValue() default "";
    // endregion

    // region maxValue
    // anotacao utilizada para configurar a data maxima contida
    // dentro da propriedade da classe
    String maxValue() default "";
    // endregion
}
