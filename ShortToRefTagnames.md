If you have an ONIX file with **Short** tag-names, which you wish to convert into the equivalent **Reference** tag-named file, you can use the XSLT scripts provided by EDItEUR as explained [here](http://www.editeur.org/15/Previous-Releases/#tagname) and [here](http://www.editeur.org/files/ONIX%203/ONIX%203.0%20tagname%20converter%20v1.html).

# Steps #

  * Create a folder containing the following files:
    1. the Short-tagnamed ONIX file you wish to convert
    1. one of the following XSLT files, depending on the version of your input file:
      * for ONIX 2 use [switch-onix-tagnames-2.0.xsl](http://www.editeur.org/files/ONIX%203/switch-onix-tagnames-2.0.xsl)
      * for ONIX 3 use [switch-onix-3.0-tagnames-2.0.xsl](http://www.editeur.org/files/ONIX%203/switch-onix-3.0-tagnames-2.0.xsl)
    1. the java library [Saxon9.jar](http://www.java2s.com/Code/JarDownload/saxon9/saxon9.jar.zip)

  * In a command line, execute:
```
cd "<folder-where-the-above-files-are>"

java -jar saxon9.jar "<my-short-onix.xml>" switch-onix-tagnames-2.0.xsl result-document="<my-output-onix.xml>"
```

# Comments #

  * Internet connection is required, probably for XML verification