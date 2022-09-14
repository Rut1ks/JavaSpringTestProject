package com.example.Test.controllers;



import com.example.Test.models.Games;
import com.example.Test.models.News;
import com.example.Test.repositories.GamesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/games")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class GamesController {


    @Autowired
    private GamesRepository gamesRepository;

    @GetMapping("/")
    public String index(Model model)
    {
        Iterable<Games> games = gamesRepository.findAll();
        model.addAttribute("games",games);
        return "games/index";
    }

    @GetMapping("/add")
    public String addView(Model model, Games games)
    {
        model.addAttribute("news",new Games());
        return "games/add-games";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("games")
            @Valid Games newGames,
            BindingResult bindingResult,
            Model model)
    {
        if(bindingResult.hasErrors())
            return "games/add-games";


        gamesRepository.save(newGames);
        return "redirect:/games/";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam("name") String name,
            Model model)
    {
        List<Games> newsList = gamesRepository.findByNameContains(name);
        model.addAttribute("games",newsList);
        return "games/index";
    }


    @GetMapping("/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model)
    {
        Optional<Games> games = gamesRepository.findById(id);
        ArrayList<Games> gamesArrayList = new ArrayList<>();
        games.ifPresent(gamesArrayList::add);
        model.addAttribute("games",gamesArrayList);
        return "games/info-games";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model
    )
    {
        if(!gamesRepository.existsById(id))
        {
            return "redirect:/games/";
        }
        Optional<Games> games = gamesRepository.findById(id);
        ArrayList<Games> gamesArrayList = new ArrayList<>();
        games.ifPresent(gamesArrayList::add);
        model.addAttribute("games",gamesArrayList.get(0));
        return "games/edit-games";
    }
    @PostMapping("/edit/{id}")
    public String editGames(
            @PathVariable("id") Long id,
            @ModelAttribute("games") @Valid Games newGames,
            BindingResult bindingResult,
            Model model)
    {
        if(!gamesRepository.existsById(id))
        {
            return "redirect:/games";
        }
        if(bindingResult.hasErrors())
            return "games/edit-games";
        newGames.setId(id);
        gamesRepository.save(newGames);
        return "redirect:/games/";
    }

    @GetMapping("/del/{id}")
    public String del(
            @PathVariable("id") Long id
    )
    {
        Games games = gamesRepository.findById(id).orElseThrow();
        gamesRepository.delete(games);
        return "redirect:/games/";
    }
}
