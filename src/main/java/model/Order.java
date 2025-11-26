package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
@Getter
@Setter
@NoArgsConstructor
@ToString
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

    @Builder(builderClassName = "OrderBuilder", toBuilder = true)
    public Order(Number id, Number petId, int quantity, String shipDate, String status, boolean complete) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }

    public static class OrderBuilder {

        private Date shipDateDateObj; // temporary Date holder instead of String

        /** replace the original setShipDate() (no args) */
        public OrderBuilder shipDate() {
            return shipDateAsData(new Date());
        }

        public OrderBuilder shipDate(String date) {
            return shipDateAsData(new Date(date));
        }

        /** replace setShipDate(Date date) */
        public OrderBuilder shipDateAsData(Date date) {
            this.shipDateDateObj = date;
            return this;
        }

        public String getShipDate() {
            return formatDate(shipDateDateObj);
        }

        /** Override Lombok build() to convert Date → formatted string */
        public Order build() {
            Order order = new Order(
                    id,
                    petId,
                    quantity,
                    getShipDate(),
                    status,
                    complete
            );
            return order;
        }

        /** Date → shipDate ISO formatting */
        private static String formatDate(Date date) {
            if (date == null) return null;
            ZonedDateTime zoned = date.toInstant().atZone(ZoneOffset.UTC);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            return zoned.format(fmt);
        }
    }

    public void updateId(Object id) {
        this.id = (Number) id;
    }
}
