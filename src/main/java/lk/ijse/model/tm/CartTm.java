package lk.ijse.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CartTm {
   private String ItemId;
   private String ItemName;
   private int Quantity;
   private double Price;
   private double Total;
   private JFXButton btnRemove;
}
