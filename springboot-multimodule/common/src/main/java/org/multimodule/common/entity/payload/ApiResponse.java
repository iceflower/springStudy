package org.multimodule.common.entity.payload;

public class ApiResponse {

    private Object data;
    private Boolean success;

    public ApiResponse() {
    }

    public ApiResponse(Object data, Boolean success) {
        this.data = data;
        this.success = success;
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

}
