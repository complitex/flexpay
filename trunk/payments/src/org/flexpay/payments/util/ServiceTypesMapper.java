package org.flexpay.payments.util;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.persistence.config.MbServiceTypeMapping;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.List;

/**
 * Mapper maps Megabank service type codes to internal {@link org.flexpay.payments.persistence.ServiceType}
 */
public class ServiceTypesMapper {

	private JpaTemplate jpaTemplate;
	private BidiMap mapping = new DualHashBidiMap();

	/**
	 * Map megabank service type code to internal ServiceType object reference
	 *
	 * @param mbCode Megabank service type code
	 * @return Service type stub
	 */
	@SuppressWarnings ({"unchecked"})
	public Stub<ServiceType> getInternalType(String mbCode) {
		initializeMapping();
		return (Stub<ServiceType>) mapping.get(mbCode);
	}

	/**
	 * Map internal service type to Megabank code
	 *
	 * @param stub Internal service type stub
	 * @return Megabank service type code
	 */
	public String getMegabankCode(Stub<ServiceType> stub) {
		initializeMapping();
		return (String) mapping.getKey(stub);
	}

	private void initializeMapping() {

		if (!mapping.isEmpty()) {
			return;
		}

		@SuppressWarnings ({"unchecked"})
		List<MbServiceTypeMapping> mappings = jpaTemplate.findByNamedQuery("MbServiceTypeMapping.listAll");
		if (mappings.isEmpty() && !ApplicationConfig.disableSelfValidation()) {
			throw new IllegalStateException("No MegaBank service mappings found, did you set it?");
		}

		for (MbServiceTypeMapping srvMapping : mappings) {
			mapping.put(srvMapping.getMbServiceCode(), srvMapping.serviceTypeStub());
		}
	}

	@Required
	public void setJpaTemplate(JpaTemplate template) {
		this.jpaTemplate = template;
	}
}
