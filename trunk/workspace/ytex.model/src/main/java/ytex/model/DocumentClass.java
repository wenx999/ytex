package ytex.model;

import java.io.Serializable;

public class DocumentClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int documentClassID;
	private int id;
	private String task;
	private String classAuto;
	private String classGold;
	private Document document;
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public int getDocumentClassID() {
		return documentClassID;
	}
	public void setDocumentClassID(int documentClassID) {
		this.documentClassID = documentClassID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getClassAuto() {
		return classAuto;
	}
	public void setClassAuto(String classAuto) {
		this.classAuto = classAuto;
	}
	public String getClassGold() {
		return classGold;
	}
	public void setClassGold(String classGold) {
		this.classGold = classGold;
	}
	public DocumentClass() {
		super();
	}
}
