package org.jonix.composite;

import java.io.Serializable;

import org.jonix.JonixComposite;
import org.jonix.JonixConsumer;
import org.jonix.JonixUtils;
import org.jonix.codelist.AvailabilityStatuses;
import org.jonix.codelist.SupplierRoles;
import org.jonix.resolve.JonixResolver;
import org.jonix.resolve.ONIX;

public class SupplyDetails extends JonixComposite<SupplyDetails.SupplyDetail>
{
	private static final long serialVersionUID = -6225128787940495004L;

	public static class SupplyDetail implements Serializable
	{
		private static final long serialVersionUID = -2597332679296933932L;

		public SupplierRoles supplierRole;
		public String supplierName;
		public AvailabilityStatuses availability;
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

	private transient SupplyDetail activeItem;

	@Override
	public JonixConsumer consume(Object o, Object parent, Object grandParent, JonixResolver resolver)
	{
		if (resolver.onixTypeOf(o) == ONIX.SupplyDetail)
		{
			add(activeItem = new SupplyDetail());
			return this;
		}

		if (resolver.onixTypeOf(parent) == ONIX.SupplyDetail)
		{
			switch (resolver.onixTypeOf(o))
			{
				case SupplierRole:
					activeItem.supplierRole = SupplierRoles.fromCode(JonixUtils.getValueAsStr(o));
					break;

				case SupplierName:
					activeItem.supplierName = JonixUtils.getValueAsStr(o);
					break;

				case AvailabilityCode:
					activeItem.availability = AvailabilityStatuses.fromCode((String) JonixUtils.getProperty(o, "value", "name()"));
					break;

				case Price:
					return activeItem.prices.consume(o, parent, grandParent, resolver);

				default:
					break;
			}
		}

		return null;
	}
}
