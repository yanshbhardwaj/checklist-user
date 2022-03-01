package com.iedaas.checklistuser;

import com.iedaas.checklistuser.dto.UserDTO;
import com.iedaas.checklistuser.services.UserServices;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Transactional
public class AuthorizationFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Autowired
    private UserServices userServices;

    private static final String HEADER_STRING = "Authorization";

    @Value("${secretKey}")
    String secret;

    public String authenticate(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if(token==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Token Found");
        }

        String userEmail;
        try {
            logger.info("Validating Jwt token :={}", token);
             userEmail = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody()
                     .get("email", String.class);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token or token is expired");
        }
        boolean isUserExist = userServices.ifUserExist(userEmail);
        if(!isUserExist){
            logger.info("user email :={}, exist :={}", userEmail, isUserExist);
            String[] fullName = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace("Bearer", ""))
                    .getBody()
                    .get("name", String.class).split("\\s+");
            String firstName = fullName[0];
            String lastName = fullName[1];
            UserDTO userDTO = new UserDTO();
            userDTO.setFirstName(firstName);
            userDTO.setLastName(lastName);
            userDTO.setEmailId(userEmail);
            userDTO.setStatus(1);
            userServices.addUser(userDTO);
            logger.debug("userEmail :={}, userDto :={}", userEmail, userDTO);
        }
        return userEmail;
    }
}
