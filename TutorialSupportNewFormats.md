# Introduction #

Following is a useful and simple example of how to derive a custom exporter. In this case, instead of generating a tab-delimited text, we generate an SQL file, consisting of INSERT statements. We override the method `onExportUniqueList()` where the exporting itself takes place.

# Details #

We chose to extend `JonixUniqueExporter`, which guarantees one record per ISBN, making it easier for us to use it as a table primary key.
The rest of the code is straightforward..

```
public class SqlExporter<H extends JonixTag, P extends JonixTagColumnable> extends JonixUniqueExporter<H, P>
{
	private DateFormat df;

	public SqlExporter(JonixFactory<H, P> jonixFactory, PrintStream out, PrintStream log, DateFormat df)
	{
		super(jonixFactory, out, log);
		this.df = df;
	}

	@Override
	protected void onExportUniqueList(List<ProductEx<P>> productsEx)
	{
		out.println("--truncate table onix;");

		for (ProductEx<P> productEx : productsEx)
		{
			String[][] prodMatrix = JonixFormatter.productAsStringMatrix(productEx._product, columns);
			out.print("insert into onix values (");
			for (int i = 0; i < prodMatrix.length; i++)
			{
				for (int j = 0; j < prodMatrix[i].length; j++)
				{
					if (i > 0 || j > 0)
						out.print(",");

					String value = prodMatrix[i][j];
					if (value == null)
						out.print("''"); // null
					else
					{
						out.print("'");
						out.print(value.replaceAll("'", "''"));
						out.print("'");
					}
				}
			}
			out.print(",'");
			out.print(df.format(productEx._timestamp.getTime()));
			out.println("');");
		}
	}
}
```