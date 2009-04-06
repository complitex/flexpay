-- Init organizations
-- EIRC is the first one, ID=1
-- CN is the fourth one, ID=4
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp)
	VALUES (1, 0, 0, '', '', '-------', '123');
SELECT @organization_eirc:=1;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Eirc itself', @ru_id, @organization_eirc);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('EIRC', @ru_id, @organization_eirc);

INSERT INTO common_flexpay_modules_tbl (name) VALUES ('orgs');
