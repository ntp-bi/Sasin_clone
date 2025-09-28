package com.ntp.sasin_be.mapper;

import com.ntp.sasin_be.dto.CartDTO;
import com.ntp.sasin_be.dto.CartItemDTO;
import com.ntp.sasin_be.entities.Cart;
import com.ntp.sasin_be.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "user.id", target = "userId")
    CartDTO toCartDto(Cart cart);

    List<CartDTO> toCartDtoList(List<Cart> cartList);

    @Mapping(target = "user", ignore = true)
    Cart toCartEntity(CartDTO cartDTO);

    List<Cart> toCartEntityList(List<CartDTO> cartDTOList);

    // Cart Items
    @Mapping(source = "product.title", target = "productTitle")
    @Mapping(source = "product.id", target = "productId")
    CartItemDTO toCartItemDTO(CartItem cartItem);

    List<CartItemDTO> toCartItemDtoList(List<CartItem> cartItemList);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItem toCartItemEntity(CartItemDTO cartItemDTO);

    List<CartItem> toCartItemEntityList(List<CartItemDTO> cartItemDTOList);
}
