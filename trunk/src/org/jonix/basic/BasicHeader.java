package org.jonix.basic;

import org.jonix.JonixTag;
import org.jonix.element.GenericStringElement;
import org.jonix.resolve.JonixResolver;
import org.jonix.resolve.ONIX;

/**
 * Container for an ONIX's <code>HEADER</code> tag, consisting of only the fields considered basic by Jonix. This object is being populated
 * very similarly to {@link BasicProduct}, but unlike the latter, it contains only Elements (no Composites at this stage)
 * <p>
 * Similarly to {@link BasicProduct}, here, too, getters and setters are not used, and members are directly accessible.
 * 
 * @author Zach Melamed
 * 
 */
public class BasicHeader extends JonixTag
{
	// elements
	public final GenericStringElement fromCompany = new GenericStringElement(ONIX.FromCompany);
	public final GenericStringElement fromPerson = new GenericStringElement(ONIX.FromPerson);
	public final GenericStringElement fromEmail = new GenericStringElement(ONIX.FromEmail);
	public final GenericStringElement toCompany = new GenericStringElement(ONIX.ToCompany);
	public final GenericStringElement sentDate = new GenericStringElement(ONIX.SentDate);

	public BasicHeader(JonixResolver resolver)
	{
		super(resolver);
	}
}
