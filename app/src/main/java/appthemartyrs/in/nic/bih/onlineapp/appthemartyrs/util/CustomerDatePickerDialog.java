package appthemartyrs.in.nic.bih.onlineapp.appthemartyrs.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

class CustomerDatePickerDialog extends DatePickerDialog {

    public CustomerDatePickerDialog(Context context,
            OnDateSetListener callBack, int year, int monthOfYear,
            int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
//        mDialog.setTitle((month + 1) + "-" + day + "-");
    }
}