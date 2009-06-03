package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class SelectCorrectionTypeAction extends org.flexpay.payments.actions.registry.corrections.SelectCorrectionTypeAction {

    @Override
    protected boolean checkApartmentType(int objectType) {
        return super.checkApartmentType(objectType) && typeRegistry.getType(BtiApartment.class) == objectType;
    }
}
