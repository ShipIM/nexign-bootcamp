package com.example.nexign.converter;

import com.example.nexign.api.converter.Converter;
import com.example.nexign.model.CustomerSummary;
import com.example.nexign.presenter.CustomerSummaryPresenter;
import com.example.nexign.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SummaryPresenterConverter implements Converter<CustomerSummaryPresenter, CustomerSummary> {

    private final TimeUtils timeUtils;

    @Override
    public CustomerSummaryPresenter convertTo(CustomerSummary element) {
        return new CustomerSummaryPresenter(
                element.getMsisdn(),
                new CustomerSummaryPresenter.IncomingCall(timeUtils.formatSeconds(element.getIncoming())),
                new CustomerSummaryPresenter.OutcomingCall(timeUtils.formatSeconds(element.getOutcoming()))
        );
    }

    @Override
    public CustomerSummary convertFrom(CustomerSummaryPresenter element) {
        return new CustomerSummary(
                element.getMsisdn(),
                timeUtils.parseTime(element.getIncoming().getTotalTime()),
                timeUtils.parseTime(element.getOutcoming().getTotalTime())
        );
    }

}
