package org.jonix.element;

import org.jonix.JonixElement;
import org.jonix.JonixUtils;
import org.jonix.resolve.ONIX;

public final class CountryOfPublicationElement extends JonixElement<String>
{
	public CountryOfPublicationElement()
	{
		super(ONIX.CountryOfPublication);
	}

	@Override
	protected String consume(Object o)
	{
		return (String) JonixUtils.getProperty(o, "value", "name()");
	}
}