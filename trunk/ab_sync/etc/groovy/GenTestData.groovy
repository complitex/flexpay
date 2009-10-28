import au.com.bytecode.opencsv.CSVReader
import java.text.SimpleDateFormat

def srcFile = new File('/windows/F/tmp/absync3.csv')
def dstFile = new File('/windows/F/tmp/1.sql')

static enum FieldType {

	District(0), StreetType(1), StreetName(2), HouseNumber(3), Bulk(4), Apartment(5),
	DistrictId(6), StreetId(7), BuildingId(8), StreetTypeId(9),
	FirstName(10), MiddleName(11), LastName(12), INN(13), ResidenceApartmentId(14), OwnedApartmentId(15)

	int id;

	FieldType(int id) {
		this.id = id
	}

	public static FieldType getById(int id) {

		switch (id) {
			case 0: return District
			case 1: return StreetType
			case 2: return StreetName
			case 3: return HouseNumber
			case 4: return Bulk
			case 5: return Apartment

			case 6: return DistrictId
			case 7: return StreetId
			case 8: return BuildingId
			case 9: return StreetTypeId

			case 10: return FirstName
			case 11: return MiddleName
			case 12: return LastName
			case 13: return INN
			case 14: return ResidenceApartmentId
			case 15: return OwnedApartmentId
		}

		throw new IllegalArgumentException("Invalid field type code: " + id);
	}
}

static enum ObjType {

	Town(0, 0), District(1, 1), Street(2, 3), Building(3, 4), StreetType(4, 2), Apartment(5, 5), Person(6, 6)

	int id
	int orderWeight

	ObjType(int id, int orderWeight) {
		this.id = id
		this.orderWeight = orderWeight
	}

	static ObjType getById(int id) {

		switch (id) {
			case 0: return Town
			case 1: return District
			case 2: return Street
			case 3: return Building
			case 4: return StreetType
			case 5: return Apartment
			case 6: return Person
		}

		throw new IllegalArgumentException("Invalid ObjType id: " + id)
	}
}

static enum SyncAction {

	Unknown(-1), Create(0), Change(1), Delete(2)

	int code;

	private SyncAction(int code) {
		this.code = code;
	}

	public static SyncAction getByCode(int code) {
		if (code == Create.code) {
			return Create;
		}
		if (code == Change.code) {
			return Change;
		}
		if (code == Delete.code) {
			return Delete;
		}

		throw new IllegalArgumentException('Unsupported sync operation type code: ' + code);
	}
}

static class HistoryRec {

	Long recordId
	Date recordDate
	String oldValue
	String currentValue
	Long objectId
	FieldType fieldType
	ObjType objectType
	SyncAction syncAction
	int processed

	String[] csvLine


	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("HistoryRec");
		sb.append("{recordId=").append(recordId);
		sb.append(", recordDate=").append(recordDate);
		sb.append(", oldValue='").append(oldValue).append('\'');
		sb.append(", currentValue='").append(currentValue).append('\'');
		sb.append(", objectId=").append(objectId);
		sb.append(", fieldType=").append(fieldType);
		sb.append(", objectType=").append(objectType);
		sb.append(", syncAction=").append(syncAction);
		sb.append('}');
		return sb.toString();
	}
}

static class RecordParser {

	private final SimpleDateFormat DF = new SimpleDateFormat('d/M/y H:m:s')

	CSVReader csvReader
	String[] nextLine
	int maxLine
	LineNumberReader reader

	def debugFilter = {HistoryRec rec, RecordParser parser -> false }

	RecordParser(LineNumberReader r, int line = 0, int maxLine = -1) {
		csvReader = new CSVReader(r, ';' as char, '"' as char, line)
		this.maxLine = maxLine
		this.reader = r
	}

	boolean hasNext() {
		if (nextLine != null)
			return true;
		if (maxLine >= 0 && reader.lineNumber >= maxLine) {
			println 'End by maxLine'
			return false;
		}
		nextLine = csvReader.readNext()
		return nextLine != null
	}

	HistoryRec parse() {
		if (!hasNext()) {
			throw new IllegalStateException("No more lines")
		}

		try {
			def n = 0
			HistoryRec rec = new HistoryRec()
			rec.csvLine = nextLine
			rec.recordId = Long.valueOf(nextLine[n++])
			rec.recordDate = DF.parse(nextLine[n++])
			rec.oldValue = nextLine[n++]
			rec.currentValue = nextLine[n++]
			rec.objectType = ObjType.getById(Integer.valueOf(nextLine[n++]))
			rec.objectId = Long.valueOf(nextLine[n++])
			rec.fieldType = nextLine[n] ? FieldType.getById(Integer.valueOf(nextLine[n])) : null
			++n
			rec.syncAction = SyncAction.getByCode(Integer.valueOf(nextLine[n]))

			if (debugFilter(rec, this)) {
				println "Line #${reader.lineNumber}: $nextLine"
			}

			return rec
		} catch (Throwable ex) {
			ex.printStackTrace()
			throw ex
		} finally {
			nextLine = null
		}
	}
}

