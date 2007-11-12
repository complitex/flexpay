drop table if exists Act;

drop table if exists Act_Files;

drop table if exists Acts;

drop table if exists Apartment;

drop table if exists Apartment_Attribute;

drop table if exists Apartment_Attribute_Type;

drop table if exists Apartment_Attribute_Type_Name;

drop table if exists Apartment_Number;

drop table if exists Apartment_Relation;

drop table if exists Apartment_relation_type;

drop table if exists Building;

drop table if exists Building_Attribute;

drop table if exists Building_Attribute_Type;

drop table if exists Building_Attribute_Type_Lang;

drop table if exists Building_Status;

drop table if exists Buildings;

drop table if exists Country;

drop table if exists District;

drop table if exists District_Name;

drop table if exists District_Name_Lang_Value;

drop table if exists History;

drop table if exists Identities;

drop table if exists Identity_Name;

drop table if exists Identity_Type;

drop table if exists Person;

drop table if exists PersonAct;

drop table if exists Person_ATTRIBUTE;

drop table if exists Person_Identity;

drop table if exists Person_Identity_ATTRIBUTE;

drop table if exists Region;

drop table if exists Region_Name;

drop table if exists Region_Name_Lang_Value;

drop table if exists Room;

drop table if exists Room_Attribute;

drop table if exists Room_Attribute_Type;

drop table if exists Room_Attribute_Type_Name;

drop table if exists Rooms;

drop table if exists Street;

drop table if exists Street_Name;

drop table if exists Street_Name_Lang_Value;

drop table if exists Street_Type;

drop table if exists Street_Types;

drop table if exists Town;

drop table if exists Town_Name;

drop table if exists Town_Name_Lang_Value;

drop table if exists Town_Type;

drop table if exists Town_Types;

