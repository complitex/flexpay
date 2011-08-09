package org.flexpay.payments.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class ServiceType extends DomainObjectWithStatus {

	//Квартплата
	public static final int RENT = 1;
	//Содержание собак
	public static final int DOGS = 2;
	//Гараж
	public static final int GARAGE = 3;
	//Отопление
	public static final int HEATING = 4;
	//Подогрев воды
	public static final int WATER_COOLING = 5;
	//Холодная вода
	public static final int COLD_WATER = 7;
	//Горячая вода
	public static final int HOT_WATER = 8;
	//Уборка территории
	public static final int TERRITORY_CLEANING = 10;
	//Водоснабжение
	public static final int WATER_SUPPLY = 12;
	//Водоотведение
	public static final int WATER_DISPOSAL = 13;
	//Очистка мусоросборников
	public static final int CLEANING_GARBAGE_COLLECTORS = 20;
	//Уборка подвалов, тех.этажей, крыш
	public static final int CLEANING_EXT = 30;
	//Вывоз и утилизация ТБО
	public static final int TBO = 40;
	//ТО лифтов
	public static final int TO_ELEVATORS = 50;
	//ТО систем диспетчеризации
	public static final int TO_DISPATCHING = 60;
	//ТО систем водоснабжения
	public static final int TO_WATER_SUPPLY = 70;
	//ТО систем водоотведения
	public static final int TO_WATER_DISPOSAL = 80;
	//ТО систем теплоснабжения
	public static final int TO_HEAT_SUPPLY = 90;
	//ТО систем горячего водоснабжения
	public static final int TO_HOT_WATER_SUPPLY = 100;
	//ТО бойлеров
	public static final int TO_BOILERS = 110;
	//Обслуживание дымоотв. каналов
	public static final int FOG_CANALS = 120;
	//Очистка дворовых туалетов
	public static final int CLEANING_TOILETS = 130;
	//Освещение мест общего пльзования
	public static final int PUBLIC_SPACE_LIGHTING = 140;
	//Энергоснабж. для подкачки воды
	public static final int WATER_SUPPLY_ENERGY = 150;
	//Энергоснабжение для лифтов
	public static final int ELEVATORS_ENERGY = 160;
	//Уборка лестничных клеток
	public static final int STAIRCASES_CLEANING = 170;
	//Дератизация и дезинфекция
	public static final int DISINFECTION = 180;
	//ТО бытовых электроплит
	public static final int ELECTRIC_COOKER = 190;
	//Гараж
	public static final int GARAGE2 = 220;
	//Погреба
	public static final int CELLAR = 38;
	//Содержание животных
	public static final int KEEP_OF_ANIMALS = 250;
	//Электроэнергия
	public static final int ELECTRICITY = 1002;
	//Сарай
	public static final int SHED = 230;
	//Кладовая
	public static final int LARDER = 1004;
	//Хозрасходы
	public static final int HOUSEHOLD_COST = 1005;
	//Канализация
	public static final int SEWERAGE = 1006;
	//Газ варочный
	public static final int COOKING_GAS = 1007;
	//Газ отопительный
	public static final int HEATING_GAS = 1008;
	//Радио
	public static final int RADIO = 1009;
	//Антена
	public static final int ANTENNA = 1010;
	//Телефон
	public static final int PHONE = 1011;
	//Ассенизация
	public static final int CESSPOOL_CLEANING = 1012;
	//Лифт
	public static final int LIFT = 1013;
	//Налог на землю
	public static final int GROUND_TAX = 1014;
	//Повторное подключение
	public static final int REPEAT_TURN_ON = 1015;
	//Оплата по актам
	public static final int ACTS_PAYMENT = 1016;
	//Ремонт счётчиков
	public static final int COUNTERS_REPAIR = 1017;

	private int code;
	private Set<ServiceTypeNameTranslation> typeNames = set();

	public ServiceType() {
	}

	public ServiceType(Long id) {
		super(id);
	}

	public ServiceType(Stub<ServiceType> stub) {
		super(stub.getId());
	}

	public Set<ServiceTypeNameTranslation> getTypeNames() {
		return typeNames;
	}

	public void setTypeNames(Set<ServiceTypeNameTranslation> typeNames) {
		this.typeNames = typeNames;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setTypeName(ServiceTypeNameTranslation nameTranslation) {
		if (typeNames == null) {
			typeNames = set();
		}

		ServiceTypeNameTranslation candidate = null;
		for (ServiceTypeNameTranslation name : typeNames) {
			if (name.getLang().getId().equals(nameTranslation.getLang().getId())) {
				candidate = name;
				break;
			}
		}

		if (candidate != null) {
			if (StringUtils.isBlank(nameTranslation.getName()) && StringUtils.isBlank(nameTranslation.getDescription())) {
				typeNames.remove(candidate);
				return;
			}
			candidate.setName(nameTranslation.getName());
			candidate.setDescription(nameTranslation.getDescription());
			return;
		}

		if (StringUtils.isBlank(nameTranslation.getName()) && StringUtils.isBlank(nameTranslation.getDescription())) {
			return;
		}

		nameTranslation.setTranslatable(this);
		typeNames.add(nameTranslation);
	}

	public String getName(@NotNull Locale locale) {
		return TranslationUtil.getTranslation(typeNames, locale).getName();
	}

	public String getName() {
		return getName(ApplicationConfig.getDefaultLocale());
	}

	/**
	 * Get name translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public ServiceTypeNameTranslation getTranslation(@NotNull Language lang) {

		for (ServiceTypeNameTranslation translation : getTypeNames()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}


	@Override
	public boolean equals(Object obj) {
		return obj instanceof ServiceType && super.equals(obj);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("code", code).
				toString();
	}

}
