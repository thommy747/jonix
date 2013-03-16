package ac.jonix.example;

import org.jonix.JonixColumn;
import org.jonix.codelist.MeasureTypes;

import ac.jonix.example.Measures.Measure;

public enum MyColumns implements JonixColumn<MyProduct>
{
	Measures
	{
		@Override
		public int getRepetitions()
		{
			return 5;
		}

		@Override
		public String[] getSubColumnNames()
		{
			return new String[]
				{ "Measure", "Amount", "Unit" };
		}

		@Override
		public boolean extractTo(String[] fieldData, MyProduct product)
		{
			int pos = 0;
			for (Measure measure : product.measures)
			{
				fieldData[pos + 0] = measure.measureType.name();
				fieldData[pos + 1] = String.format("%.3f", measure.measurement);
				fieldData[pos + 2] = measure.measureUnit.name();
				if ((pos += 3) == fieldData.length)
					break;
			}
			return pos > 0;
		}
	},

	Height
	{

		@Override
		public int getRepetitions()
		{
			return 1;
		}

		@Override
		public String[] getSubColumnNames()
		{
			return new String[]
				{ "Height", "HeightUnit" };
		}

		@Override
		public boolean extractTo(String[] fieldData, MyProduct product)
		{
			Measure measure = product.findMeasure(MeasureTypes.Height);
			if (measure != null)
			{
				fieldData[0] = String.format("%.3f", measure.measurement);
				fieldData[1] = measure.measureUnit.name();
				return true;
			}
			return false;
		}
	};
}