package org.flexpay.common.persistence.history.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlDateTime;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.history.ExternalHistoryPack;
import org.flexpay.common.persistence.history.HistoryUnpackManager;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.Security;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Date;

@Endpoint
public class SoapInHistoryEndpoint {

    private static final String NS = "http://flexpay.org/schemas/history";

    protected Logger log = LoggerFactory.getLogger(getClass());

    private XPath groupIdExpression;
    private XPath instanceIdExpression;
    private XPath createdExpression;
    private XPath fileExpression;

    private FPFileService fileService;
    private HistoryUnpackManager unpackManager;

    public SoapInHistoryEndpoint() throws JDOMException {
        Namespace namespace = Namespace.getNamespace("h", NS);
        groupIdExpression = XPath.newInstance("//h:groupId");
        groupIdExpression.addNamespace(namespace);
        instanceIdExpression = XPath.newInstance("//h:instanceId");
        instanceIdExpression.addNamespace(namespace);
        createdExpression = XPath.newInstance("//h:created");
        createdExpression.addNamespace(namespace);
        fileExpression = XPath.newInstance("//h:file");
        fileExpression.addNamespace(namespace);
    }

    /**
     * Template method. Subclasses must implement this. Offers the request payload as a JDOM <code>Element</code>, and
     * allows subclasses to return a response <code>Element</code>.
     *
     * @param requestElement the contents of the SOAP message as JDOM element
     * @return the response element. Can be <code>null</code> to specify no response.
     * @throws Exception exception
     */
    @PayloadRoot(localPart = "invokeInternal", namespace = "http://flexpay.org/schemas/history")
    @ResponsePayload
    protected Element invokeInternal(@RequestPayload Element requestElement) throws Exception {

        Security.authenticateSyncer();

        String groupId = groupIdExpression.valueOf(requestElement);
        String instanceId = instanceIdExpression.valueOf(requestElement);

        XmlDateTime xmlDate = XmlDateTime.Factory.newInstance();
        xmlDate.setStringValue(createdExpression.valueOf(requestElement));

        Date created = xmlDate.getDateValue();

        byte[] fileContent = Base64.decodeBase64(fileExpression.valueOf(requestElement).getBytes("UTF-8"));

        FPFile file = new FPFile();
        file.setModule(fileService.getModuleByName("common"));
        file.setOriginalName("history-" + instanceId + "-" + groupId + ".soap.xml.gz");
        file.setCreationDate(created);
        FPFileUtil.createEmptyFile(file);

        OutputStream os = file.getOutputStream();
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
            IOUtils.copy(is, os);
        } finally {
            IOUtils.closeQuietly(os);
        }

        try {
            log.debug("Data recieved, saving file");
            fileService.create(file);

            log.debug("Creating pack instance");
            ExternalHistoryPack pack = new ExternalHistoryPack();
            pack.setFile(file);
            pack.setConsumptionGroupId(Long.parseLong(groupId));
            pack.setSourceInstanceId(instanceId);
            if ("CASA_TEST".equals(instanceId)) {
                log.debug("Composite application test request, ignoring");
            } else if (StringUtils.equals(ApplicationConfig.getInstanceId(), instanceId)) {
                log.debug("Received self made packet, do nothing");
            } else {
                log.debug("Creating a new pack: ", pack);
                unpackManager.create(pack);
            }
        } catch (Exception ex) {
            log.error("Failed saving file and creating process", ex);
            throw ex;
        }

        log.debug("All done!");
        return new Element("SaveHistoryResponse", Namespace.getNamespace("http://flexpay.org/schemas/history"))
                .addContent(ApplicationConfig.getInstanceId() + " OK!");
    }

    @Required
    public void setFileService(FPFileService fileService) {
        this.fileService = fileService;
    }

    @Required
    public void setUnpackManager(HistoryUnpackManager unpackManager) {
        this.unpackManager = unpackManager;
    }
}
