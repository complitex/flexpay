package org.flexpay.bti.service.importexport.impl;

import org.flexpay.bti.service.importexport.AttributeNameMapper;
import org.flexpay.common.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.flexpay.bti.persistence.building.BuildingAttributeConfig.*;

public class CNCSVAttributeNameMapper implements AttributeNameMapper {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static final List<String> attributeNames = CollectionUtils.list(
			null,
			// 1
			null,
			// 2
			null,
			// 3
			null,
			// 4
			null,
			// 5
			null,
			// 6
			null,
			// 7 G	№ участка
			ATTR_SECTION_NUMBER,
			// 8 H год постройки
			ATTR_BUILD_YEAR,
			// 9 I серия, тип дома
			ATTR_HOUSE_TYPE,
			// 10 J кол-во этажей
			ATTR_FLOORS_NUMBER,
			// 11 K кол-во подъездов
			ATTR_DOORWAYS_NUMBER,
			// 12 L кол-во квартир
			ATTR_APARTMENTS_NUMBER,
			// 13 M жилая площадь
			ATTR_LIVE_SQUARE,
			// 14 N кол-во проживающих
			ATTR_HABITANTS_NUMBER,
			// 15 O кол-во бойлерных установок
			ATTR_BOILER_PLANT_NUMBER,
			// 16 P протяженность сетей цетрального отопления
			ATTR_CENTRAL_WARN_NET_LENGTH,
			// 17 Q общая площадь жилого фонда, оборудованного центральным отоплением
			ATTR_TOTAL_SQUARE_WITH_CENTRAL_WARM,
			// 18 R общая площадь жилого фонда, оборудованного центральным горячим водоснабжением
			ATTR_TOTAL_SQUARE_WITH_CENTRAL_HOT_WATER,
			// 19 S общая площадь жилого фонда с бойлерными установками
			ATTR_TOTAL_SQUARE_WITH_BOILER_PLANTS,
			// 20 T общая площадь жилого фонда, оборудованного водопроводом
			ATTR_TOTAL_SQUARE_WITH_WATER,
			// 21 U общая площадь жилого фонда, оборудованного канализацией
			ATTR_TOTAL_SQUARE_WITH_SEWER,
			// 22 V общая площадь жилого фонда, оборудованного мусоропроводами
			ATTR_TOTAL_SQUARE_WITH_REFUSE_CHUTES,
			// 23 W общая площадь жилого фонда
			ATTR_TOTAL_SQUARE,
			// 24 X общая площадь жилых помещений в домах, где вода подается при помощи насосов подкчки
			ATTR_TOTAL_SQUARE_WITH_WATER_PUMPS,
			// 25 Y кол-во квартир гостинничного типа
			ATTR_HOTEL_TYPE_APARTMENTS_NUMBER,
			// 26 Z площадь кровли (мягкая)
			ATTR_SOFT_ROOF_SQUARE,
			// 27 AA площадь кровли (шифер)
			ATTR_HARD_SLATE_ROOF_SQUARE,
			// 28 AB площадь кровли (металлическая)
			ATTR_HARD_METAL_ROOF_SQUARE,
			// 29 AC площадь подвалов
			ATTR_BASEMENT_SQUARE,
			// 30 AD площадь тех.этажей
			ATTR_TECHNICAL_FLOORS_SQUARE,
			// 31 AE Площадь чердаков
			ATTR_ARRET_SQUARE,
			// 32 AF Кол-во вентиляционных каналов
			ATTR_VENT_CHANNEL_NUMBER,
			// 33 AG Кол-во дымоходов
			ATTR_FLUES_NUMBER,
			// 34 AH общая площадь первых этажей
			ATTR_FIRST_FLOORS_TOTAL_SQUARE,
			// 35 AI общая площадь квартир, оборудованных лифтами
			ATTR_LIFTED_APARTMENTS_TOTAL_SQUARE,
			// 36 AJ кол-во лифтов
			ATTR_LIFTS_NUMBER,
			// 37 AK тип лифта
			ATTR_LIFT_TYPE,
			// 38 AL тип лифта
			ATTR_LIFT_TYPE_2,
			// 39 AM срок эксплуатации лифта
			ATTR_LIFT_USAGE_AGE,
			// 40 AN срок эксплуатации лифта
			ATTR_LIFT_USAGE_AGE_2,
			// 41 AO диспетчеризованные лифты
			ATTR_LIFTS_WITH_DISPETCHER,
			// 42 AP диспетчеризованные лифты
			ATTR_LIFTS_WITH_DISPETCHER_2,
			// 43 AQ недиспетчеризованные лифты
			ATTR_LIFTS_WITHOUT_DISPETCHER,
			// 44 AR недиспетчеризованные лифты
			ATTR_LIFTS_WITHOUT_DISPETCHER_2,
			// 45 AS общая площадь квартир в домах, оборудованных АДС
			ATTR_ADS_SUITED_APARTMENTS_TOTAL_SQUARE,
			// 46 AT тип оборудования АДС
			ATTR_ADS_EQUIPMENT_TYPE,
			// 47 AU адрес пульта АДС
			ATTR_ADS_CONSOLE_ADDRESS,
			// 48 AV адрес пульта АДС
			ATTR_ADS_CONSOLE_ADDRESS_2,
			// 49 AW адрес пульта АДС
			ATTR_ADS_CONSOLE_ADDRESS_3,
			// 50 AX вводной щит здания
			ATTR_BUILDING_INPUT_SHIELD,
			// 51 AY щит подъезда
			ATTR_DOORWAY_SHIELD,
			// 52 AZ домофон
			ATTR_DOOR_INTERCOM,
			// 53 BA блок освещения
			ATTR_LIGHTENING_BLOCK,
			// 54 BB административное переговорное устройство
			ATTR_ADMINISTER_INTERCOM,
			// 55 BC блок лифта
			ATTR_LIFT_BLOCK,
			// 56 BD лифтовое переговорное устройство
			ATTR_LIFT_INTERCOM,
			// 57 BE блок разъемов
			ATTR_SOCKETS_BLOCK,
			// 58 BF щит бойлерной
			ATTR_BOILERPLACE_SHIELD,
			// 59 BG кол-во квартир со скрытой электропроводкой
			ATTR_OPEN_ELECTRIC_WIRING_APARTMENT_NUMBER,
			// 60 BH кол-во квартир с открытой электропроводкой
			ATTR_OPEN_ELECTRIC_WIRING_APARTMENT_NUMBER_2,
			// 61 BI кол-во подвалов
			ATTR_BASEMENTS_NUMBER,
			// 62 BJ общая площади придомовой территории
			ATTR_NEAR_HOUSE_TERRITORY_TOTAL_SQUARE,
			// 63 BK площади внекатигорийной придомовой территории
			ATTR_NEAR_HOUSE_NONCATEGORY_TERRITORY_TOTAL_SQUARE,
			// 64 BL площади придомовой территории 1-й категории
			ATTR_NEAR_HOUSE_1ST_CATEGORY_TERRITORY_TOTAL_SQUARE,
			// 65 BM площади придомовой территории 2-й категории
			ATTR_NEAR_HOUSE_2ND_CATEGORY_TERRITORY_TOTAL_SQUARE,
			// 66 BN площади придомовой территории 3-й категории
			ATTR_NEAR_HOUSE_3D_CATEGORY_TERRITORY_TOTAL_SQUARE,
			// 67 BO общая площадь жилого фонда, оборудованного водостоками
			ATTR_GUTTER_EQUIPPED_TOTAL_SQUARE,
			// 68 BP протяженность внутридомойвой канализации
			ATTR_INHOUSE_SEWER_LENGTH,
			// 69 BQ материал внутридомойвой канализации
			ATTR_INHOUSE_SEWER_MATERIAL,
			// 70 BR тип системы внутридомойвой канализации
			ATTR_INHOUSE_SEWER_SYSTEM_TYPE,
			// 71 BS общая площадь жилого фонда, жильцы которого пользуются дворовыми туалетами, подключенными к центральной канализации
			ATTR_TOILETS_WITH_CENTRAL_SEWER_TOTAL_SQUARE,
			// 72 BT общая площадь квартир
			ATTR_APARTMENTS_TOTAL_SQUARE,
			// 73 BU тариф за уборку придомовой территории
			ATTR_NEARHOUSE_TERRITORY_CLEANUP_TARIFF,
			// 74 BV тариф за очистку мусоропроводов и загрузку контейнеров
			ATTR_RUBBISH_CHUTE_CLEANUP_TARIFF,
			// 75 BW тариф за уборку подвалов, тех.этажей и кровель
			ATTR_BASEMENTS_CLEANUP_TARIFF,
			// 76 BX тариф за вывоз и утилизацию твердых бытовых и негабаритных отходов
			ATTR_BIG_GARBAGE_REMOVAL_TARIFF,
			// 77 BY тариф за тех.обслуживание лифтов
			ATTR_LIFT_SUPPORT_TARIFF,
			// 78 BZ тариф за обслуживание систем диспетчеризации
			ATTR_DISPETCHER_SYSTEMS_SUPPORT_TARIFF,
			// 79 CA тариф за тех.обслуживание внутридомовых сетей водоснабжения
			ATTR_WATER_SUPPLY_NETS_SUPPORT_TARIFF,
			// 80 CB тариф за тех.обслуживание внутридомовых сетей водоотведения
			ATTR_WATER_REMOVAL_NETS_SUPPORT_TARIFF,
			// 81 CC тариф за тех.обслуживание внутридомовых сетей теплоснабжения
			ATTR_WARM_SUPPLY_NETS_SUPPORT_TARIFF,
			// 82 CD тариф за тех.обслуживание внутридомовых сетей горячего водоснабжения
			ATTR_HOT_WATER_SUPPLY_NETS_SUPPORT_TARIFF,
			// 83 CE тариф за тех.обслуживание бойлеров
			ATTR_BOILERS_SUPPORT_TARIFF,
			// 84 CF тариф за обслуживание дымовентиляционных каналов
			ATTR_VENT_CHANNEL_SUPPORT_TARIFF,
			// 85 CG тариф за очистку дворовых туалетов
			ATTR_COURT_TOILETS_CLEANUP_TARIFF,
			// 86 CH тариф за освещение мест общего пользования
			ATTR_COMMON_PLACES_LIGHTENING_TARIFF,
			// 87 CI тариф за энергоснабжение насосов подкачки воды
			ATTR_WATER_PUMPS_ELECTRICITY_TARIFF,
			// 88 CJ тариф за энергоснабжение лифтов
			ATTR_LIFT_ENERGYSAVE_TARIFF,
			// 89 CK итоговый тариф
			ATTR_TOTAL_TARIFF,
			// 90 CL
			null,
			// 91 CM тариф за освещение мест общего пользования (в части электриков)
			ATTR_COMMON_PLACES_LIGHTENING_TARIFF_PART_ELECTRICIANS,
			// 92 CN тариф за освещение мест общего пользования (в части «Облэнерго»)
			ATTR_COMMON_PLACES_LIGHTENING_TARIFF_PART_OBLENERGO,
			// 93 CO тариф за уборку подвалов, тех.этажей и кровель (в части подвалов)
			ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP_TARIFF_PART_BASEMENTS,
			// 94 CP тариф за уборку подвалов, тех.этажей и кровель (в части тех.этажей и кровель)
			ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP_TARIFF_PART_TECHFLOORS_ROOFS,
			// 95 CQ ???? (есть значение ТЦРП)
			ATTR_XXXX_TCRP,
			// 96 CR
			null,
			// 97 CS
			null,
			// 98 CT уборка придомовой территории
			ATTR_NEAR_HOUSE_TERRITORY_CLEANUP,
			// 99 CU очистка мусоропроводов и загрузка контейнеров
			ATTR_RUBBISH_CHUTES_CLEANUP,
			// 100 CV уборка подвалов, тех.этажей и кровель
			ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP,
			// 101 CW вывоз и утилизация твердых бытовых и негабаритных отходов
			ATTR_TBO_REMOVAL,
			// 102 CX тех.обслуживание лифтов
			ATTR_LIFTS_TECH_SUPPORT,
			// 103 CY обслуживание систем диспетчеризации
			ATTR_DISPETCHER_SYSTEMS_TECH_SUPPORT,
			// 104 CZ тех.обслуживание внутридомовых сетей водоснабжения
			ATTR_WATER_SUPPLY_NETS_TECH_SUPPORT,
			// 105 DA тех.обслуживание внутридомовых сетей водоотведения
			ATTR_WATER_REMOVAL_NETS_TECH_SUPPORT,
			// 106 DB тех.обслуживание внутридомовых сетей теплоснабжения
			ATTR_WARM_SUPPLY_NETS_TECH_SUPPORT,
			// 107 DC тех.обслуживание внутридомовых сетей горячего водоснабжения
			ATTR_HOT_WATER_NETS_TECH_SUPPORT,
			// 108 DD тех.обслуживание бойлеров
			ATTR_BOILERS_TECH_SUPPORT,
			// 109 DE обслуживание дымовентиляционных каналов
			ATTR_VENT_CHANNELS_SUPPORT,
			// 110 DF очистка дворовых туалетов
			ATTR_COURT_TOILETS_CLEANUP,
			// 111 DG освещение мест общего пользования
			ATTR_COMMON_PLACES_LIGHTENING,
			// 112 DH энергоснабжение насосов подкачки воды
			ATTR_WATER_PUMPS_ENERGY_SUPPLY,
			// 113 DI энергоснабжение лифтов
			ATTR_LIFTS_ENERGY_SUPPLY,
			// 114 DJ итого
			ATTR_TOTAL,
			// 115 DK уборка подвалов, тех.этажей и кровель (в части подвалов)
			ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP_PART_BASEMENTS,
			// 116 DL уборка подвалов, тех.этажей и кровель (в части кровель)
			ATTR_BASEMENTS_TECHFLOORS_ROOFS_CLEANUP_PART_ROOF,
			// 117 DM кол-во мусоропроводов
			ATTR_RUBBISH_CHUTES_NUMBER,
			// 118 DN ошибки
			ATTR_ERRORS,
			// 119 DO площадь, оборудованная дворовыми туалетами
			ATTR_COURT_TOILETS_EQUIPED_SQUARE,
			// 120 DP освещение мест общего пользования без одноэтажных домов
			ATTR_COMMON_PLACES_LIGHTENING_WITHOUT_ONEFLOOR_HOUSES,
			// 121 DQ кол-во контейнеров для сбора ТБО
			ATTR_TBO_CONTAINER_NUMBER,
			// 122 DR площадь, оборудованная дворовыми туалетами ????
			ATTR_TOTAL_SQUARE_EQUIPPED_COURT_TOILETS,
			// 123 DS ???? ЕСЛИ(V = W; 1; 0)
			ATTR_DS,
			// 124 DT тех.обслуживание лифтов в месяц, грн.
			ATTR_COST_LIFT_TECHNICAL_SUPPORT,
			// 125 DU тех.обслуживание лифтов в месяц с КП «Жилкомсервис», грн. DT * 1.214263
			ATTR_COST_LIFT_TECHNICAL_SUPPORT_ZHILKOMSERVIS,
			// 126 DV диспетчеризация в месяц, грн.
			ATTR_COST_DISPETCHERING,
			// 127 DW диспетчеризация в месяц с КП «Жилкомсервис», грн. DV * 1.214263
			ATTR_COST_DISPETCHIRING_ZHILKOMSERVIS,
			// 128 DX освещение мест общего пользования (в части КСП «Харьковгорлифт»), грн.
			ATTR_COST_COMMON_PLACES_LIGHTENING_KHARKOVGORLIFT,
			// 129 DY освещение мест общего пользования (в части КСП «Харьковгорлифт») с КП «Жилкомсервис», грн. DX * 1.214263
			ATTR_COST_COMMON_PLACES_LIGHTENING_KHARKOVGORLIFT_ZHILKOMSERVIS
	);

	static {
		if (attributeNames.size() != 130) {
			throw new IllegalStateException("Invalid mapping configuration, expected 129 attribute names");
		}
	}

	/**
	 * Get name of the n-th attribute, returned <code>null</code> is for unknown position of attribute
	 *
	 * @param n Order of attribute to get name for, 1-based
	 * @return Attribute name
	 */
	public String getName(int n) {

		if (n < 1 || n >= attributeNames.size()) {
			log.warn("Illegal attribute position requested {}, max is {}", n, attributeNames.size()-1);
			return null;
		}

		return attributeNames.get(n);
	}

}
