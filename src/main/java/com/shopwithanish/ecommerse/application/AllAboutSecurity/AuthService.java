package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import com.shopwithanish.ecommerse.application.Enums.AppRole;
import com.shopwithanish.ecommerse.application.Model.Role;
import com.shopwithanish.ecommerse.application.Repository.RoleRepository;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.UserRepository;
import com.shopwithanish.ecommerse.application.RequestDtos.SignupRequestDto;
import com.shopwithanish.ecommerse.application.RequestDtos.UserDTO;
import com.shopwithanish.ecommerse.application.ResponceDtos.UserResponseDTO;
import com.shopwithanish.ecommerse.application.Services.RazorpayCustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class AuthService {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTutil jwTutil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RazorpayCustomerService razorpayCustomerService;

    @Autowired
    ModelMapper modelMapper;

    public ResponseEntity<?> LoginInUserfn(LoginRequest loginRequest) {

        Authentication authentication;
        try{
            authentication= authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {

            Map<String , Object> map=new HashMap<>();
            map.put("message" , "BAD_CREDIENTIALS");
            map.put("status" ,false);
            return new ResponseEntity<>(map , HttpStatus.NOT_FOUND);
        }
        //if authentication is successful we come here and set authentication obj to security context holder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //now i have to generate token to send in login responce to tell signing is completed successfully

        Users userentity= (Users) authentication.getPrincipal();

       // String token= jwTutil.generateTokenFromUsernameFn(userentity.getUsername()); //here need to pass userdetails obj so create first userdetauls obj
        //now we playing with cookies so we first create cookie and get token from it
            ResponseCookie cookie=  jwTutil.crateCookieFn(userentity);

        String username= userentity.getUsername();
        Long userid=userentity.getUserid();

        List<String> roles=userentity.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());

        //   “From the user, -> get all authority objects → extract role names → put them into a list.”
        //Each item is a GrantedAuthority object
        //| Before (object)          | After (string) |
        //| ------------------------ | -------------- |
        //| `SimpleGrantedAuthority` | `"ROLE_USER"`  |
        //| `SimpleGrantedAuthority` | `"ROLE_ADMIN"` |

        LoginResponce responce=new LoginResponce( cookie.toString(),  username , roles, userid);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(responce);
    }


    public ResponseEntity<?> signUpNewUser(SignupRequestDto signupRequestDto) {
        //check if user already exist in our system
         Users useridDB=  userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);

         if(useridDB!=null) {
             return new ResponseEntity<>(new MessageResponce("username exist in system login in directly"), HttpStatus.OK);
         }
         Users useridDBB=  userRepository.findByEmail (signupRequestDto.getUsername()).orElse(null);

         if(useridDBB!=null) {
             return new ResponseEntity<>(new MessageResponce("email exist in system login in directly"), HttpStatus.OK);
         }

         Users users=new Users();
         users.setEmail(signupRequestDto.getEmail());;
         users.setUsername(signupRequestDto.getUsername());;
         users.setPhoneNo(signupRequestDto.getPhoneNo());
         users.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        Set<String> stringsroleset = signupRequestDto.getRoles();
        Set<Role> roleSet = new HashSet<>();

        if (stringsroleset == null || stringsroleset.isEmpty()) {

            // Default USER role
            Role role = roleRepository
                    .findByRoleName(AppRole.USER)
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            roleSet.add(role);
        }
        else {
            for (String role : stringsroleset) {
                switch (role.toLowerCase()) {

                    case "admin":
                        roleSet.add(
                                roleRepository.findByRoleName(AppRole.ADMIN)
                                        .orElseThrow(() -> new RuntimeException("ADMIN role not found"))
                        );
                        break;

                    case "seller":
                        roleSet.add(
                                roleRepository.findByRoleName(AppRole.SELLER)
                                        .orElseThrow(() -> new RuntimeException("SELLER role not found"))
                        );
                        break;

                    default:
                        roleSet.add(
                                roleRepository.findByRoleName(AppRole.USER)
                                        .orElseThrow(() -> new RuntimeException("USER role not found"))
                        );
                }
            }
        }

        users.setRoles(roleSet);



        Users savedUser= userRepository.save(users);

        // ✅ Create Razorpay customer
        try {
            String customerId = razorpayCustomerService.createCustomer(savedUser);
            log.info("Razorpay customer created: {} for user: {}", customerId, savedUser.getEmail());
        } catch (Exception e) {
            log.error("Failed to create Razorpay customer during signup, will retry on first payment", e);
            // Don't fail signup if Razorpay customer creation fails
        }

        return new ResponseEntity<>( new MessageResponce("user registered successfully !!!") , HttpStatus.OK);
    }

    public ResponseEntity<?> getCurrentNname(Authentication authentication) {
       Users users= (Users) authentication.getPrincipal();
         if(authentication!=null){
             return new ResponseEntity<>(users.getUsername() , HttpStatus.OK);
         }
         else {
             return null;
         }
    }

    public ResponseEntity<?> signOutUserFn() {
       ResponseCookie cookie= jwTutil.getCleanCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponce("you have been sign out buddy"));
    }

    public UserResponseDTO getAllUsers( Integer pageNumber , Integer pageSize , String sortBy , String sortOrder){

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of( pageNumber, pageSize , sort);

        //becoz everyone are users , sellers or admin
        Page<Users> userpage=userRepository.findAll(pageable);

        List<Users> usersList=userpage.getContent();

        List<UserDTO> userDTOList=new ArrayList<>();

        for(Users user: usersList){

           UserDTO userDTO= modelMapper.map(user , UserDTO.class);
           userDTOList.add(userDTO);
        }

        UserResponseDTO userResponseDTO=new UserResponseDTO();

        userResponseDTO.setContent(userDTOList);
        userResponseDTO.setPageNumber(userpage.getNumber());
        userResponseDTO.setPageSize(userpage.getSize());
        userResponseDTO.setTotalPages(userpage.getTotalPages());
        userResponseDTO.setTotalElements((int) userpage.getTotalElements());
        userResponseDTO.setLastPage(userpage.isLast());
        return userResponseDTO;


    }

    public UserResponseDTO getAllSellers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of( pageNumber, pageSize , sort);

        //becoz everyone are users , sellers or admin
        Page<Users> sellerPage =
                    userRepository.findUsersByRole(AppRole.SELLER, pageable);

        List<Users> usersList=sellerPage.getContent();

        List<UserDTO> userDTOList=new ArrayList<>();

        for(Users user: usersList){

            UserDTO userDTO= modelMapper.map(user , UserDTO.class);
            userDTOList.add(userDTO);
        }

        UserResponseDTO userResponseDTO=new UserResponseDTO();

        userResponseDTO.setContent(userDTOList);
        userResponseDTO.setPageNumber(sellerPage.getNumber());
        userResponseDTO.setPageSize(sellerPage.getSize());
        userResponseDTO.setTotalPages(sellerPage.getTotalPages());
        userResponseDTO.setTotalElements((int) sellerPage.getTotalElements());
        userResponseDTO.setLastPage(sellerPage.isLast());
        return userResponseDTO;

    }
}
