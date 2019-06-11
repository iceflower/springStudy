package org.multimodule.webflux.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROLES")
public enum Role {
	ROLE_USER, ROLE_ADMIN;
	
	@Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id = null;

    @Column(name = "NAME")
    private String name;

    private Role() {
    	 this.name = toString();
    }
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
