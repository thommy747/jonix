package org.jonix.codegen;

import java.io.IOException;

public class CodeGen
{
	// folders
	private static final String BASE_FOLDER = "C:/DEV/projects/jonix";
	public static final String FORMAT_SPEC_ROOT_FOLDER = BASE_FOLDER + "/doc/editeur";
	public static final String CODELIST_HTMLS_FOLDER = BASE_FOLDER + "/doc/editeur/Onix3.0.1/codelists";
	public static final String GENERATAED_CODE_FOLDER = BASE_FOLDER + "/gen";

	// packages
	public static final String EDITEUR_HOME_PACKAGE = "org.editeur.onix";
	public static final String JONIX_RESOLVE_PACKAGE = "org.jonix.resolve";
	public static final String JONIX_CODELIST_PACKAGE = "org.jonix.codelist";

	// supported versions
	public static final int V_COUNT = 2;
	public static final String[] VERSION_POSTFIX =
		{ "v21", "v30" };
	public static final String[] VERSION_NAME =
		{ "2.1.04, April 2011", "3.0.01, January 2012" };
	public static final String[] HTML_NAME =
		{ "/Onix2.1.4/ONIX_for_Books_Format_Specification_2.1.4.html", "/Onix3.0.1/ONIX_for_Books_Format_Specification_3.0.1.html" };

	public static void main(String[] args) throws IOException
	{
		CodeGenTags cgTags = new CodeGenTags();
		cgTags.parseOnixTags();

		CodeGenLists cgCodelists = new CodeGenLists();
		cgCodelists.parseOnixCodeLists();
	}
}
