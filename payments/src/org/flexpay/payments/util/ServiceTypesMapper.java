package org.flexpay.payments.util;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.config.MbServiceTypeMapping;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.List;

/**
 * Mapper maps Megabank service type codes to internal {@link org.flexpay.payments.persistence.ServiceType}
 */
public class ServiceTypesMapper {

	private BidiMap mapping = new DualHashBidiMap();

	/**
	 * Map megabank service type code to internal ServiceType object reference
	 *
	 * @param mbCode Megabank service type code
	 * @return Service type stub
	 */
	@SuppressWarnings ({"unchecked"})
	public Stub<ServiceType> getInternalType(String mbCode) {
		return (Stub<ServiceType>) mapping.get(mbCode);
	}

	/**
	 * Map internal service type to Megabank code
	 *
	 * @param stub Internal service type stub
	 * @return Megabank service type code
	 */
	public String getMegabankCode(Stub<ServiceType> stub) {
		return (String) mapping.getKey(stub);
	}

	@Required
	public void setHibernateTemplate(HibernateTemplate template) {
		@SuppressWarnings ({"unchecked"})
		List<MbServiceTypeMapping> mappings = template.findByNamedQuery("MbServiceTypeMapping.listAll");
		if (mappings.isEmpty()) {
			throw new IllegalStateException("No MegaBank service mappings found, did you set it?");
		}

		for (MbServiceTypeMapping srvMapping : mappings) {
			mapping.put(srvMapping.getMbServiceCode(), srvMapping.serviceTypeStub());
		}
	}
}
