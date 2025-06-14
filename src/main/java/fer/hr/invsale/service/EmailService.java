package fer.hr.invsale.service;

import fer.hr.invsale.DAO.Order;
import fer.hr.invsale.DTO.orderItem.OrderItemDTO;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.rmi.NoSuchObjectException;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    ProductService productService;

    private String buildBodyFromOrder(Order order) throws NoSuchObjectException {
        StringBuilder sb = new StringBuilder();

        sb.append("Hvala na Vašoj narudžbi!\n\n");
        sb.append("Adresa dostave: ").append(order.getShippingAddress() != null ? order.getShippingAddress() : "Nije navedena").append("\n\n");

        sb.append("Stavke narudžbe:\n");

        List<OrderItemDTO> items = orderItemService.getAllOrderItemsByOrderId(order.getIdOrder());
        for (OrderItemDTO item : items) {
            double itemTotal = productService.getPrice(item.getProductId()) * item.getQuantity();

            sb.append("- ").append(item.getProductName())
                    .append(" (").append(item.getQuantity()).append(" kom " + item.getUnitName() + ") - ")
                    .append(String.format("%.2f", itemTotal)).append(" EUR");

            sb.append("\n");
        }

        sb.append("\nUkupna cijena: ").append(String.format("%.2f", order.getTotalPrice())).append(" EUR");

        sb.append("\n\nNačin plaćanja: ").append(order.getPaymentMethod());
        sb.append("\nStatus narudžbe: ").append(order.getOrderStatus().getName()); // pretpostavka da ima getName()

        sb.append("\n\nDatum narudžbe: ").append(order.getOrderTimestamp());

        return sb.toString();
    }

    public void sendSimpleEmail(Order order) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("d.dragojevic2510@gmail.com"); // ili postavi u properties kao default
        message.setTo("d.dragojevic2510@gmail.com");
        message.setSubject("Order successful");
        try {
            message.setText(buildBodyFromOrder(order));
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }
}

