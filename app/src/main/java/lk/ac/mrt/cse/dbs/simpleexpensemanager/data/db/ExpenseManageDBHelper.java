package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.DB_NAME;

public class ExpenseManageDBHelper extends SQLiteOpenHelper {

    private final String CREATE_ACCOUNTS_TABLE_STATEMENT = "CREATE TABLE "+ Table.AccountsTable.TABLE_NAME+"("
            + Table.AccountsTable.ACCOUNT_NO + " VARCHAR(20) PRIMARY KEY,"
            + Table.AccountsTable.ACCOUNT_HOLDER_NAME + " VARCHAR(50),"
            + Table.AccountsTable.BANK_NAME + " VARCHAR(50),"
            + Table.AccountsTable.BALANCE + " DOUBLE"
            +");";

    private final String CREATE_TRANSACTION_TABLE_STATEMENT = "CREATE TABLE "+ Table.TransactionsTable.TABLE_NAME+"("
            + Table.TransactionsTable.ACCOUNT_NO + " VARCHAR(20) ,"
            + Table.TransactionsTable.EXPENSE_TYPE + " VARCHAR(50),"
            + Table.TransactionsTable.DATE+ " VARCHAR(50),"
            + Table.TransactionsTable.AMOUNT + " DOUBLE,"
            + "FOREIGN KEY (" + Table.TransactionsTable.ACCOUNT_NO + ") REFERENCES " + Table.AccountsTable.TABLE_NAME+"(" + Table.AccountsTable.ACCOUNT_NO +")"
            +");";

    public ExpenseManageDBHelper(Context context) {
        super(context, DB_NAME,null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ACCOUNTS_TABLE_STATEMENT);
        sqLiteDatabase.execSQL(CREATE_TRANSACTION_TABLE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table.TransactionsTable.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table.AccountsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
