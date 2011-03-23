package org.flexpay.tc.persistence;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class TariffConfig {

	// Уборка территории
	public static final String TARIFF_CLEANING_TERRITORY = "010";
	// Очистка мусоросборников
	public static final String TARIFF_CLEANING_REFUSE_CHUTES = "020";
	// Уборка подвалов, тех.этажей, крыш
	public static final String TARIFF_CLEANING_BASEMENTS = "030";
	// Вывоз и утилизация ТБО
	public static final String TARIFF_UTILIZATION_HARD_WASTE = "040";
	// ТО лифтов
	public static final String TARIFF_SERVICE_LIFTS = "050";
	// ТО систем диспетчеризации
	public static final String TARIFF_SERVICE_PRODUCTION_CONTROL_SYSTEMS = "060";
	// ТО систем водоснабжения
	public static final String TARIFF_SERVICE_WATER_SUPPLY_SYSTEMS = "070";
	// ТО систем водоотведения
	public static final String TARIFF_SERVICE_WATER = "080";
	// ТО систем теплоснабжения
	public static final String TARIFF_SERVICE_HEAT_SUPPLY_SYSTEMS = "090";
	// ТО систем горячего водоснабжения
	public static final String TARIFF_SERVICE_HOT_WATER_SUPPLY_SYSTEMS = "100";
	// ТО бойлеров
	public static final String TARIFF_SERVICE_BOILERS = "110";
	// Обслуживание дымоотв. каналов
	public static final String TARIFF_SERVICE_OFFTAKES = "120";
	// Очистка дворовых туалетов
	public static final String TARIFF_CLEANING_YARD_TOILETS = "130";
	// Освещение мест общего пользования
	public static final String TARIFF_LIGHTING_COMMON_PLACES = "140";
	// Энергоснабжение для подкачки воды
	public static final String TARIFF_WATER_ENERGY_SUPPLY = "150";
	// Энергоснабжение для лифтов
	public static final String TARIFF_LIFTS_ENERGY_SUPPLY = "160";
	// Уборка лестничных клеток
	public static final String TARIFF_CLEANING_STAIRWELLS = "170";
	// Дератизация и дезинфекция
	public static final String TARIFF_DISINFECTION = "180";
	// ТО бытовых электроплит
	public static final String TARIFF_SERVICE_ELECTRIC_RANGES = "190";

	public static List<String> getAllTariffCodes() {
		List<String> codes = list();

		codes.add(TARIFF_CLEANING_TERRITORY);
		codes.add(TARIFF_CLEANING_REFUSE_CHUTES);
		codes.add(TARIFF_CLEANING_BASEMENTS);
		codes.add(TARIFF_UTILIZATION_HARD_WASTE);
		codes.add(TARIFF_SERVICE_LIFTS);
		codes.add(TARIFF_SERVICE_PRODUCTION_CONTROL_SYSTEMS);
		codes.add(TARIFF_SERVICE_WATER_SUPPLY_SYSTEMS);
		codes.add(TARIFF_SERVICE_WATER);
		codes.add(TARIFF_SERVICE_HEAT_SUPPLY_SYSTEMS);
		codes.add(TARIFF_SERVICE_HOT_WATER_SUPPLY_SYSTEMS);
		codes.add(TARIFF_SERVICE_BOILERS);
		codes.add(TARIFF_SERVICE_OFFTAKES);
		codes.add(TARIFF_CLEANING_YARD_TOILETS);
		codes.add(TARIFF_LIGHTING_COMMON_PLACES);
		codes.add(TARIFF_WATER_ENERGY_SUPPLY);
		codes.add(TARIFF_LIFTS_ENERGY_SUPPLY);
		codes.add(TARIFF_CLEANING_STAIRWELLS);
		codes.add(TARIFF_DISINFECTION);
		codes.add(TARIFF_SERVICE_ELECTRIC_RANGES);

		return codes;
	}

}
