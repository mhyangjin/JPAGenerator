package kr.co.codeJ.JPAGenerator.JPAMapper;

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
	GenericDAO("import kr.co.codeJ.JPA.DAOModel.GenericDAO"),
	GenericDAOImpl("import kr.co.codeJ.JPA.DAOModel.GenericDAOImpl"),
	GenericEntity("import kr.co.codeJ.JPA.DAOModel.GenericEntity"),
	GenericEntityImpl("import kr.co.codeJ.JPA.DAOModel.GenericEntityImpl"),
	GenericEntityKeyImpl("import kr.co.codeJ.JPA.DAOModel.GenericEntityKeyImpl"),
	GenericEntityKey("import kr.co.codeJ.JPA.DAOModel.GenericEntityKey"),
	GenericDTO("import kr.co.codeJ.JPA.DAOModel.GenericDTO"),
	GenericDTOImpl("import kr.co.codeJ.JPA.DAOModel.GenericDTOImpl"),
	GenericDTOMapper("import kr.co.codeJ.JPA.DAOModel.GenericDTOMapper"),
	GenericDTOMapperImpl("import kr.co.codeJ.JPA.DAOModel.GenericDTOMapperImpl"),
	ManyToOne("import javax.persistence.ManyToOne"),
	JoinColumn("import javax.persistence.JoinColumn"),
	GeneratedValue("import javax.persistence.GeneratedValue"),
	GenerationType("import javax.persistence.GenerationType"),
	SequenceGenerator("import javax.persistence.SequenceGenerator"),
	;
	
	private String importClass;
	TypeToImportClass(String importClass) {
		this.importClass =  importClass;
	}

	public String getImport() {
		return this.importClass;
	}
}
