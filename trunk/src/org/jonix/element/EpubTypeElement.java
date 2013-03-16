package org.jonix.element;

import org.jonix.JonixElement;
import org.jonix.JonixUtils;
import org.jonix.codelist.EpubTypes;
import org.jonix.resolve.ONIX;

public final class EpubTypeElement extends JonixElement<EpubTypes>
{
	public EpubTypeElement()
	{
		super(ONIX.EpubType);
	}

	@Override
	protected EpubTypes consume(Object o)
	{
		return EpubTypes.fromCode(JonixUtils.getValueAsStr(o));
	}
}