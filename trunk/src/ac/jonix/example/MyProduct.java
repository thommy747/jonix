package ac.jonix.example;

import org.jonix.JonixColumn;
import org.jonix.JonixTagColumnable;
import org.jonix.basic.BasicColumn;
import org.jonix.basic.BasicProduct;
import org.jonix.codelist.MeasureTypes;
import org.jonix.resolve.JonixResolver;

import ac.jonix.example.Measures.Measure;

public class MyProduct extends BasicProduct
{
	public MyProduct(JonixResolver resolver)
	{
		super(resolver);
	}

	public final Measures measures = new Measures();

	@SuppressWarnings("unchecked")
	@Override
	public <P extends JonixTagColumnable> JonixColumn<P>[] getDefaultColumns()
	{
		return (JonixColumn<P>[]) new JonixColumn<?>[]
			{ BasicColumn.ISBN13, BasicColumn.Title, MyColumns.Height };
	}

	// LOOKUP SERVICES

	public Measure findMeasure(MeasureTypes requestedType)
	{
		for (Measure measure : measures)
		{
			if (measure.measureType == requestedType)
				return measure;
		}
		return null;
	}

}
