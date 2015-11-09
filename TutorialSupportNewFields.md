# Introduction #

In this tutorial we'll add the `Measure` composite of ONIX, which describes the physical dimensions of a printed book. It has the following generalform:

```
<Measure>
	<MeasureTypeCode>01</MeasureTypeCode>
	<Measurement>9.25</Measurement>
	<MeasureUnitCode>in</MeasureUnitCode>
</Measure>
<Measure>
	<MeasureTypeCode>02</MeasureTypeCode>
	<Measurement>6.25</Measurement>
	<MeasureUnitCode>in</MeasureUnitCode>
</Measure>
<Measure>
	<MeasureTypeCode>03</MeasureTypeCode>
	<Measurement>1.2</Measurement>
	<MeasureUnitCode>in</MeasureUnitCode>
</Measure>
```

Being an ONIX composite, it repeats the same structure several times, each with its own values.

# Part 1: The Consumer #

The hardest part of supporting a new ONIX composite, is to develop its corresponding `JonixConsumer` - the class that will transform the above XML into an easy-to-use Java class. This transformation, actually, doesn't start with the XML string itself, but with a JAXB-generated Java representation of it. So we're facing a conversion from a very raw Java object into a more specific, Jonix-style object. This is exactly what `JonixConsumer` is all about.

So we start by finding out how JAXB would generate the raw Java objects. Open `ONIX.java`, which is a huge enumerator spanning all supported ONIX packages, and look for the object `Measure`:

```
/**
 * An optional and repeatable group of data elements which together identify a measurement and the units in which it is expressed.
 * 
 * @version
 *          2.1.04, April 2011
 * @version
 *          3.0.01, January 2012
 * @links
 *        {@link org.editeur.onix.v21.references.Measure}<br/>
 *        {@link org.editeur.onix.v21.shorts.Measure}<br/>
 *        {@link org.editeur.onix.v30.references.Measure}<br/>
 *        {@link org.editeur.onix.v30.shorts.Measure}<br/>
 */
Measure,
```

In its comment, there are links to several JAXB classes, all representing the `Measure` composites (in different versions/forms). For convenience, open the first, `org.editeur.onix.v21.references.Measure`, and look for the juicy parts:

```
...
@XmlType(name = "", propOrder = {
    "measureTypeCode",
    "measurement",
    "measureUnitCode"
})
@XmlRootElement(name = "Measure")
public class Measure {

    @XmlElement(name = "MeasureTypeCode", required = true)
    protected MeasureTypeCode measureTypeCode;
    @XmlElement(name = "Measurement", required = true)
    protected Measurement measurement;
    @XmlElement(name = "MeasureUnitCode", required = true)
    protected MeasureUnitCode measureUnitCode;
    ...
```

We see that the three items we're interested in (`measureTypeCode`, `measurement` and `measureUnitCode`) are all fields, and each has its own JAXB-generated type. The first, `measureTypeCode` is of type `MeasureTypeCode`, which, if you look at its implementation:
  * has a single property, `value`, stored as a String
  * refers to the code-list 48, as explained at the beginning of the class' comment
This code-list, like all code-lists, must have a corresponding Jonix enum in the package `org.jonix.codelist`. It can be located by the name and the comment it has. In this case, the Jonix enum from code-list 48 is the class `org.jonix.codelist.MeasureTypes`.

Go ahead and explore the other two types by yourself. You'll see that the third property, `measureUnitCode` is of type `MeasureUnitCode`, which has a `value` property of type `List50`, which in turn has its own `value` property, containing the String that we're after.

This is the kind of information needed to extract data from a raw JAXB object and convert it into a Jonix object. The class that would retain a single `Measure` record in our implementation is going to look like that:

```
public class Measure
{
	// we keep the members public and final, to avoid cumbersome getters/setters
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
```

The composite class, however, is going to be a `List` of such objects. We'll call it `Measures`. It will also have to implement `JonixConsumer` in order to "consume" the `<Measure>` tags during the parsing process. By convention in Jonix, an inner-class syntax is used, as follows:

```
public class Measures extends JonixComposite<Measures.Measure>
{
	public static class Measure implements Serializable
	{
		// shown above
	}

	@Override
	public JonixConsumer consume(Object o, Object parent, Object grandParent, JonixResolver resolver)
	{
		// coming up next..
	}
}
```

Note that `JonixComposite<T>` extends `LinkedList<T>`, so our composite class, `Measures` is, in fact, a list of `Measure` items.

