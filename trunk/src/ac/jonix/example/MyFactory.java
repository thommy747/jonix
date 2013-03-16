package ac.jonix.example;

import org.jonix.JonixParser.JonixFactory;
import org.jonix.basic.BasicHeader;
import org.jonix.resolve.JonixResolver;

public class MyFactory implements JonixFactory<BasicHeader, MyProduct>
{
	@Override
	public BasicHeader newHeaderProcessor(JonixResolver resolver)
	{
		return new BasicHeader(resolver);
	}
	
	@Override
	public MyProduct newProductProcessor(JonixResolver resolver)
	{
		return new MyProduct(resolver);
	}
}
