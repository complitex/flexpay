package org.flexpay.common.service.transport;

import org.flexpay.common.persistence.file.FPFile;

public interface InTransport {

	void recieve(FPFile file);
}
