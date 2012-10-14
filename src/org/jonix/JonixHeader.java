package org.jonix;

public class JonixHeader
{
	// elements
	public String FromCompany;
	public String FromPerson;
	public String FromEmail;
	public String ToCompany;
	public String SentDate;
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		appendElements(sb);
		return sb.toString();
	}

	private void appendElements(StringBuilder sb)
	{
		if (FromCompany != null)
			sb.append("FromCompany: ").append(FromCompany).append("\n");
		if (FromPerson != null)
			sb.append("FromPerson: ").append(FromPerson).append("\n");
		if (FromEmail != null)
			sb.append("FromEmail: ").append(FromEmail).append("\n");
		if (ToCompany != null)
			sb.append("ToCompany: ").append(ToCompany).append("\n");
		if (SentDate != null)
			sb.append("SentDate: ").append(SentDate).append("\n");
	}
}
