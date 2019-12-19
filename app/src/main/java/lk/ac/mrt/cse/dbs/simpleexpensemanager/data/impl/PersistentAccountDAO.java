package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.ExpenseManageDBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.Table;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private ExpenseManageDBHelper expenseManageDBHelper;

    public PersistentAccountDAO(Context context){
        expenseManageDBHelper = new ExpenseManageDBHelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = expenseManageDBHelper.getReadableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + Table.AccountsTable.TABLE_NAME,null);
        results.moveToFirst();
        ArrayList<String> nums = new ArrayList<>();
        while(results.moveToNext()){
            nums.add(results.getString(results.getColumnIndex(Table.AccountsTable.ACCOUNT_NO)));
        }
        results.close();
        return nums;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = expenseManageDBHelper.getReadableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + Table.AccountsTable.TABLE_NAME,null);
        ArrayList<Account> accounts = new ArrayList<>();
        while(results.moveToNext()){
            Account account = new Account(
                    results.getString(results.getColumnIndex(Table.AccountsTable.ACCOUNT_NO)),
                    results.getString(results.getColumnIndex(Table.AccountsTable.BANK_NAME)),
                    results.getString(results.getColumnIndex(Table.AccountsTable.ACCOUNT_HOLDER_NAME)),
                    results.getDouble(results.getColumnIndex(Table.AccountsTable.BALANCE))
            );
            accounts.add(account);
        }
        results.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = expenseManageDBHelper.getReadableDatabase();
        Cursor results = db.rawQuery(
                "SELECT * FROM " + Table.AccountsTable.TABLE_NAME + " WHERE " + Table.AccountsTable.ACCOUNT_NO + "=?",
                new String[]{accountNo}
        );
        if (results.moveToNext()) {
            Account account = new Account(
                    results.getString(results.getColumnIndex(Table.AccountsTable.ACCOUNT_NO)),
                    results.getString(results.getColumnIndex(Table.AccountsTable.BANK_NAME)),
                    results.getString(results.getColumnIndex(Table.AccountsTable.ACCOUNT_HOLDER_NAME)),
                    results.getDouble(results.getColumnIndex(Table.AccountsTable.BALANCE))
            );
            results.close();
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.AccountsTable.ACCOUNT_NO,account.getAccountNo());
        contentValues.put(Table.AccountsTable.ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(Table.AccountsTable.BANK_NAME,account.getBankName());
        contentValues.put(Table.AccountsTable.BALANCE, account.getBalance());

        SQLiteDatabase db = expenseManageDBHelper.getWritableDatabase();
        db.insert(Table.AccountsTable.TABLE_NAME,null,contentValues);

    }

    @Override
    public void removeAccount(String accountNo){
        SQLiteDatabase db = expenseManageDBHelper.getWritableDatabase();
        db.delete(Table.AccountsTable.TABLE_NAME, Table.AccountsTable.ACCOUNT_NO + "=?", new String[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);

        switch (expenseType){
            case INCOME:
                account.setBalance(account.getBalance()+amount);
                break;
            case EXPENSE:
                account.setBalance(account.getBalance()-amount);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.AccountsTable.ACCOUNT_NO,account.getAccountNo());
        contentValues.put(Table.AccountsTable.ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        contentValues.put(Table.AccountsTable.BANK_NAME,account.getBankName());
        contentValues.put(Table.AccountsTable.BALANCE, account.getBalance());

        SQLiteDatabase db = expenseManageDBHelper.getWritableDatabase();
        db.update(Table.AccountsTable.TABLE_NAME, contentValues, Table.AccountsTable.ACCOUNT_NO+"=?", new String[]{accountNo});
    }
}
