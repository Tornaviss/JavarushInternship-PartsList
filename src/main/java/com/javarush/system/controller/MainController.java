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


    @Autowired
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView allParts(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int resultsCount,
                                 @RequestParam(defaultValue = "-1") int searchingId) {
        this.page = page;
        this.resultsCount = resultsCount;
        if (partService.getFilterAttr() == null) partService.setFilterAttr("all");

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
            if (searchingId != -1) modelAndView.addObject("searchingId", searchingId);
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
    public String removeOne(@PathVariable int id) {
        if (partService.getById(id).getCount() == 0) this.page = partService.checkPage(page, resultsCount);
        partService.removeOne(id);
        page = partService.checkPage(page, resultsCount);
        return "redirect:/?page=" + this.page + "&resultsCount=" + this.resultsCount;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String remove(@PathVariable int id) {
        Part part = partService.getById(id);
        partService.remove(part);
        page = partService.checkPage(page, resultsCount);
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
        partService.setOrdering(param);
        return "redirect:/?page=" + this.page + "&resultsCount=" + this.resultsCount;
    }
    @RequestMapping(value = "/search/{partName}", method = RequestMethod.GET)
    public String search(@PathVariable String partName, RedirectAttributes redirectAttributes) {
        partName = partName.trim();
        int res = partService.searchPartPage(partName, page, resultsCount);
        if (res == -1) {
            redirectAttributes.addFlashAttribute("flashMessage", "Part '" + partName + "' not found.");
        } else {
            page = res;
            return "redirect:/?page=" + page + "&resultsCount=" + this.resultsCount + "&searchingId=" + partService.getIdByName(partName);
        }
        return "redirect:/?page=" + page + "&resultsCount=" + this.resultsCount;
    }
    @RequestMapping(value = "/applyFilter/{filter}", method = RequestMethod.GET)
    public String applyFilter(@PathVariable String filter) {
        partService.setFilterAttr(filter);
        return "redirect:/";
    }
}
