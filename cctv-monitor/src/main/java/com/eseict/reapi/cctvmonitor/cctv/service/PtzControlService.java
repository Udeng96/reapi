package com.eseict.reapi.cctvmonitor.cctv.service;

import com.eseict.reapi.cctvmonitor.cctv.client.XeusPtzApiClient;
import com.eseict.reapi.cctvmonitor.cctv.dto.PtzRequest;
import org.springframework.stereotype.Service;

@Service
public class PtzControlService {

    private final XeusPtzApiClient xeusPtzApiClient;

    public PtzControlService(XeusPtzApiClient xeusPtzApiClient) {
        this.xeusPtzApiClient = xeusPtzApiClient;
    }

    public String controlPtz(PtzRequest request) {
        return xeusPtzApiClient.controlPtz(request);
    }
}
