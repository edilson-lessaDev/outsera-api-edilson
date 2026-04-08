package br.com.outsera.api.dto;
import java.util.List;

public class ProducerIntervalResponse {

    private List<ProducerIntervalDto> min;
    private List<ProducerIntervalDto> max;

    public ProducerIntervalResponse() {
    }

    public ProducerIntervalResponse(List<ProducerIntervalDto> min, List<ProducerIntervalDto> max) {
        this.min = min;
        this.max = max;
    }

    public List<ProducerIntervalDto> getMin() {
        return min;
    }

    public List<ProducerIntervalDto> getMax() {
        return max;
    }

    public void setMin(List<ProducerIntervalDto> min) {
        this.min = min;
    }

    public void setMax(List<ProducerIntervalDto> max) {
        this.max = max;
    }
}