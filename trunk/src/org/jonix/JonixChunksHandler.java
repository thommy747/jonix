package org.jonix;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;

public class JonixChunksHandler extends XMLFilterImpl
{
	public static interface OnChunkListener
	{
		public void onChunk(Object onixObj);
	}
	
	private final JAXBContext context;
	private String globalNS;
	private OnChunkListener onChunkListener;
	private int depth = 0;

	public JonixChunksHandler(JAXBContext context, String globalNS, OnChunkListener onChunkListener)
	{
		this.context = context;
		this.globalNS = globalNS;
		this.onChunkListener = onChunkListener;
	}

	private Locator locator;

	@Override
	public void setDocumentLocator(Locator locator)
	{
		super.setDocumentLocator(locator);
		this.locator = locator;
	}
	
	private UnmarshallerHandler unmarshallerHandler;
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		if (depth == 1)
		{
			Unmarshaller unmarshaller;
			try
			{
				unmarshaller = context.createUnmarshaller();
			}
			catch (JAXBException e)
			{
				throw new SAXException(e);
			}
			
			unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
			setContentHandler(unmarshallerHandler);
			unmarshallerHandler.startDocument();
			unmarshallerHandler.setDocumentLocator(locator);
		}
		
		super.startElement(globalNS, localName, qName, atts);
		depth++;
	}

	private DefaultHandler nullHandler = new DefaultHandler();
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		super.endElement(globalNS, localName, qName);
		depth--;
		
		if (depth == 1)
		{
			unmarshallerHandler.endDocument();
			setContentHandler(nullHandler);
			try
			{
				onChunkListener.onChunk(unmarshallerHandler.getResult());
			}
			catch (JAXBException je)
			{
				System.err.println("unable to process line " + locator.getLineNumber());
				return;
			}
			unmarshallerHandler = null;
		}
	}
	
	public OnChunkListener getOnChunkListener()
	{
		return onChunkListener;
	}
}
