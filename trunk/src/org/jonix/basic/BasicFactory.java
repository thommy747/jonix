package org.jonix.basic;

import org.jonix.JonixParser.JonixFactory;
import org.jonix.resolve.JonixResolver;

/**
 * Trivial implementation of the {@link JonixFactory} interface required to parse an ONIX source. It generates {@link BasicProduct} and {@link BasicHeader},
 * hence suitable for using the 'vanilla' (i.e. Basic) implementations that come with Jonix.
 * 
 * @author Zach Melamed
 * 
 */
public class BasicFactory implements JonixFactory<BasicHeader, BasicProduct>
{
	@Override
	public BasicHeader newHeaderProcessor(JonixResolver resolver)
	{
		return new BasicHeader(resolver);
	}

	@Override
	public BasicProduct newProductProcessor(JonixResolver resolver)
	{
		return new BasicProduct(resolver);
	}
}