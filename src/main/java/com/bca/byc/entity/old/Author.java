package com.bca.byc.entity.old;

import java.time.LocalDate;
import java.util.List;

import com.bca.byc.entity.AbstractBaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "author")
//@DynamicUpdate
@SQLDelete(sql = "UPDATE author SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
public class Author extends AbstractBaseEntity {
	
	//postgre-> bigserial
	//mysql->autoincrement
	//strategy -> identity -> cons: batch insert disabled
	//batch insert -> stored producured
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4415917570527208430L;

	//strategy sequence -> pros: enable batch insert
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_generator")
	@SequenceGenerator(name = "author_generator", sequenceName = "author_id_seq")
	private Long id;
	
	
	@Column(name = "author_name", nullable = false, columnDefinition = "varchar(300)")
	private String name;
	
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<Address> addresses;
	

}