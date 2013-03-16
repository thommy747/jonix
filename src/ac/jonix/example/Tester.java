package ac.jonix.example;

import org.jonix.JonixColumn;
import org.jonix.JonixPackages;
import org.jonix.basic.BasicColumn;
import org.jonix.basic.BasicHeader;
import org.jonix.export.JonixTabDelimitedExporter;

public class Tester
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		JonixTabDelimitedExporter<BasicHeader, MyProduct> exporter = new JonixTabDelimitedExporter<BasicHeader, MyProduct>(new MyFactory());
		exporter.setColumns((JonixColumn<MyProduct>[]) new JonixColumn<?>[]
			{ BasicColumn.ISBN13, BasicColumn.Title, MyColumns.Height });
		exporter.scanFile(JonixPackages.v21_Reference, "C:\\DEV\\projects\\jonix\\doc\\OnixSample.xml");
	}
}
