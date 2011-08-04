package org.flexpay.common.esb;

import org.flexpay.common.persistence.EsbXmlSyncObject;

import java.io.IOException;

public interface EsbSyncRequestExecutor<T extends EsbXmlSyncObject> {

    void executeRequest(T object) throws IOException;

}
