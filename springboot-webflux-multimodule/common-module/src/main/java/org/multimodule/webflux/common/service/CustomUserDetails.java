package org.multimodule.webflux.common.service;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.multimodule.webflux.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails extends User implements UserDetails{
	
	  public CustomUserDetails(final User user) {
	        super(user);
	    }

	  @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return getRoles().stream()
	                .map(role -> new SimpleGrantedAuthority(role.getName()))
	                .collect(Collectors.toList());
	    }

	    @Override
	    public String getPassword() {
	        return super.getPassword();
	    }

	    @Override
	    public String getUsername() {
	        return super.getEmail();
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(getId());
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (obj == this) {
	            return true;
	        }
	        if (obj == null || getClass() != obj.getClass()) {
	            return false;
	        }
	        CustomUserDetails that = (CustomUserDetails) obj;
	        return Objects.equals(getId(), that.getId());
	    }

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

}
