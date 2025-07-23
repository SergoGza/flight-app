package com.tokioschool.flightapp.core.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class EnumValidImpl implements ConstraintValidator<EnumValid, String> {

  private List<String> entries;
  private boolean required;

  @Override
  public void initialize(EnumValid constraintAnnotation) {
    Enum<?>[] enumConstants = constraintAnnotation.target().getEnumConstants();
    // CORRECCIÓN: Asignar el resultado a la variable de instancia 'entries'
    this.entries = Arrays.stream(enumConstants).map(Enum::toString).toList();
    this.required = constraintAnnotation.required();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    String trimmed = StringUtils.trimToNull(value);

    if (!required && trimmed == null) {
      return true;
    }

    // Si es requerido y está vacío, no es válido
    if (required && trimmed == null) {
      return false;
    }

    return entries.contains(trimmed);
  }
}