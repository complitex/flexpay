package org.flexpay.payments.service.registry.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.filter.ImportErrorTypeFilter;
import org.flexpay.common.persistence.filter.RegistryRecordStatusFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.RegistryRecordContainer;
import org.flexpay.common.persistence.registry.RegistryStatus;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.service.RegistryStatusService;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.registry.PaymentsRegistryMBGenerator;
import org.flexpay.payments.service.registry.RegistryWriter;
import org.flexpay.payments.util.ServiceTypesMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.Signature;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Generate the payments registry in MB format.
 */
public class PaymentsRegistryMBGeneratorImpl implements PaymentsRegistryMBGenerator {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	private static final SimpleDateFormat paymentDateFormat = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat paymentPeriodDateFormat = new SimpleDateFormat("yyyyMM");

	private static final String[] TABLE_HEADERS = {
			"код квит",
			"л.с. ЕРЦ ",
			"  л.с.    ",
			" Ф. И. О.      ",
			"   ",
			" Улица          ",
			"Дом    ",
			"Кв. ",
			"Услуга       ",
			" Нач. ",
			" Кон. ",
			"Рзн",
			"Дата пл.",
			"   с  ",
			"   по ",
			"Всего  "
	};
	private static final Map<String, String> SERVICE_NAMES = new HashMap<String, String>();

	static {
		SERVICE_NAMES.put("01", "ЭЛЕКТР  ");
		SERVICE_NAMES.put("02", "КВ/ЭКСПЛ"); // точно известно
		SERVICE_NAMES.put("03", "ОТОПЛ   ");
		SERVICE_NAMES.put("04", "ГОР ВОДА");
		SERVICE_NAMES.put("05", "ХОЛ ВОДА");
		SERVICE_NAMES.put("06", "КАНАЛИЗ ");
		SERVICE_NAMES.put("07", "ГАЗ ВАР ");
		SERVICE_NAMES.put("08", "ГАЗ ОТОП");
		SERVICE_NAMES.put("09", "РАДИО   ");
		SERVICE_NAMES.put("10", "АНТ     "); // точно известно
		SERVICE_NAMES.put("11", "ЖИВ     "); // точно известно
		SERVICE_NAMES.put("12", "ГАРАЖ   "); // точно известно
		SERVICE_NAMES.put("13", "ПОГРЕБ  "); // точно известно
		SERVICE_NAMES.put("14", "САРАЙ   "); // точно известно
		SERVICE_NAMES.put("15", "КЛАДОВКА"); // точно известно
		SERVICE_NAMES.put("16", "ТЕЛЕФОН ");
		SERVICE_NAMES.put("19", "АССЕНИЗ ");
		SERVICE_NAMES.put("20", "ЛИФТ    ");
		SERVICE_NAMES.put("21", "ХОЗ РАСХ"); // точно известно
		SERVICE_NAMES.put("22", "НАЛ ЗЕМЛ");
		SERVICE_NAMES.put("23", "ПОВ ПОДК");
		SERVICE_NAMES.put("24", "ОПЛ АКТ ");
		SERVICE_NAMES.put("25", "РЕМ СЧЕТ");
	}

	private RegistryService registryService;
	private RegistryStatusService registryStatusService;
	private RegistryRecordService registryRecordService;
	private FPFileService fpFileService;
	private ServiceTypesMapper serviceTypesMapper;
	private SPService spService;

	private Signature signature = null;