static class TypeRecords {

	ObjType type
	Map<Long, List<HistoryRec>> recs = [:]

	TypeRecords(ObjType type) {
		this.type = type
	}

	void addRecord(HistoryRec rec) {
		List<HistoryRec> records = recs.get(rec.getObjectId())
		if (!records) {
			records = []
		}
		records.add rec
		recs.put(rec.getObjectId(), records)
	}
}

def syncData = [
		(ObjType.Town): new TypeRecords(ObjType.Town),
		(ObjType.District): new TypeRecords(ObjType.District),
		(ObjType.StreetType): new TypeRecords(ObjType.StreetType),
		(ObjType.Street): new TypeRecords(ObjType.Street),
		(ObjType.Building): new TypeRecords(ObjType.Building),
		(ObjType.Apartment): new TypeRecords(ObjType.Apartment),
		(ObjType.Person): new TypeRecords(ObjType.Person),
]

def syncStats = [
		(ObjType.Town): 0,
		(ObjType.District): 0,
		(ObjType.StreetType): 0,
		(ObjType.Street): 0,
		(ObjType.Building): 0,
		(ObjType.Apartment): 0,
		(ObjType.Person): 0,
]

def requiredApartments = [] as Set<Long>

// initialize persons syncData
srcFile.withReader('cp1251') {BufferedReader r ->
	RecordParser parser = new RecordParser(new LineNumberReader(r), 846880)
	int n = 0;
	TypeRecords personsRecords = syncData[ObjType.Person]
	Long lastObjectId = null
	while (syncStats[ObjType.Person] < 11 && parser.hasNext()) {
		HistoryRec rec = parser.parse()
		if (rec.objectType == ObjType.Person) {
			lastObjectId = rec.objectId
			personsRecords.addRecord rec
			syncStats[ObjType.Person] = personsRecords.recs.size()
			++n

			// append references to apartments to required apartments
			if (rec.fieldType in [FieldType.ResidenceApartmentId, FieldType.OwnedApartmentId]) {
				if (rec.oldValue) {
					requiredApartments << Long.valueOf(rec.oldValue)
				}
				if (rec.currentValue) {
					requiredApartments << Long.valueOf(rec.currentValue)
				}
			}
		}
		if (n % 100 == 0) {
			println "Total persons lines: $n"
		}
	}

	// remove 1001-th object
	personsRecords.recs.remove(lastObjectId)
	syncStats[ObjType.Person] = personsRecords.recs.size()
}

//println "Free mem: ${Runtime.getRuntime().freeMemory()} of ${Runtime.getRuntime().totalMemory()}"

def requiredBuildings = [] as Set<Long>

// initialize apartments syncData
srcFile.withReader('cp1251') {BufferedReader r ->
	RecordParser parser = new RecordParser(new LineNumberReader(r), 36560, 846880)
	int n = 0;
	TypeRecords apartmentsRecords = syncData[ObjType.Apartment]
	Long lastObjectId = null
	Long lastRemovedId = null
	while ((syncStats[ObjType.Apartment] < 31 || !requiredApartments.isEmpty() || lastObjectId == lastRemovedId) && parser.hasNext()) {
		HistoryRec rec = parser.parse()
		if (rec.objectType == ObjType.Apartment) {
			lastObjectId = rec.objectId
			if (requiredApartments.remove(lastObjectId)) {
				lastRemovedId = lastObjectId
			}
			if (apartmentsRecords.recs.size() < 31 || lastObjectId == lastRemovedId) {
				apartmentsRecords.addRecord rec
				syncStats[ObjType.Apartment] = apartmentsRecords.recs.size()
			}
			++n

			// append references to apartments to required apartments
			if (rec.fieldType in [FieldType.BuildingId]) {
				if (rec.oldValue) {
					requiredBuildings << Long.valueOf(rec.oldValue)
				}
				if (rec.currentValue) {
					requiredBuildings << Long.valueOf(rec.currentValue)
				}
			}
		}
		if (n % 100 == 0) {
			println "Total apartments lines: $n"
		}
	}

	// remove 1001-th object
	apartmentsRecords.recs.remove(lastObjectId)
	syncStats[ObjType.Apartment] = apartmentsRecords.recs.size()
}

println "Stats: $syncStats"

if (!requiredApartments.isEmpty()) {
	throw new IllegalArgumentException("Not found apartments history: " + requiredApartments)
}

