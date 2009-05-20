package org.flexpay.payments.process.export.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.RegistryStatusService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GeneratePaymentsMBRegistry {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final long FLASH_FILE = 100;
    private static final Integer PAGE_SIZE = 20;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat paymentDateFormat = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat paymentPeriodDateFormat = new SimpleDateFormat("yyyyMM");
    private static final String[] tableHeader = {
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
    private static final Map<String, String> serviceNames = new HashMap<String, String>();

    static {
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

    public void exportToMegaBank(@NotNull Registry registry, @NotNull File file, @NotNull Organization organization) throws FlexPayException {
        RegistryWriter rg = null;
        try {
            rg = new RegistryWriter(file, '|', RegistryWriter.NO_QUOTE_CHARACTER, RegistryWriter.NO_ESCAPE_CHARACTER);

            // служебные строки
            log.info("Writing service lines");
            rg.writeCharToLine('_', 128);
            writeDigitalSignature(rg);
            rg.writeCharToLine('_', 128);

            // заголовочные строки
            log.info("Write header lines");
            rg.writeLine("Реестр поступивших платежей. Мемориальный ордер №" + registry.getId());
            rg.writeLine("Для «" + organization.getName(getLocation()) + "». День распределения платежей " + dateFormat.format(new Date()) + ".");
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

            rg.flush();

            // информационные строки
            log.info("Write info lines");
            log.info("Total info lines: " + registry.getRecordsNumber());
            Page<RegistryRecord> page = new Page<RegistryRecord>(PAGE_SIZE);  // TODO change paging to range
            List<RegistryRecord> registryRecords;
            registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.PROCESSING));
            registryService.update(registry);
            while ((registryRecords = getRegistryRecords(registry, page)).size() > 0) {
                int i = 0;
                int k = 0;
                try {
                    for (RegistryRecord registryRecord : registryRecords) {
                        String[] infoLine = createInfoLine(registryRecord);
                        rg.writeLine(infoLine);
                        log.info("Writed line " + String.valueOf(++i));
                        if (++k >= FLASH_FILE) {
                            rg.flush();
                            k = 0;
                        }
                    }
                } catch (Exception e) {
                    registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.PROCESSING_WITH_ERROR));
                    registryService.update(registry);
                    throw new FlexPayException(e);
                }
                page.nextPage();
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

    private List<RegistryRecord> getRegistryRecords(Registry registry, Page<RegistryRecord> page) {
        return registryRecordService.listRecords(registry,
                new ImportErrorTypeFilter(),
                new RegistryRecordStatusFilter(),
                page);
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
        String paymentDate = operationDate != null ? paymentDateFormat.format(operationDate) : null;
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

        return (String[]) infoLine.toArray();
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