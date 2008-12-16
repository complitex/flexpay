package org.flexpay.common.persistence;

public enum FlexPayFileStatus {

    TEMP("common.file_status.temp"),
    IMPORTED("common.file_status.imported"),
    MARKED_FOR_PROCCESSING("common.file_status.marked_for_processing"),
    PROCESSING("common.file_status.processing"),
    PROCESSED("common.file_status.processed"),
    PROCESSED_WITH_ERRORS("common.file_status.processed_with_errors"),
    MARKED_TO_DELETE("common.file_status.marked_to_delete");

    private String message;

    private FlexPayFileStatus(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

}
