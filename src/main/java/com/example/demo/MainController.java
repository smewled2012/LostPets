package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    PetsRepository petsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    CloudinaryConfig cloudinaryConfig;


    @RequestMapping("/")
    public String home(Model model){

        model.addAttribute("pets", petsRepository.findAll());

        if(userService.getUser()!=null){

            model.addAttribute("user_id",userService.getUser().getId());
        }

        return "homepage";
    }

    @RequestMapping("/mark/{id}")
    public String markFound(@PathVariable("id") long id, Pets pet){
        pet=petsRepository.findById(id);
        pet.getStatus("found");
        petsRepository.save(pet);
        return "redirect:/";

    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        if(result.hasErrors()){
            return "registration";
        }

        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "redirect:/";
    }




    @RequestMapping("/addpet")
    public String addMessage(Model model){

        model.addAttribute("pet", new Pets());
        model.addAttribute("pets", petsRepository.findAll());

        return "petform";

    }

    //save the message
    @PostMapping("/addpet")
    public String processForm(@Valid @ModelAttribute("pet") Pets pet, @RequestParam("file") MultipartFile file, BindingResult result ){

        if(file.isEmpty()){
            return "redirect:/";
        }

        try{
            Map uploadresult = cloudinaryConfig.upload(file.getBytes(), ObjectUtils.asMap("resourcetype","auto"));
            pet.setPostImg(uploadresult.get("url").toString());
            pet.setUser(userService.getUser());
            petsRepository.save(pet);

        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/addpet";
        }

        if(result.hasErrors()){
            return "petform";
        }

      //  pet.setUser(userService.getUser());


        return "redirect:/";
    }

    @RequestMapping("/lostpets")
    public String lostDogs(Model model){
        String status = "lost";
        ArrayList<Pets> results = (ArrayList<Pets>)
                petsRepository.findAllByStatusContainingIgnoreCase(status);

        model.addAttribute("pets",results);
        return "lostpets";
    }


    @RequestMapping("/foundpets")
    public String foundDogs(Model model){
        String status = "found";
        ArrayList<Pets> results = (ArrayList<Pets>)
                petsRepository.findAllByStatusContainingIgnoreCase(status);
        model.addAttribute("pets",results);
        return "foundpets";
    }


    //to see in detail
    @RequestMapping("/detail/{id}")
    public String showCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("pet", petsRepository.findById(id).get());

        if(userService.getUser()!=null){
            model.addAttribute("user_id",userService.getUser().getId());
        }
        return "show";
    }

    //update a content
    @RequestMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") long id, Model model){

        model.addAttribute("pet", petsRepository.findById(id).get());
        model.addAttribute("user",userRepository.findById(id));
        return "petform";
    }

    // delete a content
    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id){
        petsRepository.deleteById(id);
        return "redirect:/";
    }





}
