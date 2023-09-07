package com.dictionary.learningcards.controllers;

import com.dictionary.learningcards.models.Card;
import com.dictionary.learningcards.models.Group;
import com.dictionary.learningcards.security.UserDetails;
import com.dictionary.learningcards.services.CardService;
import com.dictionary.learningcards.services.GroupService;
import com.dictionary.learningcards.util.CardValidator;
import com.dictionary.learningcards.util.FormatIsNotSupportedException;
import jakarta.validation.Valid;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;
    private final GroupService groupService;
    private final CardValidator cardValidator;

    @Autowired
    public CardController(CardService cardService, GroupService groupService, CardValidator cardValidator) {
        this.cardService = cardService;
        this.groupService = groupService;
        this.cardValidator = cardValidator;
    }

    @GetMapping("")
    public String showAllCards(@RequestParam(name = "group", required = false, defaultValue = "none") String group,
            @RequestParam(name = "isLearned", required = false, defaultValue = "none") String filterIsLearned,
                                Model model) {
        group = group.replace("*", " ");
        model.addAttribute("groups", groupService.findAll());
        if (filterIsLearned.equals("true")) {
            if (!group.equals("none"))
                model.addAttribute(model.addAttribute("cards",
                        cardService.findByGroup(group).stream().filter(Card::isLearned).collect(Collectors.toList())));
            else
                model.addAttribute(model.addAttribute("cards",
                    cardService.findAll().stream().filter(Card::isLearned).collect(Collectors.toList())));
        } else if (filterIsLearned.equals("false")) {
            if (!group.equals("none"))
                model.addAttribute(model.addAttribute("cards",
                        cardService.findByGroup(group).stream().filter(card -> !card.isLearned()).collect(Collectors.toList())));
            else
                model.addAttribute(model.addAttribute("cards",
                    cardService.findAll().stream().filter(card -> !card.isLearned()).collect(Collectors.toList())));
        } else {
            if (!group.equals("none"))
                model.addAttribute("cards", cardService.findByGroup(group));
            else
                model.addAttribute(model.addAttribute("cards", cardService.findAll()));
        }
        return "cards/index";
    }

    @GetMapping("/{id}/edit")
    public String editCard(Model model, @PathVariable int id) {
        model.addAttribute("card", cardService.findById(id));
        model.addAttribute("AllGroups", groupService.findAll());
        return "cards/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable int id,
                         @ModelAttribute("card") @Valid Card card,
                         BindingResult bindingResult) {
        // TODO solve problem with groups  cardValidator.validate(card, bindingResult);
        if (bindingResult.hasErrors()) {
            return "cards/edit";
        }
        if (Objects.equals(card.getTranscription(), "")) {
            card.setTranscription(null);
        }
        if (Objects.equals(card.getExamples(), "")) {
            card.setExamples(null);
        }
        cardService.update(id, card);
        return "redirect:/cards/" + id;
    }

    @GetMapping("/add")
    public String addNewCard(@ModelAttribute("card") Card card,
                             Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "cards/new";
    }

    @PostMapping("/add")
    public String createCard(@ModelAttribute("card") @Valid Card card,
                             BindingResult bindingResult) {

        // TODO solve problem with groups cardValidator.validate(card, bindingResult);
        if (bindingResult.hasErrors()) {
            return "cards/new";
        }
        if (Objects.equals(card.getTranscription(), "")) {
            card.setTranscription(null);
        }
        if (Objects.equals(card.getExamples(), "")) {
            card.setExamples(null);
        }
        cardService.save(card);
        return "redirect:/cards";
    }

    @GetMapping("/addCardsFromFile")
    public String addCards() {
        return "cards/loadFile";
    }

    @PostMapping("/import")
    public String importCards(@RequestParam("file") MultipartFile reapExcelDataFile,
                              Model model) throws IOException {
        List<Card> tempCardsList = new ArrayList<>();
        if (reapExcelDataFile.getOriginalFilename() == null ||
                (!reapExcelDataFile.getOriginalFilename().endsWith(".xlsx") &&
                !reapExcelDataFile.getOriginalFilename().endsWith(".xls"))) {

            model.addAttribute("successfullyQuality", 0);
            model.addAttribute("message", new FormatIsNotSupportedException(reapExcelDataFile.getOriginalFilename()).getMessage());
            model.addAttribute("failedQuality",0);
            return "cards/import";
        }

        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
            Card cardInRow = new Card();

            XSSFRow row = worksheet.getRow(i);

            cardInRow.setWord(row.getCell(0).getStringCellValue());
            cardInRow.setTranslation(row.getCell(1).getStringCellValue());
            String example = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : null;
            cardInRow.setExamples(example);
            String transcription = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null;
            cardInRow.setTranscription(transcription);
            String group = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : null;
            if (group != null) {
                 if (groupService.findGroupByName(group) == null) {
                     groupService.save(new Group(group));
                 }
                 cardInRow.setGroups(Collections.singletonList(groupService.findGroupByName(group)));
            }
            tempCardsList.add(cardInRow);
        }

        List<String> failed = new ArrayList<>();
        for (Card card : tempCardsList) {
            try {
                cardService.save(card);
            } catch (Exception e) {
                if (cardService.findByWord(card.getWord()).isPresent())
                    failed.add(card.getWord() + "   - this word is exist already");
                else {
                    failed.add(card.getWord());
                }
            }
        }
        model.addAttribute("failed", failed);
        model.addAttribute("successfullyQuality", tempCardsList.size() - failed.size());
        model.addAttribute("failedQuality", failed.size());
        return "cards/import";
    }

    @GetMapping("/{id}")
    public String showInfo(Model model,
                           @ModelAttribute("cardFor") Card cardFor,
                           @PathVariable("id") int id) {
        Card card = cardService.findById(id);
        if (card == null)
            return "redirect:/cards";
        model.addAttribute("card", card);
        List<Group> groups = card.getGroups();
        if (groups.isEmpty()) {
            model.addAttribute("groupsList", groupService.findAll());
            System.out.println(model.getAttribute("groupsList"));
        } else {
            model.addAttribute("groups", groups);
            System.out.println(groups);
        }
        return "cards/info";
    }

    @PostMapping("/{id}/assignLearned")
    private String assignLearned(@ModelAttribute("cardFor") Card card,
        @PathVariable int id) {
        Card card1 = cardService.findById(id);
        card1.setLearned(card.isLearned());
        System.out.println(card1);
        cardService.update(id, card1);
        return "redirect:/cards";
    }

    @PostMapping("/{id}/assignGroup")
    private String assignGroup(@ModelAttribute("cardFor") Card card,
                          @PathVariable int id) {
        Card card1 = cardService.findById(id);
        card1.setGroups(card.getGroups());
        cardService.update(id, card1);
        return "redirect:/cards";
    }

    @DeleteMapping("{id}/delete")
    private String delete(@PathVariable int id) {
        cardService.deleteById(id);
        return "redirect:/cards";
    }
}