def requiredStreets = [] as Set<Long>

// initialize buildings syncData
srcFile.withReader('cp1251') {BufferedReader r ->
	RecordParser parser = new RecordParser(new LineNumberReader(r), 6000, 36560)
	int n = 0;
	TypeRecords buildingsRecords = syncData[ObjType.Building]
	Long lastObjectId = null
	Long lastRemovedId = null
	while ((syncStats[ObjType.Building] < 21 || !requiredBuildings.isEmpty() || lastObjectId == lastRemovedId) && parser.hasNext()) {
		HistoryRec rec = parser.parse()
		if (rec.objectType == ObjType.Building) {
			lastObjectId = rec.objectId
			if (requiredBuildings.remove(lastObjectId)) {
				lastRemovedId = lastObjectId
			}
			if (buildingsRecords.recs.size() < 21 || lastObjectId == lastRemovedId || buildingsRecords.recs.containsKey(lastObjectId)) {
				buildingsRecords.addRecord rec
				syncStats[ObjType.Building] = buildingsRecords.recs.size()
			}
			++n

			// append references to apartments to required apartments
			if (rec.fieldType in [FieldType.StreetId]) {
				if (rec.oldValue) {
					requiredStreets << Long.valueOf(rec.oldValue)
				}
				if (rec.currentValue) {
					requiredStreets << Long.valueOf(rec.currentValue)
				}
			}
		}
	}

	// remove 1001-th object
	buildingsRecords.recs.remove(lastObjectId)
	syncStats[ObjType.Building] = buildingsRecords.recs.size()
}

if (!requiredBuildings.isEmpty()) {
	throw new IllegalArgumentException("Not found buildings history: " + requiredBuildings)
}
requiredBuildings = null

// initialize streets syncData
srcFile.withReader('cp1251') {BufferedReader r ->
	RecordParser parser = new RecordParser(new LineNumberReader(r), 46, 7000)
	int n = 0;
	TypeRecords streetsRecords = syncData[ObjType.Street]
	Long lastObjectId = null
	Long lastRemovedId = null
	while (parser.hasNext()) {
		HistoryRec rec = parser.parse()
		if (rec.objectType == ObjType.Street) {
			lastObjectId = rec.objectId
			if (requiredStreets.remove(lastObjectId)) {
				lastRemovedId = lastObjectId
			}
			if (streetsRecords.recs.size() < 21 || lastObjectId == lastRemovedId || streetsRecords.recs.containsKey(lastObjectId)) {
				streetsRecords.addRecord rec
				syncStats[ObjType.Street] = streetsRecords.recs.size()
			}
			++n
		}
	}

	// remove 1001-th object
	streetsRecords.recs.remove(lastObjectId)
	syncStats[ObjType.Building] = streetsRecords.recs.size()
}

if (!requiredStreets.isEmpty()) {
	throw new IllegalArgumentException("Not found streets history: " + requiredStreets)
}

println "Stats: $syncStats"

// initialize Town, District, StreetType syncData
srcFile.withReader('cp1251') {BufferedReader r ->
	RecordParser parser = new RecordParser(new LineNumberReader(r), 0, 1000)
//	parser.debugFilter = {HistoryRec rec, RecordParser p -> true }
	while (parser.hasNext()) {
		HistoryRec rec = parser.parse()
		if (rec.objectType in [ObjType.Town, ObjType.District, ObjType.StreetType]) {
			syncData[rec.objectType].addRecord rec
			syncStats[rec.objectType] = syncData[rec.objectType].recs.size()
		}
	}
}

println "Stats: $syncStats"

//syncData[ObjType.Person].recs.each {Long id, List<HistoryRec> records ->
//	records.each {HistoryRec rec ->
//		println rec.csvLine
//	}
//}

dstFile.withWriter {BufferedWriter w ->
	SimpleDateFormat df = new SimpleDateFormat('yyyy/MM/dd hh:mm:ss')
	w.write '''insert into ab_sync_changes_tbl (record_id, record_date, old_value, current_value, object_type, object_id, field, action_type, processed, order_weight) values
'''
	boolean hasData = false
	syncData.each {ObjType type, TypeRecords typeRecords ->
		typeRecords.recs.each {Long objectId, List<HistoryRec> records ->
			records.each {HistoryRec rec ->
				w.write """	${hasData ? ',' : ''}(${rec.recordId}, '${df.format(rec.recordDate)}', '${rec.oldValue}', '${rec.currentValue}', ${type.id}, ${rec.objectId}, ${rec.fieldType ? rec.fieldType.id: null}, ${rec.syncAction.code}, 0, ${type.orderWeight})
"""
			}
			hasData = true
		}
	}
}
