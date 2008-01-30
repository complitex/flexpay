package org.flexpay.bti.persistence;
// Generated 15.11.2007 14:59:52 by Hibernate Tools 3.2.0.b11


import javax.persistence.*;

/**
 * ActFiles generated by hbm2java
 */
@Entity
@Table (name = "act_files"
		, catalog = "flexpay_db"
)
public class ActFiles implements java.io.Serializable {


	private ActFilesId id;
	private Act act;

	public ActFiles() {
	}

	public ActFiles(ActFilesId id, Act act) {
		this.id = id;
		this.act = act;
	}

	@EmbeddedId

	@AttributeOverrides ({
	@AttributeOverride (name = "id", column = @Column (name = "ID", nullable = false)),
	@AttributeOverride (name = "actId", column = @Column (name = "Act_ID", nullable = false)),
	@AttributeOverride (name = "fileName", column = @Column (name = "FileName", nullable = false, length = 4000)),
	@AttributeOverride (name = "fileOrder", column = @Column (name = "file_order", nullable = false))})
	public ActFilesId getId() {
		return this.id;
	}

	public void setId(ActFilesId id) {
		this.id = id;
	}

	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "Act_ID", nullable = false, insertable = false, updatable = false)
	public Act getAct() {
		return this.act;
	}

	public void setAct(Act act) {
		this.act = act;
	}

}


