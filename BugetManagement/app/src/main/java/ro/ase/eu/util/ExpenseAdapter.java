package ro.ase.eu.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ro.ase.eu.bugetmanagement.R;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    private Context context;
    private int resource;
    private List<Expense> expenses;
    private LayoutInflater inflater;

    public ExpenseAdapter(@NonNull Context context,
                          int resource,
                          @NonNull List<Expense> objects,
                          LayoutInflater inflater) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.expenses = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        View row = inflater.inflate(resource, parent, false);

        TextView tvDate = row.findViewById(R.id.tv_lv_expenses_row_date);
        TextView tvCategory = row.findViewById(R.id.tv_lv_expenses_row_category);
        TextView tvAmount = row.findViewById(R.id.tv_lv_expenses_row_amount);

        Expense expense = expenses.get(position);

        tvDate.setText(expense.getDate() != null ?
                Constants.simpleDateFormat.format(expense.getDate())
                : context.getString(R.string.expense_adapter_no_date_msg));
        tvAmount.setText(expense.getAmount() != null ? expense.getAmount().toString()
                : context.getString(R.string.expense_adapter_no_amount_msg));
        tvCategory.setText(expense.getCategory());

        return row;
    }
}
