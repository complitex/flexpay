/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.engine.fill;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRRenderable;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context used to store data shared by virtualized objects resulted from a report fill process.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRVirtualizationContext.java 4595 2011-09-08 15:55:10Z teodord $
 */
public class JRVirtualizationContext implements Serializable
{
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	private static final Logger log = LoggerFactory.getLogger(JRVirtualizationContext.class);
	
	private static final ReferenceMap contexts = new ReferenceMap(ReferenceMap.WEAK, ReferenceMap.WEAK);
	
	private Map<String,JRRenderable> cachedRenderers;
	private Map<String,JRTemplateElement> cachedTemplates;
	
	private boolean readOnly;
	
	/**
	 * Constructs a context.
	 */
	public JRVirtualizationContext()
	{
		cachedRenderers = new HashMap<String,JRRenderable>();
		cachedTemplates = new HashMap<String,JRTemplateElement>();
	}

	
	/**
	 * Caches an image renderer.
	 * 
	 * @param image the image whose renderer should be cached
	 */
	public void cacheRenderer(JRPrintImage image)
	{
		JRRenderable renderer = image.getRenderer();
		if (renderer != null)
		{
			cachedRenderers.put(renderer.getId(), renderer);
		}
	}

	
	/**
	 * Retrieves a cached image renderer based on an ID.
	 * 
	 * @param id the ID
	 * @return the cached image renderer for the ID
	 */
	public JRRenderable getCachedRenderer(String id)
	{
		return cachedRenderers.get(id);
	}

	
	/**
	 * Determines whether a cached image renderer for a specified ID exists.
	 * 
	 * @param id the ID
	 * @return <code>true</code> if and only if the context contains a cached renderer with the specified ID
	 */
	public boolean hasCachedRenderer(String id)
	{
		return cachedRenderers.containsKey(id);
	}

	
	/**
	 * Determines whether a cached {@link JRTemplateElement template} with a specified ID exists.
	 * 
	 * @param id the template ID
	 * @return <code>true</code> if and only if the context contains a cached template with the specified ID
	 */
	public boolean hasCachedTemplate(String id)
	{
		return cachedTemplates.containsKey(id);
	}
	
	
	/**
	 * Caches an element template.
	 * 
	 * @param template the template to cache
	 */
	public void cacheTemplate(JRTemplateElement template)
	{
		Object old = cachedTemplates.put(template.getId(), template);
		if (old == null && log.isDebugEnabled())
		{
			log.debug("Cached template " + template + " having id " + template.getId());
		}
	}
	
	
	/**
	 * Retrieves a cached template.
	 * 
	 * @param templateId the template ID
	 * @return the cached template having the given ID
	 */
	public JRTemplateElement getCachedTemplate(String templateId)
	{
		return cachedTemplates.get(templateId);
	}


	/**
	 * Determines whether this context has been marked as read-only.
	 * 
	 * @return whether this context has been marked as read-only
	 * @see #setReadOnly(boolean)
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}


	/**
	 * Sets the read-only flag for this context.
	 * <p>
	 * When in read-only mode, all the virtualizable objects belonging to this context
	 * are assumed final by the virtualizer and any change in a virtualizable object's data
	 * would be discarded on virtualization.
	 * 
	 * @param readOnly the read-only flag
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}
	
	
	/**
	 * Registers a virtualization context for {@link JasperPrint JasperPrint} object.
	 * 
	 * @param context the virtualization context
	 * @param print the print object
	 */
	public static void register(JRVirtualizationContext context, JasperPrint print)
	{
		synchronized (contexts)
		{
			contexts.put(print, context);
		}
	}

	
	/**
	 * Returns the virtualization context registered for a print object.
	 * <p>
	 * When the engine fills a report using a virtualizer, it {@link #register(JRVirtualizationContext, JasperPrint) registers}
	 * the virtualization context with the generated {@link JasperPrint JasperPrint} object so that the caller
	 * would be able to retrieve the context based on the returned print object.
	 * 
	 * @param print a print object
	 * @return the virtualization context registered for the print object, or <code>null</code> if no context
	 * has been registered
	 */
	public static JRVirtualizationContext getRegistered(JasperPrint print)
	{
		synchronized (contexts)
		{
			return (JRVirtualizationContext) contexts.get(print);
		}
	}
}
