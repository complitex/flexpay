ALTER TABLE payments_documents_tbl CHANGE COLUMN reference_document_id reference_document_id bigint comment 'Reference document reference';

update common_version_tbl set last_modified_date='2009-04-15', date_version=0;