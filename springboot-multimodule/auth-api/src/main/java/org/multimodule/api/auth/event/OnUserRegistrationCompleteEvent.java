package org.multimodule.api.auth.event;

import org.multimodule.common.entity.User;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

public class OnUserRegistrationCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private transient UriComponentsBuilder redirectUrl;
    private User user;

    public OnUserRegistrationCompleteEvent(User user, UriComponentsBuilder redirectUrl) {
        super(user);
        this.user = user;
        this.redirectUrl = redirectUrl;
    }

    public UriComponentsBuilder getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(UriComponentsBuilder redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
