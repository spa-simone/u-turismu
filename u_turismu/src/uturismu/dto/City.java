/*
 * This file is part of "U Turismu" project. 
 * 
 * U Turismu is an enterprise application in support of calabrian tour operators.
 * This system aims to promote tourist services provided by the operators
 * and to develop and improve tourism in Calabria.
 *
 * Copyright (C) 2012 "LagrecaSpaccarotella" team.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uturismu.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.ForeignKey;

/**
 * @author "LagrecaSpaccarotella" team.
 * 
 */
@Entity(name = "CITY")
public class City implements Serializable {

	private static final long serialVersionUID = -5177619158328836473L;
	private Long id;
	private String name;
	private String province;
	private String description;
	private Set<CityTag> cityTags;

	public City() {
		cityTags = new HashSet<CityTag>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getProvince() {
		return province;
	}

	public String getDescription() {
		return description;
	}

	@ManyToMany
	@JoinTable(name = "CITY_CLASSIFICATION", joinColumns = @JoinColumn(name = "id_city"), inverseJoinColumns = @JoinColumn(name = "id_city_tag"))
	@ForeignKey(name = "FK_CITYCLASSIFICATION_CITY", inverseName = "FK_CITYCLASSIFICATION_CITYTAG")
	public Set<CityTag> getCityTags() {
		return Collections.unmodifiableSet(cityTags);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	protected void setCityTags(Set<CityTag> cityTags) {
		this.cityTags = cityTags;
	}

	public boolean addCityTag(CityTag cityTag) {
		return this.cityTags.add(cityTag);
	}

	public boolean removeCityTag(CityTag cityTag) {
		return this.cityTags.remove(cityTag);
	}

}