package org.multimodule.api.auth.event;

import org.multimodule.common.entity.User;
import org.springframework.context.ApplicationEvent;

public class OnUserAccountChangeEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private User user;
    private String action;
    private String actionStatus;

    public OnUserAccountChangeEvent(User user, String action, String actionStatus) {
        super(user);
        this.user = user;
        this.action = action;
        this.actionStatus = actionStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }
}
