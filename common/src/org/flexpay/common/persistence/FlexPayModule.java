package org.flexpay.common.persistence;

public enum FlexPayModule {

    AB_MODULE("common.module.ab"),
    BTI_MODULE("common.module.bti"),
    COMMON_MODULE("common.module.common"),
    EIRC_MODULE("common.module.eirc"),
    SZ_MODULE("common.module.sz");

    private String message;

    private FlexPayModule(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
