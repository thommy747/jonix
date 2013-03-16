package org.jonix.codegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class CodeGenTags
{
	private class TagInfo
	{
		String[] shortName = new String[CodeGen.V_COUNT];
		String[] format = new String[CodeGen.V_COUNT];
		String[] codeList = new String[CodeGen.V_COUNT];
		String[] description = new String[CodeGen.V_COUNT];

		public TagInfo(String shortName, String format, String codeList, String description, int position)
		{
			set(shortName, format, codeList, description, position);
		}

		public void set(String shortName, String format, String codeList, String description, int position)
		{
			this.shortName[position] = shortName;
			this.format[position] = format;
			this.codeList[position] = codeList;
			this.description[position] = description;
		}

		public void generateComment(StringBuilder sb, String refName)
		{
			int i = firstActivePosition();
			sb.append("/**\n* ").append(description[i]).append("\n");
			if (format[i] != null)
				sb.append("* @format\n* ").append(format[i]).append("\n");
			if (codeList[i] != null)
				sb.append("* @codelist\n* ").append(codeList[i]).append("\n");
			for (int j = 0; j < CodeGen.V_COUNT; j++)
			{
				if (shortName[j] != null)
				{
					sb.append("* @version\n");
					sb.append("* ").append(CodeGen.VERSION_NAME[j]).append("\n");
				}
			}
			sb.append("* @links\n");
			for (int j = 0; j < CodeGen.V_COUNT; j++)
			{
				if (shortName[j] != null)
				{
					sb.append("* {@link ").append(refClassName(j, refName)).append("}<br/>\n");
					sb.append("* {@link ").append(shortClassName(j, shortName[j])).append("}<br/>\n");
				}
			}
			sb.append("*/\n");
		}

		private int firstActivePosition()
		{
			for (int i = 0; i < CodeGen.V_COUNT; i++)
				if (shortName[i] != null)
					return i;
			return -1;
		}
	}

	private Map<String, TagInfo> tagInfos = new LinkedHashMap<String, TagInfo>();

	public void parseOnixTags() throws IOException, FileNotFoundException
	{
		for (int i = 0; i < CodeGen.V_COUNT; i++)
			generateTagInfos(CodeGen.FORMAT_SPEC_ROOT_FOLDER + CodeGen.HTML_NAME[i], i);

		StringBuilder OnixEnumSB = new StringBuilder();
		OnixEnumSB.append("package " + CodeGen.JONIX_RESOLVE_PACKAGE + ";\n\n");
		OnixEnumSB.append("/**\n");
		OnixEnumSB.append(" * Master Enum, listing all ONIX types in all versions\n");
		OnixEnumSB.append(" * \n");
		OnixEnumSB.append(" * @author Zach Melamed\n");
		OnixEnumSB.append(" * \n");
		OnixEnumSB.append(" */\n");
		OnixEnumSB.append("public enum ONIX\n");
		OnixEnumSB.append("{\n");

		StringBuilder RefResolverSB[] = new StringBuilder[CodeGen.V_COUNT];
		StringBuilder ShortResolverSB[] = new StringBuilder[CodeGen.V_COUNT];
		for (int i = 0; i < CodeGen.V_COUNT; i++)
		{
			RefResolverSB[i] = new StringBuilder();
			ShortResolverSB[i] = new StringBuilder();
		}

		boolean first = true;
		Iterator<Entry<String, TagInfo>> iter = tagInfos.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, TagInfo> entry = iter.next();
			String refName = entry.getKey();
			TagInfo tagInfo = entry.getValue();

			if (!first)
				OnixEnumSB.append(",\n\n");
			else
				first = false;

			tagInfo.generateComment(OnixEnumSB, refName);
			OnixEnumSB.append(refName);

			for (int i = 0; i < CodeGen.V_COUNT; i++)
			{
				String shortName = tagInfo.shortName[i];
				if (shortName != null)
				{
					RefResolverSB[i].append("map.put(").append(refClassName(i, refName)).append(".class, ONIX.").append(refName).append(");\n");
					ShortResolverSB[i].append("map.put(").append(shortClassName(i, shortName)).append(".class, ONIX.").append(refName).append(");\n");
				}
			}
		}
		OnixEnumSB.append("\n}\n");

		String outputFolder = CodeGen.GENERATAED_CODE_FOLDER + "/resolve/";
		(new File(outputFolder)).mkdirs();

		System.setOut(new PrintStream(outputFolder + "ONIX.java", "UTF-8"));
		System.out.println(OnixEnumSB.toString());

		String resolverJavaFile = "package org.jonix.resolve;\n\n";
		resolverJavaFile += "public class %1$s extends JonixResolver\n";
		resolverJavaFile += "{\n";
		resolverJavaFile += "public %1$s()\n";
		resolverJavaFile += "{\n";
		resolverJavaFile += "%2$s";
		resolverJavaFile += "}\n";
		resolverJavaFile += "}";

		for (int i = 0; i < CodeGen.V_COUNT; i++)
		{
			String classNameRef = "JonixResolver_" + CodeGen.VERSION_POSTFIX[i] + "_reference";
			System.setOut(new PrintStream(outputFolder + classNameRef + ".java"));
			System.out.println(String.format(resolverJavaFile, classNameRef, RefResolverSB[i].toString()));

			String classNameShort = "JonixResolver_" + CodeGen.VERSION_POSTFIX[i] + "_short";
			System.setOut(new PrintStream(outputFolder + classNameShort + ".java"));
			System.out.println(String.format(resolverJavaFile, classNameShort, ShortResolverSB[i].toString()));
		}
	}

	private void generateTagInfos(String fileName, int position) throws IOException
	{
		Document doc = Jsoup.parse(new File(fileName), "UTF-8");
		Elements refs = doc.getElementsByClass("referencename");
		for (int i = 0; i < refs.size(); i++)
		{
			// extract the reference name
			String refName = strip(nextSiblingText(refs.get(i)));
			Elements shorts = refs.get(i).parent().getElementsByClass("shorttag");
			String shortName = (shorts.size() == 0) ? null : capitalizeFirstLetter(strip(nextSiblingText(shorts.first())));

			Elements formats = refs.get(i).parent().getElementsByClass("format");
			String format = (formats.size() == 0) ? null : unbreak(nextSiblingText(formats.first()));

			Elements codelists = refs.get(i).parent().getElementsByClass("codelist");
			String codeList = (codelists.size() == 0) ? null : unbreak(nextSiblingText(codelists.first()));

			String description = unbreak(refs.get(i).parent().parent().getElementsByTag("p").first().childNode(0).toString());

			TagInfo tagInfo = tagInfos.get(refName);
			if (tagInfo == null)
			{
				tagInfos.put(refName, tagInfo = new TagInfo(shortName, format, codeList, description, position));
				// System.err.println(refName);
			}
			else
			{
				if (tagInfo.shortName[position] == null)
				{
					tagInfo.set(shortName, format, codeList, description, position);
					if ((position > 0) && (tagInfo.shortName[position - 1] != null))
					{
						if (!tagInfo.shortName[position - 1].equals(shortName))
							System.err.println("INFO: <" + refName + "> has different shorts in different ONIX versions: <" + tagInfo.shortName[position - 1]
									+ "> and <" + shortName + ">");
					}
				}
				else
				{
					if (!tagInfo.shortName[position].equals(shortName))
						System.err.println("SEVERE: <" + refName + "> has two different shorts IN THE SAME ONIX VERSION: <" + tagInfo.shortName[position]
								+ "> and <" + shortName + ">");
					// if (!tagInfo.description[position].equals(description))
					// System.err.format("<" + refName + "> has two different descriptions:\n%s\n%s\n\n", tagInfo.description[position], description);
					continue;
				}
			}
		}
		System.err.println("FINISHED processing tag information for position " + position);
	}

	private String nextSiblingText(Node node)
	{
		// first 'while' is BFS - traverses siblings until non-empty node (i.e. which can hold text) is found
		while ((node = node.nextSibling()) != null)
		{
			// second 'while' is DFS - dives down into children until the bottom, child-less node is found
			while (node.childNodes().size() > 0)
			{
				node = node.childNode(0);
				if (node.childNodes().size() == 0)
					return node.toString();
			}
		}
		return null;
	}

	private String refClassName(int version, String refName)
	{
		return String.format("%s.%s.references.%s", CodeGen.EDITEUR_HOME_PACKAGE, CodeGen.VERSION_POSTFIX[version], refName);
	}

	private String shortClassName(int version, String shortName)
	{
		return String.format("%s.%s.shorts.%s", CodeGen.EDITEUR_HOME_PACKAGE, CodeGen.VERSION_POSTFIX[version], shortName);
	}

	private String strip(String x)
	{
		return x.replaceAll("\\ |&lt;|&gt;|\\n|/", "");
	}

	private String unbreak(String x)
	{
		return x.replaceAll("\\n", "");
	}

	private String capitalizeFirstLetter(String x)
	{
		return x.substring(0, 1).toUpperCase() + x.substring(1);
	}
}
