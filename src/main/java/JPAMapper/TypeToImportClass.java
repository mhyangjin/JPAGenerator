package JPAMapper;

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
	GenericDAOImpl("import com.udmtek.DBCore.DAOModel.GenericDAOImpl"),
	DBCoreEntity("import com.udmtek.DBCore.DAOModel.DBCoreEntity"),
	DBCoreEntityImpl("import com.udmtek.DBCore.DAOModel.DBCoreEntityImpl"),
	DBCoreEntityKeyImpl("import com.udmtek.DBCore.DAOModel.DBCoreEntityKeyImpl"),
	DBCoreDTO("import com.udmtek.DBCore.DAOModel.DBCoreDTO"),
	DBCoreDTOImpl("import com.udmtek.DBCore.DAOModel.DBCoreDTOImpl"),
	DBCoreDTOMapperImpl("import com.udmtek.DBCore.DAOModel.DBCoreDTOMapperImpl;"),
	;
	
	private String importClass;
	TypeToImportClass(String importClass) {
		this.importClass =  importClass;
	}

	public String getImport() {
		return this.importClass;
	}
}
