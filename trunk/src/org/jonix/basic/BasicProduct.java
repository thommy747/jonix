package org.jonix.basic;

import java.util.ArrayList;
import java.util.List;

import org.jonix.JonixColumn;
import org.jonix.JonixConsumer;
import org.jonix.JonixElement;
import org.jonix.JonixTagColumnable;
import org.jonix.JonixTag;
import org.jonix.codelist.ContributorRoles;
import org.jonix.codelist.EditionTypes;
import org.jonix.codelist.EpubTypes;
import org.jonix.codelist.LanguageRoles;
import org.jonix.codelist.NotificationTypes;
import org.jonix.codelist.PriceTypes;
import org.jonix.codelist.ProductForms;
import org.jonix.codelist.ProductIdTypes;
import org.jonix.codelist.SalesRightsTypes;
import org.jonix.codelist.SubjectSchemeIds;
import org.jonix.codelist.TextTypes;
import org.jonix.codelist.TitleTypes;
import org.jonix.composite.Audiences;
import org.jonix.composite.Contributors;
import org.jonix.composite.Contributors.Contributor;
import org.jonix.composite.Imprints;
import org.jonix.composite.Languages;
import org.jonix.composite.Languages.Language;
import org.jonix.composite.MainSubjects;
import org.jonix.composite.OtherTexts;
import org.jonix.composite.OtherTexts.OtherText;
import org.jonix.composite.Prices.Price;
import org.jonix.composite.ProductIdentifiers;
import org.jonix.composite.ProductIdentifiers.ProductIdentifier;
import org.jonix.composite.Publishers;
import org.jonix.composite.SalesRightss;
import org.jonix.composite.SalesRightss.SalesRights;
import org.jonix.composite.Serieses;
import org.jonix.composite.Subjects;
import org.jonix.composite.Subjects.Subject;
import org.jonix.composite.SupplyDetails;
import org.jonix.composite.SupplyDetails.SupplyDetail;
import org.jonix.composite.Titles;
import org.jonix.composite.Titles.Title;
import org.jonix.element.CountryOfPublicationElement;
import org.jonix.element.EditionTypeElement;
import org.jonix.element.EpubTypeElement;
import org.jonix.element.GenericStringElement;
import org.jonix.element.NotificationTypeElement;
import org.jonix.element.ProductFormElement;
import org.jonix.resolve.JonixResolver;
import org.jonix.resolve.ONIX;

/**
 * Container for an ONIX's <code>PRODUCT</code> tag, consisting of only the fields considered basic by Jonix. This object extends
 * {@link JonixTag}, which means it's being populated with by {@link JonixTag#process(Object)}, which goes over all of its registered
 * {@link JonixConsumer}s, and let them populate themselves by consuming the ONIX tag that they represent. The members of this class are
 * divided into two groups, following ONIX terminology -
 * <b>Elements</b> and <b>Composites</b>:
 * <ul>
 * <li>Elements are single-value tags, typically located directly under <code>PRODUCT</code>, and they are typically represented by a String
 * or an Enum. Examples include: Number-Of-Pages, Publication-Date, etc.
 * <li>Composites are basically <code>List</code>s of objects, some of which may themselves be lists. They represent repeatable ONIX tags.
 * Examples include: Contributors, Subjects, etc.
 * </ul>
 * In order to prevent this already-cumbersome structure from becoming even more cumbersome to use, getters and setters are not used, and
 * members are accessible directly. This does not create a risk, as once this object is populated, it's in the sole hands of the user to
 * read and modify.
 * <p>
 * The class also provides lookup services for most composites, given a lookup criteria.
 * 
 * @author Zach Melamed
 * 
 */
public class BasicProduct extends JonixTagColumnable
{
	// generic string elements
	public final GenericStringElement recordReference = new GenericStringElement(ONIX.RecordReference);
	public final GenericStringElement editionNumber = new GenericStringElement(ONIX.EditionNumber);
	public final GenericStringElement cityOfPublication = new GenericStringElement(ONIX.CityOfPublication);
	public final GenericStringElement publicationDate = new GenericStringElement(ONIX.PublicationDate);
	public final GenericStringElement numberOfPages = new GenericStringElement(ONIX.NumberOfPages);
	public final GenericStringElement bisacMainSubject = new GenericStringElement(ONIX.BASICMainSubject);
	public final GenericStringElement bicMainSubject = new GenericStringElement(ONIX.BICMainSubject);