The last thing left to do is to actually participate in the parsing process of an ONIX source and "snatch" the `Measure` tags into our composite class. The reader is encouraged to read the Javadocs pertaining to the parsing process in general and the consumption mechanism in particular (see the Javadocs for the interface `JonixConsumer` to begin with).
In general lines, when the parser reaches a new top-tag (i.e. right below `<Product>`) it asks all the consumers, who "knows" how to consume it. When a consumer says "I do", it well be given an exclusive access to the tag as well as its child tags (NOTE: in `Measure` tag, all the information we need is parsed by JAXB into properties at the parent level, so we don't need to handle sub-tags. This is not the case in more complex composites, such as `Price` and `Series`, whose consumption code you can study). So we need to do the following in the `consume()` method:
  * find out whether the raw Java object in question is a `Measure` tag (remember, it has a different tag-name in the Short form)
  * extract the values of the three properties in such a way that our code will work in all ONIX variants
  * signal to the parser whether or not we have consumed the tag
This is how it's done in Jonix:

```
@Override
public JonixConsumer consume(Object o, Object parent, Object grandParent, JonixResolver resolver)
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
```

The first `if` relies on the Jonix framework services. Given a `resolver`, which represents the specific ONIX version of the source, we can translate the object in question (passed as `o`) into an `ONIX` enum (which is shared among all ONIX versions). This enum is then compared to the enum object representing the tag we want to consume - `ONIX.Measure`.

Once we know that the object `o` represents a `Measure` JAXB-object, we extract the three properties. Again, we use a Jonix framework service, called `getProperty(o, properties..)`, which, given the object `o`, accesses one of its field (provided by name). If this field value is expected to be an object, a second field name can be passed, and so on. For example, we've already established that the value of the `measureUnitCode` property is actually stored in a path which can be described as follows:

`o` --> "measureUnitCode" --> "value" --> "value"

This path if passed to `getProperty()` just like that, with one exception: in the Short version of ONIX, the property names are different. To make our code generic, we use `resolver.onixPropOf()` which returns a version-depedant property name.

Once we have all three parameters, we create a `new Measure()` and call `add()` which is derived from `LinkedList`. This way, when all `<Measure>` tags in the ONIX source have been consumed, we have a `List` of `Measure` items, containing the parsed content of the original tags.


# Part 2: The Product and Factory #

We have a new `JonixConsumer` capable of processing the `<Measure>` tags. These tags are just part of a greater `<Product>` tag representing a while e-book metadata. In Jonix, the `<Product>` and `<Header>` root-tags are represented by a `JonixTag` class, which is, esentially, a container of `JonixConsumer`s who work together to consume the ONIX tags and populate the Product/Header.

Now that we know how to consume the `<Measure>` tag, we need to combine that in the context of a Product class. While not mandatory, its only reasonable to extend an Product class which already takes care of other tags for us. Jonix comes with a `BasicProduct` and `BasicHeader` implementation which are obvious super-classes for any extension of the package.

Here's what our Product class can look like:

```
public class MyProduct extends BasicProduct
{
	public MyProduct(JonixResolver resolver)
	{
		super(resolver);
	}

	public final Measures measures = new Measures();
}
```

That's it! The default behavior at `JonixTag` class, just before parsing, is to go over all public members that implement `JonixConsumer` and add them to the consumers list. So simply by adding a public member of type `Measures`, our derived class, `MyProduct` will consume all the tags handled by `BasicProduct` as well as the `<Measure>` tag.

To allow this subclass to be considered by the Jonix parser, we also have to provide a factory class, responsible for generating a Header class and a Product class whenever the parses needs these. In our case, as we are OK with the default Header class, `BasicHeader`, our factory looks like that:

```
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
```


# Part 3: Formatting #

For our subclass, `MyProduct` to take place benefit from Jonix formatting services (which is not mandatory), especially as tab-delimited text, we may want to implement the `JonixColumn<MyProduct>` interface. The best way to do it (although not the only one) is by using an enum that lists all the columns we wish to support (i.e. columns that will be available for tab-delimited rendering). We'll start with enum with one item:

```
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
	};
}
```

What the implementation means is that if `MyColumns.Measures` is included in a tab-delimited output, it would require 15 columns (for 5 types of measurements, each comprising 3 sub-columns: the measure-type, measure-value and measure-unit). Clearly, not all columns will always be filled. The outputting of the values into the 15-items long String array is performed in the implementation of `extractTo()`.

A more useful service for end users would be to extract a specific type of measurement, say Height. This will require a single column whose content is consistent between executions, hence very useful for spreadsheets and databases. We extend our enum:

```
public enum MyColumns implements JonixColumn<MyProduct>
{
	Measures
	{
		// Unchanged
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
```

The implementation relies of a look service at `MyProduct`:

```
public class MyProduct extends BasicProduct
{
	...
	
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
```


# Part 4: Executing #

Having defined all our classes, we can execute the following code, which reads an ONIX file and prints 3 columns of interest (including `Height`) as a tab-delimited text.

```
public static void main(String[] args)
{
	JonixTabDelimitedExporter<BasicHeader, MyProduct> exporter = new JonixTabDelimitedExporter<BasicHeader, MyProduct>(new MyFactory());
	
	exporter.setColumns((JonixColumn<MyProduct>[]) new JonixColumn<?>[]
		{ BasicColumn.ISBN13, BasicColumn.Title, MyColumns.Height });
		
	exporter.scanFile(JonixPackages.v21_Reference, "C:\\OnixSource.xml");
}
```

The columns order was explicitly stated in this example. Jonix allows the Product itself to define a default column set:

```
public class MyProduct extends BasicProduct
{
	...
	
	@Override
	public <P extends JonixTagColumnable> JonixColumn<P>[] getDefaultColumns()
	{
		return (JonixColumn<P>[]) new JonixColumn<?>[]
			{ BasicColumn.ISBN13, BasicColumn.Title, MyColumns.Height };
	}
=}
```

With this default, we can omit the `setColumns()` order in the `main()` method above.