	/**
	 * Export DB registry to MB registry file.
	 *
	 * @param registry	 DB registry
	 * @param file		 MB registry
	 * @param organization Service provider organization
	 * @throws FlexPayException
	 */
	public void exportToMegaBank(@NotNull Registry registry, @NotNull FPFile file, @NotNull Organization organization)
			throws FlexPayException {

		RegistryWriter rg = null;
		try {
			FPFile tmpFile = new FPFile();
			tmpFile.setModule(file.getModule());
			tmpFile.setDescription(file.getDescription());
			tmpFile.setOriginalName(file.getOriginalName());
			tmpFile.setUserName(file.getUserName());

			FPFileUtil.createEmptyFile(tmpFile);

			rg = new RegistryWriterImpl(tmpFile, '|', RegistryWriter.NO_QUOTE_CHARACTER, RegistryWriter.NO_ESCAPE_CHARACTER);
			rg.setSignature(signature);

			// заголовочные строки
			log.debug("Writing header lines");
			rg.writeLine("\tРеестр поступивших платежей. Мемориальный ордер №" + registry.getId());
			rg.writeLine("\tДля \"" + organization.getName(getLocation()) + "\". День распределения платежей " +
						 dateFormat.format(new Date()) + ".");
			rg.writeCharToLine(' ', 128);
			rg.writeCharToLine(' ', 128);
			BigDecimal amount = registry.getAmount();
			if (amount == null) {
				amount = new BigDecimal(0);
			}

			rg.writeLine("\tВсего " + (amount.multiply(new BigDecimal("100")).intValue()) +
						 " коп. Суммы указаны в копейках. Всего строк " + registry.getRecordsNumber());
			rg.writeCharToLine(' ', 128);

			// шапка таблицы
			log.debug("Writing table header lines");
			rg.write("|");
			rg.writeLine(TABLE_HEADERS);
			StringBuffer bf = new StringBuffer();
			for (String s : TABLE_HEADERS) {
				bf.append('+');
				for (int i = 0; i < s.length(); i++) {
					bf.append('-');
				}
			}
			rg.writeLine(bf.toString());

			// информационные строки
			log.debug("Write info lines");
			log.debug("Total info lines: {}", registry.getRecordsNumber());
			Page<RegistryRecord> page = new Page<RegistryRecord>();  // TODO change paging to range
			List<RegistryRecord> registryRecords;
			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.PROCESSING));
			registryService.update(registry);
			while ((registryRecords = getRegistryRecords(registry, page)).size() > 0) {
				try {
					for (RegistryRecord registryRecord : registryRecords) {
						String[] infoLine = createInfoLine(registry, registryRecord);
						rg.writeLine(infoLine);
					}
				} catch (Exception e) {
					registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.PROCESSED_WITH_ERROR));
					registryService.update(registry);
					throw new FlexPayException(e);
				}
				page.nextPage();
			}
			registry.setRegistryStatus(registryStatusService.findByCode(RegistryStatus.PROCESSED));
			registryService.update(registry);

			rg.close();
			byte[] sign = rg.getSign();
			log.debug("Registry file size={}, signature size: {}", rg.getFileSize(), sign.length);

			// служебные строки
			rg = new RegistryWriterImpl(file, '|', RegistryWriter.NO_QUOTE_CHARACTER, RegistryWriter.NO_ESCAPE_CHARACTER);

			log.info("Writing service lines");
			rg.writeCharToLine('_', 128);
			writeDigitalSignature(rg, sign);
			rg.writeCharToLine('_', 128);

			BufferedInputStream is = new BufferedInputStream(tmpFile.getInputStream());

			int bytesRead;
			byte[] buffer = new byte[1024];
			while ((bytesRead = is.read(buffer)) != -1) {
				rg.write(buffer, 0, bytesRead);
			}
			is.close();
			file.setSize(rg.getFileSize());