	// custom elements
	public final JonixElement<NotificationTypes> notificationType = new NotificationTypeElement();
	public final JonixElement<ProductForms> productForm = new ProductFormElement();
	public final JonixElement<EpubTypes> epubType = new EpubTypeElement();
	public final JonixElement<EditionTypes> editionType = new EditionTypeElement();
	public final JonixElement<String> countryOfPublication = new CountryOfPublicationElement();

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
	public final Imprints imprints = new Imprints();
	public final SupplyDetails supplyDetails = new SupplyDetails();
	public final SalesRightss salesRightss = new SalesRightss();

	public BasicProduct(JonixResolver resolver)
	{
		super(resolver);
	}

	@Override
	public String getLabel()
	{
		return (titles.size() > 0) ? titles.get(0).titleText : recordReference.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P extends JonixTagColumnable> JonixColumn<P> getDefaultIdColumn()
	{
		return (JonixColumn<P>) BasicColumn.ISBN13;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P extends JonixTagColumnable> JonixColumn<P>[] getDefaultColumns()
	{
		return (JonixColumn<P>[]) BasicColumn.all;
	}

	// LOOKUP CONVENIENCE SERVICES

	public ProductIdentifier findProductId(ProductIdTypes requestedType)
	{
		for (ProductIdentifier prodId : productIdentifiers)
		{
			if (prodId.productIDType == requestedType)
				return prodId;
		}
		return null;
	}

	public Title findTitle(TitleTypes requestedType)
	{
		for (Title title : titles)
		{
			if (title.titleType == requestedType)
				return title;
		}
		return null;
	}

	public List<Contributor> findContributors(ContributorRoles requestedRole)
	{
		List<Contributor> matches = new ArrayList<Contributor>();
		for (Contributor contributor : contributors)
		{
			if (contributor.contributorRole == requestedRole)
				matches.add(contributor);
		}
		return matches;
	}

	public Language findLanguage(LanguageRoles requestedType)
	{
		for (Language language : languages)
		{
			if (language.languageRole == requestedType)
				return language;
		}
		return null;
	}

	public List<Subject> findSubjects(SubjectSchemeIds requestedScheme)
	{
		List<Subject> matches = new ArrayList<Subject>();

		if (requestedScheme == SubjectSchemeIds.BISAC_Subject_Heading)
		{
			if (bisacMainSubject.getValue() != null && !bisacMainSubject.getValue().isEmpty())
				matches.add(new Subject(SubjectSchemeIds.BISAC_Subject_Heading, bisacMainSubject.getValue()));
		}
		else if (requestedScheme == SubjectSchemeIds.BIC_subject_category)
		{
			if (bicMainSubject.getValue() != null && !bicMainSubject.getValue().isEmpty())
				matches.add(new Subject(SubjectSchemeIds.BIC_subject_category, bicMainSubject.getValue()));
		}

		for (Subject subject : mainSubjects)
		{
			if (subject.subjectSchemeIdentifier == requestedScheme)
				matches.add(subject);
		}
		for (Subject subject : subjects)
		{
			if (subject.subjectSchemeIdentifier == requestedScheme)
				matches.add(subject);
		}
		return matches;
	}

	public OtherText findOtherText(TextTypes requestedType)
	{
		for (OtherText otherText : otherTexts)
		{
			if (otherText.textType == requestedType)
				return otherText;
		}
		return null;
	}

	public List<Price> findPrices(List<PriceTypes> requestedTypes)
	{
		List<Price> matches = new ArrayList<Price>();
		for (SupplyDetail supplyDetail : supplyDetails)
		{
			for (Price price : supplyDetail.prices)
			{
				if (requestedTypes.contains(price.priceType))
					matches.add(price);
			}
		}
		return matches;
	}

	public List<SalesRights> findSalesRightss(List<SalesRightsTypes> requestedTypes)
	{
		List<SalesRights> matches = new ArrayList<SalesRights>();
		for (SalesRights salesRights : salesRightss)
		{
			if (requestedTypes.contains(salesRights.salesRightsType))
				matches.add(salesRights);
		}
		return matches;
	}

	// TODO: findPrices() by currencyCode and/or tax-status
	// TODO: findSeries()
	// TODO: findAudience()
	// TODO: findPublished()
}
