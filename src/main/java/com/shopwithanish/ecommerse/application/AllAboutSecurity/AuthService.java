package com.shopwithanish.ecommerse.application.AllAboutSecurity;

import com.shopwithanish.ecommerse.application.Enums.AppRole;
import com.shopwithanish.ecommerse.application.Model.Role;
import com.shopwithanish.ecommerse.application.Repository.RoleRepository;
import com.shopwithanish.ecommerse.application.Model.Users;
import com.shopwithanish.ecommerse.application.Repository.UserRepository;
import com.shopwithanish.ecommerse.application.RequestDtos.SignupRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
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

        List<String> roles=userentity.getAuthorities().stream()
                .map(item->item.getAuthority())
                .collect(Collectors.toList());

        //   “From the user, -> get all authority objects → extract role names → put them into a list.”
        //Each item is a GrantedAuthority object
        //| Before (object)          | After (string) |
        //| ------------------------ | -------------- |
        //| `SimpleGrantedAuthority` | `"ROLE_USER"`  |
        //| `SimpleGrantedAuthority` | `"ROLE_ADMIN"` |

        LoginResponce responce=new LoginResponce( cookie.toString(),  username , roles);

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
         users.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        Set<String> stringsroleset =  signupRequestDto.getRoles();
        Set<Role> roleSet=new HashSet<>(); //users inbuilt format

        if(stringsroleset ==null){
            //means user not send roles then make him by default of type user role
            Role role=roleRepository.findByRoleName(AppRole.USER).orElse(null);
            if(role==null)throw new RuntimeException("role is null");

            roleSet.add(role);

            users.setRoles(roleSet);
        }
        else{
            for(String role: stringsroleset){
                switch (role){

                    case "admin" :  Role adminObj=roleRepository.findByRoleName(AppRole.ADMIN).orElseThrow(()-> new RuntimeException(" admin role not found"));
                                    roleSet.add(adminObj);
                                    users.setRoles(roleSet);
                     break;

                    case "seller" :  Role sellerObj=roleRepository.findByRoleName(AppRole.SELLER).orElseThrow(()-> new RuntimeException(" seller role not found"));
                                       roleSet.add(sellerObj);
                                       users.setRoles(roleSet);
                        break;

                    default:  Role userObj=roleRepository.findByRoleName(AppRole.USER).orElseThrow(()-> new RuntimeException(" user role not found"));
                              roleSet.add(userObj);
                              users.setRoles(roleSet);
                        break;

                }
            }
        }
        userRepository.save(users);
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
}
