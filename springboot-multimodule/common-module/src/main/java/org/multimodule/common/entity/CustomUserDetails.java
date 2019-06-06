package org.multimodule.common.entity;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails extends User implements UserDetails {

    public CustomUserDetails(final User user) {
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
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
    public boolean isAccountNonLocked() {
        return super.getActive();
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return super.getEmailVerified();
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomUserDetails [getAuthorities()=");
		builder.append(getAuthorities());
		builder.append(", getPassword()=");
		builder.append(getPassword());
		builder.append(", getUsername()=");
		builder.append(getUsername());
		builder.append(", isAccountNonExpired()=");
		builder.append(isAccountNonExpired());
		builder.append(", isAccountNonLocked()=");
		builder.append(isAccountNonLocked());
		builder.append(", isCredentialsNonExpired()=");
		builder.append(isCredentialsNonExpired());
		builder.append(", isEnabled()=");
		builder.append(isEnabled());
		builder.append(", hashCode()=");
		builder.append(hashCode());
		builder.append(", getId()=");
		builder.append(getId());
		builder.append(", getFirstName()=");
		builder.append(getFirstName());
		builder.append(", getLastName()=");
		builder.append(getLastName());
		builder.append(", getEmail()=");
		builder.append(getEmail());
		builder.append(", getActive()=");
		builder.append(getActive());
		builder.append(", getRoles()=");
		builder.append(getRoles());
		builder.append(", getEmailVerified()=");
		builder.append(getEmailVerified());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append(", getCreatedAt()=");
		builder.append(getCreatedAt());
		builder.append(", getUpdatedAt()=");
		builder.append(getUpdatedAt());
		builder.append(", getClass()=");
		builder.append(getClass());
		builder.append("]");
		return builder.toString();
	}
    
}