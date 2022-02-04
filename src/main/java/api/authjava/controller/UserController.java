package api.authjava.controller;

import api.authjava.auxi.EmailPassword;
import api.authjava.auxi.Message;
import api.authjava.controller.advice.ResourceNotFoundException;
import api.authjava.model.User;
import api.authjava.auxi.UserWithOutPass;
import api.authjava.service.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:8080", "https://pokemon-marketplace-accenture.herokuapp.com"})
@RequestMapping("/users")
public class UserController {
        @Autowired
        private UserServiceImpl userService;

        @PostMapping("/auth")
        @ApiOperation("User Authentication")
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "JSON with user founded and authorized"),
                @ApiResponse(code = 400, message = "Wrong Values"),
                @ApiResponse(code = 401, message = "User not found and not authorized"),
                @ApiResponse(code = 404, message = "The rote not founded"),
                @ApiResponse(code = 500, message = "Internal Server Error")
        })
        public ResponseEntity<User> findUser(@Valid @RequestBody EmailPassword emailPassword) {
                Optional<User> user = this.userService.getEmailAndPassword(
                        emailPassword.getEmail(), emailPassword.getPassword());
                if(user.isPresent()) {
                        return new ResponseEntity<>(user.get(), HttpStatus.OK);
                } else {
                        throw new ResourceNotFoundException("user");
                }
        }


        @PostMapping("/saveuser")
        @ApiOperation("Create new user")
        @ApiResponses(value = {
                @ApiResponse(code = 201, message = "A new user created"),
                @ApiResponse(code = 400, message = "Wrong Values"),
                @ApiResponse(code = 500, message = "Internal Server Error")
        })
        public ResponseEntity<Message> create(@Valid @RequestBody User user) {
                Boolean canSaved = this.userService.save(user);
                if(canSaved){
                        return new ResponseEntity<>(new Message("sucess - A new user created"), HttpStatus.CREATED);
                }

                return new ResponseEntity<>(new Message("error - This user already exist"), HttpStatus.BAD_REQUEST);

        }

        @PutMapping("/updateuser")
        @ApiOperation("Update user")
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "JSON with user with the password updated"),
                @ApiResponse(code = 400, message = "JSON with user with wrong or null values"),
                @ApiResponse(code = 500, message = "Internal Server Error")
        })

        public ResponseEntity<User> update(@Valid @RequestBody UserWithOutPass user) {
                User upUser = this.userService.update(user);
                if(upUser.getId() == null){
                        return new ResponseEntity<>( upUser, HttpStatus.OK);
                }
                return new ResponseEntity<>( upUser, HttpStatus.BAD_REQUEST);
        }

        @PutMapping("/password")
        @ApiOperation("Update password")
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "A password updated"),
                @ApiResponse(code = 400, message = "Wrong Values"),
                @ApiResponse(code = 500, message = "Internal Server Error")
        })
        public ResponseEntity<Message> updatePassword(@Valid @RequestBody EmailPassword emailPassword) {
                Boolean isUpdated = this.userService.updatePassword(emailPassword.getEmail(), emailPassword.getPassword());

                if (isUpdated) {
                        return new ResponseEntity<>(new Message("sucess- password updated"), HttpStatus.OK);
                }
                return new ResponseEntity<>(new Message("error- password not updated, this email not exist"), HttpStatus.BAD_REQUEST);

        }
}
