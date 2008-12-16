package org.flexpay.common.persistence;

public enum FlexPayFileType {

    TARIF("common.file_type.tarif", "common.file_type.tarif.mask"),
    CHARACTERISTICS("common.file_type.characteristics", "common.file_type.characteristics.mask"),
    SUBSIDY("common.file_type.subsidy", "common.file_type.subsidy.mask"),
    SRV("common.file_type.srv", "common.file_type.srv.mask"),
    FORM2_FILES("common.file_type.form2", "common.file_type.form2.mask"),
    UNKNOWN("common.file_type.unknown", "");

    private String message;
    private String mask;

    private FlexPayFileType(String message, String mask) {
        this.message = message;
        this.mask = mask;
    }

    public String getMask() {
        return mask;
    }

    @Override
    public String toString() {
        return message;
    }

}
