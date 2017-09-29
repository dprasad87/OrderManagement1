package om.data;

import om.domain.Order;
import org.springframework.stereotype.Repository;

@Repository("data.OrderRepository")
public class OrderRepository extends InMemoryRepository<Order> {


    protected void updateIfExists(Order original, Order desired) {
        original.setDescription(desired.getDescription());
        original.setCostInCents(desired.getCostInCents());
        original.setComplete(desired.isComplete());
    }


}
