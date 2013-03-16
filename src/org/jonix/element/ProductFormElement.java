package org.jonix.element;

import org.jonix.JonixElement;
import org.jonix.JonixUtils;
import org.jonix.codelist.ProductForms;
import org.jonix.resolve.ONIX;

public final class ProductFormElement extends JonixElement<ProductForms>
{
	public ProductFormElement()
	{
		super(ONIX.ProductForm);
	}

	@Override
	protected ProductForms consume(Object o)
	{
		return ProductForms.fromCode(JonixUtils.getValueAsStr(o));
	}
}