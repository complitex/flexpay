package org.flexpay.eirc.action.registry.corrections;

import org.flexpay.bti.persistence.apartment.BtiApartment;

public class SelectCorrectionTypeAction extends org.flexpay.payments.action.registry.corrections.SelectCorrectionTypeAction {

    @Override
    protected boolean checkApartmentType(int objectType) {
        return super.checkApartmentType(objectType) && typeRegistry.getType(BtiApartment.class) == objectType;
    }

}
