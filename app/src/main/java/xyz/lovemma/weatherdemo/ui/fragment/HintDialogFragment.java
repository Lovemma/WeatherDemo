package xyz.lovemma.weatherdemo.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import xyz.lovemma.weatherdemo.MainActivity;
import xyz.lovemma.weatherdemo.R;

public class HintDialogFragment extends DialogFragment {

    public static final String TITLE = "title";
    public static final String MESSAGE = "message";

    public static HintDialogFragment newInstance(int title, int message) {
        HintDialogFragment frag = new HintDialogFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        args.putInt(MESSAGE, message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(TITLE);
        int message = getArguments().getInt(MESSAGE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity) getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity) getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();
    }


}
