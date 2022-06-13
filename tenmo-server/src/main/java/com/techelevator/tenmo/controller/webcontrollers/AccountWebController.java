package com.techelevator.tenmo.controller.webcontrollers;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.services.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("webaccount/")
public class AccountWebController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public AccountWebController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/balancebyAccountid?id=2002")
    public String getAccount(Model model, int id){
      //  User currentUser = this.createUserFromId(id);
        Account account = this.accountService.findAccountById(id);
        model.addAttribute("account", account);
        return "account";
    }

}
