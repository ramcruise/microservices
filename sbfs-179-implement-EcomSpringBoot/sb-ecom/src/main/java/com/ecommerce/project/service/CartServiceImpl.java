package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    AuthUtil authUtil;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find existing cart or create one
        Cart cart = createCart();

        //Retrieve product details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        //Perform Validations
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem != null) {
            throw new APIException("Product already exists in the cart with "+product.getProductName());
        }
        if (product.getQuantity()==0) {
            throw new APIException(product.getProductName()+" Product is out of stock");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please make an order of the "+product.getProductName()+" " +
                    "less than or equal to the quality "+product.getQuantity());
        }
        //Create Cart Item
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getPrice());
        newCartItem.setDiscount(product.getDiscount());

        //Save Cart Item
        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));

        cartRepository.save(cart);

         //Return updated Cart
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
         List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().
                map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;

    }
}
