SET JAXB_HOME=C:\DEV\tools\jaxb-ri-2.2.6

SET INPUT_FOLDER=C:\DEV\projects\jonix\doc\Onix3.0.1\schema
SET OUTPUT_FOLDER=C:\DEV\projects\jonix\gen

SET INPUT_REF=%INPUT_FOLDER%\ONIX_BookProduct_3.0_reference.xsd
SET INPUT_SHORT=%INPUT_FOLDER%\ONIX_BookProduct_3.0_short.xsd

CALL "%JAXB_HOME%\bin\xjc.bat" -d "%OUTPUT_FOLDER%" -p org.editeur.onix.v30.references "%INPUT_REF%" -b bindings.xml
CALL "%JAXB_HOME%\bin\xjc.bat" -d "%OUTPUT_FOLDER%" -p org.editeur.onix.v30.shorts "%INPUT_SHORT%" -b bindings.xml

PAUSE