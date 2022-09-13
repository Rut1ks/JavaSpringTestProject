package com.example.Test.controllers;

import com.example.Test.models.Address;
import com.example.Test.models.Users;
import com.example.Test.repositories.AddressRepository;
import com.example.Test.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainControllerOTM {
    @Autowired
    public AddressRepository addressRepository;
    @Autowired
    public PersonRepository personRepository;

    @GetMapping("/personOTM")
    public String Main(Model model){
        Iterable<Address> address = addressRepository.findAll();
        model.addAttribute("address",address);
        return "personOTM";
    }

    @PostMapping("/personOTM/add")
    public String blogPostAdd(@RequestParam String name, @RequestParam String street, Model model)
    {
        Address address = addressRepository.findByStreet(street);
        Users users = new Users(name, address);
        personRepository.save(users);
        return "redirect:/personOTM/";
    }
}
