package org.flexpay.common.service;

import org.flexpay.common.persistence.FlexPayFileType;
import org.flexpay.common.persistence.FlexPayFile;
import org.flexpay.common.exception.FlexPayException;

import java.io.InputStream;
import java.io.IOException;

public interface FlexPayFileService {

    FlexPayFile createFile(InputStream is, FlexPayFile file) throws FlexPayException;

    void updateFile(InputStream is, FlexPayFile file) throws FlexPayException;

    void deleteFile(Long fileId) throws FlexPayException;

    FlexPayFile getFile(Long fileId) throws FlexPayException;

    InputStream getContent(FlexPayFile file) throws IOException;

}
