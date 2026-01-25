package com.shopwithanish.ecommerse.application.ServiceImplementations;

import com.shopwithanish.ecommerse.application.AllAboutSecurity.AuthUtil;
import com.shopwithanish.ecommerse.application.AllCustomExceptions.ApiException;
import com.shopwithanish.ecommerse.application.Model.Cart;
import com.shopwithanish.ecommerse.application.Model.CartItem;
import com.shopwithanish.ecommerse.application.Model.Product;
import com.shopwithanish.ecommerse.application.Repository.CartItemRepository;
import com.shopwithanish.ecommerse.application.Repository.CartRepository;
import com.shopwithanish.ecommerse.application.Repository.ProductRepository;
import com.shopwithanish.ecommerse.application.ResponceDtos.CartResponceDto;
import com.shopwithanish.ecommerse.application.ResponceDtos.ProductResponceDto;
import com.shopwithanish.ecommerse.application.Services.CartService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    @Override
    public CartResponceDto  addProductToCartFN(Long productid, Long requestedQuantity) {

        //find if cart obj already exist then need to add this product in that cart and if not then need to create cart obj
        Cart cart=CreateCartFn();

        //get product details
        Product product=productRepository.findById(productid).orElseThrow(()-> new ApiException("product not found with this id"));

        //perform validations
        //check if this same item/product already exist as cart-item in same Cart
       CartItem cartItem= cartItemRepository.findCartItemByProductIdAndCartId( productid , cart.getCartId() );
       if(cartItem!=null){
           throw new ApiException("this same product already exist in cart");
       }
       if(product.getStock()==0){
           throw new ApiException("this product is out of stock");
       }
       if(product.getStock() < requestedQuantity){
           throw new ApiException(" please make an order less than or equal to this available quantity: "+ product.getStock());
       }

       //create cart-item object from product to add in Cart
        CartItem cartItem1=new CartItem();
       cartItem1.setProduct(product);
       cartItem1.setQuantity(requestedQuantity);
       cartItem1.setProductsPrice(product.getSpecialPrice());
       cartItem1.setDiscountOnProduct(product.getDiscount());
       cartItem1.setCart(cart);
        // add to cart collection (maintain both sides)
        cart.getCartItemList().add(cartItem1);
       cartItemRepository.save(cartItem1);

       cart.setTotalCartPrice(cart.getTotalCartPrice()+ cartItem1.getProductsPrice()* requestedQuantity);
       cartRepository.save(cart);

       List<CartItem> cartItemList=  cart.getCartItemList();

       CartResponceDto cartResponceDto=new CartResponceDto();
       cartResponceDto.setCartId(cart.getCartId());
       cartResponceDto.setTotalCartPrice(cart.getTotalCartPrice());

       for(CartItem item: cartItemList){

           Product prod=item.getProduct();
           ProductResponceDto productResponceDto=modelMapper.map(prod , ProductResponceDto.class);
           productResponceDto.setQuantity(item.getQuantity()); // caritem (product) kiti quantity ne buy kela ahe
           cartResponceDto.getProductResponceDtoList().add(productResponceDto);
       }
       return cartResponceDto;



    }



    public Cart CreateCartFn(){
        String email = authUtil.LoggedInEmail();
        Cart existingCart = cartRepository.findCartByEmail(email).orElse(null);

        if (existingCart != null) {
            return existingCart; // managed, loaded from DB
        }

        // create + save new cart and return the managed instance
        Cart cart = new Cart();
        cart.setTotalCartPrice(0.0);
        cart.setUsers(authUtil.LoggedInUser());
        return cartRepository.save(cart);
    }

    @Override
    public List<CartResponceDto> fetchAllcarts() {
        //one cart> one CartResponceDto
        //many carts> List<cartResponceDto>
        List<CartResponceDto> cartResponceDtoList=new ArrayList<>();

        //how will u find all carts of all users> simple from DB> from cart-Repo
        List<Cart> cartList =cartRepository.findAll();
        if(cartList.isEmpty())throw new ApiException("empty carts");

        //cart madhe direct product nahi ahe
        //cart.get-cartitemlist> every item in cartitemlist connected to one product > product to productResponce dto> return cart responce dto> then list
       for(Cart cart: cartList){

           //for every one cart there is one cartResponceDto
           CartResponceDto cartResponceDto=new CartResponceDto();
           cartResponceDto.setCartId(cart.getCartId());
           cartResponceDto.setTotalCartPrice(cart.getTotalCartPrice());

         //one single cart consist of List of cart-items
         List<CartItem> cartItemList=  cart.getCartItemList();
         for(CartItem item: cartItemList){
             //every item >indicate one product
            Product prod=  item.getProduct();
            //cartResponceDto include list of productResponceDto hence convert every product to productResponceDto
            ProductResponceDto productResponceDto= modelMapper.map(prod , ProductResponceDto.class);
             productResponceDto.setQuantity(item.getQuantity()); // caritem (product) kiti quantity ne buy kela ahe
             cartResponceDto.getProductResponceDtoList().add(productResponceDto);
         }
           // add the cart DTO once per cart (not inside the items loop)
           cartResponceDtoList.add(cartResponceDto);
       }

        return cartResponceDtoList;
    }

    @Override
    public CartResponceDto fetchLoggedUserCartDetail(String email, Long cartId) {

        Cart existInDb=cartRepository.findCartByEmailAndByCartId(email , cartId);
        if(existInDb==null){
            throw new ApiException("No cart exist with given cardID: "+cartId + "and with given email: "+email);
        }
        //if not null need to return CartResponceDto
        //CartResponceDto inclued productResponcedto
        CartResponceDto cartResponceDto=new CartResponceDto();
        cartResponceDto.setCartId(cartId);
        cartResponceDto.setTotalCartPrice(existInDb.getTotalCartPrice());


        List<CartItem > cartItemList =existInDb.getCartItemList();

        cartItemList.forEach(cartItem -> {
            Product product=cartItem.getProduct();
          ProductResponceDto productResponceDto1 =modelMapper.map(product, ProductResponceDto.class);
          productResponceDto1.setQuantity(cartItem.getQuantity());
          cartResponceDto.getProductResponceDtoList().add(productResponceDto1);
        });

        return cartResponceDto;
    }

    @Transactional
    @Override
    public CartResponceDto updateQuantityOfProductinCart(Long productId, Long newQuantity) {

        String email=  authUtil.LoggedInEmail();
        Cart cartinDB=  cartRepository.findCartByEmail(email).orElseThrow(()-> new ApiException(" cart not found"));
        Long cartId= cartinDB.getCartId();
        //double check
        Cart cartExist= cartRepository.findCartByEmailAndByCartId(email , cartId);
        if(cartExist==null) throw new ApiException(" cart not exist ");

        // 2. fetch cartItem for this product
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId ,cartId );
        if (cartItem == null) {
            throw new ApiException("Product not found in cart");
        }
        //get product details
        Product product=productRepository.findById(productId).orElseThrow(()-> new ApiException("product not found with this id"));

        if(product.getStock()==0){
            throw new ApiException("this product is out of stock");
        }
        if(product.getStock() < newQuantity){
            throw new ApiException(" please make an order less than or equal to this available quantity: "+ product.getStock());
        }
        // 3. update quantity
        cartItem.setQuantity( cartItem.getQuantity() + newQuantity);
        // 4. update item total price
        Double Spprice = cartItem.getProduct().getSpecialPrice();
        cartItem.setProductsPrice(Spprice);
        cartItem.setDiscountOnProduct(product.getDiscount());
        CartItem savedCartitem =cartItemRepository.save(cartItem);

        cartExist.setTotalCartPrice( cartExist.getTotalCartPrice() + savedCartitem.getProductsPrice() * newQuantity);
        Cart savedCArt= cartRepository.save(cartExist);

        if(savedCartitem.getQuantity()==0){

            cartItemRepository.deleteById(savedCartitem.getCartitemId());
        }
        cartItemRepository.save(cartItem);


        //cart to cartResDto
        CartResponceDto cartResponceDto=new CartResponceDto();
        cartResponceDto.setCartId(savedCArt.getCartId());
        cartResponceDto.setTotalCartPrice(savedCArt.getTotalCartPrice());

        List<CartItem > cartItemList =savedCArt.getCartItemList();

        cartItemList.forEach(cartItemm -> {
            Product productt =cartItemm.getProduct();
            ProductResponceDto productResponceDto1 =modelMapper.map(productt, ProductResponceDto.class);
            productResponceDto1.setQuantity(cartItemm.getQuantity());
            cartResponceDto.getProductResponceDtoList().add(productResponceDto1);
        });

        return cartResponceDto;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        //check cart exist for this cartId
        Cart cart =cartRepository.findById(cartId).orElseThrow(()-> new ApiException(" cart not exist associted with this cartId: "+ cartId));
        //check product exist for this productId
        Product product=productRepository.findById(productId).orElseThrow(()-> new ApiException("product not found with this id"));
        //check this product exist in this same cart or not > product will exist in cart as cart-item hence need to find cart-item
        CartItem cartItem=  cartItemRepository.findCartItemByProductIdAndCartId( productId , cartId);
        //got product> as cart item
        //before delete reduce original cart total value then save upadated cart
        //                                                  single product price      * his quantity as cart-item
        cart.setTotalCartPrice(cart.getTotalCartPrice() -  cartItem.getProductsPrice() * cartItem.getQuantity());

        cartItemRepository.deleteCartItemByCartIdAndByProductId( cartId , productId);



        return "Product with name :"+product.getProductName() +" deleted from your cart !!!";
    }

    @Transactional
    @Override
    public CartItem updateThisCartItemFN(Cart cartt , CartItem cartItemm, Long productid) {
        //old/original cart price
        Product Upproduct=  productRepository.findById(productid).orElseThrow(()-> new ApiException("product not exist with given id"));

        Double old_cartPrice= cartt.getTotalCartPrice();
        //old product with old price reduced
        Double priceAfterReduction  = old_cartPrice- (cartItemm.getProductsPrice() * cartItemm.getQuantity() );

        //new price set
        cartItemm.setProductsPrice(Upproduct.getSpecialPrice());
        cartItemm.setDiscountOnProduct(Upproduct.getDiscount());
       CartItem c= cartItemRepository.save(cartItemm);
        //recalulate cart price with upadated cartitem productprice
        Double priceAfterAddition=priceAfterReduction + cartItemm.getProductsPrice() *cartItemm.getQuantity();

        cartt.setTotalCartPrice(priceAfterAddition);
        //save cart
        cartRepository.save(cartt);

 return c;
    }
}
