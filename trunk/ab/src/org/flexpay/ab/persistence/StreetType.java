package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TemporaryType;

/**
 * StreetType entity class holds a general representation of various types of streets.
 */
public class StreetType extends TemporaryType<StreetType, StreetTypeTranslation> {

	public StreetType() {
	}

	public StreetType(Long id) {
		super(id);
	}

    public StreetType(Stub<StreetType> stub) {
		super(stub.getId());
	}

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <streetType>\n");

        if (ACTION_UPDATE.equals(action)) {
            builder.append("        <id>").append(id).append("</id>\n");
        }

        if (ACTION_INSERT.equals(action) || ACTION_UPDATE.equals(action)) {
            builder.append("        <translations>\n");
            for (StreetTypeTranslation translation : getTranslations()) {
                builder.append("            <org.flexpay.mule.request.MuleTranslation>\n").
                        append("                <name>").append(translation.getName()).append("</name>\n").
                        append("                <shortName>").append(translation.getShortName()).append("</shortName>\n").
                        append("                <languageId>").append(translation.getLang().getId()).append("</languageId>\n").
                        append("            </org.flexpay.mule.request.MuleTranslation>\n");
            }
            builder.append("        </translations>\n");
        } else if (ACTION_DELETE.equals(action)) {
            builder.append("        <ids>\n");
            for (Long id : ids) {
                builder.append("            <long>").append(id).append("</long>\n");
            }
            builder.append("        </ids>\n");
        }

        builder.append("    </streetType>\n");

        return builder.toString();
    }

    /**
     * Get null value
     *
     * @return Null representation of this value
     */
    @Override
    public StreetType getEmpty() {
        return new StreetType();
    }

	@Override
	public boolean equals(Object obj) {
		return obj instanceof StreetType && super.equals(obj);
	}

}
