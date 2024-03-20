package com.example.nexign.presenter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerSummaryPresenter {

    private Integer msisdn;

    private IncomingCall incoming;

    private OutcomingCall outcoming;

    @Getter
    @AllArgsConstructor
    public static class IncomingCall {
        private String totalTime;
    }

    @Getter
    @AllArgsConstructor
    public static class OutcomingCall {
        private String totalTime;
    }

}