/*==============================================================*/
/* Table: Act                                                   */
/*==============================================================*/
create table Act
(
   id                             INTEGER                        not null,
   name                           varchar(4000)                  not null,
   creation_date                  timestamp                      not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Act_Files                                             */
/*==============================================================*/
create table Act_Files
(
   ID                             Integer                        not null,
   Act_ID                         INTEGER                        not null,
   FileName                       varchar(4000)                  not null,
   file_order                     INTEGER                        not null
)
type = InnoDB;

/*==============================================================*/
/* Table: Acts                                                  */
/*==============================================================*/
create table Acts
(
   Apartment_Relation_ID          INTEGER                        not null,
   Act_ID                         INTEGER                        not null
)
type = InnoDB;

/*==============================================================*/
/* Table: Apartment                                             */
/*==============================================================*/
create table Apartment
(
   ID                             INTEGER                        not null,
   Apartment_Status               INTEGER                        not null,
   Building_ID                    INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Apartment_Attribute                                   */
/*==============================================================*/
create table Apartment_Attribute
(
   id                             INTEGER                        not null,
   Apartment_ID                   INTEGER,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   value                          varchar(2000)                 not null,
   Apartment_Attribute_type_ID    INTEGER,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Apartment_Attribute_Type                              */
/*==============================================================*/
create table Apartment_Attribute_Type
(
   ID                             INTEGER                        not null,
   attribute_type_code            varchar(255)                  not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Apartment_Attribute_Type_Name                         */
/*==============================================================*/
create table Apartment_Attribute_Type_Name
(
   ID                             INTEGER                        not null,
   name                           varchar(255)                   not null,
   LANG                           varchar(255)                   not null,
   Apartment_Attribute_Type_ID    INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;


/*==============================================================*/
/* Table: Apartment_Number                                      */
/*==============================================================*/
create table Apartment_Number
(
   ID                             INTEGER                        not null,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   Apartment_ID                   INTEGER                        not null,
   value                          varchar(255)                   not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Apartment_Relation                                    */
/*==============================================================*/
create table Apartment_Relation
(
   id                             INTEGER                        not null,
   Apartment_ID                   INTEGER                        not null,
   Person_ID                      INTEGER                        not null,
   owning_part                    INTEGER                        not null,
   Apartment_relation_type_ID     INTEGER                        not null,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   primary key (id)
)
type = InnoDB;


/*==============================================================*/
/* Table: Apartment_relation_type                               */
/*==============================================================*/
create table Apartment_relation_type
(
   id                             INTEGER                        not null,
   Name                           varchar(120)                   not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Building                                              */
/*==============================================================*/
create table Building
(
   ID                             INTEGER                        not null,
   Building_Status                INTEGER                        not null,
   District_ID                    INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;


/*==============================================================*/
/* Table: Building_Attribute                                    */
/*==============================================================*/
create table Building_Attribute
(
   ID                             INTEGER                        not null,
   value                          varchar(255)                   not null,
   attribute_type_id              INTEGER                        not null,
   Buildings_ID                   INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;


/*==============================================================*/
/* Table: Building_Attribute_Type                               */
/*==============================================================*/
create table Building_Attribute_Type
(
   ID                             INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Building_Attribute_Type_Lang                          */
/*==============================================================*/
create table Building_Attribute_Type_Lang
(
   ID                             INTEGER                        not null,
   name                           varchar(255)                  not null,
   LANG                           varchar(255)                  not null,
   short_name                     varchar(255)                  not null,
   Building_Attribute_Type_ID     INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Building_Status                                       */
/*==============================================================*/
create table Building_Status
(
   ID                             INTEGER                        not null,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   Building_ID                    INTEGER                        not null,
   value                          INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Buildings                                             */
/*==============================================================*/
create table Buildings
(
   id                             integer                        not null,
   Street_ID                      integer                        not null,
   Building_ID                    integer                        not null,
   status                         integer                        not null,
   primary key (id)
)
type = InnoDB;


/*==============================================================*/
/* Table: Country                                               */
/*==============================================================*/
create table Country
(
   ID                             INTEGER                        not null,
   Country_Name                   varchar(200)                   not null,
   Country_status                 INTEGER                        not null,
   Country_Short_Name             varchar(200)                   not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: District                                              */
/*==============================================================*/
create table District
(
   ID                             INTEGER                        not null,
   District_Status                INTEGER                        not null,
   Town_ID                        INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;


/*==============================================================*/
/* Table: District_Name                                         */
/*==============================================================*/
create table District_Name
(
   ID                             INTEGER                        not null,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   District_ID                    INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: District_Name_Lang_Value                              */
/*==============================================================*/
create table District_Name_Lang_Value
(
   id                             INTEGER                        not null,
   value                          varchar(255)                   not null,
   LANG                           varchar(255)                   not null,
   DistrictName_ID                INTEGER                        not null,
   primary key (id)
)
type = InnoDB;


/*==============================================================*/
/* Table: History                                               */
/*==============================================================*/
create table History
(
   id                             INTEGER                        not null,
   record_date                    timestamp                      not null,
   action_type                    varchar(255)                   not null,
   value                          varchar(4000)                  not null,
   History_ID                     INTEGER,
   old_value                      varchar(4000),
   primary key (id)
)
type = InnoDB;


/*==============================================================*/
/* Table: Identities                                            */
/*==============================================================*/
create table Identities
(
   ID                             INTEGER                        not null,
   Person_ID                      INTEGER                        not null,
   Idenity_ID                     INTEGER                        not null,
   status                         INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;


/*==============================================================*/
/* Table: Identity_Name                                         */
/*==============================================================*/
create table Identity_Name
(
   id                             INTEGER                        not null,
   Name                           varchar(120)                   not null,
   LANG                           varchar(120)                   not null,
   IdentityType_ID                INTEGER                        not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Identity_Type                                         */
/*==============================================================*/
create table Identity_Type
(
   id                             INTEGER                        not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Person                                                */
/*==============================================================*/
create table Person
(
   id                             INTEGER                        not null,
   Apartment_ID                   INTEGER,
   BirthDate                      timestamp                      not null,
   gender                         varchar(100)                   not null,
   primary key (id)
)
type = InnoDB;


/*==============================================================*/
/* Table: PersonAct                                             */
/*==============================================================*/
create table PersonAct
(
   ID                             Integer                        not null,
   Act_ID                         Integer                        not null,
   Person_ID                      Integer                        not null
)
type = InnoDB;

/*==============================================================*/
/* Table: Person_ATTRIBUTE                                      */
/*==============================================================*/
create table Person_ATTRIBUTE
(
   ID                             INTEGER                        not null,
   Person_ID                      INTEGER                        not null,
   VALUE                          varchar(4000)                 not null,
   NAME                           varchar(255)                  not null,
   LANG                           varchar(255)                  not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: Person_Identity                                       */
/*==============================================================*/
create table Person_Identity
(
   ID                             INTEGER                        not null,
   identity_type_id               INTEGER                        not null,
   begin_date                     timestamp                      not null,
   end_date                       timestamp,
   organization                   varchar(4000)                  not null,
   first_name                     varchar(255)                   not null,
   last_name                      varchar(255)                   not null,
   middle_name                    varchar(255),
   serial_number                  INTEGER                        not null,
   document_number                INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Person_Identity_ATTRIBUTE                             */
/*==============================================================*/
create table Person_Identity_ATTRIBUTE
(
   ID                             INTEGER                        not null,
   Person_Identity_ID             INTEGER                        not null,
   VALUE                          varchar(4000)                 not null,
   NAME                           varchar(255)                  not null,
   LANG                           varchar(255)                  not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: Region                                                */
/*==============================================================*/
create table Region
(
   ID                             INTEGER                        not null,
   Region_Status                  INTEGER                        not null,
   Region_Short_Name              varchar(200)                   not null,
   Country_ID                     INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Region_Name                                           */
/*==============================================================*/
create table Region_Name
(
   ID                             INTEGER                        not null,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   Region_ID                      INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Region_Name_Lang_Value                                */
/*==============================================================*/
create table Region_Name_Lang_Value
(
   id                             INTEGER                        not null,
   value                          varchar(255)                   not null,
   LANG                           varchar(255)                   not null,
   RegionName_ID                  INTEGER                        not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Room                                                  */
/*==============================================================*/
create table Room
(
   id                             INTEGER                        not null,
   room_type_ID                   INTEGER                        not null,
   status                         char(1)                        not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Room_Attribute                                        */
/*==============================================================*/
create table Room_Attribute
(
   id                             INTEGER                        not null,
   Room_ID                        INTEGER,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   value                          varchar(2000)                 not null,
   Room_Attribute_type_ID         INTEGER,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Room_Attribute_Type                                   */
/*==============================================================*/
create table Room_Attribute_Type
(
   ID                             INTEGER                        not null,
   attribute_Type_code            varchar(255)                  not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Room_Attribute_Type_Name                              */
/*==============================================================*/
create table Room_Attribute_Type_Name
(
   ID                             INTEGER                        not null,
   name                           varchar(255)                   not null,
   LANG                           varchar(255)                   not null,
   Room_Attribute_Type_ID         INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Rooms                                                 */
/*==============================================================*/
create table Rooms
(
   id                             INTEGER                        not null,
   Room_ID                        INTEGER                        not null,
   Apartment_ID                   INTEGER                        not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Street                                                */
/*==============================================================*/
create table Street
(
   ID                             INTEGER                        not null,
   Street_status                  INTEGER                        not null,
   District_ID                    INTEGER,
   Town_ID                        INTEGER                        not null,
   Street_Type_ID                 INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Street_Name                                           */
/*==============================================================*/
create table Street_Name
(
   ID                             INTEGER                        not null,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   Street_ID                      INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Street_Name_Lang_Value                                */
/*==============================================================*/
create table Street_Name_Lang_Value
(
   id                             INTEGER                        not null,
   value                          varchar(255)                   not null,
   LANG                           varchar(255)                   not null,
   StreetName_ID                  INTEGER                        not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Street_Type                                           */
/*==============================================================*/
create table Street_Type
(
   ID                             INTEGER                        not null,
   Street_Type_ID                 INTEGER,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   Street_ID                      INTEGER                        not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Street_Types                                          */
/*==============================================================*/
create table Street_Types
(
   id                             INTEGER                        not null,
   Name                           varchar(120)                   not null,
   LANG                           varchar(255)                         not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Town                                                  */
/*==============================================================*/
create table Town
(
   ID                             INTEGER                        not null,
   Town_Status                    INTEGER                        not null,
   Region_ID                      INTEGER                        not null,
   Town_Type_id                   INTEGER,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Town_Name                                             */
/*==============================================================*/
create table Town_Name
(
   ID                             INTEGER                        not null,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   Town_ID                        INTEGER                   not null,
   primary key (ID)
)
type = InnoDB;

/*==============================================================*/
/* Table: Town_Name_Lang_Value                                  */
/*==============================================================*/
create table Town_Name_Lang_Value
(
   id                             INTEGER                        not null,
   value                          varchar(255)                   not null,
   LANG                           varchar(255)                   not null,
   TownName_ID                    INTEGER                        not null,
   primary key (id)
)
type = InnoDB;

/*==============================================================*/
/* Table: Town_Type                                             */
/*==============================================================*/
create table Town_Type
(
   id                             INTEGER                        not null,
   TownType_ID                    INTEGER                        not null,
   begin                          timestamp                     not null,
   end                            timestamp                     not null,
   primary key (id)
)
type = InnoDB;


/*==============================================================*/
/* Table: Town_Types                                            */
/*==============================================================*/
create table Town_Types
(
   id                             INTEGER                        not null,
   Name                           varchar(120)                   not null,
   LANG                           varchar(255)                         not null,
   primary key (id)
)
type = InnoDB;

alter table Act_Files add constraint FK_ActFiles_REF_Act foreign key (Act_ID)
      references Act (id) on delete restrict on update restrict;

alter table Acts add constraint FK_Act_REF_ApartmentRelation foreign key (Apartment_Relation_ID)
      references Apartment_Relation (id) on delete restrict on update restrict;

alter table Acts add constraint FK_ApartmentRelation_REF_Act foreign key (Act_ID)
      references Act (id) on delete restrict on update restrict;

alter table Apartment add constraint FK_Apartment_REF_Building foreign key (Building_ID)
      references Building (ID) on delete restrict on update restrict;

alter table Apartment_Attribute add constraint FK_ApartmentAttribute_REF_Apartment foreign key (Apartment_ID)
      references Apartment (ID) on delete restrict on update restrict;

alter table Apartment_Attribute add constraint FK_ApartmentAttribute_REF_ApartmentAttributeType foreign key (Apartment_Attribute_type_ID)
      references Apartment_Attribute_Type (ID) on delete restrict on update restrict;

alter table Apartment_Attribute_Type_Name add constraint FK_ApartmentAttributeName_REF_ApartmentAttributeType foreign key (Apartment_Attribute_Type_ID)
      references Apartment_Attribute_Type (ID) on delete restrict on update restrict;

alter table Apartment_Number add constraint FK_ApartmentNumber_REF_Apartment foreign key (Apartment_ID)
      references Apartment (ID) on delete restrict on update restrict;

alter table Apartment_Relation add constraint FK_APARTNMENT_REF_PERSON foreign key (Person_ID)
      references Person (id) on delete restrict on update restrict;

alter table Apartment_Relation add constraint FK_Apartment_REF_AType foreign key (Apartment_relation_type_ID)
      references Apartment_relation_type (id) on delete restrict on update restrict;

alter table Apartment_Relation add constraint FK_ApartmentRelation_REF_APARTMENT foreign key (Apartment_ID)
      references Apartment (ID) on delete restrict on update restrict;

alter table Building add constraint FK_Building_REF_District foreign key (District_ID)
      references District (ID) on delete restrict on update restrict;

alter table Building_Attribute add constraint FK_BuildingAttr_REF_BuildingAttrType foreign key (attribute_type_id)
      references Building_Attribute_Type (ID) on delete restrict on update restrict;

alter table Building_Attribute add constraint FK_BuildingAttr_REF_Buildings foreign key (Buildings_ID)
      references Buildings (id) on delete restrict on update restrict;

alter table Building_Attribute_Type_Lang add constraint FK_LangNameBuildingAttr_REF_BuildingAttrType foreign key (Building_Attribute_Type_ID)
      references Building_Attribute_Type (ID) on delete restrict on update restrict;

alter table Building_Status add constraint FK_BuildingStatus_REF_Building foreign key (Building_ID)
      references Building (ID) on delete restrict on update restrict;

alter table Buildings add constraint FK_Buildings_REF_Building foreign key (Building_ID)
      references Building (ID) on delete restrict on update restrict;

alter table Buildings add constraint FK_Buildings_REF_Street foreign key (Street_ID)
      references Street (ID) on delete restrict on update restrict;

alter table District add constraint FK_District_REF_Town foreign key (Town_ID)
      references Town (ID) on delete restrict on update restrict;

alter table District_Name add constraint FK_DistrictName_REF_Distrikt foreign key (District_ID)
      references District (ID) on delete restrict on update restrict;

alter table District_Name_Lang_Value add constraint FK_DistrictNameLangValue_REF_Object foreign key (DistrictName_ID)
      references District_Name (ID) on delete restrict on update restrict;

alter table History add constraint FK_History_REF_History foreign key (History_ID)
      references History (id) on delete restrict on update restrict;

alter table Identities add constraint FK_PIS_REF_PI foreign key (Idenity_ID)
      references Person_Identity (ID) on delete restrict on update restrict;

alter table Identities add constraint FK_PIS_REF_Person foreign key (Person_ID)
      references Person (id) on delete restrict on update restrict;

alter table Identity_Name add constraint FK_IdentityName_REF_IdentityType foreign key (IdentityType_ID)
      references Identity_Type (id) on delete restrict on update restrict;

alter table Person add constraint FK_Person_REF_Apartment foreign key (Apartment_ID)
      references Apartment (ID) on delete restrict on update restrict;

alter table PersonAct add constraint FK_Person_REF_Act foreign key (Act_ID)
      references Act (id) on delete restrict on update restrict;

alter table PersonAct add constraint FK_Act_REF_Person foreign key (Person_ID)
      references Person (id) on delete restrict on update restrict;

alter table Person_ATTRIBUTE add constraint FK_PAttr_REF_Person foreign key (Person_ID)
      references Person (id) on delete restrict on update restrict;

alter table Person_Identity add constraint FK_PersonIdentity_REF_IType foreign key (identity_type_id)
      references Identity_Type (id) on delete restrict on update restrict;

alter table Person_Identity_ATTRIBUTE add constraint FK_PIAttr_REF_PIdentity foreign key (Person_Identity_ID)
      references Person_Identity (ID) on delete restrict on update restrict;

alter table Region add constraint FK_Region_REF_Country foreign key (Country_ID)
      references Country (ID) on delete restrict on update restrict;

alter table Region_Name add constraint FK_RegionName_REF_Region foreign key (Region_ID)
      references Region (ID) on delete restrict on update restrict;

alter table Region_Name_Lang_Value add constraint FK_RegionNameLangValue_REF_Object foreign key (RegionName_ID)
      references Region_Name (ID) on delete restrict on update restrict;

alter table Room_Attribute add constraint FK_RoomAttribute_REF_AttrType foreign key (Room_Attribute_type_ID)
      references Room_Attribute_Type (ID) on delete restrict on update restrict;

alter table Room_Attribute add constraint FK_RoomAttribute_REF_Room foreign key (Room_ID)
      references Room (id) on delete restrict on update restrict;

alter table Room_Attribute_Type_Name add constraint FK_RoomAttributeName_REF_RoomAttributeType foreign key (Room_Attribute_Type_ID)
      references Room_Attribute_Type (ID) on delete restrict on update restrict;

alter table Rooms add constraint FK_Apartment_REF_Room foreign key (id)
      references Room (id) on delete restrict on update restrict;

alter table Rooms add constraint FK_Room_REF_Apartment foreign key (Apartment_ID)
      references Apartment (ID) on delete restrict on update restrict;

alter table Street add constraint FK_Street_REF_District foreign key (District_ID)
      references District (ID) on delete restrict on update restrict;

alter table Street add constraint FK_Street_REF_StreetType foreign key (Street_Type_ID)
      references Street_Type (ID) on delete restrict on update restrict;

alter table Street add constraint FK_Street_REF_Town foreign key (Town_ID)
      references Town (ID) on delete restrict on update restrict;

alter table Street_Name add constraint FK_StreetName_REF_Street foreign key (Street_ID)
      references Street (ID) on delete restrict on update restrict;

alter table Street_Name_Lang_Value add constraint FK_StreetNameLangValue_REF_Object foreign key (StreetName_ID)
      references Street_Name (ID) on delete restrict on update restrict;

alter table Street_Type add constraint FK_StreetType_REF_StreetTypes foreign key (Street_Type_ID)
      references Street_Types (id) on delete restrict on update restrict;

alter table Town add constraint FK_TOWN_REF_REGION foreign key (Region_ID)
      references Region (ID) on delete restrict on update restrict;

alter table Town add constraint FK_Town_REF_TownType foreign key (Town_Type_id)
      references Town_Type (id) on delete restrict on update restrict;

alter table Town_Name add constraint FK_TownName_REF_TOWN foreign key (ID)
      references Town (ID) on delete restrict on update restrict;

alter table Town_Name_Lang_Value add constraint FK_TownNameLangValue_REF_Object foreign key (TownName_ID)
      references Town_Name (ID) on delete restrict on update restrict;

alter table Town_Type add constraint FK_TownType_REF_TownTypes foreign key (id)
      references Town_Types (id) on delete restrict on update restrict;
