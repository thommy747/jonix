package ac.jonix.example;

import java.io.Serializable;

import org.jonix.JonixComposite;
import org.jonix.JonixConsumer;
import org.jonix.JonixUtils;
import org.jonix.codelist.MeasureTypes;
import org.jonix.codelist.MeasureUnits;
import org.jonix.resolve.JonixResolver;
import org.jonix.resolve.ONIX;

public class Measures extends JonixComposite<Measures.Measure>
{
	private static final long serialVersionUID = 5628412245138447757L;

	public static class Measure implements Serializable
	{
		private static final long serialVersionUID = 757786695247515504L;

		public final MeasureTypes measureType;
		public final double measurement;
		public final MeasureUnits measureUnit;

		public Measure(MeasureTypes measureType, double measurement, MeasureUnits measureUnit)
		{
			this.measureType = measureType;
			this.measurement = measurement;
			this.measureUnit = measureUnit;
		}

		@Override
		public String toString()
		{
			return String.format("Measure %s = %.3f [%s]", measureType.name(), measurement, measureUnit.name());
		}
	}

	@Override
	public JonixConsumer consume(Object o, Object parent, Object grandParent, JonixResolver resolver)
	{
		if (resolver.onixTypeOf(o) == ONIX.Measure)
		{
			String measureTypeCode = (String) JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.MeasureTypeCode), "value");
			String measureUnitCode = (String) JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.MeasureUnitCode), "value", "value");
			String measurementStr = (String) JonixUtils.getProperty(o, resolver.onixPropOf(ONIX.Measurement), "value");

			add(new Measure(MeasureTypes.fromCode(measureTypeCode), Double.valueOf(measurementStr), MeasureUnits.fromCode(measureUnitCode)));
			return this;
		}

		return null;
	}
}
