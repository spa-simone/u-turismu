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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

import uturismu.dto.enumtype.ReservationType;

/**
 * @author "LagrecaSpaccarotella" team.
 * 
 */
@Entity(name = "RESERVATION")
public class Reservation extends Service {

	private static final long serialVersionUID = -1401386687925117984L;
	private ReservationType type;
	private Date reservationTimestamp;
	private POI pointOfInterest;

	public Reservation() {
	}

	@Enumerated(EnumType.STRING)
	public ReservationType getType() {
		return type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "reservation_timestamp")
	public Date getReservationTimestamp() {
		return reservationTimestamp;
	}

	@ManyToOne
	@JoinColumn(name = "id_poi")
	@ForeignKey(name = "FK_RESERVATION_POI")
	public POI getPointOfInterest() {
		return pointOfInterest;
	}

	public void setType(ReservationType type) {
		this.type = type;
	}
	
	public void setReservationTimestamp(Date reservationTimestamp) {
		this.reservationTimestamp = reservationTimestamp;
	}

	public void setPointOfInterest(POI pointOfInterest) {
		this.pointOfInterest = pointOfInterest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pointOfInterest == null) ? 0 : pointOfInterest.hashCode());
		result = prime * result
				+ ((reservationTimestamp == null) ? 0 : reservationTimestamp.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (!super.equals(obj))
			return false;
		Reservation other = (Reservation) obj;
		if (pointOfInterest == null) {
			if (other.pointOfInterest != null)
				return false;
		} else if (!pointOfInterest.equals(other.pointOfInterest))
			return false;
		if (reservationTimestamp == null) {
			if (other.reservationTimestamp != null)
				return false;
		} else if (!reservationTimestamp.equals(other.reservationTimestamp))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
