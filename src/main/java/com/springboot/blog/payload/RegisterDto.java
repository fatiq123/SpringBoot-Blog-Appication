package com.springboot.blog.payload;

import com.springboot.blog.entity.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private String name;
    private String username;
    private String email;
    private String password;
    private String role;

}
