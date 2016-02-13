/**
 * RDFProperty.java
 * 
 * RDFBeans Feb 4, 2011 10:44:05 PM alex
 *
 * $Id: RDFProperty.java 36 2012-12-09 05:58:20Z alexeya $
 *  
 */
package com.viceversatech.rdfbeans.reflect;

import java.beans.PropertyDescriptor;

import org.ontoware.rdf2go.model.node.URI;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFContainer;
import com.viceversatech.rdfbeans.annotations.RDFContainer.ContainerType;
import com.viceversatech.rdfbeans.exceptions.RDFBeanException;
import com.viceversatech.rdfbeans.exceptions.RDFBeanValidationException;

/**
 * RDFProperty.
 * 
 * @author alex
 * 
 */
public class RDFProperty extends AbstractRDFBeanProperty {

	private boolean inversionOfProperty;
	private URI uri;
	private RDFContainer.ContainerType containerType = ContainerType.NONE;

	/**
	 * @param propertyDescriptor
	 * @param rdfBeanInfo
	 * @throws RDFBeanException
	 */
	public RDFProperty(PropertyDescriptor propertyDescriptor,
			RDFBeanInfo rdfBeanInfo, RDF annotation,
			RDFContainer containerAnnotation) throws RDFBeanValidationException {
		super(propertyDescriptor);
		if (annotation != null) {
			String uriValue = null;
			if ((annotation.inverseOf() != null) && !annotation.inverseOf().isEmpty()) {
				uriValue = annotation.inverseOf();
				inversionOfProperty = true;
			}
			else if ((annotation.value() != null) && !annotation.value().isEmpty()){
				uriValue = annotation.value();
				inversionOfProperty = false;
			}
			if (uriValue == null) {
				throw new RDFBeanValidationException(
						"RDF property or \"inverseOf\" parameter is missing in "
								+ RDF.class.getName() + " annotation on  "
								+ propertyDescriptor.getReadMethod().getName()
								+ " method", rdfBeanInfo.getRDFBeanClass());
			}
			uri = rdfBeanInfo.createUri(uriValue);
			if (!uri.asJavaURI().isAbsolute()) {
				throw new RDFBeanValidationException(
						"RDF property URI parameter of " + RDF.class.getName()
								+ " annotation on "
								+ propertyDescriptor.getReadMethod().getName()
								+ " method  must be an absolute valid URI: "
								+ uriValue, rdfBeanInfo.getRDFBeanClass());

			}
			if (containerAnnotation != null) {
				if (inversionOfProperty) {
					throw new RDFBeanValidationException(
							RDFContainer.class.getSimpleName() + " annotation  on " + propertyDescriptor.getReadMethod().getName()
							+ " method is not allowed (\"inverseOf\" property)", rdfBeanInfo.getRDFBeanClass());							
				}
				containerType = containerAnnotation.value();
			}
		} else {
			throw new RDFBeanValidationException(rdfBeanInfo.getRDFBeanClass(),
					new NullPointerException());
		}
	}

	public boolean isInversionOfProperty() {
		return inversionOfProperty;
	}
	
	public URI getUri() {
		return uri;
	}

	public RDFContainer.ContainerType getContainerType() {
		return containerType;
	}

	/*
	@Override
	public Object getValue(Object rdfBean) throws RDFBeanException {
		if (isInversionOfProperty()) {
			//XXX
			return "XXX";
		}
		else {
			return super.getValue(rdfBean);
		}
	}

	@Override
	public void setValue(Object rdfBean, Object v) throws RDFBeanException {
		if (isInversionOfProperty()) {
			//no-op			
		}
		else {
			super.setValue(rdfBean, v);
		}
	}
	*/

}