//			final String encoding = rg.getFileEncoding();
//			file.withOutputStream(new OutputStreamCallback() {
//				@Override
//				public void write(OutputStream os) throws IOException {
//					byte[] line = StringUtils.repeat("_", 128).getBytes(encoding);
//					os.write(line);
//					os.write("\n".getBytes(encoding));
//				}
//			});

			fpFileService.deleteFromFileSystem(tmpFile);
		} catch (FileNotFoundException e) {
			throw new FlexPayException(e);
		} catch (IOException e) {
			throw new FlexPayException(e);
		} catch (SignatureException e) {
			throw new FlexPayException(e);
		} finally {
			if (rg != null) {
				rg.close();
				fpFileService.update(file);
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
	private String[] createInfoLine(Registry registry, @NotNull RegistryRecord record) throws FlexPayException {
		List<String> infoLine = new ArrayList<String>();
		//граница таблицы
		infoLine.add(createCellData("", 0, ' '));

		// код квитанции
		infoLine.add(createCellData(String.valueOf(record.getUniqueOperationNumber()), TABLE_HEADERS[0].length(), ' '));

		// лиц. счет ЕРЦ
		String eircCount = null;
		List<RegistryRecordContainer> containers = registryRecordService.getRecordContainers(record);
		for (RegistryRecordContainer container : containers) {
			if (container.getData() != null && container.getData().startsWith("15:")) {
				String[] contenerFields = container.getData().split(":");
				if (contenerFields.length >= 3) {
					eircCount = contenerFields[2];
					break;
				}
			}
		}
		infoLine.add(createCellData(eircCount, TABLE_HEADERS[1].length(), ' '));

		// лиц. счет поставщика услуг
		infoLine.add(createCellData(record.getPersonalAccountExt(), TABLE_HEADERS[2].length(), ' '));

		// ФИО
		String fio = record.getLastName();
		if (record.getFirstName() != null && record.getFirstName().length() > 0) {
			fio += " " + record.getFirstName().charAt(0);
			if (record.getMiddleName() != null && record.getMiddleName().length() > 0) {
				fio += " " + record.getMiddleName().charAt(0);
			}
		}
		infoLine.add(createCellData(fio, TABLE_HEADERS[3].length(), ' '));

		// тип улицы
		String streetType = record.getStreetType();
		if (streetType != null && streetType.length() > 3) {
			streetType = streetType.substring(0, 2);
		}
		infoLine.add(createCellData(streetType, TABLE_HEADERS[4].length(), ' '));

		// название улицы
		infoLine.add(createCellData(record.getStreetName(), TABLE_HEADERS[5].length(), ' '));

		// дом
		String building = record.getBuildingNum();
		if (building != null && record.getBuildingBulkNum() != null) {
			building += " " + record.getBuildingBulkNum();
		}
		infoLine.add(createCellData(building, TABLE_HEADERS[6].length(), ' '));

		// квартира
		infoLine.add(createCellData(record.getApartmentNum(), TABLE_HEADERS[7].length(), ' '));

		// услуга
		String serviceCode = record.getServiceCode();
		if (serviceCode == null) {
			throw new FlexPayException("Registry record`s service code is null. Registry record Id: " + record.getId());
		}
		EircRegistryProperties props = (EircRegistryProperties) registry.getProperties();
		Service srv = spService.findService(props.getServiceProviderStub(), serviceCode);
		serviceCode = serviceTypesMapper.getMegabankCode(srv.serviceTypeStub());
		if (serviceCode == null) {
			throw new FlexPayException("Can not find MB service code. Service type: " + srv.serviceTypeStub() +
									   ", registry record Id: " + record.getId());
		}
		serviceCode = StringUtils.leftPad(serviceCode, 2, '0');

		String service = serviceCode + "." + SERVICE_NAMES.get(serviceCode) + " " + "*";
		infoLine.add(createCellData(service, TABLE_HEADERS[8].length(), ' '));

		// начальное показание счетчика
		infoLine.add(createCellData("0", TABLE_HEADERS[9].length(), ' '));

		// конечное показание счетчика
		infoLine.add(createCellData("0", TABLE_HEADERS[10].length(), ' '));

		// разница показаний счетчика
		infoLine.add(createCellData("0", TABLE_HEADERS[11].length(), ' '));

		// дата платежа
		Date operationDate = record.getOperationDate();
		String paymentDate = operationDate != null ? paymentDateFormat.format(operationDate) : null;
		infoLine.add(createCellData(paymentDate, TABLE_HEADERS[12].length(), ' '));

		// с какого месяца оплачена услуга
		String paymentMounth = null;
		if (operationDate != null) {
			Calendar cal = (Calendar) Calendar.getInstance().clone();
			cal.setTime(operationDate);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.roll(Calendar.MONTH, -1);
			paymentMounth = paymentPeriodDateFormat.format(cal.getTime());
		}
		infoLine.add(createCellData(paymentMounth, TABLE_HEADERS[13].length(), ' '));

		// по какой месяц оплачена услуга
		infoLine.add(createCellData(paymentMounth, TABLE_HEADERS[14].length(), ' '));

		// сумма (значение суммы изначально передаётся в рублях, но должно быть записано в копейках)\
		int summ = record.getAmount().multiply(new BigDecimal("100")).intValue();
		infoLine.add(createCellData(String.valueOf(summ), null, ' '));

		return infoLine.toArray(new String[infoLine.size()]);
	}

	@NotNull
	private String createCellData(@Nullable String data, @Nullable Integer length, char ch) {
		String cellData = data;
		if (cellData == null) {
			cellData = "";
		}
		if (length == null) {
			return cellData;
		}
		if (cellData.length() > length) {
			return cellData.substring(0, length);
		}
		StringBuffer sb = new StringBuffer(cellData);
		while (sb.length() < length) {
			sb.append(ch);
		}
		return sb.toString();
	}

	private void writeDigitalSignature(@NotNull RegistryWriter rg, @Nullable byte[] sign) throws IOException, SignatureException {
		if (sign != null) {
			rg.writeLine(sign);
			String str = new String(sign, rg.getFileEncoding());
			int nLineFeeds = StringUtils.countMatches(str, "\n") + 1; // one added in rg.writeLine(sign);
			while (nLineFeeds < 2) {
				rg.writeLine("");
				++nLineFeeds;
			}
		} else {
			rg.writeLine("");
			rg.writeLine("");
		}
	}

	@Override
	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	@NotNull
	private Locale getLocation() {
		return new Locale("ru");
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

	@Required
	public void setRegistryStatusService(RegistryStatusService registryStatusService) {
		this.registryStatusService = registryStatusService;
	}

	@Required
	public void setRegistryRecordService(RegistryRecordService registryRecordService) {
		this.registryRecordService = registryRecordService;
	}

	@Required
	public void setServiceTypesMapper(ServiceTypesMapper serviceTypesMapper) {
		this.serviceTypesMapper = serviceTypesMapper;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}
}