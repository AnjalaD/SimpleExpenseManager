package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.ExpenseManageDBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.Table;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private ExpenseManageDBHelper expenseManageDBHelper;

    public PersistentTransactionDAO(Context context) {
        this.expenseManageDBHelper = new ExpenseManageDBHelper(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db= expenseManageDBHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.TransactionsTable.DATE,date.getTime());
        contentValues.put(Table.TransactionsTable.ACCOUNT_NO,accountNo);
        contentValues.put(Table.TransactionsTable.EXPENSE_TYPE,expenseType.toString());
        contentValues.put(Table.TransactionsTable.AMOUNT,amount);

        db.insert(Table.TransactionsTable.TABLE_NAME,null,contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = expenseManageDBHelper.getReadableDatabase();
        Cursor results = db.rawQuery(
                "SELECT * FROM " + Table.TransactionsTable.TABLE_NAME,
                null
        );

        ArrayList<Transaction> transactions = new ArrayList<>();
        while (results.moveToNext()){
            Transaction transaction = new Transaction(
              new Date(results.getLong(results.getColumnIndex(Table.TransactionsTable.DATE))),
              results.getString(results.getColumnIndex(Table.TransactionsTable.ACCOUNT_NO)),
              results.getString(results.getColumnIndex(Table.TransactionsTable.EXPENSE_TYPE)).equals(ExpenseType.EXPENSE.toString())? ExpenseType.EXPENSE: ExpenseType.INCOME,
              results.getDouble(results.getColumnIndex(Table.TransactionsTable.AMOUNT))
            );
            transactions.add(transaction);
        }
        results.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = expenseManageDBHelper.getReadableDatabase();
        Cursor results = db.rawQuery(
                "SELECT * FROM " + Table.TransactionsTable.TABLE_NAME + " LIMIT ?",
                new String[]{String.valueOf(limit)}
        );

        ArrayList<Transaction> transactions = new ArrayList<>();
        while (results.moveToNext()){
            System.out.println(results.getString(results.getColumnIndex(Table.TransactionsTable.EXPENSE_TYPE)));
            Transaction transaction = new Transaction(
                    new Date(results.getLong(results.getColumnIndex(Table.TransactionsTable.DATE))),
                    results.getString(results.getColumnIndex(Table.TransactionsTable.ACCOUNT_NO)),
                    ExpenseType.valueOf(results.getString(results.getColumnIndex(Table.TransactionsTable.EXPENSE_TYPE))),
                    results.getDouble(results.getColumnIndex(Table.TransactionsTable.AMOUNT))
            );
            transactions.add(transaction);
        }
        results.close();
        return transactions;
    }
}
