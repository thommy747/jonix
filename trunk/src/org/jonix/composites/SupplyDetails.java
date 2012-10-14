package org.jonix.composites;

import org.jonix.codelists.Availabilities;
import org.jonix.codelists.SupplierRoles;

@SuppressWarnings("serial")
public class SupplyDetails extends JonixComposite<SupplyDetails.SupplyDetail>
{
	public SupplyDetails()
	{
		super(SupplyDetail.class);
	}

	public static class SupplyDetail
	{
		public SupplierRoles supplierRole;
		public String supplierName;
		public Availabilities availability;
		public final Prices prices = new Prices();

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			for (Prices.Price price : prices)
				sb.append("\n    > ").append(price.toString());
			String supplierRoleStr = (supplierRole == null) ? null : supplierRole.name();
			String availabilityStr = (availability == null) ? null : availability.name();
			return String.format("SupplyDetail [%s]: %s (%s) %s", supplierRoleStr, supplierName, availabilityStr, sb.toString());
		}
	}
}
