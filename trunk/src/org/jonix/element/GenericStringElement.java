package org.jonix.element;

import org.jonix.JonixElement;
import org.jonix.JonixUtils;
import org.jonix.resolve.ONIX;

public class GenericStringElement extends JonixElement<String>
{
	public GenericStringElement(ONIX onix)
	{
		super(onix);
	}

	@Override
	protected String consume(Object o)
	{
		return JonixUtils.getValueAsStr(o);
	}
}
