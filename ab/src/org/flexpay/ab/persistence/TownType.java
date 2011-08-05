package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TemporaryType;
import org.jetbrains.annotations.NotNull;

/**
 * TownType entity class holds a general representation of various types of localities, such as towns, villages, etc.
 */
public class TownType extends TemporaryType<TownType, TownTypeTranslation> {

	public TownType() {
	}

	public TownType(Long id) {
		super(id);
	}

	public TownType(@NotNull Stub<TownType> stub) {
		super(stub.getId());
	}

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <townType>\n");

        if (ACTION_UPDATE.equals(action)) {
            builder.append("        <id>").append(id).append("</id>\n");
        }

        if (ACTION_INSERT.equals(action) || ACTION_UPDATE.equals(action)) {
            builder.append("        <translations>\n");
            for (TownTypeTranslation translation : getTranslations()) {
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

        builder.append("    </townType>\n");

        return builder.toString();
    }

	/**
	 * Get null value
	 *
	 * @return Null representation of this value
	 */
	@Override
	public TownType getEmpty() {
		return new TownType();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TownType && super.equals(obj);
	}

}
