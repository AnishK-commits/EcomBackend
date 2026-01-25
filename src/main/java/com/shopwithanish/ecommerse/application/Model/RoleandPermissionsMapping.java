package com.shopwithanish.ecommerse.application.Model;



import com.shopwithanish.ecommerse.application.Enums.AppRole;
import com.shopwithanish.ecommerse.application.Enums.PermissionsTypes;

import java.util.Map;
import java.util.Set;

public class RoleandPermissionsMapping {

   private static final  Map<AppRole, Set<PermissionsTypes>> role_permission_map= Map.of
            (  AppRole.ADMIN , Set.of(PermissionsTypes.APPOINTMENT_CREATE , PermissionsTypes.APPOINTMENT_READ
                                   ,PermissionsTypes.PATIENT_CREATE ,PermissionsTypes.PATIENT_READ,PermissionsTypes.PATIENT_UPDATE),

                    AppRole.USER , Set.of(PermissionsTypes.APPOINTMENT_CREATE , PermissionsTypes.APPOINTMENT_READ,
                          PermissionsTypes.APPOINTMENT_DELETE,PermissionsTypes.APPOINTMENT_UPDATE),

                    AppRole.SELLER, Set.of(PermissionsTypes.APPOINTMENT_CREATE , PermissionsTypes.APPOINTMENT_READ
                            ,PermissionsTypes.PATIENT_CREATE ,PermissionsTypes.PATIENT_READ,PermissionsTypes.PATIENT_UPDATE,
                            PermissionsTypes.APPOINTMENT_DELETE,PermissionsTypes.APPOINTMENT_UPDATE) );


   public static Set<PermissionsTypes> getPermissionForParticularRole(Role role){

       return role_permission_map.getOrDefault(role,Set.of());
   }
}
