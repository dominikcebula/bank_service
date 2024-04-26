package com.dominikcebula.bank.service.bls.ds;

import com.dominikcebula.bank.service.dto.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
@Getter
public class LockableAccount {
    private final Lock lock = new ReentrantLock();

    private final Account account;

    public boolean tryLock(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return lock.tryLock(timeout, timeUnit);
    }

    public void unlock() {
        lock.unlock();
    }
}
