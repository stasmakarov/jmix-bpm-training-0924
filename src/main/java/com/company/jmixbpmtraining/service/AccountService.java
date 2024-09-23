/*
 * Copyright (c) 2024. Haulmont.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.company.jmixbpmtraining.service;

import com.company.jmixbpmtraining.entity.Account;
import com.company.jmixbpmtraining.entity.TransferDirection;
import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import org.flowable.engine.delegate.BpmnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static java.lang.Thread.sleep;

@Component("jbt_AccountService")
public class AccountService {

    public static final int PROBABILITY_FAIL = 30;

    @Autowired
    private DataManager dataManager;
    
    @Autowired
    private SystemAuthenticator authenticator;
    
    public boolean debit(String owner, Long amountToDebit) {
        if(!failure())
            return transferMoney(owner, amountToDebit, TransferDirection.DEBIT);
        else
            return false;
    }

    public boolean credit(String owner, Long amountToCredit) {
        if(!failure())
            return transferMoney(owner, amountToCredit, TransferDirection.CREDIT);
        else
            return false;
    }

    private boolean transferMoney(String owner, Long amount, TransferDirection direction) {
        Account account = getAccount(owner);

        double newAmount = 0;
            switch (direction) {
                case DEBIT -> {newAmount = account.getAmount() - amount;}
                case CREDIT -> {newAmount = account.getAmount() + amount;}
            }

            if (newAmount < 0L) {
                System.out.println("Transfer error: account " + owner);
                return false;
            } else {
                account.setAmount(newAmount);
                dataManager.save(account);
                System.out.println("Transfer success: account " + owner + " by " + amount);
                return true;
            }
    }

    private Account getAccount(String owner) {
        Account account;
        authenticator.begin();
        try {
            account = dataManager.load(Account.class)
                    .query("select a from jbt_Account a where a.owner = :owner")
                    .parameter("owner", owner)
                    .one();
        } finally {
            authenticator.end();
        }
        return account;
    }

    private boolean failure() {
        Random random = new Random();
        if(random.nextInt(100) > PROBABILITY_FAIL)
            throw new BpmnError("BusinessExceptionOccurred");

        return false;
    }
    
}
