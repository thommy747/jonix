package org.jonix;

import org.jonix.mappers.IHeaderMapper;
import org.jonix.mappers.IProductMapper;

public enum JonixPackages
{
	v21_Reference("org.editeur.onix.v21.references", "http://www.editeur.org/onix/2.1/reference", new org.jonix.mappers.v21.references.ProductMapper(), new org.jonix.mappers.v21.references.HeaderMapper()),

	v21_Short("org.editeur.onix.v21.shorts", "http://www.editeur.org/onix/2.1/short", new org.jonix.mappers.v21.shorts.ProductMapper(), new org.jonix.mappers.v21.shorts.HeaderMapper()),

	v30_Reference("org.editeur.onix.v30.references", "http://ns.editeur.org/onix/3.0/reference", null, null),

	v30_Short("org.editeur.onix.v30.shorts", "http://ns.editeur.org/onix/3.0/short", null, null);

	public final String onixPackage;
	public final String onixNameSpace;
	public final IProductMapper productMapper;
	public final IHeaderMapper headerMapper;

	private JonixPackages(String onixPackage, String onixNameSpace, IProductMapper productMapper, IHeaderMapper headerMapper)
	{
		this.onixPackage = onixPackage;
		this.onixNameSpace = onixNameSpace;
		this.productMapper = productMapper;
		this.headerMapper = headerMapper;
	}
}
