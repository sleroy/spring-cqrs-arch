package com.byoskill.spring.cqrs.utils.validation;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.constraints.NotEmpty;

import org.junit.Assert;
import org.junit.Test;

import com.byoskill.spring.cqrs.utils.validation.ObjectValidation;


public class ObjectValidationTest {

    public class ObjectWithValidation {
	@NotEmpty
	public String str;

	public ObjectWithValidation() {
	    super();
	}

	public ObjectWithValidation(final String string) {
	    str = string;
	}
    }

    @Test
    public final void testIsValid() {
	Assert.assertFalse(new ObjectValidation(Validation.buildDefaultValidatorFactory().getValidator()).isValid(new ObjectWithValidation()));
	Assert.assertTrue(new ObjectValidation(Validation.buildDefaultValidatorFactory().getValidator()).isValid(new ObjectWithValidation("try")));
    }

    @Test(expected=ConstraintViolationException.class)
    public final void testValidateWithErrors() {
	new ObjectValidation(Validation.buildDefaultValidatorFactory().getValidator()).validate(new ObjectWithValidation());
    }

    @Test()
    public final void testValidateWithoutErrors() {
	new ObjectValidation(Validation.buildDefaultValidatorFactory().getValidator()).validate(new ObjectWithValidation("str"));
    }

}
