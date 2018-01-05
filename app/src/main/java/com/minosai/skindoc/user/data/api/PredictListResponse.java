package com.minosai.skindoc.user.data.api;

import java.util.List;

/**
 * Created by minos.ai on 05/01/18.
 */

public class PredictListResponse {
    List<PredictResponse> response;

    public List<PredictResponse> getResponse() {
        return response;
    }

    public void setResponse(List<PredictResponse> response) {
        this.response = response;
    }
}
