package org.flexpay.payments.service.export;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.*;
import org.flexpay.common.service.*;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.OperationService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


public class TestGenerateRegistry {

    public class RegistryWriter {
        private final Logger log = LoggerFactory.getLogger(getClass());

        private BufferedOutputStream bos;

        private char separator;

        private char quotechar;

        private char escapechar;

        private String lineEnd;

        private String fileEncoding;

        public static final char DEFAULT_ESCAPE_CHARACTER = '"';

        public static final char DEFAULT_SEPARATOR = ',';

        public static final char DEFAULT_QUOTE_CHARACTER = '"';

        public static final char NO_QUOTE_CHARACTER = '\u0000';

        public static final char NO_ESCAPE_CHARACTER = '\u0000';

        public static final String DEFAULT_LINE_END = "\n";


        public RegistryWriter(@NotNull File file) throws FileNotFoundException {
            this(file, DEFAULT_SEPARATOR);
        }

        public RegistryWriter(@NotNull File file, char separator) throws FileNotFoundException {
            this(file, separator, DEFAULT_QUOTE_CHARACTER);
        }

        public RegistryWriter(@NotNull File file, char separator, char quotechar) throws FileNotFoundException {
            this(file, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
        }

        public RegistryWriter(@NotNull File file, char separator, char quotechar, char escapechar) throws FileNotFoundException {
            this(file, separator, quotechar, escapechar, DEFAULT_LINE_END);
        }

        public RegistryWriter(@NotNull File file, char separator, char quotechar, @NotNull String lineEnd) throws FileNotFoundException {
            this(file, separator, quotechar, DEFAULT_ESCAPE_CHARACTER, lineEnd);
        }

        public RegistryWriter(@NotNull File file, char separator, char quotechar, char escapechar, @NotNull String lineEnd) throws FileNotFoundException {
            FileOutputStream fos = new FileOutputStream(file);
            this.bos = new BufferedOutputStream(fos);
            this.separator = separator;
            this.quotechar = quotechar;
            this.escapechar = escapechar;
            this.lineEnd = lineEnd;
        }

        public void writeLine(@Nullable String[] nextLine) throws IOException {

            if (nextLine == null)
                return;

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < nextLine.length; i++) {

                if (i != 0) {
                    sb.append(separator);
                }

                String nextElement = nextLine[i];
                if (nextElement == null)
                    continue;
                appendCell(sb, nextElement);
            }

            sb.append(lineEnd);

            log.debug("Write line:" + sb.toString());
            bos.write(sb.toString().getBytes(getFileEncoding()));

        }

        public void writeLine(@Nullable String nextLine) throws IOException {

            if (nextLine == null)
                return;

            StringBuffer sb = new StringBuffer();

            appendCell(sb, nextLine);

            sb.append(lineEnd);
            log.debug("Write line:" + sb.toString());
            bos.write(sb.toString().getBytes(getFileEncoding()));

        }

        public void writeLine(@Nullable byte[] nextLine) throws IOException {

            if (nextLine == null)
                return;

            log.debug("Write line:" + nextLine);
            bos.write(nextLine);
            bos.write(lineEnd.getBytes(getFileEncoding()));
        }

        public void writeCharToLine(char ch, int count) throws IOException {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < count; i++) {
                sb.append(ch);
            }
            if (quotechar != NO_QUOTE_CHARACTER)
                sb.append(quotechar);

            sb.append(lineEnd);
            log.debug("Write line:" + sb.toString());
            bos.write(sb.toString().getBytes(getFileEncoding()));
        }

