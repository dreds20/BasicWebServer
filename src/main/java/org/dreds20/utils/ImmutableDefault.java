package org.dreds20.utils;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
        optionalAcceptNullable = true,
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        overshadowImplementation = true,
        typeAbstract = "",
        typeImmutable = "*Impl",
        validationMethod = Value.Style.ValidationMethod.SIMPLE,
        depluralize = true
)
public @interface ImmutableDefault {
}
