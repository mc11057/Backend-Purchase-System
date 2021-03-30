package com.ues.Purchases.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserRoleKey implements Serializable{

	private static final long serialVersionUID = -6342105763814218549L;

		@Column(name = "user_id")
		Long userId;
		
		@Column (name = "role_id")
		Long roleId;

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Long getRoleId() {
			return roleId;
		}

		public void setRoleId(Long roleId) {
			this.roleId = roleId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
			result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
			UserRoleKey other = (UserRoleKey) obj;
			if (roleId == null) {
				if (other.roleId != null)
					return false;
			} else if (!roleId.equals(other.roleId))
				return false;
			if (userId == null) {
				if (other.userId != null)
					return false;
			} else if (!userId.equals(other.userId))
				return false;
			return true;
		}


		
		
}
