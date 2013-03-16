package org.jonix.composite;

import java.io.Serializable;

import org.jonix.JonixComposite;
import org.jonix.JonixConsumer;
import org.jonix.JonixUtils;
import org.jonix.codelist.AudienceCodeTypes;
import org.jonix.codelist.AudienceCodes;
import org.jonix.resolve.JonixResolver;
import org.jonix.resolve.ONIX;

public class Audiences extends JonixComposite<Audiences.Audience>
{
	private static final long serialVersionUID = -1304790143558457014L;

	public static class Audience implements Serializable
	{
		private static final long serialVersionUID = 17308752263205814L;
		
		public final AudienceCodeTypes audienceCodeType;
		public final String audienceCodeTypeName;
		public final String audienceCodeValue;

		public Audience(AudienceCodeTypes audienceCodeType, String audienceCodeTypeName, String audienceCodeValue)
		{
			this.audienceCodeType = audienceCodeType;
			this.audienceCodeTypeName = audienceCodeTypeName;
			this.audienceCodeValue = audienceCodeValue;
		}

		@Override
		public String toString()
		{
			String audienceCodeTypeStr = (audienceCodeType == null) ? null : audienceCodeType.name();
			String audienceCodeValueStr = (audienceCodeType == AudienceCodeTypes.ONIX_audience_codes) ? AudienceCodes.fromCode(audienceCodeValue).name()
					: audienceCodeValue;
			return String.format("Audience [%s/%s]: %s", audienceCodeTypeStr, audienceCodeTypeName, audienceCodeValueStr);
		}
	}

	@Override
	public JonixConsumer consume(Object o, Object parent, Object grandParent, JonixResolver resolver)
	{
		if (resolver.onixTypeOf(o) == ONIX.Audience)
		{
			String audienceCodeTypeCode = (String) JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.AudienceCodeType), "value");
			String audienceCodeTypeName = (String) JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.AudienceCodeTypeName), "value");
			String audienceCodeValue = (String) JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.AudienceCodeValue), "value");
			add(new Audience(AudienceCodeTypes.fromCode(audienceCodeTypeCode), audienceCodeTypeName, audienceCodeValue));
			return this;
		}
		
		return null;
	}
}