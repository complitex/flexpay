update ab_sync_changes_tbl set order_weight=(
		case object_type
		when 0 then 0
		when 1 then 1
		when 2 then 3
		when 3 then 4
		when 4 then 2
		when 5 then 5
		when 6 then 6
		end
);
-- Town(0, 0), District(1, 1), Street(2, 3), Building(3, 4), StreetType(4, 2), Apartment(5, 5), Person(6, 6)
