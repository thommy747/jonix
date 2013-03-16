package org.jonix.composite;

import java.io.Serializable;

import org.jonix.JonixComposite;
import org.jonix.JonixConsumer;
import org.jonix.JonixUtils;
import org.jonix.codelist.TextFormats;
import org.jonix.codelist.TextTypes;
import org.jonix.resolve.JonixResolver;
import org.jonix.resolve.ONIX;

public class OtherTexts extends JonixComposite<OtherTexts.OtherText>
{
	private static final long serialVersionUID = 4221095225235652527L;

	public static class OtherText implements Serializable
	{
		private static final long serialVersionUID = 1090583454529037363L;

		public final TextTypes textType;
		public final TextFormats textFormat;
		public final String text;

		public OtherText(TextTypes textType, TextFormats textFormat, String text)
		{
			this.textType = textType;
			this.textFormat = textFormat;
			this.text = text;
		}

		@Override
		public String toString()
		{
			return String.format("OtherText [%s/%s]: %s", (textType == null) ? null : textType.name(), (textFormat == null) ? null : textFormat.name(), text);
		}
	}

	@Override
	public JonixConsumer consume(Object o, Object parent, Object grandParent, JonixResolver resolver)
	{
		if (resolver.onixTypeOf(o) == ONIX.OtherText)
		{
			String textTypeCode = (String) JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.TextTypeCode), "value");
			String textFormatCode = (String) JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.TextFormat), "value");

			String text = null;
			Object textObj = JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.Text));
			if (textObj != null)
			{
				text = JonixUtils.getContentAsStr(textObj);
				if (textFormatCode == null)
					textFormatCode = (String) JonixUtils.getProperty(textObj, "textformat");
			}
			add(new OtherText(TextTypes.fromCode(textTypeCode), TextFormats.fromCode(textFormatCode), text));
			return this;
		}

		return null;
	}
}
