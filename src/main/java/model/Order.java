package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * {
 *   "id": 0,
 *   "petId": 0,
 *   "quantity": 0,
 *   "shipDate": "2025-03-08T17:20:08.358Z",
 *   "status": "placed",
 *   "complete": true
 * }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order extends BaseModel implements IModel {
    @JsonProperty("id")
    private Number id;
    @JsonProperty("petId")
    private Number petId;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("shipDate")
    private String shipDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("complete")
    private boolean complete;

    private Order(Order.Builder builder) {
        this.id = builder.id;
        this.petId = builder.petId;
        this.quantity = builder.quantity;
        this.shipDate = dateToString(builder.shipDate);
        this.status = builder.status;
        this.complete = builder.complete;
    }

    private String dateToString(Date date) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return zonedDateTime.format(formatter);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", petId=" + petId +
                ", quantity=" + quantity +
                ", shipDate=" + shipDate +
                ", status='" + status + '\'' +
                ", complete=" + complete +
                '}';
    }

    public static class Builder {
        private Number id;
        private Number petId;
        private int quantity;
        private Date shipDate;
        private String status;
        private boolean complete;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setPedId(Number petId) {
            this.petId = petId;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setShipDate() {
            return setShipDate(new Date());
        }

        public Builder setShipDate(Date shipDate) {
            this.shipDate = shipDate;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setComplete(boolean complete) {
            this.complete = complete;
            return this;
        }

        public Order build() {
            return new Order(this);
        }

    }

    public Number getId() {
        return id;
    }

    public Number getPetId() {
        return petId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getShipDate() {
        return shipDate;
    }

    public String getStatus() {
        return status;
    }

    public boolean getComplete() {
        return complete;
    }

    public void updateId(Object id) {
        this.id = (Number)id;
    }
}
