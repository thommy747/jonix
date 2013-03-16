SET JAXB_HOME=C:\DEV\tools\jaxb-ri-2.2.6

SET INPUT_FOLDER=C:\DEV\projects\jonix\doc\Onix2.1.4\schema
SET OUTPUT_FOLDER=C:\DEV\projects\jonix\gen

SET INPUT_REF=%INPUT_FOLDER%\ONIX_BookProduct_Release2.1_reference.xsd
SET INPUT_SHORT=%INPUT_FOLDER%\ONIX_BookProduct_Release2.1_short.xsd

CALL "%JAXB_HOME%\bin\xjc.bat" -d "%OUTPUT_FOLDER%" -p org.editeur.onix.v21.references "%INPUT_REF%" -b bindings.xml
CALL "%JAXB_HOME%\bin\xjc.bat" -d "%OUTPUT_FOLDER%" -p org.editeur.onix.v21.shorts "%INPUT_SHORT%" -b bindings.xml

PAUSE