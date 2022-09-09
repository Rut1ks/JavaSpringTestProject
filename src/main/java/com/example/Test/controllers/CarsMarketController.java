package com.example.Test.controllers;

import com.example.Test.models.CarsMarket;
import com.example.Test.models.Games;
import com.example.Test.models.News;
import com.example.Test.repositories.CarsMarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/cars")
public class CarsMarketController {


    @Autowired
    private CarsMarketRepository carsMarketRepository;

    @GetMapping("/")
    public String index(Model model)
    {
        Iterable<CarsMarket> cars = carsMarketRepository.findAll();
        model.addAttribute("cars",cars);
        return "carsmarket/index";
    }

    @GetMapping("/add")
    public String addView(Model model, CarsMarket cars)
    {
        model.addAttribute("cars",new CarsMarket());
        return "carsmarket/add-cars";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("cars")
            @Valid CarsMarket newCars,
            BindingResult bindingResult,
            Model model)
    {
        if(bindingResult.hasErrors())
            return "carsmarket/add-cars";


        carsMarketRepository.save(newCars);
        return "redirect:/cars/";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam("name") String name,
            Model model)
    {
        List<CarsMarket> newsList = carsMarketRepository.findByName(name);
        model.addAttribute("cars",newsList);
        return "carsmarket/index";
    }


    @GetMapping("/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model)
    {
        Optional<CarsMarket> carsMarket = carsMarketRepository.findById(id);
        ArrayList<CarsMarket> carsMarketArrayList = new ArrayList<>();
        carsMarket.ifPresent(carsMarketArrayList::add);
        model.addAttribute("cars",carsMarketArrayList);
        return "carsmarket/info-cars";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model
    )
    {
        if(!carsMarketRepository.existsById(id))
        {
            return "redirect:/cars/";
        }
        Optional<CarsMarket> carsMarket = carsMarketRepository.findById(id);
        ArrayList<CarsMarket> carsMarketArrayList = new ArrayList<>();
        carsMarket.ifPresent(carsMarketArrayList::add);
        model.addAttribute("cars",carsMarketArrayList.get(0));
        return "carsmarket/edit-cars";
    }


    @PostMapping("/edit/{id}")
    public String editCars(
            @PathVariable("id") Long id,
            @ModelAttribute("cars") @Valid CarsMarket newCars,
            BindingResult bindingResult,
            Model model)
    {
        if(!carsMarketRepository.existsById(id))
        {
            return "redirect:/cars";
        }
        if(bindingResult.hasErrors())
            return "carsmarket/edit-cars";

        newCars.setId(id);

        carsMarketRepository.save(newCars);
        return "redirect:/cars/";
    }

    @GetMapping("/del/{id}")
    public String del(
            @PathVariable("id") Long id
    )
    {
        CarsMarket carsMarket = carsMarketRepository.findById(id).orElseThrow();
        carsMarketRepository.delete(carsMarket);
        return "redirect:/cars/";
    }
}
