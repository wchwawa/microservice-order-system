package com.example.bankservice.controller;

import com.example.bankservice.mapper.accountmapper;
import com.example.bankservice.model.account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class accountcontroller {
    
    private final accountmapper accountmapper;

    public accountcontroller(accountmapper accountmapper) {
        this.accountmapper = accountmapper;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<account> getAccountInfo(@PathVariable String accountId) {
        account account = accountmapper.findAccountByAccountIdWithLock(accountId);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<account> getAccountByUsername(@PathVariable String username) {
        account account = accountmapper.findAccountByUsername(username);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{username}")
    public ResponseEntity<account> deposit(@PathVariable String username, @RequestBody DepositRequest request) {
        account account = accountmapper.findAccountByUsername(username);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        BigDecimal newBalance = account.getBalance().add(request.getAmount());
        account.setBalance(newBalance);
        accountmapper.updateAccountBalance(account.getAccountId(), newBalance);

        return ResponseEntity.ok(account);
    }

    public static class DepositRequest {
        private BigDecimal amount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}