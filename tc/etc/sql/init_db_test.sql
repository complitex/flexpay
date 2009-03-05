-- Init TC Tariff calculation results for tests
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id) VALUES
    ('170.56', '2008-01-30', '2009-01-30', @building_id_1, @tariff_010),
    ('1235.56', '2008-05-30', '2009-01-30', @building_id_1, @tariff_020),
    ('6773.56', '2008-01-30', '2009-01-30', @building_id_1, @tariff_030),
    ('3142.56', '2008-03-30', '2009-01-30', @building_id_1, @tariff_040),
    ('5670.56', '2008-06-30', '2009-01-30', @building_id_2, @tariff_010),
    ('1360.56', '2008-09-23', '2009-01-30', @building_id_2, @tariff_040),
    ('1457470.56', '2008-01-12', '2009-01-30', @building_id_2, @tariff_080),
    ('16766.56', '2008-01-11', '2009-01-30', @building_id_2, @tariff_120),
    ('15346.56', '2008-01-12', '2009-01-30', @building_id_2, @tariff_130),
    ('167.56', '2008-01-23', '2009-01-30', @building_id_3, @tariff_100),
    ('1232365326.56', '2008-01-11', '2009-01-30', @building_id_3, @tariff_120),
    ('8485.575', '2008-01-11', '2009-01-30', @building_id_3, @tariff_150),
    ('32525.678', '2008-01-11', '2009-01-30', @building_id_3, @tariff_170),
    ('2525.437', '2008-01-06', '2009-01-30', @building_id_4, @tariff_010),
    ('1253.56', '2008-01-12', '2009-01-30', @building_id_4, @tariff_020),
    ('2346.56', '2008-01-07', '2009-01-30', @building_id_4, @tariff_030),
    ('3393.56', '2008-01-09', '2009-01-30', @building_id_4, @tariff_040),
    ('12262.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_050),
    ('23456456.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_060),
    ('132466.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_070),
    ('467436.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_080),
    ('3222.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_090),
    ('1111.1223', '2008-01-10', '2009-01-30', @building_id_4, @tariff_100),
    ('11251.3257', '2008-01-10', '2009-01-30', @building_id_4, @tariff_110),
    ('34345.56', '2008-05-18', '2009-01-30', @building_id_4, @tariff_120),
    ('345340.56', '2008-05-14', '2009-01-30', @building_id_4, @tariff_130),
    ('245423.56', '2008-05-13', '2009-01-30', @building_id_5, @tariff_140),
    ('1333.56', '2008-06-30', '2009-01-30', @building_id_5, @tariff_150),
    ('25352.56', '2008-06-13', '2009-01-30', @building_id_5, @tariff_160),
    ('456456.56', '2008-06-11', '2009-01-30', @building_id_5, @tariff_170),
    ('29874.56', '2008-06-23', '2009-01-30', @building_id_6, @tariff_180),
    ('4444.56', '2008-06-11', '2009-01-23', @building_id_7, @tariff_040),
    ('83932.56', '2008-01-11', '2009-01-23', @building_id_7, @tariff_060),
    ('9087.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_050),
    ('789170.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_060),
    ('1234560.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_070),
    ('684226.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_180),
    ('8493.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_190),
    ('937484.56', '2008-01-30', '2009-01-23', @building_id_9, @tariff_020),
    ('123455.56', '2008-01-30', '2009-01-23', @building_id_9, @tariff_030),
    ('23423.56', '2008-01-30', '2009-01-23', @building_id_9, @tariff_040),
    ('67382.56', '2008-01-30', '2009-01-23', @building_id_9, @tariff_050);
