package org.jonix.element;

import org.jonix.JonixElement;
import org.jonix.JonixUtils;
import org.jonix.codelist.EditionTypes;
import org.jonix.resolve.ONIX;

public final class EditionTypeElement extends JonixElement<EditionTypes>
{
	public EditionTypeElement()
	{
		super(ONIX.EditionType);
	}

	@Override
	protected EditionTypes consume(Object o)
	{
		return EditionTypes.fromCode((String) JonixUtils.getProperty(o, "value", "name()"));
	}
}