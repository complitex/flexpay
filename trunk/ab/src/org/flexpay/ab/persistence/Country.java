package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.EsbXmlSyncObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Country entity class
 */
public class Country extends EsbXmlSyncObject {

	private Set<CountryTranslation> translations = Collections.emptySet();
	private Set<Region> regions = Collections.emptySet();

	public Country() {
        super();
    }

	public Country(Long id) {
		super(id);
	}

    public Country(@NotNull Stub<Country> stub) {
		super(stub.getId());
	}

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <country>\n");

        if (ACTION_INSERT.equals(action)) {
            builder.append("        <translations>\n");
            for (CountryTranslation translation : getTranslations()) {
                builder.append("            <org.flexpay.mule.request.MuleTranslation>\n").
                        append("                <name>").append(translation.getName()).append("</name>\n").
                        append("                <shortName>").append(translation.getShortName()).append("</shortName>\n").
                        append("                <languageId>").append(translation.getLang().getId()).append("</languageId>\n").
                        append("            </org.flexpay.mule.request.MuleTranslation>\n");
            }
            builder.append("        </translations>\n");
        }

        builder.append("    </country>\n");

        return builder.toString();
    }

	public Set<Region> getRegions() {
		return regions;
	}

	public void setRegions(Set<Region> regions) {
		this.regions = regions;
	}

	public Set<CountryTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<CountryTranslation> translations) {
		this.translations = translations;
	}

	public void setTranslation(@NotNull CountryTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Country && super.equals(obj);

	}

}
