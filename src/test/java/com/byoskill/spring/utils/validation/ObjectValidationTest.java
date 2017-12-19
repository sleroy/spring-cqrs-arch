package com.byoskill.spring.utils.validation;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;

import org.junit.Assert;
import org.junit.Test;


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
	Assert.assertFalse(new ObjectValidation().isValid(new ObjectWithValidation()));
	Assert.assertTrue(new ObjectValidation().isValid(new ObjectWithValidation("try")));
    }

    @Test(expected=ConstraintViolationException.class)
    public final void testValidateWithErrors() {
	new ObjectValidation().validate(new ObjectWithValidation());
    }

    @Test()
    public final void testValidateWithoutErrors() {
	new ObjectValidation().validate(new ObjectWithValidation("str"));
    }

}