        private void appendCell(@NotNull StringBuffer sb, @NotNull String nextLine) {
            if (quotechar !=  NO_QUOTE_CHARACTER)
                sb.append(quotechar);
            for (int j = 0; j < nextLine.length(); j++) {
                char nextChar = nextLine.charAt(j);
                if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
                    sb.append(escapechar).append(nextChar);
                } else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
                    sb.append(escapechar).append(nextChar);
                } else {
                    sb.append(nextChar);
                }
            }
            if (quotechar != NO_QUOTE_CHARACTER)
                sb.append(quotechar);
        }

        public void flush() throws IOException, FlexPayException {
            try {
                log.info("flush stream");
                bos.flush();
            } catch (IOException e) {
                throw new FlexPayException(e);
            }
        }

        public void close() throws FlexPayException {
            try {
                log.info("flush and close stream");
                bos.flush();
                bos.close();
            } catch (IOException e) {
                throw new FlexPayException(e);
            }
        }

        public void setFileEncoding(@NotNull String fileEncoding) {
            this.fileEncoding = fileEncoding;
        }

        @NotNull
        public String getFileEncoding() {
            return fileEncoding != null? fileEncoding: "Cp866";
        }
    }


    public class GeneratePaymentsMBRegistry {
        private final Logger log = LoggerFactory.getLogger(getClass());

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy"); //TODO make static
        private final SimpleDateFormat paymentDateFormat = new SimpleDateFormat("yyyyMMdd"); //TODO make static
        private final SimpleDateFormat paymentPeriodDateFormat = new SimpleDateFormat("yyyyMM"); //TODO make static
        private final String[] tableHeader = {                                               //TODO make static
                "код квит",
                "л.с. ЕРЦ ",
                "  л.с.    ",
                " Ф. И. О.      ",
                "   ",
                " Улица          ",
                "Дом    ",
                "Кв. ",
                "Услуга       ",
                "Нач. ",
                " Кон. ",
                "Рзн",
                "Дата пл.",
                "с  ",
                "по ",
                "Всего"
        };
        private final Map<String, String> serviceNames = new HashMap<String, String>(); //TODO make static

        {
            serviceNames.put("1", "ЭЛЕКТР  ");
            serviceNames.put("2", "КВ/ЭКСПЛ"); // точно известно
            serviceNames.put("3", "ОТОПЛ   ");
            serviceNames.put("4", "ГОР ВОДА");
            serviceNames.put("5", "ХОЛ ВОДА");
            serviceNames.put("6", "КАНАЛИЗ ");
            serviceNames.put("7", "ГАЗ ВАР ");
            serviceNames.put("8", "ГАЗ ОТОП");
            serviceNames.put("9", "РАДИО   ");
            serviceNames.put("10", "АНТ     "); // точно известно
            serviceNames.put("11", "ЖИВ     "); // точно известно
            serviceNames.put("12", "ГАРАЖ   "); // точно известно
            serviceNames.put("13", "ПОГРЕБ  "); // точно известно
            serviceNames.put("14", "САРАЙ   "); // точно известно
            serviceNames.put("15", "КЛАДОВКА"); // точно известно
            serviceNames.put("16", "ТЕЛЕФОН ");
            serviceNames.put("19", "АССЕНИЗ ");
            serviceNames.put("20", "ЛИФТ    ");
            serviceNames.put("21", "ХОЗ РАСХ"); // точно известно
            serviceNames.put("22", "НАЛ ЗЕМЛ");
            serviceNames.put("23", "ПОВ ПОДК");
            serviceNames.put("24", "ОПЛ АКТ ");
            serviceNames.put("25", "РЕМ СЧЕТ");
        }

        private RegistryService registryService;
        private RegistryStatusService registryStatusService;
        private RegistryRecordService registryRecordService;

        public void exportToMegaBank(@NotNull Registry registry, @NotNull FPFile spFile, @NotNull Organization organization, @NotNull Date startDate, @NotNull Date endDate) throws FlexPayException {
            RegistryWriter rg = null;
            try {
                rg = new RegistryWriter(spFile.getFile());

                // служебные строки
                log.info("Writing service lines");
                rg.writeCharToLine('_', 128);
                writeDigitalSignature(rg);
                rg.writeCharToLine('_', 128);

                // заголовочные строки
                log.info("Write header lines");
                rg.writeLine("Реестр поступивших платежей. Мемориальный ордер №" + registry.getId());
                rg.writeLine("Для «" + organization.getName(getLocation()) + "». День распределения платежей " + dateFormat.format(new Date()) +".");
                rg.writeCharToLine(' ', 128);
                rg.writeCharToLine(' ', 128);
                rg.writeLine("Всего «" + registry.getAmount() + "» коп. Суммы указаны в копейках. Всего строк " + registry.getRecordsNumber());
                rg.writeCharToLine(' ', 128);

                // шапка таблицы
                log.info("Write table header lines");
                rg.writeLine(tableHeader);
                StringBuffer bf = new StringBuffer();
                for (String s : tableHeader) {
                    if (bf.length() > 0) {
                        bf.append('+');
                    }
                    for (int i = 0; i < s.length(); i++) {
                        bf.append('_');
                    }
                }
                rg.writeLine(bf.toString());

                // информационные строки
                log.info("Write info lines");
                log.info("Total info lines: " + registry.getRecordsNumber());
                List<RegistryRecord> registryRecords =  registryRecordService.listRecords(registry, new ImportErrorTypeFilter(),
                                                                                                    new RegistryRecordStatusFilter(),
                                                                                                    new Page<RegistryRecord>());
                registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.PROCESSING));
                registryService.update(registry);
                int i = 0;
                try {
                    for (RegistryRecord registryRecord : registryRecords) {
                        String[] infoLine = createInfoLine(registryRecord);
                        rg.writeLine(infoLine);
                        log.info("Writed line " + String.valueOf(++i));
                    }
                } catch (Exception e) {
                    registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.PROCESSING_WITH_ERROR));
                    registryService.update(registry);
                    throw new FlexPayException(e);
                }
                registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.PROCESSED));
                registryService.update(registry);
            } catch (FileNotFoundException e) {
                throw new FlexPayException(e);
            } catch (IOException e) {
                throw new FlexPayException(e);
            } finally {
                if (rg != null) {
                    rg.close();
                }
            }
        }

        @NotNull
        private String[] createInfoLine(@NotNull RegistryRecord record) {
            List<String> infoLine = new ArrayList<String>();

            // код квитанции
            infoLine.add(createCellData(null, tableHeader[0].length(), ' '));

            // лиц. счет ЕРЦ
            infoLine.add(createCellData(null, tableHeader[1].length(), ' '));

            // лиц. счет поставщика услуг
            infoLine.add(createCellData(record.getPersonalAccountExt(), tableHeader[2].length(), ' '));

            // ФИО
            String fio = record.getLastName();
            if (record.getFirstName() != null && record.getFirstName().length() > 0) {
                fio += record.getFirstName().charAt(0);
                if (record.getMiddleName() != null && record.getMiddleName().length() > 0) {
                    fio += " " + record.getMiddleName().charAt(0);
                }
            }
            infoLine.add(createCellData(fio, tableHeader[3].length(), ' '));

            // тип улицы
            String streetType = record.getStreetType();
            if (streetType != null && streetType.length() > 3) {
                streetType = streetType.substring(0, 2);
            }
            infoLine.add(createCellData(streetType, tableHeader[4].length(), ' '));

            // название улицы
            infoLine.add(createCellData(record.getStreetName(), tableHeader[5].length(), ' '));

            // дом
            String building = record.getBuildingNum();
            if (building != null && record.getBuildingBulkNum() != null) {
                building += " " + record.getBuildingBulkNum();
            }
            infoLine.add(createCellData(building, tableHeader[6].length(), ' '));

            // квартира
            infoLine.add(createCellData(record.getApartmentNum(), tableHeader[7].length(), ' '));

            // услуга
            String serviceCode = record.getServiceCode().substring(1);
            while (serviceCode.length() < 2) {
                serviceCode = "0" + serviceCode;
            }
            String service = serviceCode + "." + serviceNames.get(record.getServiceCode()) + " " + "*";
            infoLine.add(createCellData(service, tableHeader[7].length(), ' '));

            // начальное показание счетчика
            infoLine.add(createCellData("0", tableHeader[8].length(), ' '));

            // конечное показание счетчика
            infoLine.add(createCellData("0", tableHeader[9].length(), ' '));

            // разница показаний счетчика
            infoLine.add(createCellData("0", tableHeader[10].length(), ' '));

            // дата платежа
            Date operationDate = record.getOperationDate();
            String paymentDate = operationDate !=null? paymentDateFormat.format(operationDate): null;
            infoLine.add(createCellData(paymentDate, tableHeader[11].length(), ' '));

            // с какого месяца оплачена услуга
            String paymentMounth = null;
            if (operationDate != null) {
                Calendar cal = (Calendar) Calendar.getInstance().clone();
                cal.setTime(operationDate);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.roll(Calendar.MONTH, 1);
                paymentMounth = paymentPeriodDateFormat.format(cal.getTime());
            }
            infoLine.add(createCellData(paymentMounth, tableHeader[12].length(), ' '));

            // по какой месяц оплачена услуга
            infoLine.add(createCellData(paymentMounth, tableHeader[13].length(), ' '));

            // сумма
            infoLine.add(createCellData(String.valueOf(record.getAmount()), tableHeader[14].length(), ' '));

            return (String[])infoLine.toArray();
        }

        @NotNull
        private String createCellData(@Nullable String data, @NotNull Integer length, char ch) {
            String cellData = data;
            if (cellData == null) {
                cellData = "";
            }
            StringBuffer sb = new StringBuffer(cellData);
            while (sb.length() < length) {
                sb.append(ch);
            }
            return sb.toString();
        }

        private void writeDigitalSignature(@NotNull RegistryWriter rg) throws IOException {
            rg.writeLine("");
            rg.writeLine("");
            rg.writeLine("");
        }

        public void setRegistryService(RegistryService registryService) {
            this.registryService = registryService;
        }

        public void setRegistryStatusService(RegistryStatusService registryStatusService) {
            this.registryStatusService = registryStatusService;
        }

        public void setRegistryRecordService(RegistryRecordService registryRecordService) {
            this.registryRecordService = registryRecordService;
        }

        @NotNull
        private Locale getLocation() {
            return new Locale("rus");
        }
    }

    public class GeneratePaymentsDBRegistry {
        private final Logger log = LoggerFactory.getLogger(getClass());

        private RegistryService registryService;
        private RegistryRecordService registryRecordService;
        private RegistryTypeService registryTypeService;
        private RegistryStatusService registryStatusService;
        private RegistryArchiveStatusService registryArchiveStatusService;

        @NotNull
        protected Registry createDBRegestry(@NotNull FPFile spFile, @NotNull Organization organization, @NotNull Date fromDate, @NotNull Date tillDate) throws FlexPayException {
            log.info("Get operation by organization " + organization.getId());
            List<Operation> operations = getOperations(organization, fromDate, tillDate);
            log.info("Count operations " + operations.size());

            Registry registry = new Registry();

            registry.setCreationDate(new Date());
			registry.setSpFile(spFile);
			registry.setRegistryType(registryTypeService.findByCode(getPaymentsType()));
			registry.setArchiveStatus(registryArchiveStatusService.findByCode(RegistryArchiveStatus.NONE));
			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING));
            registry.setFromDate(fromDate);
            registry.setTillDate(tillDate);
            registryService.create(registry);

            BigDecimal summ = new BigDecimal(0);
            Long recordsNumber = 0L;
            for (Operation operation : operations) {
                for (Document document : operation.getDocuments()) {
                    if (document.getRegistryRecord() == null) {
                        RegistryRecord record = new RegistryRecord();
                        RegistryRecordStatus status = new RegistryRecordStatus();
                        status.setCode(RegistryRecordStatus.PROCESSED);
                        record.setRecordStatus(status);
                        record.setAmount(document.getSumm());
                        record.setServiceCode("#" + document.getService().getExternalCode());
                        record.setPersonalAccountExt(document.getDebtorId());
                        record.setOperationDate(operation.getCreationDate());
                        record.setRegistry(registry);

                        record.setLastName(document.getLastName());
                        record.setMiddleName(document.getMiddleName());
                        record.setFirstName(document.getFirstName());
                        record.setCity(document.getTown());
                        record.setStreetType(document.getStreetType());
                        record.setStreetName(document.getStreetName());
                        record.setBuildingNum(document.getBuildingNumber());
                        record.setBuildingBulkNum(document.getBuildingBulk());
                        record.setApartmentNum(document.getApartmentNumber());

                        List<RegistryRecordContainer> containers = new ArrayList<RegistryRecordContainer>();

                        RegistryRecordContainer container = new RegistryRecordContainer();
                        container.setOrder(0);
                        container.setData("50:" + document.getDebtorId());
                        container.setRecord(record);
                        containers.add(container);

                        record.setContainers(containers);

                        try {
                            registryRecordService.create(record);
                        } catch (FlexPayException e) {
                            registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATING_CANCELED));
                        }

                        summ = summ.add(document.getSumm());
                        recordsNumber++;
                    }
                }
            }
            registry.setAmount(summ);
            registry.setRecordsNumber(recordsNumber);
            registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.CREATED));
            registryService.update(registry);

            return registry;

        }

        @NotNull
        private List<Operation> getOperations(@NotNull Organization organization,
                                              @NotNull Date startDate,
                                              @NotNull Date endDate) {
            return operationService.listReceivedPayments(organization.getId(), startDate, endDate);
        }

        @NotNull
        private Integer getPaymentsType() {
            return RegistryType.TYPE_CASH_PAYMENTS;
        }

        private OperationService operationService;public void setRegistryService(RegistryService registryService) {
            this.registryService = registryService;
        }

        public void setRegistryRecordService(RegistryRecordService registryRecordService) {
            this.registryRecordService = registryRecordService;
        }

        public void setRegistryTypeService(RegistryTypeService registryTypeService) {
            this.registryTypeService = registryTypeService;
        }

        public void setRegistryStatusService(RegistryStatusService registryStatusService) {
            this.registryStatusService = registryStatusService;
        }

        public void setRegistryArchiveStatusService(RegistryArchiveStatusService registryArchiveStatusService) {
            this.registryArchiveStatusService = registryArchiveStatusService;
        }

        public void setOperationService(OperationService operationService) {
            this.operationService = operationService;
        }
    }

    @Test
    public void testGenerate() {

    }
}
