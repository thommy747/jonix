Using JAXB, all tags in an ONIX file are de-serialized (a.k.a unmarshalled) into Java classes. The rules that govern how each tag is to be processed are defined in a schema provided by EDItEUR as an `XSD file`. JAXB provides an XSD "compiler", which automatically creates the code for the JAXB-annotated classes reflecting the XSD rules. Jonix contains these classes in the packages `org.editeur.onix.*`. However, if you wish to re-create these Java classes directly from the XSD file, this guide shows you how.

# Steps #
  1. Download the latest JAXB reference implementation (in a zip file) from [here](http://jaxb.java.net)
  1. Extract the zip file into a folder we'll refer to as **`JAXB_HOME`**
  1. Make sure that in the folder **`JAXB_HOME\bin`** is a file named **`xjc.bat`**
  1. Download a zip archive containing an XSD file along with some dependencies:
    * For ONIX 2, download [this zip](http://www.editeur.org/files/ONIX%202.1/ONIX_for_Books_Release2-1_rev03_schema+codes_Issue_18.zip)
    * For ONIX 3, download [this zip](http://www.editeur.org/files/ONIX%203/ONIX_BookProduct_XSD_schema+codes_Issue_18.zip)
  1. Unzip the the archive into a folder we'll refer to as **`XSD_HOME`**
  1. Make sure **`XSD_HOME`** contains the requested **`.xsd`** file, for instance: `ONIX_BookProduct_Release2.1_reference.xsd`
  1. In **`XSD_HOME`** create a text file, called **`bindings.xml`**, containing the lines:
```
<bindings version="2.0" xmlns="http://java.sun.com/xml/ns/jaxb">
  <globalBindings typesafeEnumMaxMembers="1000" />
</bindings>
```
  1. Create an output folder, into which all Java-generated files will be output
  1. Choose a package name for these output Java files, for instance: `org.editeur.onix.v21.references`
  1. In a command line, execute:
```
cd "<XSD_HOME>"

"<JAXB_HOME>\bin\xjc.bat" -d "<output-folder>" -p <package-name> "<xsd-file-name>" -b bindings.xml
```