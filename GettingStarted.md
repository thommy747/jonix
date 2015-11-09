Jonix can be used as-is in many typical use cases. However, there are several scenarios where you need to extend it:
  1. You need more fields extracted from each ONIX record than what Jonix's built-in `BasicProduct` offers. In this case you'll need to develop new `JonixConsumer`s capable of extracting those fields from raw ONIX records.
  1. You need a different output format than what Jonix's canned exporters have to offer (these can be found under `org.jonix.export`). In this case you'll need to subclass the closest exporter and override proper method (based on your needs).
  1. You need to scan non file-system sources (such as DB, network, etc.). In this case you'll need to subclass `JonixScanner` and provide wrapper methods that find and pull the data from wherever it is, and hand it over to the `scan` method as `InputStream`.


# Typical Use Cases #

If what you need is to retrieve the basic fields of ONIX sources stored on the file-system, and then output them as a plain list (in memory or file) or as a tab-delimited text-file, using Jonix is straightforward.

If your goal is to export the ONIX contents into text/memory, pick one of the sub-classes of `JonixFilesExport` that suits you needs (they're all well documented), and instantiate it using `BasicHeader`, `BasicProduct` and `new BasicFactory()` as parameters. For your convenience, there's a static service creating each of them in the `Jonix` class.

**Example 1**: to read ONIX source(s) into an `ArrayList`, use the `JonixInMemExporter`, like that:

```
// allocate a List to retain the products parsed from the ONIX source(s)
List<BasicProduct> outputProducts = new ArrayList<BasicProduct>();

// create the exporter, passing it the target List and the log target (null means System.err)
JonixInMemExporter<BasicHeader, BasicProduct> exporter = Jonix.createBasicInMemExporter(outputProducts, null);

// scan a particular source, in this case a folder of .xml files formatted as v2.1-Short ONIX
exporter.scanFolder(JonixPackages.v21_Short, "C:\\onixFiles", ".xml");
```


**Example 2**: to output ONIX source(s) into a tab-delimited text file, where each ISBN appears once (only the latest occurrence is chosen if there are several), use the `JonixInMemExporter`, like that:

```
// set the output stream - a file in this case
PrintStream outputFile = new PrintStream("C:\\output.csv");

// create the exporter
exporter = Jonix.createBasicUniqueExporter(outputFile, null);

// scan two ONIX files, with different characteristics
exporter.scanFile(JonixPackages.v21_Reference, "C:\\myOnix2file.onx");
exporter.scanFile(JonixPackages.v30_Reference, "C:\\myOnix2file.onx", "UTF16");
```


If your goal is NOT to export the ONIX source(s), but to do something programmatic with it (search for a record, count records that meet specific criteria, etc.) - the exporters are not your best choice. What you're probably looking for in these cases is to subclass the core class `JonixFilesScanner` directly, as shown in the project home page:

```
// initialize a scanner object, capable of reading ONIX records one by one
JonixFilesScanner<BasicHeader, BasicProduct> scanner = new JonixFilesScanner<BasicHeader, BasicProduct>(new BasicFactory())
{
	@Override
	protected void onHeader(BasicHeader header)
	{
		// ONIX-Header was processed, do something with the data..
	}

	@Override
	protected void onProduct(BasicProduct product, int index)
	{
		// ONIX-Product (i.e. e-book) was processed, do something with the data..
	}
};

// having defined the scanner, we can use it to process ONIX sources of various types and locations 
scanner.scanFile(JonixPackages.v21_Reference, "MyOnix21.xml");
scanner.scanFile(JonixPackages.v21_Short, "MyOnix21-Short.xml");
scanner.scanFolder(JonixPackages.v30_Reference, "MyOnix30Folder", ".xml");
```


# Supporting new ONIX fields #

Check out [this tutorial](TutorialSupportNewFields.md) to see how develop a new Product class, capable of handling ONIX fields other than the default.


# Supporting new output formats #

Check out [this tutorial](TutorialSupportNewFormats.md) to see how create a new Exporter, for outputting the ONIX source in a custom format.