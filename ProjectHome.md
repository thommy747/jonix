# What is Jonix? #

**Jonix** a commercial-grade library for easily extracting data out of ONIX sources (typically files, but not limited to). It supports several variants of ONIX and is largely based on the the XML schemas provided by the maintainer of ONIX, [EDItEUR](http://www.editeur.org/).

# How it Works? #

Out of the box, Jonix is focused on the most important and commonly used fields in ONIX (while keeping it fairly trivial to extend the support to more fields). In the typical use case, you provide it with a set of ONIX sources (files, folders, etc.), and it responds by firing an event for each e-book record ("Product" in ONIX terminology) it read and processed. Each event passes a Java object containing the parsed product information, for your convenient analysis, for example:

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

Additionally, there are several ready-made (yet extendable) event handlers, to assist in exporting ONIX files into more manageable text formats, for instance:

```
String onixFolder = "C:\\MyOnixFolder";
JonixPackages onixVersion = JonixPackages.v21_Reference;
PrintStream outFile = new PrintStream("C:\\MyOnixAsTable.txt");

Jonix.createBasicTabDelimitedExporter(outFile, null).scanFolder(onixVersion, onixFolder, ".xml");
```

This will create a tabular, tab-delimited text-file with the major e-book fields from the input ONIX file. The output file can be opened as a spreadsheet, providing a very clear, one-row-per-book, view of the contents of the ONIX file (this clarity is almost impossible to achieve by viewing the ONIX file itself).

Jonix comes with a few more **Exporters** to choose from, and new ones can easily be sub-classed and developed. Additionally, the basic Product and Header implementations can be extended to support non-basic ONIX fields.

# ONIX-2-TXT in command line #

If all you need is converting an ONIX file into a tab-delimited text file (which can be opened with Excel), use the following in a command line:
```
java -jar jonix-2.0.jar [input-location] [output-file]
```
where
  * **input-locaion** is an ONIX file-name, or a folder containing ONIX files with xml extension
  * **output-file** is the name of the output file

# About ONIX #

ONIX is an XML-based international standard for storing and sharing title information between publishers, distributors and booksellers. It is extremely detailed and flexible, making it difficult to work with in bulk analysis processes (such as retrieving latest prices, book categories, sales rights, etc). On top of its inherent complexity, there exist several separate flavors of the the standard (most notably REFERENCE tags vs SHORT tags, and ONIX-2 vs ONIX-3 generations), and numerous external code-lists to consider.

# Author #

Zach (Tsahi) Melamed   [send me an e-mail](mailto:tsahim@gmail.com)