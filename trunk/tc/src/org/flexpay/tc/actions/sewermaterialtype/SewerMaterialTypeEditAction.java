package org.flexpay.tc.actions.sewermaterialtype;

import org.flexpay.bti.persistence.SewerMaterialType;
import org.flexpay.bti.persistence.SewerMaterialTypeTranslation;
import org.flexpay.bti.service.SewerMaterialTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class SewerMaterialTypeEditAction extends FPActionSupport {

    private SewerMaterialType sewerMaterialType = new SewerMaterialType();
    private Map<Long, String> names = CollectionUtils.treeMap();
    private Map<Long, String> descriptions = CollectionUtils.treeMap();

	private SewerMaterialTypeService sewerMaterialTypeService;

    @NotNull
    protected String doExecute() throws Exception {
        if (null == sewerMaterialType.getId()) {
            addActionError(getText("common.object_not_selected"));
            return REDIRECT_SUCCESS;
        }

        SewerMaterialType materialType = sewerMaterialType.isNew() ? sewerMaterialType : sewerMaterialTypeService.getSewerMaterialType(stub(sewerMaterialType));

        if (!isSubmit()) {
            sewerMaterialType = materialType;
            initNames();
            return INPUT;
        }


        for (Map.Entry<Long, String> name : names.entrySet()) {
            String value = name.getValue();
            Language lang = getLang(name.getKey());

            SewerMaterialTypeTranslation translation = new SewerMaterialTypeTranslation();
            translation.setLang(lang);
            translation.setName(value);
            translation.setDescription(descriptions.get(name.getKey()));
            materialType.setTranslation(translation);
        }

        sewerMaterialTypeService.save(materialType);

        return REDIRECT_SUCCESS;
    }

    private void initNames() {
        for (SewerMaterialTypeTranslation name : sewerMaterialType.getTranslations()) {
            names.put(name.getLang().getId(), name.getName());
            descriptions.put(name.getLang().getId(), name.getDescription());
        }

        for (Language lang : ApplicationConfig.getLanguages()) {
            if (names.containsKey(lang.getId())) {
                continue;
            }
            names.put(lang.getId(), "");
            descriptions.put(lang.getId(), "");
        }
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    public SewerMaterialType getSewerMaterialType() {
        return sewerMaterialType;
    }

    public void setSewerMaterialType(SewerMaterialType sewerMaterialType) {
        this.sewerMaterialType = sewerMaterialType;
    }

    public Map<Long, String> getNames() {
        return names;
    }

    public void setNames(Map<Long, String> names) {
        this.names = names;
    }

    public Map<Long, String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(Map<Long, String> descriptions) {
        this.descriptions = descriptions;
    }

	@Required
	public void setSewerMaterialTypeService(SewerMaterialTypeService sewerMaterialTypeService) {
		this.sewerMaterialTypeService = sewerMaterialTypeService;
	}

}
