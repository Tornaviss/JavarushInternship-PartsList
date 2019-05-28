package com.javarush.system.controller;

import com.javarush.system.exceptions.IllegalModificationException;
import com.javarush.system.model.Part;
import com.javarush.system.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MainController {
    private PartService partService;
    private int resultsCount;
    private int page;
    private static final String DEFAULT_ORDERING = "id";
    private static final String ESSENTIAL_LAST_ORDERING = "essential ASC";
    private static final String ESSENTIAL_FIRST_ORDERING = "essential DESC";
    private static final String NAME_ASC_ORDERING = "name ASC";
    private static final String NAME_DESC_ORDERING = "name DESC";
    private static final String COUNT_ASC_ORDERING = "count ASC";
    private static final String COUNT_DESC_ORDERING = "count DESC";



    @Autowired
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView allParts(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int resultsCount) {
        this.page = page;
        this.resultsCount = resultsCount;
        int partsCount = partService.partsCount();
        int pagesCount = (partsCount + 9)/resultsCount;
        if (pagesCount == 0) pagesCount = 1;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("parts");
        modelAndView.addObject("essentialPartTypes", Part.EssentialType.values());
        List<Part> allParts = partService.getAllParts(page, resultsCount);

        if (allParts.isEmpty()) {
            modelAndView.addObject("isNoElementsToShow", true);
        } else {
            modelAndView.addObject("page", page);
            modelAndView.addObject("resultsCount", resultsCount);
            modelAndView.addObject("partsCount", partsCount);
            modelAndView.addObject("pagesCount", pagesCount);
            modelAndView.addObject("partsList" , allParts);
            modelAndView.addObject("setupsCount", partService.calculateSetups());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/deleteOne/{id}", method = RequestMethod.GET)
    public String removeOnePart(@PathVariable int id) {
        partService.removeOne(id);
        return "redirect:/?page=" + this.page + "&resultsCount=" + this.resultsCount;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable int id) {
        Part part = partService.getById(id);
        partService.remove(part);
        return "redirect:/?page=" + this.page + "&resultsCount=" + this.resultsCount;
    }

    @RequestMapping(value = "/addOne/{id}", method = RequestMethod.GET)
    public String addOne(@PathVariable int id) {
        partService.addOne(id);
        return "redirect:/?page=" + this.page + "&resultsCount=" + this.resultsCount;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute Part part, RedirectAttributes redirectAttributes) {
        try {
            partService.add(part);
        } catch (IllegalModificationException e) {
            redirectAttributes.addFlashAttribute("flashMessage", e.getMessage());
        }
        return "redirect:/?page=" + this.page + "&resultsCount=" + this.resultsCount;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@ModelAttribute Part part, @PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            partService.update(id, part);
        } catch (IllegalModificationException e) {
            redirectAttributes.addFlashAttribute("flashMessage", e.getMessage());
        }
        return "redirect:/?page=" + this.page + "&resultsCount=" + this.resultsCount;
    }

    @RequestMapping(value = "/applyOrdering/{param}", method = RequestMethod.GET)
    public String applyOrdering(@PathVariable String param) {
        if (param.equals(DEFAULT_ORDERING)) partService.setOrdering(DEFAULT_ORDERING);
        if (param.equals(ESSENTIAL_FIRST_ORDERING.replaceFirst(" ", ""))) partService.setOrdering(ESSENTIAL_FIRST_ORDERING);
        if (param.equals(ESSENTIAL_LAST_ORDERING.replaceFirst(" ", ""))) partService.setOrdering((ESSENTIAL_LAST_ORDERING));
        if (param.equals(NAME_ASC_ORDERING.replaceFirst(" ", ""))) partService.setOrdering(NAME_ASC_ORDERING);
        if (param.equals(NAME_DESC_ORDERING.replaceFirst(" ", ""))) partService.setOrdering((NAME_DESC_ORDERING));
        if (param.equals(COUNT_ASC_ORDERING.replaceFirst(" ", ""))) partService.setOrdering(COUNT_ASC_ORDERING);
        if (param.equals(COUNT_DESC_ORDERING.replaceFirst(" ", ""))) partService.setOrdering((COUNT_DESC_ORDERING));
        return "redirect:/?page=" + this.page + "&resultsCount=" + this.resultsCount;
    }
}
