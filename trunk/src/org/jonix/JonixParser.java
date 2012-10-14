package org.jonix;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.jonix.JonixChunksHandler.OnChunkListener;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class JonixParser
{
	public static interface OnJonixItemListener
	{
		public void onHeader(JonixHeader header);

		public void onProduct(JonixProduct product, int index);
	}

	private final JonixPackages jonixPackage;
	private final XMLReader reader;

	public JonixParser(JonixPackages jonixPackage) throws SAXException, ParserConfigurationException, JAXBException
	{
		this.jonixPackage = jonixPackage;

		if (jonixPackage.productMapper == null)
			throw new UnsupportedOperationException("package " + jonixPackage.name() + " is still unimplemented");

		// create a new XML parser
		reader = createFactory().newSAXParser().getXMLReader();

		// prepare a chunk-by-chunk content handler
		JAXBContext context = JAXBContext.newInstance(jonixPackage.onixPackage);
		ContentHandler handler = new JonixChunksHandler(context, jonixPackage.onixNameSpace, onChunkListener);

		// connect the two
		reader.setContentHandler(handler);
	}

	/**
	 * creates a validation-free SAX factory
	 * 
	 * @return
	 */
	private SAXParserFactory createFactory()
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		try
		{
			factory.setFeature("http://xml.org/sax/features/validation", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		}
		catch (Exception e)
		{
			// not critical, we can continue
		}
		return factory;
	}

	private class OnChunkListenerImpl implements OnChunkListener
	{
		int productCount = 0;
		private OnJonixItemListener onJonixItemListener;

		public void reset(OnJonixItemListener onJonixItemListener)
		{
			productCount = 0;
			this.onJonixItemListener = onJonixItemListener;
		}

		@Override
		public void onChunk(Object onixObj)
		{
//			JonixUtils.print(onixObj, System.out);
			if (onixObj.getClass().getSimpleName().equals("Product"))
			{
				JonixProduct product = jonixPackage.productMapper.execute(onixObj);
				onJonixItemListener.onProduct(product, ++productCount);
			}
			else if (onixObj.getClass().getSimpleName().equals("Header") && jonixPackage.headerMapper != null)
			{
				JonixHeader header = jonixPackage.headerMapper.execute(onixObj);
				onJonixItemListener.onHeader(header);
			}
		}
	};

	private OnChunkListenerImpl onChunkListener = new OnChunkListenerImpl();

	public void parse(String fileName, final OnJonixItemListener onJonixItemListener) throws IOException, SAXException
	{
		onChunkListener.reset(onJonixItemListener);
		reader.parse(new InputSource(fileName));
	}

	public int getProductCount()
	{
		return onChunkListener.productCount;
	}
}
