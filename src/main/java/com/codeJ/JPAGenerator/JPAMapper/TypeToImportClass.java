package com.codeJ.JPAGenerator.JPAMapper;

import javax.persistence.ManyToOne;

public enum TypeToImportClass {
	Date("import java.util.Date"),
	Column("import javax.persistence.Column"),
	Entity("import javax.persistence.Entity"),
	Id("import javax.persistence.Id"),
	IdClass("import javax.persistence.IdClass"),
	Table("import javax.persistence.Table"),
	Transient("import javax.persistence.Transient"),
	Temporal("import javax.persistence.Temporal"),
	TemporalType("import javax.persistence.TemporalType"),
	ToString("import lombok.ToString"),
	Autowired("import org.springframework.beans.factory.annotation.Autowired"),
	Repository("import org.springframework.stereotype.Repository" ),
	Scope("import org.springframework.context.annotation.Scope"),
	Component("import org.springframework.stereotype.Component"),
	Setter("import lombok.Setter"),	
	Getter("import lombok.Getter"),
	GenericDAO("import com.codeJ.JPA.DAOModel.GenericDAO"),
	GenericDAOImpl("import com.codeJ.JPA.DAOModel.GenericDAOImpl"),
	GenericEntity("import com.codeJ.JPA.DAOModel.GenericEntity"),
	GenericEntityImpl("import com.codeJ.JPA.DAOModel.GenericEntityImpl"),
	GenericEntityKeyImpl("import com.codeJ.JPA.DAOModel.GenericEntityKeyImpl"),
	GenericEntityKey("import com.codeJ.JPA.DAOModel.GenericEntityKey"),
	GenericDTO("import com.codeJ.JPA.DAOModel.GenericDTO"),
	GenericDTOImpl("import com.codeJ.JPA.DAOModel.DGenericDTOImpl"),
	GenericDTOMapper("import com.codeJ.JPA.DAOModel.GenericDTOMapper"),
	GenericDTOMapperImpl("import com.codeJ.JPA.DAOModel.GenericDTOMapperImpl"),
	ManyToOne("import javax.persistence.ManyToOne"),
	JoinColumn("import javax.persistence.JoinColumn"),
	;
	
	private String importClass;
	TypeToImportClass(String importClass) {
		this.importClass =  importClass;
	}

	public String getImport() {
		return this.importClass;
	}
}
