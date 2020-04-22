package integration.com.banking;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "beneficiaryAccountId", "amount", "payment" })
public class PaymentTransferOrderClientResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("beneficiaryAccountId")
    private String beneficiaryAccountId;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("payment")
    private PaymentResponse payment;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("beneficiaryAccountId")
    public String getBeneficiaryAccountId() {
        return beneficiaryAccountId;
    }

    @JsonProperty("beneficiaryAccountId")
    public void setBeneficiaryAccountId(String beneficiaryAccountId) {
        this.beneficiaryAccountId = beneficiaryAccountId;
    }

    @JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @JsonProperty("payment")
    public PaymentResponse getPayment() {
        return payment;
    }

    @JsonProperty("payment")
    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
