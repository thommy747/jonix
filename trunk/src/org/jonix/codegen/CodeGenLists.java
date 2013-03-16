package org.jonix.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CodeGenLists
{
	private static class ListInfo
	{
		final String enumClassName;
		final int onixListId;

		public ListInfo(String enumClassName, int onixListId)
		{
			this.enumClassName = enumClassName;
			this.onixListId = onixListId;
		}
	}

	private static final ListInfo[] CODELISTS =
	{ 
		new ListInfo("NotificationTypes", 1), 
		new ListInfo("ProductCompositions", 2), 
		new ListInfo("RecordSourceTypes", 3), 
		new ListInfo("ProductIdTypes", 5), 
		new ListInfo("BarcodeIndicators", 6), 
		new ListInfo("ProductForms", 7), 
		new ListInfo("ProductClassificationTypes", 9), 
		new ListInfo("EpubTypes", 10), 
		new ListInfo("EpubFormats", 11), 
		new ListInfo("TradeCategories", 12), 
		new ListInfo("SeriesIdTypes", 13), 
		new ListInfo("TextCaseFlags", 14), 
		new ListInfo("TitleTypes", 15), 
		new ListInfo("WorkIdTypes", 16), 
		new ListInfo("ContributorRoles", 17), 
		new ListInfo("PersonOrganizationNameTypes", 18), 
		new ListInfo("UnnamedPersons", 19), 
		new ListInfo("ConferenceRoles", 20), 
		new ListInfo("EditionTypes", 21), 
		new ListInfo("LanguageRoles", 22), 
		new ListInfo("ExtentTypes", 23), 
		new ListInfo("ExtentUnits", 24), 
		new ListInfo("IllustrationAndOtherContentTypes", 25), 
		new ListInfo("MainSubjectSchemeIds", 26), 
		new ListInfo("SubjectSchemeIds", 27), 
		new ListInfo("AudienceCodes", 28), 
		new ListInfo("AudienceCodeTypes", 29), 
		new ListInfo("AudienceRangeQualifiers", 30), 
		new ListInfo("AudienceRangePrecisions", 31), 
		new ListInfo("ComplexitySchemeIds", 32), 
		new ListInfo("TextTypes", 33), 
		new ListInfo("TextFormats", 34), 
		new ListInfo("TextLinkTypes", 35), 
		new ListInfo("FrontCoverImageFileFormats", 36), 
		new ListInfo("FrontCoverImageFileLinkTypes", 37), 
		new ListInfo("ImageAudioVideoFileTypes", 38), 
		new ListInfo("ImageAudioVideoFileFormats", 39), 
		new ListInfo("ImageAudioVideoFileLinkTypes", 40), 
		new ListInfo("PrizeOrAwardAchievements", 41), 
		new ListInfo("TextItemTypes", 42), 
		new ListInfo("TextItemIdTypes", 43), 
		new ListInfo("NameCodeTypes", 44), 
		new ListInfo("PublishingRoles", 45), 
		new ListInfo("SalesRightsTypes", 46), 
		new ListInfo("RightsRegions", 47), 
		new ListInfo("MeasureTypes", 48), 
		new ListInfo("Regions", 49), 
		new ListInfo("MeasureUnits", 50), 
		new ListInfo("ProductRelations", 51), 
		new ListInfo("SupplyToRegions", 52), 
		new ListInfo("ReturnsConditionsCodeTypes", 53), 
		new ListInfo("AvailabilityStatuses", 54), 
		new ListInfo("DateFormats", 55), 
		new ListInfo("AudienceRestrictionFlags", 56), 
		new ListInfo("UnpricedItemTypes", 57), 
		new ListInfo("PriceTypes", 58), 
		new ListInfo("PriceTypeQualifiers", 59), 
		new ListInfo("UnitOfPricings", 60), 
		new ListInfo("PriceStatuses", 61), 
		new ListInfo("TaxRates", 62), 
		new ListInfo("IntermediarySupplierAvailabilities", 63), 
		new ListInfo("PublishingStatuses", 64), 
		new ListInfo("ProductAvailabilities", 65), 
		new ListInfo("BisacReturnableIndicators", 66), 
		new ListInfo("MarketDateRoles", 67), 
		new ListInfo("MarketPublishingStatuses", 68), 
		new ListInfo("AgentRoles", 69), 
		new ListInfo("StockQuantityCodeTypes", 70), 
		new ListInfo("SalesRestrictionTypes", 71), 
		new ListInfo("ThesisTypes", 72), 
		new ListInfo("WebsiteRoles", 73), 
		new ListInfo("LanguageCodeIso6392Bs", 74), 
		new ListInfo("PersonDateRoles", 75), 
		new ListInfo("ProductFormFeatureValueDvdRegionCodess", 76), 
		new ListInfo("NorthAmericanSchoolOrCollegeGrades", 77), 
		new ListInfo("ProductFormDetails", 78), 
		new ListInfo("ProductFormFeatureTypes", 79), 
		new ListInfo("ProductPackagingTypes", 80), 
		new ListInfo("ProductContentTypes", 81), 
		new ListInfo("BibleContentss", 82), 
		new ListInfo("BibleVersions", 83), 
		new ListInfo("StudyBibleTypes", 84), 
		new ListInfo("BiblePurposes", 85), 
		new ListInfo("BibleTextOrganizations", 86), 
		new ListInfo("BibleReferenceLocations", 87), 
		new ListInfo("ReligiousTextIds", 88), 
		new ListInfo("ReligiousTextFeatureTypes", 89), 
		new ListInfo("ReligiousTextFeatures", 90), 
		new ListInfo("CountryCodeIso31661s", 91), 
		new ListInfo("SupplierIdTypes", 92), 
		new ListInfo("SupplierRoles", 93), 
		new ListInfo("DefaultLinearUnits", 94), 
		new ListInfo("DefaultUnitOfWeights", 95), 
		new ListInfo("CurrencyCodeIso4217s", 96), 
		new ListInfo("BibleTextFeatures", 97), 
		new ListInfo("ProductFormFeatureValueBindingOrPageEdgeColors", 98), 
		new ListInfo("ProductFormFeatureValueSpecialCoverMaterials", 99), 
		new ListInfo("DiscountCodeTypes", 100), 
		new ListInfo("PersonNameIdTypes", 101), 
		new ListInfo("SalesOutletIdTypes", 102), 
		new ListInfo("TextScriptCodeIso15924s", 121), 
		new ListInfo("TransliterationSchemes", 138), 
		new ListInfo("OnixSalesOutletIdss", 139), 
		new ListInfo("UsCpsiaChokingHazardWarnings", 140), 
		new ListInfo("BarcodeIndicators", 141), 
		new ListInfo("PositionOnProducts", 142), 
		new ListInfo("UsCpsiaChokingHazardWarnings", 143), 
		new ListInfo("EpubTechnicalProtections", 144), 
		new ListInfo("UsageTypes", 145), 
		new ListInfo("UsageStatuses", 146), 
		new ListInfo("UnitOfUsages", 147), 
		new ListInfo("CollectionTypes", 148), 
		new ListInfo("TitleElementLevels", 149), 
		new ListInfo("ProductForms", 150), 
		new ListInfo("ContributorPlaceRelators", 151), 
		new ListInfo("IllustratedNotIllustrateds", 152), 
		new ListInfo("TextTypes", 153), 
		new ListInfo("ContentAudiences", 154), 
		new ListInfo("ContentDateRoles", 155), 
		new ListInfo("CitedContentTypes", 156), 
		new ListInfo("ContentSourceTypes", 157), 
		new ListInfo("ResourceContentTypes", 158), 
		new ListInfo("ResourceModes", 159), 
		new ListInfo("ResourceFeatureTypes", 160), 
		new ListInfo("ResourceForms", 161), 
		new ListInfo("ResourceVersionFeatureTypes", 162), 
		new ListInfo("PublishingDateRoles", 163), 
		new ListInfo("WorkRelations", 164), 
		new ListInfo("SupplierOwnCodeTypes", 165), 
		new ListInfo("SupplyDateRoles", 166), 
		new ListInfo("PriceConditionTypes", 167), 
		new ListInfo("PriceConditionQuantityTypes", 168), 
		new ListInfo("QuantityUnits", 169), 
		new ListInfo("DiscountTypes", 170), 
		new ListInfo("TaxTypes", 171), 
		new ListInfo("CurrencyZones", 172), 
		new ListInfo("PriceDateRoles", 173), 
		new ListInfo("PrintedOnProducts", 174), 
		new ListInfo("ProductFormDetails", 175), 
		new ListInfo("ProductFormFeatureValueOperatingSystems", 176), 
		new ListInfo("PersonOrganizationDateRoles", 177), 
		new ListInfo("SupportingResourceFileFormats", 178), 
		new ListInfo("PriceCodeTypes", 179), 
		new ListInfo("EuToySafetyDirectiveHazardWarnings", 184), 
		new ListInfo("EpubAccessibilityDetailss", 196), 
		new ListInfo("CollectionSequenceTypes", 197), 
		new ListInfo("ProductContactRoles", 198), 
		new ListInfo("OnixAdultAudienceRatings", 203), 
		new ListInfo("OnixReturnsConditionss", 204) 
	};

	public void parseOnixCodeLists() throws IOException
	{
		String outputFolder = CodeGen.GENERATAED_CODE_FOLDER + "/codelist/";
		(new File(outputFolder)).mkdirs();
		
		for (ListInfo li : CODELISTS)
		{
			String fileName = outputFolder + li.enumClassName + ".java";
			System.err.println(fileName);
			System.setOut(new PrintStream(fileName, "UTF-8"));
			System.out.println(generateJavaEnumFile(li.onixListId, li.enumClassName));
		}
	}

	private String generateJavaEnumFile(int onixListId, String enumClassName) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + CodeGen.JONIX_CODELIST_PACKAGE + ";\n\n");
		sb.append("/**\n");
		sb.append(" * Enum that corresponds to Onix's CodeList " + onixListId + "\n");
		sb.append(" * \n");
		sb.append(" * @author Zach Melamed\n");
		sb.append(" * \n");
		sb.append(" */\n");
		sb.append("public enum " + enumClassName + "\n");
		sb.append("{\n");

		Set<String> enumNames = new HashSet<String>();

		Document doc = Jsoup.parse(new File(String.format("%s/onix-codelist-%d.htm", CodeGen.CODELIST_HTMLS_FOLDER, onixListId)), "UTF-8");
		Elements rows = doc.getElementsByTag("tbody").first().getElementsByTag("tr");
		boolean first = true;
		for (int i = 3; i < rows.size(); i++)
		{
			Element tr = rows.get(i);
			if (tr.children().size() == 3)
			{
				String enumCode = tr.child(0).text().trim();
				String enumName = tr.child(1).text().trim().replaceAll(";.*$|\\-|\\(|\\)|'|’", "").replaceAll("\\W", "_").replaceAll("^([0-9])", "_$0");
				String enumComment = tr.child(2).text().trim();

				while (!enumNames.add(enumName))
					enumName += "_";

				if (!first)
					sb.append(",\n\n");
				else
					first = false;
				
				if (!enumComment.isEmpty())
					enumComment = String.format("\t/**\n\t * %s\n\t */\n", enumComment);
				sb.append(String.format("%s\t%s(\"%s\")", enumComment, enumName, enumCode));
			}
		}
		sb.append(";\n\n");
		sb.append("\tpublic final String code;\n\n");
		sb.append("\tprivate " + enumClassName + "(String code)\n");
		sb.append("\t{\n");
		sb.append("\t\tthis.code = code;\n");
		sb.append("\t}\n\n");
		sb.append("\tprivate static " + enumClassName + "[] values = " + enumClassName + ".values();\n");
		sb.append("\tpublic static " + enumClassName + " fromCode(String code)\n");
		sb.append("\t{\n");
		sb.append("\t\tif (code != null && !code.isEmpty())\n");
		sb.append("\t\t\tfor (" + enumClassName + " item : values)\n");
		sb.append("\t\t\t\tif (item.code.equals(code))\n");
		sb.append("\t\t\t\t\treturn item;\n");
		sb.append("\t\treturn null;\n");
		sb.append("\t}\n");
		sb.append("}\n");

		String result = sb.toString();
		return result;
	}
}
