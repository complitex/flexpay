package org.flexpay.bti.persistence;

import org.flexpay.common.util.CollectionUtils;

import java.util.Map;

public class BuildingAttributeConfig {

	private static final Map<String, Boolean> temporalFlags = CollectionUtils.map();

	// 7 G	№ участка
	public static final String ATTR_SECTION_NUMBER = "building_attribute_section_number";
	// 8 H год постройки
	public static final String ATTR_BUILD_YEAR = "";
	// 9 I серия, тип дома
	public static final String ATTR_HOUSE_TYPE = "";
	// 10 J кол-во этажей
	public static final String ATTR_FLOORS_NUMBER = "";
	// 11 K кол-во подъездов
	public static final String ATTR_DOORWAYS_NUMBER = "";
	// 12 L кол-во квартир
	public static final String ATTR_APARTMENTS_NUMBER = "";
	// 13 M жилая площадь
	public static final String ATTR_LIVE_SQUARE = "";
	// 14 N кол-во проживающих
	public static final String ATTR_HABITANTS_NUMBER = "";
	// 15 O кол-во бойлерных установок
	public static final String ATTR_BOILER_PLANT_NUMBER = "";
	// 16 P протяженность сетей цетрального отопления
	public static final String ATTR_CENTRAL_WARN_NET_LENGTH = "";
	// 17 Q общая площадь жилого фонда, оборудованного центральным отоплением
	public static final String ATTR_TOTAL_SQUARE_WITH_CENTRAL_WARM = "";
	// 18 R общая площадь жилого фонда, оборудованного центральным горячим водоснабжением
	public static final String ATTR_TOTAL_SQUARE_WITH_CENTRAL_HOT_WATER = "";
	// 19 S общая площадь жилого фонда с бойлерными установками
	public static final String ATTR_TOTAL_SQUARE_WITH_BOILER_PLANTS = "";
	// 20 T общая площадь жилого фонда, оборудованного водопроводом
	public static final String ATTR_TOTAL_SQUARE_WITH_WATER = "";
	// 21 U общая площадь жилого фонда, оборудованного канализацией
	public static final String ATTR_TOTAL_SQUARE_WITH_SEWER = "";
	// 22 V общая площадь жилого фонда, оборудованного мусоропроводами
	public static final String ATTR_TOTAL_SQUARE_WITH_REFUSE_CHUTES = "";
	// 23 W общая площадь жилого фонда
	public static final String ATTR_TOTAL_SQUARE_WITH = "";
	// 24 X общая площадь жилых помещений в домах, где вода подается при помощи насосов подкчки
	public static final String ATTR_TOTAL_SQUARE_WITH_WATER_PUMPS = "";
	// 25 Y кол-во квартир гостинничного типа
	public static final String ATTR_HOTEL_TYPE_APARTMENTS_NUMBER = "";
	// 26 Z площадь крови (мягкая)
	public static final String ATTR_SOFT_ROOF_SQUARE = "";
	// 27 AA площадь крови (шифер)
	public static final String ATTR_HARD_SLATE_ROOF_SQUARE = "";
	// 28 AB площадь крови (металлическая)
	public static final String ATTR_HARD_METAL_ROOF_SQUARE = "";
	// 29 AC площадь подвалов
	public static final String ATTR_BASEMENT_SQUARE = "";
	// 30 AD площадь тех.этажей
	public static final String ATTR_TECHNICAL_FLOORS_SQUARE = "";
	// 31 AE Площадь чердаков
	public static final String ATTR_ARRET_SQUARE = "";
	// 32 AF Кол-во вентиляционных каналов
	public static final String ATTR_VENT_CHANNEL_NUMBER = "";
	// 33 AG Кол-во дымоходов
	public static final String ATTR_FLUES_NUMBER = "";
	// 34 AH общая площадь первых этажей
	public static final String ATTR_FIRST_FLOORS_TOTAL_SQUARE = "";
	// 35 AI общая площадь квартир, оборудованных лифтами
	public static final String ATTR_LIFTED_APARTMENTS_TOTAL_SQUARE = "";
	// 36 AJ кол-во лифтов
	public static final String ATTR_LISTS_NUMBER = "";
	// 37 AK тип лифта
	public static final String ATTR_LIFT_TYPE = "";
	// 38 AL тип лифта
	public static final String ATTR_LIFT_TYPE_2 = "";
	// 39 AM срок эксплуатации лифта
	public static final String ATTR_LIFT_USAGE_AGE = "";
	// 40 AN срок эксплуатации лифта
	public static final String ATTR_LIFT_USAGE_AGE_2 = "";
	// 41 AO диспетчеризованные лифты
	public static final String ATTR_LIFTS_WITH_DISPETCHER = "";
	// 42 AP диспетчеризованные лифты
	public static final String ATTR_LIFTS_WITH_DISPETCHER_2 = "";
	// 43 AQ недиспетчеризованные лифты
	public static final String ATTR_LIFTS_WITHOUT_DISPETCHER = "";
	// 44 AR недиспетчеризованные лифты
	public static final String ATTR_LIFTS_WITHOUT_DISPETCHER_2 = "";
	// 45 AS общая площадь квартир в домах, оборудованных АДС
	public static final String ATTR_ADS_SUITED_APARTMENTS_TOTAL_SQUARE = "";
	// 46 AT тип оборудования АДС
	public static final String ATTR_ADS_EQUIPMENT_TYPE = "";
	// 47 AU адрес пульта АДС
	public static final String ATTR_ADS_CONSOLE_ADDRESS = "";
	// 48 AV адрес пульта АДС
	public static final String ATTR_ADS_CONSOLE_ADDRESS_2 = "";
	// 49 AW адрес пульта АДС
	public static final String ATTR_ADS_CONSOLE_ADDRESS_3 = "";
	// 50 AX вводной щит здания
	public static final String ATTR_BUILDING_INPUT_SHIELD = "";
	// 51 AY щит подъезда
	public static final String ATTR_DOORWAY_SHIELD = "";
	// 52 AZ домофон
	public static final String ATTR_DOOR_INTERCOM = "";
	// 53 BA блок освещения
	public static final String ATTR_LIGHTENING_BLOCK = "";
	// 54 BB административное переговорное устройство
	public static final String ATTR_ADMINISTER_INTERCOM = "";
	// 55 BC блок лифта
	public static final String ATTR_LIFT_BLOCK = "";
	// 56 BD лифтовое переговорное устройство
	public static final String ATTR_LIFT_INTERCOM = "";
	// 57 BE блок разъемов
	public static final String ATTR_SOCKETS_BLOCK = "";
	// 58 BF щит бойлерной
	public static final String ATTR_BOILERPLACE_SHIELD = "";
	// 59 BG кол-во квартир со скрытой электропроводкой
	public static final String ATTR_OPEN_ELECTRIC_WIRING_APARTMENT_NUMBER = "";
	// 60 BH кол-во квартир с открытой электропроводкой
	public static final String ATTR_OPEN_ELECTRIC_WIRING_APARTMENT_NUMBER_2 = "";
	// 61 BI кол-во подвалов
	public static final String ATTR_BASEMENTS_NUMBER = "";
	// 62 BJ общая площади придомовой территории
	public static final String ATTR_NEAR_HOUSE_TERRITORY_TOTAL_SQUARE = "";
	// 63 BK площади внекатигорийной придомовой территории
	public static final String ATTR_NEAR_HOUSE_NONCATEGORY_TERRITORY_TOTAL_SQUARE = "";
	// 64 BL площади придомовой территории 1-й категории
	public static final String ATTR_NEAR_HOUSE_1ST_CATEGORY_TERRITORY_TOTAL_SQUARE = "";
	// 65 BM площади придомовой территории 2-й категории
	public static final String ATTR_NEAR_HOUSE_2ND_CATEGORY_TERRITORY_TOTAL_SQUARE = "";
	// 66 BN площади придомовой территории 3-й категории
	public static final String ATTR_NEAR_HOUSE_3D_CATEGORY_TERRITORY_TOTAL_SQUARE = "";
	// 67 BO общая площадь жилого фонда, оборудованного водостоками
	public static final String ATTR_GUTTER_EQUIPPED_TOTAL_SQUARE = "";
	// 68 BP протяженность внутридомойвой канализации
	public static final String ATTR_INHOUSE_SEWER_LENGTH = "";
	// 69 BQ материал внутридомойвой канализации
	public static final String ATTR_INHOUSE_SEWER_MATERIAL = "";
	// 70 BR тип системы внутридомойвой канализации
	public static final String ATTR_INHOUSE_SEWER_SYSTEM_TYPE = "";
	// 71 BS общая площадь жилого фонда, жильцы которого пользуются дворовыми туалетами, подключенными к центральной канализации
	public static final String ATTR_TOILETS_WITH_CENTRAL_SEWER_TOTAL_SQUARE = "";
	// 72 BT общая площадь квартир
	public static final String ATTR_APARTMENTS_TOTAL_SQUARE = "";
	// 73 BU тариф за уборку придомовой территории
	public static final String ATTR_NEARHOUSE_TERRITORY_CLEANUP_TARIF = "";
	// 74 BV тариф за очистку мусоропроводов и загрузку контейнеров
	public static final String ATTR_RUBBISH_CHUTE_CLEANUP_TARIF = "";
	// 75 BW тариф за уборку подвалов, тех.этажей и кровель
	public static final String ATTR_BASEMENTS_CLEANUP_TARIF = "";
	// 76 BX тариф за вывоз и утилизацию твердых бытовых и негабаритных отходов
	public static final String ATTR_BIG_GARBAGE_REMOVAL_TARIF = "";
	// 77 BY тариф за тех.обслуживание лифтов
	public static final String ATTR_LIFT_SUPPORT_TARIF = "";
	// 78 BZ тариф за обслуживание систем диспетчеризации
	public static final String ATTR_DISPETCHER_SYSTEMS_SUPPORT_TARIF = "";
	// 79 CA тариф за тех.обслуживание внутридомовых сетей водоснабжения
	public static final String ATTR_WATER_SUPPLY_NETS_SUPPORT_TARIF = "";
	// 80 CB тариф за тех.обслуживание внутридомовых сетей водоотведения
	public static final String ATTR_WATER_REMOVAL_NETS_SUPPORT_TARIF = "";
	// 81 CC тариф за тех.обслуживание внутридомовых сетей теплоснабжения
	public static final String ATTR_WARM_SUPPLY_NETS_SUPPORT_TARIF = "";
	// 82 CD тариф за тех.обслуживание внутридомовых сетей горячего водоснабжения
	public static final String ATTR_HOT_WATER_SUPPLY_NETS_SUPPORT_TARIF = "";
	// 83 CE тариф за тех.обслуживание бойлеров
	public static final String ATTR_BOILERS_SUPPORT_TARIF = "";
	// 84 CF тариф за обслуживание дымовентиляционных каналов
	public static final String ATTR_VENT_CHANNEL_SUPPORT_TARIF = "";
	// 85 CG тариф за очистку дворовых туалетов
	public static final String ATTR_COURT_TOILETS_CLEANUP_TARIF = "";
	// 86 CH тариф за освещение мест общего пользования
	public static final String ATTR_COMMON_PLACES_LIGHTENING_TARIF = "";
	// 87 CI тариф за энергоснабжение насосов подкачки воды
	public static final String ATTR_WATER_PUMPS_ELECTRICITY_TARIF = "";
	// 88 CJ тариф за энергоснабжение лифтов
	public static final String ATTR_LIFT_ENERGYSAVE_TARIF = "";
	// 89 CK итоговый тариф
	public static final String ATTR_TOTAL_TARIF = "";
	// 90 CL
	// 91 CM тариф за освещение мест общего пользования (в части электриков)
	public static final String ATTR_COMMON_PLACES_LIGHTENING_TARIF_PART_ELECTRICIANS = "";
	// 92 CN тариф за освещение мест общего пользования (в части «Облэнерго»)
	public static final String ATTR_COMMON_PLACES_LIGHTENING_TARIF_PART_OBLENERGO = "";
	// 93 CO тариф за уборку подвалов, тех.этажей и кровель (в части подвалов)
	public static final String ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP_TARIF_PART_BASEMENTS = "";
	// 94 CP тариф за уборку подвалов, тех.этажей и кровель (в части тех.этажей и кровель)
	public static final String ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP_TARIF_PART_TECHFLOORS_ROOFS = "";
	// 95 CQ ???? (есть значение ТЦРП)
	public static final String ATTR_XXXX_TCRP = "";
	// 96 CR
	// 97 CS
	// 98 CT уборка придомовой территории
	public static final String ATTR_NEAR_HOUSE_TERRITORY_CLEANUP = "";
	// 99 CU очистка мусоропроводов и загрузка контейнеров
	public static final String ATTR_RUBBISH_CHUTES_CLEANUP = "";
	// 100 CV уборка подвалов, тех.этажей и кровель
	public static final String ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP = "";
	// 101 CW вывоз и утилизация твердых бытовых и негабаритных отходов
	public static final String ATTR_TBO_REMOVAL = "";
	// 102 CX тех.обслуживание лифтов
	public static final String ATTR_LIFTS_TECH_SUPPORT = "";
	// 103 CY обслуживание систем диспетчеризации
	public static final String ATTR_DISPETCHER_SYSTEMS_TECH_SUPPORT = "";
	// 104 CZ тех.обслуживание внутридомовых сетей водоснабжения
	public static final String ATTR_WATER_SUPPLY_NETS_TECH_SUPPORT = "";
	// 105 DA тех.обслуживание внутридомовых сетей водоотведения
	public static final String ATTR_WATER_REMOVAL_NETS_TECH_SUPPORT = "";
	// 106 DB тех.обслуживание внутридомовых сетей теплоснабжения
	public static final String ATTR_WARM_SUPPLY_NETS_TECH_SUPPORT = "";
	// 107 DC тех.обслуживание внутридомовых сетей горячего водоснабжения
	public static final String ATTR_HOT_WATER_NETS_TECH_SUPPORT = "";
	// 108 DD тех.обслуживание бойлеров
	public static final String ATTR_BOILERS_TECH_SUPPORT = "";
	// 109 DE обслуживание дымовентиляционных каналов
	public static final String ATTR_VENT_CHANNELS_SUPPORT = "";
	// 110 DF очистка дворовых туалетов
	public static final String ATTR_COURT_TOILETS_CLEANUP = "";
	// 111 DG освещение мест общего пользования
	public static final String ATTR_COMMON_PLACES_LIGHTENING = "";
	// 112 DH энергоснабжение насосов подкачки воды
	public static final String ATTR_WATER_PUMPS_ENERGY_SUPPLY = "";
	// 113 DI энергоснабжение лифтов
	public static final String ATTR_LIFTS_ENERGY_SUPPLY = "";
	// 114 DJ итого
	public static final String ATTR_TOTAL = "";
	// 115 DK уборка подвалов, тех.этажей и кровель (в части подвалов)
	public static final String ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP_PART_BASEMENTS = "";
	// 116 DL уборка подвалов, тех.этажей и кровель (в части кровель)
	public static final String ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP_PART_ROOF = "";
	// 117 DM кол-во мусоропроводов
	public static final String ATTR_RUBBISH_CHUTES_NUMBER = "";
	// 118 DN ошибки
	public static final String ATTR_ERRORS = "";
	// 119 DO площадь, оборудованная дворовыми туалетами
	public static final String ATTR_COURT_TOILETS_EQUIPED_SQUARE = "";
	// 120 DP освещение мест общего пользования без одноэтажных домов
	public static final String ATTR_COMMON_PLACES_LIGHTENING_WITHOUT_ONEFLOOR_HOUSES = "";
	// 121 DQ кол-во контейнеров для сбора ТБО
	public static final String ATTR_TBO_CONTAINER_NUMBER = "";

	static {
		temporalFlags.put(ATTR_SECTION_NUMBER, false);
	}

}
