package org.jonix;

import org.jonix.codelists.EditionTypes;
import org.jonix.codelists.EpubTypes;
import org.jonix.codelists.NotificationTypes;
import org.jonix.codelists.ProductForms;
import org.jonix.composites.Audiences;
import org.jonix.composites.Contributors;
import org.jonix.composites.Languages;
import org.jonix.composites.MainSubjects;
import org.jonix.composites.OtherTexts;
import org.jonix.composites.ProductIdentifiers;
import org.jonix.composites.Publishers;
import org.jonix.composites.SalesRightss;
import org.jonix.composites.Serieses;
import org.jonix.composites.Subjects;
import org.jonix.composites.SupplyDetails;
import org.jonix.composites.Titles;

public class JonixProduct
{
	// elements
	public String recordReference;
	public NotificationTypes notificationType;
	public ProductForms productForm;
	public EpubTypes epubType;
	public EditionTypes editionType;
	public String editionNumber;
	public String countryOfPublication;
	public String cityOfPublication;
	public String publicationDate;
	public String numberOfPages;
	public String basicMainSubject;
	public String imprintName;

	// composites
	public final ProductIdentifiers productIdentifiers = new ProductIdentifiers();
	public final Titles titles = new Titles();
	public final Contributors contributors = new Contributors();
	public final Serieses serieses = new Serieses();
	public final Languages languages = new Languages();
	public final MainSubjects mainSubjects = new MainSubjects();
	public final Subjects subjects = new Subjects();
	public final Audiences audiences = new Audiences();
	public final OtherTexts otherTexts = new OtherTexts();
	public final Publishers publishers = new Publishers();
	public final SupplyDetails supplyDetails = new SupplyDetails();
	public final SalesRightss salesRightss = new SalesRightss();

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		appendElements(sb);
		appendComposites(sb);
		return sb.toString();
	}

	private void appendElements(StringBuilder sb)
	{
		if (recordReference != null)
			sb.append("RecordReference: ").append(recordReference).append("\n");
		if (notificationType != null)
			sb.append("NotificationType: ").append(notificationType.name()).append("\n");
		if (productForm != null)
			sb.append("ProductForm: ").append(productForm.name()).append("\n");
		if (epubType != null)
			sb.append("EpubType: ").append(epubType.name()).append("\n");
		if (editionType != null)
			sb.append("EditionType: ").append(editionType.name()).append("\n");
		if (editionNumber != null)
			sb.append("EditionNumber: ").append(editionNumber).append("\n");
		if (countryOfPublication != null)
			sb.append("CountryOfPublication: ").append(countryOfPublication).append("\n");
		if (cityOfPublication != null)
			sb.append("CityOfPublication: ").append(cityOfPublication).append("\n");
		if (publicationDate != null)
			sb.append("PublicationDate: ").append(publicationDate).append("\n");
		if (numberOfPages != null)
			sb.append("NumberOfPages: ").append(numberOfPages).append("\n");
		if (basicMainSubject != null)
			sb.append("BASICMainSubject: ").append(basicMainSubject).append("\n");
		if (imprintName != null)
			sb.append("ImprintName: ").append(imprintName).append("\n");
	}

	private void appendComposites(StringBuilder sb)
	{
		for (ProductIdentifiers.ProductIdentifier item : productIdentifiers)
			sb.append(item.toString()).append("\n");
		for (Titles.Title item : titles)
			sb.append(item.toString()).append("\n");
		for (Contributors.Contributor item : contributors)
			sb.append(item.toString()).append("\n");
		for (Serieses.Series item : serieses)
			sb.append(item.toString()).append("\n");
		for (Languages.Language item : languages)
			sb.append(item.toString()).append("\n");
		for (MainSubjects.Subject item : mainSubjects)
			sb.append(item.toString()).append("\n");
		for (Subjects.Subject item : subjects)
			sb.append(item.toString()).append("\n");
		for (Audiences.Audience item : audiences)
			sb.append(item.toString()).append("\n");
		for (OtherTexts.OtherText item : otherTexts)
			sb.append(item.toString()).append("\n");
		for (Publishers.Publisher item : publishers)
			sb.append(item.toString()).append("\n");
		for (SupplyDetails.SupplyDetail item : supplyDetails)
			sb.append(item.toString()).append("\n");
		for (SalesRightss.SalesRights item : salesRightss)
			sb.append(item.toString()).append("\n");
	}
}
