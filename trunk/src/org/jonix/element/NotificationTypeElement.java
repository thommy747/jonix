package org.jonix.element;

import org.jonix.JonixElement;
import org.jonix.JonixUtils;
import org.jonix.codelist.NotificationTypes;
import org.jonix.resolve.ONIX;

public final class NotificationTypeElement extends JonixElement<NotificationTypes>
{
	public NotificationTypeElement()
	{
		super(ONIX.NotificationType);
	}

	@Override
	protected NotificationTypes consume(Object o)
	{
		return NotificationTypes.fromCode(JonixUtils.getValueAsStr(o));
	}
}