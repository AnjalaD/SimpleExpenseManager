package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

public final class Table {
    public static final class AccountsTable{
        public static String TABLE_NAME = "accounts";
        public static String ACCOUNT_NO = "accountNo";
        public static String ACCOUNT_HOLDER_NAME = "accountHolderName";
        public static String BANK_NAME = "bankName";
        public static String BALANCE = "balance";
    }

    public static final class TransactionsTable{
        public static String TABLE_NAME = "transactions";
        public static String ACCOUNT_NO = "accountNo";
        public static String DATE = "date";
        public static String AMOUNT = "amount";
        public static String EXPENSE_TYPE="expenseType";
    }
}
